package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static final String URL  = "jdbc:mysql://localhost:3306/stock_audit_db?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi DB sukses: stock_audit_db");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Koneksi DB gagal: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection c = connect();
        if (c != null) {
            try { c.close(); } catch (SQLException ignored) {}
        }
    }
}
