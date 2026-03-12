package com.emergency.repository;

import com.emergency.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    
    boolean existsByCode(String code);
    
    /**
     * Find all districts in a province
     */
    List<District> findByProvinceId(Long provinceId);
    
    List<District> findByProvinceName(String provinceName);
}
