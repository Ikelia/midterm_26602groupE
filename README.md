<<<<<<< HEAD
# Emergency Resource Allocation and Incident Reporting System

## Project Overview
A Spring Boot backend system for managing emergency incidents, resources, and user reporting with comprehensive database relationships.

## Database Configuration
- **Database Name**: emergency_db
- **Username**: root
- **Password**: Admin123
- **Port**: 3306

## Entity Relationship Diagram (ERD)

### Tables and Relationships:

1. **Province** (id, name, code)
2. **User** (id, name, email, province_id)
3. **Location** (id, address, latitude, longitude)
4. **Incident** (id, title, description, user_id, location_id, reportedAt)
5. **Resource** (id, name, type, quantity)
6. **Incident_Resource** (incident_id, resource_id) - Join Table

### Relationships:
- **Province → User**: ONE-TO-MANY (One province has many users)
- **User → Incident**: ONE-TO-MANY (One user reports many incidents)
- **Incident → Location**: ONE-TO-ONE (Each incident has one location)
- **Incident ↔ Resource**: MANY-TO-MANY (Incidents require multiple resources)

## Running the Application

```bash
mvn spring-boot:run
```

Server runs on: http://localhost:8080

## API Endpoints

### Province Endpoints
- POST /api/provinces - Create province
- GET /api/provinces - Get all provinces
- GET /api/provinces/{id} - Get province by ID

### User Endpoints
- POST /api/users - Create user
- GET /api/users - Get all users
- GET /api/users/paginated?page=0&size=10&sortBy=name - Paginated users
- GET /api/users/province/name/{provinceName} - Users by province name
- GET /api/users/province/code/{provinceCode} - Users by province code

### Location Endpoints
- POST /api/locations - Create location
- GET /api/locations/{id} - Get location by ID

### Incident Endpoints
- POST /api/incidents - Create incident
- GET /api/incidents - Get all incidents
- GET /api/incidents/{id} - Get incident by ID
- GET /api/incidents/paginated?page=0&size=5&sortBy=reportedAt - Paginated incidents

### Resource Endpoints
- POST /api/resources - Create resource
- GET /api/resources - Get all resources
- GET /api/resources/{id} - Get resource by ID

## Assessment Criteria Implementation

### 1. ERD with 5 Tables (3 Marks) ✓
**Tables**: Province, User, Location, Incident, Resource, Incident_Resource
**Relationships**: Clearly defined with proper foreign keys and join tables

### 2. Saving Location (2 Marks) ✓
**Implementation**: LocationService.saveLocation()
**Explanation**: Uses JpaRepository.save() which automatically generates INSERT SQL and persists data

### 3. Sorting & Pagination (5 Marks) ✓
**Implementation**: UserService.getUsersWithPaginationAndSorting()
**Explanation**: 
- Uses Pageable with PageRequest.of(page, size, Sort.by())
- Limits records per request for better performance
- Returns Page object with metadata (total pages, total elements)

### 4. Many-to-Many Relationship (3 Marks) ✓
**Implementation**: Incident ↔ Resource
**Explanation**: 
- @ManyToMany annotation with @JoinTable
- Creates incident_resource join table
- Columns: incident_id, resource_id

### 5. One-to-Many Relationship (2 Marks) ✓
**Implementation**: Province → User
**Explanation**: 
- @OneToMany in Province, @ManyToOne in User
- Foreign key province_id in users table

### 6. One-to-One Relationship (2 Marks) ✓
**Implementation**: Incident → Location
**Explanation**: 
- @OneToOne annotation with @JoinColumn
- Foreign key location_id in incident table

### 7. existBy() Method (2 Marks) ✓
**Implementation**: UserRepository.existsByEmail()
**Explanation**: 
- Spring Data JPA generates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
- Returns boolean for existence check

### 8. Retrieve Users by Province (4 Marks) ✓
**Implementation**: 
- findByProvinceName(String name)
- findByProvinceCode(String code)
**Explanation**: 
- Spring Data JPA auto-generates JOIN queries
- Queries users table joined with province table
=======
# midterm_26602groupE
>>>>>>> 216df8c5e045b89740e527ff29add0f28693cb8c
