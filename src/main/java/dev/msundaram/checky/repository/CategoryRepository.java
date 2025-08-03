package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query("SELECT c FROM Category c WHERE c.tenantId = :tenantId AND c.isActive = true ORDER BY c.displayOrder")
    List<Category> findByTenantIdAndActive(@Param("tenantId") Long tenantId);
    
    @Query("SELECT c FROM Category c WHERE c.tenantId = :tenantId ORDER BY c.displayOrder")
    List<Category> findByTenantId(@Param("tenantId") Long tenantId);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
} 