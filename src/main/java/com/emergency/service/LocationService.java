package com.emergency.service;

import com.emergency.entity.Location;
import com.emergency.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LocationService - Demonstrates saving Location entity
 * 
 * EXPLANATION:
 * The Location entity stores geographic information including address, latitude, and longitude.
 * When the save() method is called, Spring Data JPA automatically:
 * 1. Generates the SQL INSERT statement
 * 2. Persists the location data to the database
 * 3. Returns the saved entity with the generated ID
 * 
 * The @Transactional annotation ensures data consistency during save operations.
 */
@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    /**
     * Save Location to database
     * JpaRepository.save() handles the INSERT operation automatically
     * IMPORTANT: Location MUST be linked to a Village
     */
    public Location saveLocation(Location location) {
        if (location.getVillage() == null || location.getVillage().getId() == null) {
            throw new RuntimeException("Location must be linked to a Village");
        }
        return locationRepository.save(location);
    }
    
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }
    
    /**
     * Get all locations
     */
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    /**
     * Update location
     */
    public Location updateLocation(Long id, Location locationDetails) {
        Location location = getLocationById(id);
        
        location.setAddress(locationDetails.getAddress());
        location.setLatitude(locationDetails.getLatitude());
        location.setLongitude(locationDetails.getLongitude());
        
        return locationRepository.save(location);
    }
    
    /**
     * Delete location
     */
    public void deleteLocation(Long id) {
        Location location = getLocationById(id);
        locationRepository.delete(location);
    }
    
    /**
     * Get all locations in a village
     */
    public List<Location> getLocationsByVillageId(Long villageId) {
        return locationRepository.findByVillageId(villageId);
    }
    
    /**
     * Get all locations in a cell (through village)
     */
    public List<Location> getLocationsByCellId(Long cellId) {
        return locationRepository.findByCellId(cellId);
    }
    
    /**
     * Get all locations in a sector (through village → cell)
     */
    public List<Location> getLocationsBySectorId(Long sectorId) {
        return locationRepository.findBySectorId(sectorId);
    }
    
    /**
     * Get all locations in a district (through village → cell → sector)
     */
    public List<Location> getLocationsByDistrictId(Long districtId) {
        return locationRepository.findByDistrictId(districtId);
    }
    
    /**
     * Get all locations in a province (through village → cell → sector → district)
     */
    public List<Location> getLocationsByProvinceId(Long provinceId) {
        return locationRepository.findByProvinceId(provinceId);
    }
}
