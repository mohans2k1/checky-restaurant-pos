package dev.msundaram.checky.controller;

import dev.msundaram.checky.entity.Category;
import dev.msundaram.checky.entity.MenuItem;
import dev.msundaram.checky.repository.CategoryRepository;
import dev.msundaram.checky.repository.MenuItemRepository;
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

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu Management", description = "APIs for managing restaurant menu items and categories")
@SecurityRequirement(name = "ApiKeyAuth")
public class MenuController {
    
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final TenantService tenantService;
    
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieve all active categories for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Category>> getCategories() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        List<Category> categories = categoryRepository.findByTenantIdAndActive(currentRestaurant);
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping("/categories")
    @Operation(summary = "Create a new category", description = "Create a new menu category for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        tenantService.setRestaurantOnEntity(category);
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
    
    @GetMapping("/items")
    @Operation(summary = "Get all menu items", description = "Retrieve all available menu items for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        List<MenuItem> menuItems = menuItemRepository.findByTenantIdAndAvailable(currentRestaurant);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/items/category/{categoryId}")
    @Operation(summary = "Get menu items by category", description = "Retrieve all menu items for a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(
            @Parameter(description = "ID of the category") @PathVariable Long categoryId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        List<MenuItem> menuItems = menuItemRepository.findByTenantIdAndCategoryId(currentRestaurant, categoryId);
        return ResponseEntity.ok(menuItems);
    }
    
    @PostMapping("/items")
    @Operation(summary = "Create a new menu item", description = "Create a new menu item for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        tenantService.setRestaurantOnEntity(menuItem);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
    }
    
    @PutMapping("/items/{id}")
    @Operation(summary = "Update a menu item", description = "Update an existing menu item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key"),
        @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public ResponseEntity<MenuItem> updateMenuItem(
            @Parameter(description = "ID of the menu item") @PathVariable Long id, 
            @RequestBody MenuItem menuItem) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        if (!menuItemRepository.existsByIdAndTenantId(id, currentRestaurant)) {
            return ResponseEntity.notFound().build();
        }
        
        menuItem.setId(id);
        tenantService.setRestaurantOnEntity(menuItem);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return ResponseEntity.ok(updatedMenuItem);
    }
    
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete a menu item", description = "Delete a menu item (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key"),
        @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public ResponseEntity<Void> deleteMenuItem(
            @Parameter(description = "ID of the menu item") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        if (!menuItemRepository.existsByIdAndTenantId(id, currentRestaurant)) {
            return ResponseEntity.notFound().build();
        }
        
        // Soft delete - set active to false
        MenuItem menuItem = menuItemRepository.findById(id).orElse(null);
        if (menuItem != null) {
            menuItem.setIsActive(false); // Changed setActive to setIsActive
            menuItemRepository.save(menuItem);
        }
        
        return ResponseEntity.noContent().build();
    }
} 