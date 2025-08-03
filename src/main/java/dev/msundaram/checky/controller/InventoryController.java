package dev.msundaram.checky.controller;

import dev.msundaram.checky.entity.InventoryItem;
import dev.msundaram.checky.entity.InventoryTransaction;
import dev.msundaram.checky.service.InventoryService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing inventory items and transactions")
@SecurityRequirement(name = "ApiKeyAuth")
public class InventoryController {
    
    private final InventoryService inventoryService;
    private final TenantService tenantService;
    
    // Inventory Items
    
    @GetMapping("/items")
    @Operation(summary = "Get all inventory items", description = "Retrieve all active inventory items for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryItem> items = inventoryService.getAllInventoryItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/items/category/{category}")
    @Operation(summary = "Get inventory items by category", description = "Retrieve inventory items filtered by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryItem>> getInventoryItemsByCategory(
            @Parameter(description = "Inventory category to filter by") @PathVariable String category) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            InventoryItem.InventoryCategory itemCategory = InventoryItem.InventoryCategory.valueOf(category.toUpperCase());
            List<InventoryItem> items = inventoryService.getInventoryItemsByCategory(itemCategory);
            return ResponseEntity.ok(items);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/items/low-stock")
    @Operation(summary = "Get low stock items", description = "Retrieve items that are at or below reorder level")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Low stock items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryItem>> getLowStockItems() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryItem> items = inventoryService.getLowStockItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/items/out-of-stock")
    @Operation(summary = "Get out of stock items", description = "Retrieve items that are at or below minimum stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Out of stock items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryItem>> getOutOfStockItems() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryItem> items = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/items/expiring/{daysAhead}")
    @Operation(summary = "Get expiring items", description = "Retrieve perishable items that will expire within specified days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expiring items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryItem>> getExpiringItems(
            @Parameter(description = "Number of days ahead to check") @PathVariable int daysAhead) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryItem> items = inventoryService.getExpiringItems(daysAhead);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/items/search")
    @Operation(summary = "Search inventory items", description = "Search inventory items by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryItem>> searchInventoryItems(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryItem> items = inventoryService.searchInventoryItems(searchTerm);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/items/{id}")
    @Operation(summary = "Get inventory item by ID", description = "Retrieve a specific inventory item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory item retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryItem> getInventoryItemById(
            @Parameter(description = "ID of the inventory item") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return inventoryService.getInventoryItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/items/code/{itemCode}")
    @Operation(summary = "Get inventory item by code", description = "Retrieve a specific inventory item by its item code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory item retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryItem> getInventoryItemByCode(
            @Parameter(description = "Item code") @PathVariable String itemCode) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return inventoryService.getInventoryItemByCode(itemCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/items")
    @Operation(summary = "Create a new inventory item", description = "Create a new inventory item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inventory item created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid item data or item code already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryItem> createInventoryItem(@RequestBody InventoryItem itemRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            InventoryItem createdItem = inventoryService.createInventoryItem(itemRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/items/{id}")
    @Operation(summary = "Update an inventory item", description = "Update an existing inventory item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory item updated successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryItem> updateInventoryItem(
            @Parameter(description = "ID of the inventory item") @PathVariable Long id,
            @RequestBody InventoryItem itemRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            InventoryItem updatedItem = inventoryService.updateInventoryItem(id, itemRequest);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete an inventory item", description = "Deactivate an inventory item (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inventory item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Void> deleteInventoryItem(
            @Parameter(description = "ID of the inventory item") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            inventoryService.deleteInventoryItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Inventory Transactions
    
    @GetMapping("/transactions")
    @Operation(summary = "Get all inventory transactions", description = "Retrieve all inventory transactions for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryTransaction>> getAllTransactions() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryTransaction> transactions = inventoryService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/item/{itemId}")
    @Operation(summary = "Get transactions by item", description = "Retrieve all transactions for a specific inventory item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryTransaction>> getTransactionsByItem(
            @Parameter(description = "ID of the inventory item") @PathVariable Long itemId) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryTransaction> transactions = inventoryService.getTransactionsByItem(itemId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/type/{type}")
    @Operation(summary = "Get transactions by type", description = "Retrieve transactions filtered by transaction type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction type"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryTransaction>> getTransactionsByType(
            @Parameter(description = "Transaction type to filter by") @PathVariable String type) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            InventoryTransaction.TransactionType transactionType = InventoryTransaction.TransactionType.valueOf(type.toUpperCase());
            List<InventoryTransaction> transactions = inventoryService.getTransactionsByType(transactionType);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/transactions/date-range")
    @Operation(summary = "Get transactions by date range", description = "Retrieve transactions within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryTransaction>> getTransactionsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryTransaction> transactions = inventoryService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/number/{transactionNumber}")
    @Operation(summary = "Get transaction by number", description = "Retrieve a specific transaction by its transaction number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryTransaction> getTransactionByNumber(
            @Parameter(description = "Transaction number") @PathVariable String transactionNumber) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return inventoryService.getTransactionByNumber(transactionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Stock Operations
    
    @PostMapping("/stock-in")
    @Operation(summary = "Add stock to inventory", description = "Create a stock-in transaction to add inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock-in transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryTransaction> addStock(@RequestBody Map<String, Object> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long itemId = Long.valueOf(request.get("itemId").toString());
            BigDecimal quantity = new BigDecimal(request.get("quantity").toString());
            BigDecimal unitCost = request.get("unitCost") != null ? new BigDecimal(request.get("unitCost").toString()) : null;
            String referenceNumber = (String) request.get("referenceNumber");
            String notes = (String) request.get("notes");
            LocalDateTime expiryDate = request.get("expiryDate") != null ? 
                LocalDateTime.parse(request.get("expiryDate").toString()) : null;
            
            InventoryTransaction transaction = inventoryService.createStockInTransaction(itemId, quantity, unitCost, referenceNumber, notes, expiryDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/stock-out")
    @Operation(summary = "Remove stock from inventory", description = "Create a stock-out transaction to remove inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock-out transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data or insufficient stock"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryTransaction> removeStock(@RequestBody Map<String, Object> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long itemId = Long.valueOf(request.get("itemId").toString());
            BigDecimal quantity = new BigDecimal(request.get("quantity").toString());
            String referenceNumber = (String) request.get("referenceNumber");
            String notes = (String) request.get("notes");
            String locationFrom = (String) request.get("locationFrom");
            
            InventoryTransaction transaction = inventoryService.createStockOutTransaction(itemId, quantity, referenceNumber, notes, locationFrom);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/adjustment")
    @Operation(summary = "Adjust inventory stock", description = "Create an adjustment transaction to modify inventory levels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Adjustment transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryTransaction> adjustStock(@RequestBody Map<String, Object> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long itemId = Long.valueOf(request.get("itemId").toString());
            BigDecimal quantity = new BigDecimal(request.get("quantity").toString());
            String reason = (String) request.get("reason");
            String notes = (String) request.get("notes");
            
            InventoryTransaction transaction = inventoryService.createAdjustmentTransaction(itemId, quantity, reason, notes);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "Transfer inventory between locations", description = "Create a transfer transaction to move inventory between locations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transfer transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryTransaction> transferStock(@RequestBody Map<String, Object> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long itemId = Long.valueOf(request.get("itemId").toString());
            BigDecimal quantity = new BigDecimal(request.get("quantity").toString());
            String locationFrom = (String) request.get("locationFrom");
            String locationTo = (String) request.get("locationTo");
            String notes = (String) request.get("notes");
            
            InventoryTransaction transaction = inventoryService.createTransferTransaction(itemId, quantity, locationFrom, locationTo, notes);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Utility Endpoints
    
    @GetMapping("/categories")
    @Operation(summary = "Get inventory categories", description = "Get list of all available inventory categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryItem.InventoryCategory[]> getInventoryCategories() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(inventoryService.getInventoryCategories());
    }
    
    @GetMapping("/transaction-types")
    @Operation(summary = "Get transaction types", description = "Get list of all available transaction types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction types retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<InventoryTransaction.TransactionType[]> getTransactionTypes() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(inventoryService.getTransactionTypes());
    }
    
    @GetMapping("/transactions/pending-approvals")
    @Operation(summary = "Get pending approvals", description = "Get list of transactions pending approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending approvals retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<InventoryTransaction>> getPendingApprovals() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<InventoryTransaction> transactions = inventoryService.getPendingApprovals();
        return ResponseEntity.ok(transactions);
    }
} 