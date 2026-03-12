# Emergency Resource Allocation and Incident Reporting System

A comprehensive Spring Boot backend system demonstrating advanced JPA relationships, hierarchical data structures, and RESTful API design for emergency resource management.

## 🎯 Project Overview

This system manages emergency incidents, resources, and user reporting with a complete administrative hierarchy structure (Province → District → Sector → Cell → Village → Location).

## 📊 Database Schema (11 Tables)

### Administrative Hierarchy
- **Province** → **District** → **Sector** → **Cell** → **Village** → **Location**

### Core Entities
- **User** (linked to Province)
- **Incident** (linked to Location and User)
- **Resource** (emergency resources)
- **Incident_Resource** (join table for Many-to-Many)

## 🔗 Relationships Demonstrated

### ✅ ONE-TO-MANY (7 relationships)
- Province → District
- District → Sector
- Sector → Cell
- Cell → Village
- Village → Location
- Province → User
- User → Incident

### ✅ ONE-TO-ONE
- Incident → Location

### ✅ MANY-TO-MANY
- Incident ↔ Resource (via incident_resource join table)

## 🚀 Features

### 1. Hierarchical Location Structure
- Locations MUST be linked to a Village
- Full hierarchy traversal: Village → Cell → Sector → District → Province
- Query locations at any administrative level

### 2. Pagination & Sorting
```java
Page<User> getUsersWithPaginationAndSorting(int page, int size, String sortBy)
```
- Improves performance for large datasets
- Customizable page size and sort fields

### 3. existsBy() Validation
```java
boolean existsByEmail(String email);
boolean existsByCode(String code);
```
- Prevents duplicate entries
- Validates before save operations

### 4. Hierarchical Queries
```java
// Get all users in a province
List<User> findByProvinceName(String provinceName);
List<User> findByProvinceCode(String provinceCode);

// Get all locations in a province (through hierarchy)
List<Location> findByProvinceId(Long provinceId);
```

### 5. Complete CRUD Operations
- Create (POST)
- Read (GET)
- Update (PUT)
- Delete (DELETE)

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Additional**: Lombok, Spring Data JPA

## 📦 Installation & Setup

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Maven 3.6+
- pgAdmin 4 (optional)

### Database Setup
1. Create database in PostgreSQL:
```sql
CREATE DATABASE emergency_db;
```

2. Update credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/emergency_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### Run Application
```bash
mvn spring-boot:run
```

Server starts on: `http://localhost:8080`

## 📚 API Endpoints

### Province
- `POST /api/provinces` - Create province
- `GET /api/provinces` - Get all provinces
- `GET /api/provinces/{id}` - Get province by ID
- `PUT /api/provinces/{id}` - Update province
- `DELETE /api/provinces/{id}` - Delete province

### District
- `POST /api/districts` - Create district
- `GET /api/districts` - Get all districts
- `GET /api/districts/province/{provinceId}` - Get districts by province

### Sector
- `POST /api/sectors` - Create sector
- `GET /api/sectors/district/{districtId}` - Get sectors by district

### Cell
- `POST /api/cells` - Create cell
- `GET /api/cells/sector/{sectorId}` - Get cells by sector

### Village
- `POST /api/villages` - Create village
- `GET /api/villages/cell/{cellId}` - Get villages by cell
- `GET /api/villages/district/{districtId}` - Get villages by district
- `GET /api/villages/province/{provinceId}` - Get villages by province

### Location
- `POST /api/locations` - Create location (MUST include village)
- `GET /api/locations/village/{villageId}` - Get locations by village
- `GET /api/locations/province/{provinceId}` - Get locations by province

### User
- `POST /api/users` - Create user
- `GET /api/users/paginated?page=0&size=10&sortBy=name` - Paginated users
- `GET /api/users/province/name/{provinceName}` - Users by province name
- `GET /api/users/province/code/{provinceCode}` - Users by province code
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Incident
- `POST /api/incidents` - Create incident
- `GET /api/incidents/paginated?page=0&size=5&sortBy=reportedAt` - Paginated incidents
- `PUT /api/incidents/{id}` - Update incident
- `DELETE /api/incidents/{id}` - Delete incident

### Resource
- `POST /api/resources` - Create resource
- `GET /api/resources` - Get all resources
- `PUT /api/resources/{id}` - Update resource
- `DELETE /api/resources/{id}` - Delete resource

## 📝 Example Usage

### Create Complete Hierarchy
```bash
# 1. Create Province
POST /api/provinces
{
  "name": "Kigali City",
  "code": "KGL"
}

# 2. Create District
POST /api/districts
{
  "name": "Gasabo",
  "code": "GSB",
  "province": {"id": 1}
}

# 3. Create Sector
POST /api/sectors
{
  "name": "Kimironko",
  "code": "KMR",
  "district": {"id": 1}
}

# 4. Create Cell
POST /api/cells
{
  "name": "Kibagabaga",
  "code": "KBG",
  "sector": {"id": 1}
}

# 5. Create Village
POST /api/villages
{
  "name": "Agatare",
  "code": "AGT",
  "cell": {"id": 1}
}

# 6. Create Location
POST /api/locations
{
  "address": "Near Kibagabaga Hospital",
  "latitude": -1.9536,
  "longitude": 30.1047,
  "village": {"id": 1}
}
```

### Query Hierarchically
```bash
# Get all locations in a province
GET /api/locations/province/1

# Get all users in a province by name
GET /api/users/province/name/Kigali City

# Get all villages in a district
GET /api/villages/district/1
```

## 🧪 Testing

### Postman Collection
Import `POSTMAN_COLLECTION.json` into Postman for complete API testing.

### Testing Guide
See `POSTMAN_TESTING_GUIDE.md` for step-by-step testing instructions.

## 📖 Documentation

- `EXPLANATIONS.md` - Detailed explanations for each requirement
- `HIERARCHICAL_LOCATION_STRUCTURE.md` - Complete hierarchy documentation
- `SAMPLE_REQUESTS.md` - API request examples
- `PUT_DELETE_REQUESTS.md` - Update and delete examples
- `POSTGRES_SETUP.md` - Database setup guide

## 🎓 Assessment Criteria Coverage

### ✅ ERD with 5+ Tables (3 Marks)
11 tables with clear relationships

### ✅ Saving Location (2 Marks)
Location service with JpaRepository.save()

### ✅ Sorting & Pagination (5 Marks)
Implemented with Pageable and Sort

### ✅ Many-to-Many (3 Marks)
Incident ↔ Resource with join table

### ✅ One-to-Many (2 Marks)
7 different One-to-Many relationships

### ✅ One-to-One (2 Marks)
Incident → Location

### ✅ existBy() Method (2 Marks)
existsByEmail(), existsByCode()

### ✅ Retrieve Users by Province (4 Marks)
findByProvinceName() and findByProvinceCode()

## 🤝 Contributing

This is an academic project. For improvements or suggestions, please create an issue.

## 📄 License

This project is created for educational purposes.

## 👨‍💻 Author

Created as part of Spring Boot JPA assessment.

---

**Note**: This system demonstrates advanced JPA relationships, hierarchical data structures, and RESTful API best practices suitable for real-world emergency management systems.
