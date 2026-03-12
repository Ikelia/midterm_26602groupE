-- IMPORTANT: Execute this script while connected to the 'postgres' database (NOT emergency_db)
-- Step 1: Right-click on 'postgres' database in pgAdmin and select 'Query Tool'
-- Step 2: Paste and execute this entire script

-- Terminate all active connections to emergency_db
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'emergency_db'
  AND pid <> pg_backend_pid();

-- Drop the database
DROP DATABASE IF EXISTS emergency_db;

-- Recreate the database
CREATE DATABASE emergency_db;

-- Verify database was created
SELECT datname FROM pg_database WHERE datname = 'emergency_db';
