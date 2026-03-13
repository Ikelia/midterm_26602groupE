package com.emergency.service;

import com.emergency.entity.User;
import com.emergency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Save a new user with existsByEmail() validation
     * Demonstrates the existBy() method requirement
     */
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }
    
    /**
     * Pagination and Sorting Implementation
     * @param page - page number (0-indexed)
     * @param size - number of records per page
     * @param sortBy - field to sort by (default: name)
     * @return Page<User> containing paginated and sorted results
     * 
     * EXPLANATION:
     * - Pageable is used to define pagination parameters
     * - PageRequest.of() creates a Pageable object with page, size, and sort
     * - Sort.by() specifies the field to sort by
     * - This improves performance by limiting database queries to only fetch required records
     */
    public Page<User> getUsersWithPaginationAndSorting(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable);
    }
    
    /**
     * Retrieve all users from a given province using province NAME
     * Demonstrates query method generation from method name
     */
    public List<User> getUsersByProvinceName(String provinceName) {
        return userRepository.findByProvinceName(provinceName);
    }
    
    /**
     * Retrieve all users from a given province using province CODE
     * Demonstrates query method generation from method name
     */
    public List<User> getUsersByProvinceCode(String provinceCode) {
        return userRepository.findByProvinceCode(provinceCode);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    /**
     * Update user
     */
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        
        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDetails.getEmail());
        }
        
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getVillage() != null) {
            user.setVillage(userDetails.getVillage());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    
    /**
     * Get user with full location hierarchy
     */
    public User getUserWithFullHierarchy(Long id) {
        User user = userRepository.findByIdWithFullHierarchy(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
        return user;
    }
}
