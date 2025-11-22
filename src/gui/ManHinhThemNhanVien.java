package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dao.NhanVienDAO;
import entity.NhanVien;

public class ManHinhThemNhanVien extends JPanel implements ActionListener {
    private JTextField txtMaNV, txtHoTen, txtNgaySinh, txtEmail, txtSoDienThoai, txtNgayVaoLam;
    private JRadioButton rbNam, rbNu;
    private ButtonGroup bgGioiTinh;
    private JComboBox<String> cbChucVu, cbTrangThai;
    private JLabel lblAnh;
    private JButton btnChonAnh, btnLuu, btnXoaTrang;

    private NhanVienDAO nhanVienDAO;
    private Runnable onSaveListener;
    private String duongDanAnh; // Lưu đường dẫn ảnh đã chọn

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ManHinhThemNhanVien() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.decode("#EAF1F9"));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        nhanVienDAO = new NhanVienDAO();

        // Tiêu đề
        JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN MỚI", SwingConstants.CENTER);
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

        btnChonAnh.setPreferredSize(new Dimension(140, 35));
        btnChonAnh.setMaximumSize(new Dimension(160, 35));

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
        txtMaNV = createTextField(false); // Mã NV không cho sửa
        txtHoTen = createTextField(true);
        txtNgaySinh = createTextField(true);
        txtNgaySinh.setToolTipText("Định dạng: dd/MM/yyyy");
        txtEmail = createTextField(true);
        txtSoDienThoai = createTextField(true);
        rbNam = new JRadioButton("Nam");
        rbNu = new JRadioButton("Nữ");
        bgGioiTinh = new ButtonGroup();
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
        rbNam.setBackground(Color.decode("#EAF1F9"));
        rbNu.setBackground(Color.decode("#EAF1F9"));
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
        btnXoaTrang = createButton("Xóa trắng", "img/clear.png", new Color(231, 76, 60)); // Đổi nút Hủy -> Xóa trắng

        btnLuu.addActionListener(this);
        btnXoaTrang.addActionListener(this);

        pnButtons.add(btnLuu);
        pnButtons.add(btnXoaTrang);

        add(pnButtons, BorderLayout.SOUTH);

        // Set giá trị mặc định cho form thêm
        xoaTrang();
    }

    // Phát sinh mã NV mới
    private void phatSinhMaNV() {
        try {
            // Giả định NhanVienDAO có phương thức phatSinhMaNV()
            String maNV = nhanVienDAO.phatSinhMaNV(); 
            txtMaNV.setText(maNV);
        } catch (Exception e) {
            txtMaNV.setText("NV" + System.currentTimeMillis() % 10000); // Mã tạm
            e.printStackTrace();
        }
    }

    // Xóa trắng form
    private void xoaTrang() {
        phatSinhMaNV();
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtEmail.setText("");
        txtSoDienThoai.setText("");
        bgGioiTinh.clearSelection();
        cbChucVu.setSelectedIndex(0);
        
        // Mặc định ngày vào làm là hôm nay
        txtNgayVaoLam.setText(LocalDate.now().format(formatter));
        
        // Mặc định trạng thái là "Đang làm việc" và không cho sửa
        cbTrangThai.setSelectedIndex(0);
        cbTrangThai.setEnabled(false); 
        
        // Set ảnh mặc định
        duongDanAnh = "img/employees/default-avatar.png";
        hienThiAnh(duongDanAnh);
        
        txtHoTen.requestFocus();
    }

    // Lấy dữ liệu từ form
    private NhanVien layDuLieuTuForm() {
        try {
            String maNV = txtMaNV.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            LocalDate ngaySinh = LocalDate.parse(txtNgaySinh.getText().trim(), formatter);
            String email = txtEmail.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();
            boolean gioiTinh = rbNam.isSelected();
            String chucVu = cbChucVu.getSelectedItem().toString();
            LocalDate ngayVaoLam = LocalDate.parse(txtNgayVaoLam.getText().trim(), formatter);
            boolean trangThai = cbTrangThai.getSelectedIndex() == 0; // Luôn là true (Đang làm việc)

            // Đường dẫn ảnh đã được cập nhật khi chọn ảnh
            return new NhanVien(maNV, hoTen, ngaySinh, email, sdt, gioiTinh, chucVu, ngayVaoLam, trangThai, duongDanAnh);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Validate dữ liệu
    private boolean validateData() {
        String maNV = txtMaNV.getText().trim();
        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể lấy được mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Kiểm tra mã đã tồn tại chưa
        if (nhanVienDAO.getNhanVienTheoMa(maNV) != null) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại! Vui lòng tải lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!txtEmail.getText().trim().matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        if (txtSoDienThoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }

        // Validate phone number (10 digits)
        if (!txtSoDienThoai.getText().trim().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }

        if (!rbNam.isSelected() && !rbNu.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate date format
        try {
            LocalDate.parse(txtNgaySinh.getText().trim(), formatter);
            LocalDate.parse(txtNgayVaoLam.getText().trim(), formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! (dd/MM/yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Thêm nhân viên vào database
    private void themNhanVien() {
        if (!validateData()) {
            return;
        }

        NhanVien nv = layDuLieuTuForm();
        if (nv == null) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thêm nhân viên này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Gọi phương thức themNhanVien từ DAO
                boolean success = nhanVienDAO.themNhanVien(nv);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Thêm nhân viên thành công!",
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Gọi callback để reload danh sách (nếu có)
                    if (onSaveListener != null) {
                        onSaveListener.run();
                    }
                    
                    // Xóa trắng form để chuẩn bị thêm nv tiếp theo
                    xoaTrang();
                    
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Thêm nhân viên thất bại!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi thêm: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Set listener để xử lý sau khi lưu thành công
    public void setOnSaveListener(Runnable listener) {
        this.onSaveListener = listener;
    }

    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(16);
        textField.setFont(new Font("Arial", Font.PLAIN, 15));
        textField.setEditable(editable);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
                BorderFactory.createEmptyBorder(6, 7, 6, 7)
        ));
        
        if (editable) {
            textField.setBackground(Color.WHITE);
        } else {
            textField.setBackground(Color.decode("#EEEEEE")); // Màu xám cho ô không edit
        }
        return textField;
    }

    private JButton createButton(String text, String iconPath, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(130, 38)); // Tăng chiều rộng
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (iconPath != null) {
            try {
                ImageIcon icon = new ImageIcon(iconPath);
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                // Icon not found, continue without icon
            }
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
        Object o = e.getSource();

        if (o == btnChonAnh) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn ảnh nhân viên");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Chỉ cho chọn file ảnh (jpg, png, jpeg)
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "png", "jpeg"
            ));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Tạo thư mục lưu ảnh nếu chưa tồn tại
                File employeeDir = new File("img/employees");
                if (!employeeDir.exists()) {
                    employeeDir.mkdirs();
                }

                // Tạo tên file mới: maNV + extension
                String maNV = txtMaNV.getText().trim();
                if(maNV.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không thể lấy mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String extension = getFileExtension(selectedFile.getName());
                String newFileName = maNV + extension;
                File destFile = new File(employeeDir, newFileName);

                try {
                    // Copy file ảnh vào thư mục employees
                    Files.copy(
                            selectedFile.toPath(),
                            destFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    // Lưu đường dẫn tương đối
                    duongDanAnh = "img/employees/" + newFileName;

                    // Hiển thị ảnh
                    hienThiAnh(duongDanAnh);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Lỗi khi lưu ảnh: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnLuu) {
            themNhanVien();
        } else if (o == btnXoaTrang) {
            xoaTrang();
        }
    }


    // Hiển thị ảnh lên label
    private void hienThiAnh(String duongDan) {
        if (duongDan == null || duongDan.isEmpty()) {
            duongDan = "img/employees/default-avatar.png";
        }
        
        // Cập nhật lại biến duongDanAnh để đảm bảo nó luôn có giá trị
        this.duongDanAnh = duongDan; 

        try {
            File imageFile = new File(duongDan);
            ImageIcon icon;

            if (imageFile.exists()) {
                icon = new ImageIcon(imageFile.getAbsolutePath());
            } else {
                // Nếu file không tồn tại, thử load ảnh mặc định
                File defaultImage = new File("img/employees/default-avatar.png");
                if(defaultImage.exists()) {
                     icon = new ImageIcon(defaultImage.getAbsolutePath());
                     this.duongDanAnh = "img/employees/default-avatar.png";
                } else {
                    // Nếu cả 2 đều không tồn tại
                    lblAnh.setIcon(null);
                    lblAnh.setText("Không có ảnh");
                    System.out.println("File không tồn tại: " + imageFile.getAbsolutePath());
                    return;
                }
            }

            // Lấy kích thước của label (nếu chưa có thì dùng kích thước preferred)
            int width = lblAnh.getWidth() > 0 ? lblAnh.getWidth() : lblAnh.getPreferredSize().width;
            int height = lblAnh.getHeight() > 0 ? lblAnh.getHeight() : lblAnh.getPreferredSize().height;

            // Scale ảnh
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            lblAnh.setIcon(new ImageIcon(img));
            lblAnh.setText(null); // Xóa text nếu có

        } catch (Exception e) {
            lblAnh.setIcon(null);
            lblAnh.setText("Lỗi load ảnh");
            e.printStackTrace(); // In ra lỗi chi tiết
            System.out.println("Đường dẫn lỗi: " + duongDan);
        }
    }

    // Lấy extension của file
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return ".jpg"; // Mặc định
    }
}