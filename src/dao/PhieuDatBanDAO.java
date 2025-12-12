package dao;

import java.sql.*;
import java.time.LocalDate;
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
		        INSERT INTO PhieuDatBan (maPhieuDat, maKH, maNV, ngayDat, khungGio, soNguoi, soTienCoc, ghiChu, trangThai)
		        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
		    """;
	    Connection con = ConnectDB.getConnection();

	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, phieu.getMaPhieuDat());
	        stmt.setString(2, phieu.getKhachHang() != null ? phieu.getKhachHang().getMaKH() : null);
	        stmt.setString(3, phieu.getNhanVien() != null ? phieu.getNhanVien().getMaNV() : null);
	        stmt.setTimestamp(4, Timestamp.valueOf(phieu.getNgayDat()));
	        stmt.setInt(5, phieu.getKhungGio() > 0 ? phieu.getKhungGio() : 1);
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

                PhieuDatBan phieu = new PhieuDatBan(
                        rs.getString("maPhieuDat"),
                        kh,
                        nv,
                        null, // ✅ Bàn = null (load riêng nếu cần)
                        rs.getTimestamp("ngayDat").toLocalDateTime(),
                        rs.getInt("khungGio"),
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
    
    
    /**
     * Lấy phiếu đặt hiện tại của bàn.
     * Ưu tiên trạng thái 'Chờ xác nhận', sau đó 'Đã xác nhận' nếu không tìm thấy.
     * Sử dụng khi cần kiểm tra bàn có phiếu đặt hay không.
     */
    public PhieuDatBan getPhieuDatTheoBan(String maBan) {
        PhieuDatBan phieuDat = null;
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;

        String sql = "SELECT p.*, k.hoTen AS tenKH, k.sdt, nv.hoTen AS tenNV "
           + "FROM PhieuDatBan p "
           + "INNER JOIN ChiTietDatBan ctdb ON p.maPhieuDat = ctdb.maPhieuDat "
           + "LEFT JOIN KhachHang k ON p.maKH = k.maKH "
           + "LEFT JOIN NhanVien nv ON p.maNV = nv.maNV "
           + "WHERE ctdb.maBan = ? "
           + "AND p.trangThai IN (N'Chờ xác nhận', N'Đã xác nhận') "
           + "ORDER BY "
           + "  CASE p.trangThai "
           + "    WHEN N'Chờ xác nhận' THEN 1 "
           + "    WHEN N'Đã xác nhận' THEN 2 "
           + "  END, "
           + "  p.ngayDat DESC";


        try {
            stm = con.prepareStatement(sql);
            stm.setString(1, maBan);
            rs = stm.executeQuery();

            if (rs.next()) {
                phieuDat = new PhieuDatBan();
                phieuDat.setMaPhieuDat(rs.getString("maPhieuDat"));
                
                // Lấy thông tin khách hàng
                String maKH = rs.getString("maKH");
                if (maKH != null) {
                    KhachHang kh = new KhachHang(maKH);
                    kh.setHoTen(rs.getString("tenKH"));
                    kh.setSdt(rs.getString("sdt"));
                    phieuDat.setKhachHang(kh);
                }
                
                // Lấy thông tin nhân viên
                String maNV = rs.getString("maNV");
                if (maNV != null) {
                    NhanVien nv = new NhanVien(maNV);
                    nv.setHoTen(rs.getString("tenNV"));
                    phieuDat.setNhanVien(nv);
                }
                
                phieuDat.setBanAn(new BanAn(maBan));
                phieuDat.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                phieuDat.setKhungGio(rs.getInt("khungGio"));
                phieuDat.setSoNguoi(rs.getInt("soNguoi"));
                phieuDat.setSoTienCoc(rs.getDouble("soTienCoc"));
                phieuDat.setGhiChu(rs.getString("ghiChu"));
                phieuDat.setTrangThai(rs.getString("trangThai"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy phiếu đặt theo bàn:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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


	/**
	 * Tìm phiếu đặt theo mã phiếu.
	 * Sử dụng để lấy thông tin chi tiết phiếu đặt.
	 */
	public PhieuDatBan timPhieuTheoMaPhieuDat(String maPhieuDat) {
		String sql = "SELECT * FROM PhieuDatBan WHERE maPhieuDat = ?";
		Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maPhieuDat);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            PhieuDatBan phieu = new PhieuDatBan();
	            phieu.setMaPhieuDat(rs.getString("maPhieuDat"));
	            
	            // Load khách hàng
	            String maKH = rs.getString("maKH");
	            if (maKH != null) {
	                KhachHang kh = khachHangDAO.timKhachHangTheoMa(maKH);
	                phieu.setKhachHang(kh);
	            }

	            // Load nhân viên
	            String maNV = rs.getString("maNV");
	            if (maNV != null) {
	                NhanVien nv = nhanVienDAO.getNhanVienTheoMa(maNV);
	                phieu.setNhanVien(nv);
	            }

	            phieu.setBanAn(null); // Không set bàn ở đây (có thể có nhiều bàn)
	            phieu.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
	            phieu.setSoNguoi(rs.getInt("soNguoi"));
	            phieu.setSoTienCoc(rs.getDouble("soTienCoc"));
	            phieu.setGhiChu(rs.getString("ghiChu"));
	            phieu.setTrangThai(rs.getString("trangThai"));

	            return phieu;
	        }
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi tìm phiếu theo mã:");
	    	e.printStackTrace();
		}
	    
		return null;
	}
	
	/**
	 * Thêm bàn vào phiếu đặt (vào bảng ChiTietDatBan).
	 * Sử dụng khi lưu phiếu đặt mới hoặc thêm bàn vào phiếu đặt hiện có.
	 */
	public boolean themBanVaoPhieuDat(String maPhieuDat, String maBan, String ghiChu) {
	    String sql = "INSERT INTO ChiTietDatBan (maPhieuDat, maBan, ghiChu) VALUES (?, ?, ?)";
	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maPhieuDat);
	        ps.setString(2, maBan);
	        ps.setString(3, ghiChu != null ? ghiChu : "");
	        
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi thêm bàn vào phiếu đặt:");
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	/**
	 * Lấy phiếu đặt bàn theo mã bàn (dùng cho DialogChiTietBanAn)
	 * ⚠️ Chỉ lấy phiếu đang hoạt động (không bị hủy)
	 */
	public PhieuDatBan getPhieuDatTheoMaBan(String maBan) {
	    String sql = "SELECT DISTINCT p.* " +
	                 "FROM PhieuDatBan p " +
	                 "INNER JOIN ChiTietDatBan ctdb ON p.maPhieuDat = ctdb.maPhieuDat " +
	                 "WHERE ctdb.maBan = ? AND p.trangThai IN (N'Chờ xác nhận', N'Đã xác nhận')";
	    
	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maBan);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            PhieuDatBan phieu = new PhieuDatBan();
	            phieu.setMaPhieuDat(rs.getString("maPhieuDat"));
	            
	            // Load đầy đủ thông tin từ các bảng liên quan
	            String maKH = rs.getString("maKH");
	            if (maKH != null) {
	                phieu.setKhachHang(khachHangDAO.timKhachHangTheoMa(maKH));
	            }
	            
	            String maNV = rs.getString("maNV");
	            if (maNV != null) {
	                phieu.setNhanVien(nhanVienDAO.getNhanVienTheoMa(maNV));
	            }
	            
	            phieu.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
	            phieu.setSoNguoi(rs.getInt("soNguoi"));
	            phieu.setSoTienCoc(rs.getDouble("soTienCoc"));
	            phieu.setGhiChu(rs.getString("ghiChu"));
	            phieu.setTrangThai(rs.getString("trangThai"));
	            
	            return phieu;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	/**
	 * Lấy danh sách mã bàn trong một phiếu đặt.
	 * Sử dụng để hiển thị danh sách bàn trong chi tiết phiếu đặt.
	 */
	public List<String> getDanhSachBanTheoPhieuDat(String maPhieuDat) {
	    List<String> danhSachBan = new ArrayList<>();
	    String sql = "SELECT maBan FROM ChiTietDatBan WHERE maPhieuDat = ? ORDER BY maBan";
	    
	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maPhieuDat);
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            danhSachBan.add(rs.getString("maBan"));
	        }
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy danh sách bàn theo phiếu đặt:");
	        e.printStackTrace();
	    }
	    return danhSachBan;
	}
	
	// Ver 2	
	public List<BanAn> getDanhSachBanTheoPhieu(String maPhieuDat) {
	    List<BanAn> danhSachBan = new ArrayList<>();
	    
	    String sql = "SELECT b.* " +
	                 "FROM ChiTietDatBan ctdb " +
	                 "INNER JOIN BanAn b ON ctdb.maBan = b.maBan " +
	                 "WHERE ctdb.maPhieuDat = ?";
	    
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setString(1, maPhieuDat);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                // Sử dụng hàm map của BanAnDAO để tạo đối tượng BanAn
	                // (Giả sử BanAnDAO có hàm mapResultSetToBanAn như trong HoaDonDAO)
	                // Nếu BanAnDAO không có, bạn phải tự new BanAn() và set thủ công ở đây
	                
	                // Giả sử bạn có hàm helper trong BanAnDAO
	                BanAn ban = banAnDAO.mapResultSetToBanAn(rs); // Hoặc tên hàm tương tự
	                danhSachBan.add(ban);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return danhSachBan;
	}
	
	
	/**
	 * Lấy phiếu đặt bàn theo mã bàn, ngày và khung giờ cụ thể.
	 * Dùng để xem phiếu đặt khi tra cứu bàn theo ngày/giờ.
	 */
	public PhieuDatBan getPhieuDatTheoMaBan_NgayGio(
	        String maBan,
	        LocalDate ngayXem,
	        int khungGio
	) {
	    String sql = """
	        SELECT pdb.*
	        FROM PhieuDatBan pdb
	        INNER JOIN ChiTietDatBan ctdb ON pdb.maPhieuDat = ctdb.maPhieuDat
	        WHERE ctdb.maBan = ?
	          AND CAST(pdb.ngayDat AS DATE) = ?
	          AND pdb.khungGio = ?
	          AND pdb.trangThai IN (N'Chờ xác nhận', N'Đã xác nhận')
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maBan);
	        ps.setDate(2, java.sql.Date.valueOf(ngayXem));
	        ps.setInt(3, khungGio);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            PhieuDatBan pdb = new PhieuDatBan();
	            pdb.setMaPhieuDat(rs.getString("maPhieuDat"));
	            
	            // Load thông tin khách hàng
	            String maKH = rs.getString("maKH");
	            if (maKH != null) {
	                KhachHang kh = khachHangDAO.timKhachHangTheoMa(maKH);
	                pdb.setKhachHang(kh);
	            }

	            // Load thông tin nhân viên
	            String maNV = rs.getString("maNV");
	            if (maNV != null) {
	                NhanVien nv = nhanVienDAO.getNhanVienTheoMa(maNV);
	                pdb.setNhanVien(nv);
	            }
	            
	            pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
	            pdb.setKhungGio(rs.getInt("khungGio"));
	            pdb.setSoNguoi(rs.getInt("soNguoi"));
	            pdb.setSoTienCoc(rs.getDouble("soTienCoc"));
	            pdb.setGhiChu(rs.getString("ghiChu"));
	            pdb.setTrangThai(rs.getString("trangThai"));
	            return pdb;
	        }

	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy phiếu đặt theo bàn, ngày, giờ:");
	        e.printStackTrace();
	    }

	    return null;
	}

	/**
	 * Kiểm tra bàn có trống trong khung giờ không
	 */
	public boolean kiemTraBanTrongTrongKhungGio(String maBan, LocalDate ngayDat, int khungGio) {
	    String sql = """
	        SELECT COUNT(*) 
	        FROM ChiTietDatBan ctdb
	        JOIN PhieuDatBan pdb ON ctdb.maPhieuDat = pdb.maPhieuDat
	        WHERE ctdb.maBan = ?
	        AND CAST(pdb.ngayDat AS DATE) = ?
	        AND pdb.trangThai IN (N'Chờ xác nhận', N'Đã xác nhận')
	        AND pdb.khungGio = ?
	    """;
	    Connection con= ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maBan);
	        ps.setDate(2, java.sql.Date.valueOf(ngayDat));
	        ps.setInt(3, khungGio);
	        
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) == 0; // 0 = trống, >0 = đã đặt
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}
		
}
