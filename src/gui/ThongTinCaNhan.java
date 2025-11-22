package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;

public class ThongTinCaNhan extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMaNV;
	private JTextField txtTenNV;
	private JTextField txtSdt;
	private JTextField txtEmail;
	private JLabel lblAnh;
	private JDateChooser dcrNgayVaoLam;
	private JDateChooser dcrNgaySinh;
	private JButton btnLuu;
	private JButton btnHuy;
	private JComboBox<String> cbmGioiTinh;
	
	private NhanVienDAO nv_dao;
	private TaiKhoanDAO tk_dao;
	private JButton btnSua;
	public static String maNVDanhNhap = null; //dang nhap thanh cong
	private String duongDanAnh = "img/user.jpg"; // mặc định

	
	public ThongTinCaNhan() {
		ConnectDB.getInstance().connect();
    	tk_dao = new TaiKhoanDAO();
    	nv_dao = new NhanVienDAO();
		
		setLayout(new BorderLayout(10,10));
        setBackground(Color.decode("#EAF1F9"));
        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(16,0,8,0));
        add(lblTitle, BorderLayout.NORTH);
        
        
        JPanel pnCenter = new JPanel(new GridLayout(1, 2, 10, 0));
        pnCenter.setOpaque(false);
        add(pnCenter, BorderLayout.CENTER);
        
        //Bên trái
        JPanel pnAnh = new JPanel(new GridBagLayout());
        pnCenter.add(pnAnh);
        pnAnh.setPreferredSize(new Dimension(200, 0)); // chiều rộng panel trái
        pnAnh.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        ImageIcon anhUser = new ImageIcon("img/user.jpg");
    	Image imgUser = anhUser.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
    	anhUser = new ImageIcon(imgUser);
    	
    	lblAnh = new JLabel(anhUser);
    	lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
    	pnAnh.add(lblAnh);
    	
    	
    	//Form thông tin, bên phải
    	JPanel pnForm = new JPanel();
    	pnForm.setLayout(new GridBagLayout());
    	pnForm.setOpaque(false);
    	pnCenter.add(pnForm, BorderLayout.CENTER);
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.insets = new Insets(10,  10,  10, 10);
    	gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
    	//Mã nv
        gbc.gridx = 0; 
        gbc.gridy = row++;
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        pnForm.add(lblMaNV, gbc);
        gbc.gridx = 1;
        txtMaNV = new JTextField(15);
        txtMaNV.setPreferredSize(new Dimension(200, 35));
        txtMaNV.setEditable(false);
        pnForm.add(txtMaNV, gbc);
        

    	//Họ tên
        gbc.gridx = 0; 
        gbc.gridy = row++;
        JLabel lblTenNV = new JLabel("Họ nhân viên:");
        pnForm.add(lblTenNV, gbc);
        
        gbc.gridx = 1;
        txtTenNV = new JTextField(15);
        txtTenNV.setPreferredSize(new Dimension(200, 35));
        txtTenNV.setEditable(false);
        pnForm.add(txtTenNV, gbc);
        
    	//Giới tính
        gbc.gridx = 0; 
        gbc.gridy = row++;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        pnForm.add(lblGioiTinh, gbc);

        gbc.gridx = 1;
        cbmGioiTinh  = new JComboBox<String>(new String[] {"Nam", "Nữ"});
        cbmGioiTinh.setEnabled(false);
        cbmGioiTinh.setPreferredSize(new Dimension(200, 35));
        pnForm.add(cbmGioiTinh, gbc);
    	
    	//Ngày sinh
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        pnForm.add(lblNgaySinh, gbc);

        gbc.gridx = 1;
        dcrNgaySinh = new JDateChooser();
        dcrNgaySinh.setDateFormatString("dd/MM/yyyy");
        dcrNgaySinh.setEnabled(false);
        dcrNgaySinh.setPreferredSize(new Dimension(200, 35));
        pnForm.add(dcrNgaySinh, gbc);

    	
    	//Sđt
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setForeground(Color.decode("#2C3E50"));
        pnForm.add(lblSDT, gbc);

        gbc.gridx = 1;
        txtSdt = new JTextField(15);
        txtSdt.setEditable(false);
        txtSdt.setPreferredSize(new Dimension(200, 35));
        pnForm.add(txtSdt, gbc);
    	
    	//Email
        gbc.gridx = 0; gbc.gridy = row++;
    	JLabel lblEmail = new JLabel("Email");
        lblSDT.setForeground(Color.decode("#2C3E50"));
        pnForm.add(lblEmail, gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        txtEmail.setEditable(false);
        txtEmail.setPreferredSize(new Dimension(200, 35));
        pnForm.add(txtEmail, gbc);
        
    	
    	//Ngay vào làm
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel lblNgayLam = new JLabel("Ngày vào làm");
        lblSDT.setForeground(Color.decode("#2C3E50"));
        pnForm.add(lblNgayLam, gbc);

        gbc.gridx = 1;
        dcrNgayVaoLam = new JDateChooser();
        dcrNgayVaoLam.setDateFormatString("dd/MM/yyyy");
        dcrNgayVaoLam.setEnabled(false);
        dcrNgayVaoLam.setPreferredSize(new Dimension(200, 35));
        pnForm.add(dcrNgayVaoLam, gbc);
    	
    	//các nút
        gbc.gridy = row++;
        gbc.gridx = 0;
        btnSua = new JButton("Sửa");
        btnSua.setPreferredSize(new Dimension(200, 35));
        pnForm.add(btnSua, gbc);

        gbc.gridx = 1;
        btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(200, 35));
        pnForm.add(btnLuu, gbc);
        
        gbc.gridx = 2;
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(200, 35));
        pnForm.add(btnHuy, gbc);
        
        
        
        //Load dữ liệu
        loadThongTinTuData(); 
        
        //xử lý sự kiện
        btnHuy.addActionListener(this);
        btnLuu.addActionListener(this);
        btnSua.addActionListener(this);
	}
	
	private void loadThongTinTuData() {
	    NhanVien nv = util.Session.getNhanVienDangNhap();
	    if (nv == null) return; // chưa đăng nhập

	    txtMaNV.setText(nv.getMaNV());
	    txtTenNV.setText(nv.getHoTen());
	    cbmGioiTinh.setSelectedItem(nv.isGioiTinh() ? "Nam" : "Nữ");
	    txtEmail.setText(nv.getEmail());
	    txtSdt.setText(nv.getSoDienThoai());

	    LocalDate localDate = nv.getNgaySinh();
	    if (localDate != null) {
	        dcrNgaySinh.setDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    }

	    LocalDate localDate2 = nv.getNgayVaoLam();
	    if (localDate2 != null) {
	        dcrNgayVaoLam.setDate(Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    }

	    loadAnhNhanVien(nv.getAnhDaiDien());
	}

	private void loadAnhNhanVien(String duongDanAnh) {
	    try {
	        Image img = null;
	        
	        if (duongDanAnh != null && !duongDanAnh.trim().isEmpty()) {
	            File f = new File(duongDanAnh);
	            if (f.exists()) {
	                img = new ImageIcon(duongDanAnh).getImage();
	            }
	        }

	        // Nếu không có ảnh nhân viên hoặc file không tồn tại → dùng ảnh mặc định
	        if (img == null) {
	            java.net.URL url = getClass().getResource("/img/user.jpg");
	            if(url != null) {
	                img = new ImageIcon(url).getImage();
	            } else {
	                System.out.println("Không tìm thấy ảnh mặc định!");
	                return;
	            }
	        }

	        // Scale và set vào JLabel
	        Image scaledImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
	        lblAnh.setIcon(new ImageIcon(scaledImg));

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(btnLuu)) {
			luu();
		}
		if(o.equals(btnSua)) {
			sua();
		}
		if(o.equals(btnHuy)) {
			huy();
		}
		
	}
	
	private void sua() {
		txtTenNV.setEditable(true);
		cbmGioiTinh.setEnabled(true);
		dcrNgaySinh.setEnabled(true);
		txtSdt.setEditable(true);
		txtEmail.setEditable(true);
	}
	private void luu() {
	    try {
	        String maNV = txtMaNV.getText().trim();
	        String ten = txtTenNV.getText().trim();
	        boolean gioiTinh = cbmGioiTinh.getSelectedItem().toString().equals("Nam");
	        java.util.Date ngaySinhUtil = dcrNgaySinh.getDate();
	        String sdt = txtSdt.getText().trim();
	        String email = txtEmail.getText().trim();

	        // ====== Chuyển từ java.util.Date → LocalDate ======
	        LocalDate ngaySinh = ngaySinhUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	        // ====== Tạo đối tượng nhân viên ======
	        NhanVien nv = new NhanVien(maNV, ten, ngaySinh, email, ten, gioiTinh, email, ngaySinh, gioiTinh, ten);
	        nv.setMaNV(maNV);
	        nv.setHoTen(ten);
	        nv.setGioiTinh(gioiTinh);
	        nv.setNgaySinh(ngaySinh);
	        nv.setSoDienThoai(sdt);
	        nv.setEmail(email);
	        nv.setAnhDaiDien(duongDanAnh); // ✅ Lưu đường dẫn ảnh hiện tại

	        // ====== Gọi DAO cập nhật ======
	        boolean kq = nv_dao.capNhatThongTinCaNhan(nv);
	        if (kq) {
	            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
	            huy();
	        } else {
	            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
	        }

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin!");
	    }
	}


	private void huy() {
		txtTenNV.setEditable(false);
		cbmGioiTinh.setEnabled(false);
		dcrNgaySinh.setEnabled(false);
		txtSdt.setEditable(false);
		txtEmail.setEditable(false);
	}
	
	//test
		public static void main(String[] args) {
			SwingUtilities.invokeLater(() -> {
				JFrame frame = new JFrame("Test - Thêm ca làm");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(1200, 700);
				frame.setLocationRelativeTo(null);
				frame.add(new ThongTinCaNhan());
				frame.setVisible(true);
			});
		}
	
}
