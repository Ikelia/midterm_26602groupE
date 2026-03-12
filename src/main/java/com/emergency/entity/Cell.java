package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Cell Entity
 * MANY-TO-ONE relationship with Sector (Many Cells belong to One Sector)
 * ONE-TO-MANY relationship with Village (One Cell has Many Villages)
 */
@Entity
@Table(name = "cell")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cell {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    /**
     * MANY-TO-ONE: Many Cells belong to One Sector
     */
    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;
    
    /**
     * ONE-TO-MANY: One Cell has Many Villages
     */
    @OneToMany(mappedBy = "cell", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Village> villages;
}
