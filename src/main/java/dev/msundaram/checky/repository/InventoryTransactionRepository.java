package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantId(@Param("tenantId") Long tenantId);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.inventoryItemId = :itemId ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantIdAndItemId(@Param("tenantId") Long tenantId, @Param("itemId") Long itemId);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.transactionType = :type ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantIdAndType(@Param("tenantId") Long tenantId, @Param("type") InventoryTransaction.TransactionType type);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantIdAndDateRange(@Param("tenantId") Long tenantId, 
                                                        @Param("startDate") LocalDateTime startDate, 
                                                        @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.referenceNumber = :referenceNumber ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantIdAndReferenceNumber(@Param("tenantId") Long tenantId, @Param("referenceNumber") String referenceNumber);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.transactionNumber = :transactionNumber")
    Optional<InventoryTransaction> findByTenantIdAndTransactionNumber(@Param("tenantId") Long tenantId, @Param("transactionNumber") String transactionNumber);
    
    @Query("SELECT SUM(t.quantity) FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.inventoryItemId = :itemId AND t.transactionType = 'STOCK_IN'")
    BigDecimal getTotalStockIn(@Param("tenantId") Long tenantId, @Param("itemId") Long itemId);
    
    @Query("SELECT SUM(t.quantity) FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.inventoryItemId = :itemId AND t.transactionType = 'STOCK_OUT'")
    BigDecimal getTotalStockOut(@Param("tenantId") Long tenantId, @Param("itemId") Long itemId);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.isApproved = false ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findPendingApprovals(@Param("tenantId") Long tenantId);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.locationFrom = :location OR t.locationTo = :location ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantIdAndLocation(@Param("tenantId") Long tenantId, @Param("location") String location);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.batchNumber = :batchNumber ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByTenantIdAndBatchNumber(@Param("tenantId") Long tenantId, @Param("batchNumber") String batchNumber);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND t.expiryDate <= :expiryDate ORDER BY t.expiryDate")
    List<InventoryTransaction> findExpiringTransactions(@Param("tenantId") Long tenantId, @Param("expiryDate") LocalDateTime expiryDate);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
    
    boolean existsByTransactionNumberAndTenantId(String transactionNumber, Long tenantId);
} 