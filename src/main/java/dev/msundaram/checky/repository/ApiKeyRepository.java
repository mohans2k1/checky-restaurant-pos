package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    
    @Query("SELECT ak FROM ApiKey ak WHERE ak.apiKey = :apiKey AND ak.isActive = true AND (ak.expiresAt IS NULL OR ak.expiresAt > :currentTime)")
    Optional<ApiKey> findByApiKeyAndActive(@Param("apiKey") String apiKey, @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT ak FROM ApiKey ak WHERE ak.apiKey = :apiKey")
    Optional<ApiKey> findByApiKey(@Param("apiKey") String apiKey);
    
    @Query("SELECT ak FROM ApiKey ak WHERE ak.restaurantId = :restaurantId AND ak.isActive = true")
    List<ApiKey> findByRestaurantIdAndActive(@Param("restaurantId") Long restaurantId);
    
    @Modifying
    @Query("UPDATE ApiKey ak SET ak.lastUsedAt = :lastUsedAt WHERE ak.id = :id")
    void updateLastUsedAt(@Param("id") Long id, @Param("lastUsedAt") LocalDateTime lastUsedAt);
    
    boolean existsByApiKeyAndIsActiveTrue(String apiKey);
} 