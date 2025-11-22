package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhuVuc;

public class KhuVucDAO {
	public KhuVuc findById(String maKhuVuc) {
		String sql= "select * from KhuVuc where maKhuVuc=?";
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement(sql)){
			stmt.setString(1, maKhuVuc);
			ResultSet rs= stmt.executeQuery();
			if(rs.next()) {
				return new KhuVuc(
						rs.getString("maKhuVuc"),
						rs.getString("tenKhuVuc"),
						rs.getString("viTri")
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<KhuVuc> getAll(){
		List<KhuVuc> dsKhuVuc= new ArrayList<KhuVuc>();
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement("select * from KhuVuc");
				ResultSet rs= stmt.executeQuery()){
			while(rs.next()) {
				dsKhuVuc.add(new KhuVuc(
					rs.getString("maKhuVuc"),
					rs.getString("tenKhuVuc"),
					rs.getString("viTri")		
				));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsKhuVuc;
	}
	
	public String layMaTheoTen(String tenKhuVuc) {
	    String ma = null;
	    Connection con = ConnectDB.getConnection();
	    try {
	        
	        String sql = "SELECT maKhuVuc FROM KhuVuc WHERE tenKhuVuc = ?";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, tenKhuVuc);
	        ResultSet rs = stmt.executeQuery();
	        if(rs.next()) {
	            ma = rs.getString("maKhuVuc");
	            
	        }
	        stmt.close();
	  } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ma;
	}
	
}
