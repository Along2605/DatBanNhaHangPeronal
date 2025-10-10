package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection con = null;
    private static final ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }

    public void connect() {
        String url = "jdbc:sqlserver://localhost:1433;database=QuanLyNhaHang;encrypt=false";
        String user = "sa";
        String pass = "sql121204";

        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, user, pass);
                System.out.println("✅ Kết nối thành công!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Kết nối thất bại:");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                getInstance().connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void close() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("🔒 Kết nối đã được đóng.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
