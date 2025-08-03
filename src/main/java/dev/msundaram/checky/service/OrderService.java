package dev.msundaram.checky.service;

import dev.msundaram.checky.entity.*;
import dev.msundaram.checky.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final TenantService tenantService;
    private final RecipeService recipeService;
    
    public List<Order> getAllOrders() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return orderRepository.findByTenantId(currentRestaurant);
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return orderRepository.findByTenantIdAndStatus(currentRestaurant, status);
    }
    
    public Optional<Order> getOrderById(Long orderId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent() && orderOpt.get().getTenantId().equals(currentRestaurant)) {
            return orderOpt;
        }
        return Optional.empty();
    }
    
    public Optional<Order> getOrderByNumber(String orderNumber) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return orderRepository.findByTenantIdAndOrderNumber(currentRestaurant, orderNumber);
    }
    
    @Transactional
    public Order createOrder(Order orderRequest) {
        // Set tenant context
        tenantService.setRestaurantOnEntity(orderRequest);
        
        // Generate order number
        orderRequest.setOrderNumber(generateOrderNumber());
        
        // Calculate order totals
        calculateOrderTotals(orderRequest);
        
        // Save order
        Order savedOrder = orderRepository.save(orderRequest);
        
        // Track inventory for each order item
        if (savedOrder.getOrderItems() != null) {
            for (OrderItem orderItem : savedOrder.getOrderItems()) {
                if (orderItem.getMenuItem() != null && orderItem.getMenuItem().getId() != null) {
                    try {
                        recipeService.trackInventoryForOrderItem(orderItem.getMenuItem().getId(), orderItem.getQuantity());
                    } catch (Exception e) {
                        log.warn("Failed to track inventory for menu item {}: {}", 
                                orderItem.getMenuItem().getId(), e.getMessage());
                    }
                }
            }
        }
        
        log.info("Created order {} for restaurant {}", savedOrder.getOrderNumber(), tenantService.getCurrentRestaurant());
        return savedOrder;
    }
    
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty() || !orderOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Order not found");
        }
        
        Order order = orderOpt.get();
        order.setOrderStatus(newStatus);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order {} status to {} for restaurant {}", 
                updatedOrder.getOrderNumber(), newStatus, currentRestaurant);
        return updatedOrder;
    }
    
    @Transactional
    public Order updatePaymentStatus(Long orderId, Order.PaymentStatus newStatus) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty() || !orderOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Order not found");
        }
        
        Order order = orderOpt.get();
        order.setPaymentStatus(newStatus);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order {} payment status to {} for restaurant {}", 
                updatedOrder.getOrderNumber(), newStatus, currentRestaurant);
        return updatedOrder;
    }
    
    @Transactional
    public Order cancelOrder(Long orderId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty() || !orderOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Order not found");
        }
        
        Order order = orderOpt.get();
        order.setOrderStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Cancelled order {} for restaurant {}", order.getOrderNumber(), currentRestaurant);
        
        return order;
    }
    
    private void calculateOrderTotals(Order order) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        if (order.getOrderItems() != null) {
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getTotalPrice() != null) {
                    subtotal = subtotal.add(orderItem.getTotalPrice());
                }
            }
        }
        
        order.setSubtotal(subtotal);
        
        // Get restaurant for tax and service charge rates
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(order.getTenantId());
        if (restaurantOpt.isPresent()) {
            Restaurant restaurant = restaurantOpt.get();
            
            // Calculate tax
            BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(restaurant.getTaxRate()))
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            order.setTaxAmount(taxAmount);
            
            // Calculate service charge
            BigDecimal serviceCharge = subtotal.multiply(BigDecimal.valueOf(restaurant.getServiceChargeRate()))
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            order.setServiceCharge(serviceCharge);
            
            // Calculate total
            BigDecimal total = subtotal.add(taxAmount).add(serviceCharge);
            if (order.getDiscountAmount() != null) {
                total = total.subtract(order.getDiscountAmount());
            }
            order.setTotalAmount(total);
        }
    }
    
    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return String.format("ORD-%d-%s", currentRestaurant, timestamp);
    }
    
    public Order.OrderStatus[] getOrderStatuses() {
        return Order.OrderStatus.values();
    }
    
    public Order.PaymentStatus[] getPaymentStatuses() {
        return Order.PaymentStatus.values();
    }
} 