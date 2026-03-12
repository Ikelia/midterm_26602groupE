package com.emergency.controller;

import com.emergency.entity.Cell;
import com.emergency.service.CellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cells")
@CrossOrigin(origins = "*")
public class CellController {
    
    @Autowired
    private CellService cellService;
    
    @PostMapping
    public ResponseEntity<Cell> createCell(@RequestBody Cell cell) {
        return ResponseEntity.ok(cellService.saveCell(cell));
    }
    
    @GetMapping
    public ResponseEntity<List<Cell>> getAllCells() {
        return ResponseEntity.ok(cellService.getAllCells());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cell> getCellById(@PathVariable Long id) {
        return ResponseEntity.ok(cellService.getCellById(id));
    }
    
    /**
     * Get all cells in a sector
     * Example: GET /api/cells/sector/1
     */
    @GetMapping("/sector/{sectorId}")
    public ResponseEntity<List<Cell>> getCellsBySectorId(@PathVariable Long sectorId) {
        return ResponseEntity.ok(cellService.getCellsBySectorId(sectorId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cell> updateCell(@PathVariable Long id, @RequestBody Cell cell) {
        return ResponseEntity.ok(cellService.updateCell(id, cell));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCell(@PathVariable Long id) {
        cellService.deleteCell(id);
        return ResponseEntity.ok("Cell deleted successfully");
    }
}
