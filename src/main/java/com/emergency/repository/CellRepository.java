package com.emergency.repository;

import com.emergency.entity.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    
    boolean existsByCode(String code);
    
    /**
     * Find all cells in a sector
     */
    List<Cell> findBySectorId(Long sectorId);
}
