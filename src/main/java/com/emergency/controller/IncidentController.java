package com.emergency.controller;

import com.emergency.entity.Incident;
import com.emergency.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentController {
    
    @Autowired
    private IncidentService incidentService;
    
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.saveIncident(incident));
    }
    
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }
    
    /**
     * Pagination and Sorting endpoint for incidents
     * Example: GET /api/incidents/paginated?page=0&size=5&sortBy=reportedAt
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<Incident>> getIncidentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "reportedAt") String sortBy) {
        return ResponseEntity.ok(incidentService.getIncidentsWithPagination(page, size, sortBy));
    }
    
    /**
     * Update incident by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }
    
    /**
     * Delete incident by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.ok("Incident deleted successfully");
    }
}
