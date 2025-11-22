package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.MonAn;

public class MonAnDAO {
	
	
    public MonAn layMonAnTheoMa(String maMonAn) {
    	Connection con = ConnectDB.getConnection();
        String sql = "select * from MonAn where maMon=?";
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maMonAn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MonAn(rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getDouble("gia"),
                        rs.getString("donViTinh"),
                        rs.getBoolean("trangThai"),
                        rs.getString("hinhAnh"),
                        rs.getInt("soLuong"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiMon")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MonAn> getAllMonAn() {
        List<MonAn> dsMonAn = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM MonAn";
        try (
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dsMonAn.add(new MonAn(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getDouble("gia"),
                        rs.getString("donViTinh"),
                        rs.getBoolean("trangThai"),
                        rs.getString("hinhAnh"),
                        rs.getInt("soLuong"),
                        rs.getString("moTa"),
                        rs.getString("maLoaiMon")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsMonAn;
    }

    public String chuyenDoiLoaiSangMaLoai(String tenLoai) {
        if (tenLoai.equalsIgnoreCase("Món chính"))
            return "MC";
        if (tenLoai.equalsIgnoreCase("Món ăn kèm")) {
            return "MK";
        }
        else return "DO";
    }

    public String chuyenDoiMaLoaiSangTen(String maLoai) {
        if (maLoai.equalsIgnoreCase("MC"))
            return "Món chính";
        if (maLoai.equalsIgnoreCase("DO")) {
            return "Đồ uống";
        }
        else return "Món ăn kèm";
    }

    public boolean themMonMoi(MonAn mon) {
    	Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO MonAn (maMon, tenMon, gia, donViTinh, trangThai, hinhAnh, soLuong, moTa, maLoaiMon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, mon.getMaMon());
            stmt.setString(2, mon.getTenMon());
            stmt.setDouble(3, mon.getGia());
            stmt.setString(4, mon.getDonViTinh());
            stmt.setBoolean(5, mon.isTrangThai());
            stmt.setString(6, mon.getHinhAnh());
            stmt.setInt(7, mon.getSoLuong());
            stmt.setString(8, mon.getMoTa());
            stmt.setString(9, mon.getLoaiMon()); // Đã là mã loại (MC, DO, MK)
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaMonAn(MonAn mon) {
        String sql = "UPDATE MonAn SET tenMon = ?, gia = ?, donViTinh = ?, trangThai = ?, hinhAnh = ?, soLuong = ?, moTa = ?, maLoaiMon = ? WHERE maMon = ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, mon.getTenMon());
            stmt.setDouble(2, mon.getGia());
            stmt.setString(3, mon.getDonViTinh());
            stmt.setBoolean(4, mon.isTrangThai());
            stmt.setString(5, mon.getHinhAnh());
            stmt.setInt(6, mon.getSoLuong());
            stmt.setString(7, mon.getMoTa());
            stmt.setString(8, mon.getLoaiMon()); 
            stmt.setString(9, mon.getMaMon());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean anMonAn(String maMon) throws SQLException {
        String sql = "UPDATE MonAn SET trangThai = 0 WHERE maMon = ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maMon);
            return pst.executeUpdate() > 0;
        }
    }


    public ArrayList<String> layRaLoaiMonAn() throws SQLException {
        ArrayList<String> dsTenLoaiMonAn = new ArrayList<>();
        String sql = "SELECT tenLoaiMon FROM LoaiMonAn";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dsTenLoaiMonAn.add(rs.getString("tenLoaiMon"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return dsTenLoaiMonAn;
    }

    public ArrayList<String> layRaDonViTinh() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT donViTinh FROM MonAn WHERE donViTinh IS NOT NULL";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("donViTinh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String layMaMonLonNhatTheoLoai(String maLoai) {
    	Connection con = ConnectDB.getConnection();
        String sql = "SELECT TOP 1 maMon FROM MonAn WHERE maMon LIKE ? ORDER BY maMon DESC";
        try (
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLoai + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("maMon");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themLoaiMon(String maLoai, String tenLoai) throws SQLException {
    	Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO LoaiMonAn (maLoaiMon, tenLoaiMon) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            ps.setString(2, tenLoai);
            return ps.executeUpdate() > 0;
        }
    }
    

    public String chuyenMaLoaiSangTen(String maLoai) throws SQLException {
    	Connection con = ConnectDB.getConnection();
        String sql = "SELECT tenLoaiMon FROM LoaiMonAn WHERE maLoaiMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenLoaiMon");
            }
        }
        return maLoai; 
    }

    public String chuyenTenLoaiSangMa(String tenLoai) throws SQLException {
    	Connection con = ConnectDB.getConnection();
        String sql = "SELECT maLoaiMon FROM LoaiMonAn WHERE tenLoaiMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenLoai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maLoaiMon");
            }
        }
        return tenLoai; // fallback
    }
}