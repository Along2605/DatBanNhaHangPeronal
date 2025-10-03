package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.*;

public class ManHinhChinhQuanLy extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JMenuBar menuBar;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setTitle("Quản Lý Nhà Hàng");

        // Set BorderLayout for the entire frame
        getContentPane().setLayout(new BorderLayout());

        // Initialize JMenuBar
        menuBar = new JMenuBar();
        menuBar.setBorder(new LineBorder(new Color(0, 0, 0)));
        menuBar.setBackground(new Color(214, 116, 76, 255));
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        setJMenuBar(menuBar);

        // Add logo
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setSize(new Dimension(60, 60));
        lblNewLabel.setBackground(new Color(214, 116, 76, 180));
        lblNewLabel.setOpaque(true);
        ImageIcon icon = new ImageIcon("C:\\S1(2025-2016)\\PTUD\\DatBanNhaHang\\img\\logo_nhahang.png");
        Image img = icon.getImage().getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), Image.SCALE_SMOOTH);
        lblNewLabel.setIcon(new ImageIcon(img));
        menuBar.add(lblNewLabel);
        menuBar.add(Box.createHorizontalStrut(5));

        // Use MenuBuilder to create menus
        MenuBuilderQuanLy menuBuilder = new MenuBuilderQuanLy();
        menuBar.add(menuBuilder.createHeThongMenu());
        menuBar.add(menuBuilder.createMonAnMenu());
        menuBar.add(menuBuilder.createKhuVucMenu());
        menuBar.add(menuBuilder.createCaLamMenu());
        menuBar.add(menuBuilder.createNhanVienMenu());
        menuBar.add(menuBuilder.createDoanhThuMenu());

        // Initialize contentPane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout());
        getContentPane().add(contentPane, BorderLayout.CENTER);

        // Add background image
        JLabel lblBackground = new JLabel();
        ImageIcon bgIcon = new ImageIcon("C:\\S1(2025-2016)\\PTUD\\DatBanNhaHang\\img\\thiet-ke-nha-hang-han-quoc.jpg");
        Image bgImage = bgIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
        lblBackground.setIcon(new ImageIcon(bgImage));
        lblBackground.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(lblBackground, BorderLayout.CENTER);
    }

    // MenuBuilder class to create menus and menu items consistently
    private static class MenuBuilderQuanLy {
        private final Font menuFont = new Font("Segoe UI", Font.BOLD, 14);
        private final Font menuItemFont = new Font("Segoe UI", Font.PLAIN, 13);
        private final Dimension menuItemSize = new Dimension(200, 30);
        private final Color menuForeground = Color.WHITE;
        private final Color menuItemBackground = new Color(220, 220, 220);

        private JMenu createMenu(String title) {
            JMenu menu = new JMenu(title);
            menu.setFont(menuFont);
            menu.setForeground(menuForeground);
            menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return menu;
        }

        private JMenuItem createMenuItem(String title, ActionListener actionListener) {
            JMenuItem menuItem = new JMenuItem(title);
            menuItem.setFont(menuItemFont);
            menuItem.setPreferredSize(menuItemSize);
            menuItem.setBackground(menuItemBackground);
            if (actionListener != null) {
                menuItem.addActionListener(actionListener);
            }
            return menuItem;
        }

        public JMenu createHeThongMenu() {
            JMenu menu = createMenu("Hệ thống");
            menu.add(createMenuItem("Màn hình chính", null));
            menu.add(createMenuItem("Thoát", e -> System.exit(0)));
            return menu;
        }

        public JMenu createMonAnMenu() {
            JMenu menu = createMenu("Món ăn");
            menu.add(createMenuItem("Thêm", null));
            menu.add(createMenuItem("Cập nhật", null));
            menu.add(createMenuItem("Tra cứu", null));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createKhuVucMenu() {
            JMenu menu = createMenu("Khu vực");
            menu.add(createMenuItem("Xem danh sách", null));
            menu.add(createMenuItem("Thêm khu vực mới", null));
            menu.add(createMenuItem("Cập nhật", null));
            
            return menu;
        }

        public JMenu createCaLamMenu() {
            JMenu menu = createMenu("Ca làm");
            menu.add(createMenuItem("Tra cứu", null));
            menu.add(createMenuItem("Phân ca", null));
            menu.add(createMenuItem("Thêm ca mới", null));
            return menu;
        }

        public JMenu createNhanVienMenu() {
            JMenu menu = createMenu("Nhân viên");
            menu.add(createMenuItem("Xem danh sách", null));
            menu.add(createMenuItem("Cập nhật", null));
            menu.add(createMenuItem("Phân quyền", null));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createDoanhThuMenu() {
            JMenu menu = createMenu("Doanh thu");
            menu.add(createMenuItem("Thống kê trong ngày", null));
            menu.add(createMenuItem("Thống kê theo tuần", null));
            return menu;
        }
    }
}