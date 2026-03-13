package com.emergency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * District Entity
 * ONE-TO-MANY relationship with Province (Many Districts belong to One Province)
 * ONE-TO-MANY relationship with Sector (One District has Many Sectors)
 */
@Entity
@Table(name = "district")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    /**
     * MANY-TO-ONE: Many Districts belong to One Province
     */
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    @JsonIgnoreProperties({"districts"})
    private Province province;
    
    /**
     * ONE-TO-MANY: One District has Many Sectors
     */
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sector> sectors;
}
