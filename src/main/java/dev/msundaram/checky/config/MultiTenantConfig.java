package dev.msundaram.checky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import dev.msundaram.checky.entity.Restaurant;

@Configuration
public class MultiTenantConfig {

    @Bean
    @RequestScope
    public TenantContext tenantContext() {
        return new TenantContext();
    }

    public static class TenantContext {
        private Restaurant currentRestaurant;

        public Restaurant getCurrentRestaurant() {
            return currentRestaurant;
        }

        public void setCurrentRestaurant(Restaurant currentRestaurant) {
            this.currentRestaurant = currentRestaurant;
        }
    }
} 