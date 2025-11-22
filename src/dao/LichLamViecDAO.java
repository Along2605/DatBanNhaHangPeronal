package dao;

import connectDB.ConnectDB;
import entity.LichLamViec;
import entity.NhanVien;
import entity.CaLamViec;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LichLamViecDAO {
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private CaLamViecDAO caLamViecDAO = new CaLamViecDAO();

    // Thêm lịch làm việc mới
    public boolean themLichLamViec(LichLamViec lich) {
        String sql = "INSERT INTO LichLamViec (maLich, ngayLamViec, maNV, maCa, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, lich.getMaLich());
            stmt.setDate(2, Date.valueOf(lich.getNgayLamViec()));
            stmt.setString(3, lich.getNhanVien().getMaNV());
            stmt.setString(4, lich.getCaLamViec().getMaCa());
            stmt.setBoolean(5, lich.isTrangThai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật lịch làm việc
    public boolean capNhatLichLamViec(LichLamViec lich) {
        String sql = "UPDATE LichLamViec SET ngayLamViec = ?, maNV = ?, maCa = ?, trangThai = ? WHERE maLich = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(lich.getNgayLamViec()));
            stmt.setString(2, lich.getNhanVien().getMaNV());
            stmt.setString(3, lich.getCaLamViec().getMaCa());
            stmt.setBoolean(4, lich.isTrangThai());
            stmt.setString(5, lich.getMaLich());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa lịch làm việc
    public boolean xoaLichLamViec(String maLich) {
        String sql = "DELETE FROM LichLamViec WHERE maLich = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLich);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy lịch làm việc theo mã
    public LichLamViec timLichlamViec(String maLich) {
        String sql = "SELECT * FROM LichLamViec WHERE maLich = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLich);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NhanVien nv = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNV"));
                CaLamViec ca = caLamViecDAO.timCaLamViecTheoMa(rs.getString("maCa"));
                return new LichLamViec(
                    rs.getString("maLich"),
                    rs.getDate("ngayLamViec").toLocalDate(),
                    nv,
                    ca,
                    rs.getBoolean("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả lịch làm việc
    public List<LichLamViec> getAllLichLamViec() {
        List<LichLamViec> dsLich = new ArrayList<>();
        String sql = "SELECT * FROM LichLamViec";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                NhanVien nv = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNV"));
                CaLamViec ca = caLamViecDAO.timCaLamViecTheoMa(rs.getString("maCa"));
                dsLich.add(new LichLamViec(
                    rs.getString("maLich"),
                    rs.getDate("ngayLamViec").toLocalDate(),
                    nv,
                    ca,
                    rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLich;
    }
}