package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Village Entity
 * MANY-TO-ONE relationship with Cell (Many Villages belong to One Cell)
 * ONE-TO-MANY relationship with Location (One Village has Many Locations)
 */
@Entity
@Table(name = "village")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Village {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    /**
     * MANY-TO-ONE: Many Villages belong to One Cell
     */
    @ManyToOne
    @JoinColumn(name = "cell_id", nullable = false)
    private Cell cell;
    
    /**
     * ONE-TO-MANY: One Village has Many Locations
     */
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Location> locations;
    
    /**
     * ONE-TO-MANY: One Village has Many Users
     */
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users;
}
