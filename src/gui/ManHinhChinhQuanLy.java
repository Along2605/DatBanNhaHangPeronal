package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import connectDB.ConnectDB;

import java.awt.event.*;

public class ManHinhChinhQuanLy extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JMenuBar menuBar;
    
    private static JPanel mainPanelRef;

    public static void showPanel(JPanel panel) {
    	Component[] comps = mainPanelRef.getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel p) {
                p.removeAll();           // Dọn sạch bên trong
                p.setVisible(false);
            }
        }
        mainPanelRef.removeAll();        // Xóa khỏi container chính
        System.gc();                     // Gợi ý Java dọn rác (tạm thời)
        
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
        
        // thoát ứng dụng và đóng kết nối database      
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ManHinhChinhQuanLy.this,
                    "Bạn có chắc muốn thoát không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    ConnectDB.getInstance().close();
                    System.exit(0);
                }
            }
        });
        setTitle("Quản Lý Nhà Hàng");

        // Thiết lập layout cho toàn bộ frame
        getContentPane().setLayout(new BorderLayout());
        
        
     // Lấy kích thước màn hình và thiết lập kích thước cửa sổ
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

        // Hiển thị cửa sổ
        setVisible(true);
        
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
        ImageIcon icon = new ImageIcon("img\\logo_nhahang.png");
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
        
        mainPanelRef = contentPane;
        
        // Add background image
        JLabel lblBackground = new JLabel();
        ImageIcon bgIcon = new ImageIcon("img\\thiet-ke-nha-hang-han-quoc.jpg");
        Image bgImage = bgIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
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
            menu.add(createMenuItem("Đăng xuất", e-> {
            	JFrame topFrame= (JFrame) SwingUtilities.getWindowAncestor(mainPanelRef);
            	if(topFrame !=null) {
            		topFrame.dispose();  // đóng mh chính
            	}
            	new DangNhap().setVisible(true);
            }));
            menu.add(createMenuItem("Thoát", e -> System.exit(0)));
            return menu;
        }

       
		public JMenu createMonAnMenu() {
            JMenu menu = createMenu("Món ăn");
       
            menu.add(createMenuItem("Cập nhật", e-> showPanel(new ThemMonAn())));
            menu.add(createMenuItem("Tra cứu", e->showPanel(new TraCuuMonAn())));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createKhuVucMenu() {
            JMenu menu = createMenu("Khu vực");
            menu.add(createMenuItem("Xem danh sách bàn ăn", null));
            menu.add(createMenuItem("Cập nhật bàn ăn", e -> showPanel(new ThemBanAn())));
            menu.add(createMenuItem("Tra cứu bàn ăn", e -> showPanel(new TraCuuBanAn())));
            
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
            menu.add(createMenuItem("Xem danh sách", e-> showPanel(new ManHinhDSNhanVien())));
            menu.add(createMenuItem("Cập nhật", e-> showPanel(new ManHinhCapNhatNhanVien())));
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