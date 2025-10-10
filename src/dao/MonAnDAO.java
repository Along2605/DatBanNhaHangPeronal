package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.MonAn;

public class MonAnDAO {
	public MonAn layMonAnTheoMa(String maMonAn) {
		String sql= "seelct * from MonAn where maMon=?";
		try (Connection con= ConnectDB.getConnection();
				PreparedStatement stmt= con.prepareStatement(sql)){
			stmt.setString(1, maMonAn);
			ResultSet rs= stmt.executeQuery();
			if(rs.next()) {
				return new MonAn(rs.getString("maMon"),
						rs.getString("tenMon"),
						rs.getDouble("gia"),
						rs.getString("donViTinh"),
						rs.getBoolean("trangThai"),
						rs.getString("hinhAnh"),
						rs.getInt("soLuong"),
						rs.getString("moTa"),
						rs.getString("loaiMon")
						
				);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public List<MonAn> getAllMonAn() {
        List<MonAn> dsMonAn = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dsMonAn.add(new MonAn(
                    rs.getString("maMon"),
                    rs.getString("tenMon"),
                    rs.getDouble("gia"),
                    rs.getString("donViTinh"),
                    rs.getBoolean("trangThai"),
                    rs.getString("hinhAnh"),
                    rs.getInt("soLuong"),
                    rs.getString("moTa"),
                    rs.getString("loaiMon")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsMonAn;
    }
	
	public boolean themMonMoi(MonAn mon) {
        String sql = "INSERT INTO MonAn (maMon, tenMon, gia, donViTinh, trangThai, hinhAnh, soLuong, moTa, loaiMon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, mon.getMaMon());
            stmt.setString(2, mon.getTenMon());
            stmt.setDouble(3, mon.getGia());
            stmt.setString(4, mon.getDonViTinh());
            stmt.setBoolean(5, mon.isTrangThai());
            stmt.setString(6, mon.getHinhAnh());
            stmt.setInt(7, mon.getSoLuong());
            stmt.setString(8, mon.getMoTa());
            stmt.setString(9, mon.getLoaiMon());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean capNhatMon(MonAn mon) {
        String sql = "UPDATE MonAn SET tenMon = ?, gia = ?, donViTinh = ?, trangThai = ?, hinhAnh = ?, soLuong = ?, moTa = ?, loaiMon = ? WHERE maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, mon.getTenMon());
            stmt.setDouble(2, mon.getGia());
            stmt.setString(3, mon.getDonViTinh());
            stmt.setBoolean(4, mon.isTrangThai());
            stmt.setString(5, mon.getHinhAnh());
            stmt.setInt(6, mon.getSoLuong());
            stmt.setString(7, mon.getMoTa());
            stmt.setString(8, mon.getLoaiMon());
            stmt.setString(9, mon.getMaMon());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean xoaMonAn(String maMon) {
		String sql="delete from MonAn where maMon=?";
		try (Connection con= ConnectDB.getConnection();
				PreparedStatement stmt= con.prepareStatement(sql)){
			stmt.setString(1, maMon);
			return stmt.executeUpdate() >0;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
		
	}


}
