# Hierarchical Location Structure

## Overview
The system implements a complete administrative hierarchy for Rwanda:
**Province → District → Sector → Cell → Village → Location**

## Database Structure (11 Tables Total)

### Administrative Hierarchy Tables:
1. **Province** - Top level (e.g., Kigali, Eastern Province)
2. **District** - Belongs to Province (e.g., Gasabo, Nyarugenge)
3. **Sector** - Belongs to District (e.g., Kimironko, Remera)
4. **Cell** - Belongs to Sector (e.g., Kibagabaga, Rukiri)
5. **Village** - Belongs to Cell (e.g., Agatare, Kabuye)
6. **Location** - Belongs to Village (specific incident location)

### Other Tables:
7. **User** - Linked to Province
8. **Incident** - Linked to Location (ONE-TO-ONE)
9. **Resource** - Emergency resources
10. **Incident_Resource** - Join table (MANY-TO-MANY)
11. **Users** - System users

## Relationships Explained

### Province → District (ONE-TO-MANY)
```java
// Province has many Districts
@OneToMany(mappedBy = "district")
private List<District> districts;

// District belongs to one Province
@ManyToOne
@JoinColumn(name = "province_id")
private Province province;
```

### District → Sector (ONE-TO-MANY)
```java
// District has many Sectors
@OneToMany(mappedBy = "sector")
private List<Sector> sectors;

// Sector belongs to one District
@ManyToOne
@JoinColumn(name = "district_id")
private District district;
```

### Sector → Cell (ONE-TO-MANY)
```java
// Sector has many Cells
@OneToMany(mappedBy = "cell")
private List<Cell> cells;

// Cell belongs to one Sector
@ManyToOne
@JoinColumn(name = "sector_id")
private Sector sector;
```

### Cell → Village (ONE-TO-MANY)
```java
// Cell has many Villages
@OneToMany(mappedBy = "village")
private List<Village> villages;

// Village belongs to one Cell
@ManyToOne
@JoinColumn(name = "cell_id")
private Cell cell;
```

### Village → Location (ONE-TO-MANY)
```java
// Village has many Locations
@OneToMany(mappedBy = "location")
private List<Location> locations;

// Location MUST belong to one Village
@ManyToOne
@JoinColumn(name = "village_id", nullable = false)
private Village village;
```

## Key Requirement: Location MUST be Linked to Village

**IMPORTANT**: A location cannot be saved without being linked to a village.

```java
public Location saveLocation(Location location) {
    if (location.getVillage() == null || location.getVillage().getId() == null) {
        throw new RuntimeException("Location must be linked to a Village");
    }
    return locationRepository.save(location);
}
```

## Hierarchical Queries

### 1. Get All Locations in a Province
```java
@Query("SELECT l FROM Location l WHERE l.village.cell.sector.district.province.id = :provinceId")
List<Location> findByProvinceId(@Param("provinceId") Long provinceId);
```

**Explanation**: Traverses the hierarchy:
- Location → Village → Cell → Sector → District → Province

### 2. Get All Villages in a District
```java
@Query("SELECT v FROM Village v WHERE v.cell.sector.district.id = :districtId")
List<Village> findByDistrictId(@Param("districtId") Long districtId);
```

**Explanation**: Traverses:
- Village → Cell → Sector → District

### 3. Get All Users in a Province
```java
List<User> findByProvinceName(String provinceName);
List<User> findByProvinceCode(String provinceCode);
```

**Explanation**: Direct relationship User → Province

## API Endpoints

### Create Hierarchy (Top-Down)

#### 1. Create Province
```http
POST /api/provinces
{
  "name": "Kigali City",
  "code": "KGL"
}
```

#### 2. Create District (linked to Province)
```http
POST /api/districts
{
  "name": "Gasabo",
  "code": "GSB",
  "province": {"id": 1}
}
```

#### 3. Create Sector (linked to District)
```http
POST /api/sectors
{
  "name": "Kimironko",
  "code": "KMR",
  "district": {"id": 1}
}
```

#### 4. Create Cell (linked to Sector)
```http
POST /api/cells
{
  "name": "Kibagabaga",
  "code": "KBG",
  "sector": {"id": 1}
}
```

#### 5. Create Village (linked to Cell)
```http
POST /api/villages
{
  "name": "Agatare",
  "code": "AGT",
  "cell": {"id": 1}
}
```

#### 6. Create Location (MUST be linked to Village)
```http
POST /api/locations
{
  "address": "Near Kibagabaga Hospital",
  "latitude": -1.9536,
  "longitude": 30.1047,
  "village": {"id": 1}
}
```

## Query Examples

### Get All Locations in Province 1
```http
GET /api/locations/province/1
```

### Get All Villages in District 1
```http
GET /api/villages/district/1
```

### Get All Cells in Sector 1
```http
GET /api/cells/sector/1
```

### Get All Sectors in District 1
```http
GET /api/sectors/district/1
```

### Get All Districts in Province 1
```http
GET /api/districts/province/1
```

## Retrieve User's Full Location Hierarchy by Village ID

Given a village ID, you can retrieve the complete hierarchy:

```java
Village village = villageRepository.findById(villageId);
Cell cell = village.getCell();
Sector sector = cell.getSector();
District district = sector.getDistrict();
Province province = district.getProvince();
```

**Example Response Structure**:
```json
{
  "village": {
    "id": 1,
    "name": "Agatare",
    "code": "AGT",
    "cell": {
      "id": 1,
      "name": "Kibagabaga",
      "code": "KBG",
      "sector": {
        "id": 1,
        "name": "Kimironko",
        "code": "KMR",
        "district": {
          "id": 1,
          "name": "Gasabo",
          "code": "GSB",
          "province": {
            "id": 1,
            "name": "Kigali City",
            "code": "KGL"
          }
        }
      }
    }
  }
}
```

## Benefits of This Approach

1. **Clear Hierarchy**: Each level has a clear parent-child relationship
2. **Data Integrity**: Foreign keys ensure referential integrity
3. **Flexible Queries**: Can query at any level of the hierarchy
4. **Scalability**: Easy to add more administrative levels if needed
5. **Demonstrates Multiple ONE-TO-MANY Relationships**: Shows mastery of JPA relationships

## Assessment Criteria Coverage

✅ **ONE-TO-MANY Relationships**: 
- Province → District
- District → Sector
- Sector → Cell
- Cell → Village
- Village → Location
- Province → User
- User → Incident

✅ **Query Methods**: 
- findByProvinceId()
- findByDistrictId()
- findBySectorId()
- findByCellId()

✅ **Hierarchical Queries**: 
- Retrieve all locations in a province
- Retrieve district/sector/cell by village ID
- Show all users in a province

## Logic Explanation for Assessment

**Question**: "How do you retrieve a user's district, sector, cell by just a village id?"

**Answer**: 
"Given a village ID, we can traverse up the hierarchy using the relationships:
1. Get Village by ID
2. Access village.getCell() → returns Cell
3. Access cell.getSector() → returns Sector
4. Access sector.getDistrict() → returns District
5. Access district.getProvince() → returns Province

This works because each entity maintains a reference to its parent through the @ManyToOne relationship. JPA automatically loads these relationships, allowing us to navigate the entire hierarchy from any point."

**Question**: "Show all users in a province"

**Answer**:
"We use Spring Data JPA's query derivation:
```java
List<User> findByProvinceName(String provinceName);
```
Spring automatically generates:
```sql
SELECT u.* FROM users u 
JOIN province p ON u.province_id = p.id 
WHERE p.name = ?
```
This works because User has a direct @ManyToOne relationship with Province."
