package dev.msundaram.checky.service;

import dev.msundaram.checky.entity.RestaurantTable;
import dev.msundaram.checky.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantTableService {
    
    private final RestaurantTableRepository restaurantTableRepository;
    private final TenantService tenantService;
    
    public List<RestaurantTable> getAllTables() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findByTenantIdAndActive(currentRestaurant);
    }
    
    public List<RestaurantTable> getTablesByStatus(RestaurantTable.TableStatus status) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findByTenantIdAndStatus(currentRestaurant, status);
    }
    
    public List<RestaurantTable> getTablesByType(RestaurantTable.TableType type) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findByTenantIdAndType(currentRestaurant, type);
    }
    
    public List<RestaurantTable> getAvailableTables() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findByTenantIdAndStatus(currentRestaurant, RestaurantTable.TableStatus.AVAILABLE);
    }
    
    public List<RestaurantTable> getReservableTables() {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findReservableTablesByTenantId(currentRestaurant);
    }
    
    public List<RestaurantTable> getTablesByCapacity(Integer minCapacity) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findByTenantIdAndCapacityGreaterThanEqual(currentRestaurant, minCapacity);
    }
    
    public Optional<RestaurantTable> getTableById(Long tableId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findById(tableId);
        if (tableOpt.isPresent() && tableOpt.get().getTenantId().equals(currentRestaurant)) {
            return tableOpt;
        }
        return Optional.empty();
    }
    
    public Optional<RestaurantTable> getTableByNumber(String tableNumber) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        return restaurantTableRepository.findByTenantIdAndTableNumber(currentRestaurant, tableNumber);
    }
    
    @Transactional
    public RestaurantTable createTable(RestaurantTable tableRequest) {
        // Set tenant context
        tenantService.setRestaurantOnEntity(tableRequest);
        
        // Check if table number already exists
        if (restaurantTableRepository.existsByTableNumberAndTenantId(tableRequest.getTableNumber(), tenantService.getCurrentRestaurant())) {
            throw new RuntimeException("Table number already exists");
        }
        
        RestaurantTable savedTable = restaurantTableRepository.save(tableRequest);
        log.info("Created table {} for restaurant {}", savedTable.getTableNumber(), tenantService.getCurrentRestaurant());
        return savedTable;
    }
    
    @Transactional
    public RestaurantTable updateTable(Long tableId, RestaurantTable tableRequest) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<RestaurantTable> existingTableOpt = restaurantTableRepository.findById(tableId);
        if (existingTableOpt.isEmpty() || !existingTableOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Table not found");
        }
        
        RestaurantTable existingTable = existingTableOpt.get();
        
        // Update fields
        existingTable.setTableName(tableRequest.getTableName());
        existingTable.setCapacity(tableRequest.getCapacity());
        existingTable.setTableType(tableRequest.getTableType());
        existingTable.setLocation(tableRequest.getLocation());
        existingTable.setIsActive(tableRequest.getIsActive());
        existingTable.setIsReservable(tableRequest.getIsReservable());
        existingTable.setNotes(tableRequest.getNotes());
        
        RestaurantTable updatedTable = restaurantTableRepository.save(existingTable);
        log.info("Updated table {} for restaurant {}", updatedTable.getTableNumber(), currentRestaurant);
        return updatedTable;
    }
    
    @Transactional
    public RestaurantTable updateTableStatus(Long tableId, RestaurantTable.TableStatus newStatus) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findById(tableId);
        if (tableOpt.isEmpty() || !tableOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Table not found");
        }
        
        RestaurantTable table = tableOpt.get();
        table.setTableStatus(newStatus);
        
        RestaurantTable updatedTable = restaurantTableRepository.save(table);
        log.info("Updated table {} status to {} for restaurant {}", updatedTable.getTableNumber(), newStatus, currentRestaurant);
        return updatedTable;
    }
    
    @Transactional
    public void deleteTable(Long tableId) {
        Long currentRestaurant = tenantService.getCurrentRestaurant();
        
        Optional<RestaurantTable> tableOpt = restaurantTableRepository.findById(tableId);
        if (tableOpt.isEmpty() || !tableOpt.get().getTenantId().equals(currentRestaurant)) {
            throw new RuntimeException("Table not found");
        }
        
        RestaurantTable table = tableOpt.get();
        table.setIsActive(false);
        restaurantTableRepository.save(table);
        
        log.info("Deactivated table {} for restaurant {}", table.getTableNumber(), currentRestaurant);
    }
    
    public RestaurantTable.TableStatus[] getTableStatuses() {
        return RestaurantTable.TableStatus.values();
    }
    
    public RestaurantTable.TableType[] getTableTypes() {
        return RestaurantTable.TableType.values();
    }
} 