package dev.msundaram.checky.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.msundaram.checky.listener.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "restaurant_tables")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RestaurantTable extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "table_number", nullable = false)
    private String tableNumber;
    
    @Column(name = "table_name")
    private String tableName;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "table_status")
    private TableStatus tableStatus = TableStatus.AVAILABLE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "table_type")
    private TableType tableType = TableType.INDOOR;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_reservable")
    private Boolean isReservable = true;
    
    @Column(name = "notes")
    private String notes;
    
    public enum TableStatus {
        AVAILABLE, OCCUPIED, RESERVED, CLEANING, OUT_OF_SERVICE
    }
    
    public enum TableType {
        INDOOR, OUTDOOR, BAR, BOOTH, PRIVATE_ROOM
    }
} 