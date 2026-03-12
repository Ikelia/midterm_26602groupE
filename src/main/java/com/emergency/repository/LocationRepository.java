package com.emergency.repository;

import com.emergency.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    /**
     * Find all locations in a village
     */
    List<Location> findByVillageId(Long villageId);
    
    /**
     * Find all locations in a cell (through village)
     */
    @Query("SELECT l FROM Location l WHERE l.village.cell.id = :cellId")
    List<Location> findByCellId(@Param("cellId") Long cellId);
    
    /**
     * Find all locations in a sector (through village → cell)
     */
    @Query("SELECT l FROM Location l WHERE l.village.cell.sector.id = :sectorId")
    List<Location> findBySectorId(@Param("sectorId") Long sectorId);
    
    /**
     * Find all locations in a district (through village → cell → sector)
     */
    @Query("SELECT l FROM Location l WHERE l.village.cell.sector.district.id = :districtId")
    List<Location> findByDistrictId(@Param("districtId") Long districtId);
    
    /**
     * Find all locations in a province (through village → cell → sector → district)
     */
    @Query("SELECT l FROM Location l WHERE l.village.cell.sector.district.province.id = :provinceId")
    List<Location> findByProvinceId(@Param("provinceId") Long provinceId);
}
