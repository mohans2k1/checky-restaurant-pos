package dev.msundaram.checky.service;

import dev.msundaram.checky.entity.BaseEntity;
import dev.msundaram.checky.security.TenantSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    
    @Autowired
    private TenantSecurityService tenantSecurityService;
    
    public Long getCurrentRestaurant() {
        String tenantId = tenantSecurityService.getCurrentTenant();
        return tenantId != null ? Long.valueOf(tenantId) : null;
    }
    
    public void setRestaurantOnEntity(BaseEntity entity) {
        entity.setTenantId(getCurrentRestaurant());
    }
    
    public boolean isCurrentRestaurant(Long restaurantId) {
        Long currentRestaurant = getCurrentRestaurant();
        return currentRestaurant != null && currentRestaurant.equals(restaurantId);
    }
    
    public boolean isAuthenticated() {
        return tenantSecurityService.isAuthenticated();
    }
    
    // Legacy method for backward compatibility
    public String getCurrentTenant() {
        Long restaurantId = getCurrentRestaurant();
        return restaurantId != null ? restaurantId.toString() : null;
    }
    
    // Legacy method for backward compatibility
    public void setTenantOnEntity(BaseEntity entity) {
        setRestaurantOnEntity(entity);
    }
} 