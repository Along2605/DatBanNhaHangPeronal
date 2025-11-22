package dao;

import connectDB.ConnectDB;
import entity.BanAn;
import entity.KhuVuc;
import entity.LoaiBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BanAnDAO {
	
	
    
	public List<BanAn> getAllBanAn() {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    
	    String sql = """
	    	    SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	    	    FROM BanAn b
	    	    LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	    	    LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	    	    ORDER BY b.maBan
	    	""";
	    Connection con = ConnectDB.getConnection();
	    try (
	         PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);

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
			SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	        WHERE b.maBan = ?
        """;
		
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement(sql);){
			stmt.setString(1, maBan);
			ResultSet rs= stmt.executeQuery();
			
			if (rs.next()) {				
				
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            
	            String trangThai = rs.getString("trangThai");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            
	          
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null); // viTri không được lấy trong truy vấn này
	            }
	            
	            String ghiChu = rs.getString("ghiChu");
  
	            BanAn ban= new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	         	return ban;	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	
	public List<BanAn> getBanTheoTen(String tenBan) {
		List<BanAn> dsTimThay = new ArrayList<>();
		String sql = """
			SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	        WHERE b.tenBan = ?
        """;
		
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement(sql);){
			stmt.setString(1, tenBan);
			ResultSet rs= stmt.executeQuery();
			
			while (rs.next()) {				
				
	            String maBan = rs.getString("maBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            
	            String trangThai = rs.getString("trangThai");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            
	          
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null); // viTri không được lấy trong truy vấn này
	            }
	            
	            String ghiChu = rs.getString("ghiChu");
  
	            BanAn ban= new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	         	dsTimThay.add(ban);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return dsTimThay;
	}
	
	public List<BanAn> getDSBanTrong() {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    String sql = """
	         SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	    	 FROM BanAn b
	    	 LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	    	 LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	         WHERE b.trangThai = N'Trống'
	        """;
	    Connection con = ConnectDB.getConnection();
	   
	    try (PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        int count = 0;
	        while (rs.next()) {
	            count++;
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null);
	            }
	            String ghiChu = rs.getString("ghiChu");
	            BanAn ban= new BanAn(maBan, tenBan, soLuongCho, loaiBan, tenKhuVuc, khuVuc, ghiChu);
	            dsBanAn.add(ban);
	        }
	        System.out.println("Found " + count + " available tables");
	    } catch (SQLException e) {
	        System.err.println("❌ Error fetching available tables: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return dsBanAn;
	}

	public boolean capNhatTrangThaiBan(String maBan, String trangThai) {
		String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, trangThai);
			ps.setString(2, maBan);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean themBan(BanAn banAn) {
	    String sql = """
	        INSERT INTO BanAn (maBan, tenBan, soLuongCho, maLoaiBan, trangThai, maKhuVuc, ghiChu)
	        VALUES (?, ?, ?, ?, ?, ?, ?)
	        """;

	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, banAn.getMaBan());
	        ps.setString(2, banAn.getTenBan());
	        ps.setInt(3, banAn.getSoLuongCho());
	        ps.setString(4, banAn.getLoaiBan().getMaLoaiBan()); 
	        ps.setString(5, banAn.getTrangThai());
	        if (banAn.getKhuVuc() != null) {
	            ps.setString(6, banAn.getKhuVuc().getMaKhuVuc());
	        } else {
	            ps.setNull(6, java.sql.Types.VARCHAR);
	        }
	        ps.setString(7, banAn.getGhiChu());

	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi thêm bàn ăn mới:");
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean xoaBan(String maBan) {
	    String sql = "DELETE FROM BanAn WHERE maBan = ?";
	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maBan);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi xóa bàn ăn có mã: " + maBan);
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	public String generateMaBanMoi() {
		Connection con = ConnectDB.getConnection();
		String maMoi = "B001";
		PreparedStatement stmt = null;
		try{
			String sql = "SELECT MAX(CAST(SUBSTRING(maBan, 3, LEN(maBan)-2) AS INT)) AS maxSo FROM BanAn";
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int maxSo = rs.getInt("maxSo");
				int soMoi = maxSo + 1;
				maMoi = String.format("B%03d", soMoi);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maMoi;
	}
	
	
	public boolean suaThongTinBan(BanAn ban) {
	    String sql = """
	        UPDATE BanAn 
	        SET tenBan = ?, soLuongCho = ?, maLoaiBan = ?, trangThai = ?, maKhuVuc = ?, ghiChu = ?
	        WHERE maBan = ?
	        """;

	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, ban.getTenBan());
	        ps.setInt(2, ban.getSoLuongCho());

	        if (ban.getLoaiBan() != null)
	            ps.setString(3, ban.getLoaiBan().getMaLoaiBan());
	        else
	            ps.setNull(3, java.sql.Types.VARCHAR);

	        ps.setString(4, ban.getTrangThai());

	        if (ban.getKhuVuc() != null)
	            ps.setString(5, ban.getKhuVuc().getMaKhuVuc());
	        else
	            ps.setNull(5, java.sql.Types.VARCHAR);

	        ps.setString(6, ban.getGhiChu());
	        ps.setString(7, ban.getMaBan());

	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi cập nhật bàn ăn:");
	        e.printStackTrace();
	        return false;
	    }
	}	
	
}
    