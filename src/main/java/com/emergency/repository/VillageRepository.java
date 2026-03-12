package com.emergency.repository;

import com.emergency.entity.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
    
    boolean existsByCode(String code);
    
    /**
     * Find all villages in a cell
     */
    List<Village> findByCellId(Long cellId);
    
    /**
     * Find all villages in a sector (through cell)
     */
    @Query("SELECT v FROM Village v WHERE v.cell.sector.id = :sectorId")
    List<Village> findBySectorId(@Param("sectorId") Long sectorId);
    
    /**
     * Find all villages in a district (through cell → sector)
     */
    @Query("SELECT v FROM Village v WHERE v.cell.sector.district.id = :districtId")
    List<Village> findByDistrictId(@Param("districtId") Long districtId);
    
    /**
     * Find all villages in a province (through cell → sector → district)
     */
    @Query("SELECT v FROM Village v WHERE v.cell.sector.district.province.id = :provinceId")
    List<Village> findByProvinceId(@Param("provinceId") Long provinceId);
}
