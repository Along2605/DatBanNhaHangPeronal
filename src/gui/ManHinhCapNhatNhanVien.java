package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ManHinhCapNhatNhanVien extends JPanel implements ActionListener{
    private JTextField txtMaNV, txtHoTen, txtNgaySinh, txtEmail, txtSoDienThoai, txtNgayVaoLam;
    private JRadioButton rbNam, rbNu;
    private JComboBox<String> cbChucVu, cbTrangThai;
    private JLabel lblAnh;
    private JButton btnChonAnh, btnLuu, btnHuy;

    public ManHinhCapNhatNhanVien() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.decode("#EAF1F9"));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("HỒ SƠ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle, BorderLayout.NORTH);

        // Main content
        JPanel pnMain = new JPanel(new BorderLayout(20, 0));
        pnMain.setBackground(Color.decode("#EAF1F9"));
        add(pnMain, BorderLayout.CENTER);

        // Ảnh hồ sơ bên trái
        JPanel pnLeft = new JPanel();
        pnLeft.setBackground(Color.decode("#EAF1F9"));
        pnLeft.setLayout(new BoxLayout(pnLeft, BoxLayout.Y_AXIS));
        pnLeft.setBorder(new EmptyBorder(0, 0, 0, 0));

        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(180, 220));
        lblAnh.setMaximumSize(new Dimension(210, 220));
        lblAnh.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 2));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setVerticalAlignment(SwingConstants.CENTER);
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.WHITE);

        btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setFont(new Font("Arial", Font.PLAIN, 14));
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.setBackground(new Color(52, 152, 219));
        btnChonAnh.setForeground(Color.WHITE);
        btnChonAnh.setFocusPainted(false);

        pnLeft.add(lblAnh);
        pnLeft.add(Box.createVerticalStrut(15));
        pnLeft.add(btnChonAnh);
        
        btnChonAnh.addActionListener(this);

        pnMain.add(pnLeft, BorderLayout.WEST);

        // Form thông tin bên phải
        JPanel pnRight = new JPanel(new GridBagLayout());
        pnRight.setBackground(Color.decode("#EAF1F9"));
        pnRight.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(44, 62, 80), 1),
            "Thông tin chi tiết",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Khởi tạo component
        txtMaNV = createTextField(false);
        txtHoTen = createTextField(true);
        txtNgaySinh = createTextField(true);
        txtNgaySinh.setToolTipText("Định dạng: dd/MM/yyyy");
        txtEmail = createTextField(true);
        txtSoDienThoai = createTextField(true);
        rbNam = new JRadioButton("Nam");
        rbNu = new JRadioButton("Nữ");
        ButtonGroup bgGioiTinh = new ButtonGroup();
        bgGioiTinh.add(rbNam);
        bgGioiTinh.add(rbNu);
        cbChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên phục vụ", "Nhân viên bếp", "Thu ngân"});
        txtNgayVaoLam = createTextField(true);
        txtNgayVaoLam.setToolTipText("Định dạng: dd/MM/yyyy");
        cbTrangThai = new JComboBox<>(new String[]{"Đang làm việc", "Nghỉ việc"});

        // Row 0: Mã NV, Họ tên
        gbc.gridy = 0;
        addLabelAndField(pnRight, gbc, 0, "Mã nhân viên:", txtMaNV);
        addLabelAndField(pnRight, gbc, 2, "Họ tên:", txtHoTen);

        // Row 1: Ngày sinh, Email
        gbc.gridy = 1;
        addLabelAndField(pnRight, gbc, 0, "Ngày sinh:", txtNgaySinh);
        addLabelAndField(pnRight, gbc, 2, "Email:", txtEmail);

        // Row 2: SĐT, Giới tính
        gbc.gridy = 2;
        addLabelAndField(pnRight, gbc, 0, "Số điện thoại:", txtSoDienThoai);

        JPanel pnGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnGioiTinh.setBackground(Color.decode("#EAF1F9"));
        rbNam.setFont(new Font("Arial", Font.PLAIN, 14));
        rbNu.setFont(new Font("Arial", Font.PLAIN, 14));
        pnGioiTinh.add(rbNam);
        pnGioiTinh.add(rbNu);
        addLabelAndField(pnRight, gbc, 2, "Giới tính:", pnGioiTinh);

        // Row 3: Chức vụ, Ngày vào làm
        gbc.gridy = 3;
        addLabelAndField(pnRight, gbc, 0, "Chức vụ:", cbChucVu);
        addLabelAndField(pnRight, gbc, 2, "Ngày vào làm:", txtNgayVaoLam);

        // Row 4: Trạng thái
        gbc.gridy = 4;
        addLabelAndField(pnRight, gbc, 0, "Trạng thái:", cbTrangThai);

        pnMain.add(pnRight, BorderLayout.CENTER);

        // Button panel dưới cùng
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 5));
        pnButtons.setBackground(Color.decode("#EAF1F9"));

        btnLuu = createButton("Lưu", "img/save.png", new Color(46, 204, 113));
        btnHuy = createButton("Hủy", "img/cancel.png", new Color(231, 76, 60));
        pnButtons.add(btnLuu);
        pnButtons.add(btnHuy);

        add(pnButtons, BorderLayout.SOUTH);
    }

    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(16);
        textField.setFont(new Font("Arial", Font.PLAIN, 15));
        textField.setEditable(editable);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
            BorderFactory.createEmptyBorder(6, 7, 6, 7)
        ));
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private JButton createButton(String text, String iconPath, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 38));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        }
        return button;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int col, String label, Component field) {
        gbc.gridx = col;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(new Color(44, 62, 80));
        panel.add(lbl, gbc);

        gbc.gridx = col + 1;
        panel.add(field, gbc);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o= e.getSource();
		if(o==btnChonAnh) {
			// TODO Auto-generated method stub
			JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setDialogTitle("Chọn ảnh nhân viên");
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

	        // Chỉ cho chọn file ảnh (jpg, png, jpeg)
	        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
	            "Image files", "jpg", "png", "jpeg"
	        ));

	        int result = fileChooser.showOpenDialog(null);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            // Lấy file được chọn
	            File selectedFile = fileChooser.getSelectedFile();
	            // Tạo ảnh từ file
	            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
	            // Đổi kích thước ảnh cho vừa khung hiển thị
	            Image img = icon.getImage().getScaledInstance(
	                lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH
	            );
	            lblAnh.setIcon(new ImageIcon(img));
	        }
		}
		
		
	}
}