package com.emergency.controller;

import com.emergency.entity.Province;
import com.emergency.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@CrossOrigin(origins = "*")
public class ProvinceController {
    
    @Autowired
    private ProvinceService provinceService;
    
    @PostMapping
    public ResponseEntity<Province> createProvince(@RequestBody Province province) {
        return ResponseEntity.ok(provinceService.saveProvince(province));
    }
    
    @GetMapping
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(provinceService.getAllProvinces());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.getProvinceById(id));
    }
    
    /**
     * Update province by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<Province> updateProvince(@PathVariable Long id, @RequestBody Province province) {
        return ResponseEntity.ok(provinceService.updateProvince(id, province));
    }
    
    /**
     * Delete province by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProvince(@PathVariable Long id) {
        provinceService.deleteProvince(id);
        return ResponseEntity.ok("Province deleted successfully");
    }
}
