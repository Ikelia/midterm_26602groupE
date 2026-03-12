package com.emergency.controller;

import com.emergency.entity.Sector;
import com.emergency.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectors")
@CrossOrigin(origins = "*")
public class SectorController {
    
    @Autowired
    private SectorService sectorService;
    
    @PostMapping
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector) {
        return ResponseEntity.ok(sectorService.saveSector(sector));
    }
    
    @GetMapping
    public ResponseEntity<List<Sector>> getAllSectors() {
        return ResponseEntity.ok(sectorService.getAllSectors());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Sector> getSectorById(@PathVariable Long id) {
        return ResponseEntity.ok(sectorService.getSectorById(id));
    }
    
    /**
     * Get all sectors in a district
     * Example: GET /api/sectors/district/1
     */
    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<Sector>> getSectorsByDistrictId(@PathVariable Long districtId) {
        return ResponseEntity.ok(sectorService.getSectorsByDistrictId(districtId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Sector> updateSector(@PathVariable Long id, @RequestBody Sector sector) {
        return ResponseEntity.ok(sectorService.updateSector(id, sector));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSector(@PathVariable Long id) {
        sectorService.deleteSector(id);
        return ResponseEntity.ok("Sector deleted successfully");
    }
}
