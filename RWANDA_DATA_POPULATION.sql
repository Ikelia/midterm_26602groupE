-- Rwanda Administrative Structure Population Script
-- Execute this after dropping and recreating the emergency_db database

-- 1. PROVINCE: Kigali City
INSERT INTO province (name, code) VALUES ('Kigali City', 'KGL');

-- 2. DISTRICTS under Kigali City
INSERT INTO district (name, code, province_id) VALUES 
('Gasabo', 'GAS', 1),
('Kicukiro', 'KIC', 1),
('Nyarugenge', 'NYA', 1);

-- 3. SECTORS under Gasabo District
INSERT INTO sector (name, code, district_id) VALUES 
('Remera', 'REM', 1),
('Kacyiru', 'KAC', 1);

-- 4. SECTORS under Kicukiro District
INSERT INTO sector (name, code, district_id) VALUES 
('Kagarama', 'KAG', 2),
('Niboye', 'NIB', 2);

-- 5. SECTORS under Nyarugenge District
INSERT INTO sector (name, code, district_id) VALUES 
('Muhima', 'MUH', 3),
('Nyamirambo', 'NYM', 3);

-- 6. CELLS under Remera Sector (Gasabo)
INSERT INTO cell (name, code, sector_id) VALUES 
('Rukiri I', 'RUK1', 1),
('Rukiri II', 'RUK2', 1),
('Nyabisindu', 'NYAB', 1);

-- 7. CELLS under Kacyiru Sector (Gasabo)
INSERT INTO cell (name, code, sector_id) VALUES 
('Kamatamu', 'KAM', 2),
('Kagugu', 'KAG', 2);

-- 8. CELLS under Kagarama Sector (Kicukiro)
INSERT INTO cell (name, code, sector_id) VALUES 
('Kanserege', 'KAN', 3),
('Muyange', 'MUY', 3);

-- 9. CELLS under Niboye Sector (Kicukiro)
INSERT INTO cell (name, code, sector_id) VALUES 
('Niboye', 'NIB', 4),
('Nyakabanda', 'NYAK', 4);

-- 10. CELLS under Muhima Sector (Nyarugenge)
INSERT INTO cell (name, code, sector_id) VALUES 
('Nyabugogo', 'NYAB', 5),
('Kabasengerezi', 'KAB', 5);

-- 11. CELLS under Nyamirambo Sector (Nyarugenge)
INSERT INTO cell (name, code, sector_id) VALUES 
('Cyivugiza', 'CYI', 6),
('Rugarama', 'RUG', 6);

-- 12. VILLAGES under Rukiri I Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'RUK1-VA', 1),
('Village B', 'RUK1-VB', 1),
('Village C', 'RUK1-VC', 1);

-- 13. VILLAGES under Rukiri II Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'RUK2-VA', 2),
('Village B', 'RUK2-VB', 2);

-- 14. VILLAGES under Nyabisindu Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'NYAB-VA', 3),
('Village B', 'NYAB-VB', 3);

-- 15. VILLAGES under Kamatamu Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'KAM-VA', 4),
('Village B', 'KAM-VB', 4);

-- 16. VILLAGES under Kagugu Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'KAG-VA', 5),
('Village B', 'KAG-VB', 5);

-- 17. VILLAGES under Kanserege Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'KAN-VA', 6),
('Village B', 'KAN-VB', 6);

-- 18. VILLAGES under Muyange Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'MUY-VA', 7),
('Village B', 'MUY-VB', 7);

-- 19. VILLAGES under Niboye Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'NIB-VA', 8),
('Village B', 'NIB-VB', 8);

-- 20. VILLAGES under Nyakabanda Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'NYAK-VA', 9),
('Village B', 'NYAK-VB', 9);

-- 21. VILLAGES under Nyabugogo Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'NYAB-VA', 10),
('Village B', 'NYAB-VB', 10);

-- 22. VILLAGES under Kabasengerezi Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'KAB-VA', 11),
('Village B', 'KAB-VB', 11);

-- 23. VILLAGES under Cyivugiza Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'CYI-VA', 12),
('Village B', 'CYI-VB', 12);

-- 24. VILLAGES under Rugarama Cell
INSERT INTO village (name, code, cell_id) VALUES 
('Village A', 'RUG-VA', 13),
('Village B', 'RUG-VB', 13);

-- Verification Queries
-- Check the complete hierarchy
SELECT 
    p.name as Province,
    d.name as District,
    s.name as Sector,
    c.name as Cell,
    v.name as Village
FROM village v
JOIN cell c ON v.cell_id = c.id
JOIN sector s ON c.sector_id = s.id
JOIN district d ON s.district_id = d.id
JOIN province p ON d.province_id = p.id
ORDER BY p.name, d.name, s.name, c.name, v.name;

-- Count summary
SELECT 
    (SELECT COUNT(*) FROM province) as Provinces,
    (SELECT COUNT(*) FROM district) as Districts,
    (SELECT COUNT(*) FROM sector) as Sectors,
    (SELECT COUNT(*) FROM cell) as Cells,
    (SELECT COUNT(*) FROM village) as Villages;
