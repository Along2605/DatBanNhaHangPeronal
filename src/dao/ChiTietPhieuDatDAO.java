package dao;

import connectDB.ConnectDB;
import entity.ChiTietPhieuDat;
import entity.PhieuDatBan;
import entity.MonAn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDatDAO {
    private PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
    private MonAnDAO monAnDAO = new MonAnDAO();

    
    public boolean themChiTietPhieudat(ChiTietPhieuDat ct) {
        String sql = "INSERT INTO ChiTietPhieuDat (maPhieuDat, maMon, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ct.getPhieuDatBan().getMaPhieuDat());
            stmt.setString(2, ct.getMonAn().getMaMon());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setDouble(4, ct.getDonGia());
            stmt.setString(5, ct.getGhiChu());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật chi tiết phiếu đặt
    public boolean capNhatCTPD(ChiTietPhieuDat ct) {
        String sql = "UPDATE ChiTietPhieuDat SET soLuong = ?, donGia = ?, ghiChu = ? WHERE maPhieuDat = ? AND maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, ct.getSoLuong());
            stmt.setDouble(2, ct.getDonGia());
            stmt.setString(3, ct.getGhiChu());
            stmt.setString(4, ct.getPhieuDatBan().getMaPhieuDat());
            stmt.setString(5, ct.getMonAn().getMaMon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa chi tiết phiếu đặt
    public boolean xoaCTPD(String maPhieuDat, String maMon) {
        String sql = "DELETE FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND maMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDat);
            stmt.setString(2, maMon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy chi tiết phiếu đặt theo mã phiếu và mã món
//    public ChiTietPhieuDat timCTPDTheoMaPhieuVaMaMon(String maPhieuDat, String maMon) {
//        String sql = "SELECT * FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND maMon = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//            stmt.setString(1, maPhieuDat);
//            stmt.setString(2, maMon);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                PhieuDatBan phieu = phieuDatBanDAO.getPhieuDatTheoMa(rs.getString("maPhieuDat"));
//                MonAn mon = monAnDAO.layMonAnTheoMa(rs.getString("maMon"));
//                return new ChiTietPhieuDat(
//                    phieu,
//                    mon,
//                    rs.getInt("soLuong"),
//                    rs.getDouble("donGia"),
//                    rs.getString("ghiChu")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // Lấy tất cả chi tiết phiếu đặt theo mã phiếu
//    public List<ChiTietPhieuDat> getCTPDByMaPhieu(String maPhieuDat) {
//        List<ChiTietPhieuDat> dsChiTiet = new ArrayList<>();
//        String sql = "SELECT * FROM ChiTietPhieuDat WHERE maPhieuDat = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//            stmt.setString(1, maPhieuDat);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                PhieuDatBan phieu = phieuDatBanDAO.getPhieuDatTheoMa(rs.getString("maPhieuDat"));
//                MonAn mon = monAnDAO.layMonAnTheoMa(rs.getString("maMon"));
//                dsChiTiet.add(new ChiTietPhieuDat(
//                    phieu,
//                    mon,
//                    rs.getInt("soLuong"),
//                    rs.getDouble("donGia"),
//                    rs.getString("ghiChu")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsChiTiet;
//    }
}