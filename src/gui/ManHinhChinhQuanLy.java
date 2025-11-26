package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import connectDB.ConnectDB;
import entity.NhanVien;
import util.Session;
import java.awt.event.*;

public class ManHinhChinhQuanLy extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JMenuBar menuBar;
    private static JPanel mainPanelRef;
    private JMenu menuTaiKhoan; // Lưu tham chiếu để cập nhật

    public static void showPanel(JPanel panel) {
        Component[] comps = mainPanelRef.getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel p) {
                p.removeAll();
                p.setVisible(false);
            }
        }
        mainPanelRef.removeAll();
        System.gc();
        mainPanelRef.add(panel, BorderLayout.CENTER);
        mainPanelRef.revalidate();
        mainPanelRef.repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManHinhChinhQuanLy frame = new ManHinhChinhQuanLy();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManHinhChinhQuanLy() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ManHinhChinhQuanLy.this,
                    "Bạn có chắc muốn thoát không?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    ConnectDB.getInstance().close();
                    System.exit(0);
                }
            }
        });

        setTitle("Quản Lý Nhà Hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setVisible(true);

        getContentPane().setLayout(new BorderLayout());

        // === MENU BAR ===
        menuBar = new JMenuBar();
        menuBar.setBorder(new LineBorder(new Color(0, 0, 0)));
        menuBar.setBackground(new Color(214, 116, 76, 255));
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
        setJMenuBar(menuBar);

        // Logo
        JLabel lblLogo = new JLabel("");
        lblLogo.setPreferredSize(new Dimension(60, 60));
        lblLogo.setBackground(new Color(214, 116, 76, 180));
        lblLogo.setOpaque(true);
        ImageIcon icon = new ImageIcon("img/logo_nhahang.png");
        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(img));
        menuBar.add(lblLogo);
        menuBar.add(Box.createHorizontalStrut(10));

        // Tạo menu
        MenuBuilderQuanLy menuBuilder = new MenuBuilderQuanLy();
        menuBar.add(menuBuilder.createTrangChu());
        menuBar.add(menuBuilder.createMonAnMenu());
        menuBar.add(menuBuilder.createKhuVucMenu());
        menuBar.add(menuBuilder.createCaLamMenu());
        menuBar.add(menuBuilder.createNhanVienMenu());
        menuBar.add(menuBuilder.createKhachHangMenu());
        menuBar.add(menuBuilder.createHoaDonMenu());
        menuBar.add(Box.createHorizontalGlue());

        // Tạo menu tài khoản (ban đầu ẩn)
        menuTaiKhoan = new JMenu("Tài khoản");
        menuTaiKhoan.setVisible(false);
        menuBar.add(menuTaiKhoan);

        // === CONTENT PANE ===
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout());
        getContentPane().add(contentPane, BorderLayout.CENTER);
        mainPanelRef = contentPane;

        // Background
        JLabel lblBackground = new JLabel();
        ImageIcon bgIcon = new ImageIcon("img/thiet-ke-nha-hang-han-quoc.jpg");
        Image bgImage = bgIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        lblBackground.setIcon(new ImageIcon(bgImage));
        lblBackground.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(lblBackground, BorderLayout.CENTER);

        // === KIỂM TRA ĐĂNG NHẬP & CẬP NHẬT MENU ===
        capNhatMenuTaiKhoan();
    }

    // Gọi khi đăng nhập/đăng xuất
    public void capNhatMenuTaiKhoan() {
        menuTaiKhoan.removeAll();
        menuTaiKhoan.setVisible(false);

        NhanVien nv = Session.getNhanVienDangNhap();
        if (nv != null) {
            String hoTen = nv.getHoTen() != null ? nv.getHoTen() : "Nhân viên";
            String gioiTinh = nv.isGioiTinh() == true ? "Nam" : "Nữ";
            String path = gioiTinh.equalsIgnoreCase("Nam") ? "img/man.png" : "img/woman.png";

            ImageIcon iconUser = new ImageIcon(path);
            Image imgUser = iconUser.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            iconUser = new ImageIcon(imgUser);

            menuTaiKhoan.setText("Xin chào, " + hoTen);
            menuTaiKhoan.setIcon(iconUser);
            menuTaiKhoan.setFont(new Font("Segoe UI", Font.BOLD, 14));
            menuTaiKhoan.setForeground(Color.WHITE);

            menuTaiKhoan.add(createMenuItem("Thông tin cá nhân", e -> showPanel(new ThongTinCaNhan())));
            menuTaiKhoan.add(createMenuItem("Đổi mật khẩu", e -> showPanel(new DoiMatKhau())));
            menuTaiKhoan.addSeparator();
            menuTaiKhoan.add(createMenuItem("Đăng xuất", e -> dangXuat()));

            menuTaiKhoan.setVisible(true);
        } else {
            // Nếu chưa login → có thể thêm "Đăng nhập"
            menuTaiKhoan.setText("Tài khoản");
            menuTaiKhoan.add(createMenuItem("Đăng nhập", e -> moDangNhap()));
            menuTaiKhoan.setVisible(true);
        }
        menuBar.revalidate();
        menuBar.repaint();
    }

    private void dangXuat() {
        Session.logout();
        JOptionPane.showMessageDialog(this, "Đăng xuất thành công!");
        dispose();
        new DangNhap().setVisible(true);
    }

    private void moDangNhap() {
        new DangNhap().setVisible(true);
        dispose();
    }

    private JMenuItem createMenuItem(String title, ActionListener action) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (action != null) item.addActionListener(action);
        return item;
    }

    // === MenuBuilderQuanLy (giữ nguyên, chỉ sửa 1 chỗ nhỏ) ===
    private class MenuBuilderQuanLy {
        private final Font menuFont = new Font("Segoe UI", Font.BOLD, 14);
        private final Font menuItemFont = new Font("Segoe UI", Font.PLAIN, 13);
        private final Color menuForeground = Color.WHITE;

        private JMenu createMenu(String title) {
            JMenu menu = new JMenu(title);
            menu.setFont(menuFont);
            menu.setForeground(menuForeground);
            menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return menu;
        }

        private JMenuItem createMenuItem(String title, ActionListener action) {
            JMenuItem item = new JMenuItem(title);
            item.setFont(menuItemFont);
            if (action != null) item.addActionListener(action);
            return item;
        }

        public JMenu createTrangChu() {
            JMenu menu = createMenu("Trang chủ");
            menu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPanel(new Home());
                }
            });
            return menu;
        }

        public JMenu createMonAnMenu() {
            JMenu menu = createMenu("Món ăn");
            menu.add(createMenuItem("Tra cứu", e -> showPanel(new TraCuuMonAn())));
            menu.add(createMenuItem("Cập nhật", e -> showPanel(new ThemMonAn())));
            menu.add(createMenuItem("Đặt món", e -> showPanel(new ManHinhDatMon(null))));
            menu.add(createMenuItem("Thống kê", e -> showPanel(new ManHinhThongKeMonAn())));
            return menu;
        }

        public JMenu createKhuVucMenu() {
            JMenu menu = createMenu("Khu vực");
            menu.add(createMenuItem("Cập nhật bàn ăn", e -> showPanel(new ThemBanAn())));
            menu.add(createMenuItem("Tra cứu bàn ăn", e -> showPanel(new TraCuuBanAn())));
            return menu;
        }

        public JMenu createCaLamMenu() {
            JMenu menu = createMenu("Ca làm");
            menu.add(createMenuItem("Tra cứu ca làm", e -> showPanel(new TraCuuCaLam())));
            menu.add(createMenuItem("Phân ca làm", e -> showPanel(new PhanCaLam())));
            menu.add(createMenuItem("Cập nhật ca làm", e -> showPanel(new ThemCaLam())));
            return menu;
        }

        public JMenu createNhanVienMenu() {
            JMenu menu = createMenu("Nhân viên");
            menu.add(createMenuItem("Xem danh sách", e -> showPanel(new ManHinhDSNhanVien())));
            menu.add(createMenuItem("Cập nhật", e -> showPanel(new ManHinhCapNhatNhanVien())));
            menu.add(createMenuItem("Nhật ký hoạt động", e -> showPanel(new ManHinhNhatKyNhanVien())));
            
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createKhachHangMenu() {
            JMenu menu = createMenu("Khách hàng");
            menu.add(createMenuItem("Thêm mới", e -> showPanel(new TraCuuKhachHang())));
            menu.add(createMenuItem("Tra cứu", e -> showPanel(new TraCuuKhachHang())));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createHoaDonMenu() {
            JMenu menu = createMenu("Hóa đơn");
            menu.add(createMenuItem("Tra cứu hóa đơn", e -> showPanel(new TraCuuHoaDon())));
            JMenu menuThongKe = createMenu("Thống kê");
            menuThongKe.add(createMenuItem("Theo ngày", e -> showPanel(new ManHinhThongKeHoaDonTheoNgay())));
            menuThongKe.add(createMenuItem("Theo tháng", e -> showPanel(new ManHinhThongKeHoaDonTheoThang())));
            menuThongKe.add(createMenuItem("Theo năm", e -> showPanel(new ManHinhThongKeHoaDonTheoNam())));
            menu.add(menuThongKe);
            return menu;
        }
    }
}