package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
    public boolean themKhachHang(KhachHang kh) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "INSERT INTO KhachHang(maKH, hoTen, gioiTinh, sdt, diemTichLuy, ngayDangKy, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, kh.getMaKH());
                stmt.setString(2, kh.getHoTen());
                stmt.setBoolean(3, kh.isGioiTinh());
                stmt.setString(4, kh.getSdt());
                
                stmt.setInt(5, kh.getDiemTichLuy());
                stmt.setDate(6, Date.valueOf(kh.getNgayDangKy()));
                stmt.setBoolean(7, kh.isTrangThai());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "UPDATE KhachHang SET hoTen=?, gioiTinh=?, sdt=? diemTichLuy=? WHERE maKH=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, kh.getHoTen());
                stmt.setBoolean(2, kh.isGioiTinh());
                stmt.setString(3, kh.getSdt());
                
                stmt.setInt(4, kh.getDiemTichLuy());
                stmt.setString(5, kh.getMaKH());
                stmt.setDate(6, Date.valueOf(kh.getNgayDangKy()));
                stmt.setBoolean(7, kh.isTrangThai());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<KhachHang> layDanhSachKhachHang() {
        List<KhachHang> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "SELECT * FROM KhachHang";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getBoolean("gioiTinh"),
                        rs.getString("sdt"),
                        
                        rs.getInt("diemTichLuy"),
                        rs.getDate("ngayDangKy").toLocalDate(),
                        rs.getBoolean("trangThai")
                    );
                    ds.add(kh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public KhachHang timKhachHangTheoMa(String maKH) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "SELECT * FROM KhachHang WHERE maKH=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maKH);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("hoTen"),
                            rs.getBoolean("gioiTinh"),
                            rs.getString("sdt"),
                            
                            rs.getInt("diemTichLuy"),
                            rs.getDate("ngayDangKy").toLocalDate(),
                            rs.getBoolean("trangThai")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}