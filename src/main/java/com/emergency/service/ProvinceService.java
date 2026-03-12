package com.emergency.service;

import com.emergency.entity.Province;
import com.emergency.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    
    @Autowired
    private ProvinceRepository provinceRepository;
    
    public Province saveProvince(Province province) {
        if (provinceRepository.existsByCode(province.getCode())) {
            throw new RuntimeException("Province with code " + province.getCode() + " already exists");
        }
        return provinceRepository.save(province);
    }
    
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
    
    public Province getProvinceById(Long id) {
        return provinceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Province not found with id: " + id));
    }
    
    /**
     * Update province
     */
    public Province updateProvince(Long id, Province provinceDetails) {
        Province province = getProvinceById(id);
        
        // Update name (always)
        province.setName(provinceDetails.getName());
        
        // Only check and update code if it's actually different
        String existingCode = province.getCode().trim();
        String newCode = provinceDetails.getCode().trim();
        
        if (!existingCode.equalsIgnoreCase(newCode)) {
            // Code is changing, check if new code already exists
            if (provinceRepository.existsByCode(newCode)) {
                throw new RuntimeException("Province code already exists: " + newCode);
            }
            province.setCode(newCode);
        }
        
        return provinceRepository.save(province);
    }
    
    /**
     * Delete province
     */
    public void deleteProvince(Long id) {
        Province province = getProvinceById(id);
        provinceRepository.delete(province);
    }
}
