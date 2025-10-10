package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.BanAn;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;

public class HoaDonDAO {
	private BanAnDAO banAnDAO= new BanAnDAO();
	private KhachHangDAO khachHangDAO= new KhachHangDAO();
	private NhanVienDAO nhanVienDAO= new NhanVienDAO();
	private KhuyenMaiDAO khuyenMaiDAO= new KhuyenMaiDAO();
	
	public boolean themHoaDon(HoaDon hd) {
		String sql= "insert into HoaDon (maHoaDon, maBan, maKH, maNV, ngayLapHoaDon, thueVAT, maKhuyenMai) values (?,?,?,?,?,?,?)";
		try (Connection con= ConnectDB.getConnection();
				PreparedStatement stmt=con.prepareStatement(sql)){
			stmt.setString(1, hd.getMaHoaDon());
			stmt.setString(2, hd.getBanAn().getMaBan());
			stmt.setString(3, hd.getKhachHang().getMaKH());
			stmt.setString(4, hd.getNhanVien().getMaNV());
			stmt.setDate(5, Date.valueOf(hd.getNgayLapHoaDon()));
			stmt.setDouble(6, hd.getThueVAT());
			stmt.setString(7, hd.getKhuyenMai() !=null ? hd.getKhuyenMai().getMaKhuyenMai() : null);
			return stmt.executeUpdate() >0;
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	 public boolean capNhatHoaDon(HoaDon hd) {
	        String sql = "UPDATE HoaDon SET maBan = ?, maKH = ?, maNV = ?, ngayLapHoaDon = ?, thueVAT = ?, maKhuyenMai = ? WHERE maHoaDon = ?";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement stmt = con.prepareStatement(sql)) {
	            stmt.setString(1, hd.getBanAn().getMaBan());
	            stmt.setString(2, hd.getKhachHang().getMaKH());
	            stmt.setString(3, hd.getNhanVien().getMaNV());
	            stmt.setDate(4, java.sql.Date.valueOf(hd.getNgayLapHoaDon()));
	            stmt.setDouble(5, hd.getThueVAT());
	            stmt.setString(6, hd.getKhuyenMai() != null ? hd.getKhuyenMai().getMaKhuyenMai() : null);
	            stmt.setString(7, hd.getMaHoaDon());
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    // Xóa hóa đơn
	    public boolean xoaHoaDon(String maHoaDon) {
	        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement stmt = con.prepareStatement(sql)) {
	            stmt.setString(1, maHoaDon);
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    // Lấy hóa đơn theo mã
//	    public HoaDon timHoaDonTheoMa(String maHoaDon) {
//	        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
//	        try (Connection con = ConnectDB.getConnection();
//	             PreparedStatement stmt = con.prepareStatement(sql)) {
//	            stmt.setString(1, maHoaDon);
//	            ResultSet rs = stmt.executeQuery();
//	            if (rs.next()) {
//	                BanAn ban = banAnDAO.timBanTheoMa(rs.getString("maBan"));
//	                KhachHang kh = khachHangDAO.timKhachHangTheoMa(rs.getString("maKH"));
//	                NhanVien nv = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));
//	                KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
//	                return new HoaDon(
//	                    rs.getString("maHoaDon"),
//	                    ban,
//	                    kh,
//	                    nv,
//	                    rs.getDate("ngayLapHoaDon").toLocalDate(),
//	                    rs.getDouble("thueVAT"),
//	                    km
//	                );
//	            }
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	        return null;
//	    }

	    // Lấy tất cả hóa đơn
//	    public List<HoaDon> findAll() {
//	        List<HoaDon> dsHoaDon = new ArrayList<>();
//	        String sql = "SELECT * FROM HoaDon";
//	        try (Connection con = ConnectDB.getConnection();
//	             PreparedStatement stmt = con.prepareStatement(sql);
//	             ResultSet rs = stmt.executeQuery()) {
//	            while (rs.next()) {
//	                BanAn ban = banAnDAO.timBanTheoMa(rs.getString("maBan"));
//	                KhachHang kh = khachHangDAO.timKhachHangTheoMa(rs.getString("maKH"));
//	                NhanVien nv = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));
//	                KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
//	                dsHoaDon.add(new HoaDon(
//	                    rs.getString("maHoaDon"),
//	                    ban,
//	                    kh,
//	                    nv,
//	                    rs.getDate("ngayLapHoaDon").toLocalDate(),
//	                    rs.getDouble("thueVAT"),
//	                    km
//	                ));
//	            }
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	        return dsHoaDon;
//	    }
	
	
}
