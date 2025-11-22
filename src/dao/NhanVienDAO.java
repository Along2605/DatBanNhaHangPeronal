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
//            if (con == null || con.isClosed()) {
//                ConnectDB.getInstance().connect();
//                con = ConnectDB.getConnection();
//            }
            String sql = "INSERT INTO NhanVien(maNV, hoTen, ngaySinh, email, soDienThoai, gioiTinh, chucVu, ngayVaoLam, trangThai, anhDaiDien) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                stmt.setString(10, nv.getAnhDaiDien());
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
            String sql = "UPDATE NhanVien SET hoTen=?, ngaySinh=?, email=?, soDienThoai=?, gioiTinh=?, chucVu=?, ngayVaoLam=?, trangThai=?, anhDaiDien= ? WHERE maNV=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, nv.getHoTen());
                stmt.setDate(2, Date.valueOf(nv.getNgaySinh()));
                stmt.setString(3, nv.getEmail());
                stmt.setString(4, nv.getSoDienThoai());
                stmt.setBoolean(5, nv.isGioiTinh());
                stmt.setString(6, nv.getChucVu());
                stmt.setDate(7, Date.valueOf(nv.getNgayVaoLam()));
                stmt.setBoolean(8, nv.isTrangThai());
                stmt.setString(9, nv.getAnhDaiDien());
                stmt.setString(10, nv.getMaNV());
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
           
            String sql = "SELECT * FROM NhanVien";
            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
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
                        rs.getBoolean("trangThai"),
                        rs.getString("anhDaiDien")
                       
                    );
                    ds.add(nv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public NhanVien getNhanVienTheoMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        Connection con = ConnectDB.getConnection();
        try {
            
        
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNV"),
                    rs.getString("hoTen"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getBoolean("gioiTinh"),
                    rs.getString("chucVu"),
                    rs.getDate("ngayVaoLam").toLocalDate(),
                    rs.getBoolean("trangThai"),
                    rs.getString("anhDaiDien")
                );              
                return nv;
            }  
          
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy nhân viên theo mã:");
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public boolean capNhatTrangThaiNhanVien(String maNV, boolean trangThaiMoi) {
        String sql = "UPDATE NhanVien SET trangThai = ? WHERE maNV = ?";
        Connection con = ConnectDB.getConnection();
        
        
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            
            stmt.setBoolean(1, trangThaiMoi);
            stmt.setString(2, maNV);
            
            int n = stmt.executeUpdate();
            return n > 0; // Trả về true nếu có > 0 dòng bị ảnh hưởng
            
        } catch (SQLException e) {
            e.printStackTrace();
       
        }
        return false; 
    }
    
    
//    public boolean xoaNhanVien(String maNV) {
//    	String sql= "delete from NhanVien where maNV=?";
//    	Connection con= ConnectDB.getConnection();
//    	try (
//    			PreparedStatement stmt= con.prepareStatement(sql)){
//    		stmt.setString(1, maNV);
//    		return stmt.executeUpdate() >0;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	return false;
//    }
    
    public String phatSinhMaNV() {
        String maMoi = "NV001"; // mặc định nếu chưa có dữ liệu
        String sql = "SELECT MAX(maNV) AS maCuoi FROM NhanVien";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maCuoi"); // ví dụ: NV012
                if (maCuoi != null) {
                    // Tách phần số: "012" → 12
                    int so = Integer.parseInt(maCuoi.substring(2));
                    so++;               
                    maMoi = String.format("NV%03d", so);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maMoi;
    }

	//Cap nhat thong tin ca nhan
    public boolean capNhatThongTinCaNhan(NhanVien nv) {
        Connection con = ConnectDB.getConnection();
        try {
            String sql = "UPDATE NhanVien SET hoTen=?, ngaySinh=?, email=?, soDienThoai=?, gioiTinh=?, anhDaiDien= ? WHERE maNV=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, nv.getHoTen());
                stmt.setDate(2, Date.valueOf(nv.getNgaySinh()));
                stmt.setString(3, nv.getEmail());
                stmt.setString(4, nv.getSoDienThoai());
                stmt.setBoolean(5, nv.isGioiTinh());
                stmt.setString(6, nv.getAnhDaiDien());
                stmt.setString(7, nv.getMaNV());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}