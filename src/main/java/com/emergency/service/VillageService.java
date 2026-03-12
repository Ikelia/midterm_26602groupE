package com.emergency.service;

import com.emergency.entity.Village;
import com.emergency.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageService {
    
    @Autowired
    private VillageRepository villageRepository;
    
    public Village saveVillage(Village village) {
        if (villageRepository.existsByCode(village.getCode())) {
            throw new RuntimeException("Village code already exists: " + village.getCode());
        }
        return villageRepository.save(village);
    }
    
    public List<Village> getAllVillages() {
        return villageRepository.findAll();
    }
    
    public Village getVillageById(Long id) {
        return villageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Village not found with id: " + id));
    }
    
    /**
     * Get all villages in a cell
     */
    public List<Village> getVillagesByCellId(Long cellId) {
        return villageRepository.findByCellId(cellId);
    }
    
    /**
     * Get all villages in a sector (through cell)
     */
    public List<Village> getVillagesBySectorId(Long sectorId) {
        return villageRepository.findBySectorId(sectorId);
    }
    
    /**
     * Get all villages in a district (through cell → sector)
     */
    public List<Village> getVillagesByDistrictId(Long districtId) {
        return villageRepository.findByDistrictId(districtId);
    }
    
    /**
     * Get all villages in a province (through cell → sector → district)
     */
    public List<Village> getVillagesByProvinceId(Long provinceId) {
        return villageRepository.findByProvinceId(provinceId);
    }
    
    public Village updateVillage(Long id, Village villageDetails) {
        Village village = getVillageById(id);
        
        if (!village.getCode().equals(villageDetails.getCode()) && 
            villageRepository.existsByCode(villageDetails.getCode())) {
            throw new RuntimeException("Village code already exists: " + villageDetails.getCode());
        }
        
        village.setName(villageDetails.getName());
        village.setCode(villageDetails.getCode());
        if (villageDetails.getCell() != null) {
            village.setCell(villageDetails.getCell());
        }
        
        return villageRepository.save(village);
    }
    
    public void deleteVillage(Long id) {
        Village village = getVillageById(id);
        villageRepository.delete(village);
    }
}
