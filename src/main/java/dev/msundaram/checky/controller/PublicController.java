package dev.msundaram.checky.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public APIs", description = "Public endpoints that don't require authentication")
public class PublicController {
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check the health status of the application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application is healthy")
    })
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Checky POS System");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/info")
    @Operation(summary = "Application info", description = "Get information about the application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application information retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Checky Multi-Tenant POS");
        response.put("version", "1.0.0");
        response.put("description", "A modern, scalable Point of Sale system for multi-tenant restaurant management");
        response.put("authentication", "API Key based authentication required for protected endpoints");
        return ResponseEntity.ok(response);
    }
} 