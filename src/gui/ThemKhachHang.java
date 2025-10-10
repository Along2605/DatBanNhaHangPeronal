package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ThemKhachHang extends JPanel{
	private JScrollPane scroll;
	private DefaultTableModel dataKH;
	private JLabel lblNhap;
	private JTextField txtTim;
	private JButton btnTim;
	private JButton btnLamMoi;
	private JTable tableKhachHang;
	private JLabel lblMaKH;
	private JTextField txtMaKH;
	private JLabel lblTenKH;
	private JTextField txtTenKH;
	private JTextField txtHoTenKH;
	private JComboBox<String> cbGioiTinh;
	private JTextField txtSDT;
	private JTextField txtEmail;
	private Component btnThem;
	private JButton btnXoa;
	private JButton btnSua;

	public ThemKhachHang() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#EAF1F9"));
		
		Box boxTop = Box.createVerticalBox();

		// Tiêu đề
		JLabel lblTitle = new JLabel("THÊM KHÁCH HÀNG");
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
		gbc.gridx = 0; gbc.gridy = 0;
		pnForm.add(new JLabel("Mã khách hàng "), gbc);
		gbc.gridx = 1;
		txtMaKH = new JTextField(15);
		txtMaKH.setEditable(false);
		pnForm.add(txtMaKH, gbc);
		
		gbc.gridx = 2;
		pnForm.add(new JLabel("Họ tên "), gbc);
		gbc.gridx = 3;
		txtHoTenKH = new JTextField(15);
		pnForm.add(txtHoTenKH, gbc);
		
		//hàng 2
		gbc.gridx = 0; gbc.gridy = 1;
		pnForm.add(new JLabel("Giới tính"), gbc);
		gbc.gridx = 1;
		cbGioiTinh = new JComboBox<>(new String[] {"Nam", "Nữ"});
		pnForm.add(cbGioiTinh, gbc);
		
		gbc.gridx = 2;
		pnForm.add(new JLabel("Số điện thoại"), gbc);
		gbc.gridx = 3;
		txtSDT = new JTextField(15);
		pnForm.add(txtSDT, gbc);
		txtEmail= new JTextField(15);
		pnForm.add(txtEmail, gbc);
		// Hàng 3
		gbc.gridx = 0; gbc.gridy = 2;
		pnForm.add(new JLabel("Email: "), gbc);
		gbc.gridx = 1;
		txtEmail= new JTextField(15);
		pnForm.add(txtEmail, gbc);
		
	    //hàng 4:
		gbc.gridx = 0; gbc.gridy = 3;
		ImageIcon iconSua = new ImageIcon("img/edit.png");
	    Image imgSua = iconSua.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); //thu nhỏ icon
	    btnSua = new JButton("Sửa", new ImageIcon(imgSua));
	    btnSua.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnSua, gbc);
		
		gbc.gridx = 1;
		ImageIcon iconThem = new ImageIcon("img/add.png");
	    Image imgThem = iconThem.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); //thu nhỏ icon
	    btnThem = new JButton("Thêm", new ImageIcon(imgThem));
	    btnThem.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnThem, gbc);
	    
		gbc.gridx = 2;
		ImageIcon iconXoa = new ImageIcon("img/delete.png");
	    Image imgXoa = iconXoa.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); //thu nhỏ icon
	    btnXoa = new JButton("Xóa", new ImageIcon(imgXoa));
	    btnXoa.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnXoa, gbc);
		
	    
		gbc.gridx = 3;
		ImageIcon iconLamMoi = new ImageIcon("img/refresh.png");
	    Image imgLamMoi = iconLamMoi.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); //thu nhỏ icon
	    btnLamMoi = new JButton("Làm mới", new ImageIcon(imgLamMoi));
	    btnLamMoi.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnLamMoi, gbc);
		//Danh sách khách hàng
		String[] tieuDeKhachHang = {"Mã khách hàng", "Tên khách hàng", "Giới tính", "Số điện thoại", "Email", "Điểm tích lũy"};
		scroll = new JScrollPane(tableKhachHang = new JTable(dataKH = new DefaultTableModel(tieuDeKhachHang, 0)));
		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
	   
		add(scroll, BorderLayout.CENTER);
	   
	  
	}
}
