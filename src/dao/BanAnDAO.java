package dao;

import connectDB.ConnectDB;
import entity.BanAn;
import entity.KhuVuc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BanAnDAO {
	
	
    
	public List<BanAn> getAllBanAn() {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    
	    String sql = "select b.*, k.tenKhuVuc from BanAn b left join KhuVuc k on b.maKhuVuc= k.maKhuVuc";
	    Connection con = ConnectDB.getConnection();
	    try (
	         PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String loaiBan = rs.getString("loaiBan");
	            String trangThai = rs.getString("trangThai");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            
	          
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null); // viTri không được lấy trong truy vấn này
	            }
	            
	            String ghiChu = rs.getString("ghiChu");
	            
	            BanAn ban = new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	            dsBanAn.add(ban);
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy danh sách bàn ăn:");
	        e.printStackTrace();
	    }
	    
	    return dsBanAn;
	}
	
	public BanAn getBanTheoMa(String maBan) {
		String sql = """
			    SELECT b.*, k.tenKhuVuc
			    FROM BanAn b
			    LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
			    WHERE b.maBan = ?
			    """;
		
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement(sql);){
			stmt.setString(1, maBan);
			ResultSet rs= stmt.executeQuery();
			
			if (rs.next()) {				
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String loaiBan = rs.getString("loaiBan");
	            String trangThai = rs.getString("trangThai");
	           
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null); // viTri không được lấy trong truy vấn này
	            }
	            
	            String ghiChu = rs.getString("ghiChu");
	            
	            BanAn ban = new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	            return ban;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	public List<BanAn> getDSBanTrong() {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    String sql = """
	        SELECT b.*, k.tenKhuVuc
	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        WHERE b.trangThai = 'Trống'
	        """;
	    Connection con = ConnectDB.getConnection();
	    if (con == null) {
	        System.err.println("❌ Database connection is null");
	        return dsBanAn;
	    }
	    try (PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        int count = 0;
	        while (rs.next()) {
	            count++;
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String loaiBan = rs.getString("loaiBan");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null);
	            }
	            String ghiChu = rs.getString("ghiChu");
	            BanAn ban = new BanAn(maBan, tenBan, soLuongCho, loaiBan, "Trống", khuVuc, ghiChu);
	            dsBanAn.add(ban);
	        }
	        System.out.println("Found " + count + " available tables");
	    } catch (SQLException e) {
	        System.err.println("❌ Error fetching available tables: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return dsBanAn;
	}
	
	public boolean capNhatTrangThaiBan(String maBan, String trangThaiMoi) {
	    String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, trangThaiMoi);
	        stmt.setString(2, maBan);
	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

    
}