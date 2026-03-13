package com.emergency.repository;

import com.emergency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * existsByEmail() - Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Retrieve all users from a given village
     */
    List<User> findByVillageId(Long villageId);
    
    /**
     * Retrieve all users from a given province using province NAME
     * Traverses: User → Village → Cell → Sector → District → Province
     */
    @Query("SELECT u FROM User u WHERE u.village.cell.sector.district.province.name = :provinceName")
    List<User> findByProvinceName(@Param("provinceName") String provinceName);
    
    /**
     * Retrieve all users from a given province using province CODE
     * Traverses: User → Village → Cell → Sector → District → Province
     */
    @Query("SELECT u FROM User u WHERE u.village.cell.sector.district.province.code = :provinceCode")
    List<User> findByProvinceCode(@Param("provinceCode") String provinceCode);
    
    /**
     * Pagination and Sorting support
     */
    Page<User> findAll(Pageable pageable);
    
    /**
     * Fetch user with all location hierarchy eagerly loaded
     */
    @Query("SELECT u FROM User u " +
           "JOIN FETCH u.village v " +
           "JOIN FETCH v.cell c " +
           "JOIN FETCH c.sector s " +
           "JOIN FETCH s.district d " +
           "JOIN FETCH d.province p " +
           "WHERE u.id = :userId")
    User findByIdWithFullHierarchy(@Param("userId") Long userId);
}
