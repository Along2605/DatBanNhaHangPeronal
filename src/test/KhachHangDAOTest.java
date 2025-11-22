package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.*;

import connectDB.ConnectDB;
import dao.KhachHangDAO;
import entity.KhachHang;

public class KhachHangDAOTest {

    private static Connection con;
    private KhachHangDAO dao;
    private KhachHang khTest;

    @BeforeAll
    static void beforeAll() {
        try {
            // Kết nối đến SQL Server 1 lần cho toàn bộ test
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
            System.out.println("✅ Đã kết nối database thành công.");
        } catch (Exception e) {
            fail("❌ Kết nối database thất bại: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        dao = new KhachHangDAO();

        // Tạo 1 khách hàng test mẫu
        khTest = new KhachHang(
                "KH999",
                "Nguyễn Tester",
                true,
                "0909000999",
                100,
                LocalDate.now(),
                true
        );
    }

    @AfterEach
    void tearDown() {
        // Xóa khách hàng test sau khi chạy xong để DB sạch
        try {
            String sql = "DELETE FROM KhachHang WHERE maKH = ?";
            var ps = con.prepareStatement(sql);
            ps.setString(1, khTest.getMaKH());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("⚠️ Không thể xóa dữ liệu test: " + e.getMessage());
        }
    }

    @AfterAll
    static void afterAll() {
        try {
            con.close();
            System.out.println("🔒 Đã đóng kết nối database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void testThemKhachHang() {
        boolean result = dao.themKhachHang(khTest);
        assertTrue(result, "❌ Thêm khách hàng thất bại!");
    }

    @Test
    void testTimKhachHangTheoMa() {
        
        dao.themKhachHang(khTest);

        KhachHang found = dao.timKhachHangTheoMa("KH999");
        assertNotNull(found, "❌ Không tìm thấy khách hàng theo mã!");
        assertEquals(khTest.getHoTen(), found.getHoTen(), "Tên khách hàng không khớp!");
    }

    @Test
    void testTimKhachHangTheoSDT() {
        dao.themKhachHang(khTest);

        KhachHang found = dao.timKhachHangTheoSDT("0909000999");
        assertNotNull(found, "❌ Không tìm thấy khách hàng theo SĐT!");
        assertEquals("KH999", found.getMaKH());
    }

    @Test
    void testLayDanhSachKhachHang() {
        List<KhachHang> ds = dao.layDanhSachKhachHang();
        assertNotNull(ds, "❌ Danh sách khách hàng null!");
        assertTrue(ds.size() > 0, "❌ Danh sách khách hàng rỗng!");
    }

    @Test
    void testTaoMaKhachHangTuDong() {
        String maMoi = dao.taoMaKhachHangTuDong();
        assertTrue(maMoi.startsWith("KH"), "❌ Mã không có tiền tố KH");
        assertTrue(maMoi.length() >= 4, "❌ Mã khách hàng quá ngắn");
    }
}
