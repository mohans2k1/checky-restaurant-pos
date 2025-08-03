package dev.msundaram.checky.controller;

import dev.msundaram.checky.entity.RestaurantTable;
import dev.msundaram.checky.service.RestaurantTableService;
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
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@Tag(name = "Table Management", description = "APIs for managing restaurant tables")
@SecurityRequirement(name = "ApiKeyAuth")
public class RestaurantTableController {
    
    private final RestaurantTableService restaurantTableService;
    private final TenantService tenantService;
    
    @GetMapping
    @Operation(summary = "Get all tables", description = "Retrieve all active tables for the current restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tables retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<RestaurantTable> tables = restaurantTableService.getAllTables();
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available tables", description = "Retrieve all available tables")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available tables retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RestaurantTable>> getAvailableTables() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<RestaurantTable> tables = restaurantTableService.getAvailableTables();
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/reservable")
    @Operation(summary = "Get reservable tables", description = "Retrieve all tables that can be reserved")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservable tables retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RestaurantTable>> getReservableTables() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<RestaurantTable> tables = restaurantTableService.getReservableTables();
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get tables by status", description = "Retrieve tables filtered by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tables retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RestaurantTable>> getTablesByStatus(
            @Parameter(description = "Table status to filter by") @PathVariable String status) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            RestaurantTable.TableStatus tableStatus = RestaurantTable.TableStatus.valueOf(status.toUpperCase());
            List<RestaurantTable> tables = restaurantTableService.getTablesByStatus(tableStatus);
            return ResponseEntity.ok(tables);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Get tables by type", description = "Retrieve tables filtered by type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tables retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid type"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RestaurantTable>> getTablesByType(
            @Parameter(description = "Table type to filter by") @PathVariable String type) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            RestaurantTable.TableType tableType = RestaurantTable.TableType.valueOf(type.toUpperCase());
            List<RestaurantTable> tables = restaurantTableService.getTablesByType(tableType);
            return ResponseEntity.ok(tables);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/capacity/{minCapacity}")
    @Operation(summary = "Get tables by capacity", description = "Retrieve tables with minimum capacity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tables retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<List<RestaurantTable>> getTablesByCapacity(
            @Parameter(description = "Minimum capacity required") @PathVariable Integer minCapacity) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<RestaurantTable> tables = restaurantTableService.getTablesByCapacity(minCapacity);
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get table by ID", description = "Retrieve a specific table by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Table retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Table not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable> getTableById(
            @Parameter(description = "ID of the table") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return restaurantTableService.getTableById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{tableNumber}")
    @Operation(summary = "Get table by number", description = "Retrieve a specific table by its table number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Table retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Table not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable> getTableByNumber(
            @Parameter(description = "Table number") @PathVariable String tableNumber) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return restaurantTableService.getTableByNumber(tableNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create a new table", description = "Create a new table for the restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Table created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid table data or table number already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable> createTable(@RequestBody RestaurantTable tableRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            RestaurantTable createdTable = restaurantTableService.createTable(tableRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a table", description = "Update an existing table")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Table updated successfully"),
        @ApiResponse(responseCode = "404", description = "Table not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable> updateTable(
            @Parameter(description = "ID of the table") @PathVariable Long id,
            @RequestBody RestaurantTable tableRequest) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            RestaurantTable updatedTable = restaurantTableService.updateTable(id, tableRequest);
            return ResponseEntity.ok(updatedTable);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update table status", description = "Update the status of a table")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Table status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "404", description = "Table not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable> updateTableStatus(
            @Parameter(description = "ID of the table") @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String status = request.get("status");
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            RestaurantTable.TableStatus tableStatus = RestaurantTable.TableStatus.valueOf(status.toUpperCase());
            RestaurantTable updatedTable = restaurantTableService.updateTableStatus(id, tableStatus);
            return ResponseEntity.ok(updatedTable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a table", description = "Deactivate a table (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Table deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Table not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<Void> deleteTable(
            @Parameter(description = "ID of the table") @PathVariable Long id) {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            restaurantTableService.deleteTable(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/statuses")
    @Operation(summary = "Get available table statuses", description = "Get list of all available table statuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statuses retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable.TableStatus[]> getTableStatuses() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(restaurantTableService.getTableStatuses());
    }
    
    @GetMapping("/types")
    @Operation(summary = "Get available table types", description = "Get list of all available table types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Types retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid API key")
    })
    public ResponseEntity<RestaurantTable.TableType[]> getTableTypes() {
        if (!tenantService.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(restaurantTableService.getTableTypes());
    }
} 