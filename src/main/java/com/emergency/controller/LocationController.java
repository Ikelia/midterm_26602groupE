package com.emergency.controller;

import com.emergency.entity.Location;
import com.emergency.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    /**
     * Save Location endpoint
     * Example POST body:
     * {
     *   "address": "123 Main St, Johannesburg",
     *   "latitude": -26.2041,
     *   "longitude": 28.0473
     * }
     */
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.saveLocation(location));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }
    
    /**
     * Update location by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }
    
    /**
     * Delete location by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Location deleted successfully");
    }
    
    /**
     * Get all locations
     */
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
    
    /**
     * Get all locations in a village
     * Example: GET /api/locations/village/1
     */
    @GetMapping("/village/{villageId}")
    public ResponseEntity<List<Location>> getLocationsByVillageId(@PathVariable Long villageId) {
        return ResponseEntity.ok(locationService.getLocationsByVillageId(villageId));
    }
    
    /**
     * Get all locations in a cell
     * Example: GET /api/locations/cell/1
     */
    @GetMapping("/cell/{cellId}")
    public ResponseEntity<List<Location>> getLocationsByCellId(@PathVariable Long cellId) {
        return ResponseEntity.ok(locationService.getLocationsByCellId(cellId));
    }
    
    /**
     * Get all locations in a sector
     * Example: GET /api/locations/sector/1
     */
    @GetMapping("/sector/{sectorId}")
    public ResponseEntity<List<Location>> getLocationsBySectorId(@PathVariable Long sectorId) {
        return ResponseEntity.ok(locationService.getLocationsBySectorId(sectorId));
    }
    
    /**
     * Get all locations in a district
     * Example: GET /api/locations/district/1
     */
    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<Location>> getLocationsByDistrictId(@PathVariable Long districtId) {
        return ResponseEntity.ok(locationService.getLocationsByDistrictId(districtId));
    }
    
    /**
     * Get all locations in a province
     * Example: GET /api/locations/province/1
     */
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<Location>> getLocationsByProvinceId(@PathVariable Long provinceId) {
        return ResponseEntity.ok(locationService.getLocationsByProvinceId(provinceId));
    }
}
