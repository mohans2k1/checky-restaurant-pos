package dev.msundaram.checky.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.msundaram.checky.listener.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InventoryTransaction extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_number", nullable = false)
    private String transactionNumber;
    
    @Column(name = "inventory_item_id", nullable = false)
    private Long inventoryItemId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
    
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "previous_stock")
    private BigDecimal previousStock;
    
    @Column(name = "new_stock")
    private BigDecimal newStock;
    
    @Column(name = "reference_number")
    private String referenceNumber; // PO number, invoice number, etc.
    
    @Column(name = "reference_type")
    private String referenceType; // PURCHASE_ORDER, SALE, ADJUSTMENT, TRANSFER
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "batch_number")
    private String batchNumber;
    
    @Column(name = "location_from")
    private String locationFrom;
    
    @Column(name = "location_to")
    private String locationTo;
    
    @Column(name = "is_approved")
    private Boolean isApproved = true;
    
    @Column(name = "approved_by")
    private String approvedBy;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    public enum TransactionType {
        STOCK_IN, STOCK_OUT, ADJUSTMENT, TRANSFER, RETURN, DAMAGED, EXPIRED
    }
} 