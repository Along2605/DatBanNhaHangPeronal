package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.BanAnDAO;
import dao.KhuVucDAO;
import dao.LoaiBanDAO;
import entity.BanAn;
import entity.KhuVuc;
import entity.LoaiBan;

public class ThemBanAn extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private JScrollPane scroll;
	private JButton btnXoa;
	private JTextField txtMaBA;
	private JTextField txtSoCho;
	private JButton btnLuu;
	private JComboBox<LoaiBan> cbLoaiBA;
	private JComboBox<String> cbTrangThaiBan;
	private JTextArea txaGhiChu;
	private JComboBox<String> cbKhuVuc;
	private JButton btnHuy;
	private DefaultTableModel modelBanAn;
	private JTable tableBanAn;

	private BanAnDAO ban_dao;
	private KhuVucDAO kv_dao;
	private LoaiBanDAO loaiBan_dao;
	private JTextField txtTenBan;
	private JButton btnTim;
	private JTextField txtTim;
	private JButton btnThem;
	private JButton btnSua;

	public ThemBanAn() {
		ConnectDB.getInstance().connect();
		ban_dao = new BanAnDAO();
		kv_dao = new KhuVucDAO();
		loaiBan_dao = new LoaiBanDAO();
		///////////
		setLayout(new BorderLayout());
		setBackground(Color.decode("#EAF1F9"));

		Box boxTop = Box.createVerticalBox();

		// Tiêu đề
		JLabel lblTitle = new JLabel("QUẢN LÝ BÀN ĂN");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		boxTop.add(Box.createVerticalStrut(10));
		boxTop.add(lblTitle);
		boxTop.add(Box.createVerticalStrut(10));
		add(boxTop, BorderLayout.NORTH);

		JPanel pnForm = new JPanel();
		pnForm.setLayout(new GridBagLayout());
		boxTop.add(pnForm);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 15, 10, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Hàng 1
		gbc.gridy = 0;

		gbc.gridx = 0;
		pnForm.add(new JLabel("Mã bàn "), gbc);
		gbc.gridx = 1;
		txtMaBA = new JTextField(15);
		txtMaBA.setText(ban_dao.generateMaBanMoi());
		txtMaBA.setEditable(false);
		pnForm.add(txtMaBA, gbc);

		gbc.gridx = 2;
		gbc.gridx = 2;
		pnForm.add(new JLabel("Tên bàn "), gbc);
		gbc.gridx = 3;
		txtTenBan = new JTextField(15);
		txtTenBan.requestFocus();
		pnForm.add(txtTenBan, gbc);

		// hàng 2
		gbc.gridy = 1;
		gbc.gridx = 0;
		pnForm.add(new JLabel("Loại bàn"), gbc);
		gbc.gridx = 1;
		cbLoaiBA = new JComboBox<LoaiBan>();
		// Tạo dòng nhắc
		LoaiBan placeholder = new LoaiBan(null, "--Vui lòng chọn loại bàn --");
		cbLoaiBA.addItem(placeholder);
		// Đổ dữ liệu cào
		List<LoaiBan> listLoaiBan = loaiBan_dao.getAllLoaiBan();
		for (LoaiBan loaiBan : listLoaiBan) {
			cbLoaiBA.addItem(loaiBan);
		}
		
		pnForm.add(cbLoaiBA, gbc);

		gbc.gridx = 2;
		pnForm.add(new JLabel("Số lượng chỗ ngồi "), gbc);
		gbc.gridx = 3;
		txtSoCho = new JTextField(15);
		pnForm.add(txtSoCho, gbc);

		// Hàng 3
		gbc.gridy = 2;
		gbc.gridx = 0;
		pnForm.add(new JLabel("Khu vực"), gbc);
		gbc.gridx = 1;
		cbKhuVuc = new JComboBox<>(new String[] { "--Chọn khu vực--" });
		// Đổ dữ liệu cào
		List<KhuVuc> listKhuVuc = kv_dao.getAll();
		for (KhuVuc khuVuc : listKhuVuc) {
			cbKhuVuc.addItem(khuVuc.getTenKhuVuc());
		}
		pnForm.add(cbKhuVuc, gbc);

		gbc.gridx = 2;
		pnForm.add(new JLabel("Trạng thái "), gbc);
		gbc.gridx = 3;
		cbTrangThaiBan = new JComboBox<>(new String[] { "--Chọn trạng thái--", "Trống", "Bảo trì" });
		pnForm.add(cbTrangThaiBan, gbc);

		// Hàng 4
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		pnForm.add(new JLabel("Ghi chú:"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		txaGhiChu = new JTextArea(3, 15);
		txaGhiChu.setLineWrap(true);
		txaGhiChu.setWrapStyleWord(true);
		JScrollPane scrollGhiChu = new JScrollPane(txaGhiChu);
		pnForm.add(scrollGhiChu, gbc);

		// hàng 5:
		gbc.gridy = 4;
		gbc.gridwidth = 1;

		// Nút Thêm
		gbc.gridx = 0;
		ImageIcon iconThem = new ImageIcon("img/add.png");
		Image imgThem = iconThem.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		btnThem = new JButton("Thêm", new ImageIcon(imgThem));
		btnThem.setPreferredSize(new Dimension(120, 35));
		btnThem.setBackground(new Color(46, 204, 113));
		btnThem.setForeground(Color.WHITE);
		pnForm.add(btnThem, gbc);

		// Nút Sửa
		gbc.gridx = 1;
		ImageIcon iconSua = new ImageIcon("img/edit.png");
		Image imgSua = iconSua.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		btnSua = new JButton("Sửa", new ImageIcon(imgSua));
		btnSua.setPreferredSize(new Dimension(120, 35));
		btnSua.setBackground(new Color(255, 204, 0));
		btnSua.setForeground(Color.WHITE);
		pnForm.add(btnSua, gbc);

		// Nút Xóa
		gbc.gridx = 2;
		ImageIcon iconXoa = new ImageIcon("img/delete.png");
		Image imgXoa = iconXoa.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		btnXoa = new JButton("Xóa", new ImageIcon(imgXoa));
		btnXoa.setPreferredSize(new Dimension(120, 35));
		btnXoa.setBackground(new Color(231, 76, 60));
		btnXoa.setForeground(Color.WHITE);
		pnForm.add(btnXoa, gbc);

		// Nút Hủy
		gbc.gridx = 3;
		ImageIcon iconHuy = new ImageIcon("img/refresh.png");
		Image imgHuy = iconHuy.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		btnHuy = new JButton("Hủy", new ImageIcon(imgHuy));
		btnHuy.setPreferredSize(new Dimension(120, 35));
		btnHuy.setBackground(new Color(52, 152, 219));
		btnHuy.setForeground(Color.WHITE);
		pnForm.add(btnHuy, gbc);

		// Danh sách khách hàng
		String[] tieuDeBanAn = { "STT", "Mã bàn", "Tên bàn ăn", "Loại bàn", "Số lượng chỗ ngồi", "Khu vực",
				"Trạng thái", "Ghi chú" };
		modelBanAn = new DefaultTableModel(tieuDeBanAn, 0);
		tableBanAn = new JTable(modelBanAn);
		scroll = new JScrollPane(tableBanAn);
		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

		add(scroll, BorderLayout.CENTER);

		// Tim
		JPanel pnTim = new JPanel();
		add(pnTim, BorderLayout.SOUTH);
		JLabel lblTim = new JLabel("Nhập tên bàn: ");
		txtTim = new JTextField(15);
		btnTim = new JButton("Tìm");
		pnTim.add(lblTim);
		pnTim.add(txtTim);
		pnTim.add(btnTim);
		// Load dữ liêu
		loadDuLieuVaoBang();
		//

		// Sự kiện
		btnHuy.addActionListener(this);
		btnXoa.addActionListener(this);
		btnThem.addActionListener(this);
		btnSua.addActionListener(this);
		btnTim.addActionListener(this);
		tableBanAn.addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem)) {
			them();
		}
		if (o.equals(btnHuy)) {
			huy();
		}
		if (o.equals(btnSua)) {
			sua();
		}
		if (o.equals(btnXoa)) {
			xoa();
		}
		if (o.equals(btnTim)) {
			timTheoTen();
		}
	}

	private void loadDuLieuVaoBang() {
	    modelBanAn.setRowCount(0);
	    List<BanAn> listBan = ban_dao.getAllBanAn();
	    int stt = 1;
	    for (BanAn ban : listBan) {
	        String tenLoaiBan = (ban.getLoaiBan() != null && ban.getLoaiBan().getTenLoaiBan() != null)
	                ? ban.getLoaiBan().getTenLoaiBan()
	                : "Không xác định";

	        String tenKhuVuc = (ban.getKhuVuc() != null && ban.getKhuVuc().getTenKhuVuc() != null)
	                ? ban.getKhuVuc().getTenKhuVuc()
	                : "Không xác định";

	        modelBanAn.addRow(new Object[] {
	            stt++,
	            ban.getMaBan(),
	            ban.getTenBan(),
	            tenLoaiBan,
	            ban.getSoLuongCho(),
	            tenKhuVuc,
	            ban.getTrangThai(),
	            ban.getGhiChu()
	        });
	    }
	}


	private boolean validdata() {
		String ten = txtTenBan.getText().trim();
		String soLuongCho = txtSoCho.getText().trim();

		if (!(ten.length() > 0 && ten.matches("^[A-Za-zÀ-Ỹà-ỹ0-9 ]{2,50}$"))) {
			JOptionPane.showMessageDialog(this, "Lỗi: Tên bàn nhập sai định dạng! \n Ví dụ: Bàn 01, Bàn gia đình...");
			return false;
		}
		int soLuong;
		try {
			soLuong = Integer.parseInt(txtSoCho.getText().trim());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Lỗi: Số lượng chỗ ngồi phải là số nguyên!");
			return false;
		}
		if (soLuong < 1 || soLuong > 10) {
			JOptionPane.showMessageDialog(this, "Error: Số lượng từ 1 đến 10 chỗ ngồi!");
			return false;
		}
		if (cbKhuVuc.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng chọn khu vực!");
			return false;
		}
		if (cbLoaiBA.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Error: Vui lòng chọn loại bàn!");
			return false;
		}
		if (cbTrangThaiBan.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Error: Vui lòng chọn trạng thái!");
			return false;
		}
		return true;
	}

	private void them() {
		if (!validdata())
			return;

		String ma = txtMaBA.getText().trim();
		String ten = txtTenBan.getText().trim();
		int soCho = Integer.parseInt(txtSoCho.getText().trim());
		String trangThai = cbTrangThaiBan.getSelectedItem().toString();
		String ghiChu = txaGhiChu.getText().trim();

		// Lấy mã từ tên
		LoaiBan loaiBan = (LoaiBan) cbLoaiBA.getSelectedItem();
		String maKhuVuc = kv_dao.layMaTheoTen(cbKhuVuc.getSelectedItem().toString());

		
		KhuVuc khuVuc = new KhuVuc(maKhuVuc, cbKhuVuc.getSelectedItem().toString(), null);

		BanAn ban = new BanAn(ma, ten, soCho, loaiBan, trangThai, khuVuc, ghiChu);

		try {
			if (ban_dao.themBan(ban)) {
				JOptionPane.showMessageDialog(this, "Thêm bàn ăn thành công!");
				loadDuLieuVaoBang();
				huy();
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi: Thêm bàn thất bại!");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi: Thêm bàn thất bại!");
		}
	}

	private void huy() {
		txtMaBA.setText(ban_dao.generateMaBanMoi());
		cbLoaiBA.setSelectedIndex(0);
		txtTenBan.setText("");
		txtSoCho.setText("");
		cbKhuVuc.setSelectedIndex(0);
		cbTrangThaiBan.setSelectedIndex(0);
		txaGhiChu.setText("");
		txtTenBan.requestFocus();
		loadDuLieuVaoBang();
		
	}

	private void sua() {
	    int row = tableBanAn.getSelectedRow();
	    if (row < 0) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn ăn để sửa!");
	        return;
	    }
	    String ma = txtMaBA.getText().trim();
	    String ten = txtTenBan.getText().trim();
	    String soChoText = txtSoCho.getText().trim();
	    int soCho;
	    try {
	        soCho = Integer.parseInt(soChoText);
	        if (soCho <= 0) {
	            JOptionPane.showMessageDialog(this, "Số lượng chỗ phải lớn hơn 0!");
	            return;
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Số lượng chỗ phải là số nguyên!");
	        return;
	    }
	    String trangThai = cbTrangThaiBan.getSelectedItem().toString();
	    String ghiChu = txaGhiChu.getText().trim();
	    LoaiBan loaiBan = (LoaiBan) cbLoaiBA.getSelectedItem();
	    String maKhuVuc = kv_dao.layMaTheoTen(cbKhuVuc.getSelectedItem().toString());
	    
	    KhuVuc khuVuc = new KhuVuc(maKhuVuc, cbKhuVuc.getSelectedItem().toString(), null);

	    BanAn banAn = new BanAn(ma, ten, soCho, loaiBan, trangThai, khuVuc, ghiChu);
	    if (ban_dao.suaThongTinBan(banAn)) {
	        modelBanAn.setValueAt(ma, row, 1);
	        modelBanAn.setValueAt(ten, row, 2);
	        modelBanAn.setValueAt(loaiBan.getTenLoaiBan(), row, 3);
	        modelBanAn.setValueAt(soCho, row, 4);
	        modelBanAn.setValueAt(cbKhuVuc.getSelectedItem(), row, 5);
	        modelBanAn.setValueAt(trangThai, row, 6);
	        modelBanAn.setValueAt(ghiChu, row, 7);

	        JOptionPane.showMessageDialog(this, "Sửa bàn ăn thành công!");
	    } else {
	        JOptionPane.showMessageDialog(this, "Sửa bàn ăn thất bại!");
	    }
	}


	private void xoa() {
		int row = tableBanAn.getSelectedRow();
		if (row >= 0) {
			String ma = tableBanAn.getValueAt(row, 1).toString();
			int chon = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa bàn ăn này không?", "Xóa",
					JOptionPane.YES_NO_OPTION);
			if (chon == JOptionPane.YES_OPTION) {
				if (ban_dao.xoaBan(ma)) {
					JOptionPane.showMessageDialog(this, "Xóa bàn ăn thành công!");
					huy();
					loadDuLieuVaoBang();
				} else {
					JOptionPane.showMessageDialog(this, "Xóa bàn ăn thất bại!");
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn ăn để xóa!");
		}
	}

	// Tìm theo mã
	private void timTheoTen() {
		String ten = txtTim.getText().trim();
		List<BanAn> dsTim = new ArrayList<>();
		if (ten.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tên bàn muốn tìm");
		} else {
			dsTim = ban_dao.getBanTheoTen(ten);
			capNhatBang(dsTim);
			if (dsTim.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy bàn ăn có tên: " + ten);
				return;
			}
		}
		
	}

	// Cập nhật lại bảng
	private void capNhatBang(List<BanAn> dsBan) {
		modelBanAn.setRowCount(0); // Xóa tất cả các hàng hiện có trong bảng
		int stt = 1;
		for (BanAn ban : dsBan) {
			modelBanAn.addRow(new Object[] { stt++, ban.getMaBan(), ban.getTenBan(), ban.getLoaiBan().getTenLoaiBan(),
					ban.getSoLuongCho(), ban.getKhuVuc().getTenKhuVuc(), ban.getTrangThai(), ban.getGhiChu() });
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tableBanAn.getSelectedRow();
		txtMaBA.setText(modelBanAn.getValueAt(row, 1).toString());
		txtTenBan.setText(modelBanAn.getValueAt(row, 2).toString());
		String tenLoaiBan = modelBanAn.getValueAt(row, 3).toString();
		LoaiBan loaiBan = loaiBan_dao.layLoaiBanTheoTen(tenLoaiBan);
	    cbLoaiBA.setSelectedItem(loaiBan);
		txtSoCho.setText(modelBanAn.getValueAt(row, 4).toString());
		cbKhuVuc.setSelectedItem(modelBanAn.getValueAt(row, 5).toString());
		cbTrangThaiBan.setSelectedItem(modelBanAn.getValueAt(row, 6).toString());
		txaGhiChu.setText(modelBanAn.getValueAt(row, 7).toString());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Tạo JFrame
			JFrame frame = new JFrame("Quản lý Bàn Ăn");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 600); 
			frame.setLocationRelativeTo(null); 

			// Tạo JPanel ThemBanAn
			ThemBanAn panel = new ThemBanAn();
			frame.setContentPane(panel); 
			frame.setVisible(true); 
		});
	}
}
