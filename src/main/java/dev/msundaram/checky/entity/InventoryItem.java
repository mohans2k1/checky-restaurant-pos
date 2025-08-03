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
@Table(name = "inventory_items")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InventoryItem extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_code", nullable = false, unique = true)
    private String itemCode;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private InventoryCategory category = InventoryCategory.INGREDIENT;
    
    @Column(name = "unit", nullable = false)
    private String unit; // kg, liters, pieces, etc.
    
    @Column(name = "current_stock", nullable = false)
    private BigDecimal currentStock = BigDecimal.ZERO;
    
    @Column(name = "minimum_stock", nullable = false)
    private BigDecimal minimumStock = BigDecimal.ZERO;
    
    @Column(name = "reorder_level", nullable = false)
    private BigDecimal reorderLevel = BigDecimal.ZERO;
    
    @Column(name = "reorder_quantity", nullable = false)
    private BigDecimal reorderQuantity = BigDecimal.ZERO;
    
    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost = BigDecimal.ZERO;
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "supplier_contact")
    private String supplierContact;
    
    @Column(name = "last_restocked_date")
    private LocalDateTime lastRestockedDate;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_perishable")
    private Boolean isPerishable = false;
    
    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;
    
    @Column(name = "location")
    private String location; // Storage location
    
    @Column(name = "notes")
    private String notes;
    
    public enum InventoryCategory {
        INGREDIENT, PACKAGING, EQUIPMENT, CLEANING_SUPPLY, OFFICE_SUPPLY, OTHER
    }
} 