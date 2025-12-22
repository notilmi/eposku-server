-- Flyway Migration: Initial DDL for eposku-server
-- Database: MySQL
-- Notes:
-- - Uses utf8mb4 charset/collation
-- - Child collections for produk use produk_id (FK) and produk_key (list index)
-- - Column names use snake_case to match Spring Data JDBC default naming

-- H2 (MODE=MySQL) doesn't support SET NAMES; keep this migration portable.

-- Table: users
CREATE TABLE IF NOT EXISTS users
(
    id        INT          NOT NULL AUTO_INCREMENT,
    username  VARCHAR(100) NOT NULL,
    full_name VARCHAR(255) NULL,
    password  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_username UNIQUE (username)
);

-- Table: sessions
CREATE TABLE IF NOT EXISTS sessions
(
    id            INT          NOT NULL AUTO_INCREMENT,
    user_id       INT          NOT NULL,
    valid_through DATE         NOT NULL,
    token         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_sessions PRIMARY KEY (id),
    CONSTRAINT fk_sessions_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE INDEX idx_sessions_user_id ON sessions (user_id);
CREATE INDEX idx_sessions_token ON sessions (token);

-- Table: produk (aggregate root)
CREATE TABLE IF NOT EXISTS produk
(
    id        INT          NOT NULL AUTO_INCREMENT,
    nama      VARCHAR(255) NOT NULL,
    deskripsi TEXT         NULL,
    harga     DOUBLE       NOT NULL,
    image_url TEXT         NULL,
    CONSTRAINT pk_produk PRIMARY KEY (id)
);
CREATE INDEX idx_produk_nama ON produk (nama);

CREATE TABLE IF NOT EXISTS user_uploaded_file
(
    id             INT          NOT NULL AUTO_INCREMENT,
    image_provider VARCHAR(100) NOT NULL,
    image_url      VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT pk_user_uploaded_file PRIMARY KEY (id)
);
CREATE INDEX idx_user_uploaded_file_image_provider ON user_uploaded_file (image_provider);
CREATE INDEX idx_user_uploaded_file_image_url ON user_uploaded_file (image_url);

-- Table: pergerakan_stok (child collection of produk)
-- Includes produk_id (FK) and produk_key (list order/index)
CREATE TABLE IF NOT EXISTS pergerakan_stok
(
    id            INT  NOT NULL AUTO_INCREMENT,
    produk_id     INT  NOT NULL,
    produk_key    INT  NOT NULL,
    jumlah_masuk  INT  NOT NULL DEFAULT 0,
    jumlah_keluar INT  NOT NULL DEFAULT 0,
    tanggal       DATE NOT NULL,
    CONSTRAINT pk_pergerakan_stok PRIMARY KEY (id),
    CONSTRAINT uq_pergerakan_stok_parent_key UNIQUE (produk_id, produk_key),
    CONSTRAINT fk_pergerakan_stok_produk FOREIGN KEY (produk_id)
        REFERENCES produk (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE INDEX idx_pergerakan_stok_produk_id ON pergerakan_stok (produk_id);
CREATE INDEX idx_pergerakan_stok_tanggal ON pergerakan_stok (tanggal);

-- Table: transaksi (child collection of produk)
-- No explicit @Id on entity, so composite key of (produk_id, produk_key) is used
CREATE TABLE IF NOT EXISTS transaksi
(
    id         INT    NOT NULL AUTO_INCREMENT,
    produk_id  INT    NOT NULL,
    produk_key INT    NOT NULL,
    jumlah     INT    NOT NULL,
    diskon     DOUBLE NOT NULL,
    harga      DOUBLE NOT NULL,
    tanggal    DATE   NOT NULL,
    CONSTRAINT pk_transaksi_id PRIMARY KEY (id),
    CONSTRAINT pk_transaksi_parent_key UNIQUE (produk_id, produk_key),
    CONSTRAINT fk_transaksi_produk FOREIGN KEY (produk_id)
        REFERENCES produk (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE INDEX idx_transaksi_tanggal ON transaksi (tanggal);
