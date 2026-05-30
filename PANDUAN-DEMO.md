# Panduan Demo Aplikasi — Stock Audit System

Panduan presentasi dari **login sampai selesai**. Anggap database & data sudah siap.
Format tiap langkah: **Klik** (yang dilakukan) + **Script** (yang diucapkan ke penguji).

> Persiapan sebelum maju (lakukan diam-diam):
> - XAMPP/MySQL **menyala**, database `stock_audit_db` sudah di-import (schema + seed).
> - Aplikasi sudah di-**Clean and Build** dan bisa Run tanpa error.
> - Tutup jendela lain, perbesar aplikasi (sudah otomatis 1280×800).
> - Hafalkan login: **admin / admin123** (dan **staff / admin123** untuk demo hak akses).
> - Sudah **Run** aplikasi sekali sebelum mulai — jangan menunggu startup di depan penguji.
> - **Logout lalu login ulang sebagai admin** supaya **Dashboard** memuat data terbaru.
> - Tutup semua jendela **JasperViewer** sebelumnya — popup baru bisa muncul di belakang jendela utama.

---

## Alur cerita demo (ikuti urutan ini)

Login → Dashboard → Master Data → Receiving (barang masuk) → Sales (penjualan) →
Movement (waste) → Recipes → Stock on Hand → Audit → End Balance → Reports → Charts →
Hak Akses (login staff) → Logout.

Inti cerita: **"Aplikasi mengikuti siklus inventory F&B: barang masuk, terjual/terpakai,
lalu dihitung ulang, dan semua angka stok otomatis dihitung sistem."**

---

## 1. Login

- **Klik:** ketik `admin` / `admin123` → tombol **Login**.
- **Script:** "Aplikasi punya 3 peran: Administrator, Manager, Staff. Saya login sebagai
  Administrator yang punya akses penuh. Password disimpan ter-enkripsi (BCrypt), bukan teks biasa."
- Tampil jendela utama. Tunjuk **pojok kanan atas** ("User : admin (Administrator)") dan
  **sidebar kiri** yang dikelompokkan: Master Data, Transactions, Stock Operations, Reports & Charts.

---

## 2. Dashboard (halaman pertama)

- **Script:** "Begitu login langsung muncul Dashboard sebagai ringkasan operasional."
- Tunjuk 4 **kartu KPI**: Penjualan hari ini, Stok menipis (low stock), Audit tertunda, Total item.
- Tunjuk tabel **Recent Transactions** dan **Low Stock Alerts**.
- **Script:** "Manager bisa lihat kondisi hari ini sekali pandang tanpa buka menu lain."

---

## 3. Master Data (data dasar)

Cukup tunjukkan sekilas, jangan lama-lama. Klik berurutan di grup **Master Data**:

- **Items** — **Script:** "Daftar semua barang: kode, nama, kategori, satuan, harga.
  Bisa tambah/ubah/hapus dan cari." (Boleh ketik di kolom Search untuk memperlihatkan filter.)
- **Categories** — kelompok barang (mis. Beverage, Dry Goods, Fish).
- **Outlets** — cabang/lokasi.
- **Vendors** — pemasok.
- **Users** — **Script:** "Pengelolaan akun & peran. Menu ini hanya muncul untuk Administrator."

> Tips: jangan demo hapus data master di sini supaya seed tidak rusak. Cukup tunjuk + cari.

---

## 4. Receiving — Barang Masuk

- **Klik:** menu **Receiving**.
- **Script:** "Mencatat barang yang datang dari vendor."
- **Klik:** pilih **Vendor**, pilih **Item** dari dropdown → harga beli terisi otomatis →
  isi qty → **Add** (baris masuk ke tabel) → ulangi 1 baris lagi → tunjuk **Total** terhitung.
- **Klik:** **Save**. Muncul notif sukses + **nomor otomatis** (mis. `RCV-2026-00002`).
- **Script:** "Nomor dokumen dibuat otomatis. Penyimpanan bersifat transaksi: header dan
  semua detailnya tersimpan sekaligus, atau dibatalkan semua kalau gagal."

---

## 5. Sales — Penjualan

- **Klik:** menu **Sales**.
- **Script:** "Mencatat penjualan/pemakaian menu."
- **Klik:** tunjuk **tanggal** (date picker, default hari ini) → pilih **Item** → harga jual
  terisi otomatis → isi qty → **Add** → tambah 1 baris lagi → tunjuk **Total**.
- **Klik:** **Save** → notif sukses + nomor `INV-2026-xxxxx`.
- **Script:** "Penjualan ini akan otomatis mengurangi stok. Saya buktikan di menu Stock on Hand nanti."

> Ingat barang yang Anda jual di sini — nanti tunjukkan stoknya berkurang di langkah 8.

---

## 6. Movement — Pergerakan Stok (Waste/Transfer)

- **Klik:** menu **Movement**.
- **Script:** "Mencatat pergerakan stok selain jual-beli: WASTE (rusak/buang),
  CONSUMPTION (pemakaian internal), serta transfer IN/OUT antar outlet."
- **Klik:** pilih tipe **WASTE** → pilih item → qty → **Save**.
- **Script:** "Untuk transfer OUT wajib isi outlet tujuan. WASTE juga otomatis mengurangi stok."

---

## 7. Recipes — Resep / BOM

- **Klik:** menu **Recipes**.
- **Script:** "Resep menjelaskan satu menu terdiri dari bahan apa saja, lengkap dengan
  persentase waste tiap bahan. Ini dasar perhitungan kebutuhan bahan baku."
- Tunjuk satu resep beserta daftar bahannya.

---

## 8. Stock on Hand — Stok Real-time (BAGIAN PENTING)

- **Klik:** menu **Stock on Hand** → **Refresh**.
- **Script:** "Ini inti sistem. Stok TIDAK disimpan manual — dihitung otomatis oleh database:
  **Saldo Awal + Penerimaan + Transfer Masuk − Penjualan − Waste − Transfer Keluar − Pemakaian.**"
- Tunjuk **item yang tadi Anda jual** di langkah 5 → **Script:** "Lihat, stoknya sudah berkurang
  sesuai penjualan tadi. Semua transaksi tadi langsung tercermin di sini."
- Tunjuk **warna status** stok (mis. Critical/Low) dan filter Kategori/Outlet/Search.

---

## 9. Audit — Stock Opname

- **Klik:** menu **Audit** → **Load Items**.
- **Script:** "Saat stock opname, sistem menampilkan stok versi sistem, lalu petugas mengisi
  **hitungan fisik** di lapangan."
- **Klik:** isi kolom **Physical Count** salah satu item beda dari sistem → tunjuk kolom
  **Variance** berubah warna (selisih).
- **Klik:** **Save**. **Script:** "Selisih terdata sebagai temuan audit untuk ditindaklanjuti."

---

## 10. End Balance — Saldo Akhir Periode

- **Klik:** menu **End Balance** → **Generate/Snapshot**.
- **Script:** "Di akhir bulan, sistem mengunci saldo akhir tiap item beserta nilai rupiahnya.
  Saldo ini jadi saldo awal periode berikutnya." (Menu ini hanya untuk Manager/Admin.)

---

## 11. Reports — Laporan

- **Klik:** menu **Reports** → pilih jenis laporan (Stock / Sales / Receiving / Audit variance) → **Generate**.
- **Script:** "Setiap laporan terbuka di **JasperViewer** — penampil laporan profesional dengan tombol **Print**, **Save As PDF/Excel** langsung di toolbar atas."
- **Klik:** ikon **disket** di toolbar JasperViewer → pilih **PDF** → simpan → buka PDF-nya untuk membuktikan.
- **Script:** "PDF siap dibagikan ke manajemen — formatnya rapi, paginasi otomatis, nomor pakai pemisah ribuan."

---

## 12. Charts — Grafik

- **Klik:** menu **Charts**.
- **Script:** "Visualisasi untuk manajemen: menu terlaris, tren penjualan harian,
  penjualan per kategori, dan waste tertinggi."
- Tunjuk keempat grafik.

---

## 13. Hak Akses Berbasis Peran (nilai plus)

- **Klik:** **Logout** (pojok bawah sidebar).
- **Klik:** login ulang sebagai **staff / admin123**.
- **Script:** "Sebagai Staff, menu **Users** dan **End Balance** otomatis hilang. Hak akses
  dibedakan per peran demi keamanan data."
- Tunjuk sidebar yang lebih sedikit.
- **Script:** "Perhatikan sidebar — menu yang sedang dibuka **disorot biru muda**, sehingga jelas posisi pengguna saat ini."

---

## 14. Penutup

- **Klik:** **Logout** → kembali ke layar login.
- **Script:** "Demikian siklus lengkap Stock Audit System: dari barang masuk, penjualan,
  pergerakan stok, hingga audit, laporan, dan grafik — dengan perhitungan stok otomatis
  dan hak akses bertingkat. Terima kasih."

---

## Catatan teknis (untuk Anda, bukan diucapkan)

- **Dashboard & Charts memuat data saat dibuka pertama.** Jika ingin angka terbaru tampil
  setelah transaksi, **logout lalu login lagi** sebelum membuka Dashboard/Charts.
- **Stock on Hand / Audit / End Balance** punya tombol **Refresh/Load** → selalu data terbaru.
- Kalau transaksi gagal save, cek MySQL menyala & database ter-import.
- **JANGAN** buka panel-panel ini di tab **Design** NetBeans: **SalesPanel** (date picker), **MovementPanel** (type dropdown), **StockOnHandPanel**, **OutletsPanel**, **UsersPanel**, **VendorsPanel**, **FormLoginFrame**. Layout/komponen yang dirapikan ada di blok generated — Design tab akan menimpa kembali ke versi lama.
- Urutan aman kalau perlu re-seed: import `db/schema.sql` dulu, baru `db/seed_real.sql`.

## Catatan data demo

Beberapa item menunjukkan **On-Hand negatif** (mis. HOTATE -1.617, KIBUN -36.582).
Itu **bukan bug** — seed memang menyertakan kasus penjualan/waste yang melampaui
penerimaan untuk menunjukkan bahwa sistem **mendeteksi inkonsistensi data**, bukan
menyembunyikannya. Jika ditanya: "Berarti perlu Receiving atau Audit untuk koreksi" —
tunjukkan caranya.

## Jawaban cepat untuk pertanyaan penguji

- **"Stok dihitung di mana?"** → Di database lewat *view* `v_stock_on_hand`, bukan disimpan manual.
- **"Kalau simpan gagal di tengah?"** → Transaksi (commit/rollback): header+detail tersimpan
  semua atau tidak sama sekali.
- **"Password aman?"** → Di-hash dengan BCrypt, tidak disimpan sebagai teks biasa.
- **"Bedanya peran?"** → Staff: tanpa Users & End Balance. Manager: tanpa Users. Admin: semua.
- **"Nomor dokumen?"** → Dibuat otomatis berformat `INV-/RCV-/RCP- + tahun + urutan`.
