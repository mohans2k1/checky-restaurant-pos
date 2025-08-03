package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    @Query("SELECT m FROM MenuItem m WHERE m.tenantId = :tenantId AND m.isAvailable = true ORDER BY m.displayOrder")
    List<MenuItem> findByTenantIdAndAvailable(@Param("tenantId") Long tenantId);
    
    @Query("SELECT m FROM MenuItem m WHERE m.tenantId = :tenantId ORDER BY m.displayOrder")
    List<MenuItem> findByTenantId(@Param("tenantId") Long tenantId);
    
    @Query("SELECT m FROM MenuItem m WHERE m.tenantId = :tenantId AND m.category.id = :categoryId AND m.isAvailable = true ORDER BY m.displayOrder")
    List<MenuItem> findByTenantIdAndCategoryId(@Param("tenantId") Long tenantId, @Param("categoryId") Long categoryId);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
} 