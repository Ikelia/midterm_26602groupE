package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Province Entity
 * Represents a province/state in the system
 * ONE-TO-MANY relationship with User (One Province has Many Users)
 */
@Entity
@Table(name = "province")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    /**
     * ONE-TO-MANY: One Province can have many Districts
     */
    @OneToMany(mappedBy = "province")
    @JsonIgnore
    private List<District> districts;
}
