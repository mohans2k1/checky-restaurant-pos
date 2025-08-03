package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.tenantId = :tenantId AND ri.recipeId = :recipeId ORDER BY ri.displayOrder")
    List<RecipeIngredient> findByTenantIdAndRecipeId(@Param("tenantId") Long tenantId, @Param("recipeId") Long recipeId);
    
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.tenantId = :tenantId AND ri.inventoryItemId = :inventoryItemId ORDER BY ri.recipeId")
    List<RecipeIngredient> findByTenantIdAndInventoryItemId(@Param("tenantId") Long tenantId, @Param("inventoryItemId") Long inventoryItemId);
    
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.tenantId = :tenantId AND ri.recipeId IN :recipeIds ORDER BY ri.recipeId, ri.displayOrder")
    List<RecipeIngredient> findByTenantIdAndRecipeIds(@Param("tenantId") Long tenantId, @Param("recipeIds") List<Long> recipeIds);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
    
    boolean existsByRecipeIdAndInventoryItemIdAndTenantId(Long recipeId, Long inventoryItemId, Long tenantId);
} 