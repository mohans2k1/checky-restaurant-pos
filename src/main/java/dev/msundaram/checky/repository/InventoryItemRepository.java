package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.isActive = true ORDER BY i.name")
    List<InventoryItem> findByTenantIdAndActive(@Param("tenantId") Long tenantId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.category = :category AND i.isActive = true ORDER BY i.name")
    List<InventoryItem> findByTenantIdAndCategory(@Param("tenantId") Long tenantId, @Param("category") InventoryItem.InventoryCategory category);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.currentStock <= i.reorderLevel AND i.isActive = true ORDER BY i.currentStock")
    List<InventoryItem> findLowStockItems(@Param("tenantId") Long tenantId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.currentStock <= i.minimumStock AND i.isActive = true ORDER BY i.currentStock")
    List<InventoryItem> findOutOfStockItems(@Param("tenantId") Long tenantId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.isPerishable = true AND i.expiryDate <= :expiryDate AND i.isActive = true ORDER BY i.expiryDate")
    List<InventoryItem> findExpiringItems(@Param("tenantId") Long tenantId, @Param("expiryDate") LocalDateTime expiryDate);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.supplierId = :supplierId AND i.isActive = true ORDER BY i.name")
    List<InventoryItem> findByTenantIdAndSupplier(@Param("tenantId") Long tenantId, @Param("supplierId") Long supplierId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.itemCode = :itemCode")
    Optional<InventoryItem> findByTenantIdAndItemCode(@Param("tenantId") Long tenantId, @Param("itemCode") String itemCode);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.name LIKE %:searchTerm% AND i.isActive = true ORDER BY i.name")
    List<InventoryItem> searchByTenantIdAndName(@Param("tenantId") Long tenantId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.currentStock > :minStock AND i.isActive = true ORDER BY i.currentStock DESC")
    List<InventoryItem> findByTenantIdAndStockGreaterThan(@Param("tenantId") Long tenantId, @Param("minStock") BigDecimal minStock);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.unitCost <= :maxCost AND i.isActive = true ORDER BY i.unitCost")
    List<InventoryItem> findByTenantIdAndCostLessThan(@Param("tenantId") Long tenantId, @Param("maxCost") BigDecimal maxCost);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.tenantId = :tenantId AND i.location = :location AND i.isActive = true ORDER BY i.name")
    List<InventoryItem> findByTenantIdAndLocation(@Param("tenantId") Long tenantId, @Param("location") String location);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
    
    boolean existsByItemCodeAndTenantId(String itemCode, Long tenantId);
} 