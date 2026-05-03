CREATE DATABASE IF NOT EXISTS db_shlmp
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE db_shlmp;

DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS user_classification;

CREATE TABLE category (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(150)
) ENGINE=InnoDB;

INSERT INTO category (name, description) VALUES
  ('FOOD',            'Bahan makanan dan minuman umum'),
  ('DIRECT ITEM',     'Bahan langsung tanpa proses tambahan'),
  ('CENTRAL KITCHEN', 'Bahan dari central kitchen');

CREATE TABLE user_classification (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(150)
) ENGINE=InnoDB;

INSERT INTO user_classification (name, description) VALUES
  ('FOOD',     'Food items'),
  ('NON FOOD', 'Non-food items'),
  ('BEVERAGE', 'Drinks and beverages');

CREATE TABLE item (
    item_number          VARCHAR(30)  PRIMARY KEY,
    item_description     VARCHAR(150) NOT NULL,
    item_class           VARCHAR(50),
    uom_schedule         VARCHAR(30),
    base_uom             VARCHAR(20),
    purchasing_uom       VARCHAR(20),
    standard_cost        DECIMAL(12,2) DEFAULT 0,
    current_cost         DECIMAL(12,2) DEFAULT 0,
    qty_purchase_uom     DECIMAL(12,2) DEFAULT 1,
    price_list           DECIMAL(12,2) DEFAULT 0,
    status               VARCHAR(5)   DEFAULT 'Yes',
    user_classification  VARCHAR(30),
    item_category        VARCHAR(30),
    outlet_name          VARCHAR(20)
) ENGINE=InnoDB;

INSERT INTO item VALUES
  ('ALKOHOL-0001',  'Absolut Blue Vodka',     'ALKOHOL',  'BTL 750 ML', 'ML',    'BOTOL', 807.00,    733.00,    750.00,  549750.00, 'Yes', 'FOOD', 'FOOD',        'SHSCI'),
  ('ALKOHOL-0014',  'Beer Sapporo',           'ALKOHOL',  'DUS 24 BTL', 'BOTOL', 'DUS',   33000.00,  33000.00,  24.00,   792000.00, 'Yes', 'FOOD', 'FOOD',        'SHLMP'),
  ('IKAN-0203',     'Uni Bento Shiro Narabi', 'IKAN',     'PAK 100 GR', 'GRAM',  'PAK',   7100.00,   7100.00,   100.00,  710000.00, 'Yes', 'FOOD', 'DIRECT ITEM', 'SHLMP'),
  ('OLAHAN-0063',   'Salmon Bersih',          'OLAHAN',   'GRAM',       'GRAM',  'GRAM',  260.00,    260.00,    1.00,    260.00,    'Yes', 'FOOD', 'FOOD',        'SHLMP'),
  ('DRYGOODS-0027', 'Beras Jepang',           'DRYGOODS', 'SAK 25 KG',  'GRAM',  'SAK',   264.00,    264.00,    25000.00,6600000.00,'Yes', 'FOOD', 'FOOD',        'SHLMP'),
  ('DRYGOODS-0133', 'Hotate',                 'DRYGOODS', 'PAK 1 KG',   'GRAM',  'PAK',   1045.00,   1045.00,   1000.00, 1045000.00,'Yes', 'FOOD', 'FOOD',        'SHLMP');
