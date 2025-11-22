package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.BanAn;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatBan;

public class PhieuDatBanDAO {
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private BanAnDAO banAnDAO = new BanAnDAO();
	private PhieuDatBan phieuDatBan;

    public boolean taoPhieuDat(PhieuDatBan phieu) {
        String sql = """
            INSERT INTO PhieuDatBan (maPhieuDat, maKH, maNV, maBan, ngayDat, soNguoi, soTienCoc, ghiChu, trangThai)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, phieu.getMaPhieuDat());
            stmt.setString(2, phieu.getKhachHang() != null ? phieu.getKhachHang().getMaKH() : null);
            stmt.setString(3, phieu.getNhanVien() != null ? phieu.getNhanVien().getMaNV() : null);
            stmt.setString(4, phieu.getBanAn() != null ? phieu.getBanAn().getMaBan() : null);
            stmt.setTimestamp(5, Timestamp.valueOf(phieu.getNgayDat())); // ✅ chuyển LocalDateTime sang SQL
            stmt.setInt(6, phieu.getSoNguoi());
            stmt.setDouble(7, phieu.getSoTienCoc());
            stmt.setString(8, phieu.getGhiChu());
            stmt.setString(9, phieu.getTrangThai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi thêm phiếu đặt bàn:");
            e.printStackTrace();
            return false;
        }
    }

   
    //Sinh mã phiếu đặt tự động dạng: PD + yyMMdd + số thứ tự (3 chữ số)
    public String taoMaPhieuDatTuDong() {
        String prefix = "PD";
        String dateStr = new java.text.SimpleDateFormat("MMdd").format(new java.util.Date()); // chỉ MMdd
        String sql = "SELECT TOP 1 maPhieuDat FROM PhieuDatBan WHERE maPhieuDat LIKE ? ORDER BY maPhieuDat DESC";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, prefix + dateStr + "%");
            ResultSet rs = stmt.executeQuery();

            int soThuTu = 1; // bắt đầu từ 001
            if (rs.next()) {
                String maCuoi = rs.getString("maPhieuDat");
                String sttStr = maCuoi.substring(maCuoi.length() - 3); // lấy phần cuối
                soThuTu = Integer.parseInt(sttStr) + 1;
            }

            String maMoi = prefix + dateStr + String.format("%03d", soThuTu);
            return maMoi;

        } catch (SQLException e) {
            System.err.println("⚠️ Lỗi khi tạo mã phiếu đặt tự động:");
            e.printStackTrace();
        }

        // fallback nếu lỗi
        String randomStr = String.format("%03d", (int)(Math.random() * 1000));
        return prefix + dateStr + randomStr;
    }


    
    public List<PhieuDatBan> getAllPhieuDat() {
        List<PhieuDatBan> ds = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = khachHangDAO.timKhachHangTheoMa(rs.getString("maKH"));
                NhanVien nv = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNV"));
                BanAn ban = banAnDAO.getBanTheoMa(rs.getString("maBan"));

                PhieuDatBan phieu = new PhieuDatBan(
                        rs.getString("maPhieuDat"),
                        kh,
                        nv,
                        ban,
                        rs.getTimestamp("ngayDat").toLocalDateTime(),
                        rs.getInt("soNguoi"),
                        rs.getDouble("soTienCoc"),
                        rs.getString("ghiChu"),
                        rs.getString("trangThai")
                );
                ds.add(phieu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
    
    
    public PhieuDatBan getPhieuDatTheoBan(String maBan) {
        PhieuDatBan phieuDat = null;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;

        // Câu lệnh SQL đã được sửa để JOIN với KhachHang và NhanVien
        String sql = "SELECT p.*, k.hoTen AS tenKH, k.sdt, nv.hoTen AS tenNV " +
                     "FROM PhieuDatBan p " +
                     "LEFT JOIN KhachHang k ON p.maKH = k.maKH " +
                     "LEFT JOIN NhanVien nv ON p.maNV = nv.maNV " +
                     "WHERE p.maBan = ? AND p.trangThai = N'Đã đặt'";

        try {
            stm = con.prepareStatement(sql);
            stm.setString(1, maBan);
            rs = stm.executeQuery();

            if (rs.next()) {
                phieuDat = new PhieuDatBan();
                phieuDat.setMaPhieuDat(rs.getString("maPhieuDat"));
                
                // === PHẦN SỬA LỖI QUAN TRỌNG NHẤT ===
                // Lấy mã khách hàng từ phiếu đặt
                String maKH = rs.getString("maKH");

                // Chỉ tạo đối tượng KhachHang nếu maKH không null
                if (maKH != null) {
                    KhachHang kh = new KhachHang(maKH);
                    kh.setHoTen(rs.getString("tenKH")); // Lấy tên từ cột đã JOIN
                    kh.setSdt(rs.getString("sdt"));     // Lấy sdt từ cột đã JOIN
                    
                    // Gán đối tượng KhachHang vào PhieuDatBan
                    phieuDat.setKhachHang(kh);
                }
                // Nếu maKH là null, phieuDat.getKhachHang() sẽ vẫn là null (đúng logic)

               
//                String maNV = rs.getString("maNV");
//                if(maNV != null) {
//                    NhanVien nv = new NhanVien(maNV);
//                    nv.setHoTen(rs.getString("tenNV"));
//                    phieuDat.setNhanVien(nv);
//                }
                
              
                phieuDat.setBanAn(new BanAn(rs.getString("maBan"))); // Cần có constructor này
                phieuDat.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                phieuDat.setSoNguoi(rs.getInt("soNguoi"));
                phieuDat.setSoTienCoc(rs.getDouble("soTienCoc"));
                phieuDat.setGhiChu(rs.getString("ghiChu"));
                phieuDat.setTrangThai(rs.getString("trangThai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return phieuDat;
    }
    
    
    public boolean capNhatTrangThaiPhieu(String maPhieuDat, String trangThaiMoi) {
        String sql = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPhieuDat = ?";
        Connection con= ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql);){
   
            stmt.setString(1, trangThaiMoi);
            stmt.setString(2, maPhieuDat);
            
            int result = stmt.executeUpdate();
           
            
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi cập nhật trạng thái phiếu:");
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public boolean huyPhieuDat(String maPhieuDat) {
        String sql = "UPDATE PhieuDatBan SET trangThai = N'Đã hủy' WHERE maPhieuDat = ?";
        Connection con= ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieuDat);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


	public PhieuDatBan timPhieuTheoMaPhieuDat(String maPhieuDat) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM PhieuDatBan WHERE maPhieuDat = ?";
		Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setString(1, maPhieuDat);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            KhachHang kh = rs.getString("maKH") != null 
	                ? new KhachHangDAO().timKhachHangTheoMa(rs.getString("maKH")) 
	                : null;

	            NhanVien nv = rs.getString("maNV") != null 
	                ? new NhanVienDAO().getNhanVienTheoMa(rs.getString("maNV")) 
	                : null;

	            BanAn ban = rs.getString("maBan") != null 
	                ? new BanAnDAO().getBanTheoMa(rs.getString("maBan")) 
	                : null;

	            PhieuDatBan phieu = new PhieuDatBan();
	            phieu.setMaPhieuDat(rs.getString("maPhieuDat"));
	            phieu.setKhachHang(kh);
	            phieu.setNhanVien(nv);
	            phieu.setBanAn(ban);
	            phieu.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
	            phieu.setSoNguoi(rs.getInt("soNguoi"));
	            phieu.setSoTienCoc(rs.getDouble("soTienCoc"));
	            phieu.setGhiChu(rs.getString("ghiChu"));
	            phieu.setTrangThai(rs.getString("trangThai"));

	            return phieu;
	        }
	    }catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}
	    
		return null;
	}

}
