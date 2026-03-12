package com.emergency.controller;

import com.emergency.entity.User;
import com.emergency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    /**
     * Pagination and Sorting endpoint
     * Example: GET /api/users/paginated?page=0&size=10&sortBy=name
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<User>> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(userService.getUsersWithPaginationAndSorting(page, size, sortBy));
    }
    
    /**
     * Get users by province name
     * Example: GET /api/users/province/name/Gauteng
     */
    @GetMapping("/province/name/{provinceName}")
    public ResponseEntity<List<User>> getUsersByProvinceName(@PathVariable String provinceName) {
        return ResponseEntity.ok(userService.getUsersByProvinceName(provinceName));
    }
    
    /**
     * Get users by province code
     * Example: GET /api/users/province/code/GP
     */
    @GetMapping("/province/code/{provinceCode}")
    public ResponseEntity<List<User>> getUsersByProvinceCode(@PathVariable String provinceCode) {
        return ResponseEntity.ok(userService.getUsersByProvinceCode(provinceCode));
    }
    
    /**
     * Update user by ID
     * Example: PUT /api/users/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
    
    /**
     * Delete user by ID
     * Example: DELETE /api/users/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    
    /**
     * Get user by ID
     * Example: GET /api/users/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
