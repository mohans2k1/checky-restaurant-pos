package dev.msundaram.checky.security;

import dev.msundaram.checky.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantSecurityService {
    
    private final ApiKeyService apiKeyService;
    
    public String getCurrentTenant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
    
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
    
    public boolean isValidApiKey(String apiKey) {
        return apiKeyService.isValidApiKey(apiKey);
    }
    
    public Optional<Long> getRestaurantFromApiKey(String apiKey) {
        return apiKeyService.getRestaurantIdFromApiKey(apiKey);
    }
} 