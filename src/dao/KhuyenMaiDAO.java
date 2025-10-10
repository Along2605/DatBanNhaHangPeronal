package dao;

import connectDB.ConnectDB;
import entity.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {
    // Thêm khuyến mãi mới
    public boolean taoKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, phanTramGiam, ngayBatDau, ngayKetThuc, loaiKhuyenMai, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, km.getMaKhuyenMai());
            stmt.setString(2, km.getTenKhuyenMai());
            stmt.setDouble(3, km.getPhanTramGiam());
            stmt.setObject(4, km.getNgayBatDau());
            stmt.setObject(5, km.getNgayKetThuc());
            stmt.setString(6, km.getLoaiKhuyenMai());
            stmt.setBoolean(7, km.isTrangThai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật khuyến mãi
    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, phanTramGiam = ?, ngayBatDau = ?, ngayKetThuc = ?, loaiKhuyenMai = ?, trangThai = ? WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, km.getTenKhuyenMai());
            stmt.setDouble(2, km.getPhanTramGiam());
            stmt.setObject(3, km.getNgayBatDau());
            stmt.setObject(4, km.getNgayKetThuc());
            stmt.setString(5, km.getLoaiKhuyenMai());
            stmt.setBoolean(6, km.isTrangThai());
            stmt.setString(7, km.getMaKhuyenMai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa khuyến mãi
    public boolean xoaKhuyenMai(String maKhuyenMai) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKhuyenMai);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy khuyến mãi theo mã
    public KhuyenMai getKhuyenMaiTheoMa(String maKhuyenMai) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new KhuyenMai(
                    rs.getString("maKhuyenMai"),
                    rs.getString("tenKhuyenMai"),
                    rs.getDouble("phanTramGiam"),
                    rs.getObject("ngayBatDau", LocalDateTime.class),
                    rs.getObject("ngayKetThuc", LocalDateTime.class),
                    rs.getString("loaiKhuyenMai"),
                    rs.getBoolean("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả khuyến mãi
    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> dsKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dsKhuyenMai.add(new KhuyenMai(
                    rs.getString("maKhuyenMai"),
                    rs.getString("tenKhuyenMai"),
                    rs.getDouble("phanTramGiam"),
                    rs.getObject("ngayBatDau", LocalDateTime.class),
                    rs.getObject("ngayKetThuc", LocalDateTime.class),
                    rs.getString("loaiKhuyenMai"),
                    rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhuyenMai;
    }
}