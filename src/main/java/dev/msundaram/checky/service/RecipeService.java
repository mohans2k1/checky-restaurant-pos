package dev.msundaram.checky.service;

import dev.msundaram.checky.entity.*;
import dev.msundaram.checky.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final InstructionRepository instructionRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final MenuItemRepository menuItemRepository;
    private final TenantService tenantService;
    
    // Recipe Management
    public List<Recipe> getAllRecipes() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.findByTenantIdAndActive(currentRestaurant);
    }
    
    public Optional<Recipe> getRecipeById(Long recipeId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isPresent() && recipeOpt.get().getTenantId().equals(currentRestaurant)) {
            return recipeOpt;
        }
        return Optional.empty();
    }
    
    public Optional<Recipe> getRecipeByMenuItem(Long menuItemId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.findByTenantIdAndMenuItemId(currentRestaurant, menuItemId);
    }
    
    public List<Recipe> getRecipesByCuisineType(String cuisineType) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.findByTenantIdAndCuisineType(currentRestaurant, cuisineType);
    }
    
    public List<Recipe> getRecipesByDifficultyLevel(String difficultyLevel) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.findByTenantIdAndDifficultyLevel(currentRestaurant, difficultyLevel);
    }
    
    public List<Recipe> getVegetarianRecipes() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.findVegetarianRecipes(currentRestaurant);
    }
    
    public List<Recipe> getGlutenFreeRecipes() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.findGlutenFreeRecipes(currentRestaurant);
    }
    
    public List<Recipe> searchRecipes(String searchTerm) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeRepository.searchByTenantIdAndName(currentRestaurant, searchTerm);
    }
    
    @Transactional
    public Recipe createRecipe(Recipe recipeRequest) {
        // Set tenant context
        tenantService.setRestaurantOnEntity(recipeRequest);
        
        // Check if recipe already exists for this menu item
        if (recipeRepository.existsByMenuItemIdAndTenantId(recipeRequest.getMenuItemId(), tenantService.getCurrentRestaurant())) {
            throw new RuntimeException("Recipe already exists for this menu item");
        }
        
        Recipe savedRecipe = recipeRepository.save(recipeRequest);
        log.info("Created recipe {} for restaurant {}", savedRecipe.getName(), tenantService.getCurrentRestaurant());
        return savedRecipe;
    }
    
    @Transactional
    public Recipe updateRecipe(Long recipeId, Recipe recipeRequest) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(recipeId);
        if (existingRecipeOpt.isEmpty() || !existingRecipeOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Recipe not found");
        }
        
        Recipe existingRecipe = existingRecipeOpt.get();
        
        // Update fields
        existingRecipe.setName(recipeRequest.getName());
        existingRecipe.setDescription(recipeRequest.getDescription());
        existingRecipe.setServingSize(recipeRequest.getServingSize());
        existingRecipe.setPreparationTimeMinutes(recipeRequest.getPreparationTimeMinutes());
        existingRecipe.setCookingTimeMinutes(recipeRequest.getCookingTimeMinutes());
        existingRecipe.setDifficultyLevel(recipeRequest.getDifficultyLevel());
        existingRecipe.setCuisineType(recipeRequest.getCuisineType());
        existingRecipe.setIsVegetarian(recipeRequest.getIsVegetarian());
        existingRecipe.setIsGlutenFree(recipeRequest.getIsGlutenFree());
        existingRecipe.setIsSpicy(recipeRequest.getIsSpicy());
        existingRecipe.setNotes(recipeRequest.getNotes());
        
        Recipe updatedRecipe = recipeRepository.save(existingRecipe);
        log.info("Updated recipe {} for restaurant {}", updatedRecipe.getName(), currentRestaurant);
        return updatedRecipe;
    }
    
    @Transactional
    public void deleteRecipe(Long recipeId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isEmpty() || !recipeOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Recipe not found");
        }
        
        Recipe recipe = recipeOpt.get();
        recipe.setIsActive(false);
        recipeRepository.save(recipe);
        
        log.info("Deactivated recipe {} for restaurant {}", recipe.getName(), currentRestaurant);
    }
    
    // Recipe Ingredients Management
    public List<RecipeIngredient> getRecipeIngredients(Long recipeId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return recipeIngredientRepository.findByTenantIdAndRecipeId(currentRestaurant, recipeId);
    }
    
    @Transactional
    public RecipeIngredient addIngredientToRecipe(Long recipeId, RecipeIngredient ingredientRequest) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        // Verify recipe exists and belongs to current tenant
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isEmpty() || !recipeOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Recipe not found");
        }
        
        // Verify inventory item exists and belongs to current tenant
        Optional<InventoryItem> inventoryItemOpt = inventoryItemRepository.findById(ingredientRequest.getInventoryItemId());
        if (inventoryItemOpt.isEmpty() || !inventoryItemOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Inventory item not found");
        }
        
        // Check if ingredient already exists for this recipe
        if (recipeIngredientRepository.existsByRecipeIdAndInventoryItemIdAndTenantId(recipeId, ingredientRequest.getInventoryItemId(), currentRestaurant)) {
            throw new RuntimeException("Ingredient already exists for this recipe");
        }
        
        ingredientRequest.setRecipeId(recipeId);
        tenantService.setRestaurantOnEntity(ingredientRequest);
        
        RecipeIngredient savedIngredient = recipeIngredientRepository.save(ingredientRequest);
        log.info("Added ingredient {} to recipe {} for restaurant {}", 
                inventoryItemOpt.get().getName(), recipeOpt.get().getName(), currentRestaurant);
        return savedIngredient;
    }
    
    @Transactional
    public void removeIngredientFromRecipe(Long recipeId, Long ingredientId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<RecipeIngredient> ingredientOpt = recipeIngredientRepository.findById(ingredientId);
        if (ingredientOpt.isEmpty() || !ingredientOpt.get().getTenantId().equals(currentRestaurant) || 
            !ingredientOpt.get().getRecipeId().equals(recipeId)) {
            throw new RuntimeException("Recipe ingredient not found");
        }
        
        recipeIngredientRepository.delete(ingredientOpt.get());
        log.info("Removed ingredient from recipe {} for restaurant {}", recipeId, currentRestaurant);
    }
    
    // Recipe Instructions Management
    public List<Instruction> getRecipeInstructions(Long recipeId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return instructionRepository.findByTenantIdAndRecipeId(currentRestaurant, recipeId);
    }
    
    @Transactional
    public Instruction addInstructionToRecipe(Long recipeId, Instruction instructionRequest) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        // Verify recipe exists and belongs to current tenant
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isEmpty() || !recipeOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Recipe not found");
        }
        
        instructionRequest.setRecipeId(recipeId);
        tenantService.setRestaurantOnEntity(instructionRequest);
        
        Instruction savedInstruction = instructionRepository.save(instructionRequest);
        log.info("Added instruction to recipe {} for restaurant {}", recipeOpt.get().getName(), currentRestaurant);
        return savedInstruction;
    }
    
    @Transactional
    public void removeInstructionFromRecipe(Long recipeId, Long instructionId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<Instruction> instructionOpt = instructionRepository.findById(instructionId);
        if (instructionOpt.isEmpty() || !instructionOpt.get().getTenantId().equals(currentRestaurant) || 
            !instructionOpt.get().getRecipeId().equals(recipeId)) {
            throw new RuntimeException("Recipe instruction not found");
        }
        
        instructionRepository.delete(instructionOpt.get());
        log.info("Removed instruction from recipe {} for restaurant {}", recipeId, currentRestaurant);
    }
    
    // Inventory Tracking Based on Orders
    @Transactional
    public void trackInventoryForOrderItem(Long menuItemId, Integer quantity) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        // Get recipe for menu item
        Optional<Recipe> recipeOpt = recipeRepository.findByTenantIdAndMenuItemId(currentRestaurant, menuItemId);
        if (recipeOpt.isEmpty()) {
            log.warn("No recipe found for menu item {}, skipping inventory tracking", menuItemId);
            return;
        }
        
        Recipe recipe = recipeOpt.get();
        
        // Get recipe ingredients
        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByTenantIdAndRecipeId(currentRestaurant, recipe.getId());
        
        // Calculate and deduct inventory for each ingredient
        for (RecipeIngredient ingredient : ingredients) {
            BigDecimal totalQuantityNeeded = ingredient.getQuantity().multiply(BigDecimal.valueOf(quantity));
            
            // Get inventory item
            Optional<InventoryItem> inventoryItemOpt = inventoryItemRepository.findById(ingredient.getInventoryItemId());
            if (inventoryItemOpt.isEmpty()) {
                log.warn("Inventory item {} not found for recipe ingredient", ingredient.getInventoryItemId());
                continue;
            }
            
            InventoryItem inventoryItem = inventoryItemOpt.get();
            
            // Check if sufficient stock is available
            if (inventoryItem.getCurrentStock().compareTo(totalQuantityNeeded) < 0) {
                log.warn("Insufficient stock for {}: required {}, available {}", 
                        inventoryItem.getName(), totalQuantityNeeded, inventoryItem.getCurrentStock());
                continue;
            }
            
            // Create stock-out transaction
            try {
                inventoryTransactionRepository.save(createStockOutTransaction(inventoryItem, totalQuantityNeeded, 
                        "Order consumption for " + recipe.getName()));
                
                // Update inventory item stock
                inventoryItem.setCurrentStock(inventoryItem.getCurrentStock().subtract(totalQuantityNeeded));
                inventoryItemRepository.save(inventoryItem);
                
                log.info("Deducted {} {} of {} for recipe {}", totalQuantityNeeded, ingredient.getUnit(), 
                        inventoryItem.getName(), recipe.getName());
            } catch (Exception e) {
                log.error("Error tracking inventory for {}: {}", inventoryItem.getName(), e.getMessage());
            }
        }
    }
    
    private InventoryTransaction createStockOutTransaction(InventoryItem inventoryItem, BigDecimal quantity, String notes) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTenantId(inventoryItem.getTenantId());
        transaction.setTransactionNumber(generateTransactionNumber());
        transaction.setInventoryItemId(inventoryItem.getId());
        transaction.setTransactionType(InventoryTransaction.TransactionType.STOCK_OUT);
        transaction.setQuantity(quantity);
        transaction.setPreviousStock(inventoryItem.getCurrentStock());
        transaction.setNewStock(inventoryItem.getCurrentStock().subtract(quantity));
        transaction.setNotes(notes);
        transaction.setTransactionDate(java.time.LocalDateTime.now());
        transaction.setIsApproved(true);
        return transaction;
    }
    
    private String generateTransactionNumber() {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return String.format("RECIPE-%d-%s", currentRestaurant, timestamp);
    }
    
    // Utility Methods
    public List<String> getCuisineTypes() {
        return List.of("ITALIAN", "AMERICAN", "JAPANESE", "CHINESE", "INDIAN", "MEXICAN", "FRENCH", "THAI", "MEDITERRANEAN", "OTHER");
    }
    
    public List<String> getDifficultyLevels() {
        return List.of("EASY", "MEDIUM", "HARD");
    }
} 