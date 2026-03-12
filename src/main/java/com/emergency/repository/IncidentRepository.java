package com.emergency.repository;

import com.emergency.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    /**
     * Pagination and Sorting for incidents
     */
    Page<Incident> findAll(Pageable pageable);
}
