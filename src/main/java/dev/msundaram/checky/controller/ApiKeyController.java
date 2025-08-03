package dev.msundaram.checky.controller;

import dev.msundaram.checky.entity.ApiKey;
import dev.msundaram.checky.service.ApiKeyService;
import dev.msundaram.checky.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/keys")
@RequiredArgsConstructor
@Tag(name = "API Key Management", description = "APIs for managing API keys for restaurant authentication")
@SecurityRequirement(name = "ApiKeyAuth")
public class ApiKeyController {
    
    private final ApiKeyService apiKeyService;
    private final TenantService tenantService;
    
    @PostMapping
    @Operation(summary = "Create a new API key", description = "Generate a new API key for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "API key created successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<ApiKey> createApiKey(@RequestBody Map<String, String> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long restaurantId = tenantService.getCurrentRestaurant();
        String description = request.get("description");
        
        ApiKey apiKey = apiKeyService.createApiKey(restaurantId, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKey);
    }
    
    @GetMapping
    @Operation(summary = "Get all API keys", description = "Retrieve all active API keys for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API keys retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<ApiKey>> getApiKeys() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long restaurantId = tenantService.getCurrentRestaurant();
        List<ApiKey> apiKeys = apiKeyService.findByRestaurantIdAndActive(restaurantId);
        return ResponseEntity.ok(apiKeys);
    }
    
    @DeleteMapping("/{apiKey}")
    @Operation(summary = "Deactivate an API key", description = "Deactivate an existing API key")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API key deactivated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key"),
        @ApiResponse(responseCode = "403", description = "Forbidden - API key does not belong to current restaurant"),
        @ApiResponse(responseCode = "404", description = "API key not found")
    })
    public ResponseEntity<Void> deactivateApiKey(
            @Parameter(description = "The API key to deactivate") @PathVariable String apiKey) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Verify the API key belongs to the current restaurant
        var apiKeyOpt = apiKeyService.findByApiKey(apiKey);
        if (apiKeyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ApiKey key = apiKeyOpt.get();
        if (!key.getRestaurantId().equals(tenantService.getCurrentRestaurant())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        apiKeyService.deactivateApiKey(apiKey);
        return ResponseEntity.ok().build();
    }
} 