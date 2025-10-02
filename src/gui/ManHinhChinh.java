package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import javax.swing.border.LineBorder;

public class ManHinhChinh extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JMenuBar menuBar;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManHinhChinh frame = new ManHinhChinh();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManHinhChinh() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setTitle("Quản Lý Nhà Hàng");

        // Set BorderLayout for the entire frame
        getContentPane().setLayout(new BorderLayout());

        // Tạo JMenuBar với màu nền
        menuBar = new JMenuBar();
        menuBar.setBorder(new LineBorder(new Color(0, 0, 0)));
        menuBar.setBackground(new Color(214, 116, 76, 255)); // Màu của nhà hàng
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        setJMenuBar(menuBar); // Place JMenuBar at the top (NORTH equivalent)

        // Thêm logo
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setSize(new Dimension(60, 60));
        lblNewLabel.setBackground(new Color(214, 116, 76, 180));
        lblNewLabel.setOpaque(true);
        ImageIcon icon = new ImageIcon("C:\\S1(2025-2016)\\PTUD\\QuanLyNhaHang\\img\\logo_nhahang.png");
        Image img = icon.getImage().getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), Image.SCALE_SMOOTH);
        lblNewLabel.setIcon(new ImageIcon(img));
        menuBar.add(lblNewLabel);
        menuBar.add(Box.createHorizontalStrut(5));

        // Tùy chỉnh phông chữ cho JMenu
        Font menuFont = new Font("Segoe UI", Font.BOLD, 14);
        Font menuItemFont = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension menuItemSize = new Dimension(200, 30);

        // 1. Menu Hệ thống
        JMenu menuHeThong = new JMenu("Hệ thống");
        menuHeThong.setFont(menuFont);
        menuHeThong.setForeground(Color.WHITE);
        menuHeThong.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuHeThong);

        JMenuItem mnht_ManHinhChinh = new JMenuItem("Màn hình chính");
        mnht_ManHinhChinh.setFont(menuItemFont);
        mnht_ManHinhChinh.setPreferredSize(menuItemSize);
        mnht_ManHinhChinh.setBackground(new Color(220, 220, 220));
        menuHeThong.add(mnht_ManHinhChinh);

        JMenuItem mnht_DangXuat = new JMenuItem("Đăng xuất");
        mnht_DangXuat.setFont(menuItemFont);
        mnht_DangXuat.setPreferredSize(menuItemSize);
        mnht_DangXuat.setBackground(new Color(220, 220, 220));
        menuHeThong.add(mnht_DangXuat);

        JMenuItem mnht_DoiMatKhau = new JMenuItem("Đổi mật khẩu");
        mnht_DoiMatKhau.setFont(menuItemFont);
        mnht_DoiMatKhau.setPreferredSize(menuItemSize);
        mnht_DoiMatKhau.setBackground(new Color(220, 220, 220));
        menuHeThong.add(mnht_DoiMatKhau);

        JMenuItem mnht_Thoat = new JMenuItem("Thoát");
        mnht_Thoat.setFont(menuItemFont);
        mnht_Thoat.setPreferredSize(menuItemSize);
        mnht_Thoat.setBackground(new Color(220, 220, 220));
        menuHeThong.add(mnht_Thoat);

        // 2. Menu Quản lý đặt bàn
        JMenu menuQuanLiDatBan = new JMenu("Đặt bàn");
        menuQuanLiDatBan.setFont(menuFont);
        menuQuanLiDatBan.setForeground(Color.WHITE);
        menuQuanLiDatBan.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuQuanLiDatBan);

        JMenuItem mnqldb_DanhSachBan = new JMenuItem("Danh sách bàn");
        mnqldb_DanhSachBan.setFont(menuItemFont);
        mnqldb_DanhSachBan.setPreferredSize(menuItemSize);
        mnqldb_DanhSachBan.setBackground(new Color(220, 220, 220));
        menuQuanLiDatBan.add(mnqldb_DanhSachBan);

        JMenuItem mnqldb_DatBan = new JMenuItem("Đặt bàn");
        mnqldb_DatBan.setFont(menuItemFont);
        mnqldb_DatBan.setPreferredSize(menuItemSize);
        mnqldb_DatBan.setBackground(new Color(220, 220, 220));
        menuQuanLiDatBan.add(mnqldb_DatBan);

        JMenuItem mnqldb_HuyDatBan = new JMenuItem("Hủy đặt bàn");
        mnqldb_HuyDatBan.setFont(menuItemFont);
        mnqldb_HuyDatBan.setPreferredSize(menuItemSize);
        mnqldb_HuyDatBan.setBackground(new Color(220, 220, 220));
        menuQuanLiDatBan.add(mnqldb_HuyDatBan);

        JMenuItem mnqldb_TrangThaiBan = new JMenuItem("Trạng thái bàn");
        mnqldb_TrangThaiBan.setFont(menuItemFont);
        mnqldb_TrangThaiBan.setPreferredSize(menuItemSize);
        mnqldb_TrangThaiBan.setBackground(new Color(220, 220, 220));
        menuQuanLiDatBan.add(mnqldb_TrangThaiBan);

        // 3. Menu Khách hàng
        JMenu menuKhachHang = new JMenu("Khách hàng");
        menuKhachHang.setFont(menuFont);
        menuKhachHang.setForeground(Color.WHITE);
        menuKhachHang.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuKhachHang);

        JMenuItem mnkh_ThemMoi = new JMenuItem("Thêm khách hàng");
        mnkh_ThemMoi.setFont(menuItemFont);
        mnkh_ThemMoi.setPreferredSize(menuItemSize);
        mnkh_ThemMoi.setBackground(new Color(220, 220, 220));
        menuKhachHang.add(mnkh_ThemMoi);

        JMenuItem mnkh_DanhSachKhachHang = new JMenuItem("Danh sách khách");
        mnkh_DanhSachKhachHang.setFont(menuItemFont);
        mnkh_DanhSachKhachHang.setPreferredSize(menuItemSize);
        mnkh_DanhSachKhachHang.setBackground(new Color(220, 220, 220));
        menuKhachHang.add(mnkh_DanhSachKhachHang);

        JMenuItem mnkh_LichSu = new JMenuItem("Lịch sử khách hàng");
        mnkh_LichSu.setFont(menuItemFont);
        mnkh_LichSu.setPreferredSize(menuItemSize);
        mnkh_LichSu.setBackground(new Color(220, 220, 220));
        menuKhachHang.add(mnkh_LichSu);

        // 4. Menu Hóa đơn
        JMenu menuHoaDon = new JMenu("Hóa đơn");
        menuHoaDon.setFont(menuFont);
        menuHoaDon.setForeground(Color.WHITE);
        menuHoaDon.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuHoaDon);

        JMenuItem mnhd_TaoMoi = new JMenuItem("Tạo hóa đơn");
        mnhd_TaoMoi.setFont(menuItemFont);
        mnhd_TaoMoi.setPreferredSize(menuItemSize);
        mnhd_TaoMoi.setBackground(new Color(220, 220, 220));
        menuHoaDon.add(mnhd_TaoMoi);

        JMenuItem mnhd_DanhSach = new JMenuItem("Danh sách hóa đơn");
        mnhd_DanhSach.setFont(menuItemFont);
        mnhd_DanhSach.setPreferredSize(menuItemSize);
        mnhd_DanhSach.setBackground(new Color(220, 220, 220));
        menuHoaDon.add(mnhd_DanhSach);

        JMenuItem mnhd_ThanhToan = new JMenuItem("Thanh toán");
        mnhd_ThanhToan.setFont(menuItemFont);
        mnhd_ThanhToan.setPreferredSize(menuItemSize);
        mnhd_ThanhToan.setBackground(new Color(220, 220, 220));
        menuHoaDon.add(mnhd_ThanhToan);

        // 5. Menu Thống kê
        JMenu menuThongKe = new JMenu("Thống kê");
        menuThongKe.setFont(menuFont);
        menuThongKe.setForeground(Color.WHITE);
        menuThongKe.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuThongKe);

        JMenuItem mntk_DoanhThu = new JMenuItem("Doanh thu");
        mntk_DoanhThu.setFont(menuItemFont);
        mntk_DoanhThu.setPreferredSize(menuItemSize);
        mntk_DoanhThu.setBackground(new Color(220, 220, 220));
        menuThongKe.add(mntk_DoanhThu);

        JMenuItem mntk_MonAn = new JMenuItem("Món ăn");
        mntk_MonAn.setFont(menuItemFont);
        mntk_MonAn.setPreferredSize(menuItemSize);
        mntk_MonAn.setBackground(new Color(220, 220, 220));
        menuThongKe.add(mntk_MonAn);

        JMenuItem mntk_XuatBaoCao = new JMenuItem("Xuất báo cáo");
        mntk_XuatBaoCao.setFont(menuItemFont);
        mntk_XuatBaoCao.setPreferredSize(menuItemSize);
        mntk_XuatBaoCao.setBackground(new Color(220, 220, 220));
        menuThongKe.add(mntk_XuatBaoCao);

        // Tạo contentPane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout());
        getContentPane().add(contentPane, BorderLayout.CENTER); // Add contentPane to CENTER of the frame

        // Thêm JLabel chứa ảnh nền
        JLabel lblBackground = new JLabel();
        ImageIcon bgIcon = new ImageIcon("C:\\S1(2025-2016)\\PTUD\\QuanLyNhaHang\\img\\thiet-ke-nha-hang-han-quoc.jpg");
        Image bgImage = bgIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH); // Scale to frame size
        lblBackground.setIcon(new ImageIcon(bgImage));
        lblBackground.setHorizontalAlignment(JLabel.CENTER); // Center the image
        contentPane.add(lblBackground, BorderLayout.CENTER); // Add to contentPane to fill the space

        // Xử lý sự kiện cho Thoát
        mnht_Thoat.addActionListener(e -> System.exit(0));
    }
}