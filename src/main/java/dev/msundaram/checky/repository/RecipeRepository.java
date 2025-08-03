package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.isActive = true ORDER BY r.name")
    List<Recipe> findByTenantIdAndActive(@Param("tenantId") Long tenantId);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.menuItemId = :menuItemId AND r.isActive = true")
    Optional<Recipe> findByTenantIdAndMenuItemId(@Param("tenantId") Long tenantId, @Param("menuItemId") Long menuItemId);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.cuisineType = :cuisineType AND r.isActive = true ORDER BY r.name")
    List<Recipe> findByTenantIdAndCuisineType(@Param("tenantId") Long tenantId, @Param("cuisineType") String cuisineType);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.difficultyLevel = :difficultyLevel AND r.isActive = true ORDER BY r.name")
    List<Recipe> findByTenantIdAndDifficultyLevel(@Param("tenantId") Long tenantId, @Param("difficultyLevel") String difficultyLevel);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.isVegetarian = true AND r.isActive = true ORDER BY r.name")
    List<Recipe> findVegetarianRecipes(@Param("tenantId") Long tenantId);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.isGlutenFree = true AND r.isActive = true ORDER BY r.name")
    List<Recipe> findGlutenFreeRecipes(@Param("tenantId") Long tenantId);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.name LIKE %:searchTerm% AND r.isActive = true ORDER BY r.name")
    List<Recipe> searchByTenantIdAndName(@Param("tenantId") Long tenantId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT r FROM Recipe r WHERE r.tenantId = :tenantId AND r.preparationTimeMinutes <= :maxTime AND r.isActive = true ORDER BY r.preparationTimeMinutes")
    List<Recipe> findByTenantIdAndMaxPreparationTime(@Param("tenantId") Long tenantId, @Param("maxTime") Integer maxTime);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
    
    boolean existsByMenuItemIdAndTenantId(Long menuItemId, Long tenantId);
} 