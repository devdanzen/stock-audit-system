-- =============================================================================
-- Stock Audit System — REAL DATA SEED (adapted from the source audit sheets)
-- Source: AUDIT.csv / ENDBALL_11_NOV.csv / Movement.csv / RESEP.csv / RCV.
-- Run AFTER schema.sql. (Use this OR seed_demo.sql, not both.)
--
-- 19 real items across all 4 sections (B=Bar, F=Food, K=Kitchen, S=Sushi).
-- The numbers below are the real audit-sheet columns, so the app's
-- v_stock_on_hand (EndBal + Receiving + In - Sales - Waste - Out - Consumption)
-- reproduces the real SOH column.
--
-- ADAPTATIONS (source quirks -> ERD):
--   * item codes kept as-is; item_class = code prefix; category from Section.
--   * source has NO per-item selling price -> we use current_cost as the sales
--     unit price so revenue charts are non-zero (a demo proxy, not real menu price).
--   * Excel-serial dates replaced with recent relative dates (CURDATE-based)
--     so Dashboard "today" + last-30-days charts populate. Opening balance is
--     one end_balance snapshot dated 2025-11-10.
-- =============================================================================

USE stock_audit_db;

-- Clear only the 4 placeholder items from schema.sql (they have no child rows).
-- NOTE: this seed is NOT re-runnable on an already-seeded DB (FKs block it and
-- item_code is UNIQUE). To re-seed, re-import schema.sql first, then this file once.
DELETE FROM master_item WHERE item_code IN ('LIQ-0001','DRY-0497','FISH-0042','MENU-001');

-- ---- MASTER ITEMS (19 real + 1 finished menu item for the recipe) ----
INSERT INTO master_item (item_code, description, item_class, category_id, outlet_id,
                         base_unit, purchase_unit, qty_per_purchase_unit, current_cost, selling_price, status)
SELECT v.code, v.descr, v.cls,
       (SELECT category_id FROM category WHERE category_code = v.section),
       (SELECT outlet_id FROM outlet WHERE outlet_code = 'SHLMP'),
       v.uom, v.uom, v.qtypu, v.cost, v.cost, 'Active'
FROM (
  SELECT 'ALKOHOL-0014' code,'BEER SAPORO' descr,'ALKOHOL' cls,'B' section,'BOTOL' uom,1 qtypu,33000 cost UNION ALL
  SELECT 'ALKOHOL-0018','BIR BINTANG PINT','ALKOHOL','B','BOTOL',1,21266.3 UNION ALL
  SELECT 'CK-0205','BASQUE MATCHA CHEESECAKE','CK','B','SLICE',1,24700 UNION ALL
  SELECT 'DRYGOODS-0122','GULA','DRYGOODS','B','GRAM',1000,19.91 UNION ALL
  SELECT 'SOFTDRINK-0021','COCA COLA 250 ML','SOFTDRINK','B','KALENG',1,4767.4 UNION ALL
  SELECT 'NONALKOHOL-0674','HAZELNUT MAISON 1883','NONALKOHOL','B','ML',1000,159.467 UNION ALL
  SELECT 'PACKAGING-0004','BOWL BENTO','PACKAGING','F','PCS',1,2040.5 UNION ALL
  SELECT 'PACKAGING-0010','CUP SOUFLE SIP 180ML +TUTUP','PACKAGING','F','PCS',1,1045 UNION ALL
  SELECT 'PACKAGING-0051','SUSHI BOX SB-10 A KECIL','PACKAGING','F','PCS',1,853.6 UNION ALL
  SELECT 'PACKAGING-0735','ALUMUNIUM FOIL TRAY RX 212','PACKAGING','F','PCS',1,1034 UNION ALL
  SELECT 'DRYGOODS-0497','EDAMAME','DRYGOODS','K','GRAM',500,31.9 UNION ALL
  SELECT 'OLAHAN-0031','PAHA AYAM','OLAHAN','K','GRAM',1000,59.4 UNION ALL
  SELECT 'DRYGOODS-0027','BERAS JEPANG','DRYGOODS','K','GRAM',1000,33 UNION ALL
  SELECT 'CK-0089','TORI SKIN - CK','CK','K','GRAM',1,51.9 UNION ALL
  SELECT 'DRYGOODS-0317','TELUR AYAM NEGERI','DRYGOODS','K','GRAM',1000,24.75 UNION ALL
  SELECT 'CKE-0327','CHASIU AYAM','CKE','K','GRAM',1,153 UNION ALL
  SELECT 'DRYGOODS-0008','AJITSUKE INARI','DRYGOODS','S','PCS',40,2392.5 UNION ALL
  SELECT 'DRYGOODS-0133','HOTATE','DRYGOODS','S','GRAM',1000,1045 UNION ALL
  SELECT 'DRYGOODS-0150','KIBUN KANI STICK','DRYGOODS','S','GRAM',500,110
) v;

-- finished menu item (target of the recipe below); sold at a real menu price
INSERT INTO master_item (item_code, description, item_class, category_id, outlet_id,
                         base_unit, purchase_unit, qty_per_purchase_unit, current_cost, selling_price, status)
VALUES ('SH00001','EDAMAME (finished dish)','MENU',
        (SELECT category_id FROM category WHERE category_code='K'),
        (SELECT outlet_id FROM outlet WHERE outlet_code='SHLMP'),
        'PORTION','PORTION',1,0,35000,'Active');

-- ---- Extra users (password = admin123) ----
INSERT INTO `user` (username, password_hash, role, outlet_id, status) VALUES
 ('manager', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Manager', 1, 'Active'),
 ('staff',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Staff',   1, 'Active');

-- ---- OPENING BALANCE (End Balance column) ----
INSERT INTO end_balance (item_id, period_date, end_balance, unit_cost, extended_cost)
SELECT mi.item_id, DATE '2025-11-10', v.qty, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'ALKOHOL-0014' code, 20 qty UNION ALL SELECT 'ALKOHOL-0018',21 UNION ALL
  SELECT 'CK-0205',20 UNION ALL SELECT 'DRYGOODS-0122',14000 UNION ALL
  SELECT 'SOFTDRINK-0021',31 UNION ALL SELECT 'NONALKOHOL-0674',1940 UNION ALL
  SELECT 'PACKAGING-0004',114 UNION ALL SELECT 'PACKAGING-0010',575 UNION ALL
  SELECT 'PACKAGING-0051',2 UNION ALL SELECT 'PACKAGING-0735',118 UNION ALL
  SELECT 'DRYGOODS-0497',12060 UNION ALL SELECT 'OLAHAN-0031',68289 UNION ALL
  SELECT 'DRYGOODS-0027',160000 UNION ALL SELECT 'CK-0089',2915 UNION ALL
  SELECT 'DRYGOODS-0317',44513 UNION ALL SELECT 'CKE-0327',3572 UNION ALL
  SELECT 'DRYGOODS-0008',326 UNION ALL SELECT 'DRYGOODS-0133',1358 UNION ALL
  SELECT 'DRYGOODS-0150',8881
) v JOIN master_item mi ON mi.item_code = v.code;

-- ---- RECEIVING (Receiving column) — one receipt, all items ----
INSERT INTO receiving_header (receipt_number, receipt_type, receipt_date, po_number,
                              vendor_id, outlet_id, posting_status, posted_user_id)
VALUES ('RCV-2026-00001','Standard PO', CURDATE() - INTERVAL 5 DAY, 'PO-AUDIT-1',
        (SELECT vendor_id FROM vendor WHERE vendor_code='M080'),
        (SELECT outlet_id FROM outlet WHERE outlet_code='SHLMP'),
        'Posted', (SELECT user_id FROM `user` WHERE username='admin'));

INSERT INTO receiving_detail (receiving_id, item_id, qty_received, qty_invoiced, qty_returned, unit, unit_cost, extended_cost)
SELECT (SELECT receiving_id FROM receiving_header WHERE receipt_number='RCV-2026-00001'),
       mi.item_id, v.qty, v.qty, 0, mi.base_unit, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'ALKOHOL-0014' code, 48 qty UNION ALL SELECT 'ALKOHOL-0018',24 UNION ALL
  SELECT 'CK-0205',48 UNION ALL SELECT 'DRYGOODS-0122',62000 UNION ALL
  SELECT 'SOFTDRINK-0021',48 UNION ALL SELECT 'NONALKOHOL-0674',3000 UNION ALL
  SELECT 'PACKAGING-0004',900 UNION ALL SELECT 'PACKAGING-0010',550 UNION ALL
  SELECT 'PACKAGING-0051',1950 UNION ALL SELECT 'PACKAGING-0735',300 UNION ALL
  SELECT 'DRYGOODS-0497',60000 UNION ALL SELECT 'OLAHAN-0031',235000 UNION ALL
  SELECT 'DRYGOODS-0027',1330000 UNION ALL SELECT 'CK-0089',7000 UNION ALL
  SELECT 'DRYGOODS-0317',180000 UNION ALL SELECT 'CKE-0327',21365 UNION ALL
  SELECT 'DRYGOODS-0008',4400 UNION ALL SELECT 'DRYGOODS-0133',11000 UNION ALL
  SELECT 'DRYGOODS-0150',87500
) v JOIN master_item mi ON mi.item_code = v.code;

-- ---- SALES (Sales column) — 3 invoices on 3 recent days ----
-- INV1 today: Bar items
INSERT INTO sales_header (invoice_number, sale_date, outlet_id, total_amount)
VALUES ('INV-2026-00001', CURDATE(), (SELECT outlet_id FROM outlet WHERE outlet_code='SHLMP'), 0);
INSERT INTO sales_detail (sales_id, item_id, quantity, unit_price, extended_price)
SELECT (SELECT sales_id FROM sales_header WHERE invoice_number='INV-2026-00001'),
       mi.item_id, v.qty, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'ALKOHOL-0014' code,30 qty UNION ALL SELECT 'ALKOHOL-0018',23 UNION ALL
  SELECT 'CK-0205',46 UNION ALL SELECT 'SOFTDRINK-0021',43 UNION ALL
  SELECT 'NONALKOHOL-0674',3780
) v JOIN master_item mi ON mi.item_code = v.code;

-- INV2 yesterday: Kitchen items
INSERT INTO sales_header (invoice_number, sale_date, outlet_id, total_amount)
VALUES ('INV-2026-00002', CURDATE() - INTERVAL 1 DAY, (SELECT outlet_id FROM outlet WHERE outlet_code='SHLMP'), 0);
INSERT INTO sales_detail (sales_id, item_id, quantity, unit_price, extended_price)
SELECT (SELECT sales_id FROM sales_header WHERE invoice_number='INV-2026-00002'),
       mi.item_id, v.qty, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'DRYGOODS-0497' code,65700 qty UNION ALL SELECT 'OLAHAN-0031',251690 UNION ALL
  SELECT 'CK-0089',9400 UNION ALL SELECT 'DRYGOODS-0317',49840 UNION ALL
  SELECT 'CKE-0327',23610
) v JOIN master_item mi ON mi.item_code = v.code;

-- INV3 3 days ago: Food + Sushi items
INSERT INTO sales_header (invoice_number, sale_date, outlet_id, total_amount)
VALUES ('INV-2026-00003', CURDATE() - INTERVAL 3 DAY, (SELECT outlet_id FROM outlet WHERE outlet_code='SHLMP'), 0);
INSERT INTO sales_detail (sales_id, item_id, quantity, unit_price, extended_price)
SELECT (SELECT sales_id FROM sales_header WHERE invoice_number='INV-2026-00003'),
       mi.item_id, v.qty, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'PACKAGING-0004' code,964 qty UNION ALL SELECT 'PACKAGING-0010',499 UNION ALL
  SELECT 'PACKAGING-0051',1895 UNION ALL SELECT 'PACKAGING-0735',198 UNION ALL
  SELECT 'DRYGOODS-0008',3907 UNION ALL SELECT 'DRYGOODS-0133',10933 UNION ALL
  SELECT 'DRYGOODS-0150',120467
) v JOIN master_item mi ON mi.item_code = v.code;

UPDATE sales_header sh
SET total_amount = (SELECT COALESCE(SUM(extended_price),0) FROM sales_detail sd WHERE sd.sales_id = sh.sales_id)
WHERE invoice_number IN ('INV-2026-00001','INV-2026-00002','INV-2026-00003');

-- ---- MOVEMENTS ----
-- Transfer IN
INSERT INTO movement (item_id, movement_date, movement_type, note, quantity, unit, unit_cost, extended_cost)
SELECT mi.item_id, CURDATE() - INTERVAL 4 DAY, 'IN', 'Transfer in (audit period)', v.qty, mi.base_unit, mi.current_cost, v.qty*mi.current_cost
FROM (SELECT 'CK-0089' code,1000 qty UNION ALL SELECT 'DRYGOODS-0008',80 UNION ALL SELECT 'DRYGOODS-0133',135) v
JOIN master_item mi ON mi.item_code = v.code;

-- WASTE
INSERT INTO movement (item_id, movement_date, movement_type, note, quantity, unit, unit_cost, extended_cost)
SELECT mi.item_id, CURDATE() - INTERVAL 2 DAY, 'WASTE', 'Spoiled / over-cook', v.qty, mi.base_unit, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'CK-0205' code,1 qty UNION ALL SELECT 'SOFTDRINK-0021',1 UNION ALL
  SELECT 'PACKAGING-0735',83 UNION ALL SELECT 'OLAHAN-0031',4041 UNION ALL
  SELECT 'CK-0089',1400 UNION ALL SELECT 'DRYGOODS-0008',22 UNION ALL
  SELECT 'DRYGOODS-0133',2961
) v JOIN master_item mi ON mi.item_code = v.code;

-- CONSUMPTION (Pemakaian)
INSERT INTO movement (item_id, movement_date, movement_type, note, quantity, unit, unit_cost, extended_cost)
SELECT mi.item_id, CURDATE() - INTERVAL 2 DAY, 'CONSUMPTION', 'Kitchen usage', v.qty, mi.base_unit, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'DRYGOODS-0122' code,72000 qty UNION ALL SELECT 'PACKAGING-0010',204 UNION ALL
  SELECT 'DRYGOODS-0027',1410000 UNION ALL SELECT 'DRYGOODS-0317',157117 UNION ALL
  SELECT 'DRYGOODS-0150',11996
) v JOIN master_item mi ON mi.item_code = v.code;

-- Transfer OUT (requires destination outlet)
INSERT INTO movement (item_id, destination_outlet_id, movement_date, movement_type, delivery_note_number, quantity, unit, unit_cost, extended_cost)
SELECT mi.item_id, (SELECT outlet_id FROM outlet WHERE outlet_code='SHSCI'),
       CURDATE() - INTERVAL 1 DAY, 'OUT', 'DN-2026-001', v.qty, mi.base_unit, mi.current_cost, v.qty*mi.current_cost
FROM (
  SELECT 'CK-0205' code,6 qty UNION ALL SELECT 'DRYGOODS-0497',2000 UNION ALL
  SELECT 'OLAHAN-0031',3130 UNION ALL SELECT 'DRYGOODS-0027',30000 UNION ALL
  SELECT 'DRYGOODS-0008',240 UNION ALL SELECT 'DRYGOODS-0133',216 UNION ALL
  SELECT 'DRYGOODS-0150',500
) v JOIN master_item mi ON mi.item_code = v.code;

-- ---- AUDIT (Audit physical count + variance) ----
INSERT INTO audit (item_id, category_id, audit_date, audit_quantity, variance, note)
SELECT mi.item_id, mi.category_id, CURDATE() - INTERVAL 1 DAY, v.aud, v.var, NULL
FROM (
  SELECT 'ALKOHOL-0014' code,38 aud,0 var UNION ALL SELECT 'ALKOHOL-0018',22,0 UNION ALL
  SELECT 'CK-0205',16,1 UNION ALL SELECT 'DRYGOODS-0122',12000,8000 UNION ALL
  SELECT 'SOFTDRINK-0021',35,0 UNION ALL SELECT 'NONALKOHOL-0674',1350,190 UNION ALL
  SELECT 'PACKAGING-0004',50,0 UNION ALL SELECT 'PACKAGING-0010',410,-12 UNION ALL
  SELECT 'PACKAGING-0051',57,0 UNION ALL SELECT 'PACKAGING-0735',142,5 UNION ALL
  SELECT 'DRYGOODS-0497',6765,2405 UNION ALL SELECT 'OLAHAN-0031',45335,907 UNION ALL
  SELECT 'DRYGOODS-0027',52000,2000 UNION ALL SELECT 'CK-0089',375,260 UNION ALL
  SELECT 'DRYGOODS-0317',22380,4824 UNION ALL SELECT 'CKE-0327',3300,1973 UNION ALL
  SELECT 'DRYGOODS-0008',580,-57 UNION ALL SELECT 'DRYGOODS-0133',285,1902 UNION ALL
  SELECT 'DRYGOODS-0150',8630,45212
) v JOIN master_item mi ON mi.item_code = v.code;

-- ---- RECIPE (from RESEP.csv: SH00001 EDAMAME made from raw EDAMAME) ----
INSERT INTO recipe_header (recipe_code, item_id, item_class)
VALUES ('RCP-2026-0001', (SELECT item_id FROM master_item WHERE item_code='SH00001'), 'MENU');
INSERT INTO recipe_detail (recipe_id, item_id, initial_weight, final_weight, waste_percentage, unit)
VALUES ((SELECT recipe_id FROM recipe_header WHERE recipe_code='RCP-2026-0001'),
        (SELECT item_id FROM master_item WHERE item_code='DRYGOODS-0497'), 150, 150, 0, 'GRAM');

-- =============================================================================
-- Sanity check after load (computed SOH should match the audit sheet's SOH):
--   SELECT item_code, soh_computed FROM v_stock_on_hand ORDER BY item_code;
--   e.g. BEER SAPORO=38, PAHA AYAM=44428, EDAMAME=4360, HOTATE=-1617 (real quirk).
-- Logins: admin/manager/staff, all password admin123.
-- =============================================================================
