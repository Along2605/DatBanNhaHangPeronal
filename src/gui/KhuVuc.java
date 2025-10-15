package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class KhuVuc extends JPanel {
	/**
	 * */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cbLoaiBan;
	private JComboBox<String> cbViTri;
	private JComboBox<String> cbTrangThai;
	private JTextField txtTim;
	private JButton btnTim;

	public KhuVuc() {
		setLayout(new BorderLayout());

		// Tiêu đề và bộ lọc
		JPanel pnTop = new JPanel();
		add(pnTop, BorderLayout.NORTH);
		pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));

		JLabel lblTitle = new JLabel("ĐẶT BÀN", JLabel.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		pnTop.add(lblTitle, BorderLayout.NORTH);

		JPanel pnLoc = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		pnTop.add(pnLoc, BorderLayout.CENTER);

		JLabel lblLoaiBan = new JLabel("Loại bàn:");
		cbLoaiBan = new JComboBox<>(new String[] { "Vip", "Thường", "Phòng riêng" }); // test
		pnLoc.add(lblLoaiBan);
		pnLoc.add(cbLoaiBan);

		String[] khuVuc = { "Tầng 1", "Tầng 2", "Sân thượng" };
		JLabel lblViTri = new JLabel("Vị trí:");
		cbViTri = new JComboBox<>(khuVuc);
		pnLoc.add(lblViTri);
		pnLoc.add(cbViTri);

		JLabel lblTrangThai = new JLabel("Trạng thái:");
		cbTrangThai = new JComboBox<>(new String[] { "Đã đặt", "Trống", "Đang vệ sinh" });
		pnLoc.add(lblTrangThai);
		pnLoc.add(cbTrangThai);

		JLabel lblTim = new JLabel("Nhập thông tin: ");
		txtTim = new JTextField(10);
		btnTim = new JButton("Tìm");
		pnLoc.add(lblTim);
		pnLoc.add(txtTim);
		pnLoc.add(btnTim);

		// Danh sách bàn
		JPanel pnDanhSachBan = new JPanel();
		pnDanhSachBan.setLayout(new BoxLayout(pnDanhSachBan, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(pnDanhSachBan);
		add(scroll, BorderLayout.CENTER);
		int[][] soBan = { { 6 }, { 4 }, { 2 } }; // Số bàn từng khu

		for (int k = 0; k < khuVuc.length; k++) {
			JPanel pnKhuVuc = new JPanel(new GridLayout(0, 3, 10, 10));
			pnKhuVuc.setBorder(BorderFactory.createTitledBorder(khuVuc[k])); // Tên khu vực + đường gạch

			for (int i = 1; i <= soBan[k][0]; i++) {
				pnKhuVuc.add(taoBan(khuVuc[k], "Bàn " + i, "Đã đặt", "VIP"));
			}

			pnDanhSachBan.add(pnKhuVuc);
			pnDanhSachBan.add(Box.createRigidArea(new Dimension(0, 10)));
		}

	}

	private JPanel taoBan(String viTri, String maBan, String trangThai, String loaiBan) {
		JPanel pnBan = new JPanel(new BorderLayout());
		pnBan.setBorder(new LineBorder(Color.BLACK, 2));

		// Dòng trên: tên bàn + trạng thái
		JLabel lblTenBan = new JLabel(maBan + " - " + viTri, JLabel.LEFT);
		lblTenBan.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel lblTrangThai = new JLabel(trangThai, JLabel.RIGHT);
		lblTrangThai.setFont(new Font("Arial", Font.PLAIN, 14));

		JPanel pnTop = new JPanel(new BorderLayout());
		pnTop.add(lblTenBan, BorderLayout.WEST);
		pnTop.add(lblTrangThai, BorderLayout.EAST);
		pnBan.add(pnTop, BorderLayout.NORTH);

		// Nội dung chính
		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
		pnInfo.setPreferredSize(new Dimension(250, 80));

		Color color = null;
		switch (trangThai) {
			case "Trống" -> {
				color = Color.RED;
				setIcon(pnInfo, "img/ban_trong.png");
			}
			case "Đang phục vụ" -> {
				color = Color.GREEN;
				setIcon(pnInfo, "img/co_khach.png");
			}
			case "Đã đặt" -> {
				color = Color.BLUE;
				setInfo(pnInfo, "Nguyễn Văn A", "0147258369", "19:30 - 12/10/2025");
			}
			case "Bảo trì" -> {
				color = Color.YELLOW;
				setIcon(pnInfo, "img/edit.png");
			}
			case "Đang dọn dẹp" -> {
				pnBan.setBackground(new Color(255, 240, 200));
				setInfo(pnInfo, "Trần Thị B", "0987654321", "20:00 - 15/10/2025");
			}
			default -> {
				pnBan.setBackground(Color.LIGHT_GRAY);
				setIcon(pnInfo, "icon_unknown.png");
			}
		}
		pnBan.setBackground(color);
		pnTop.setBackground(color);
		pnInfo.setBackground(color);
		
		
		// Tạo panel riêng cho loại bàn ở cuối
		JPanel pnLoai = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel lblLoai = new JLabel("Loại bàn: " + loaiBan);
		lblLoai.setFont(new Font("Arial", Font.BOLD, 13));
		pnLoai.add(lblLoai);
		pnInfo.add(pnLoai);
		pnBan.add(pnInfo, BorderLayout.CENTER);
		pnBan.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				switch (trangThai) {
				case "Trống" -> DialogPhucVu(maBan);
				case "Đang phục vụ" -> JOptionPane.showMessageDialog(pnBan, maBan + " đang phục vụ khách.");
				case "Đã đặt", "Đã đặt trước" -> DialogPhucVu(maBan); // mở dialog hóa đơn
				case "Bảo trì" -> JOptionPane.showMessageDialog(pnBan, maBan + " đang bảo trì.");
				default -> JOptionPane.showMessageDialog(pnBan, maBan + " trạng thái không xác định.");
				}
			}
		});

		pnLoai.setBackground(color);
		return pnBan;
	}

	// Hiển thị thông tin khách đặt
	private void setInfo(JPanel panel, String ten, String sdt, String time) {
		panel.add(new JLabel("Người đặt: " + ten));
		panel.add(new JLabel("SĐT: " + sdt));
		panel.add(new JLabel("Thời gian: " + time));
	}

	// Hiển thị icon cho trạng thái
	private void setIcon(JPanel panel, String iconPath) {
		ImageIcon iconTim = new ImageIcon(iconPath);
		Image imgTim = iconTim.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JLabel lblIcon = new JLabel(new ImageIcon(imgTim));
		lblIcon.setPreferredSize(new Dimension(100, 100));
		panel.add(lblIcon);
	}

	// Bàn đang phục vụ
	private void DialogPhucVu(String maBan) {
		// Tạo dialog modal
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Hóa đơn - " + maBan, true);
		dialog.setLayout(new BorderLayout());
		dialog.setSize(400, 500);
		dialog.setLocationRelativeTo(this);

		// Các món ăn
		List<String> dsMonAn = new ArrayList<>(); // test
		dsMonAn.add("Phở bò x2 = 100.000đ");
		dsMonAn.add("Bún chả x1 = 40.000đ");
		dsMonAn.add("Nước ngọt x3 = 30.000đ");

		// Khách hàng
		JPanel pnThongTin = new JPanel(new GridLayout(3, 2, 10, 5));
		dialog.add(pnThongTin, BorderLayout.NORTH);

		pnThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
		pnThongTin.setPreferredSize(new Dimension(400, 100));
		pnThongTin.add(new JLabel("Tên khách hàng:"));
		pnThongTin.add(new JLabel("Nguyễn Văn A")); // test

		pnThongTin.add(new JLabel("Số điện thoại:"));
		pnThongTin.add(new JLabel("0147258963")); // test

		pnThongTin.add(new JLabel("Giờ đặt: "));
		pnThongTin.add(new JLabel("19:20 - 22/10/2025")); // test

		// Món ăn
		String[] col = { "STT", "Món ăn", "Số lượng", "Thành tiền" };
		Object[][] data = { { 1, "Phở bò", 2, "100.000" }, { 2, "Bún chả", 1, "40.000" },
				{ 3, "Nước ngọt", 3, "30.000" } }; // test

		JTable tableMonAn = new JTable(data, col);
		tableMonAn.setFont(new Font("Arial", Font.PLAIN, 14));
		tableMonAn.setRowHeight(25);
		tableMonAn.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

		JScrollPane scroll = new JScrollPane(tableMonAn);
		dialog.add(scroll, BorderLayout.CENTER);

		// Thành tiền
		JPanel pnTong = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTong = new JLabel("Tổng tiền: 170.000đ");
		lblTong.setFont(new Font("Arial", Font.BOLD, 16));
		pnTong.add(lblTong);

		// Nút Thanh toán
		JButton btnThanhToan = new JButton("Thanh toán");
		JButton btnThemMonAn = new JButton("Thêm món ăn");

		// Xử lý thanh toán
		btnThanhToan.addActionListener(e -> {
			JOptionPane.showMessageDialog(dialog, "Thanh toán thành công cho " + maBan + "!");
			dialog.dispose();
		});
		// Xử lý thêm món ăn
		btnThemMonAn.addActionListener(e -> {
			// Trang đặt món
		});
		JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnButton.add(btnThemMonAn);
		pnButton.add(btnThanhToan);
		dialog.add(pnButton, BorderLayout.SOUTH);

		dialog.setVisible(true);
	}

}