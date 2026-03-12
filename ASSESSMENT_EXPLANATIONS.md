# Emergency Resource Allocation System - Assessment Explanations

## Complete Implementation Guide for 30 Marks Assessment

---

## 1. Entity Relationship Diagram (ERD) with 5+ Tables (3 Marks)

### Implementation
We implemented **11 tables** demonstrating complex relationships:

#### Administrative Hierarchy (6 tables):
1. **Province** - Top-level administrative division
2. **District** - Belongs to Province
3. **Sector** - Belongs to District
4. **Cell** - Belongs to Sector
5. **Village** - Belongs to Cell
6. **Location** - Geographic coordinates, belongs to Village

#### Core System (4 tables):
7. **User** - System users, linked to Village
8. **Incident** - Emergency reports
9. **Resource** - Emergency resources (ambulances, equipment)
10. **Incident_Resource** - Join table for Many-to-Many

### Relationship Logic

```
Province (1) ──→ (Many) District
District (1) ──→ (Many) Sector
Sector (1) ──→ (Many) Cell
Cell (1) ──→ (Many) Village
Village (1) ──→ (Many) Location
Village (1) ──→ (Many) User
User (1) ──→ (Many) Incident
Incident (1) ──→ (1) Location
Incident (Many) ←→ (Many) Resource
```

### Key Design Decision
**Users are linked to Village, NOT Province directly**. This enables:
- Automatic province resolution through hierarchy traversal
- Flexible querying at any administrative level
- Proper data normalization

### Code Example (User Entity):
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    
    // User linked to Village (not Province!)
    @ManyToOne
    @JoinColumn(name = "village_id", nullable = false)
    private Village village;
}
```

---

## 2. Implementation of Saving Location (2 Marks)

### How Data is Stored

**LocationService.java:**
```java
public Location saveLocation(Location location) {
    // Validation: Location MUST be linked to a Village
    if (location.getVillage() == null || location.getVillage().getId() == null) {
        throw new RuntimeException("Location must be linked to a Village");
    }
    return locationRepository.save(location);
}
```

### Explanation of Storage Process

1. **Validation**: Before saving, we check that the location has a valid village_id
2. **JpaRepository.save()**: Spring Data JPA automatically:
   - Generates SQL INSERT statement
   - Persists data to PostgreSQL database
   - Creates foreign key relationship to village table
   - Returns saved entity with generated ID

3. **Database Operation**:
```sql
INSERT INTO location (address, latitude, longitude, village_id) 
VALUES ('KN 5 Ave, Kigali', -1.9441, 30.0619, 1);
```

### Relationship Handling

The `village_id` foreign key creates a relationship that allows:
- Querying all locations in a village
- Traversing up the hierarchy (Location → Village → Cell → Sector → District → Province)
- Maintaining referential integrity (can't delete village if locations exist)

### API Request Example:
```json
POST /api/locations
{
  "address": "KN 5 Ave, Remera, Kigali",
  "latitude": -1.9441,
  "longitude": 30.0619,
  "village": {"id": 1}
}
```

---

## 3. Sorting and Pagination Implementation (5 Marks)

### Sorting Implementation

**UserService.java:**
```java
public Page<User> getUsersWithPaginationAndSorting(int page, int size, String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
    return userRepository.findAll(pageable);
}
```

### How Sorting Works

1. **Sort.by(sortBy)**: Creates a Sort object specifying which field to sort by
2. **ascending()**: Defines sort direction (can also use descending())
3. **Spring Data JPA** automatically generates:
```sql
SELECT * FROM users ORDER BY name ASC LIMIT 10 OFFSET 0;
```

### Pagination Implementation

**Components:**
- **page**: Page number (0-indexed)
- **size**: Number of records per page
- **PageRequest.of()**: Creates Pageable object combining page, size, and sort

### How Pagination Improves Performance

1. **Reduced Memory Usage**: Only loads required records into memory
   - Without pagination: Loading 10,000 users = 10,000 objects in memory
   - With pagination (size=10): Only 10 objects in memory

2. **Faster Database Queries**: Uses SQL LIMIT and OFFSET
   ```sql
   -- Page 0, Size 10
   SELECT * FROM users ORDER BY name LIMIT 10 OFFSET 0;
   
   -- Page 1, Size 10
   SELECT * FROM users ORDER BY name LIMIT 10 OFFSET 10;
   ```

3. **Better User Experience**: 
   - Faster page load times
   - Reduced network bandwidth
   - Smoother UI rendering

### API Request:
```
GET /api/users/paginated?page=0&size=10&sortBy=name
```

### Response Structure:
```json
{
  "content": [/* array of users */],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {"sorted": true}
  },
  "totalPages": 5,
  "totalElements": 47,
  "size": 10,
  "number": 0
}
```

**Metadata Explanation:**
- `totalElements`: Total records in database (47 users)
- `totalPages`: Total pages available (47 ÷ 10 = 5 pages)
- `number`: Current page (0)
- `size`: Records per page (10)

---

## 4. Many-to-Many Relationship (3 Marks)

### Implementation: Incident ↔ Resource

**Incident Entity:**
```java
@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany
    @JoinTable(
        name = "incident_resource",
        joinColumns = @JoinColumn(name = "incident_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    private List<Resource> resources;
}
```

**Resource Entity:**
```java
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(mappedBy = "resources")
    @JsonIgnore
    private List<Incident> incidents;
}
```

### Join Table Explanation

**incident_resource table structure:**
```sql
CREATE TABLE incident_resource (
    incident_id BIGINT REFERENCES incident(id),
    resource_id BIGINT REFERENCES resource(id),
    PRIMARY KEY (incident_id, resource_id)
);
```

### How the Relationship is Mapped

1. **@JoinTable**: Specifies the join table name and column names
2. **joinColumns**: Foreign key to the owning side (Incident)
3. **inverseJoinColumns**: Foreign key to the inverse side (Resource)
4. **mappedBy**: In Resource entity, indicates Incident owns the relationship

### Real-World Example

One incident can require multiple resources:
- Medical Emergency → Ambulance + Medical Kit + Paramedics

One resource can be used in multiple incidents:
- Ambulance #1 → Used in Incident #5, #12, #23

### Database Result:
```
incident_resource table:
incident_id | resource_id
------------|------------
1           | 1          (Incident 1 uses Ambulance)
1           | 3          (Incident 1 uses Medical Kit)
2           | 2          (Incident 2 uses Fire Truck)
```

### API Request:
```json
POST /api/incidents
{
  "title": "Medical Emergency",
  "user": {"id": 1},
  "location": {"id": 1},
  "resources": [{"id": 1}, {"id": 3}]  // Multiple resources
}
```

---

## 5. One-to-Many Relationship (2 Marks)

### Implementation: Village → User

**Village Entity (One side):**
```java
@Entity
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users;
}
```

**User Entity (Many side):**
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "village_id", nullable = false)
    private Village village;
}
```

### Relationship Mapping Explanation

1. **@OneToMany**: In Village, indicates one village has many users
2. **@ManyToOne**: In User, indicates many users belong to one village
3. **mappedBy = "village"**: Tells JPA that User entity owns the relationship
4. **@JoinColumn**: Creates foreign key column in users table

### Foreign Key Usage

**Database Schema:**
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    village_id BIGINT NOT NULL,
    FOREIGN KEY (village_id) REFERENCES village(id)
);
```

**Foreign Key Benefits:**
- **Referential Integrity**: Can't insert user with non-existent village_id
- **Cascade Operations**: Can configure delete/update behavior
- **Query Optimization**: Database can use indexes on foreign keys

### Real-World Example

Village "Rukiri I" (id=1) has multiple users:
- John Doe (village_id=1)
- Jane Smith (village_id=1)
- Bob Johnson (village_id=1)

### Query Example:
```java
// Get all users in a village
List<User> users = userRepository.findByVillageId(1L);
```

Generated SQL:
```sql
SELECT * FROM users WHERE village_id = 1;
```

---

## 6. One-to-One Relationship (2 Marks)

### Implementation: Incident → Location

**Incident Entity (Owning side):**
```java
@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "location_id", unique = true)
    private Location location;
}
```

**Location Entity (Inverse side):**
```java
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "location")
    private Incident incident;
}
```

### How Entities are Connected

1. **@OneToOne**: Declares one-to-one relationship
2. **@JoinColumn**: Creates foreign key in incident table
3. **unique = true**: Ensures one location can only be linked to one incident
4. **mappedBy**: In Location, indicates Incident owns the relationship

### Database Schema:
```sql
CREATE TABLE incident (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    location_id BIGINT UNIQUE,
    FOREIGN KEY (location_id) REFERENCES location(id)
);
```

### Why One-to-One?

Each incident occurs at exactly one location, and each location is associated with exactly one incident report. This prevents:
- Multiple incidents sharing the same location record
- Confusion about which incident occurred where

### Real-World Example

Incident #1 "Medical Emergency" → Location #1 (KN 5 Ave, -1.9441, 30.0619)
- No other incident can use Location #1
- Location #1 is exclusively linked to Incident #1

### Query Behavior:
```java
Incident incident = incidentRepository.findById(1L);
Location location = incident.getLocation(); // Automatic join
```

Generated SQL:
```sql
SELECT i.*, l.* 
FROM incident i 
LEFT JOIN location l ON i.location_id = l.id 
WHERE i.id = 1;
```

---

## 7. existBy() Method Implementation (2 Marks)

### Implementation: UserRepository

**UserRepository.java:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
```

**UserService.java:**
```java
public User saveUser(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new RuntimeException("User with email " + user.getEmail() + " already exists");
    }
    return userRepository.save(user);
}
```

### How Existence Checking Works

1. **Method Naming Convention**: Spring Data JPA parses method name
   - `existsBy` → Generates existence check query
   - `Email` → Field to check

2. **Generated SQL**:
```sql
SELECT COUNT(*) > 0 FROM users WHERE email = ?;
```

3. **Return Value**: 
   - `true` if record exists
   - `false` if record doesn't exist

### Why Use existsBy() Instead of findBy()?

**Performance Comparison:**

```java
// BAD: Loads entire entity
User user = userRepository.findByEmail(email);
if (user != null) { /* exists */ }

// GOOD: Only checks existence
if (userRepository.existsByEmail(email)) { /* exists */ }
```

**existsBy() Benefits:**
- Faster query (COUNT vs SELECT *)
- Less memory usage (no entity loading)
- Clearer intent in code

### Real-World Usage

**Preventing Duplicate Emails:**
```json
POST /api/users
{
  "email": "john@example.com"
}
```

If email exists:
```json
{
  "error": "User with email john@example.com already exists"
}
```

### Additional Examples:

```java
// Check if village exists before creating location
boolean exists = villageRepository.existsByCode("RUK1-VA");

// Check if resource name is taken
boolean exists = resourceRepository.existsByName("Ambulance");
```

---

## 8. Retrieve Users by Province (4 Marks)

### Implementation

**UserRepository.java:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.village.cell.sector.district.province.code = :provinceCode")
    List<User> findByProvinceCode(@Param("provinceCode") String provinceCode);
    
    @Query("SELECT u FROM User u WHERE u.village.cell.sector.district.province.name = :provinceName")
    List<User> findByProvinceName(@Param("provinceName") String provinceName);
}
```

### Query Logic Explanation

**Hierarchical Traversal:**
```
User → village → cell → sector → district → province
```

Even though User is linked to Village (not Province), we can traverse the entire hierarchy using dot notation in JPQL.

### How It Works

1. **@Query**: Custom JPQL query annotation
2. **Dot Notation**: `u.village.cell.sector.district.province.code`
   - Starts from User entity
   - Navigates through each relationship
   - Reaches Province entity
   - Accesses code field

3. **@Param**: Binds method parameter to query parameter

### Generated SQL

Spring Data JPA generates complex JOIN query:
```sql
SELECT u.* 
FROM users u
JOIN village v ON u.village_id = v.id
JOIN cell c ON v.cell_id = c.id
JOIN sector s ON c.sector_id = s.id
JOIN district d ON s.district_id = d.id
JOIN province p ON d.province_id = p.id
WHERE p.code = 'KGL';
```

### Repository Method Used

**UserService.java:**
```java
public List<User> getUsersByProvinceCode(String provinceCode) {
    return userRepository.findByProvinceCode(provinceCode);
}

public List<User> getUsersByProvinceName(String provinceName) {
    return userRepository.findByProvinceName(provinceName);
}
```

### API Endpoints

```
GET /api/users/province/code/KGL
GET /api/users/province/name/Kigali City
```

### Why This Design is Powerful

**Flexibility:**
- Users stored with village_id (normalized data)
- Can query by any administrative level
- No data duplication

**Example Scenario:**
```
Village "Rukiri I" → Cell "Rukiri I" → Sector "Remera" → District "Gasabo" → Province "Kigali City"

Users in Village "Rukiri I":
- John Doe
- Jane Smith

Query: GET /api/users/province/code/KGL
Result: Returns John Doe and Jane Smith (and all other users in Kigali City)
```

### Verification Query (PostgreSQL):
```sql
SELECT 
    u.name as user_name,
    v.name as village,
    c.name as cell,
    s.name as sector,
    d.name as district,
    p.name as province,
    p.code as province_code
FROM users u
JOIN village v ON u.village_id = v.id
JOIN cell c ON v.cell_id = c.id
JOIN sector s ON c.sector_id = s.id
JOIN district d ON s.district_id = d.id
JOIN province p ON d.province_id = p.id
WHERE p.code = 'KGL';
```

This demonstrates:
- Users are linked to villages
- Province is automatically resolved through relationships
- Query works correctly through the hierarchy

---

## Summary: All 30 Marks Covered

✅ **1. ERD (3 marks)**: 11 tables with clear relationships
✅ **2. Save Location (2 marks)**: Validation + JpaRepository.save() with village link
✅ **3. Sorting & Pagination (5 marks)**: Pageable with performance benefits explained
✅ **4. Many-to-Many (3 marks)**: Incident↔Resource with join table
✅ **5. One-to-Many (2 marks)**: Village→User with foreign key
✅ **6. One-to-One (2 marks)**: Incident→Location with unique constraint
✅ **7. existBy() (2 marks)**: Email validation with COUNT query
✅ **8. Province Query (4 marks)**: Hierarchical JPQL traversal with @Query

**Total: 30 Marks**
