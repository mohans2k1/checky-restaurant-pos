package dev.msundaram.checky.entity;

import dev.msundaram.checky.listener.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "restaurants")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column
    private String address;
    
    @Column
    private String phone;
    
    @Column
    private String email;
    
    @Column(name = "tax_rate")
    private Double taxRate = 0.0;
    
    @Column(name = "service_charge_rate")
    private Double serviceChargeRate = 0.0;
    
    @Column(name = "currency_code")
    private String currencyCode = "USD";
    
    @Column(name = "timezone")
    private String timezone = "UTC";
    
    @Column(name = "is_active")
    private Boolean isActive = true;
} 