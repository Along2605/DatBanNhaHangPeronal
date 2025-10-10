package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import connectDB.ConnectDB;

import java.awt.event.*;

public class ManHinhChinhNhanVien extends JFrame {

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
                ManHinhChinhNhanVien frame = new ManHinhChinhNhanVien();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
   
    public ManHinhChinhNhanVien() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // thoát ứng dụng và đóng kết nối database
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ManHinhChinhNhanVien.this,
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
        getContentPane().setLayout(new BorderLayout());
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

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
        MenuBuilder menuBuilder = new MenuBuilder();
        menuBar.add(menuBuilder.createHeThongMenu());
        menuBar.add(menuBuilder.createBanAnMenu());
        menuBar.add(menuBuilder.createKhachHangMenu());
        menuBar.add(menuBuilder.createHoaDonMenu());
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
    private static class MenuBuilder {
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

        public JMenu createBanAnMenu() {
            JMenu menu = createMenu("Bàn ăn");
            menu.add(createMenuItem("Tra cứu", e-> showPanel(new TraCuuBanAn())));
            menu.add(createMenuItem("Đặt bàn", e-> showPanel(new DatBan())));
            menu.add(createMenuItem("Hủy bàn", null));
            return menu;
        }

        public JMenu createKhachHangMenu() {
            JMenu menu = createMenu("Khách hàng");
            menu.add(createMenuItem("Thêm mới", e -> showPanel(new ThemKhachHang())));
            menu.add(createMenuItem("Tra cứu", e -> showPanel(new TraCuuKhachHang())));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createHoaDonMenu() {
            JMenu menu = createMenu("Hóa đơn");
           
            menu.add(createMenuItem("Tra cứu", null));
            return menu;
        }

        public JMenu createDoanhThuMenu() {
            JMenu menu = createMenu("Doanh thu");
            menu.add(createMenuItem("Thống kê trong ngày", null));
            return menu;
        }
    }
}