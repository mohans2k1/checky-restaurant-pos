package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    
    @Query("SELECT i FROM Instruction i WHERE i.tenantId = :tenantId AND i.recipeId = :recipeId ORDER BY i.stepNumber")
    List<Instruction> findByTenantIdAndRecipeId(@Param("tenantId") Long tenantId, @Param("recipeId") Long recipeId);
    
    @Query("SELECT i FROM Instruction i WHERE i.tenantId = :tenantId AND i.recipeId IN :recipeIds ORDER BY i.recipeId, i.stepNumber")
    List<Instruction> findByTenantIdAndRecipeIds(@Param("tenantId") Long tenantId, @Param("recipeIds") List<Long> recipeIds);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
} 