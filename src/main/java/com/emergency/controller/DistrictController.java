package com.emergency.controller;

import com.emergency.entity.District;
import com.emergency.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@CrossOrigin(origins = "*")
public class DistrictController {
    
    @Autowired
    private DistrictService districtService;
    
    @PostMapping
    public ResponseEntity<District> createDistrict(@RequestBody District district) {
        return ResponseEntity.ok(districtService.saveDistrict(district));
    }
    
    @GetMapping
    public ResponseEntity<List<District>> getAllDistricts() {
        return ResponseEntity.ok(districtService.getAllDistricts());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable Long id) {
        return ResponseEntity.ok(districtService.getDistrictById(id));
    }
    
    /**
     * Get all districts in a province
     * Example: GET /api/districts/province/1
     */
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<District>> getDistrictsByProvinceId(@PathVariable Long provinceId) {
        return ResponseEntity.ok(districtService.getDistrictsByProvinceId(provinceId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<District> updateDistrict(@PathVariable Long id, @RequestBody District district) {
        return ResponseEntity.ok(districtService.updateDistrict(id, district));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDistrict(@PathVariable Long id) {
        districtService.deleteDistrict(id);
        return ResponseEntity.ok("District deleted successfully");
    }
}
