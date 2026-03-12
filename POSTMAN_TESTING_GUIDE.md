# Postman Testing Guide - Complete Flow

## Step 1: Verify Application is Running
- Stop the current application (Ctrl+C in terminal)
- Run: `mvn spring-boot:run`
- Wait for: "Started EmergencySystemApplication"
- Server should be on: http://localhost:8080

## Step 2: Verify Tables in PostgreSQL (pgAdmin)
1. Open pgAdmin 4
2. Navigate to: Servers → PostgreSQL 18 → Databases → emergency_db → Schemas → public → Tables
3. You should see **11 tables**:
   - cell
   - district
   - incident
   - incident_resource
   - location
   - province
   - resource
   - sector
   - users
   - village

## Step 3: Test in Postman (Follow This Order)

### Test 1: Create Province
```
POST http://localhost:8080/api/provinces
Content-Type: application/json

{
  "name": "Kigali City",
  "code": "KGL"
}
```
**Expected**: Status 200, returns province with id: 1

---

### Test 2: Create District (linked to Province)
```
POST http://localhost:8080/api/districts
Content-Type: application/json

{
  "name": "Gasabo",
  "code": "GSB",
  "province": {"id": 1}
}
```
**Expected**: Status 200, returns district with id: 1

---

### Test 3: Create Sector (linked to District)
```
POST http://localhost:8080/api/sectors
Content-Type: application/json

{
  "name": "Kimironko",
  "code": "KMR",
  "district": {"id": 1}
}
```
**Expected**: Status 200, returns sector with id: 1

---

### Test 4: Create Cell (linked to Sector)
```
POST http://localhost:8080/api/cells
Content-Type: application/json

{
  "name": "Kibagabaga",
  "code": "KBG",
  "sector": {"id": 1}
}
```
**Expected**: Status 200, returns cell with id: 1

---

### Test 5: Create Village (linked to Cell)
```
POST http://localhost:8080/api/villages
Content-Type: application/json

{
  "name": "Agatare",
  "code": "AGT",
  "cell": {"id": 1}
}
```
**Expected**: Status 200, returns village with id: 1

---

### Test 6: Create Location (MUST be linked to Village)
```
POST http://localhost:8080/api/locations
Content-Type: application/json

{
  "address": "Near Kibagabaga Hospital",
  "latitude": -1.9536,
  "longitude": 30.1047,
  "village": {"id": 1}
}
```
**Expected**: Status 200, returns location with id: 1

---

### Test 7: Create User (linked to Province)
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "province": {"id": 1}
}
```
**Expected**: Status 200, returns user with id: 1

---

### Test 8: Create Resource
```
POST http://localhost:8080/api/resources
Content-Type: application/json

{
  "name": "Ambulance Unit 1",
  "type": "Medical",
  "quantity": 1
}
```
**Expected**: Status 200, returns resource with id: 1

---

### Test 9: Create Another Resource
```
POST http://localhost:8080/api/resources
Content-Type: application/json

{
  "name": "Fire Truck Alpha",
  "type": "Fire",
  "quantity": 1
}
```
**Expected**: Status 200, returns resource with id: 2

---

### Test 10: Create Incident (with Location and Resources)
```
POST http://localhost:8080/api/incidents
Content-Type: application/json

{
  "title": "Building Fire Emergency",
  "description": "Large fire at commercial building",
  "user": {"id": 1},
  "location": {"id": 1},
  "resources": [
    {"id": 1},
    {"id": 2}
  ]
}
```
**Expected**: Status 200, returns incident with id: 1

---

## Step 4: Test Hierarchical Queries

### Test 11: Get All Locations in Province 1
```
GET http://localhost:8080/api/locations/province/1
```
**Expected**: Returns array with 1 location

---

### Test 12: Get All Villages in District 1
```
GET http://localhost:8080/api/villages/district/1
```
**Expected**: Returns array with 1 village

---

### Test 13: Get All Users by Province Name
```
GET http://localhost:8080/api/users/province/name/Kigali City
```
**Expected**: Returns array with 1 user (John Doe)

---

### Test 14: Get All Users by Province Code
```
GET http://localhost:8080/api/users/province/code/KGL
```
**Expected**: Returns array with 1 user (John Doe)

---

### Test 15: Get Village by ID (Shows Full Hierarchy)
```
GET http://localhost:8080/api/villages/1
```
**Expected**: Returns village with nested cell → sector → district → province

---

## Step 5: Test Pagination and Sorting

### Test 16: Get Users with Pagination
```
GET http://localhost:8080/api/users/paginated?page=0&size=10&sortBy=name
```
**Expected**: Returns Page object with users sorted by name

---

### Test 17: Get Incidents with Pagination
```
GET http://localhost:8080/api/incidents/paginated?page=0&size=5&sortBy=reportedAt
```
**Expected**: Returns Page object with incidents sorted by reportedAt

---

## Step 6: Test PUT (Update) Operations

### Test 18: Update User
```
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "province": {"id": 1}
}
```
**Expected**: Status 200, returns updated user

---

### Test 19: Update Province
```
PUT http://localhost:8080/api/provinces/1
Content-Type: application/json

{
  "name": "Kigali City Province",
  "code": "KGL"
}
```
**Expected**: Status 200, returns updated province

---

### Test 20: Update Location
```
PUT http://localhost:8080/api/locations/1
Content-Type: application/json

{
  "address": "Updated Address - Kibagabaga Hospital",
  "latitude": -1.9536,
  "longitude": 30.1047,
  "village": {"id": 1}
}
```
**Expected**: Status 200, returns updated location

---

## Step 7: Test existsBy() Method

### Test 21: Try Creating Duplicate User (Should Fail)
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "john.smith@example.com",
  "province": {"id": 1}
}
```
**Expected**: Status 500, error message: "User with email john.smith@example.com already exists"

---

## Step 8: Test DELETE Operations

### Test 22: Delete Resource
```
DELETE http://localhost:8080/api/resources/2
```
**Expected**: Status 200, message: "Resource deleted successfully"

---

### Test 23: Verify Resource Deleted
```
GET http://localhost:8080/api/resources/2
```
**Expected**: Status 500, error: "Resource not found with id: 2"

---

## Step 9: Test Error Handling

### Test 24: Try Creating Location Without Village (Should Fail)
```
POST http://localhost:8080/api/locations
Content-Type: application/json

{
  "address": "Test Address",
  "latitude": -1.9536,
  "longitude": 30.1047
}
```
**Expected**: Status 500, error: "Location must be linked to a Village"

---

### Test 25: Get Non-Existent User
```
GET http://localhost:8080/api/users/999
```
**Expected**: Status 500, error: "User not found with id: 999"

---

## Verification Checklist

After testing, verify in pgAdmin:

### Check Province Table
```sql
SELECT * FROM province;
```
Should show: Kigali City Province (KGL)

### Check District Table
```sql
SELECT * FROM district;
```
Should show: Gasabo (GSB) linked to province_id: 1

### Check Sector Table
```sql
SELECT * FROM sector;
```
Should show: Kimironko (KMR) linked to district_id: 1

### Check Cell Table
```sql
SELECT * FROM cell;
```
Should show: Kibagabaga (KBG) linked to sector_id: 1

### Check Village Table
```sql
SELECT * FROM village;
```
Should show: Agatare (AGT) linked to cell_id: 1

### Check Location Table
```sql
SELECT * FROM location;
```
Should show: Location linked to village_id: 1

### Check Users Table
```sql
SELECT * FROM users;
```
Should show: John Smith linked to province_id: 1

### Check Incident Table
```sql
SELECT * FROM incident;
```
Should show: Building Fire Emergency linked to user_id: 1 and location_id: 1

### Check Incident_Resource Join Table
```sql
SELECT * FROM incident_resource;
```
Should show: 2 rows (incident_id: 1 with resource_id: 1 and 2)

### Check Resource Table
```sql
SELECT * FROM resource;
```
Should show: 1 resource (Ambulance Unit 1) - Fire Truck was deleted

---

## Summary of What to Test

✅ **CRUD Operations**: Create, Read, Update, Delete for all entities
✅ **Hierarchical Relationships**: Province → District → Sector → Cell → Village → Location
✅ **Query Methods**: findByProvinceName(), findByProvinceCode()
✅ **Pagination & Sorting**: Users and Incidents with page/size/sortBy
✅ **existsBy() Method**: Duplicate email validation
✅ **ONE-TO-MANY**: Multiple relationships demonstrated
✅ **ONE-TO-ONE**: Incident → Location
✅ **MANY-TO-MANY**: Incident ↔ Resource with join table
✅ **Error Handling**: Proper error messages for invalid operations
✅ **Location Validation**: Must be linked to Village

---

## Expected Table Count in PostgreSQL

**Total: 11 Tables**
1. province
2. district
3. sector
4. cell
5. village
6. location
7. users
8. incident
9. resource
10. incident_resource (join table)
