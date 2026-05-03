# management-stock-audit

SHLMP Inventory Management System — Java Swing + MySQL.

## Prasyarat

- **XAMPP** (Apache + MySQL) — https://www.apachefriends.org/
- **NetBeans 21+** dengan **JDK 25** — https://netbeans.apache.org/
- **Git** (untuk clone repo)

MySQL Connector (`lib/mysql-connector-j-9.7.0.jar`) sudah ikut di repo, tidak perlu download lagi.

## Setup

### 1. Clone repo

```bash
git clone https://github.com/devdanzen/management-stock-audit.git
cd management-stock-audit
```

### 2. Setup database

1. Start XAMPP → tekan **Start** untuk **Apache** dan **MySQL**
2. Buka http://localhost/phpmyadmin
3. Klik tab **Import** di atas
4. Pilih file `db/schema.sql` dari folder repo
5. Klik **Go**
6. Verifikasi: di sidebar kiri muncul database **`db_shlmp`** dengan 2 tabel (`category`, `item`)

Konfigurasi default yang diasumsikan:
- Host: `localhost`
- Port: `3306`
- User: `root`
- Password: *(kosong)*

Kalau setting MySQL kamu beda, edit `src/koneksi/Koneksi.java` (baris URL/USER/PASS).

### 3. Buka project di NetBeans

1. **File → Open Project** → pilih folder `management-stock-audit`
2. Tunggu NetBeans selesai resolve dependency (status bar bawah)
3. Di panel **Projects**, expand **Libraries** → harus muncul `mysql-connector-j-9.7.0.jar`

### 4. Test koneksi

1. Buka `src/koneksi/Koneksi.java`
2. Tekan **Shift+F6** (Run File)
3. Output panel harus print:
   ```
   Koneksi DB sukses: db_shlmp
   ```

### 5. Jalankan aplikasi

1. Buka `src/tampilan/MasterItemForm.java`
2. Tekan **Shift+F6** (Run File)
3. Form Master Item muncul dengan 6 sample item ter-load di tabel

## Fitur

### Form Master Item
- **Add Item** — simpan item baru ke database
- **Update Item** — ubah item terpilih (klik baris di tabel dulu)
- **Delete Item** — hapus item terpilih (dengan dialog konfirmasi)
- **Reset** — kosongkan semua field
- Klik baris di tabel → semua field terisi otomatis
- Combo **Category** auto-load dari tabel `category` di database

## Struktur Project

```
management-stock-audit/
├── db/
│   └── schema.sql              # Database schema + sample data
├── lib/
│   └── mysql-connector-j-9.7.0.jar
├── src/
│   ├── koneksi/
│   │   └── Koneksi.java        # MySQL connection helper
│   ├── management/stock/audit/
│   │   └── ManagementStockAudit.java  # Main class
│   └── tampilan/
│       ├── MasterItemForm.java # Form Master Item (UI + logic)
│       └── MasterItemForm.form
└── README.md
```

## Troubleshooting

| Error | Penyebab | Solusi |
|---|---|---|
| `Driver MySQL tidak ditemukan` | Jar tidak ke-load NetBeans | Klik kanan project → Properties → Libraries → cek `mysql-connector-j-9.7.0.jar` ada |
| `Unknown database 'db_shlmp'` | Schema belum di-import | Ulangi step "Setup database" |
| `Access denied for user 'root'` | Password MySQL bukan kosong | Edit `Koneksi.java` → set `PASS` ke password MySQL kamu |
| `Communications link failure` | XAMPP MySQL belum Start | Buka XAMPP Control Panel → Start MySQL |
