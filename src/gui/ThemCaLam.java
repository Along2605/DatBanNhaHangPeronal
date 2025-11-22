package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.CaLamViecDAO;
import entity.BanAn;
import entity.CaLamViec;

public class ThemCaLam extends JPanel implements ActionListener, MouseListener {
	
	private boolean isResetting = false;
	
	private static final long serialVersionUID = 1L;
	private JTextField txtMaCa;
	private JTextField txtTenCa;
	private JTextArea txaGhiChu;
	private JRadioButton rdHoatDong;
	private JRadioButton rdNgung;
	private JTable tableCaLam;
	private DefaultTableModel modelCaLam;
	private JButton btnThem;
	private JButton btnHuy;
	private JButton btnXoa;
	private JButton btnSua;
	private JSpinner spngioKT;
	private JSpinner spngioBD;

	private CaLamViecDAO caLam_dao;

	public ThemCaLam() {
		ConnectDB.getInstance().connect();
		caLam_dao = new CaLamViecDAO();

		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.decode("#EAF1F9"));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		// Title
		JLabel lblTitle = new JLabel("QUẢN LÝ CA LÀM", SwingConstants.CENTER);
		add(lblTitle, BorderLayout.NORTH);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(Color.decode("#333333"));

		// Form nhập
		JPanel pnForm = new JPanel(new GridBagLayout());
		add(pnForm, BorderLayout.WEST);
		pnForm.setBackground(Color.WHITE);
		pnForm.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
				"Thêm ca làm", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(25, 118, 210)));

		int row = 0;
		// Input
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 15, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST; // Căn trái
		// mã ca
		txtMaCa = new JTextField(15);
		txtMaCa.setEditable(false);
		txtMaCa.setText(caLam_dao.generateMaCaLam());
		addFormField(pnForm, gbc, row++, "Mã ca làm *", txtMaCa);
		// tên ca
		txtTenCa = new JTextField(15);
		addFormField(pnForm, gbc, row++, "Tên ca làm *", txtTenCa);

		// giờ làm
		SpinnerDateModel modelBD = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY);
		spngioBD = new JSpinner(modelBD);
		spngioBD.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		spngioBD.setEditor(new JSpinner.DateEditor(spngioBD, "HH:mm"));
		addFormField(pnForm, gbc, row++, "Giờ bắt đầu *", spngioBD);
		// tan ca
		SpinnerDateModel modelKT = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY);
		spngioKT = new JSpinner(modelKT);
		spngioKT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		spngioKT.setEditor(new JSpinner.DateEditor(spngioKT, "HH:mm"));
		addFormField(pnForm, gbc, row++, "Giờ kết thúc *", spngioKT);

		// trạng thái
		JPanel pnTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rdHoatDong = new JRadioButton("Còn hiệu lực");
		rdHoatDong.setSelected(true);
//		rdHoatDong.setOpaque(false);
		rdNgung = new JRadioButton("Hết hiệu lực");
		rdNgung.setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		group.add(rdHoatDong);
		group.add(rdNgung);
		pnTrangThai.add(rdHoatDong);
		pnTrangThai.add(rdNgung);
		pnTrangThai.setOpaque(false);
		addFormField(pnForm, gbc, row++, "Trạng thái", pnTrangThai);

		// các nút
		JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		btnThem = new JButton("Thêm");
		btnSua = new JButton("Sửa");
		btnXoa = new JButton("Xóa");
		btnHuy = new JButton("Hủy");
		btnThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
		pnButton.add(btnThem);
		pnButton.add(btnSua);
		pnButton.add(btnXoa);
		pnButton.add(btnHuy);
		pnButton.setOpaque(false);
		gbc.gridy = row++;
		pnForm.add(pnButton, gbc);

		// bảng thông tin
		JPanel pnCaLam = new JPanel();
		pnCaLam.setBackground(Color.WHITE);
		pnCaLam.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
				"Thông tin ca làm", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(25, 118, 210)));
		add(pnCaLam, BorderLayout.CENTER);
		String[] col = { "STT", "Mã ca làm", "Tên ca làm", "Giờ vô ca", "Giờ tan ca", "Trạng thái" };
		modelCaLam = new DefaultTableModel(col, 0);
		tableCaLam = new JTable(modelCaLam);
		add(new JScrollPane(tableCaLam));

		// Load dữ liệu
		loadDuLieuVaoBang();

		// Xử lý sự kiện
		btnThem.addActionListener(this);
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);
		btnHuy.addActionListener(this);
		tableCaLam.addMouseListener(this);
	}

	// Hàm tiện ích thêm 1 hàng label + component
	private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		if (labelText.contains("*"))
			label.setForeground(new Color(183, 28, 28));
		panel.add(label, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		panel.add(component, gbc);

	}

	// Load dữ liệu từ Data lên bảng
	private void loadDuLieuVaoBang() {
		modelCaLam.setRowCount(0);
		List<CaLamViec> listCaLam = caLam_dao.layDanhSachCaLamViec();
		int stt = 1;
		for (CaLamViec ca : listCaLam) {
			modelCaLam.addRow(new Object[] { stt++, ca.getMaCa(), ca.getTenCa(), ca.getGioVaoLam(), ca.getGioTanLam(),
					ca.isTrangThai() ? "Còn hiệu lực" : "Hết hiệu lực" });
		}
	}
	
	//Click chuột vào bảng
	@Override
	public void mouseClicked(MouseEvent e) {
		if (isResetting) return; 
		int row = tableCaLam.getSelectedRow();
		txtMaCa.setText(modelCaLam.getValueAt(row, 1).toString());
		txtTenCa.setText(modelCaLam.getValueAt(row, 2).toString());
		try {
			spngioBD.setValue(new SimpleDateFormat("HH:mm").parse(modelCaLam.getValueAt(row, 3).toString()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			spngioKT.setValue(new SimpleDateFormat("HH:mm").parse(modelCaLam.getValueAt(row, 4).toString()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String trangThai = modelCaLam.getValueAt(row, 5).toString();
		rdHoatDong.setSelected(trangThai.equalsIgnoreCase("Còn hiệu lực"));
		rdNgung.setSelected(trangThai.equalsIgnoreCase("Hết hiệu lực"));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem)) {
			themCaLam();
		}
		if (o.equals(btnXoa)) {
			xoaCaLam();
		}
		if (o.equals(btnSua)) {
			suaCaLam();
		}
		if (o.equals(btnHuy)) {
			huy();
		}

	}

	// Sự kiện các nút
	private void huy() {
		isResetting = true;
		txtMaCa.setText(caLam_dao.generateMaCaLam());
		txtTenCa.setText("");
		txtTenCa.requestFocus();
		spngioBD.setValue(new Date());
		spngioKT.setValue(new Date());
		rdHoatDong.setSelected(true);
		tableCaLam.clearSelection();
	}
	
	//Thêm
	private void themCaLam() {
	    if (!validata())
	        return;

	    String maCa = txtMaCa.getText().trim();
	    String tenCa = txtTenCa.getText().trim();
	    Date gioBDDate = (Date) spngioBD.getValue();
	    Date gioKTDate = (Date) spngioKT.getValue();
	    boolean trangThai = rdHoatDong.isSelected();
	    // Chuyển sang LocalTime
	    LocalTime gioVaoLam = new Time(gioBDDate.getTime()).toLocalTime();
	    LocalTime gioTanLam = new Time(gioKTDate.getTime()).toLocalTime();
	    
	    CaLamViec ca = new CaLamViec(maCa, tenCa, gioVaoLam, gioTanLam, trangThai);

	    // Thêm vào data
	    try {
	        if (caLam_dao.themCaLamViec(ca)) {
	            JOptionPane.showMessageDialog(this, "Thêm ca làm thành công!");
	            loadDuLieuVaoBang();
	            huy();
	        } else {
	            JOptionPane.showMessageDialog(this, "Lỗi: Thêm ca làm thất bại!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi: Thêm ca làm thất bại!");
	    }
	}
	
	//Xóa
	private void xoaCaLam() {
	    int row = tableCaLam.getSelectedRow();
	    if (row >= 0) {
	        String maCa = tableCaLam.getValueAt(row, 1).toString();
	        int chon = JOptionPane.showConfirmDialog(this, 
	                    "Bạn có muốn xóa ca làm này không?", "Xóa", 
	                    JOptionPane.YES_NO_OPTION);
	        if (chon == JOptionPane.YES_OPTION) {
	            if (caLam_dao.xoaCaLam(maCa)) {
	                JOptionPane.showMessageDialog(this, "Xóa ca làm thành công!");
	                huy(); // reset form
	                loadDuLieuVaoBang(); // refresh bảng
	            } else {
	                JOptionPane.showMessageDialog(this, "Xóa ca làm thất bại!");
	            }
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn ca làm để xóa!");
	    }
	}
	
	//Sửa
	private void suaCaLam() {
		if(!validata()) return;
		
	    int row = tableCaLam.getSelectedRow();
	    if (row < 0) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn ca làm để sửa!");
	        return;
	    }

	    String maCa = txtMaCa.getText().trim();
	    String tenCa = txtTenCa.getText().trim();
	    Date gioBDDate = (Date) spngioBD.getValue();
	    Date gioKTDate = (Date) spngioKT.getValue();
	    boolean trangThai = rdHoatDong.isSelected();

	    LocalTime gioVaoLam = new Time(gioBDDate.getTime()).toLocalTime();
	    LocalTime gioTanLam = new Time(gioKTDate.getTime()).toLocalTime();
	    
	    // Tạo đối tượng
	    CaLamViec ca = new CaLamViec(maCa, tenCa, gioVaoLam, gioTanLam, trangThai);

	    // Cập nhật DB
	    if (caLam_dao.capNhatCaLamViec(ca)) {
	        tableCaLam.setValueAt(maCa, row, 1);
	        tableCaLam.setValueAt(tenCa, row, 2);
	        tableCaLam.setValueAt(gioVaoLam, row, 3);
	        tableCaLam.setValueAt(gioTanLam, row, 4);
	        tableCaLam.setValueAt(trangThai ? "Còn hiệu lực" : "Hết hiệu lực", row, 5);

	        JOptionPane.showMessageDialog(this, "Sửa ca làm thành công!");
	    } else {
	        JOptionPane.showMessageDialog(this, "Sửa ca làm thất bại!");
	    }
	    huy();
	}
	
	//Kiểm tra khi nhập
	private boolean validata() {
		String tenCa = txtTenCa.getText().toString().trim();
		Date gioBDDate = (Date) spngioBD.getValue();
	    Date gioKTDate = (Date) spngioKT.getValue();
		LocalTime gioLam = new Time(gioBDDate.getTime()).toLocalTime();
		LocalTime gioTan =new Time(gioKTDate.getTime()).toLocalTime();
		
		if(!(tenCa.length() > 0 && tenCa.matches("^[A-Za-zÀ-Ỹà-ỹ0-9 ]{2,50}$"))) {
			JOptionPane.showMessageDialog(this, "Lỗi: Tên ca làm nhập sai định dạng!\n Ví dụ: Ca sáng, Ca tối...");
			return false;
		}
		if(!gioLam.isAfter(LocalTime.of(8, 59)) ||  !gioLam.isBefore(gioTan)) {
			JOptionPane.showMessageDialog(this, "Lỗi: Giờ làm bắt đầu từ 9 giờ và phải trước giờ tan làm!");
			return false;
		}
		if(!gioTan.isBefore(LocalTime.of(23, 1)) || !gioTan.isAfter(gioLam)) {
			JOptionPane.showMessageDialog(this, "Lỗi: Giờ tan làm phải trước 23 giờ và phả sau giờ làm!");
			return false;
		}
		return true;
	}
	// Cập nhật lại bảng
		private void capNhatBang(List<CaLamViec> dsCa) {
			modelCaLam.setRowCount(0); // Xóa tất cả các hàng hiện có trong bảng
			int stt = 0;
			for (CaLamViec ca : dsCa) {
				modelCaLam.addRow(new Object[] { stt++, ca.getMaCa(), ca.getTenCa(), ca.getGioVaoLam(), ca.getGioTanLam(),
						ca.isTrangThai() ? "Còn hiệu lực" : "Hết hiệu lực" });
			}
		}
	//test
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Test - Thêm ca làm");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1200, 700);
			frame.setLocationRelativeTo(null);
			frame.add(new ThemCaLam());
			frame.setVisible(true);
		});
	}
}
