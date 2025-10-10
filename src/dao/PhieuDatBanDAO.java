package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
//import java.sql.Timestamp;


import connectDB.ConnectDB;
import entity.BanAn;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatBan;

public class PhieuDatBanDAO {
	private KhachHangDAO khachHangDAO= new KhachHangDAO();
	private NhanVienDAO nhanVienDAO= new NhanVienDAO();
	private BanAnDAO banAnDAO= new BanAnDAO();
	
	public boolean taoPhieuDat(PhieuDatBan phieu) {
        String sql = "INSERT INTO PhieuDatBan (maPhieuDat, maKH, maNV, maBan, ngayDat, soNguoi, soTienCoc, ghiChu, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, phieu.getMaPhieuDat());
            stmt.setString(2, phieu.getKhachHang().getMaKH());
            stmt.setString(3, phieu.getNhanVien().getMaNV());
            stmt.setString(4, phieu.getBanAn().getMaBan());
            stmt.setObject(5, phieu.getNgayDat());// Dùng setObject() với LocalDateTime phụ thuộc vào driver JDBC
//            stmt.setTimestamp(5, Timestamp.valueOf(phieu.getNgayDat()));
            stmt.setInt(6, phieu.getSoNguoi());
            stmt.setDouble(7, phieu.getSoTienCoc());
            stmt.setString(8, phieu.getGhiChu());
            stmt.setString(9, phieu.getTrangThai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }       
    }
	
	// Cập nhật phiếu đặt bàn
    public boolean capNhatPhieuDat(PhieuDatBan phieu) {
        String sql = "UPDATE PhieuDatBan SET maKH = ?, maNV = ?, maBan = ?, ngayDat = ?, soNguoi = ?, soTienCoc = ?, ghiChu = ?, trangThai = ? WHERE maPhieuDat = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, phieu.getKhachHang().getMaKH());
            stmt.setString(2, phieu.getNhanVien().getMaNV());
            stmt.setString(3, phieu.getBanAn().getMaBan());
            stmt.setObject(4, phieu.getNgayDat());
            stmt.setInt(5, phieu.getSoNguoi());
            stmt.setDouble(6, phieu.getSoTienCoc());
            stmt.setString(7, phieu.getGhiChu());
            stmt.setString(8, phieu.getTrangThai());
            stmt.setString(9, phieu.getMaPhieuDat());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa phiếu đặt bàn
    public boolean xoaPhieuDat(String maPhieuDat) {
        String sql = "DELETE FROM PhieuDatBan WHERE maPhieuDat = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDat);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy phiếu đặt bàn theo mã
//    public PhieuDatBan getPhieuDatTheoMa(String maPhieuDat) {
//        String sql = "SELECT * FROM PhieuDatBan WHERE maPhieuDat = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//            stmt.setString(1, maPhieuDat);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                KhachHang kh = khachHangDAO.timKhachHangTheoMa(rs.getString("maKH"));
//                NhanVien nv = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));
//                BanAn ban = banAnDAO.timBanTheoMa(rs.getString("maBan"));
//                return new PhieuDatBan(
//                    rs.getString("maPhieuDat"),
//                    kh,
//                    nv,
//                    ban,
//                    rs.getObject("ngayDat", LocalDateTime.class),
////                    rs.getTimestamp("ngayDat").toLocalDateTime();
//                    rs.getInt("soNguoi"),
//                    rs.getDouble("soTienCoc"),
//                    rs.getString("ghiChu"),
//                    rs.getString("trangThai")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // Lấy tất cả phiếu đặt bàn
//    public List<PhieuDatBan> getAllPhieDat() {
//        List<PhieuDatBan> dsPhieu = new ArrayList<>();
//        String sql = "SELECT * FROM PhieuDatBan";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                KhachHang kh = khachHangDAO.timKhachHangTheoMa(rs.getString("maKH"));
//                NhanVien nv = nhanVienDAO.timNhanVienTheoMa(rs.getString("maNV"));
//                BanAn ban = banAnDAO.timBanTheoMa(rs.getString("maBan"));
//                dsPhieu.add(new PhieuDatBan(
//                    rs.getString("maPhieuDat"),
//                    kh,
//                    nv,
//                    ban,
//                    rs.getObject("ngayDat", LocalDateTime.class),
//                    rs.getInt("soNguoi"),
//                    rs.getDouble("soTienCoc"),
//                    rs.getString("ghiChu"),
//                    rs.getString("trangThai")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsPhieu;
//    }
//    
    
    
//    public List<PhieuDatBan> findAll() {
//        List<PhieuDatBan> dsPhieu = new ArrayList<>();
//        String sql = """
//            SELECT pdb.*, 
//                   kh.maKH, kh.tenKH, kh.sdt, 
//                   nv.maNV, nv.tenNV, 
//                   ba.maBan, ba.tenBan
//            FROM PhieuDatBan pdb
//            JOIN KhachHang kh ON pdb.maKH = kh.maKH
//            JOIN NhanVien nv ON pdb.maNV = nv.maNV
//            JOIN BanAn ba ON pdb.maBan = ba.maBan
//        """;
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                // Tạo KhachHang từ dữ liệu JOIN
//                KhachHang kh = new KhachHang();
//                kh.setMaKH(rs.getString("maKH"));
//                kh.setTenKH(rs.getString("tenKH"));
//                kh.setSdt(rs.getString("sdt"));
//
//                // Tạo NhanVien từ dữ liệu JOIN
//                NhanVien nv = new NhanVien();
//                nv.setMaNV(rs.getString("maNV"));
//                nv.setTenNV(rs.getString("tenNV"));
//
//                // Tạo BanAn từ dữ liệu JOIN
//                BanAn ban = new BanAn();
//                ban.setMaBan(rs.getString("maBan"));
//                ban.setTenBan(rs.getString("tenBan"));
//
//                // Tạo PhieuDatBan
//                PhieuDatBan phieu = new PhieuDatBan(
//                    rs.getString("maPhieuDat"),
//                    kh,
//                    nv,
//                    ban,
//                    rs.getTimestamp("ngayDat").toLocalDateTime(),
//                    rs.getInt("soNguoi"),
//                    rs.getDouble("soTienCoc"),
//                    rs.getString("ghiChu"),
//                    rs.getString("trangThai")
//                );
//                dsPhieu.add(phieu);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dsPhieu;
//    }


}
