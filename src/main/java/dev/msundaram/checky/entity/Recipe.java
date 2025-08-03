package dev.msundaram.checky.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.msundaram.checky.listener.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recipes")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recipe extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "menu_item_id", nullable = false)
    private Long menuItemId;
    
    @Column(name = "serving_size")
    private Integer servingSize = 1;
    
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes;
    
    @Column(name = "cooking_time_minutes")
    private Integer cookingTimeMinutes;
    
    @Column(name = "difficulty_level")
    private String difficultyLevel; // EASY, MEDIUM, HARD
    
    @Column(name = "cuisine_type")
    private String cuisineType; // ITALIAN, AMERICAN, JAPANESE, etc.
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_vegetarian")
    private Boolean isVegetarian = false;
    
    @Column(name = "is_gluten_free")
    private Boolean isGlutenFree = false;
    
    @Column(name = "is_spicy")
    private Boolean isSpicy = false;
    
    @Column(name = "notes")
    private String notes;
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("recipe")
    private List<RecipeIngredient> ingredients;
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("recipe")
    private List<Instruction> instructions;
} 