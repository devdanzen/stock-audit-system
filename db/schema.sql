-- =============================================================================
-- Stock Audit System - Database Schema
-- Final Version: Full English Naming + CHECK Constraints for Data Integrity
-- Dialect: MySQL 8.0+ / MariaDB 10.5+
-- Target: XAMPP + phpMyAdmin + NetBeans Java Swing
--
-- HOW TO IMPORT (phpMyAdmin):
--   1. Open phpMyAdmin (http://localhost/phpmyadmin)
--   2. Click "Import" tab (no need to create/select a DB first -
--      this script creates and uses stock_audit_db automatically)
--   3. Choose this file → click "Import" button
--   4. Verify: 14 tables, 4 views, seed data should appear
--
-- HOW TO IMPORT (MySQL CLI):
--   mysql -u root -p < schema.sql
-- =============================================================================

-- Create database and select it (makes this script self-contained)
CREATE DATABASE IF NOT EXISTS stock_audit_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stock_audit_db;

-- Drop existing tables (in reverse dependency order)
SET FOREIGN_KEY_CHECKS = 0;
DROP VIEW IF EXISTS v_top_waste;
DROP VIEW IF EXISTS v_daily_sales;
DROP VIEW IF EXISTS v_top_sellers;
DROP VIEW IF EXISTS v_stock_on_hand;
DROP TABLE IF EXISTS sales_detail;
DROP TABLE IF EXISTS sales_header;
DROP TABLE IF EXISTS receiving_detail;
DROP TABLE IF EXISTS receiving_header;
DROP TABLE IF EXISTS recipe_detail;
DROP TABLE IF EXISTS recipe_header;
DROP TABLE IF EXISTS movement;
DROP TABLE IF EXISTS audit;
DROP TABLE IF EXISTS end_balance;
DROP TABLE IF EXISTS master_item;
DROP TABLE IF EXISTS vendor;
DROP TABLE IF EXISTS outlet;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS `user`;
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- REFERENCE / LOOKUP TABLES
-- =============================================================================

-- USER table (authentication only, no FK to business data)
CREATE TABLE `user` (
    user_id         INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            ENUM('Administrator', 'Manager', 'Staff') DEFAULT 'Staff',
    outlet_id       INT,
    status          ENUM('Active', 'Inactive') DEFAULT 'Active',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at   TIMESTAMP NULL,

    CONSTRAINT chk_username_not_empty CHECK (CHAR_LENGTH(TRIM(username)) > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CATEGORY (section codes: B=Bar, F=Food, K=Kitchen, S=Sushi)
CREATE TABLE category (
    category_id     INT AUTO_INCREMENT PRIMARY KEY,
    category_code   CHAR(2)     NOT NULL UNIQUE,
    category_name   VARCHAR(50) NOT NULL,

    CONSTRAINT chk_category_code_uppercase CHECK (category_code = UPPER(category_code))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- OUTLET (physical store locations)
CREATE TABLE outlet (
    outlet_id       INT AUTO_INCREMENT PRIMARY KEY,
    outlet_code     VARCHAR(10)  NOT NULL UNIQUE,
    outlet_name     VARCHAR(100) NOT NULL,
    status          ENUM('Active', 'Inactive') DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- VENDOR (suppliers)
CREATE TABLE vendor (
    vendor_id       INT AUTO_INCREMENT PRIMARY KEY,
    vendor_code     VARCHAR(20)  NOT NULL UNIQUE,
    vendor_name     VARCHAR(150) NOT NULL,
    contact_phone   VARCHAR(30),
    status          ENUM('Active', 'Inactive') DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- CORE ENTITY: MASTER_ITEM
-- =============================================================================
CREATE TABLE master_item (
    item_id              INT AUTO_INCREMENT PRIMARY KEY,
    item_code            VARCHAR(50)  NOT NULL UNIQUE,
    description          VARCHAR(255) NOT NULL,
    item_class           VARCHAR(50),
    category_id          INT,
    outlet_id            INT,
    base_unit            VARCHAR(20),
    purchase_unit        VARCHAR(20),
    qty_per_purchase_unit DECIMAL(18,4) DEFAULT 0,
    current_cost         DECIMAL(18,4) DEFAULT 0,
    selling_price        DECIMAL(18,4) DEFAULT 0,
    status               ENUM('Active', 'Inactive') DEFAULT 'Active',

    CONSTRAINT fk_item_category FOREIGN KEY (category_id)
        REFERENCES category(category_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_item_outlet FOREIGN KEY (outlet_id)
        REFERENCES outlet(outlet_id)
        ON UPDATE CASCADE ON DELETE SET NULL,

    CONSTRAINT chk_item_cost_nonneg CHECK (current_cost >= 0),
    CONSTRAINT chk_item_price_nonneg CHECK (selling_price >= 0),
    CONSTRAINT chk_item_qty_pu_positive CHECK (qty_per_purchase_unit > 0),

    INDEX idx_item_category (category_id),
    INDEX idx_item_outlet (outlet_id),
    INDEX idx_item_code (item_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Add FK from user to outlet (now that outlet exists)
ALTER TABLE `user`
    ADD CONSTRAINT fk_user_outlet FOREIGN KEY (outlet_id)
        REFERENCES outlet(outlet_id)
        ON UPDATE CASCADE ON DELETE SET NULL;

-- =============================================================================
-- M-M #1: RECEIVING (header + detail)
-- =============================================================================
CREATE TABLE receiving_header (
    receiving_id        INT AUTO_INCREMENT PRIMARY KEY,
    receipt_number      VARCHAR(50) NOT NULL UNIQUE,
    receipt_type        VARCHAR(30),
    receipt_date        DATE        NOT NULL,
    po_number           VARCHAR(50),
    vendor_id           INT,
    outlet_id           INT,
    posting_status      ENUM('Pending', 'Approved', 'Posted') DEFAULT 'Pending',
    posted_user_id      INT,

    CONSTRAINT fk_rcv_vendor FOREIGN KEY (vendor_id)
        REFERENCES vendor(vendor_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_rcv_outlet FOREIGN KEY (outlet_id)
        REFERENCES outlet(outlet_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_rcv_user FOREIGN KEY (posted_user_id)
        REFERENCES `user`(user_id)
        ON UPDATE CASCADE ON DELETE SET NULL,

    INDEX idx_rcv_date (receipt_date),
    INDEX idx_rcv_vendor (vendor_id),
    INDEX idx_rcv_outlet (outlet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE receiving_detail (
    receiving_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    receiving_id        INT NOT NULL,
    item_id             INT NOT NULL,
    qty_received        DECIMAL(18,4) NOT NULL DEFAULT 0,
    qty_invoiced        DECIMAL(18,4) NOT NULL DEFAULT 0,
    qty_returned        DECIMAL(18,4) DEFAULT 0,
    unit                VARCHAR(20),
    unit_cost           DECIMAL(18,4) NOT NULL DEFAULT 0,
    extended_cost       DECIMAL(18,4) NOT NULL DEFAULT 0,

    CONSTRAINT fk_rd_header FOREIGN KEY (receiving_id)
        REFERENCES receiving_header(receiving_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rd_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT chk_rcv_qty_positive CHECK (qty_received > 0),
    CONSTRAINT chk_rcv_qty_invoiced_nonneg CHECK (qty_invoiced >= 0),
    CONSTRAINT chk_rcv_qty_returned_nonneg CHECK (qty_returned >= 0),
    CONSTRAINT chk_rcv_unit_cost_nonneg CHECK (unit_cost >= 0),
    CONSTRAINT chk_rcv_ext_cost_nonneg CHECK (extended_cost >= 0),

    INDEX idx_rd_header (receiving_id),
    INDEX idx_rd_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- M-M #2: SALES (header + detail)
-- =============================================================================
CREATE TABLE sales_header (
    sales_id        INT AUTO_INCREMENT PRIMARY KEY,
    invoice_number  VARCHAR(50) NOT NULL UNIQUE,
    sale_date       DATE        NOT NULL,
    outlet_id       INT,
    total_amount    DECIMAL(18,4) DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sh_outlet FOREIGN KEY (outlet_id)
        REFERENCES outlet(outlet_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT chk_sales_total_nonneg CHECK (total_amount >= 0),

    INDEX idx_sales_date (sale_date),
    INDEX idx_sales_outlet (outlet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sales_detail (
    sales_detail_id  INT AUTO_INCREMENT PRIMARY KEY,
    sales_id         INT NOT NULL,
    item_id          INT NOT NULL,
    quantity         DECIMAL(18,4) NOT NULL DEFAULT 0,
    unit_price       DECIMAL(18,4) NOT NULL DEFAULT 0,
    extended_price   DECIMAL(18,4) NOT NULL DEFAULT 0,

    CONSTRAINT fk_sd_header FOREIGN KEY (sales_id)
        REFERENCES sales_header(sales_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_sd_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT chk_sales_qty_positive CHECK (quantity > 0),
    CONSTRAINT chk_sales_price_nonneg CHECK (unit_price >= 0),
    CONSTRAINT chk_sales_ext_price_nonneg CHECK (extended_price >= 0),

    INDEX idx_sd_header (sales_id),
    INDEX idx_sd_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- M-M #3: RECIPE (header + detail) - 1:0..1 from master_item via UNIQUE
-- =============================================================================
CREATE TABLE recipe_header (
    recipe_id       INT AUTO_INCREMENT PRIMARY KEY,
    recipe_code     VARCHAR(50) NOT NULL UNIQUE,
    item_id         INT NOT NULL UNIQUE,         -- UNIQUE enforces 1:0..1
    item_class      VARCHAR(50),

    CONSTRAINT fk_recipe_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE recipe_detail (
    recipe_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    recipe_id        INT NOT NULL,
    item_id          INT NOT NULL,               -- the ingredient
    initial_weight   DECIMAL(18,4) NOT NULL DEFAULT 0,
    final_weight     DECIMAL(18,4) NOT NULL DEFAULT 0,
    waste_percentage DECIMAL(8,4) DEFAULT 0,
    unit             VARCHAR(20),

    CONSTRAINT fk_rcd_header FOREIGN KEY (recipe_id)
        REFERENCES recipe_header(recipe_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rcd_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT chk_initial_wt_positive CHECK (initial_weight > 0),
    CONSTRAINT chk_final_wt_positive CHECK (final_weight > 0),
    CONSTRAINT chk_final_lte_initial CHECK (final_weight <= initial_weight),
    CONSTRAINT chk_waste_pct_range CHECK (waste_percentage >= 0 AND waste_percentage <= 100),

    INDEX idx_rcd_header (recipe_id),
    INDEX idx_rcd_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- MOVEMENT - IN / OUT / WASTE / CONSUMPTION
-- destination_outlet_id is only populated when movement_type = 'OUT'
-- =============================================================================
CREATE TABLE movement (
    movement_id            INT AUTO_INCREMENT PRIMARY KEY,
    item_id                INT NOT NULL,
    destination_outlet_id  INT,                  -- nullable, only for OUT
    movement_date          DATE        NOT NULL,
    movement_type          ENUM('IN','OUT','WASTE','CONSUMPTION') NOT NULL,
    note                   VARCHAR(255),         -- reason or description
    delivery_note_number   VARCHAR(50),          -- for OUT transfers
    quantity               DECIMAL(18,4) NOT NULL DEFAULT 0,
    unit                   VARCHAR(20),
    unit_cost              DECIMAL(18,4) DEFAULT 0,
    extended_cost          DECIMAL(18,4) DEFAULT 0,

    CONSTRAINT fk_mov_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_mov_outlet FOREIGN KEY (destination_outlet_id)
        REFERENCES outlet(outlet_id)
        ON UPDATE CASCADE ON DELETE SET NULL,

    CONSTRAINT chk_movement_qty_positive CHECK (quantity > 0),
    CONSTRAINT chk_movement_cost_nonneg CHECK (unit_cost >= 0),
    CONSTRAINT chk_movement_out_has_dest CHECK (
        movement_type != 'OUT' OR destination_outlet_id IS NOT NULL
    ),

    INDEX idx_mov_date (movement_date),
    INDEX idx_mov_type (movement_type),
    INDEX idx_mov_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- AUDIT - physical stock count vs system SOH
-- =============================================================================
CREATE TABLE audit (
    audit_id        INT AUTO_INCREMENT PRIMARY KEY,
    item_id         INT NOT NULL,
    category_id     INT,
    audit_date      DATE NOT NULL,
    audit_quantity  DECIMAL(18,4) NOT NULL DEFAULT 0,
    variance        DECIMAL(18,4) DEFAULT 0,
    note            VARCHAR(255),

    CONSTRAINT fk_audit_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_audit_category FOREIGN KEY (category_id)
        REFERENCES category(category_id)
        ON UPDATE CASCADE ON DELETE SET NULL,

    CONSTRAINT chk_audit_qty_nonneg CHECK (audit_quantity >= 0),

    UNIQUE KEY uq_audit_item_date (item_id, audit_date),
    INDEX idx_audit_date (audit_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- END_BALANCE - period-end inventory snapshot
-- =============================================================================
CREATE TABLE end_balance (
    end_balance_id  INT AUTO_INCREMENT PRIMARY KEY,
    item_id         INT NOT NULL,
    period_date     DATE NOT NULL,            -- last day of period (e.g., 2025-12-31)
    end_balance     DECIMAL(18,4) DEFAULT 0,
    unit_cost       DECIMAL(18,4) DEFAULT 0,
    extended_cost   DECIMAL(18,4) DEFAULT 0,

    CONSTRAINT fk_endbal_item FOREIGN KEY (item_id)
        REFERENCES master_item(item_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT chk_endbal_nonneg CHECK (end_balance >= 0),
    CONSTRAINT chk_endbal_cost_nonneg CHECK (unit_cost >= 0),

    UNIQUE KEY uq_endbal_item_period (item_id, period_date),
    INDEX idx_endbal_period (period_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- SEED DATA - reference tables
-- =============================================================================

INSERT INTO category (category_code, category_name) VALUES
    ('B', 'Bar'),
    ('F', 'Food'),
    ('K', 'Kitchen'),
    ('S', 'Sushi');

INSERT INTO outlet (outlet_code, outlet_name, status) VALUES
    ('SHLMP', 'SH Lippo Mall Puri',    'Active'),
    ('SHSCI', 'SH SCBD',                'Active'),
    ('SHMTA', 'SH Mall Taman Anggrek', 'Active'),
    ('SHNEO', 'SH Neo Soho',            'Active'),
    ('SHALS', 'SH Alam Sutera',         'Active'),
    ('SHPLU', 'SH Pluit',               'Active'),
    ('SHBIN', 'SH Bintaro',             'Active'),
    ('SHISL', 'SH Senayan',             'Active'),
    ('SHBEX', 'SH Bekasi',              'Inactive');

INSERT INTO vendor (vendor_code, vendor_name, contact_phone, status) VALUES
    ('M080', 'Mahkota Ice Distribution',   '+62 21 5550-1234', 'Active'),
    ('M105', 'Sumber Pangan Sejahtera',    '+62 21 5550-2345', 'Active'),
    ('M203', 'PT Ikan Segar Nusantara',    '+62 21 5550-3456', 'Active'),
    ('M298', 'Toko Bahan Kering Mulia',    '+62 21 5550-4567', 'Active'),
    ('M340', 'Distributor Minuman Asia',   '+62 21 5550-5678', 'Active'),
    ('M421', 'Sayur Mayur Pak Budi',       '+62 21 5550-6789', 'Inactive');

-- Default admin user - username=admin, password=admin123 (BCrypt hash)
-- Generate new hashes in Java via BCrypt.hashpw("password", BCrypt.gensalt())
INSERT INTO `user` (username, password_hash, role, status) VALUES
    ('admin', '$2a$12$Sbq3UwQzp77awtyw6TWHy.sB.D/hJGXrjY5qf.aBhNGfAeI9D58gC', 'Administrator', 'Active');

-- Sample master items (for testing the connection works)
INSERT INTO master_item (item_code, description, item_class, category_id, outlet_id, base_unit, qty_per_purchase_unit, current_cost, selling_price) VALUES
    ('LIQ-0001',  'Absolut Blue Vodka',  'Drink', 1, 1, 'BOTTLE', 1, 485000, 850000),
    ('DRY-0497',  'Edamame',             'Dry',   3, 1, 'GRAM',   1, 450,    0),
    ('FISH-0042', 'Salmon Trout',        'Fish',  4, 1, 'GRAM',   1, 180000, 0),
    ('MENU-001',  'Edamame (finished)',  'Menu',  3, 1, 'PORTION', 1, 0,     35000);

-- =============================================================================
-- VIEWS - replicate audit sheet's SUMIFS calculations
-- =============================================================================

CREATE OR REPLACE VIEW v_stock_on_hand AS
SELECT
    mi.item_id,
    mi.item_code,
    mi.description,
    COALESCE(eb.last_end_balance, 0)              AS end_balance,
    COALESCE(rcv.total_received, 0)               AS total_received,
    COALESCE(mov_in.total_qty, 0)                 AS total_transfer_in,
    COALESCE(sd.total_sold, 0)                    AS total_sold,
    COALESCE(mov_csm.total_qty, 0)                AS total_consumption,
    COALESCE(mov_waste.total_qty, 0)              AS total_waste,
    COALESCE(mov_out.total_qty, 0)                AS total_transfer_out,
    (COALESCE(eb.last_end_balance, 0)
     + COALESCE(rcv.total_received, 0)
     + COALESCE(mov_in.total_qty, 0))
    - (COALESCE(sd.total_sold, 0)
       + COALESCE(mov_waste.total_qty, 0)
       + COALESCE(mov_out.total_qty, 0)
       + COALESCE(mov_csm.total_qty, 0))
    AS soh_computed
FROM master_item mi
LEFT JOIN (
    SELECT item_id, SUM(end_balance) AS last_end_balance
    FROM end_balance GROUP BY item_id
) eb ON eb.item_id = mi.item_id
LEFT JOIN (
    SELECT item_id, SUM(qty_received) AS total_received
    FROM receiving_detail GROUP BY item_id
) rcv ON rcv.item_id = mi.item_id
LEFT JOIN (
    SELECT item_id, SUM(quantity) AS total_qty FROM movement
    WHERE movement_type = 'IN' GROUP BY item_id
) mov_in ON mov_in.item_id = mi.item_id
LEFT JOIN (
    SELECT item_id, SUM(quantity) AS total_qty FROM movement
    WHERE movement_type = 'OUT' GROUP BY item_id
) mov_out ON mov_out.item_id = mi.item_id
LEFT JOIN (
    SELECT item_id, SUM(quantity) AS total_qty FROM movement
    WHERE movement_type = 'WASTE' GROUP BY item_id
) mov_waste ON mov_waste.item_id = mi.item_id
LEFT JOIN (
    SELECT item_id, SUM(quantity) AS total_qty FROM movement
    WHERE movement_type = 'CONSUMPTION' GROUP BY item_id
) mov_csm ON mov_csm.item_id = mi.item_id
LEFT JOIN (
    SELECT item_id, SUM(quantity) AS total_sold
    FROM sales_detail GROUP BY item_id
) sd ON sd.item_id = mi.item_id;

CREATE OR REPLACE VIEW v_top_sellers AS
SELECT
    mi.item_id,
    mi.item_code,
    mi.description,
    SUM(sd.quantity) AS total_qty_sold,
    SUM(sd.extended_price) AS total_revenue
FROM sales_detail sd
JOIN master_item mi ON mi.item_id = sd.item_id
GROUP BY mi.item_id, mi.item_code, mi.description
ORDER BY total_qty_sold DESC;

CREATE OR REPLACE VIEW v_daily_sales AS
SELECT
    sh.sale_date,
    SUM(sd.extended_price)        AS daily_revenue,
    COUNT(DISTINCT sh.sales_id)   AS invoice_count
FROM sales_header sh
JOIN sales_detail sd ON sd.sales_id = sh.sales_id
GROUP BY sh.sale_date
ORDER BY sh.sale_date;

CREATE OR REPLACE VIEW v_top_waste AS
SELECT
    mi.item_id,
    mi.item_code,
    mi.description,
    SUM(m.quantity)      AS total_waste_qty,
    SUM(m.extended_cost) AS total_waste_cost
FROM movement m
JOIN master_item mi ON mi.item_id = m.item_id
WHERE m.movement_type = 'WASTE'
GROUP BY mi.item_id, mi.item_code, mi.description
ORDER BY total_waste_cost DESC;

-- =============================================================================
-- END OF SCHEMA
-- =============================================================================
-- After import, verify by running:
--   SHOW TABLES;                              -- should return 14 tables
--   SHOW FULL TABLES WHERE Table_type = 'VIEW';   -- should return 4 views
--   SELECT * FROM category;                   -- should return 4 rows
--   SELECT * FROM outlet;                     -- should return 9 rows
--   SELECT * FROM vendor;                     -- should return 6 rows
--   SELECT * FROM `user`;                     -- should return 1 admin row
--   SELECT * FROM master_item;                -- should return 4 sample items
--
-- Test CHECK constraints (these should ALL fail):
--   INSERT INTO sales_detail (sales_id, item_id, quantity, unit_price)
--     VALUES (1, 1, -5, 100);                 -- ERROR: chk_sales_qty_positive
--   INSERT INTO recipe_detail (recipe_id, item_id, initial_weight, final_weight)
--     VALUES (1, 1, 100, 150);                -- ERROR: chk_final_lte_initial
--   INSERT INTO movement (item_id, movement_date, movement_type, quantity)
--     VALUES (1, '2025-12-15', 'OUT', 5);     -- ERROR: chk_movement_out_has_dest
-- =============================================================================
