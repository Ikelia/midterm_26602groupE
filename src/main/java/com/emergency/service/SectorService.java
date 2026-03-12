package com.emergency.service;

import com.emergency.entity.Sector;
import com.emergency.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {
    
    @Autowired
    private SectorRepository sectorRepository;
    
    public Sector saveSector(Sector sector) {
        if (sectorRepository.existsByCode(sector.getCode())) {
            throw new RuntimeException("Sector code already exists: " + sector.getCode());
        }
        return sectorRepository.save(sector);
    }
    
    public List<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }
    
    public Sector getSectorById(Long id) {
        return sectorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sector not found with id: " + id));
    }
    
    /**
     * Get all sectors in a district
     */
    public List<Sector> getSectorsByDistrictId(Long districtId) {
        return sectorRepository.findByDistrictId(districtId);
    }
    
    public Sector updateSector(Long id, Sector sectorDetails) {
        Sector sector = getSectorById(id);
        
        if (!sector.getCode().equals(sectorDetails.getCode()) && 
            sectorRepository.existsByCode(sectorDetails.getCode())) {
            throw new RuntimeException("Sector code already exists: " + sectorDetails.getCode());
        }
        
        sector.setName(sectorDetails.getName());
        sector.setCode(sectorDetails.getCode());
        if (sectorDetails.getDistrict() != null) {
            sector.setDistrict(sectorDetails.getDistrict());
        }
        
        return sectorRepository.save(sector);
    }
    
    public void deleteSector(Long id) {
        Sector sector = getSectorById(id);
        sectorRepository.delete(sector);
    }
}
