package com.emergency.service;

import com.emergency.entity.District;
import com.emergency.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    
    @Autowired
    private DistrictRepository districtRepository;
    
    public District saveDistrict(District district) {
        if (districtRepository.existsByCode(district.getCode())) {
            throw new RuntimeException("District code already exists: " + district.getCode());
        }
        return districtRepository.save(district);
    }
    
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }
    
    public District getDistrictById(Long id) {
        return districtRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("District not found with id: " + id));
    }
    
    /**
     * Get all districts in a province
     */
    public List<District> getDistrictsByProvinceId(Long provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }
    
    public District updateDistrict(Long id, District districtDetails) {
        District district = getDistrictById(id);
        
        if (!district.getCode().equals(districtDetails.getCode()) && 
            districtRepository.existsByCode(districtDetails.getCode())) {
            throw new RuntimeException("District code already exists: " + districtDetails.getCode());
        }
        
        district.setName(districtDetails.getName());
        district.setCode(districtDetails.getCode());
        if (districtDetails.getProvince() != null) {
            district.setProvince(districtDetails.getProvince());
        }
        
        return districtRepository.save(district);
    }
    
    public void deleteDistrict(Long id) {
        District district = getDistrictById(id);
        districtRepository.delete(district);
    }
}
