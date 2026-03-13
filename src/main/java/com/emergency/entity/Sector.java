package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Sector Entity
 * MANY-TO-ONE relationship with District (Many Sectors belong to One District)
 * ONE-TO-MANY relationship with Cell (One Sector has Many Cells)
 */
@Entity
@Table(name = "sector")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sector {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    /**
     * MANY-TO-ONE: Many Sectors belong to One District
     */
    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @JsonIgnoreProperties({"sectors", "province"})
    private District district;
    
    /**
     * ONE-TO-MANY: One Sector has Many Cells
     */
    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cell> cells;
}
