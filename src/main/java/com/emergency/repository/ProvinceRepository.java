package com.emergency.repository;

import com.emergency.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    
    /**
     * Check if a province exists by code
     * Spring Data JPA generates: SELECT COUNT(*) > 0 FROM province WHERE code = ?
     */
    boolean existsByCode(String code);
    
    /**
     * Check if a province exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Find province by code
     */
    Optional<Province> findByCode(String code);
    
    /**
     * Find province by name
     */
    Optional<Province> findByName(String name);
}
