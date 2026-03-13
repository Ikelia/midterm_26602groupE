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
**Implementation**: 10 tables total (Province, District, Sector, Cell, Village, Location, User, Incident, Resource, Incident_Resource)

**Explanation**: The database uses a hierarchical administrative structure for Rwanda's location system. Each table has a primary key (id) and foreign keys to establish relationships. The hierarchy flows from Province → District → Sector → Cell → Village, with Users and Locations linked to Villages. This design allows efficient querying at any administrative level while maintaining data integrity through foreign key constraints.

**Relationships**: 
- Administrative hierarchy uses cascading ONE-TO-MANY relationships
- Users are linked to Villages (not directly to Provinces), enabling automatic traversal up the hierarchy
- Incidents connect Users, Locations, and Resources through various relationship types

### 2. Saving Location (2 Marks) ✓
**Implementation**: LocationService.saveLocation() with village validation

**Code Reference**: `src/main/java/com/emergency/service/LocationService.java`

**Explanation**: 
```java
public Location saveLocation(Location location) {
    // Validate that village exists before saving
    if (!villageService.existsById(location.getVillage().getId())) {
        throw new RuntimeException("Village not found");
    }
    return locationRepository.save(location);
}
```

**How it works**:
1. Client sends POST request with location data including village_id
2. Service validates that the village exists using existsById()
3. If valid, Hibernate persists the Location entity to the database
4. Foreign key constraint ensures data integrity (village_id must reference existing village)
5. The location is automatically linked to the administrative hierarchy through the village

**Relationship Handling**: The @ManyToOne annotation on Location.village creates a foreign key column (village_id) in the location table. When saving, Hibernate automatically manages the relationship by storing the village's ID, not the entire village object.

### 3. Sorting & Pagination (5 Marks) ✓
**Implementation**: Pageable interface with PageRequest and Sort

**Code Reference**: `src/main/java/com/emergency/service/UserService.java`

**Sorting Explanation**:
```java
Sort.by(sortBy).ascending()
```
- Spring Data JPA's Sort class specifies which field to order by
- Can sort by any entity field (name, email, reportedAt, etc.)
- Translates to SQL ORDER BY clause
- Supports ascending/descending order

**Pagination Explanation**:
```java
Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
return userRepository.findAll(pageable);
```

**How Pagination Works**:
1. **PageRequest.of(page, size, sort)** creates a Pageable object with:
   - page: Zero-indexed page number (page 0 = first page)
   - size: Number of records per page
   - sort: Sorting criteria

2. **Repository Method**: findAll(Pageable) returns Page<Entity> containing:
   - content: List of entities for current page
   - totalElements: Total records in database
   - totalPages: Total number of pages
   - number: Current page number
   - size: Page size

3. **SQL Translation**: Hibernate converts Pageable to SQL LIMIT and OFFSET:
   ```sql
   SELECT * FROM users ORDER BY name LIMIT 10 OFFSET 0
   ```

**Performance Benefits**:
- Reduces memory usage by loading only required records
- Decreases database query time (fetching 10 records vs 10,000)
- Improves network transfer speed (smaller JSON response)
- Better user experience (faster page loads)
- Scalable for large datasets

### 4. Many-to-Many Relationship (3 Marks) ✓
**Implementation**: Incident ↔ Resource via incident_resource join table

**Code Reference**: `src/main/java/com/emergency/entity/Incident.java`

**Explanation**:
```java
@ManyToMany
@JoinTable(
    name = "incident_resource",
    joinColumns = @JoinColumn(name = "incident_id"),
    inverseJoinColumns = @JoinColumn(name = "resource_id")
)
private List<Resource> resources;
```

**Join Table Structure**:
- **Table Name**: incident_resource
- **Columns**: 
  - incident_id (foreign key to incident table)
  - resource_id (foreign key to resource table)
- **Composite Primary Key**: (incident_id, resource_id)

**How it Works**:
1. One incident can require multiple resources (ambulance, medical kit, etc.)
2. One resource can be used in multiple incidents
3. @JoinTable creates the intermediate table automatically
4. Hibernate manages insertions/deletions in the join table
5. When saving an incident with resources, Hibernate:
   - Saves the incident to incident table
   - Saves each resource to resource table (if new)
   - Creates entries in incident_resource join table linking them

**Mapping Logic**: The @ManyToMany annotation tells Hibernate to create a separate table to store the relationships, avoiding data duplication and maintaining referential integrity.

### 5. One-to-Many Relationship (2 Marks) ✓
**Implementation**: Multiple examples (Province→District, Village→User, User→Incident)

**Code Reference**: `src/main/java/com/emergency/entity/User.java` and `src/main/java/com/emergency/entity/Village.java`

**Explanation**:
```java
// In User entity (Many side)
@ManyToOne
@JoinColumn(name = "village_id", nullable = false)
private Village village;

// In Village entity (One side)
@OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
private List<User> users;
```

**Relationship Mapping**:
- **@ManyToOne**: Placed on the "many" side (User), creates foreign key column
- **@JoinColumn**: Specifies the foreign key column name (village_id)
- **@OneToMany**: Placed on the "one" side (Village), references the field in User
- **mappedBy**: Indicates that User.village owns the relationship

**Foreign Key Usage**:
1. The users table has a village_id column (foreign key)
2. This column references the id column in the village table
3. Database enforces referential integrity (can't delete village if users exist)
4. Hibernate automatically manages the relationship when saving/updating

**Example**: When creating a user, you only provide village_id. Hibernate:
- Validates the village exists
- Stores the village_id in the users table
- Maintains the bidirectional relationship

### 6. One-to-One Relationship (2 Marks) ✓
**Implementation**: Incident → Location

**Code Reference**: `src/main/java/com/emergency/entity/Incident.java` and `src/main/java/com/emergency/entity/Location.java`

**Explanation**:
```java
// In Incident entity (owning side)
@OneToOne(cascade = {CascadeType.MERGE})
@JoinColumn(name = "location_id", referencedColumnName = "id")
private Location location;

// In Location entity (inverse side)
@OneToOne(mappedBy = "location")
private Incident incident;
```

**How Entities are Connected**:
1. **Owning Side**: Incident has the foreign key (location_id column)
2. **@JoinColumn**: Creates location_id column in incident table
3. **referencedColumnName**: Specifies which column in location table to reference (id)
4. **Unique Constraint**: Database ensures one location can only be linked to one incident
5. **mappedBy**: Location.incident indicates Incident owns the relationship

**Cascade Behavior**: CascadeType.MERGE allows updating existing locations when updating incidents, but doesn't automatically delete locations when incidents are deleted.

**Use Case**: Each emergency incident occurs at exactly one location, and each location is associated with one specific incident report.

### 7. existBy() Method (2 Marks) ✓
**Implementation**: UserRepository.existsByEmail() and VillageService.existsByCode()

**Code Reference**: `src/main/java/com/emergency/repository/UserRepository.java`

**Explanation**:
```java
boolean existsByEmail(String email);
```

**How Existence Checking Works**:
1. **Method Naming Convention**: Spring Data JPA parses the method name
   - "existsBy" → generates existence check query
   - "Email" → field name to check
   
2. **Generated SQL**: Hibernate creates an optimized COUNT query:
   ```sql
   SELECT COUNT(*) > 0 FROM users WHERE email = ?
   ```

3. **Return Value**: Returns boolean (true if exists, false if not)

4. **Performance**: More efficient than findByEmail() because:
   - Doesn't load entire entity into memory
   - Uses COUNT(*) instead of SELECT *
   - Returns immediately after finding first match

**Usage in Code**:
```java
if (userRepository.existsByEmail(user.getEmail())) {
    throw new RuntimeException("User already exists");
}
```

**Benefits**: Prevents duplicate entries, validates data before insertion, and improves data integrity.

### 8. Retrieve Users by Province (4 Marks) ✓
**Implementation**: findByProvinceName() and findByProvinceCode() with @Query

**Code Reference**: `src/main/java/com/emergency/repository/UserRepository.java`

**Explanation**:
```java
@Query("SELECT u FROM User u WHERE u.village.cell.sector.district.province.code = :provinceCode")
List<User> findByProvinceCode(@Param("provinceCode") String provinceCode);
```

**Query Logic**:
1. **JPQL (Java Persistence Query Language)**: Object-oriented query language
2. **Dot Notation Traversal**: Navigates through relationships
   - u.village → User's village
   - .cell → Village's cell
   - .sector → Cell's sector
   - .district → Sector's district
   - .province → District's province
   - .code → Province's code

3. **Hibernate Translation**: Converts JPQL to SQL with JOINs:
   ```sql
   SELECT u.* FROM users u
   JOIN village v ON u.village_id = v.id
   JOIN cell c ON v.cell_id = c.id
   JOIN sector s ON c.sector_id = s.id
   JOIN district d ON s.district_id = d.id
   JOIN province p ON d.province_id = p.id
   WHERE p.code = ?
   ```

4. **@Param Annotation**: Binds method parameter to query parameter

**Repository Method Logic**:
- Spring Data JPA automatically implements the query
- Returns List<User> containing all users in the specified province
- Leverages the hierarchical structure (User → Village → Cell → Sector → District → Province)

**Why This Works**: Users are linked to Villages (not Provinces directly), but the hierarchical relationships allow automatic traversal up the administrative structure to filter by province. This design is efficient and maintains data normalization.
