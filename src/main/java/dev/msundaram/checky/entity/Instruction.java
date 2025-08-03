package dev.msundaram.checky.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.msundaram.checky.listener.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "instructions")
@EntityListeners(TenantEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Instruction extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;
    
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;
    
    @Column(name = "instruction_text", nullable = false, columnDefinition = "TEXT")
    private String instructionText;
    
    @Column(name = "time_minutes")
    private Integer timeMinutes;
    
    @Column(name = "temperature_celsius")
    private Integer temperatureCelsius;
    
    @Column(name = "notes")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Recipe recipe;
} 