# Database Reset and Rwanda Data Population Guide

## Step 1: Drop and Recreate Database

Open pgAdmin and execute:

```sql
DROP DATABASE emergency_db;
CREATE DATABASE emergency_db;
```

## Step 2: Restart Spring Boot Application

Stop your Spring Boot application (if running) and restart it:

```bash
mvn spring-boot:run
```

This will automatically create all tables with the CORRECT schema where User is linked to Village (not Province).

## Step 3: Verify Tables Created

In pgAdmin, check that these tables exist:
- province
- district
- sector
- cell
- village
- location
- users
- incident
- resource
- incident_resource

**IMPORTANT**: Check the `users` table - it should have `village_id` column, NOT `province_id`.

## Step 4: Populate Rwanda Administrative Data

In pgAdmin, open and execute the `RWANDA_DATA_POPULATION.sql` file.

This will populate:
- 1 Province (Kigali City)
- 3 Districts (Gasabo, Kicukiro, Nyarugenge)
- 6 Sectors
- 13 Cells
- 26 Villages

## Step 5: Test User Creation with Village

Use Postman to create a user linked to a village:

```json
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "village": {"id": 1}
}
```

**Note**: We use `village_id`, NOT `province_id`!

## Step 6: Test Retrieving Users by Province

Even though users are linked to villages, we can retrieve them by province:

```
GET http://localhost:8080/api/users/province/code/KGL
```

This works because the query traverses: User → Village → Cell → Sector → District → Province

## Step 7: Create Sample Data

### Create Location
```json
POST http://localhost:8080/api/locations
{
  "address": "KN 5 Ave, Kigali",
  "latitude": -1.9441,
  "longitude": 30.0619,
  "village": {"id": 1}
}
```

### Create Resource
```json
POST http://localhost:8080/api/resources
{
  "name": "Ambulance",
  "type": "VEHICLE",
  "quantity": 5
}
```

### Create Incident
```json
POST http://localhost:8080/api/incidents
{
  "title": "Medical Emergency",
  "description": "Patient needs immediate attention",
  "user": {"id": 1},
  "location": {"id": 1},
  "resources": [{"id": 1}]
}
```

## Verification Checklist

- [ ] Database dropped and recreated
- [ ] Spring Boot restarted and tables created
- [ ] `users` table has `village_id` column (not `province_id`)
- [ ] Rwanda data populated (1 province, 3 districts, 6 sectors, 13 cells, 26 villages)
- [ ] User created with village_id
- [ ] Users retrieved by province code (KGL) successfully
- [ ] Location created with village validation
- [ ] Incident created with Many-to-Many resource relationship
- [ ] incident_resource join table populated

## Expected Results

After completing all steps, you should have:
- Complete Rwanda administrative hierarchy in database
- Users linked to villages (automatic province resolution)
- Working pagination and sorting
- All CRUD operations functional
- Many-to-Many relationship working (Incident ↔ Resource)
