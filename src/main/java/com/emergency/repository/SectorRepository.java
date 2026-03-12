package com.emergency.repository;

import com.emergency.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    
    boolean existsByCode(String code);
    
    /**
     * Find all sectors in a district
     */
    List<Sector> findByDistrictId(Long districtId);
}
