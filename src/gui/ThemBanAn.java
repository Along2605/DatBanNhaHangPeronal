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
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;


public class ThemBanAn extends JPanel{
	private JScrollPane scroll;
	private JButton btnLamMoi;
	private Component btnThem;
	private JButton btnXoa;
	private JButton btnSua;
	private JTextField txtGia;
	private JTextComponent txtMaBA;
	private JTextField txtTenBA;
	private JComboBox cbLoaiBA;
	private JComboBox cbTrangThaiBan;
	private JTextField txtGhiChu;
	private DefaultTableModel dataBA;

	public ThemBanAn() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#EAF1F9"));
		
		Box boxTop = Box.createVerticalBox();

		// Tiêu đề
		JLabel lblTitle = new JLabel("THÊM BÀN ĂN");
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
		pnForm.add(new JLabel("Mã bàn "), gbc);
		gbc.gridx = 1;
		txtMaBA = new JTextField(15);
		txtMaBA.setEditable(false);
		pnForm.add(txtMaBA, gbc);
		
		gbc.gridx = 2;
		pnForm.add(new JLabel("Số lượng chỗ ngồi "), gbc);
		gbc.gridx = 3;
		txtTenBA = new JTextField(15);
		pnForm.add(txtTenBA, gbc);
		
		//hàng 2
		gbc.gridx = 0; gbc.gridy = 1;
		pnForm.add(new JLabel("Loại bàn"), gbc);
		gbc.gridx = 1;
		cbLoaiBA = new JComboBox<>(new String[] {});
		pnForm.add(cbLoaiBA, gbc);
		
		gbc.gridx = 2;
		pnForm.add(new JLabel("Khu vực"), gbc);
		gbc.gridx = 3;
		txtGia = new JTextField(15);
		pnForm.add(txtGia, gbc);
		
		// Hàng 3
		gbc.gridx = 0; gbc.gridy = 2;
		pnForm.add(new JLabel("Trạng thái "), gbc);
		gbc.gridx = 1;
		cbTrangThaiBan = new JComboBox<>(new String[] {"Đang hoạt động", "Ngưng hoạt động", "Bảo trì"});
		pnForm.add(cbTrangThaiBan, gbc);
		
		gbc.gridx = 2;
		pnForm.add(new JLabel("Ghi chú"), gbc);
		gbc.gridx = 3;
		txtGhiChu = new JTextField(15);
		pnForm.add(txtGhiChu, gbc);
				
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
		 String[] tieuDeBanAn = {"Mã bàn", "Số lượng chỗ ngồi", "Loại bàn", "Khu vực", "Trạng thái", "Ghi chú"};
	     scroll = new JScrollPane(new JTable(dataBA = new DefaultTableModel(tieuDeBanAn, 0)));
	     scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
	       
		add(scroll, BorderLayout.CENTER);
	}
}
