package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    Optional<Restaurant> findByTenantId(Long tenantId);
    
    boolean existsByTenantId(Long tenantId);
} 