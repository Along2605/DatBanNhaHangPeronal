package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.NhatKyThaoTac;
import util.Session;

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
				p.removeAll(); // Dọn sạch bên trong
				p.setVisible(false);
			}
		}
		mainPanelRef.removeAll(); // Xóa khỏi container chính
		System.gc(); // Gợi ý Java dọn rác (tạm thời)

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
				int confirm = JOptionPane.showConfirmDialog(ManHinhChinhQuanLy.this, "Bạn có chắc muốn thoát không?",
						"Xác nhận", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					ConnectDB.getInstance().close();
					System.exit(0);
				}
			}
		});
		setTitle("Quản Lý");
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Thiết lập layout cho toàn bộ frame
//		getContentPane().setLayout(new BorderLayout());

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
		menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
		setJMenuBar(menuBar);

		// Add logo
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setSize(new Dimension(60, 60));
		lblNewLabel.setBackground(new Color(214, 116, 76, 180));
		lblNewLabel.setOpaque(true);
		ImageIcon icon = new ImageIcon("img\\logo_nhahang.png");
		Image img = icon.getImage().getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(),
				Image.SCALE_SMOOTH);
		lblNewLabel.setIcon(new ImageIcon(img));
		menuBar.add(lblNewLabel);
		menuBar.add(Box.createHorizontalStrut(10));

		// Use MenuBuilder to create menus
		MenuBuilderQuanLy menuBuilder = new MenuBuilderQuanLy();
		menuBar.add(menuBuilder.createTrangChu());
		menuBar.add(menuBuilder.createMonAnMenu());
		menuBar.add(menuBuilder.createBanAnMenu());
		menuBar.add(menuBuilder.createPhieuDatMenu());
		menuBar.add(menuBuilder.createKhuVucMenu());
		menuBar.add(menuBuilder.createCaLamMenu());
		menuBar.add(menuBuilder.createNhanVienMenu());
		menuBar.add(menuBuilder.createKhachHangMenu());
		menuBar.add(menuBuilder.createHoaDonMenu());
		
		menuBar.add(Box.createHorizontalGlue()); // đẩy các componet ra cuối
		
		menuBar.add(menuBuilder.createTaiKhoanMenu());
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
			   if(title.equalsIgnoreCase("Thống kê")) {
				    menu.setFont(menuItemFont);
					menu.setPreferredSize(menuItemSize);
					menu.setBackground(menuItemBackground);
					menu.setBorder(null);
					menu.setForeground(null);
			   }
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

		public JMenu createTrangChu() {
            JMenu menu = createMenu("Trang chủ");
            
            menu.addMouseListener(new MouseAdapter() {
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
			menu.add(createMenuItem("Đặt món", e -> showPanel(new ManHinhDatMon())));
			menu.add(createMenuItem("Thống kê", e -> showPanel(new ManHinhThongKeMonAn())));
			return menu;
		}
		
		public JMenu createBanAnMenu() {
			JMenu menu = createMenu("Bàn ăn");
			menu.add(createMenuItem("Đặt bàn", e -> showPanel(new DatBan())));
			menu.add(createMenuItem("Cập nhật bàn ăn", e -> showPanel(new ThemBanAn())));
			menu.add(createMenuItem("Thêm loại bàn", e -> showPanel(new QuanLyLoaiBan())));
			menu.add(createMenuItem("Tra cứu bàn ăn", e -> showPanel(new TraCuuBanAn())));

			return menu;
		}
		
		 public JMenu createPhieuDatMenu() {
	            JMenu menu = createMenu("Phiếu đặt");
	            menu.add(createMenuItem("Tra cứu", e -> showPanel(new TraCuuPhieuDat())));
	            
	            return menu;
	       }

		public JMenu createKhuVucMenu() {
			JMenu menu = createMenu("Khu vực");
			menu.add(createMenuItem("Sơ đồ bàn ăn", e -> showPanel(new SoDoBanAn())));
			menu.add(createMenuItem("Quản lý khu vực", e -> showPanel(new QuanLyKhuVuc())));

			return menu;
		}


		public JMenu createCaLamMenu() {
            JMenu menu = createMenu("Ca làm");
            menu.add(createMenuItem("Xem lịch làm", e -> showPanel(new XemLichLam())));
            menu.add(createMenuItem("Cập nhật ca làm", e -> showPanel(new ThemCaLam())));
            menu.add(createMenuItem("Phân ca làm", e -> showPanel(new PhanCaLam())));

            return menu;
        }

		public JMenu createNhanVienMenu() {
			JMenu menu = createMenu("Nhân viên");
			menu.add(createMenuItem("Xem danh sách", e -> showPanel(new ManHinhDSNhanVien())));
			menu.add(createMenuItem("Cập nhật", e -> showPanel(new ManHinhCapNhatNhanVien())));
			menu.add(createMenuItem("Nhật ký hoạt động", e -> showPanel(new ManHinhNhatKyNhanVien())));
			menu.add(createMenuItem("Thống kê", e -> showPanel(new ManHinhThongKeNhanVien())));
			return menu;
		}
		public JMenu createKhachHangMenu() {
            JMenu menu = createMenu("Khách hàng");
            menu.add(createMenuItem("Tra cứu", e -> showPanel(new TraCuuKhachHang())));
            return menu;
        }
		
		public JMenu createHoaDonMenu() {
			JMenu menu = createMenu("Hóa đơn");
			menu.add(createMenuItem("Tra cứu hóa đơn", e -> showPanel(new TraCuuHoaDon())));
			JMenu thongKe = createMenu("Thống kê");
			thongKe.add(createMenuItem("Theo ngày", e -> showPanel(new ManHinhThongKeHoaDonTheoNgay())));
			thongKe.add(createMenuItem("Theo tháng", e -> showPanel(new ManHinhThongKeHoaDonTheoThang())));
			thongKe.add(createMenuItem("Theo năm", e -> showPanel(new ManHinhThongKeHoaDonTheoNam())));
			menu.add(thongKe);
			return menu;
		}
		
		public JMenu createTaiKhoanMenu() {
        	String gioiTinh = "Nu";
        	String path = gioiTinh.equalsIgnoreCase("Nam") ? "img/man.png" : "img/woman.png";
        	ImageIcon iconUser = new ImageIcon(path);
        	Image imgUser = iconUser.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        	iconUser = new ImageIcon(imgUser);
        	
        	NhanVien nv= Session.getNhanVienDangNhap();
        	
        	String ten= nv.getHoTen();
        	
            JMenu menu = createMenu("Xin chào, "+ ten) ; //test
            menu.setIcon(iconUser);
            menu.add(createMenuItem("Thông tin cá nhân", e -> showPanel(new ThongTinCaNhan())));
            menu.add(createMenuItem("Đổi mật khẩu", e -> showPanel(new DoiMatKhau())));
            menu.add(createMenuItem("Đăng xuất", e -> {
            	// Xóa thông tin session
                util.Session.logout();
            	JFrame topFrame= (JFrame) SwingUtilities.getWindowAncestor(mainPanelRef);
            	if(topFrame !=null) {
            		topFrame.dispose();  // đóng màn hình chính
            	}
            	new DangNhap().setVisible(true);
            }));
            return menu;
        }

	}
}