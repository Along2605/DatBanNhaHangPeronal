package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connectDB.ConnectDB; // Lớp kết nối của bạn
import entity.LoaiMon;

public class LoaiMonAnDAO {
    public List<LoaiMon> getDsLoaiMonAn() {
        List<LoaiMon> dsLoaiMon = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM LoaiMonAn";
        try (Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                LoaiMon loai = new LoaiMon(
                    rs.getString("maLoaiMon"),
                    rs.getString("tenLoaiMon")
                );
                dsLoaiMon.add(loai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLoaiMon;
    }
}