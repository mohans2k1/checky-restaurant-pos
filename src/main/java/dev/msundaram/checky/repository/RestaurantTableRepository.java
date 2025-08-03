package dev.msundaram.checky.repository;

import dev.msundaram.checky.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    
    @Query("SELECT t FROM RestaurantTable t WHERE t.tenantId = :tenantId AND t.isActive = true ORDER BY t.tableNumber")
    List<RestaurantTable> findByTenantIdAndActive(@Param("tenantId") Long tenantId);
    
    @Query("SELECT t FROM RestaurantTable t WHERE t.tenantId = :tenantId AND t.tableStatus = :status ORDER BY t.tableNumber")
    List<RestaurantTable> findByTenantIdAndStatus(@Param("tenantId") Long tenantId, @Param("status") RestaurantTable.TableStatus status);
    
    @Query("SELECT t FROM RestaurantTable t WHERE t.tenantId = :tenantId AND t.tableType = :type ORDER BY t.tableNumber")
    List<RestaurantTable> findByTenantIdAndType(@Param("tenantId") Long tenantId, @Param("type") RestaurantTable.TableType type);
    
    @Query("SELECT t FROM RestaurantTable t WHERE t.tenantId = :tenantId AND t.tableNumber = :tableNumber")
    Optional<RestaurantTable> findByTenantIdAndTableNumber(@Param("tenantId") Long tenantId, @Param("tableNumber") String tableNumber);
    
    @Query("SELECT t FROM RestaurantTable t WHERE t.tenantId = :tenantId AND t.capacity >= :capacity AND t.isActive = true ORDER BY t.capacity")
    List<RestaurantTable> findByTenantIdAndCapacityGreaterThanEqual(@Param("tenantId") Long tenantId, @Param("capacity") Integer capacity);
    
    @Query("SELECT t FROM RestaurantTable t WHERE t.tenantId = :tenantId AND t.isReservable = true AND t.isActive = true ORDER BY t.tableNumber")
    List<RestaurantTable> findReservableTablesByTenantId(@Param("tenantId") Long tenantId);
    
    boolean existsByIdAndTenantId(Long id, Long tenantId);
    
    boolean existsByTableNumberAndTenantId(String tableNumber, Long tenantId);
} 