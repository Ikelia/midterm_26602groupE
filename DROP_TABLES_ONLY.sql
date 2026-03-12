-- Execute this in emergency_db to drop all tables
-- This avoids the need to drop the entire database

-- Drop tables in correct order (respecting foreign key constraints)
DROP TABLE IF EXISTS incident_resource CASCADE;
DROP TABLE IF EXISTS incident CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS resource CASCADE;
DROP TABLE IF EXISTS village CASCADE;
DROP TABLE IF EXISTS cell CASCADE;
DROP TABLE IF EXISTS sector CASCADE;
DROP TABLE IF EXISTS district CASCADE;
DROP TABLE IF EXISTS province CASCADE;

-- Verify all tables are dropped
SELECT tablename FROM pg_tables WHERE schemaname = 'public';
