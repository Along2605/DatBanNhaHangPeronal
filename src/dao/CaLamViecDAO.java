package dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.CaLamViec;

public class CaLamViecDAO {
    public boolean themCaLamViec(CaLamViec ca) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "INSERT INTO CaLamViec(maCa, gioVaoLam, gioTanLam, trangThai) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, ca.getMaCa());
                // chuyển LocalTime -> java.sql.Time
                stmt.setTime(2, Time.valueOf(ca.getGioVaoLam()));
                stmt.setTime(3, Time.valueOf(ca.getGioTanLam()));
                stmt.setBoolean(4, ca.isTrangThai());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatCaLamViec(CaLamViec ca) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "UPDATE CaLamViec SET gioVaoLam=?, gioTanLam=?, trangThai=? WHERE maCa=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setTime(1, Time.valueOf(ca.getGioVaoLam()));
                stmt.setTime(2, Time.valueOf(ca.getGioTanLam()));
                stmt.setBoolean(3, ca.isTrangThai());
                stmt.setString(4, ca.getMaCa());
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CaLamViec> layDanhSachCaLamViec() {
        List<CaLamViec> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "SELECT * FROM CaLamViec";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    CaLamViec ca = new CaLamViec(
                        rs.getString("maCa"),
                        rs.getTime("gioVaoLam").toLocalTime(), // chuyển Time -> LocalTime
                        rs.getTime("gioTanLam").toLocalTime(),
                        rs.getBoolean("trangThai")
                    );
                    ds.add(ca);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public CaLamViec timCaLamViecTheoMa(String maCa) {
        Connection con = ConnectDB.getConnection();
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            String sql = "SELECT * FROM CaLamViec WHERE maCa=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maCa);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new CaLamViec(
                            rs.getString("maCa"),
                            rs.getTime("gioVaoLam").toLocalTime(),
                            rs.getTime("gioTanLam").toLocalTime(),
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
}