package dev.msundaram.checky.controller;

import dev.msundaram.checky.entity.*;
import dev.msundaram.checky.service.RecipeService;
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
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipe Management", description = "APIs for managing recipes, ingredients, and instructions")
@SecurityRequirement(name = "ApiKeyAuth")
public class RecipeController {
    
    private final RecipeService recipeService;
    private final TenantService tenantService;
    
    // Recipe Management
    
    @GetMapping
    @Operation(summary = "Get all recipes", description = "Retrieve all active recipes for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID", description = "Retrieve a specific recipe by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Recipe> getRecipeById(
            @Parameter(description = "ID of the recipe") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/menu-item/{menuItemId}")
    @Operation(summary = "Get recipe by menu item", description = "Retrieve recipe for a specific menu item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Recipe> getRecipeByMenuItem(
            @Parameter(description = "ID of the menu item") @PathVariable Long menuItemId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return recipeService.getRecipeByMenuItem(menuItemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cuisine/{cuisineType}")
    @Operation(summary = "Get recipes by cuisine type", description = "Retrieve recipes filtered by cuisine type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Recipe>> getRecipesByCuisineType(
            @Parameter(description = "Cuisine type to filter by") @PathVariable String cuisineType) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Recipe> recipes = recipeService.getRecipesByCuisineType(cuisineType);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/difficulty/{difficultyLevel}")
    @Operation(summary = "Get recipes by difficulty level", description = "Retrieve recipes filtered by difficulty level")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Recipe>> getRecipesByDifficultyLevel(
            @Parameter(description = "Difficulty level to filter by") @PathVariable String difficultyLevel) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Recipe> recipes = recipeService.getRecipesByDifficultyLevel(difficultyLevel);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/vegetarian")
    @Operation(summary = "Get vegetarian recipes", description = "Retrieve all vegetarian recipes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vegetarian recipes retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Recipe>> getVegetarianRecipes() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Recipe> recipes = recipeService.getVegetarianRecipes();
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/gluten-free")
    @Operation(summary = "Get gluten-free recipes", description = "Retrieve all gluten-free recipes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gluten-free recipes retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Recipe>> getGlutenFreeRecipes() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Recipe> recipes = recipeService.getGlutenFreeRecipes();
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search recipes", description = "Search recipes by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Recipe>> searchRecipes(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Recipe> recipes = recipeService.searchRecipes(searchTerm);
        return ResponseEntity.ok(recipes);
    }
    
    @PostMapping
    @Operation(summary = "Create a new recipe", description = "Create a new recipe for a menu item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recipe created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid recipe data or recipe already exists for menu item"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipeRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Recipe createdRecipe = recipeService.createRecipe(recipeRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a recipe", description = "Update an existing recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe updated successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Recipe> updateRecipe(
            @Parameter(description = "ID of the recipe") @PathVariable Long id,
            @RequestBody Recipe recipeRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Recipe updatedRecipe = recipeService.updateRecipe(id, recipeRequest);
            return ResponseEntity.ok(updatedRecipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recipe", description = "Deactivate a recipe (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recipe deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(description = "ID of the recipe") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Recipe Ingredients Management
    
    @GetMapping("/{recipeId}/ingredients")
    @Operation(summary = "Get recipe ingredients", description = "Retrieve all ingredients for a specific recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingredients retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RecipeIngredient>> getRecipeIngredients(
            @Parameter(description = "ID of the recipe") @PathVariable Long recipeId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<RecipeIngredient> ingredients = recipeService.getRecipeIngredients(recipeId);
        return ResponseEntity.ok(ingredients);
    }
    
    @PostMapping("/{recipeId}/ingredients")
    @Operation(summary = "Add ingredient to recipe", description = "Add a new ingredient to a recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ingredient added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid ingredient data or ingredient already exists"),
        @ApiResponse(responseCode = "404", description = "Recipe or inventory item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RecipeIngredient> addIngredientToRecipe(
            @Parameter(description = "ID of the recipe") @PathVariable Long recipeId,
            @RequestBody RecipeIngredient ingredientRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            RecipeIngredient savedIngredient = recipeService.addIngredientToRecipe(recipeId, ingredientRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    @Operation(summary = "Remove ingredient from recipe", description = "Remove an ingredient from a recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ingredient removed successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe or ingredient not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Void> removeIngredientFromRecipe(
            @Parameter(description = "ID of the recipe") @PathVariable Long recipeId,
            @Parameter(description = "ID of the ingredient") @PathVariable Long ingredientId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            recipeService.removeIngredientFromRecipe(recipeId, ingredientId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Recipe Instructions Management
    
    @GetMapping("/{recipeId}/instructions")
    @Operation(summary = "Get recipe instructions", description = "Retrieve all instructions for a specific recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<Instruction>> getRecipeInstructions(
            @Parameter(description = "ID of the recipe") @PathVariable Long recipeId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Instruction> instructions = recipeService.getRecipeInstructions(recipeId);
        return ResponseEntity.ok(instructions);
    }
    
    @PostMapping("/{recipeId}/instructions")
    @Operation(summary = "Add instruction to recipe", description = "Add a new instruction to a recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Instruction added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid instruction data"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Instruction> addInstructionToRecipe(
            @Parameter(description = "ID of the recipe") @PathVariable Long recipeId,
            @RequestBody Instruction instructionRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Instruction savedInstruction = recipeService.addInstructionToRecipe(recipeId, instructionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInstruction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{recipeId}/instructions/{instructionId}")
    @Operation(summary = "Remove instruction from recipe", description = "Remove an instruction from a recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Instruction removed successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe or instruction not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Void> removeInstructionFromRecipe(
            @Parameter(description = "ID of the recipe") @PathVariable Long recipeId,
            @Parameter(description = "ID of the instruction") @PathVariable Long instructionId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            recipeService.removeInstructionFromRecipe(recipeId, instructionId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Inventory Tracking
    
    @PostMapping("/track-inventory")
    @Operation(summary = "Track inventory for order item", description = "Track inventory consumption for a menu item order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory tracked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Void> trackInventoryForOrderItem(@RequestBody Map<String, Object> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long menuItemId = Long.valueOf(request.get("menuItemId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            recipeService.trackInventoryForOrderItem(menuItemId, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Utility Endpoints
    
    @GetMapping("/cuisine-types")
    @Operation(summary = "Get cuisine types", description = "Get list of all available cuisine types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuisine types retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<String>> getCuisineTypes() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(recipeService.getCuisineTypes());
    }
    
    @GetMapping("/difficulty-levels")
    @Operation(summary = "Get difficulty levels", description = "Get list of all available difficulty levels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Difficulty levels retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<String>> getDifficultyLevels() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(recipeService.getDifficultyLevels());
    }
} 