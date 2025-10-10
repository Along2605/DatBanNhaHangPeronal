package dao;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.MonAn;
import entity.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private MonAnDAO monAnDAO = new MonAnDAO();
    private KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    // Thêm chi tiết hóa đơn
    public boolean taoCTHoaDon(ChiTietHoaDon ct) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMon, soLuong, donGia, maKhuyenMai) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ct.getHoaDon().getMaHoaDon());
            stmt.setString(2, ct.getMonAn().getMaMon());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setDouble(4, ct.getDonGia());
            stmt.setString(5, ct.getKhuyenMai() != null ? ct.getKhuyenMai().getMaKhuyenMai() : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật chi tiết hóa đơn
    public boolean capNhatCTHoaDon(ChiTietHoaDon ct) {
        String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, donGia = ?, maKhuyenMai = ? WHERE maHoaDon = ? AND maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, ct.getSoLuong());
            stmt.setDouble(2, ct.getDonGia());
            stmt.setString(3, ct.getKhuyenMai() != null ? ct.getKhuyenMai().getMaKhuyenMai() : null);
            stmt.setString(4, ct.getHoaDon().getMaHoaDon());
            stmt.setString(5, ct.getMonAn().getMaMon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa chi tiết hóa đơn
    public boolean xoaCTHoaDon(String maHoaDon, String maMon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maMon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy chi tiết hóa đơn theo mã hóa đơn và mã món
//    public ChiTietHoaDon getCTHoaDonTheoMa(String maHoaDon, String maMon) {
//        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMon = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//            stmt.setString(1, maHoaDon);
//            stmt.setString(2, maMon);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                HoaDon hd = hoaDonDAO.timHoaDonTheoMa(rs.getString("maHoaDon"));
//                MonAn mon = monAnDAO.layMonAnTheoMa(rs.getString("maMon"));
//                KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
//                return new ChiTietHoaDon(
//                    hd,
//                    mon,
//                    rs.getInt("soLuong"),
//                    rs.getDouble("donGia"),
//                    km
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // Lấy tất cả chi tiết hóa đơn theo mã hóa đơn
//    public List<ChiTietHoaDon> layCTHoaDonTheoMaHD(String maHoaDon) {
//        List<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
//        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//            stmt.setString(1, maHoaDon);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                HoaDon hd = hoaDonDAO.timHoaDonTheoMa(rs.getString("maHoaDon"));
//                MonAn mon = monAnDAO.layMonAnTheoMa(rs.getString("maMon"));
//                KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
//                dsChiTiet.add(new ChiTietHoaDon(
//                    hd,
//                    mon,
//                    rs.getInt("soLuong"),
//                    rs.getDouble("donGia"),
//                    km
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsChiTiet;
//    }
}