package dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.*;
import entity.NhatKyThaoTac.LoaiThaoTac;

public class HoaDonDAO {
    private BanAnDAO banAnDAO = new BanAnDAO();
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
    private NhatKyThaoTacDAO logDAO = NhatKyThaoTacDAO.getInstance();
    

    
    public boolean themHoaDon(HoaDon hd, String maNVThaoTac) {
        String sql = "INSERT INTO HoaDon (maHoaDon, maBan, maKH, maNV, maPhieuDat, " +
                     "ngayLapHoaDon, thueVAT, tongTien, maKhuyenMai, trangThai, tienCoc) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getBanAn() != null ? hd.getBanAn().getMaBan() : null);
            ps.setString(3, hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : null);
            ps.setString(4, hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : null);
            ps.setString(5, hd.getPhieuDat() != null ? hd.getPhieuDat().getMaPhieuDat() : null);
            ps.setTimestamp(6, hd.getNgayLapHoaDon() != null ? 
                Timestamp.valueOf(hd.getNgayLapHoaDon()) : null);
            ps.setDouble(7, hd.getThueVAT());
            ps.setDouble(8, hd.getTongTien());
            ps.setString(9, hd.getKhuyenMai() != null ? hd.getKhuyenMai().getMaKhuyenMai() : null);
            ps.setString(10, hd.getTrangThai());
            ps.setDouble(11, hd.getTienCoc());

            boolean result = ps.executeUpdate() > 0;
            
            // GHI LOG nếu thành công
            if (result && maNVThaoTac != null) {
                logDAO.logThem(maNVThaoTac, hd, "Thêm hóa đơn mới");
            }
            
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    
    public boolean capNhatHoaDon(HoaDon hdMoi, String maNVThaoTac) {
        // Lấy dữ liệu cũ trước khi cập nhật
        HoaDon hdCu = timHoaDonTheoMa(hdMoi.getMaHoaDon());
        
        String sql = "UPDATE HoaDon SET maBan = ?, maKH = ?, maNV = ?, " +
                     "ngayLapHoaDon = ?, thueVAT = ?, tongTien = ?, " +
                     "maKhuyenMai = ?, trangThai = ?, tienCoc = ? WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, hdMoi.getBanAn() != null ? hdMoi.getBanAn().getMaBan() : null);
            stmt.setString(2, hdMoi.getKhachHang() != null ? hdMoi.getKhachHang().getMaKH() : null);
            stmt.setString(3, hdMoi.getNhanVien() != null ? hdMoi.getNhanVien().getMaNV() : null);
            stmt.setTimestamp(4, hdMoi.getNgayLapHoaDon() != null ? 
                Timestamp.valueOf(hdMoi.getNgayLapHoaDon()) : null);
            stmt.setDouble(5, hdMoi.getThueVAT());
            stmt.setDouble(6, hdMoi.getTongTien());
            stmt.setString(7, hdMoi.getKhuyenMai() != null ? hdMoi.getKhuyenMai().getMaKhuyenMai() : null);
            stmt.setString(8, hdMoi.getTrangThai());
            stmt.setDouble(9, hdMoi.getTienCoc());
            stmt.setString(10, hdMoi.getMaHoaDon());

            boolean result = stmt.executeUpdate() > 0;
            
            // GHI LOG nếu thành công
            if (result && maNVThaoTac != null && hdCu != null) {
                logDAO.logSua(maNVThaoTac, hdCu, hdMoi, "Cập nhật hóa đơn");
            }
            
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHoaDon(String maHoaDon, String maNVThaoTac) {
        // Lấy dữ liệu trước khi xóa để ghi log
        HoaDon hd = timHoaDonTheoMa(maHoaDon);
        
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            boolean result = stmt.executeUpdate() > 0;
            
            // GHI LOG nếu thành công
            if (result && maNVThaoTac != null && hd != null) {
                logDAO.logXoa(maNVThaoTac, hd, "Xóa hóa đơn");
            }
            
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //TÌM HÓA ĐƠN THEO MÃ
    public HoaDon timHoaDonTheoMa(String maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToHoaDon(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //LẤY TẤT CẢ HÓA ĐƠN 
    public List<HoaDon> findAll() {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY ngayLapHoaDon DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
    
    
  

  
    public List<HoaDon> timHoaDonTheoTrangThai(String trangThai) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE trangThai = ? ORDER BY ngayLapHoaDon DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, trangThai);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    // ========== LẤY HÓA ĐƠN CHƯA THANH TOÁN THEO BÀN ==========
    public HoaDon getHoaDonChuaThanhToanTheoBan(String maBan) {
        String sql = "SELECT * FROM HoaDon WHERE maBan = ? AND trangThai = N'Chưa thanh toán'";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToHoaDon(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ========== TÌM HÓA ĐƠN THEO KHÁCH HÀNG ==========
    public List<HoaDon> timHoaDonTheoKhachHang(String maKH) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE maKH = ? ORDER BY ngayLapHoaDon DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    // ========== TÌM HÓA ĐƠN THEO NHÂN VIÊN ==========
    public List<HoaDon> timHoaDonTheoNhanVien(String maNV) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE maNV = ? ORDER BY ngayLapHoaDon DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maNV);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    // ========== TÌM HÓA ĐƠN THEO KHOẢNG THỜI GIAN ==========
    public List<HoaDon> timHoaDonTheoKhoangThoiGian(LocalDateTime tuNgay, LocalDateTime denNgay) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE ngayLapHoaDon BETWEEN ? AND ? ORDER BY ngayLapHoaDon DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(tuNgay));
            stmt.setTimestamp(2, Timestamp.valueOf(denNgay));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    // ========== TÌM HÓA ĐƠN THEO NGÀY ==========
    public List<HoaDon> timHoaDonTheoNgay(LocalDate ngay) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE CAST(ngayLapHoaDon AS DATE) = ? ORDER BY ngayLapHoaDon DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(ngay));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    public boolean capNhatTrangThai(String maHoaDon, String trangThaiMoi, String maNVThaoTac) {
        HoaDon hdCu = timHoaDonTheoMa(maHoaDon);
        String trangThaiCu = hdCu != null ? hdCu.getTrangThai() : "";
        
        String sql = "UPDATE HoaDon SET trangThai = ? WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, trangThaiMoi);
            stmt.setString(2, maHoaDon);
            boolean result = stmt.executeUpdate() > 0;
            
            // GHI LOG
            if (result && maNVThaoTac != null) {
                logDAO.logDonGian(
                    maNVThaoTac, 
                    LoaiThaoTac.SUA, 
                    "HoaDon", 
                    maHoaDon,
                    "Trạng thái: " + trangThaiCu + " -> " + trangThaiMoi,
                    "Cập nhật trạng thái hóa đơn"
                );
            }
            
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== CẬP NHẬT TỔNG TIỀN ==========
    public boolean capNhatTongTien(String maHoaDon, double tongTien) {
        String sql = "UPDATE HoaDon SET tongTien = ? WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDouble(1, tongTien);
            stmt.setString(2, maHoaDon);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== CẬP NHẬT KHUYẾN MÃI ==========
    public boolean capNhatKhuyenMai(String maHoaDon, String maKhuyenMai) {
        String sql = "UPDATE HoaDon SET maKhuyenMai = ? WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maKhuyenMai);
            stmt.setString(2, maHoaDon);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== ĐẾM SỐ HÓA ĐƠN THEO TRẠNG THÁI ==========
    public int demSoHoaDonTheoTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE trangThai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, trangThai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ========== TÍNH TỔNG DOANH THU THEO KHOẢNG THỜI GIAN ==========
    public double tinhTongDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay) {
        String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE ngayLapHoaDon BETWEEN ? AND ? AND trangThai = N'Đã thanh toán'";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(tuNgay));
            stmt.setTimestamp(2, Timestamp.valueOf(denNgay));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ========== TÍNH TỔNG DOANH THU THEO NGÀY ==========
    public double tinhDoanhThuTheoNgay(LocalDate ngay) {
        String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE CAST(ngayLapHoaDon AS DATE) = ? AND trangThai = N'Đã thanh toán'";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(ngay));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ========== KIỂM TRA TỒN TẠI HÓA ĐƠN ==========
    public boolean kiemTraTonTai(String maHoaDon) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE maHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ========== TẠO MÃ HÓA ĐƠN TỰ ĐỘNG ==========
    public String taoMaHoaDonTuDong() {
        String prefix = "HD";
        String dateStr = new SimpleDateFormat("MMdd").format(new java.util.Date());
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon WHERE maHoaDon LIKE ? ORDER BY maHoaDon DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, prefix + dateStr + "%");
            ResultSet rs = stmt.executeQuery();

            int soThuTu = 1;
            if (rs.next()) {
                String maCuoi = rs.getString("maHoaDon");
                String sttStr = maCuoi.substring(maCuoi.length() - 3);
                soThuTu = Integer.parseInt(sttStr) + 1;
            }

            return prefix + dateStr + String.format("%03d", soThuTu);

        } catch (SQLException e) {
            System.err.println("⚠️ Lỗi khi tạo mã hóa đơn tự động:");
            e.printStackTrace();
        }

        // fallback nếu lỗi
        String randomStr = String.format("%03d", (int)(Math.random() * 1000));
        return prefix + dateStr + randomStr;
    }

    // ========== MAP RESULTSET TO HOADON ==========
    /**
     * Chuyển ResultSet thành đối tượng HoaDon
     * Method dùng chung để tránh code trùng lặp
     */
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        // Load các đối tượng liên quan
        BanAn ban = rs.getString("maBan") != null ? 
            banAnDAO.getBanTheoMa(rs.getString("maBan")) : null;
        
        KhachHang kh = rs.getString("maKH") != null ? 
            khachHangDAO.timKhachHangTheoMa(rs.getString("maKH")) : null;
        
        NhanVien nv = rs.getString("maNV") != null ? 
            nhanVienDAO.getNhanVienTheoMa(rs.getString("maNV")) : null;
        
        KhuyenMai km = rs.getString("maKhuyenMai") != null ? 
            khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai")) : null;

        // Load PhieuDatBan nếu có
        PhieuDatBan phieuDat = null;
        String maPhieuDat = rs.getString("maPhieuDat");
        if (maPhieuDat != null) {
            phieuDat = phieuDatBanDAO.timPhieuTheoMaPhieuDat(maPhieuDat);
        }

        // Tạo và trả về HoaDon
        return new HoaDon(
            rs.getString("maHoaDon"),
            ban,
            kh,
            nv,
            rs.getTimestamp("ngayLapHoaDon") != null ? 
                rs.getTimestamp("ngayLapHoaDon").toLocalDateTime() : null,
            rs.getDouble("thueVAT"),
            rs.getDouble("tongTien"),
            km,
            rs.getString("trangThai"),
            phieuDat,
            rs.getDouble("tienCoc")
        );
    }
    
    public static List<HoaDon> layDanhSachHoaDonTheoThang(int thang, int nam) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MONTH(ngayLapHoaDon) = ? AND YEAR(ngayLapHoaDon) = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                String maBan = rs.getString("maBan");
                String maKH = rs.getString("maKH");
                String maNV = rs.getString("maNV");
                String maPhieuDat = rs.getString("maPhieuDat");
                LocalDateTime ngayLap = rs.getTimestamp("ngayLapHoaDon").toLocalDateTime();
                double thueVAT = rs.getDouble("thueVAT");
                double tongTien = rs.getDouble("tongTien");
                String maKhuyenMai = rs.getString("maKhuyenMai");
                String trangThai = rs.getString("trangThai");

                BanAnDAO banAnDAO = new BanAnDAO();
                KhachHangDAO khachHangDAO = new KhachHangDAO();
                NhanVienDAO nhanVienDAO = new NhanVienDAO();

                HoaDon hd = new HoaDon(
                    maHoaDon,
                    banAnDAO.getBanTheoMa(maBan),
                    khachHangDAO.timKhachHangTheoMa(maKH),
                    nhanVienDAO.getNhanVienTheoMa(maNV),
                    ngayLap,
                    thueVAT,
                    tongTien,
                    maKhuyenMai != null ? new KhuyenMai(maKhuyenMai) : null,
                    trangThai,
                    maPhieuDat != null ? new PhieuDatBan(maPhieuDat) : null,
                    0
                );
                dsHoaDon.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
    
    public static List<HoaDon> layDanhSachHoaDonTheoNgay(LocalDate ngay) {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE CAST(ngayLapHoaDon AS DATE) = ? ORDER BY ngayLapHoaDon";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ngay));
            ResultSet rs = ps.executeQuery();
            HoaDonDAO dao = new HoaDonDAO();
            while (rs.next()) {
                ds.add(dao.mapResultSetToHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
 // === LẤY HÓA ĐƠN THEO NĂM (MỚI) ===
    public static List<HoaDon> layDanhSachHoaDonTheoNam(int nam) {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE YEAR(NgayLapHoaDon) = ? ORDER BY NgayLapHoaDon";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            HoaDonDAO dao = new HoaDonDAO();

            while (rs.next()) {
                ds.add(dao.mapResultSetToHoaDon(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy hóa đơn theo năm " + nam + ": " + e.getMessage());
            e.printStackTrace();
        }
        return ds;
    }
    
    public static LocalDate layNgayCoNhieuHoaDonNhat() {
        String sql = 
            "SELECT top 1 CAST(ngayLapHoaDon AS DATE) AS ngay, COUNT(*) AS soLuong " +
            "FROM HoaDon " +
            "GROUP BY CAST(ngayLapHoaDon AS DATE) " +
            "ORDER BY soLuong DESC, ngay DESC";   // Ngày mới nhất nếu bằng nhau

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDate("ngay").toLocalDate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Không có dữ liệu
    }

    
 
    
    
}