package com.emergency.controller;

import com.emergency.entity.Village;
import com.emergency.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/villages")
@CrossOrigin(origins = "*")
public class VillageController {
    
    @Autowired
    private VillageService villageService;
    
    @PostMapping
    public ResponseEntity<Village> createVillage(@RequestBody Village village) {
        return ResponseEntity.ok(villageService.saveVillage(village));
    }
    
    @GetMapping
    public ResponseEntity<List<Village>> getAllVillages() {
        return ResponseEntity.ok(villageService.getAllVillages());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Village> getVillageById(@PathVariable Long id) {
        return ResponseEntity.ok(villageService.getVillageById(id));
    }
    
    /**
     * Get all villages in a cell
     * Example: GET /api/villages/cell/1
     */
    @GetMapping("/cell/{cellId}")
    public ResponseEntity<List<Village>> getVillagesByCellId(@PathVariable Long cellId) {
        return ResponseEntity.ok(villageService.getVillagesByCellId(cellId));
    }
    
    /**
     * Get all villages in a sector
     * Example: GET /api/villages/sector/1
     */
    @GetMapping("/sector/{sectorId}")
    public ResponseEntity<List<Village>> getVillagesBySectorId(@PathVariable Long sectorId) {
        return ResponseEntity.ok(villageService.getVillagesBySectorId(sectorId));
    }
    
    /**
     * Get all villages in a district
     * Example: GET /api/villages/district/1
     */
    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<Village>> getVillagesByDistrictId(@PathVariable Long districtId) {
        return ResponseEntity.ok(villageService.getVillagesByDistrictId(districtId));
    }
    
    /**
     * Get all villages in a province
     * Example: GET /api/villages/province/1
     */
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<Village>> getVillagesByProvinceId(@PathVariable Long provinceId) {
        return ResponseEntity.ok(villageService.getVillagesByProvinceId(provinceId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Village> updateVillage(@PathVariable Long id, @RequestBody Village village) {
        return ResponseEntity.ok(villageService.updateVillage(id, village));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVillage(@PathVariable Long id) {
        villageService.deleteVillage(id);
        return ResponseEntity.ok("Village deleted successfully");
    }
}
