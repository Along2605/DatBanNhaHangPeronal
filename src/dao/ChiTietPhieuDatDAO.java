package dao;

import connectDB.ConnectDB;
import entity.ChiTietPhieuDat;
import entity.PhieuDatBan;
import entity.MonAn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDatDAO {
    private PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
    private MonAnDAO monAnDAO = new MonAnDAO();

    
    public boolean themChiTietPhieudat(ChiTietPhieuDat ct) {
        String sql = "INSERT INTO ChiTietPhieuDat (maPhieuDat, maMon, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ct.getPhieuDatBan().getMaPhieuDat());
            stmt.setString(2, ct.getMonAn().getMaMon());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setDouble(4, ct.getDonGia());
            stmt.setString(5, ct.getGhiChu());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật chi tiết phiếu đặt
    public boolean capNhatCTPD(ChiTietPhieuDat ct) {
        String sql = "UPDATE ChiTietPhieuDat SET soLuong = ?, donGia = ?, ghiChu = ? WHERE maPhieuDat = ? AND maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, ct.getSoLuong());
            stmt.setDouble(2, ct.getDonGia());
            stmt.setString(3, ct.getGhiChu());
            stmt.setString(4, ct.getPhieuDatBan().getMaPhieuDat());
            stmt.setString(5, ct.getMonAn().getMaMon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa chi tiết phiếu đặt
    public boolean xoaCTPD(String maPhieuDat, String maMon) {
        String sql = "DELETE FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDat);
            stmt.setString(2, maMon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<ChiTietPhieuDat> getMonAnTheoPhieu(String maPhieuDat) {
        List<ChiTietPhieuDat> dsMonAn = new ArrayList<>();
        String sql = "SELECT ct.*, ma.tenMon, ma.donViTinh " +
                     "FROM ChiTietPhieuDat ct " +
                     "JOIN MonAn ma ON ct.maMon = ma.maMon " +
                     "WHERE ct.maPhieuDat = ?";
        
        Connection con= ConnectDB.getConnection();
        
        try ( PreparedStatement stmt = con.prepareStatement(sql)){
            
           
            stmt.setString(1, maPhieuDat);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Tạo món ăn
                MonAn monAn = new MonAn();
                monAn.setMaMon(rs.getString("maMon"));
                monAn.setTenMon(rs.getString("tenMon"));
                monAn.setDonViTinh(rs.getString("donViTinh"));
                
                // Tạo chi tiết phiếu đặt
                ChiTietPhieuDat chiTiet = new ChiTietPhieuDat();
                chiTiet.setMonAn(monAn);
                chiTiet.setSoLuong(rs.getInt("soLuong"));
                chiTiet.setDonGia(rs.getDouble("donGia"));
                chiTiet.setGhiChu(rs.getString("ghiChu"));
                
                dsMonAn.add(chiTiet);
            }
            
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy món ăn theo phiếu:");
            e.printStackTrace();
        }
        
        return dsMonAn;
    }

	
    
    
    public List<ChiTietPhieuDat> getChiTietTheoMaPhieu(String maPhieuDat) {
        List<ChiTietPhieuDat> ds = new ArrayList<>();
        String sql = "SELECT maMon, soLuong, donGia, ghiChu FROM ChiTietPhieuDat WHERE maPhieuDat = ?";
        Connection con= ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maPhieuDat);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietPhieuDat ctpd = new ChiTietPhieuDat();
                ctpd.setPhieuDatBan(new PhieuDatBan(maPhieuDat));
                ctpd.setMonAn(new MonAn(rs.getString("maMon")));
                ctpd.setSoLuong(rs.getInt("soLuong"));
                ctpd.setDonGia(rs.getDouble("donGia"));
                ctpd.setGhiChu(rs.getString("ghiChu"));
                ds.add(ctpd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
    
    

   
}