package dao;

import entity.*;
import java.sql.*;
import java.util.*;
import connectDB.ConnectDB;

public class ChiTietHoaDonDAO {

    // Thêm chi tiết hóa đơn
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMon, soLuong, donGia, thanhTien, ghiChu) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());
            stmt.setString(2, cthd.getMonAn().getMaMon());
            stmt.setInt(3, cthd.getSoLuong());
            stmt.setDouble(4, cthd.getDonGia());
            stmt.setDouble(5, cthd.getThanhTien());
            stmt.setString(6, cthd.getGhiChu());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách chi tiết theo mã hóa đơn
//    public List<ChiTietHoaDon> getChiTietTheoMaHD(String maHoaDon) {
//        List<ChiTietHoaDon> ds = new ArrayList<>();
//        String sql = "SELECT maMon, soLuong, donGia, thanhTien, ghiChu "
//                   + "FROM ChiTietHoaDon WHERE maHoaDon = ?";
//        Connection con = ConnectDB.getConnection();
//        try (
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//            
//            stmt.setString(1, maHoaDon);
//            ResultSet rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                ChiTietHoaDon cthd = new ChiTietHoaDon();
//                cthd.setHoaDon(new HoaDon(maHoaDon));
//                cthd.setMonAn(new MonAn(rs.getString("maMon")));
//                cthd.setSoLuong(rs.getInt("soLuong"));
//                cthd.setDonGia(rs.getDouble("donGia"));
//                cthd.setThanhTien(rs.getDouble("thanhTien"));
//                cthd.setGhiChu(rs.getString("ghiChu"));
//                ds.add(cthd);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ds;
//    }

    // Xóa chi tiết theo mã hóa đơn (thường dùng khi xóa hóa đơn)
    public boolean xoaChiTietTheoMaHD(String maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ChiTietHoaDon timChiTietTheoHoaDonVaMon(String maHoaDon, String maMon) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMon = ?";
        Connection con= ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maMon);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                // Map data từ ResultSet
                cthd.setHoaDon(new HoaDon(rs.getString("maHoaDon")));
                cthd.setMonAn(new MonAn(rs.getString("maMon")));
                cthd.setSoLuong(rs.getInt("soLuong"));
                cthd.setDonGia(rs.getDouble("donGia"));
                cthd.setThanhTien(rs.getDouble("thanhTien"));
                cthd.setGhiChu(rs.getString("ghiChu"));
                return cthd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	public boolean capNhatChiTietHoaDon(ChiTietHoaDon cthd) {
	    String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, thanhTien = ? " +
	                 "WHERE maHoaDon = ? AND maMon = ?";
	    Connection con= ConnectDB.getConnection();
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setInt(1, cthd.getSoLuong());
	        stmt.setDouble(2, cthd.getThanhTien());
	        stmt.setString(3, cthd.getHoaDon().getMaHoaDon());
	        stmt.setString(4, cthd.getMonAn().getMaMon());
	        
	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

    public boolean xoaChiTietHoaDon(String maHoaDon, String maMon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMon = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maMon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	
	/**
	 * Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
	 */
	public List<ChiTietHoaDon> layDanhSachTheoHoaDon(String maHoaDon) {
	    List<ChiTietHoaDon> list = new ArrayList<>();
	    String sql = "SELECT cthd.*, ma.tenMon, ma.gia " +
	                 "FROM ChiTietHoaDon cthd " +
	                 "INNER JOIN MonAn ma ON cthd.maMon = ma.maMon " +
	                 "WHERE cthd.maHoaDon = ?";
	    Connection con= ConnectDB.getConnection();
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, maHoaDon);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            ChiTietHoaDon cthd = new ChiTietHoaDon();
	            
	            // Set HoaDon
	            HoaDon hoaDon = new HoaDon();
	            hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
	            cthd.setHoaDon(hoaDon);
	            
	            // Set MonAn
	            MonAn monAn = new MonAn();
	            monAn.setMaMon(rs.getString("maMon"));
	            monAn.setTenMon(rs.getString("tenMon"));
	            monAn.setGia(rs.getDouble("gia"));
	            cthd.setMonAn(monAn);
	            
	            // Set thông tin chi tiết
	            cthd.setSoLuong(rs.getInt("soLuong"));
	            cthd.setDonGia(rs.getDouble("donGia"));
	            cthd.setThanhTien(rs.getDouble("thanhTien"));
	            cthd.setGhiChu(rs.getString("ghiChu"));
	            
	            list.add(cthd);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return list;
	}
	

    public double tinhTongTienHoaDon(String maHD) {
        double tong = 0;
        String sql = "SELECT SUM(ThanhTien) FROM ChiTietHoaDon WHERE MaHoaDon = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tong = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }


}

