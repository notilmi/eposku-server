-- Flyway Migration: V2
-- Description: Add created_at and updated_at timestamps (Split for H2 Compatibility)

-- 1. Modify Transaksi
ALTER TABLE transaksi ADD COLUMN created_at DATETIME NULL;
ALTER TABLE transaksi ADD COLUMN updated_at DATETIME NULL;

-- 2. Modify Produk
ALTER TABLE produk ADD COLUMN created_at DATETIME NULL;
ALTER TABLE produk ADD COLUMN updated_at DATETIME NULL;

-- 3. Modify Pergerakan Stok
ALTER TABLE pergerakan_stok ADD COLUMN created_at DATETIME NULL;
ALTER TABLE pergerakan_stok ADD COLUMN updated_at DATETIME NULL;

-- 4. Backfill existing records
UPDATE transaksi SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL;
UPDATE produk SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL;
UPDATE pergerakan_stok SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL;