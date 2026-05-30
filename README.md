# Stock Audit System

Java Swing + MySQL inventory & audit system for an F&B chain.
Login + role-based access, master data CRUD, transactions (Receiving / Sales / Movement / Recipes),
real-time Stock on Hand, Audit (stock opname), End Balance snapshot, Reports (JasperReports),
and Charts (XChart). Designed for offline/desktop use.

## Prasyarat

- **XAMPP** (Apache + MySQL): https://www.apachefriends.org/
- **NetBeans 21+** dengan **JDK 25**: https://netbeans.apache.org/
- **Git**

Semua library (MySQL Connector, JasperReports, XChart, JCalendar, BCrypt, dll) sudah ada di folder `lib/`.

## Setup

### 1. Clone

```bash
git clone https://github.com/danish-deepskill/stock-audit-system.git
cd stock-audit-system
```

### 2. Import database

1. Start **MySQL** di XAMPP.
2. Buka http://localhost/phpmyadmin
3. Tab **Import**, pilih `db/schema.sql`, klik **Go**. Ini membuat database `stock_audit_db` (14 tabel + 4 views).
4. Tab **Import** lagi, pilih `db/seed_real.sql`, klik **Go**. Ini mengisi data demo (19 item, 1 receiving, 3 sales, beberapa movement, dll).

Konfigurasi MySQL yang diasumsikan:
- Host: `localhost`, Port: `3306`, User: `root`, Password: *(kosong)*

Kalau setting beda, edit `src/db/DBConnection.java`.

### 3. Buka project di NetBeans

1. **File > Open Project** > pilih folder repo.
2. Tunggu NetBeans selesai resolve dependency.
3. Cek **Libraries** di Projects panel: harus muncul semua jar di `lib/`.

### 4. Run

1. Klik **Clean and Build** (palu + sapu).
2. Klik **Run** (▶).
3. Login dengan salah satu kredensial berikut:

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | Administrator (akses penuh) |
| `manager` | `admin123` | Manager (tanpa Users) |
| `staff` | `admin123` | Staff (tanpa Users & End Balance) |

## Fitur

| Modul | Isi |
|---|---|
| **Dashboard** | KPI hari ini, recent transactions, low-stock alerts |
| **Master Data** | Items, Categories, Outlets, Vendors, Users (CRUD + soft-delete via Inactive) |
| **Receiving** | Catat barang masuk dari vendor (header + detail, atomic save) |
| **Sales** | Catat invoice penjualan dengan auto-number |
| **Movement** | IN / OUT / WASTE / CONSUMPTION, OUT membutuhkan destination outlet |
| **Recipes** | Resep menu dengan persentase waste per bahan |
| **Stock on Hand** | Stok real-time dihitung otomatis lewat `v_stock_on_hand` |
| **Audit** | Stock opname: bandingkan fisik vs sistem, hitung variance + nilai rupiah |
| **End Balance** | Snapshot saldo akhir periode |
| **Reports** | Stock / Sales / Receiving / Audit Variance lewat JasperViewer (Print + Export PDF/Excel) |
| **Charts** | Top sellers, daily sales, sales by category, top waste (XChart) |

## Struktur

```
stock-audit-system/
├── db/
│   ├── schema.sql          # Schema (14 tabel + 4 views + admin seed)
│   └── seed_real.sql       # Data demo (19 item + transaksi)
├── lib/                    # Semua dependency (mysql, jasper, xchart, jcalendar, bcrypt, dll)
├── nbproject/              # NetBeans project files
├── src/
│   ├── app/                # Main.java (entry point)
│   ├── db/                 # DBConnection
│   ├── model/              # POJO untuk 14 tabel + view DTOs
│   ├── dao/                # 12 DAO (UserDAO, SalesDAO, ReportDAO, dll)
│   ├── util/               # Theme, Fmt, SessionManager, JasperReportUtil
│   ├── view/               # 17 panel/frame (FormLoginFrame, MainFrame, + 15 panel)
│   └── reports/            # 4 template JasperReports (.jrxml)
└── README.md
```

## Troubleshooting

| Error | Penyebab | Solusi |
|---|---|---|
| `Unknown database 'stock_audit_db'` | Schema belum di-import | Ulangi step 2 (import schema.sql) |
| `Communications link failure` | MySQL belum start | Start MySQL di XAMPP |
| `Access denied for user 'root'` | Password MySQL bukan kosong | Edit `src/db/DBConnection.java`, set password |
| Login gagal | seed_real.sql belum di-import / data user terhapus | Ulangi import seed_real.sql |
| Report buka di text dialog, bukan JasperViewer | Library Jasper tidak ke-load | Cek **Libraries** di NetBeans, semua jar di `lib/` harus terdaftar |
| Charts kosong | Belum ada transaksi di seed | Pastikan `seed_real.sql` sudah di-import |
