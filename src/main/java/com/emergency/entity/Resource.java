package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resource Entity
 * Represents emergency resources (ambulance, fire truck, police, etc.)
 * MANY-TO-MANY relationship with Incident
 */
@Entity
@Table(name = "resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String type;
    
    @Column
    private Integer quantity;
    
    /**
     * MANY-TO-MANY: One Resource can be allocated to many Incidents
     * mappedBy indicates that Incident entity owns the relationship
     */
    @ManyToMany(mappedBy = "resources")
    @JsonIgnore
    private List<Incident> incidents;
}
