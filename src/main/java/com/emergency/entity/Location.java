package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location Entity
 * Stores geographic information for incidents
 * MUST be linked to a Village (which connects to Cell → Sector → District → Province)
 * ONE-TO-ONE relationship with Incident
 * MANY-TO-ONE relationship with Village
 */
@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    /**
     * MANY-TO-ONE: Many Locations belong to One Village
     * This is REQUIRED - a location MUST be linked to a village
     */
    @ManyToOne
    @JoinColumn(name = "village_id", nullable = false)
    @JsonIgnoreProperties({"locations", "users", "cell"})
    private Village village;
    
    /**
     * ONE-TO-ONE: Each Location is linked to one Incident
     * mappedBy indicates that Incident entity owns the relationship
     */
    @OneToOne(mappedBy = "location")
    private Incident incident;
}
