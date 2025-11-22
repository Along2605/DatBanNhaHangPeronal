package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {

    public List<KhuyenMai> getKhuyenMaiHopLe() {
        List<KhuyenMai> dsKhuyenMai = new ArrayList<>();
        String sql = """
            SELECT * FROM KhuyenMai
            WHERE trangThai = 1 
              AND (ngayBatDau IS NULL OR ngayBatDau <= ?)
              AND (ngayKetThuc IS NULL OR ngayKetThuc >= ?)
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(1, now);
            stmt.setTimestamp(2, now);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsKhuyenMai.add(parseKhuyenMai(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi tải danh sách khuyến mãi hợp lệ: " + e.getMessage());
        }

        return dsKhuyenMai;
    }

    /**
     * Trả về khuyến mãi theo mã
     */
    public KhuyenMai getKhuyenMaiTheoMa(String maKhuyenMai) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhuyenMai);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return parseKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi tìm khuyến mãi theo mã: " + e.getMessage());
        }

        return null;
    }

    private KhuyenMai parseKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();

        km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
        km.setPhanTramGiam(rs.getDouble("phanTramGiam"));
        km.setSoTienGiam(rs.getDouble("soTienGiam"));
        
        Timestamp batDau = rs.getTimestamp("ngayBatDau");
        Timestamp ketThuc = rs.getTimestamp("ngayKetThuc");
        if (batDau != null) km.setNgayBatDau(batDau.toLocalDateTime());
        if (ketThuc != null) km.setNgayKetThuc(ketThuc.toLocalDateTime());
        
        km.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
        km.setTrangThai(rs.getBoolean("trangThai")); // BIT → boolean

        return km;
    }
}
