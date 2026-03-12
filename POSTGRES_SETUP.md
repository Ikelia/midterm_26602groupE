# PostgreSQL Setup Guide

## Prerequisites
- PostgreSQL installed with pgAdmin 4
- Default port: 5432

## Database Setup Steps

### Option 1: Using pgAdmin 4 (GUI)
1. Open pgAdmin 4
2. Connect to your PostgreSQL server (usually localhost)
3. Right-click on "Databases" → "Create" → "Database"
4. Database name: `emergency_db`
5. Owner: `postgres`
6. Click "Save"

### Option 2: Using SQL Query in pgAdmin
1. Open pgAdmin 4
2. Connect to PostgreSQL server
3. Open Query Tool (Tools → Query Tool)
4. Run this command:
```sql
CREATE DATABASE emergency_db;
```

### Option 3: Using Command Line (psql)
```bash
psql -U postgres
CREATE DATABASE emergency_db;
\q
```

## Verify Connection Settings

Make sure your PostgreSQL settings match:
- **Host**: localhost
- **Port**: 5432
- **Database**: emergency_db
- **Username**: postgres
- **Password**: Admin123

## Update Password (if different)

If your PostgreSQL password is different from "Admin123", update it in:
`src/main/resources/application.properties`

```properties
spring.datasource.password=YOUR_PASSWORD_HERE
```

## Run the Application

```bash
mvn spring-boot:run
```

The application will automatically create all tables (Province, User, Location, Incident, Resource, Incident_Resource) when it starts.

## Verify Tables Created

After running the application, check in pgAdmin:
1. Expand: Servers → PostgreSQL → Databases → emergency_db → Schemas → public → Tables
2. You should see 6 tables:
   - province
   - users
   - location
   - incident
   - resource
   - incident_resource
