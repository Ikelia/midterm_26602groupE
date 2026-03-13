package com.emergency.service;

import com.emergency.entity.Resource;
import com.emergency.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    public Resource saveResource(Resource resource) {
        if (resourceRepository.existsByName(resource.getName())) {
            throw new RuntimeException("Resource already exists");
        }
        return resourceRepository.save(resource);
    }
    
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
    
    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }
    
    /**
     * Get paginated resources
     */
    public Page<Resource> getPaginatedResources(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }
    
    /**
     * Update resource
     */
    public Resource updateResource(Long id, Resource resourceDetails) {
        Resource resource = getResourceById(id);
        
        // Check if name is being changed and if it already exists
        if (!resource.getName().equals(resourceDetails.getName()) && 
            resourceRepository.existsByName(resourceDetails.getName())) {
            throw new RuntimeException("Resource already exists: " + resourceDetails.getName());
        }
        
        resource.setName(resourceDetails.getName());
        resource.setType(resourceDetails.getType());
        resource.setQuantity(resourceDetails.getQuantity());
        
        return resourceRepository.save(resource);
    }
    
    /**
     * Delete resource
     */
    public void deleteResource(Long id) {
        Resource resource = getResourceById(id);
        resourceRepository.delete(resource);
    }
}
