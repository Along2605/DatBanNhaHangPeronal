package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoanDAO {
	private NhanVienDAO nhanVienDAO= new NhanVienDAO();
	public boolean themTaiKhoan(TaiKhoan tk) throws SQLException {
		Connection con= ConnectDB.getConnection();
//		if (con == null || con.isClosed()) {
//            ConnectDB.getInstance().connect();
//            con = ConnectDB.getConnection();
//        }
		String sql= "insert into TaiKhoan (userName, passWord, maNV, quyenTruyCap) values (?,?,?,?)";
		try (PreparedStatement stmt= con.prepareStatement(sql)){
			stmt.setString(1, tk.getUserName());
			stmt.setString(2, tk.getPassWord());
			stmt.setString(3, tk.getNhanVien().getMaNV());
			stmt.setString(4, tk.getQuyenTruyCap());
			return stmt.executeUpdate() >0;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;	
	}
	
	public boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET passWord = ?, maNV = ?, quyenTruyCap = ? WHERE userName = ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tk.getPassWord());
            stmt.setString(2, tk.getNhanVien().getMaNV());
            stmt.setString(3, tk.getQuyenTruyCap());
            stmt.setString(4, tk.getUserName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean xoaTaiKhoan(String userName) {
        String sql = "DELETE FROM TaiKhoan WHERE userName = ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public TaiKhoan timTaiKhoan(String userName) {
		Connection con= ConnectDB.getConnection();
		String sql= "select * from TaiKhoan where userName= ?";
		try (
				PreparedStatement stmt= con.prepareStatement(sql)){
			stmt.setString(1, userName);
			ResultSet rs= stmt.executeQuery();
			if(rs.next()) {
				NhanVien nv= nhanVienDAO.getNhanVienTheoMa(rs.getString("maNV"));
				return new TaiKhoan(rs.getString("userName"), rs.getString("passWord"), nv, rs.getString("quyenTruyCap"));		
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();	
		}
		return null;
		
	}
	
	public List<TaiKhoan> getAllTaiKhoan(){
		List<TaiKhoan> dsTK= new ArrayList<TaiKhoan>();
		String sql= "select * from TaiKhoan";
		Connection con= ConnectDB.getConnection();
		try (
				PreparedStatement stmt= con.prepareStatement(sql)){
			ResultSet rs= stmt.executeQuery();
			while(rs.next()) {
				NhanVien nv= nhanVienDAO.getNhanVienTheoMa(rs.getString("maNV"));
				dsTK.add(new TaiKhoan(rs.getString("userName"),
						rs.getString("passWord"),
						nv,
						rs.getString("quyenTruyCap")				
				));			
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsTK;
	}
	
	
	
	
	
}
