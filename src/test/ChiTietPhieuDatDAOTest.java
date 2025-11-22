package test;

import dao.ChiTietPhieuDatDAO;
import dao.MonAnDAO;
import dao.PhieuDatBanDAO;
import entity.ChiTietPhieuDat;
import entity.MonAn;
import entity.PhieuDatBan;
import connectDB.ConnectDB;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChiTietPhieuDatDAOTest {

    private static ChiTietPhieuDatDAO chiTietDAO;
    private static PhieuDatBanDAO phieuDAO;
    private static MonAnDAO monAnDAO;
    private static Connection con;

    @BeforeAll
    static void setUpBeforeAll() {
        System.out.println("Kết nối CSDL...");
        ConnectDB.getInstance().connect();
        con = ConnectDB.getConnection();

        chiTietDAO = new ChiTietPhieuDatDAO();
        phieuDAO = new PhieuDatBanDAO();
        monAnDAO = new MonAnDAO();
    }

    @AfterAll
    static void tearDownAfterAll() {
        try {
            if (con != null)
                con.close();
            System.out.println("Đã đóng kết nối CSDL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testThemChiTietPhieudat() {
        System.out.println("▶️ testThemChiTietPhieudat");

        // Giả sử DB có sẵn mã hợp lệ
        PhieuDatBan phieu = new PhieuDatBan();
        phieu.setMaPhieuDat("PD001");

        MonAn mon = new MonAn();
        mon.setMaMon("MA001");

        ChiTietPhieuDat ct = new ChiTietPhieuDat();
        ct.setPhieuDatBan(phieu);
        ct.setMonAn(mon);
        ct.setSoLuong(2);
        ct.setDonGia(55000);
        ct.setGhiChu("Món test JUnit");

        boolean result = chiTietDAO.themChiTietPhieudat(ct);
        assertTrue(result, "Không thêm được chi tiết phiếu đặt");

        System.out.println("Đã thêm chi tiết phiếu đặt thành công!");
    }

    @Test
    void testCapNhatCTPD() {
        System.out.println(" testCapNhatCTPD");

        PhieuDatBan phieu = new PhieuDatBan();
        phieu.setMaPhieuDat("PD001");

        MonAn mon = new MonAn();
        mon.setMaMon("MA001");

        ChiTietPhieuDat ct = new ChiTietPhieuDat();
        ct.setPhieuDatBan(phieu);
        ct.setMonAn(mon);
        ct.setSoLuong(5);
        ct.setDonGia(60000);
        ct.setGhiChu("Cập nhật test JUnit");

        boolean result = chiTietDAO.capNhatCTPD(ct);
        assertTrue(result, " Không cập nhật được chi tiết phiếu đặt");

        System.out.println(" Cập nhật thành công!");
    }

    @Test
    void testGetMonAnTheoPhieu() {
        System.out.println("testGetMonAnTheoPhieu");

        List<ChiTietPhieuDat> ds = chiTietDAO.getMonAnTheoPhieu("PD001");
        assertNotNull(ds, "Danh sách null");
        assertTrue(ds.size() > 0, "❌ Không có món nào trong phiếu PD001");

        System.out.println(" Lấy được " + ds.size() + " món trong phiếu đặt.");
    }

    @Test
    void testXoaCTPD() {
        System.out.println("testXoaCTPD");

        boolean result = chiTietDAO.xoaCTPD("PD001", "MA001");
        assertTrue(result, " Không xóa được chi tiết phiếu đặt");

        System.out.println(" Xóa thành công chi tiết phiếu đặt!");
    }
}
