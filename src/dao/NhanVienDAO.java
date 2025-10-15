package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.CaLamViec;
import entity.NhanVien;

public class NhanVienDAO {
    public boolean themNhanVien(NhanVien nv) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "INSERT INTO NhanVien(maNV, hoTen, ngaySinh, email, soDienThoai, gioiTinh, chucVu, ngayVaoLam, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, nv.getMaNV());
                stmt.setString(2, nv.getHoTen());
                stmt.setDate(3, Date.valueOf(nv.getNgaySinh()));
                stmt.setString(4, nv.getEmail());
                stmt.setString(5, nv.getSoDienThoai());
                stmt.setBoolean(6, nv.isGioiTinh());
                stmt.setString(7, nv.getChucVu());
                stmt.setDate(8, Date.valueOf(nv.getNgayVaoLam()));
                stmt.setBoolean(9, nv.isTrangThai());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "UPDATE NhanVien SET hoTen=?, ngaySinh=?, email=?, soDienThoai=?, gioiTinh=?, chucVu=?, ngayVaoLam=?, trangThai=? WHERE maNV=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, nv.getHoTen());
                stmt.setDate(2, Date.valueOf(nv.getNgaySinh()));
                stmt.setString(3, nv.getEmail());
                stmt.setString(4, nv.getSoDienThoai());
                stmt.setBoolean(5, nv.isGioiTinh());
                stmt.setString(6, nv.getChucVu());
                stmt.setDate(7, Date.valueOf(nv.getNgayVaoLam()));
                stmt.setBoolean(8, nv.isTrangThai());
                stmt.setString(9, nv.getMaNV());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "SELECT * FROM NhanVien";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("email"),
                        rs.getString("soDienThoai"),
                        rs.getBoolean("gioiTinh"),
                        rs.getString("chucVu"),
                        rs.getDate("ngayVaoLam").toLocalDate(),
                        rs.getBoolean("trangThai")
                       
                    );
                    ds.add(nv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public NhanVien timNhanVienTheoMa(String maNV) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "SELECT * FROM NhanVien WHERE maNV=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maNV);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                    	
                        return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("hoTen"),
                            rs.getDate("ngaySinh").toLocalDate(),
                            rs.getString("email"),
                            rs.getString("soDienThoai"),
                            rs.getBoolean("gioiTinh"),
                            rs.getString("chucVu"),
                            rs.getDate("ngayVaoLam").toLocalDate(),
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
    public boolean xoaNhanVien(String maNV) {
    	String sql= "delete from NhanVien where maNV=?";
    	try (Connection con= ConnectDB.getConnection();
    			PreparedStatement stmt= con.prepareStatement(sql)){
    		stmt.setString(1, maNV);
    		return stmt.executeUpdate() >0;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
}