package com.emergency.service;

import com.emergency.entity.Cell;
import com.emergency.repository.CellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CellService {
    
    @Autowired
    private CellRepository cellRepository;
    
    public Cell saveCell(Cell cell) {
        if (cellRepository.existsByCode(cell.getCode())) {
            throw new RuntimeException("Cell code already exists: " + cell.getCode());
        }
        return cellRepository.save(cell);
    }
    
    public List<Cell> getAllCells() {
        return cellRepository.findAll();
    }
    
    public Cell getCellById(Long id) {
        return cellRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cell not found with id: " + id));
    }
    
    /**
     * Get all cells in a sector
     */
    public List<Cell> getCellsBySectorId(Long sectorId) {
        return cellRepository.findBySectorId(sectorId);
    }
    
    public Cell updateCell(Long id, Cell cellDetails) {
        Cell cell = getCellById(id);
        
        if (!cell.getCode().equals(cellDetails.getCode()) && 
            cellRepository.existsByCode(cellDetails.getCode())) {
            throw new RuntimeException("Cell code already exists: " + cellDetails.getCode());
        }
        
        cell.setName(cellDetails.getName());
        cell.setCode(cellDetails.getCode());
        if (cellDetails.getSector() != null) {
            cell.setSector(cellDetails.getSector());
        }
        
        return cellRepository.save(cell);
    }
    
    public void deleteCell(Long id) {
        Cell cell = getCellById(id);
        cellRepository.delete(cell);
    }
}
