package gui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DanhSachBanAnUI extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    
    // Components - Filter Panel
    private JTextField txtMaBan;
    private JTextField txtTenBan;
    private JComboBox<String> cboKhuVuc;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboLoaiBan;
    
    // Table
    private JTable tableBanAn;
    private DefaultTableModel tableModel;
    private JLabel lblThongTin;
    
    // Buttons
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXemChiTiet;
    private JButton btnCapNhat;
    private JButton btnThemBan;
    private JButton btnXoaBan;
    
    // Colors
    private final Color MAIN_COLOR = new Color(214, 116, 76);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    private final Color WARNING_COLOR = new Color(255, 152, 0);
    
    // TODO: DAO
    // private BanAnDAO banAnDAO;
    
    public DanhSachBanAnUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // TODO: Initialize DAO
        // banAnDAO = new BanAnDAO();
        
        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Load initial data
        loadDanhSachBanAn();
    }
    
    /**
     * Tạo panel tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel lblTitle = new JLabel("DANH SÁCH BÀN ĂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(MAIN_COLOR);
        panel.add(lblTitle);
        
        return panel;
    }
    
    /**
     * Tạo panel chính (chia 2 phần: filter + table)
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel bên trái: Filter
        panel.add(createFilterPanel(), BorderLayout.WEST);
        
        // Panel bên phải: Table
        panel.add(createTablePanel(), BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Tạo panel filter bên trái
     */
    private JPanel createFilterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAIN_COLOR, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.setPreferredSize(new Dimension(300, 0));
        
        // Title
        JLabel lblFilterTitle = new JLabel("Bộ lọc");
        lblFilterTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFilterTitle.setForeground(MAIN_COLOR);
        lblFilterTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblFilterTitle, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // Mã bàn
        txtMaBan = createTextField();
        addFilterField(formPanel, gbc, row++, "Mã bàn:", txtMaBan);
        
        // Tên bàn
        txtTenBan = createTextField();
        addFilterField(formPanel, gbc, row++, "Tên bàn:", txtTenBan);
        
        // Khu vực
        cboKhuVuc = createComboBox(new String[]{
            "-- Tất cả --", "Tầng 1", "Tầng 2", "Sân thượng"
        });
        addFilterField(formPanel, gbc, row++, "Khu vực:", cboKhuVuc);
        
        // Trạng thái
        cboTrangThai = createComboBox(new String[]{
            "-- Tất cả --", "Trống", "Đang sử dụng", "Đã đặt", "Đang dọn"
        });
        addFilterField(formPanel, gbc, row++, "Trạng thái:", cboTrangThai);
        
        // Loại bàn
        cboLoaiBan = createComboBox(new String[]{
            "-- Tất cả --", "Bàn vuông", "Bàn tròn", "Bàn đôi"
        });
        addFilterField(formPanel, gbc, row++, "Loại bàn:", cboLoaiBan);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        btnTimKiem = createButton("Tìm kiếm", MAIN_COLOR);
        btnLamMoi = createButton("Làm mới", new Color(100, 100, 100));
        
        btnTimKiem.addActionListener(this);
        btnLamMoi.addActionListener(this);
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Tạo panel bảng danh sách bàn
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        lblThongTin = new JLabel("Tổng số bàn: 0");
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(lblThongTin);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {
            "Mã bàn", "Tên bàn", "Khu vực", "Số chỗ", 
            "Loại bàn", "Trạng thái", "Ghi chú"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableBanAn = new JTable(tableModel);
        tableBanAn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableBanAn.setRowHeight(35);
        tableBanAn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBanAn.setShowGrid(true);
        tableBanAn.setGridColor(new Color(230, 230, 230));
        
        // Header style
        JTableHeader header = tableBanAn.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(MAIN_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        // Column widths
        tableBanAn.getColumnModel().getColumn(0).setPreferredWidth(100);  // Mã bàn
        tableBanAn.getColumnModel().getColumn(1).setPreferredWidth(120);  // Tên bàn
        tableBanAn.getColumnModel().getColumn(2).setPreferredWidth(120);  // Khu vực
        tableBanAn.getColumnModel().getColumn(3).setPreferredWidth(80);   // Số chỗ
        tableBanAn.getColumnModel().getColumn(4).setPreferredWidth(100);  // Loại bàn
        tableBanAn.getColumnModel().getColumn(5).setPreferredWidth(120);  // Trạng thái
        tableBanAn.getColumnModel().getColumn(6).setPreferredWidth(200);  // Ghi chú
        
        // Double click event
        tableBanAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    xemChiTiet();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableBanAn);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        
        
        return panel;
    }
    
    /**
     * Tạo panel nút chức năng
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        btnXemChiTiet = createButton("Xem chi tiết", MAIN_COLOR);
        btnXemChiTiet.setPreferredSize(new Dimension(140, 40));
        btnXemChiTiet.addActionListener(this);
        
        btnCapNhat = createButton("Cập nhật", WARNING_COLOR);
        btnCapNhat.setPreferredSize(new Dimension(140, 40));
        btnCapNhat.addActionListener(this);
        
        btnThemBan = createButton("Thêm bàn", SUCCESS_COLOR);
        btnThemBan.setPreferredSize(new Dimension(140, 40));
        btnThemBan.addActionListener(this);
        
        btnXoaBan = createButton("Xóa bàn", DANGER_COLOR);
        btnXoaBan.setPreferredSize(new Dimension(140, 40));
        btnXoaBan.addActionListener(this);
        
        panel.add(btnXemChiTiet);
        panel.add(btnCapNhat);
        panel.add(btnThemBan);
        panel.add(btnXoaBan);
        
        return panel;
    }
    
    /**
     * Thêm field vào filter
     */
    private void addFilterField(JPanel panel, GridBagConstraints gbc, int row, 
                                String labelText, JComponent component) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(label, gbc);
        
        // Component
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(component, gbc);
    }
    
    /**
     * Tạo text field
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }
    
    /**
     * Tạo combo box
     */
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        return comboBox;
    }
    
    /**
     * Tạo button
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // ==================== SỰ KIỆN ====================
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        
        if (source == btnTimKiem) {
            timKiem();
        } else if (source == btnLamMoi) {
            lamMoi();
        } else if (source == btnXemChiTiet) {
            xemChiTiet();
        } else if (source == btnCapNhat) {
            capNhat();
        } else if (source == btnThemBan) {
            themBan();
        } else if (source == btnXoaBan) {
            xoaBan();
        }
    }
    
    // ==================== CÁC PHƯƠNG THỨC XỬ LÝ ====================
    
    /**
     * Load danh sách bàn ăn
     * TODO: Kết nối database
     */
    private void loadDanhSachBanAn() {
        tableModel.setRowCount(0);
        
        // TODO: Load từ database
        // List<BanAn> dsBanAn = banAnDAO.getAllBanAn();
        
        // Dữ liệu demo
        Object[][] demoData = {
            {"B001", "Bàn 1", "Tầng 1", 4, "Bàn vuông", "Trống", ""},
            {"B002", "Bàn 2", "Tầng 1", 6, "Bàn tròn", "Đang sử dụng", "Khách VIP"},
            {"B003", "Bàn 3", "Tầng 2", 2, "Bàn đôi", "Trống", ""},
            {"B004", "Bàn 4", "Tầng 2", 4, "Bàn vuông", "Đã đặt", "Đặt lúc 18h"},
            {"B005", "Bàn 5", "Sân thượng", 8, "Bàn tròn", "Trống", "View đẹp"},
            {"B006", "Bàn 6", "Tầng 1", 6, "Bàn tròn", "Đang dọn", ""},
            {"B007", "Bàn 7", "Tầng 2", 4, "Bàn vuông", "Trống", ""},
            {"B008", "Bàn 8", "Sân thượng", 10, "Bàn tròn", "Đã đặt", "Tiệc công ty"},
            {"B009", "Bàn 9", "Tầng 1", 2, "Bàn đôi", "Đang sử dụng", ""},
            {"B010", "Bàn 10", "Tầng 2", 6, "Bàn tròn", "Trống", ""}
        };
        
        for (Object[] row : demoData) {
            tableModel.addRow(row);
        }
        
        capNhatThongTin();
    }
    
    /**
     * Tìm kiếm bàn
     * TODO: Implement logic tìm kiếm
     */
    private void timKiem() {
        String maBan = txtMaBan.getText().trim();
        String tenBan = txtTenBan.getText().trim();
        String khuVuc = (String) cboKhuVuc.getSelectedItem();
        String trangThai = (String) cboTrangThai.getSelectedItem();
        String loaiBan = (String) cboLoaiBan.getSelectedItem();
        
        // TODO: Query database
        // List<BanAn> ketQua = banAnDAO.timKiemBanAn(maBan, tenBan, khuVuc, trangThai, loaiBan);
        
        JOptionPane.showMessageDialog(this,
            "Chức năng tìm kiếm sẽ được cài đặt sau!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Làm mới filter
     */
    private void lamMoi() {
        txtMaBan.setText("");
        txtTenBan.setText("");
        cboKhuVuc.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        cboLoaiBan.setSelectedIndex(0);
        
        tableBanAn.clearSelection();
        loadDanhSachBanAn();
    }
    
    /**
     * Xem chi tiết bàn
     * TODO: Mở dialog chi tiết
     */
    private void xemChiTiet() {
        int selectedRow = tableBanAn.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một bàn để xem chi tiết!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
        
        // TODO: Lấy thông tin từ database và mở dialog
        // BanAn banAn = banAnDAO.getBanTheoMa(maBan);
        // new DialogChiTietBanAn((Frame) SwingUtilities.getWindowAncestor(this), banAn).setVisible(true);
        
        JOptionPane.showMessageDialog(this,
            "Xem chi tiết bàn: " + maBan + "\n\n" +
            "Chức năng sẽ được cài đặt sau!",
            "Chi tiết", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Cập nhật bàn ăn
     * TODO: Mở dialog cập nhật
     */
    private void capNhat() {
        int selectedRow = tableBanAn.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một bàn để cập nhật!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
        
        // TODO: Mở dialog cập nhật thông tin bàn
        // new DialogCapNhatBanAn((Frame) SwingUtilities.getWindowAncestor(this), maBan).setVisible(true);
        
        JOptionPane.showMessageDialog(this,
            "Cập nhật bàn: " + maBan + "\n\n" +
            "Chức năng sẽ được cài đặt sau!",
            "Cập nhật", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Thêm bàn mới
     * TODO: Mở dialog thêm bàn
     */
    private void themBan() {
        // TODO: Mở dialog thêm bàn ăn mới
        // new DialogThemBanAn((Frame) SwingUtilities.getWindowAncestor(this), 
        //     () -> loadDanhSachBanAn()).setVisible(true);
        
        JOptionPane.showMessageDialog(this,
            "Chức năng thêm bàn sẽ được cài đặt sau!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Xóa bàn
     * TODO: Xóa bàn khỏi database
     */
    private void xoaBan() {
        int selectedRow = tableBanAn.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một bàn để xóa!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
        String tenBan = tableModel.getValueAt(selectedRow, 1).toString();
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa bàn " + tenBan + " (" + maBan + ")?\n\n" +
            "Hành động này không thể hoàn tác!",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Xóa từ database
            // banAnDAO.xoaBan(maBan);
            
            JOptionPane.showMessageDialog(this,
                "Xóa bàn thành công!",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            loadDanhSachBanAn();
        }
    }
    
    /**
     * Cập nhật thông tin thống kê
     */
    private void capNhatThongTin() {
        int tongSo = tableModel.getRowCount();
        int banTrong = 0;
        int banDangSuDung = 0;
        int banDaDat = 0;
        int banDangDon = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String trangThai = tableModel.getValueAt(i, 5).toString();
            
            switch (trangThai) {
                case "Trống":
                    banTrong++;
                    break;
                case "Đang sử dụng":
                    banDangSuDung++;
                    break;
                case "Đã đặt":
                    banDaDat++;
                    break;
                case "Đang dọn":
                    banDangDon++;
                    break;
            }
        }
        
        lblThongTin.setText(String.format(
            "Tổng bàn: %d  |  Trống: %d  |  Đang dùng: %d  |  Đã đặt: %d  |  Đang dọn: %d",
            tongSo, banTrong, banDangSuDung, banDaDat, banDangDon
        ));
    }
}