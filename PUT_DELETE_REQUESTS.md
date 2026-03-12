# PUT and DELETE Request Examples

## Province Endpoints

### Update Province (PUT)
```http
PUT http://localhost:8080/api/provinces/1
Content-Type: application/json

{
  "name": "Gauteng Province",
  "code": "GP"
}
```

### Delete Province (DELETE)
```http
DELETE http://localhost:8080/api/provinces/1
```

---

## User Endpoints

### Get User by ID (GET)
```http
GET http://localhost:8080/api/users/1
```

### Update User (PUT)
```http
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "province": {
    "id": 1
  }
}
```

### Delete User (DELETE)
```http
DELETE http://localhost:8080/api/users/1
```

---

## Location Endpoints

### Get All Locations (GET)
```http
GET http://localhost:8080/api/locations
```

### Update Location (PUT)
```http
PUT http://localhost:8080/api/locations/1
Content-Type: application/json

{
  "address": "456 Updated Street, Johannesburg",
  "latitude": -26.2041,
  "longitude": 28.0473
}
```

### Delete Location (DELETE)
```http
DELETE http://localhost:8080/api/locations/1
```

---

## Resource Endpoints

### Update Resource (PUT)
```http
PUT http://localhost:8080/api/resources/1
Content-Type: application/json

{
  "name": "Ambulance Unit 2",
  "type": "Medical",
  "quantity": 2
}
```

### Delete Resource (DELETE)
```http
DELETE http://localhost:8080/api/resources/1
```

---

## Incident Endpoints

### Update Incident (PUT)
```http
PUT http://localhost:8080/api/incidents/1
Content-Type: application/json

{
  "title": "Updated Fire Emergency",
  "description": "Fire has been contained",
  "user": {
    "id": 1
  },
  "location": {
    "id": 1
  },
  "resources": [
    {"id": 1},
    {"id": 2}
  ]
}
```

### Delete Incident (DELETE)
```http
DELETE http://localhost:8080/api/incidents/1
```

---

## Complete CRUD Testing Flow

### 1. CREATE (POST) - Create a Province
```http
POST http://localhost:8080/api/provinces
Content-Type: application/json

{
  "name": "Western Cape",
  "code": "WC"
}
```
**Response**: Returns created province with ID

---

### 2. READ (GET) - Get Province by ID
```http
GET http://localhost:8080/api/provinces/1
```
**Response**: Returns province details

---

### 3. UPDATE (PUT) - Update Province
```http
PUT http://localhost:8080/api/provinces/1
Content-Type: application/json

{
  "name": "Western Cape Province",
  "code": "WC"
}
```
**Response**: Returns updated province

---

### 4. DELETE (DELETE) - Delete Province
```http
DELETE http://localhost:8080/api/provinces/1
```
**Response**: "Province deleted successfully"

---

## Testing with cURL (Command Line)

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\",\"province\":{\"id\":1}}"
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Update Resource
```bash
curl -X PUT http://localhost:8080/api/resources/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Fire Truck Beta\",\"type\":\"Fire\",\"quantity\":1}"
```

### Delete Resource
```bash
curl -X DELETE http://localhost:8080/api/resources/1
```

---

## Error Handling

All endpoints include proper error handling:

- **404 Not Found**: When trying to update/delete non-existent resource
- **400 Bad Request**: When validation fails (e.g., duplicate email)
- **200 OK**: Successful operation

### Example Error Response
```json
{
  "timestamp": "2026-03-07T13:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999"
}
```
