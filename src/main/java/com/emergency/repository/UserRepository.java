package com.emergency.repository;

import com.emergency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * existsByEmail() - Check if user exists by email
     * Spring Data JPA automatically generates the query:
     * SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);
    
    /**
     * Retrieve all users from a given province using province NAME
     * Spring Data JPA generates: 
     * SELECT * FROM users u JOIN province p ON u.province_id = p.id WHERE p.name = ?
     */
    List<User> findByProvinceName(String provinceName);
    
    /**
     * Retrieve all users from a given province using province CODE
     * Spring Data JPA generates:
     * SELECT * FROM users u JOIN province p ON u.province_id = p.id WHERE p.code = ?
     */
    List<User> findByProvinceCode(String provinceCode);
    
    /**
     * Pagination and Sorting support
     * Returns a Page object containing users with pagination metadata
     */
    Page<User> findAll(Pageable pageable);
}
