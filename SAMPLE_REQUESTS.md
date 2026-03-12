# Sample API Requests

## 1. Create Province
```json
POST http://localhost:8080/api/provinces
Content-Type: application/json

{
  "name": "Gauteng",
  "code": "GP"
}
```

## 2. Create Another Province
```json
POST http://localhost:8080/api/provinces
Content-Type: application/json

{
  "name": "Western Cape",
  "code": "WC"
}
```

## 3. Create User
```json
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "province": {
    "id": 1
  }
}
```

## 4. Create Location
```json
POST http://localhost:8080/api/locations
Content-Type: application/json

{
  "address": "123 Main Street, Johannesburg",
  "latitude": -26.2041,
  "longitude": 28.0473
}
```

## 5. Create Resource
```json
POST http://localhost:8080/api/resources
Content-Type: application/json

{
  "name": "Ambulance Unit 1",
  "type": "Medical",
  "quantity": 1
}
```

## 6. Create Another Resource
```json
POST http://localhost:8080/api/resources
Content-Type: application/json

{
  "name": "Fire Truck Alpha",
  "type": "Fire",
  "quantity": 1
}
```

## 7. Create Incident (with Location and Resources)
```json
POST http://localhost:8080/api/incidents
Content-Type: application/json

{
  "title": "Building Fire Emergency",
  "description": "Large fire at commercial building",
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

## 8. Get Users with Pagination and Sorting
```
GET http://localhost:8080/api/users/paginated?page=0&size=10&sortBy=name
```

## 9. Get Users by Province Name
```
GET http://localhost:8080/api/users/province/name/Gauteng
```

## 10. Get Users by Province Code
```
GET http://localhost:8080/api/users/province/code/GP
```

## 11. Get Incidents with Pagination
```
GET http://localhost:8080/api/incidents/paginated?page=0&size=5&sortBy=reportedAt
```
