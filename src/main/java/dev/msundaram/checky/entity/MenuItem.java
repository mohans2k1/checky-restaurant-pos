package dev.msundaram.checky.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.msundaram.checky.listener.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItem extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "is_vegetarian")
    private Boolean isVegetarian = false;
    
    @Column(name = "is_gluten_free")
    private Boolean isGlutenFree = false;
    
    @Column(name = "is_spicy")
    private Boolean isSpicy = false;
    
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes = 15;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;
} 