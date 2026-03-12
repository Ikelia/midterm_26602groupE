package com.emergency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Incident Entity
 * Represents emergency incidents reported by users
 * MANY-TO-ONE with User
 * ONE-TO-ONE with Location
 * MANY-TO-MANY with Resource
 */
@Entity
@Table(name = "incident")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();
    
    /**
     * MANY-TO-ONE: Many Incidents can be reported by One User
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * ONE-TO-ONE: Each Incident has exactly one Location
     * @JoinColumn creates foreign key "location_id" in incident table
     * Using MERGE instead of ALL to handle existing locations
     */
    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    
    /**
     * MANY-TO-MANY: One Incident can require many Resources
     * and one Resource can be used in many Incidents
     * @JoinTable creates the join table "incident_resource"
     * with columns: incident_id and resource_id
     */
    @ManyToMany
    @JoinTable(
        name = "incident_resource",
        joinColumns = @JoinColumn(name = "incident_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    private List<Resource> resources;
}
