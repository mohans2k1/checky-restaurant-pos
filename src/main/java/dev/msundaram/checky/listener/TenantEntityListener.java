package dev.msundaram.checky.listener;

import dev.msundaram.checky.config.MultiTenantConfig;
import dev.msundaram.checky.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TenantEntityListener {

    private static MultiTenantConfig.TenantContext tenantContext;

    @Autowired
    public void setTenantContext(MultiTenantConfig.TenantContext tenantContext) {
        TenantEntityListener.tenantContext = tenantContext;
    }

    @PrePersist
    public void setTenantId(BaseEntity entity) {
        if (entity.getTenantId() != null) {
            return;
        }
        if (tenantContext != null && tenantContext.getCurrentRestaurant() != null) {
            entity.setTenantId(tenantContext.getCurrentRestaurant().getId());
        }

    }
}