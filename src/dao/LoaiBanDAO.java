package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiBan;

public class LoaiBanDAO {
	public List<LoaiBan> getAllLoaiBan() {
		List<LoaiBan> dsLoaiBan = new ArrayList<LoaiBan>();
		Connection con= ConnectDB.getConnection();
		String sql = "Select * From LoaiBanAn";
		try (PreparedStatement stmt= con.prepareStatement(sql)){

			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				String maLoai = rs.getString("maLoaiBan");
				String tenLoai = rs.getString("tenLoaiBan");
				
				LoaiBan loaiban = new LoaiBan(maLoai, tenLoai);
				dsLoaiBan.add(loaiban);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return dsLoaiBan;
	}
	
	public String layMaTheoTen(String maLoaiBan) {
		String ma = null;
		Connection con = ConnectDB.getConnection();
		try {
			
			String sql = "SELECT tenLoaiBan FROM LoaiBanAn WHERE maLoaiBan = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, maLoaiBan);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				ma = rs.getString("tenLoaiBan");
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ma;
	}
	
	public LoaiBan layLoaiBanTheoTen(String tenLoaiBan) {
	    LoaiBan loaiBan = null;
	    String sql = "SELECT * FROM LoaiBanAn WHERE tenLoaiBan = ?";

	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, tenLoaiBan);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String maLoaiBan = rs.getString("maLoaiBan");
	                String ten = rs.getString("tenLoaiBan");
	                loaiBan = new LoaiBan(maLoaiBan, ten);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy loại bàn theo tên: " + tenLoaiBan);
	        e.printStackTrace();
	    }

	    return loaiBan;
	}

}
