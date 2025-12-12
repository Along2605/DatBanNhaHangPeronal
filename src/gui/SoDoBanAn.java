package gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Timer;

import connectDB.ConnectDB;
import dao.BanAnDAO;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhuVucDAO;
import dao.LoaiBanDAO;
import dao.PhieuDatBanDAO;
import entity.BanAn;
import entity.LoaiBan;
import entity.PhieuDatBan;

public class SoDoBanAn extends JPanel {

	private JComboBox<String> cbLoaiBan;
	private JComboBox<String> cbViTri;
	private JComboBox<String> cbTrangThai;

	private BanAnDAO ban_DAO;
	private KhuVucDAO khu_DAO;
	private LoaiBanDAO loaiBan_DAO;
	private ChiTietHoaDonDAO ctHD_DAO;
	private HoaDonDAO hoaDon_DAO;
	private BanAnCallback chuyenBanCallback;
	private JPanel pnDanhSachBan; // panel chính chứa sơ đồ bàn
	private BanAn banCanChuyen = null;
	private JLabel lblTitle;
	private JTextField txtTim;
	private JButton btnTim;
	private JButton btnLamMoi;
	private boolean isChuyenMode = true;
	private PhieuDatBanDAO phieuDat_DAO;

	public SoDoBanAn() {
		try {
			ConnectDB.getInstance().connect();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		ban_DAO = new BanAnDAO();
		loaiBan_DAO = new LoaiBanDAO();
		khu_DAO = new KhuVucDAO();
		hoaDon_DAO = new HoaDonDAO();
		ctHD_DAO = new ChiTietHoaDonDAO();
		phieuDat_DAO = new PhieuDatBanDAO();

		setLayout(new BorderLayout());

		initTopPanel();
		initDanhSachBan();
	}

	private void initTopPanel() {
		JPanel pnTop = new JPanel();
		pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
		add(pnTop, BorderLayout.NORTH);

		lblTitle = new JLabel("SƠ ĐỒ BÀN ĂN", JLabel.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		pnTop.add(lblTitle);

		JPanel pnLoc = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		pnTop.add(pnLoc);

		// Loại bàn
		List<LoaiBan> dsLoaiBan = loaiBan_DAO.getAllLoaiBan();
		LinkedHashSet<String> tenLoaiBanSet = new LinkedHashSet<>();
		tenLoaiBanSet.add("--Tất cả--");
		for (LoaiBan loai : dsLoaiBan) {
			tenLoaiBanSet.add(loai.getTenLoaiBan());
		}
		cbLoaiBan = new JComboBox<>(tenLoaiBanSet.toArray(new String[0]));
		pnLoc.add(new JLabel("Loại bàn:"));
		pnLoc.add(cbLoaiBan);

		// Vị trí
		ArrayList<String> dsKhuVuc = khu_DAO.layViTri();
		List<String> dsKhuVucArr = new ArrayList<>();
		dsKhuVucArr.add("--Tất cả--");
		dsKhuVucArr.addAll(new LinkedHashSet<>(dsKhuVuc));
		cbViTri = new JComboBox<>(dsKhuVucArr.toArray(new String[0]));
		pnLoc.add(new JLabel("Vị trí:"));
		pnLoc.add(cbViTri);

		// Trạng thái
		List<BanAn> dsBan = ban_DAO.getAllBanAn();
		LinkedHashSet<String> setStatus = new LinkedHashSet<>();
		setStatus.add("--Tất cả--");
		for (BanAn b : dsBan) {
			setStatus.add(b.getTrangThai());
		}
		cbTrangThai = new JComboBox<>(setStatus.toArray(new String[0]));
		pnLoc.add(new JLabel("Trạng thái:"));
		pnLoc.add(cbTrangThai);

		// Text nhập
		JLabel lblTim = new JLabel("Nhập");
		txtTim = new JTextField();
		txtTim.setPreferredSize(new Dimension(140, 25));
		btnTim = createButton("Tìm kiếm", "img/search.png");
		btnLamMoi = createButton("Làm mới", "img/refresh.png");
		pnLoc.add(new JLabel("Nhập"));
		pnLoc.add(txtTim);
		pnLoc.add(btnTim);
		pnLoc.add(btnLamMoi);

		btnTim.addActionListener(e -> {
			String loaiBan = (String) cbLoaiBan.getSelectedItem();
			String viTri = (String) cbViTri.getSelectedItem();
			String trangThai = (String) cbTrangThai.getSelectedItem();
			String tim = txtTim.getText().trim();

			List<BanAn> dsBanAn = ban_DAO.getAllBanAn();
			List<BanAn> ketQua = new ArrayList<BanAn>();

			for (BanAn b : dsBanAn) {
				boolean ok = true;
				if (!"--Tất cả--".equals(loaiBan) && !b.getLoaiBan().getTenLoaiBan().equals(loaiBan)) {
					ok = false;
				}
				if (!"--Tất cả--".equals(viTri) && !b.getKhuVuc().getViTri().equals(viTri)) {
					ok = false;
				}
				if (!"--Tất cả--".equals(trangThai) && !b.getTrangThai().equals(trangThai)) {
					ok = false;
				}
				if (!tim.isEmpty()
						&& !(b.getMaBan().toLowerCase().contains(tim) || b.getTenBan().toLowerCase().contains(tim))) {
					ok = false;
				}
				if (ok)
					ketQua.add(b);
			}

			pnDanhSachBan.removeAll();
			Map<String, List<BanAn>> kVBanAn = new LinkedHashMap<>();
			for (BanAn b : ketQua) {
				kVBanAn.computeIfAbsent(b.getKhuVuc().getViTri(), k -> new ArrayList<>()).add(b);
			}

			List<String> sortedKeys = new ArrayList<>(kVBanAn.keySet());
			sortedKeys.sort((a, b1) -> {
				try {
					int numA = Integer.parseInt(a.replaceAll("\\D+", ""));
					int numB = Integer.parseInt(b1.replaceAll("\\D+", ""));
					return Integer.compare(numA, numB);
				} catch (Exception ex) {
					return a.compareTo(b1);
				}
			});

			for (String viTriKey : sortedKeys) {
				JPanel pnViTri = new JPanel(new GridLayout(0, 3, 10, 10));
				pnViTri.setBorder(BorderFactory.createTitledBorder(viTriKey));
				for (BanAn b : kVBanAn.get(viTriKey)) {
					pnViTri.add(taoBan(b.getKhuVuc().getTenKhuVuc(), b.getTenBan(), b.getTrangThai(),
							b.getLoaiBan().getTenLoaiBan(), b));
				}
				pnDanhSachBan.add(pnViTri);
				pnDanhSachBan.add(Box.createRigidArea(new Dimension(0, 10)));
			}

			pnDanhSachBan.revalidate();
			pnDanhSachBan.repaint();
		});

		btnLamMoi.addActionListener(e -> {
			cbLoaiBan.setSelectedIndex(0);
			cbTrangThai.setSelectedIndex(0);
			cbViTri.setSelectedIndex(0);
			txtTim.setText("");
			loadBanAn();
		});
	}

	private void initDanhSachBan() {
		pnDanhSachBan = new JPanel();
		pnDanhSachBan.setLayout(new BoxLayout(pnDanhSachBan, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(pnDanhSachBan);
		add(scroll, BorderLayout.CENTER);

		loadBanAn();
	}

	public void loadBanAn() {
		pnDanhSachBan.removeAll();

		List<BanAn> dsBan = ban_DAO.getAllBanAn();
		Map<String, List<BanAn>> kVBanAn = new LinkedHashMap<>();
		//Thông báo bàn đã đặt
		kiemTraBan();
		// CHỉ load trạng thái đã đặt trong 30 phút tới
		LocalDateTime now = LocalDateTime.now();
		for (BanAn b : dsBan) {
			if ("Đã đặt".equals((b.getTrangThai()))) {
				PhieuDatBan phieuDat = phieuDat_DAO.getPhieuDatTheoBan(b.getMaBan());

				if (phieuDat == null) {
					continue;
				}
				LocalDateTime ngayDat = phieuDat.getNgayDat();

				// Nếu thời gian đặt sau 45 phút so với hiện tại thì không khóa bàn
				if (ngayDat.isAfter(now.plusMinutes(45))) {
					continue;
				}
			}
			kVBanAn.computeIfAbsent(b.getKhuVuc().getViTri(), k -> new ArrayList<>()).add(b);
		}

		List<String> sortedKeys = new ArrayList<>(kVBanAn.keySet());
		sortedKeys.sort((a, b) -> {
			try {
				int numA = Integer.parseInt(a.replaceAll("\\D+", ""));
				int numB = Integer.parseInt(b.replaceAll("\\D+", ""));
				return Integer.compare(numA, numB);
			} catch (Exception e) {
				return a.compareTo(b);
			}
		});

		for (String viTri : sortedKeys) {
			JPanel pnViTri = new JPanel(new GridLayout(0, 3, 10, 10));
			pnViTri.setBorder(BorderFactory.createTitledBorder(viTri));
			for (BanAn b : kVBanAn.get(viTri)) {
				pnViTri.add(taoBan(b.getKhuVuc().getTenKhuVuc(), b.getTenBan(), b.getTrangThai(),
						b.getLoaiBan().getTenLoaiBan(), b));
			}
			pnDanhSachBan.add(pnViTri);
			pnDanhSachBan.add(Box.createRigidArea(new Dimension(0, 10)));
		}

		pnDanhSachBan.revalidate();
		pnDanhSachBan.repaint();
	}

	private JPanel taoBan(String viTri, String tenBan, String trangThai, String loaiBan, BanAn ban) {
		JPanel pnBan = new JPanel(new BorderLayout());
		Dimension size = new Dimension(200, 160);
		pnBan.setPreferredSize(size);
		pnBan.setMaximumSize(size);
		pnBan.setMinimumSize(size);
		pnBan.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		pnBan.setBackground(Color.WHITE);

		JLabel lblTenBan = new JLabel(tenBan + " • " + viTri);
		lblTenBan.setFont(new Font("Segoe UI", Font.BOLD, 16));
		JLabel lblTrangThai = new JLabel(trangThai);
		lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		JPanel pnTop = new JPanel(new BorderLayout());
		pnTop.setOpaque(false);
		pnTop.add(lblTenBan, BorderLayout.WEST);
		pnTop.add(lblTrangThai, BorderLayout.EAST);

		JLabel lblLoai = new JLabel("Loại: " + loaiBan);
		lblLoai.setFont(new Font("Segoe UI", Font.BOLD, 13));
		JPanel pnBottom = new JPanel();
		pnBottom.setOpaque(false);
		pnBottom.add(lblLoai);

		pnBan.add(pnTop, BorderLayout.NORTH);
		pnBan.add(pnBottom, BorderLayout.SOUTH);

		Color bg = switch (trangThai) {
		case "Trống" -> new Color(255, 240, 240);
		case "Đang phục vụ" -> new Color(235, 255, 235);
		case "Đã đặt" -> new Color(240, 240, 255);
		case "Đang dọn dẹp" -> new Color(255, 250, 230);
		default -> new Color(240, 240, 240);
		};
		pnBan.setBackground(bg);

		if ("Đã đặt".equals(trangThai)) {
			PhieuDatBan phieuDat = phieuDat_DAO.getPhieuDatTheoBan(ban.getMaBan());

			if (phieuDat == null) {
				return pnBan;
			}
			LocalDateTime ngayDat = phieuDat.getNgayDat();
			String ngay = ngayDat.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String gio = ngayDat.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
			if (phieuDat != null) {
				JLabel lblThongTin = new JLabel("<html>" + "<span style='font-family:Segoe UI; font-size:12px;'>"
						+ "<b>Khách:</b> " + phieuDat.getKhachHang().getHoTen() + "<br>" + "<b>Ngày đặt: </b> " + ngay
						+ "<br>" + "<b>Giờ đặt: </b> " + gio + "<br>" + "</span></html>");
				lblThongTin.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

				lblThongTin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				JPanel pnThongTin = new JPanel(new FlowLayout(FlowLayout.CENTER));
				pnThongTin.setOpaque(false);
				pnThongTin.add(lblThongTin);
				pnBan.add(pnThongTin, BorderLayout.CENTER);
			}
		}

		pnBan.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (banCanChuyen != null) {
					// Chuyển đến bàn hiện tại
					if (ban.getMaBan().equals(banCanChuyen.getMaBan())) {
						JOptionPane.showMessageDialog(pnBan, "Bạn đang chọn lại bàn nguồn!\nHãy chọn bàn đích khác.");
						return;
					}
					String maHDCu1 = ban_DAO.layMaHDTuBan(banCanChuyen.getMaBan());
					// Chuyển bàn
					if ("Trống".equals(ban.getTrangThai())) {
						int xacNhan = JOptionPane.showConfirmDialog(pnBan,
								"Chuyển từ bàn " + banCanChuyen.getTenBan() + " sang bàn " + ban.getTenBan() + "?",
								"Xác nhận", JOptionPane.YES_NO_CANCEL_OPTION);
						// Xác nhận chuyển
						if (xacNhan == JOptionPane.YES_OPTION) {
							try {
//								String maHDCu = ban_DAO.layMaHDTuBan(banCanChuyen.getMaBan());
								System.out.println("- Mã hóa đơn cũ: " + maHDCu1);

								if (maHDCu1 == null) {
									JOptionPane.showMessageDialog(pnBan, "Chuyển bàn thấy bại!");
									return;
								}
								boolean updateHD = hoaDon_DAO.chuyenBan(maHDCu1, ban.getMaBan());
								if (!updateHD) {
									JOptionPane.showMessageDialog(null, "Chuyển bàn thất bại!");
									return;
								}
								ban_DAO.capNhatTrangThaiBan(banCanChuyen.getMaBan(), "Trống");
								ban_DAO.capNhatTrangThaiBan(ban.getMaBan(), "Đang sử dụng");

								JOptionPane.showMessageDialog(pnBan, "Chuyển bàn thành công!");
								loadBanAn();

								if (chuyenBanCallback != null) {
									chuyenBanCallback.onBanSelected(banCanChuyen, ban, true);
								}

								banCanChuyen = null;
							} catch (Exception e2) {
								e2.printStackTrace();
								JOptionPane.showMessageDialog(pnBan, "Chuyển bàn thất bại!");
							}
						}
					}
					// Gộp bàn
					else if ("Đang sử dụng".equals(ban.getTrangThai())) {

						String maBanCu = banCanChuyen.getMaBan();
						String maBanMoi = ban.getMaBan();

						String maHDCu = ban_DAO.layMaHDTuBan(maBanCu);
						String maHDDich = ban_DAO.layMaHDTuBan(maBanMoi);

						if (maHDCu == null) {
							JOptionPane.showMessageDialog(null, "Bàn nguồn không có hóa đơn nên không thể gộp!");
							banCanChuyen = null;
							return;
						}

						try {
							if (maHDDich == null) {
								JOptionPane.showMessageDialog(null,
										"Bàn " + ban.getTenBan() + " chưa có hóa đơn nên không thể gộp bàn!");
								return;
							}

							boolean gopHD = ctHD_DAO.gopHoaDon(maHDCu, maHDDich, maBanCu, maBanMoi);
							if (!gopHD) {
								JOptionPane.showMessageDialog(null, "Không thể xóa hóa đơn nguồn!");
								return;
							}

							ban_DAO.capNhatTrangThaiBan(maBanCu, "Trống");
							JOptionPane.showMessageDialog(null, "Gộp bàn thành công!");
							loadBanAn();
							banCanChuyen = null;

						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Gộp bàn thất bại!");
						}
					}
				}
				// Xem bàn chi tiết
				else {
					DialogChiTietBanAn dialog = new DialogChiTietBanAn((Frame) SwingUtilities.getWindowAncestor(pnBan),
							ban, ((banNguon, banDich, sussecces) -> {
								banCanChuyen = banNguon;
								isChuyenMode = sussecces;
								loadBanAn();
							})

					);
					dialog.setVisible(true);
				}
			}
		});

		return pnBan;
	}

	private JButton createButton(String text, String iconPath) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setPreferredSize(new Dimension(120, 30));
		button.setBackground(Color.decode("#4CAF50"));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		if (iconPath != null) {
			try {
				ImageIcon icon = new ImageIcon(iconPath);
				Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
				button.setIcon(new ImageIcon(img));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return button;
	}

	private void kiemTraBan() {
		LocalDateTime now = LocalDateTime.now();
		List<BanAn> dsBan = ban_DAO.getAllBanAn();
		for(BanAn ban : dsBan) {
			if("Đang sử dụng".equals(ban.getTrangThai())) {
				PhieuDatBan phieuDat = phieuDat_DAO.getPhieuDatTheoBan(ban.getMaBan());
				if(phieuDat == null) return;
				
				LocalDateTime ngayDat = phieuDat.getNgayDat();
				if(ngayDat.isBefore(now.plusMinutes(15))) {
					JOptionPane.showMessageDialog(null,
					        "Bàn " + ban.getTenBan() + " sắp được đặt lúc " + ngayDat.format(DateTimeFormatter.ofPattern("HH:mm")) +
					        ". Vui lòng thông báo khách chuyển bàn!",
					        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
				}
			}
			if("Đang dọn dẹp".equals(ban.getTrangThai())) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						ban_DAO.capNhatTrangThaiBan(ban.getMaBan(), "Trống");
						System.out.println("Bàn " + ban.getTenBan() + "đâ chuyển sang trạng thái trống!");
					}
				}, 1 * 60 * 100);
			}
		}
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Sơ đồ bàn ăn");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1200, 700);
			frame.setLocationRelativeTo(null);
			frame.add(new SoDoBanAn());
			frame.setVisible(true);
		});
	}
}
