package gui;

import javax.swing.*;
import javax.swing.border.*;
import entity.BanAn;
import connectDB.ConnectDB;
import java.awt.*;
import java.sql.*;

public class DialogTaoHoaDon extends JDialog {
    
    private static final long serialVersionUID = 1L;
    private static final Color MAIN_COLOR = new Color(41, 128, 185);
    
    private BanAn banAn;
    private JTextField txtMaHoaDon;
    private JTextField txtSDTKhachHang;
    private JTextField txtTenKhachHang;
    private JTextField txtNhanVien;
    private JTextField txtNgayLap;
    private JTextArea txtGhiChu;
    private String maKhachHang = null;
    
    // Callback để refresh lại màn hình cha sau khi tạo hóa đơn
    private Runnable onSuccess;
    
    public DialogTaoHoaDon(Frame parent, BanAn banAn, Runnable onSuccess) {
        super(parent, "Tạo hóa đơn mới", true);
        this.banAn = banAn;
        this.onSuccess = onSuccess;
        
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
    }
    
    private void initComponents() {
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("TẠO HÓA ĐƠN MỚI", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(MAIN_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Thông tin bàn (read-only)
        JPanel banInfoPanel = createBanInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        formPanel.add(banInfoPanel, gbc);
        
        // Reset gridwidth
        gbc.gridwidth = 1;
        
        // Mã hóa đơn (tự động)
        txtMaHoaDon = createTextField();
        txtMaHoaDon.setText(taoMaHoaDonTuDong());
        txtMaHoaDon.setEditable(false);
        txtMaHoaDon.setBackground(new Color(240, 240, 240));
        addFormRow(formPanel, gbc, 1, "Mã hóa đơn:", txtMaHoaDon);
        
        // SĐT khách hàng + nút tìm
        txtSDTKhachHang = createTextField();
        JButton btnTimKH = createSmallButton("🔍");
        btnTimKH.addActionListener(e -> timKhachHang());
        
        JPanel sdtPanel = new JPanel(new BorderLayout(5, 0));
        sdtPanel.setBackground(Color.WHITE);
        sdtPanel.add(txtSDTKhachHang, BorderLayout.CENTER);
        sdtPanel.add(btnTimKH, BorderLayout.EAST);
        addFormRow(formPanel, gbc, 2, "SĐT khách hàng:", sdtPanel);
        
        // Tên khách hàng
        txtTenKhachHang = createTextField();
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setBackground(new Color(240, 240, 240));
        addFormRow(formPanel, gbc, 3, "Tên khách hàng:", txtTenKhachHang);
        
        // Nhân viên lập
        txtNhanVien = createTextField();
        txtNhanVien.setText(getNhanVienDangNhap());
        txtNhanVien.setEditable(false);
        txtNhanVien.setBackground(new Color(240, 240, 240));
        addFormRow(formPanel, gbc, 4, "Nhân viên lập:", txtNhanVien);
        
        // Ngày lập
        txtNgayLap = createTextField();
        txtNgayLap.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                          .format(new java.util.Date()));
        txtNgayLap.setEditable(false);
        txtNgayLap.setBackground(new Color(240, 240, 240));
        addFormRow(formPanel, gbc, 5, "Ngày lập:", txtNgayLap);
        
        // Ghi chú
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGhiChu.setVerticalAlignment(JLabel.TOP);
        formPanel.add(lblGhiChu, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(scrollGhiChu, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panel nút
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Tạo panel hiển thị thông tin bàn
     */
    private JPanel createBanInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 5));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(MAIN_COLOR, 2),
                "Thông tin bàn",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                MAIN_COLOR
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        panel.add(createBoldLabel("Mã bàn:"));
        panel.add(createNormalLabel(banAn.getMaBan()));
        panel.add(createBoldLabel("Tên bàn:"));
        panel.add(createNormalLabel(banAn.getTenBan()));
        panel.add(createBoldLabel("Khu vực:"));
        panel.add(createNormalLabel(
            banAn.getKhuVuc() != null && banAn.getKhuVuc().getTenKhuVuc() != null
                ? banAn.getKhuVuc().getTenKhuVuc()
                : "Chưa xác định"
        ));
        
        return panel;
    }
    
    /**
     * Tạo panel chứa các nút
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        
        JButton btnTaoHoaDon = createButton("Tạo hóa đơn", MAIN_COLOR);
        btnTaoHoaDon.addActionListener(e -> taoHoaDon());
        
        JButton btnHuy = createButton("Hủy", new Color(100, 100, 100));
        btnHuy.addActionListener(e -> dispose());
        
        panel.add(btnTaoHoaDon);
        panel.add(btnHuy);
        
        return panel;
    }
    
    /**
     * Tìm khách hàng theo số điện thoại
     */
    private void timKhachHang() {
        String sdt = txtSDTKhachHang.getText().trim();
        
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập số điện thoại!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT maKH, hoTen FROM KhachHang WHERE sdt = ?";
        
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, sdt);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Tìm thấy khách hàng
                maKhachHang = rs.getString("maKH");
                txtTenKhachHang.setText(rs.getString("hoTen"));
                
                JOptionPane.showMessageDialog(this,
                    "Tìm thấy khách hàng!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Không tìm thấy
                maKhachHang = null;
                txtTenKhachHang.setText("");
                
                int choice = JOptionPane.showConfirmDialog(this,
                    "Không tìm thấy khách hàng!\n" +
                    "Bạn có muốn thêm khách hàng mới không?",
                    "Thông báo", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    // TODO: Mở form thêm khách hàng
                    // new DialogThemKhachHang(this, sdt, (maKH, tenKH) -> {
                    //     maKhachHang = maKH;
                    //     txtTenKhachHang.setText(tenKH);
                    // }).setVisible(true);
                    
                    JOptionPane.showMessageDialog(this,
                        "Chức năng thêm khách hàng sẽ được cài đặt sau!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException ex) {
            System.err.println("❌ Lỗi khi tìm khách hàng:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm khách hàng!\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tạo hóa đơn
     */
    private void taoHoaDon() {
        // Validate
        if (txtSDTKhachHang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập thông tin khách hàng!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDTKhachHang.requestFocus();
            return;
        }
        
        if (maKhachHang == null) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng tìm khách hàng hợp lệ!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận tạo hóa đơn cho bàn " + banAn.getTenBan() + "?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Thực hiện tạo hóa đơn
        try {
            Connection con = ConnectDB.getConnection();
            con.setAutoCommit(false); // Bắt đầu transaction
            
            try {
                // 1. Tạo hóa đơn
                String sqlHoaDon = "INSERT INTO HoaDon (maHoaDon, maBan, maKH, maNV, " +
                                   "ngayLapHoaDon, thueVAT, trangThai) " +
                                   "VALUES (?, ?, ?, ?, GETDATE(), 10, N'Chưa thanh toán')";
                
                PreparedStatement pstmtHoaDon = con.prepareStatement(sqlHoaDon);
                pstmtHoaDon.setString(1, txtMaHoaDon.getText());
                pstmtHoaDon.setString(2, banAn.getMaBan());
                pstmtHoaDon.setString(3, maKhachHang);
                pstmtHoaDon.setString(4, getMaNhanVienDangNhap());
                
                pstmtHoaDon.executeUpdate();
                pstmtHoaDon.close();
                
                // 2. Cập nhật trạng thái bàn
                String sqlBan = "UPDATE BanAn SET trangThai = N'Đang sử dụng' WHERE maBan = ?";
                PreparedStatement pstmtBan = con.prepareStatement(sqlBan);
                pstmtBan.setString(1, banAn.getMaBan());
                pstmtBan.executeUpdate();
                pstmtBan.close();
                
                // 3. Commit transaction
                con.commit();
                con.setAutoCommit(true);
                
                // Thông báo thành công
                JOptionPane.showMessageDialog(this,
                    "Tạo hóa đơn thành công!\n" +
                    "Mã hóa đơn: " + txtMaHoaDon.getText() + "\n\n" +
                    "Bàn " + banAn.getTenBan() + " đã chuyển sang trạng thái 'Đang sử dụng'",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Callback để refresh màn hình cha
                if (onSuccess != null) {
                    onSuccess.run();
                }
                
                dispose();
                
                // TODO: Mở màn hình order món ăn
                // ManHinhChinhNhanVien.showPanel(new OrderMonAn(txtMaHoaDon.getText(), banAn.getMaBan()));
                
            } catch (SQLException ex) {
                // Rollback nếu có lỗi
                con.rollback();
                con.setAutoCommit(true);
                throw ex;
            }
            
        } catch (SQLException ex) {
            System.err.println("❌ Lỗi khi tạo hóa đơn:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tạo hóa đơn!\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tạo mã hóa đơn tự động
     */
    private String taoMaHoaDonTuDong() {
        String prefix = "HD";
        String dateStr = new java.text.SimpleDateFormat("yyMMdd").format(new java.util.Date());
        
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT COUNT(*) as soLuong FROM HoaDon " +
                     "WHERE maHoaDon LIKE ? AND CAST(ngayLapHoaDon AS DATE) = CAST(GETDATE() AS DATE)";
        
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, prefix + dateStr + "%");
            ResultSet rs = pstmt.executeQuery();
            
            int soLuong = 0;
            if (rs.next()) {
                soLuong = rs.getInt("soLuong");
            }
            
            rs.close();
            pstmt.close();
            
            String stt = String.format("%03d", soLuong + 1);
            return prefix + dateStr + stt;
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi tạo mã hóa đơn:");
            e.printStackTrace();
            return prefix + dateStr + String.format("%03d", (int)(Math.random() * 1000));
        }
    }
    
    /**
     * Lấy thông tin nhân viên đăng nhập
     */
    private String getNhanVienDangNhap() {
        // TODO: Lấy từ session
        return "NV001 - Nguyễn Minh Đức";
    }
    
    private String getMaNhanVienDangNhap() {
        // TODO: Lấy từ session
        return "NV001";
    }
    
    // ========== Helper methods ==========
    
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(40, 30));
        button.setBackground(MAIN_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    // font chữ in đậm
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }
    // font chữ thường
    private JLabel createNormalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, 
                            String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(component, gbc);
    }
    
  
}