package com.emergency.repository;

import com.emergency.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    /**
     * Check if resource exists by name
     */
    boolean existsByName(String name);
}
