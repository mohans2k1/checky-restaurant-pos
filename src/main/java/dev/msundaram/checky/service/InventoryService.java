package dev.msundaram.checky.service;

import dev.msundaram.checky.entity.InventoryItem;
import dev.msundaram.checky.entity.InventoryTransaction;
import dev.msundaram.checky.repository.InventoryItemRepository;
import dev.msundaram.checky.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final TenantService tenantService;
    
    // Inventory Item Management
    public List<InventoryItem> getAllInventoryItems() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryItemRepository.findByTenantIdAndActive(currentRestaurant);
    }
    
    public List<InventoryItem> getInventoryItemsByCategory(InventoryItem.InventoryCategory category) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryItemRepository.findByTenantIdAndCategory(currentRestaurant, category);
    }
    
    public List<InventoryItem> getLowStockItems() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryItemRepository.findLowStockItems(currentRestaurant);
    }
    
    public List<InventoryItem> getOutOfStockItems() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryItemRepository.findOutOfStockItems(currentRestaurant);
    }
    
    public List<InventoryItem> getExpiringItems(int daysAhead) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(daysAhead);
        return inventoryItemRepository.findExpiringItems(currentRestaurant, expiryDate);
    }
    
    public List<InventoryItem> searchInventoryItems(String searchTerm) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryItemRepository.searchByTenantIdAndName(currentRestaurant, searchTerm);
    }
    
    public Optional<InventoryItem> getInventoryItemById(Long itemId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(itemId);
        if (itemOpt.isPresent() && itemOpt.get().getTenantId().equals(currentRestaurant)) {
            return itemOpt;
        }
        return Optional.empty();
    }
    
    public Optional<InventoryItem> getInventoryItemByCode(String itemCode) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryItemRepository.findByTenantIdAndItemCode(currentRestaurant, itemCode);
    }
    
    @Transactional
    public InventoryItem createInventoryItem(InventoryItem itemRequest) {
        // Set tenant context
        tenantService.setRestaurantOnEntity(itemRequest);
        
        // Check if item code already exists
        if (inventoryItemRepository.existsByItemCodeAndTenantId(itemRequest.getItemCode(), tenantService.getCurrentRestaurant())) {
            throw new RuntimeException("Item code already exists");
        }
        
        InventoryItem savedItem = inventoryItemRepository.save(itemRequest);
        log.info("Created inventory item {} for restaurant {}", savedItem.getItemCode(), tenantService.getCurrentRestaurant());
        return savedItem;
    }
    
    @Transactional
    public InventoryItem updateInventoryItem(Long itemId, InventoryItem itemRequest) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<InventoryItem> existingItemOpt = inventoryItemRepository.findById(itemId);
        if (existingItemOpt.isEmpty() || !existingItemOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Inventory item not found");
        }
        
        InventoryItem existingItem = existingItemOpt.get();
        
        // Update fields
        existingItem.setName(itemRequest.getName());
        existingItem.setDescription(itemRequest.getDescription());
        existingItem.setCategory(itemRequest.getCategory());
        existingItem.setUnit(itemRequest.getUnit());
        existingItem.setMinimumStock(itemRequest.getMinimumStock());
        existingItem.setReorderLevel(itemRequest.getReorderLevel());
        existingItem.setReorderQuantity(itemRequest.getReorderQuantity());
        existingItem.setUnitCost(itemRequest.getUnitCost());
        existingItem.setSupplierId(itemRequest.getSupplierId());
        existingItem.setSupplierName(itemRequest.getSupplierName());
        existingItem.setSupplierContact(itemRequest.getSupplierContact());
        existingItem.setIsPerishable(itemRequest.getIsPerishable());
        existingItem.setShelfLifeDays(itemRequest.getShelfLifeDays());
        existingItem.setLocation(itemRequest.getLocation());
        existingItem.setNotes(itemRequest.getNotes());
        
        InventoryItem updatedItem = inventoryItemRepository.save(existingItem);
        log.info("Updated inventory item {} for restaurant {}", updatedItem.getItemCode(), currentRestaurant);
        return updatedItem;
    }
    
    @Transactional
    public void deleteInventoryItem(Long itemId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(itemId);
        if (itemOpt.isEmpty() || !itemOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Inventory item not found");
        }
        
        InventoryItem item = itemOpt.get();
        item.setIsActive(false);
        inventoryItemRepository.save(item);
        
        log.info("Deactivated inventory item {} for restaurant {}", item.getItemCode(), currentRestaurant);
    }
    
    // Inventory Transaction Management
    public List<InventoryTransaction> getAllTransactions() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryTransactionRepository.findByTenantId(currentRestaurant);
    }
    
    public List<InventoryTransaction> getTransactionsByItem(Long itemId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryTransactionRepository.findByTenantIdAndItemId(currentRestaurant, itemId);
    }
    
    public List<InventoryTransaction> getTransactionsByType(InventoryTransaction.TransactionType type) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryTransactionRepository.findByTenantIdAndType(currentRestaurant, type);
    }
    
    public List<InventoryTransaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryTransactionRepository.findByTenantIdAndDateRange(currentRestaurant, startDate, endDate);
    }
    
    public Optional<InventoryTransaction> getTransactionByNumber(String transactionNumber) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryTransactionRepository.findByTenantIdAndTransactionNumber(currentRestaurant, transactionNumber);
    }
    
    @Transactional
    public InventoryTransaction createStockInTransaction(Long itemId, BigDecimal quantity, BigDecimal unitCost, 
                                                      String referenceNumber, String notes, LocalDateTime expiryDate) {
        return createTransaction(itemId, InventoryTransaction.TransactionType.STOCK_IN, quantity, unitCost, 
                              referenceNumber, notes, null, null, expiryDate, null);
    }
    
    @Transactional
    public InventoryTransaction createStockOutTransaction(Long itemId, BigDecimal quantity, String referenceNumber, 
                                                       String notes, String locationFrom) {
        return createTransaction(itemId, InventoryTransaction.TransactionType.STOCK_OUT, quantity, null, 
                              referenceNumber, notes, locationFrom, null, null, null);
    }
    
    @Transactional
    public InventoryTransaction createAdjustmentTransaction(Long itemId, BigDecimal quantity, String reason, String notes) {
        return createTransaction(itemId, InventoryTransaction.TransactionType.ADJUSTMENT, quantity, null, 
                              null, notes + " - " + reason, null, null, null, null);
    }
    
    @Transactional
    public InventoryTransaction createTransferTransaction(Long itemId, BigDecimal quantity, String locationFrom, 
                                                      String locationTo, String notes) {
        return createTransaction(itemId, InventoryTransaction.TransactionType.TRANSFER, quantity, null, 
                              null, notes, locationFrom, locationTo, null, null);
    }
    
    private InventoryTransaction createTransaction(Long itemId, InventoryTransaction.TransactionType type, 
                                                BigDecimal quantity, BigDecimal unitCost, String referenceNumber, 
                                                String notes, String locationFrom, String locationTo, 
                                                LocalDateTime expiryDate, String batchNumber) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        // Get inventory item
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(itemId);
        if (itemOpt.isEmpty() || !itemOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Inventory item not found");
        }
        
        InventoryItem item = itemOpt.get();
        BigDecimal previousStock = item.getCurrentStock();
        BigDecimal newStock = previousStock;
        
        // Calculate new stock based on transaction type
        switch (type) {
            case STOCK_IN:
                newStock = previousStock.add(quantity);
                break;
            case STOCK_OUT:
                if (previousStock.compareTo(quantity) < 0) {
                    throw new RuntimeException("Insufficient stock");
                }
                newStock = previousStock.subtract(quantity);
                break;
            case ADJUSTMENT:
                newStock = previousStock.add(quantity);
                break;
            case TRANSFER:
                // For transfers, we might need to handle differently
                break;
        }
        
        // Create transaction
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTenantId(currentRestaurant);
        transaction.setTransactionNumber(generateTransactionNumber());
        transaction.setInventoryItemId(itemId);
        transaction.setTransactionType(type);
        transaction.setQuantity(quantity);
        transaction.setUnitCost(unitCost);
        transaction.setTotalCost(unitCost != null ? unitCost.multiply(quantity) : null);
        transaction.setPreviousStock(previousStock);
        transaction.setNewStock(newStock);
        transaction.setReferenceNumber(referenceNumber);
        transaction.setNotes(notes);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setExpiryDate(expiryDate);
        transaction.setBatchNumber(batchNumber);
        transaction.setLocationFrom(locationFrom);
        transaction.setLocationTo(locationTo);
        transaction.setIsApproved(true);
        
        // Save transaction
        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(transaction);
        
        // Update inventory item stock
        item.setCurrentStock(newStock);
        if (type == InventoryTransaction.TransactionType.STOCK_IN) {
            item.setLastRestockedDate(LocalDateTime.now());
        }
        inventoryItemRepository.save(item);
        
        log.info("Created {} transaction for item {}: {} -> {}", type, item.getItemCode(), previousStock, newStock);
        return savedTransaction;
    }
    
    private String generateTransactionNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return String.format("INV-%d-%s", currentRestaurant, timestamp);
    }
    
    // Utility Methods
    public InventoryItem.InventoryCategory[] getInventoryCategories() {
        return InventoryItem.InventoryCategory.values();
    }
    
    public InventoryTransaction.TransactionType[] getTransactionTypes() {
        return InventoryTransaction.TransactionType.values();
    }
    
    public List<InventoryTransaction> getPendingApprovals() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return inventoryTransactionRepository.findPendingApprovals(currentRestaurant);
    }
} 