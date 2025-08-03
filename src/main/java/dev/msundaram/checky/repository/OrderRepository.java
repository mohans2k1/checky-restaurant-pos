package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE o.tenantId = :tenantId ORDER BY o.createdAt DESC")
    List<Order> findByTenantId(@Param("tenantId") Long tenantId);
    
    @Query("SELECT o FROM Order o WHERE o.tenantId = :tenantId AND o.orderStatus = :status ORDER BY o.createdAt DESC")
    List<Order> findByTenantIdAndStatus(@Param("tenantId") Long tenantId, @Param("status") Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.tenantId = :tenantId AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByTenantIdAndDateRange(@Param("tenantId") Long tenantId, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.tenantId = :tenantId AND o.orderNumber = :orderNumber")
    Optional<Order> findByTenantIdAndOrderNumber(@Param("tenantId") Long tenantId, @Param("orderNumber") String orderNumber);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
} 