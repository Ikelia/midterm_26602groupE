# Emergency Resource Allocation and Incident Reporting System

## Project Overview
A Spring Boot backend system for managing emergency incidents, resources, and user reporting with comprehensive hierarchical location structure and database relationships.

## Database Configuration
- **Database Name**: emergency_db
- **Username**: postgres
- **Password**: Admin123
- **Port**: 5432
- **Database**: PostgreSQL

## Entity Relationship Diagram (ERD)

### Tables (10 Total):

1. **Province** (id, name, code)
2. **District** (id, name, code, province_id)
3. **Sector** (id, name, code, district_id)
4. **Cell** (id, name, code, sector_id)
5. **Village** (id, name, code, cell_id)
6. **Location** (id, address, latitude, longitude, village_id)
7. **User** (id, name, email, village_id)
8. **Incident** (id, title, description, user_id, location_id, reportedAt)
9. **Resource** (id, name, type, quantity)
10. **Incident_Resource** (incident_id, resource_id) - Join Table

### Hierarchical Location Structure:
```
Province → District → Sector → Cell → Village → Location
                                        ↓
                                      User
```

### Key Relationships:
- **Village → User**: ONE-TO-MANY (Users linked to Village, NOT Province directly)
- **User → Incident**: ONE-TO-MANY (One user reports many incidents)
- **Incident → Location**: ONE-TO-ONE (Each incident has one location)
- **Incident ↔ Resource**: MANY-TO-MANY (Incidents require multiple resources)
- **Administrative Hierarchy**: Province → District → Sector → Cell → Village

## Running the Application

```bash
mvn spring-boot:run
```

Server runs on: http://localhost:8080

## API Endpoints

### Administrative Hierarchy Endpoints
- POST /api/provinces - Create province
- POST /api/districts - Create district
- POST /api/sectors - Create sector
- POST /api/cells - Create cell
- POST /api/villages - Create village

### User Endpoints
- POST /api/users - Create user (with village_id)
- GET /api/users/paginated?page=0&size=10&sortBy=name - Paginated users
- GET /api/users/province/name/{provinceName} - Users by province name
- GET /api/users/province/code/{provinceCode} - Users by province code
- PUT /api/users/{id} - Update user
- DELETE /api/users/{id} - Delete user

### Location Endpoints
- POST /api/locations - Create location (with village validation)
- GET /api/locations/{id} - Get location by ID

### Incident Endpoints
- POST /api/incidents - Create incident
- GET /api/incidents/paginated?page=0&size=5&sortBy=reportedAt - Paginated incidents
- PUT /api/incidents/{id} - Update incident
- DELETE /api/incidents/{id} - Delete incident

### Resource Endpoints
- POST /api/resources - Create resource
- GET /api/resources/{id} - Get resource by ID
- PUT /api/resources/{id} - Update resource
- DELETE /api/resources/{id} - Delete resource

## Assessment Criteria Implementation

### 1. ERD with 5+ Tables (3 Marks) ✓
**Tables**: 10 tables total (Province, District, Sector, Cell, Village, Location, User, Incident, Resource, Incident_Resource)
**Relationships**: Complete hierarchical structure with proper foreign keys

### 2. Saving Location (2 Marks) ✓
**Implementation**: LocationService validates village exists before saving
**Explanation**: Uses village_id foreign key, ensuring location is linked to administrative hierarchy

### 3. Sorting & Pagination (5 Marks) ✓
**Implementation**: Pageable with PageRequest.of(page, size, Sort.by())
**Explanation**: Improves performance by limiting database queries to required records only

### 4. Many-to-Many Relationship (3 Marks) ✓
**Implementation**: Incident ↔ Resource via incident_resource join table
**Explanation**: @ManyToMany with @JoinTable creates automatic join table management

### 5. One-to-Many Relationship (2 Marks) ✓
**Implementation**: Multiple (Province→District, District→Sector, Village→User, etc.)
**Explanation**: @ManyToOne creates foreign key in child table

### 6. One-to-One Relationship (2 Marks) ✓
**Implementation**: Incident → Location
**Explanation**: @OneToOne with @JoinColumn creates unique foreign key constraint

### 7. existBy() Method (2 Marks) ✓
**Implementation**: UserRepository.existsByEmail(), VillageService.existsByCode()
**Explanation**: Spring Data JPA generates COUNT query for existence validation

### 8. Retrieve Users by Province (4 Marks) ✓
**Implementation**: findByProvinceName() and findByProvinceCode()
**Explanation**: Uses @Query to traverse User→Village→Cell→Sector→District→Province hierarchy
```java
@Query("SELECT u FROM User u WHERE u.village.cell.sector.district.province.code = :provinceCode")
```
