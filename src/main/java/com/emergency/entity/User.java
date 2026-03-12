package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * User Entity
 * Represents users in the emergency system
 * MANY-TO-ONE relationship with Village (NOT Province directly!)
 * ONE-TO-MANY relationship with Incident
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    /**
     * MANY-TO-ONE: Many Users belong to One Village
     * User is linked to Village, which automatically connects to:
     * Village → Cell → Sector → District → Province
     * This allows retrieving users by any administrative level
     */
    @ManyToOne
    @JoinColumn(name = "village_id", nullable = false)
    private Village village;
    
    /**
     * ONE-TO-MANY: One User can report many Incidents
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Incident> incidents;
}
