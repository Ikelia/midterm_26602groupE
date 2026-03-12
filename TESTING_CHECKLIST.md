# Testing Checklist Before GitHub Push

## ☑️ Step 1: Restart Application
```bash
# Stop current application (Ctrl+C)
mvn clean
mvn spring-boot:run
```
**Wait for**: "Started EmergencySystemApplication"

## ☑️ Step 2: Check PostgreSQL Tables
Open pgAdmin → emergency_db → Schemas → public → Tables

**Expected 11 tables**:
- [ ] cell
- [ ] district  
- [ ] incident
- [ ] incident_resource
- [ ] location
- [ ] province
- [ ] resource
- [ ] sector
- [ ] users
- [ ] village

## ☑️ Step 3: Test in Postman (Order Matters!)

### Create Hierarchy (Do in Order)
- [ ] 1. POST /api/provinces (Kigali City)
- [ ] 2. POST /api/districts (Gasabo)
- [ ] 3. POST /api/sectors (Kimironko)
- [ ] 4. POST /api/cells (Kibagabaga)
- [ ] 5. POST /api/villages (Agatare)
- [ ] 6. POST /api/locations (with village)
- [ ] 7. POST /api/users (John Doe)
- [ ] 8. POST /api/resources (Ambulance)
- [ ] 9. POST /api/resources (Fire Truck)
- [ ] 10. POST /api/incidents (with location & resources)

### Test Queries
- [ ] 11. GET /api/locations/province/1
- [ ] 12. GET /api/users/province/name/Kigali City
- [ ] 13. GET /api/users/paginated?page=0&size=10&sortBy=name

### Test Updates
- [ ] 14. PUT /api/users/1 (update name)
- [ ] 15. PUT /api/provinces/1 (update name)

### Test Deletes
- [ ] 16. DELETE /api/resources/2
- [ ] 17. GET /api/resources/2 (should fail)

### Test Validation
- [ ] 18. POST /api/users (duplicate email - should fail)
- [ ] 19. POST /api/locations (without village - should fail)

## ☑️ Step 4: Verify Data in PostgreSQL
Run these queries in pgAdmin:
```sql
SELECT * FROM province;
SELECT * FROM district;
SELECT * FROM sector;
SELECT * FROM cell;
SELECT * FROM village;
SELECT * FROM location;
SELECT * FROM users;
SELECT * FROM incident;
SELECT * FROM resource;
SELECT * FROM incident_resource;
```

## ☑️ Step 5: Prepare for GitHub

### Initialize Git
```bash
git init
git add .
git commit -m "Initial commit: Emergency Resource Allocation System"
```

### Create GitHub Repository
1. Go to github.com
2. Click "New repository"
3. Name: emergency-resource-system
4. Don't initialize with README
5. Click "Create repository"

### Push to GitHub
```bash
git remote add origin https://github.com/YOUR_USERNAME/emergency-resource-system.git
git branch -M main
git push -u origin main
```

## ✅ All Done!
Your project is now on GitHub with:
- 11 tables with proper relationships
- Complete CRUD operations
- Hierarchical location structure
- Pagination & sorting
- Comprehensive documentation
