package com.emergency.service;

import com.emergency.entity.Incident;
import com.emergency.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    /**
     * Save incident with all relationships
     * Demonstrates ONE-TO-ONE (Location) and MANY-TO-MANY (Resources)
     */
    public Incident saveIncident(Incident incident) {
        return incidentRepository.save(incident);
    }
    
    /**
     * Pagination and Sorting for incidents
     */
    public Page<Incident> getIncidentsWithPagination(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return incidentRepository.findAll(pageable);
    }
    
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }
    
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
    }
    
    /**
     * Update incident
     */
    public Incident updateIncident(Long id, Incident incidentDetails) {
        Incident incident = getIncidentById(id);
        
        incident.setTitle(incidentDetails.getTitle());
        incident.setDescription(incidentDetails.getDescription());
        
        if (incidentDetails.getLocation() != null) {
            incident.setLocation(incidentDetails.getLocation());
        }
        
        if (incidentDetails.getUser() != null) {
            incident.setUser(incidentDetails.getUser());
        }
        
        if (incidentDetails.getResources() != null) {
            incident.setResources(incidentDetails.getResources());
        }
        
        return incidentRepository.save(incident);
    }
    
    /**
     * Delete incident
     */
    public void deleteIncident(Long id) {
        Incident incident = getIncidentById(id);
        incidentRepository.delete(incident);
    }
}
