package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
    public boolean themKhachHang(KhachHang kh) {
        Connection con = ConnectDB.getConnection();
        try {
            
            String sql = "INSERT INTO KhachHang(maKH, hoTen, gioiTinh, sdt, diemTichLuy, ngayDangKy, trangThai) VALUES (?, ?, ?, ?, ?, GETDATE(), 1)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, kh.getMaKH());
                stmt.setString(2, kh.getHoTen());
                stmt.setBoolean(3, kh.isGioiTinh());
                stmt.setString(4, kh.getSdt());
                
                stmt.setInt(5, kh.getDiemTichLuy());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
        	System.err.println("❌ Lỗi khi thêm khách hàng:");
            e.printStackTrace();
        }
        return false;
    }

	public boolean capNhatKhachHang(KhachHang kh) {
		Connection con = ConnectDB.getConnection();
		String sql = "UPDATE KhachHang SET hoTen=?, gioiTinh=?, sdt=?, diemTichLuy=? WHERE maKH=?";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, kh.getHoTen());
			stmt.setBoolean(2, kh.isGioiTinh());
			stmt.setString(3, kh.getSdt());
			stmt.setInt(4, kh.getDiemTichLuy());
			stmt.setString(5, kh.getMaKH());
			return stmt.executeUpdate() > 0;
		} catch (Exception e) {
			System.err.println("❌ Lỗi khi cập nhật khách hàng:");
			e.printStackTrace();
		}
		return false;
	}

	public List<KhachHang> layDanhSachKhachHang() {
		List<KhachHang> ds = new ArrayList<>();
		String sql = "SELECT * FROM KhachHang WHERE trangThai = 1 ORDER BY ngayDangKy DESC";
		Connection con = ConnectDB.getConnection();
		try {
			try (PreparedStatement stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					KhachHang kh = new KhachHang(rs.getString("maKH"), 
							rs.getString("hoTen"), 
							rs.getBoolean("gioiTinh"),
							rs.getString("sdt"), 
							rs.getInt("diemTichLuy"), 
							rs.getDate("ngayDangKy").toLocalDate(), 
																																																	// ký
							rs.getBoolean("trangThai") 
					);
					ds.add(kh);
				}
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi lấy ds khách hàng");
			e.printStackTrace();
		}
		return ds;
	}

    public KhachHang timKhachHangTheoMa(String maKH) {
        Connection con = ConnectDB.getConnection();
        try {
            String sql = "SELECT * FROM KhachHang WHERE maKH=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maKH);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("hoTen"),
                            rs.getBoolean("gioiTinh"),
                            rs.getString("sdt"),
                            
                            rs.getInt("diemTichLuy"),
                            rs.getDate("ngayDangKy").toLocalDate(),
                            rs.getBoolean("trangThai")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public KhachHang timKhachHangTheoSDT(String sdt) {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM KhachHang WHERE sdt = ?";
        try {
           
            
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, sdt);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("hoTen"),
                            rs.getBoolean("gioiTinh"),
                            rs.getString("sdt"),
                            rs.getInt("diemTichLuy"),
                            rs.getDate("ngayDangKy").toLocalDate(),
                            rs.getBoolean("trangThai")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }

    
    
    public String taoMaKhachHangTuDong() {
        String prefix = "KH";
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT TOP 1 maKH FROM KhachHang WHERE maKH LIKE ? ORDER BY maKH DESC";

        try {
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();

            int soThuTu = 1;
            if (rs.next()) {
                String maCuoi = rs.getString("maKH");
                String sttStr = maCuoi.substring(prefix.length());
                soThuTu = Integer.parseInt(sttStr) + 1;
            }

            // Không kiểm tra trùng lặp nữa, vì đã chọn mã lớn nhất
            return prefix + String.format("%03d", soThuTu);

        } catch (SQLException e) {
            System.err.println("⚠️ Lỗi khi tạo mã khách hàng tự động:");
            e.printStackTrace();
        }

        // Fallback nếu lỗi
        return prefix + String.format("%03d", (int)(Math.random() * 1000));
    }
    
    
    public boolean capNhatTrangThai(String maKH, boolean trangThai) {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE KhachHang SET trangThai = ? WHERE maKH = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBoolean(1, trangThai);
            stmt.setString(2, maKH);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi cập nhật trạng thái khách hàng:");
            e.printStackTrace();
        }
        return false;
    }



    
   

}