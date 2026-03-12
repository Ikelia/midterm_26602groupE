# Detailed Implementation Explanations

## 1. Entity Relationship Diagram (ERD) - 3 Marks

### Tables:
1. **Province**: Stores province information (id, name, code)
2. **User**: Stores user details (id, name, email, province_id)
3. **Location**: Geographic data (id, address, latitude, longitude)
4. **Incident**: Emergency reports (id, title, description, user_id, location_id)
5. **Resource**: Emergency resources (id, name, type, quantity)
6. **Incident_Resource**: Join table (incident_id, resource_id)

### Relationships Logic:
- **Province → User (ONE-TO-MANY)**: One province contains many users, but each user belongs to one province
- **User → Incident (ONE-TO-MANY)**: One user can report multiple incidents
- **Incident → Location (ONE-TO-ONE)**: Each incident occurs at exactly one location
- **Incident ↔ Resource (MANY-TO-MANY)**: One incident needs multiple resources, one resource serves multiple incidents

---

## 2. Saving Location - 2 Marks

### Implementation:
```java
public Location saveLocation(Location location) {
    return locationRepository.save(location);
}
```

### Explanation:
The Location entity stores geographic information including address, latitude, and longitude. When the `save()` method of JpaRepository is called:

1. Spring Data JPA automatically generates the SQL INSERT statement
2. The location data is persisted to the database
3. The saved entity with the auto-generated ID is returned

The `@GeneratedValue(strategy = GenerationType.IDENTITY)` annotation ensures the database auto-generates the primary key.

---

## 3. Sorting and Pagination - 5 Marks

### Implementation:
```java
public Page<User> getUsersWithPaginationAndSorting(int page, int size, String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
    return userRepository.findAll(pageable);
}
```

### Explanation:

**Pagination**:
- Uses `Pageable` interface to define pagination parameters
- `PageRequest.of(page, size)` creates a Pageable object
- `page`: Zero-based page index (0 = first page)
- `size`: Number of records per page
- Returns `Page<User>` containing:
  - List of users for current page
  - Total number of pages
  - Total number of elements
  - Current page number

**Sorting**:
- `Sort.by(sortBy)` specifies the field to sort by
- `.ascending()` or `.descending()` defines sort order
- Can sort by any entity field (name, email, etc.)

**Performance Benefits**:
- Reduces memory usage by loading only required records
- Decreases database query time
- Improves API response time
- Better user experience with manageable data chunks

---

## 4. Many-to-Many Relationship - 3 Marks

### Implementation:
```java
@ManyToMany
@JoinTable(
    name = "incident_resource",
    joinColumns = @JoinColumn(name = "incident_id"),
    inverseJoinColumns = @JoinColumn(name = "resource_id")
)
private List<Resource> resources;
```

### Explanation:

**Why Many-to-Many?**
- One incident can require multiple resources (ambulance, fire truck, police)
- One resource can be allocated to multiple incidents

**Join Table**:
- `@JoinTable` creates a separate table named `incident_resource`
- Contains two foreign keys:
  - `incident_id`: References incident table
  - `resource_id`: References resource table
- No additional fields needed in this join table
- Spring Data JPA manages the relationship automatically

**How it works**:
When you save an incident with resources, JPA:
1. Saves the incident
2. Saves entries in incident_resource table linking incident to each resource

---

## 5. One-to-Many Relationship - 2 Marks

### Implementation:

**Province Entity**:
```java
@OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
private List<User> users;
```

**User Entity**:
```java
@ManyToOne
@JoinColumn(name = "province_id", nullable = false)
private Province province;
```

### Explanation:

**Relationship Logic**:
- One province can have many users
- Each user belongs to exactly one province

**Mapping Details**:
- `@OneToMany` in Province indicates the "one" side
- `@ManyToOne` in User indicates the "many" side
- `mappedBy = "province"` means User entity owns the relationship
- `@JoinColumn(name = "province_id")` creates foreign key column in users table
- The foreign key `province_id` in users table references province.id

**Cascade Operations**:
- `CascadeType.ALL` means operations on Province cascade to Users
- Deleting a province can delete associated users (if configured)

---

## 6. One-to-One Relationship - 2 Marks

### Implementation:
```java
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "location_id", referencedColumnName = "id")
private Location location;
```

### Explanation:

**Relationship Logic**:
- Each incident occurs at exactly one location
- Each location is associated with one incident

**Mapping Details**:
- `@OneToOne` annotation establishes the relationship
- `@JoinColumn(name = "location_id")` creates foreign key in incident table
- `referencedColumnName = "id"` specifies it references location.id
- `cascade = CascadeType.ALL` means saving incident also saves location

**How it works**:
When creating an incident with a location:
1. Location is saved first (if new)
2. Incident is saved with location_id foreign key
3. Both entities are linked in the database

---

## 7. existBy() Method - 2 Marks

### Implementation:
```java
boolean existsByEmail(String email);

// Usage in service:
if (userRepository.existsByEmail(email)) {
    throw new RuntimeException("User already exists");
}
```

### Explanation:

**How it works**:
- Spring Data JPA automatically generates the query from method name
- Generated SQL: `SELECT COUNT(*) > 0 FROM users WHERE email = ?`
- Returns `true` if at least one record exists, `false` otherwise

**Method Naming Convention**:
- `existsBy` + `FieldName`
- Spring parses the method name and creates appropriate query
- No need to write SQL or JPQL manually

**Use Cases**:
- Validation before saving (prevent duplicates)
- Check if resource exists before operations
- Efficient existence checking without loading entire entity

**Performance**:
- More efficient than `findBy()` methods
- Only checks existence, doesn't load data
- Returns boolean immediately

---

## 8. Retrieve Users by Province - 4 Marks

### Implementation:
```java
List<User> findByProvinceName(String provinceName);
List<User> findByProvinceCode(String provinceCode);
```

### Explanation:

**Query Generation**:
Spring Data JPA automatically generates JOIN queries:

For `findByProvinceName`:
```sql
SELECT u.* FROM users u 
JOIN province p ON u.province_id = p.id 
WHERE p.name = ?
```

For `findByProvinceCode`:
```sql
SELECT u.* FROM users u 
JOIN province p ON u.province_id = p.id 
WHERE p.code = ?
```

**How it works**:
1. Spring parses method name: `findBy` + `Province` + `Name`
2. Recognizes `Province` as a relationship in User entity
3. Automatically creates JOIN with province table
4. Filters by the specified field (name or code)

**Usage**:
```java
// Get all users from Gauteng province
List<User> gautengUsers = userRepository.findByProvinceName("Gauteng");

// Get all users from GP province code
List<User> gpUsers = userRepository.findByProvinceCode("GP");
```

**Benefits**:
- No manual SQL writing required
- Type-safe queries
- Automatic JOIN handling
- Clean, readable code
