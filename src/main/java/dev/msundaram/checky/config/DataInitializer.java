package dev.msundaram.checky.config;

import dev.msundaram.checky.entity.*;
import dev.msundaram.checky.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final RestaurantRepository restaurantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final InstructionRepository instructionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeRestaurants();
        initializeApiKeys();
        initializeCategories();
        initializeMenuItems();
        initializeTables();
        initializeInventoryItems();
        initializeRecipes();
        initializeOrders();
    }
    
    private void initializeRestaurants() {
        if (restaurantRepository.count() == 0) {
            log.info("Initializing restaurants...");
            
            Restaurant restaurant1 = new Restaurant();
            restaurant1.setTenantId(1L);
            restaurant1.setName("Pizza Palace");
            restaurant1.setDescription("Best pizza in town");
            restaurant1.setAddress("123 Main St, City");
            restaurant1.setPhone("555-1234");
            restaurant1.setEmail("info@pizzapalace.com");
            restaurant1.setTaxRate(8.5);
            restaurant1.setServiceChargeRate(10.0);
            restaurant1.setCreatedAt(LocalDateTime.now());
            restaurant1.setUpdatedAt(LocalDateTime.now());
            restaurantRepository.save(restaurant1);
            
            Restaurant restaurant2 = new Restaurant();
            restaurant2.setTenantId(2L);
            restaurant2.setName("Burger Joint");
            restaurant2.setDescription("Gourmet burgers and fries");
            restaurant2.setAddress("456 Oak Ave, City");
            restaurant2.setPhone("555-5678");
            restaurant2.setEmail("info@burgerjoint.com");
            restaurant2.setTaxRate(7.5);
            restaurant2.setServiceChargeRate(0.0);
            restaurant2.setCreatedAt(LocalDateTime.now());
            restaurant2.setUpdatedAt(LocalDateTime.now());
            restaurantRepository.save(restaurant2);
            
            Restaurant restaurant3 = new Restaurant();
            restaurant3.setTenantId(3L);
            restaurant3.setName("Sushi Bar");
            restaurant3.setDescription("Fresh sushi and Japanese cuisine");
            restaurant3.setAddress("789 Pine St, City");
            restaurant3.setPhone("555-9012");
            restaurant3.setEmail("info@sushibar.com");
            restaurant3.setTaxRate(9.0);
            restaurant3.setServiceChargeRate(15.0);
            restaurant3.setCreatedAt(LocalDateTime.now());
            restaurant3.setUpdatedAt(LocalDateTime.now());
            restaurantRepository.save(restaurant3);
            
            log.info("Restaurants initialized successfully");
        }
    }
    
    private void initializeApiKeys() {
        if (apiKeyRepository.count() == 0) {
            log.info("Initializing API keys...");
            
            // Create API keys for each restaurant
            createApiKey(1L, "Pizza Palace - Primary API Key");
            createApiKey(2L, "Burger Joint - Primary API Key");
            createApiKey(3L, "Sushi Bar - Primary API Key");
            
            log.info("API keys initialized successfully");
        }
    }
    
    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            log.info("Initializing categories...");
            
            // Pizza Palace categories
            createCategory(1L, "Pizzas", "Fresh baked pizzas", 1);
            createCategory(1L, "Sides", "Appetizers and sides", 2);
            createCategory(1L, "Drinks", "Beverages", 3);
            
            // Burger Joint categories
            createCategory(2L, "Burgers", "Gourmet burgers", 1);
            createCategory(2L, "Sides", "French fries and sides", 2);
            createCategory(2L, "Drinks", "Beverages", 3);
            
            // Sushi Bar categories
            createCategory(3L, "Sushi", "Fresh sushi rolls", 1);
            createCategory(3L, "Sashimi", "Fresh sashimi", 2);
            createCategory(3L, "Drinks", "Beverages", 3);
            
            log.info("Categories initialized successfully");
        }
    }
    
    private void initializeMenuItems() {
        if (menuItemRepository.count() == 0) {
            log.info("Initializing menu items...");
            
            // Pizza Palace items
            createMenuItem(1L, "Margherita Pizza", "Classic tomato and mozzarella", new BigDecimal("12.99"), 1L, 1);
            createMenuItem(1L, "Pepperoni Pizza", "Spicy pepperoni with cheese", new BigDecimal("14.99"), 1L, 2);
            createMenuItem(1L, "Garlic Bread", "Fresh garlic bread", new BigDecimal("4.99"), 2L, 3);
            createMenuItem(1L, "Coke", "Coca Cola", new BigDecimal("2.99"), 3L, 4);
            
            // Burger Joint items
            createMenuItem(2L, "Classic Burger", "Beef burger with lettuce and tomato", new BigDecimal("9.99"), 4L, 5);
            createMenuItem(2L, "Cheese Burger", "Beef burger with cheese", new BigDecimal("10.99"), 4L, 6);
            createMenuItem(2L, "French Fries", "Crispy french fries", new BigDecimal("3.99"), 5L, 7);
            createMenuItem(2L, "Coke", "Coca Cola", new BigDecimal("2.99"), 6L, 8);
            
            // Sushi Bar items
            createMenuItem(3L, "California Roll", "Crab, avocado, cucumber", new BigDecimal("8.99"), 7L, 9);
            createMenuItem(3L, "Salmon Nigiri", "Fresh salmon nigiri", new BigDecimal("6.99"), 8L, 10);
            createMenuItem(3L, "Green Tea", "Hot green tea", new BigDecimal("2.99"), 9L, 11);
            
            log.info("Menu items initialized successfully");
        }
    }
    
    private void initializeTables() {
        if (restaurantTableRepository.count() == 0) {
            log.info("Initializing restaurant tables...");
            
            // Pizza Palace tables
            createTable(1L, "T1", "Table 1", 4, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Main Dining Area");
            createTable(1L, "T2", "Table 2", 6, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Main Dining Area");
            createTable(1L, "T3", "Table 3", 2, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Window Side");
            createTable(1L, "B1", "Booth 1", 4, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.BOOTH, "Corner Booth");
            
            // Burger Joint tables
            createTable(2L, "T1", "Table 1", 4, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Main Area");
            createTable(2L, "T2", "Table 2", 4, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Main Area");
            createTable(2L, "O1", "Outdoor 1", 6, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.OUTDOOR, "Patio");
            createTable(2L, "O2", "Outdoor 2", 6, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.OUTDOOR, "Patio");
            
            // Sushi Bar tables
            createTable(3L, "T1", "Table 1", 4, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Main Dining");
            createTable(3L, "T2", "Table 2", 4, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.INDOOR, "Main Dining");
            createTable(3L, "B1", "Bar Seat 1", 1, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.BAR, "Sushi Bar");
            createTable(3L, "B2", "Bar Seat 2", 1, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.BAR, "Sushi Bar");
            createTable(3L, "PR1", "Private Room 1", 8, RestaurantTable.TableStatus.AVAILABLE, RestaurantTable.TableType.PRIVATE_ROOM, "Private Room");
            
            log.info("Restaurant tables initialized successfully");
        }
    }
    
    private void initializeInventoryItems() {
        if (inventoryItemRepository.count() == 0) {
            log.info("Initializing inventory items...");
            
            // Pizza Palace inventory
            createInventoryItem(1L, "FLOUR-001", "All-Purpose Flour", "High-quality flour for pizza dough", 
                              InventoryItem.InventoryCategory.INGREDIENT, "kg", new BigDecimal("50.0"), 
                              new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("25.0"), 
                              new BigDecimal("2.50"), 1L, "Flour Supplier", "555-0001", 
                              LocalDateTime.now().plusDays(30), true, 90, "Dry Storage", "Main ingredient");
            
            createInventoryItem(1L, "CHEESE-001", "Mozzarella Cheese", "Fresh mozzarella for pizzas", 
                              InventoryItem.InventoryCategory.INGREDIENT, "kg", new BigDecimal("20.0"), 
                              new BigDecimal("2.0"), new BigDecimal("5.0"), new BigDecimal("10.0"), 
                              new BigDecimal("8.00"), 2L, "Dairy Supplier", "555-0002", 
                              LocalDateTime.now().plusDays(7), true, 7, "Refrigerator", "Fresh cheese");
            
            createInventoryItem(1L, "TOMATO-001", "Tomato Sauce", "Pizza sauce base", 
                              InventoryItem.InventoryCategory.INGREDIENT, "liters", new BigDecimal("15.0"), 
                              new BigDecimal("2.0"), new BigDecimal("5.0"), new BigDecimal("10.0"), 
                              new BigDecimal("3.50"), 3L, "Sauce Supplier", "555-0003", 
                              LocalDateTime.now().plusDays(14), true, 14, "Refrigerator", "Pizza sauce");
            
            // Burger Joint inventory
            createInventoryItem(2L, "BEEF-001", "Ground Beef", "Premium ground beef for burgers", 
                              InventoryItem.InventoryCategory.INGREDIENT, "kg", new BigDecimal("30.0"), 
                              new BigDecimal("3.0"), new BigDecimal("8.0"), new BigDecimal("15.0"), 
                              new BigDecimal("12.00"), 4L, "Meat Supplier", "555-0004", 
                              LocalDateTime.now().plusDays(5), true, 5, "Freezer", "Fresh beef");
            
            createInventoryItem(2L, "BUNS-001", "Burger Buns", "Fresh burger buns", 
                              InventoryItem.InventoryCategory.INGREDIENT, "pieces", new BigDecimal("100.0"), 
                              new BigDecimal("10.0"), new BigDecimal("20.0"), new BigDecimal("50.0"), 
                              new BigDecimal("0.50"), 5L, "Bakery Supplier", "555-0005", 
                              LocalDateTime.now().plusDays(3), true, 3, "Dry Storage", "Fresh buns");
            
            createInventoryItem(2L, "POTATO-001", "Potatoes", "Fresh potatoes for fries", 
                              InventoryItem.InventoryCategory.INGREDIENT, "kg", new BigDecimal("40.0"), 
                              new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("20.0"), 
                              new BigDecimal("2.00"), 6L, "Produce Supplier", "555-0006", 
                              LocalDateTime.now().plusDays(7), true, 7, "Cool Storage", "Fresh potatoes");
            
            // Sushi Bar inventory
            createInventoryItem(3L, "RICE-001", "Sushi Rice", "Premium sushi rice", 
                              InventoryItem.InventoryCategory.INGREDIENT, "kg", new BigDecimal("25.0"), 
                              new BigDecimal("3.0"), new BigDecimal("8.0"), new BigDecimal("12.0"), 
                              new BigDecimal("4.50"), 7L, "Rice Supplier", "555-0007", 
                              LocalDateTime.now().plusDays(30), false, 180, "Dry Storage", "Sushi rice");
            
            createInventoryItem(3L, "SALMON-001", "Fresh Salmon", "Fresh salmon for sushi", 
                              InventoryItem.InventoryCategory.INGREDIENT, "kg", new BigDecimal("8.0"), 
                              new BigDecimal("1.0"), new BigDecimal("3.0"), new BigDecimal("5.0"), 
                              new BigDecimal("25.00"), 8L, "Fish Supplier", "555-0008", 
                              LocalDateTime.now().plusDays(2), true, 2, "Freezer", "Fresh salmon");
            
            createInventoryItem(3L, "NORI-001", "Nori Sheets", "Dried seaweed for sushi", 
                              InventoryItem.InventoryCategory.INGREDIENT, "sheets", new BigDecimal("200.0"), 
                              new BigDecimal("20.0"), new BigDecimal("50.0"), new BigDecimal("100.0"), 
                              new BigDecimal("0.25"), 9L, "Seaweed Supplier", "555-0009", 
                              LocalDateTime.now().plusDays(60), false, 180, "Dry Storage", "Nori sheets");
            
            log.info("Inventory items initialized successfully");
        }
    }
    
    private void initializeRecipes() {
        if (recipeRepository.count() == 0) {
            log.info("Initializing recipes...");
            createRecipe(1L, "Margherita Pizza Recipe", "Classic Italian margherita pizza", 1L, 15, 20, 20, "MEDIUM", "ITALIAN", false, false, false);
            createRecipe(1L, "Pepperoni Pizza Recipe", "Spicy pepperoni pizza", 2L, 15, 20, 20, "MEDIUM", "ITALIAN", false, false, true);
            
            createRecipe(2L, "Classic Burger Recipe", "Traditional beef burger", 5L, 10, 15, 15, "EASY", "AMERICAN", false, false, false);
            createRecipe(2L, "Cheese Burger Recipe", "Beef burger with cheese", 6L, 10, 15, 15, "EASY", "AMERICAN", false, false, false);
            createRecipe(2L, "French Fries Recipe", "Crispy french fries", 7L, 5, 12, 12, "EASY", "AMERICAN", true, false, false);
            
            createRecipe(3L, "California Roll Recipe", "California roll with crab and avocado", 9L, 2, 10, 10, "MEDIUM", "JAPANESE", false, false, false);
            createRecipe(3L, "Salmon Nigiri Recipe", "Fresh salmon nigiri", 10L, 15, 0, 0, "MEDIUM", "JAPANESE", false, false, false);
            
            // Add ingredients to recipes
            addRecipeIngredients();
            
            // Add instructions to recipes
            addRecipeInstructions();
            
            log.info("Recipes initialized successfully");
        }
    }
    
    private void initializeOrders() {
        if (orderRepository.count() == 0) {
            log.info("Initializing sample orders...");
            
            // Create sample orders for each restaurant
            createSampleOrder(1L, "John Doe", "555-0001", "Table 1", Order.OrderType.DINE_IN);
            createSampleOrder(2L, "Jane Smith", "555-0002", "Table 3", Order.OrderType.DINE_IN);
            createSampleOrder(3L, "Mike Johnson", "555-0003", null, Order.OrderType.TAKEAWAY);
            
            log.info("Sample orders initialized successfully");
        }
    }
    
    private void createApiKey(Long restaurantId, String description) {
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKey("api_key_restaurant" + restaurantId);
        apiKey.setRestaurantId(restaurantId);
        apiKey.setDescription(description);
        apiKey.setIsActive(true);
        apiKey.setTenantId(restaurantId);
        apiKey.setCreatedAt(LocalDateTime.now());
        apiKey.setUpdatedAt(LocalDateTime.now());
        apiKeyRepository.save(apiKey);
    }
    
    private void createCategory(Long tenantId, String name, String description, int displayOrder) {
        Category category = new Category();
        category.setTenantId(tenantId);
        category.setName(name);
        category.setDescription(description);
        category.setDisplayOrder(displayOrder);
        category.setIsActive(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    private void createMenuItem(Long tenantId, String name, String description, BigDecimal price, Long categoryId, int displayOrder) {
        MenuItem menuItem = new MenuItem();
        menuItem.setTenantId(tenantId);
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setDisplayOrder(displayOrder);
        menuItem.setIsAvailable(true);
        menuItem.setIsActive(true);
        
        Category category = new Category();
        category.setId(categoryId);
        menuItem.setCategory(category);
        
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());
        menuItemRepository.save(menuItem);
    }
    
    private void createTable(Long tenantId, String tableNumber, String tableName, Integer capacity, 
                           RestaurantTable.TableStatus status, RestaurantTable.TableType type, String location) {
        RestaurantTable table = new RestaurantTable();
        table.setTenantId(tenantId);
        table.setTableNumber(tableNumber);
        table.setTableName(tableName);
        table.setCapacity(capacity);
        table.setTableStatus(status);
        table.setTableType(type);
        table.setLocation(location);
        table.setIsActive(true);
        table.setIsReservable(true);
        table.setCreatedAt(LocalDateTime.now());
        table.setUpdatedAt(LocalDateTime.now());
        restaurantTableRepository.save(table);
    }
    
    private void createInventoryItem(Long tenantId, String itemCode, String name, String description, 
                                   InventoryItem.InventoryCategory category, String unit, BigDecimal currentStock,
                                   BigDecimal minimumStock, BigDecimal reorderLevel, BigDecimal reorderQuantity,
                                   BigDecimal unitCost, Long supplierId, String supplierName, String supplierContact,
                                   LocalDateTime expiryDate, Boolean isPerishable, Integer shelfLifeDays,
                                   String location, String notes) {
        InventoryItem item = new InventoryItem();
        item.setTenantId(tenantId);
        item.setItemCode(itemCode);
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setUnit(unit);
        item.setCurrentStock(currentStock);
        item.setMinimumStock(minimumStock);
        item.setReorderLevel(reorderLevel);
        item.setReorderQuantity(reorderQuantity);
        item.setUnitCost(unitCost);
        item.setSupplierId(supplierId);
        item.setSupplierName(supplierName);
        item.setSupplierContact(supplierContact);
        item.setLastRestockedDate(LocalDateTime.now());
        item.setExpiryDate(expiryDate);
        item.setIsActive(true);
        item.setIsPerishable(isPerishable);
        item.setShelfLifeDays(shelfLifeDays);
        item.setLocation(location);
        item.setNotes(notes);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        inventoryItemRepository.save(item);
    }
    
    private void createRecipe(Long tenantId, String name, String description, Long menuItemId, Integer servingSize,
                            Integer preparationTime, Integer cookingTime, String difficultyLevel, String cuisineType,
                            Boolean isVegetarian, Boolean isGlutenFree, Boolean isSpicy) {
        Recipe recipe = new Recipe();
        recipe.setTenantId(tenantId);
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setMenuItemId(menuItemId);
        recipe.setServingSize(servingSize);
        recipe.setPreparationTimeMinutes(preparationTime);
        recipe.setCookingTimeMinutes(cookingTime);
        recipe.setDifficultyLevel(difficultyLevel);
        recipe.setCuisineType(cuisineType);
        recipe.setIsActive(true);
        recipe.setIsVegetarian(isVegetarian);
        recipe.setIsGlutenFree(isGlutenFree);
        recipe.setIsSpicy(isSpicy);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        recipeRepository.save(recipe);
    }
    
    private void addRecipeIngredients() {
        // Margherita Pizza ingredients
        createRecipeIngredient(1L, 1L, 1L, new BigDecimal("0.25"), "kg", 1, "Pizza dough base");
        createRecipeIngredient(1L, 1L, 2L, new BigDecimal("0.15"), "kg", 2, "Mozzarella cheese");
        createRecipeIngredient(1L, 1L, 3L, new BigDecimal("0.05"), "liters", 3, "Tomato sauce");
        
        // Pepperoni Pizza ingredients
        createRecipeIngredient(1L, 2L, 1L, new BigDecimal("0.25"), "kg", 1, "Pizza dough base");
        createRecipeIngredient(1L, 2L, 2L, new BigDecimal("0.15"), "kg", 2, "Mozzarella cheese");
        createRecipeIngredient(1L, 2L, 3L, new BigDecimal("0.05"), "liters", 3, "Tomato sauce");
        
        // Classic Burger ingredients
        createRecipeIngredient(2L, 3L, 4L, new BigDecimal("0.15"), "kg", 1, "Ground beef patty");
        createRecipeIngredient(2L, 3L, 5L, new BigDecimal("1"), "pieces", 2, "Burger bun");
        
        // Cheese Burger ingredients
        createRecipeIngredient(2L, 4L, 4L, new BigDecimal("0.15"), "kg", 1, "Ground beef patty");
        createRecipeIngredient(2L, 4L, 5L, new BigDecimal("1"), "pieces", 2, "Burger bun");
        createRecipeIngredient(2L, 4L, 2L, new BigDecimal("0.02"), "kg", 3, "Cheese slice");
        
        // French Fries ingredients
        createRecipeIngredient(2L, 5L, 6L, new BigDecimal("0.20"), "kg", 1, "Potatoes");
        
        // California Roll ingredients
        createRecipeIngredient(3L, 6L, 7L, new BigDecimal("0.10"), "kg", 1, "Sushi rice");
        createRecipeIngredient(3L, 6L, 9L, new BigDecimal("1"), "sheets", 2, "Nori sheet");
        
        // Salmon Nigiri ingredients
        createRecipeIngredient(3L, 7L, 7L, new BigDecimal("0.05"), "kg", 1, "Sushi rice");
        createRecipeIngredient(3L, 7L, 8L, new BigDecimal("0.03"), "kg", 2, "Fresh salmon");
    }
    
    private void createRecipeIngredient(Long tenantId, Long recipeId, Long inventoryItemId, BigDecimal quantity, 
                                      String unit, Integer displayOrder, String notes) {
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setTenantId(tenantId);
        ingredient.setRecipeId(recipeId);
        ingredient.setInventoryItemId(inventoryItemId);
        ingredient.setQuantity(quantity);
        ingredient.setUnit(unit);
        ingredient.setDisplayOrder(displayOrder);
        ingredient.setNotes(notes);
        ingredient.setCreatedAt(LocalDateTime.now());
        ingredient.setUpdatedAt(LocalDateTime.now());
        recipeIngredientRepository.save(ingredient);
    }
    
    private void addRecipeInstructions() {
        // Margherita Pizza instructions
        createInstruction(1L, 1L, 1, "Preheat oven to 450°F (230°C)", 5, null);
        createInstruction(1L, 1L, 2, "Roll out pizza dough on a floured surface", 3, null);
        createInstruction(1L, 1L, 3, "Spread tomato sauce evenly on dough", 2, null);
        createInstruction(1L, 1L, 4, "Sprinkle mozzarella cheese on top", 2, null);
        createInstruction(1L, 1L, 5, "Bake for 15-20 minutes until cheese is melted and crust is golden", 20, 450);
        
        // Classic Burger instructions
        createInstruction(2L, 3L, 1, "Form ground beef into a patty", 2, null);
        createInstruction(2L, 3L, 2, "Season patty with salt and pepper", 1, null);
        createInstruction(2L, 3L, 3, "Grill patty for 4-5 minutes per side", 10, null);
        createInstruction(2L, 3L, 4, "Toast burger bun", 2, null);
        createInstruction(2L, 3L, 5, "Assemble burger with patty and bun", 1, null);
        
        // California Roll instructions
        createInstruction(3L, 6L, 1, "Cook sushi rice according to package instructions", 30, null);
        createInstruction(3L, 6L, 2, "Place nori sheet on bamboo mat", 1, null);
        createInstruction(3L, 6L, 3, "Spread rice evenly on nori", 2, null);
        createInstruction(3L, 6L, 4, "Add crab and avocado in center", 2, null);
        createInstruction(3L, 6L, 5, "Roll tightly using bamboo mat", 3, null);
        createInstruction(3L, 6L, 6, "Cut into 8 pieces", 2, null);
    }
    
    private void createInstruction(Long tenantId, Long recipeId, Integer stepNumber, String instructionText, 
                                 Integer timeMinutes, Integer temperatureCelsius) {
        Instruction instruction = new Instruction();
        instruction.setTenantId(tenantId);
        instruction.setRecipeId(recipeId);
        instruction.setStepNumber(stepNumber);
        instruction.setInstructionText(instructionText);
        instruction.setTimeMinutes(timeMinutes);
        instruction.setTemperatureCelsius(temperatureCelsius);
        instruction.setCreatedAt(LocalDateTime.now());
        instruction.setUpdatedAt(LocalDateTime.now());
        instructionRepository.save(instruction);
    }
    
    private void createSampleOrder(Long tenantId, String customerName, String customerPhone, String tableNumber, Order.OrderType orderType) {
        Order order = new Order();
        order.setTenantId(tenantId);
        order.setOrderNumber("ORD-" + tenantId + "-" + System.currentTimeMillis());
        order.setOrderType(orderType);
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setTableNumber(tableNumber);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setNotes("Sample order for testing");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Add order items
        order.setOrderItems(new java.util.ArrayList<>());
        
        // Get actual menu items for this tenant
        List<MenuItem> menuItems = menuItemRepository.findByTenantId(tenantId);
        if (menuItems.isEmpty()) {
            log.warn("No menu items found for tenant {}, skipping order creation", tenantId);
            return;
        }
        
        // Add 2 items to the order using actual menu items
        for (int i = 0; i < Math.min(2, menuItems.size()); i++) {
            MenuItem menuItem = menuItems.get(i);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setTenantId(tenantId);
            orderItem.setQuantity(i + 1);
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setTotalPrice(menuItem.getPrice().multiply(BigDecimal.valueOf(i + 1)));
            orderItem.setNotes("Sample item " + (i + 1));
            orderItem.setItemStatus(OrderItem.ItemStatus.PENDING);
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());
            
            // Set the actual menu item reference
            orderItem.setMenuItem(menuItem);
            
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        
        orderRepository.save(order);
    }
} 