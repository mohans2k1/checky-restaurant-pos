package dev.msundaram.checky.service;

import dev.msundaram.checky.entity.ApiKey;
import dev.msundaram.checky.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    
    private final ApiKeyRepository apiKeyRepository;
    
    @Transactional
    public Optional<Long> getRestaurantIdFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Optional.empty();
        }
        
        Optional<ApiKey> apiKeyEntity = apiKeyRepository.findByApiKeyAndActive(apiKey, LocalDateTime.now());
        
        if (apiKeyEntity.isPresent()) {
            ApiKey key = apiKeyEntity.get();
            // Update last used timestamp
            apiKeyRepository.updateLastUsedAt(key.getId(), LocalDateTime.now());
            return Optional.of(key.getRestaurantId());
        }
        
        return Optional.empty();
    }
    
    public boolean isValidApiKey(String apiKey) {
        return apiKeyRepository.existsByApiKeyAndIsActiveTrue(apiKey);
    }
    
    public Optional<ApiKey> findByApiKey(String apiKey) {
        return apiKeyRepository.findByApiKey(apiKey);
    }
    
    public List<ApiKey> findByRestaurantIdAndActive(Long restaurantId) {
        return apiKeyRepository.findByRestaurantIdAndActive(restaurantId);
    }
    
    @Transactional
    public ApiKey createApiKey(Long restaurantId, String description) {
        String generatedApiKey = generateApiKey();
        
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKey(generatedApiKey);
        apiKey.setRestaurantId(restaurantId);
        apiKey.setDescription(description);
        apiKey.setIsActive(true);
        
        return apiKeyRepository.save(apiKey);
    }
    
    @Transactional
    public void deactivateApiKey(String apiKey) {
        Optional<ApiKey> existingKey = apiKeyRepository.findByApiKey(apiKey);
        if (existingKey.isPresent()) {
            ApiKey key = existingKey.get();
            key.setIsActive(false);
            apiKeyRepository.save(key);
        }
    }
    
    private String generateApiKey() {
        // Generate a secure API key (in production, use a more secure method)
        return "api_key_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
} 