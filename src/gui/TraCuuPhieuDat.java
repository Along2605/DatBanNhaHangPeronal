package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import dao.PhieuDatBanDAO;
import entity.PhieuDatBan;

public class TraCuuPhieuDat extends JPanel implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;
    
    // Components tìm kiếm
    private JTextField txtMaPhieu;
    private JTextField txtSDTKhachHang;
    private JTextField txtTenKhachHang;
    private JComboBox<String> cboTrangThai;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JComboBox<String> cboKhungGio;
    
    private JTable tablePhieuDat;
    private DefaultTableModel tableModel;
    
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXemChiTiet;
    private JButton btnXacNhan;
    private JButton btnHuyPhieu;
    
    private JLabel lblThongTin;
    
    private PhieuDatBanDAO phieuDatDAO;
    
    // Colors
    private final Color MAIN_COLOR = new Color(214, 116, 76);
    private final Color HOVER_COLOR = new Color(234, 136, 96);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color WARNING_COLOR = new Color(255, 152, 0);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    
    // Formatters
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    
    public TraCuuPhieuDat() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        phieuDatDAO = new PhieuDatBanDAO();
        
        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSearchPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Load initial data
        loadDanhSachPhieuDat();
    }
    
    /**
     * Tạo panel tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel lblTitle = new JLabel("TRA CỨU PHIẾU ĐẶT BÀN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(MAIN_COLOR);
        panel.add(lblTitle);
        
        return panel;
    }
    
    /**
     * Tạo panel tìm kiếm bên trái
     */
    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAIN_COLOR, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.setPreferredSize(new Dimension(320, 0));
        
        // Title
        JLabel lblSearchTitle = new JLabel("Tiêu chí tìm kiếm");
        lblSearchTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSearchTitle.setForeground(MAIN_COLOR);
        lblSearchTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblSearchTitle, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // Mã phiếu
        addFormField(formPanel, gbc, row++, "Mã phiếu:", txtMaPhieu = createTextField());
        
        // SĐT khách hàng
        addFormField(formPanel, gbc, row++, "SĐT khách hàng:", txtSDTKhachHang = createTextField());
        
        // Tên khách hàng
        addFormField(formPanel, gbc, row++, "Tên khách hàng:", txtTenKhachHang = createTextField());
        
        // Trạng thái (3 trạng thái chuẩn của phiếu đặt)
        cboTrangThai = createComboBox(new String[]{
            "-- Tất cả --", "Chờ xác nhận", "Đã xác nhận", "Đã hủy"
        });
        addFormField(formPanel, gbc, row++, "Trạng thái:", cboTrangThai);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JSeparator separator1 = new JSeparator();
        separator1.setBorder(new EmptyBorder(10, 0, 10, 0));
        formPanel.add(separator1, gbc);
        gbc.gridwidth = 1;
        
        // Label thời gian
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel lblTimeFilter = new JLabel("Lọc theo thời gian đặt");
        lblTimeFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTimeFilter.setForeground(MAIN_COLOR);
        formPanel.add(lblTimeFilter, gbc);
        gbc.gridwidth = 1;
        
        // Từ ngày
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateFrom.setPreferredSize(new Dimension(0, 30));
        addFormField(formPanel, gbc, row++, "Từ ngày:", dateFrom);
        
        // Đến ngày
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setDate(new Date()); // Mặc định hôm nay
        dateTo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateTo.setPreferredSize(new Dimension(0, 30));
        addFormField(formPanel, gbc, row++, "Đến ngày:", dateTo);
        
        // Khung giờ
        cboKhungGio = createComboBox(new String[]{
            "-- Tất cả --", "Sáng (6-11h)", "Trưa (11-14h)", "Chiều (14-17h)", "Tối (17-22h)"
        });
        addFormField(formPanel, gbc, row++, "Khung giờ:", cboKhungGio);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        btnTimKiem = createButton("Tìm kiếm", MAIN_COLOR);
        btnLamMoi = createButton("Làm mới", new Color(100, 100, 100));
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        btnTimKiem.addActionListener(this);
        btnLamMoi.addActionListener(this);
        
        return mainPanel;
    }
    
    /**
     * Tạo panel bảng dữ liệu
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Top bar
        JPanel topBarPanel = new JPanel(new BorderLayout(10, 0));
        topBarPanel.setBackground(Color.WHITE);
        topBarPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        lblThongTin = new JLabel("Tổng số phiếu: 0");
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topBarPanel.add(lblThongTin, BorderLayout.WEST);
        
        // Lọc nhanh
        JPanel quickFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        quickFilterPanel.setBackground(Color.WHITE);
        
        JButton btnHomNay = new JButton("Hôm nay");
        btnHomNay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHomNay.setBackground(new Color(240, 240, 240));
        btnHomNay.setFocusPainted(false);
        btnHomNay.addActionListener(e -> {
            dateFrom.setDate(new Date());
            dateTo.setDate(new Date());
            timKiemPhieuDat();
        });
        
        JButton btnTuanNay = new JButton("Tuần này");
        btnTuanNay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTuanNay.setBackground(new Color(240, 240, 240));
        btnTuanNay.setFocusPainted(false);
        btnTuanNay.addActionListener(e -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
            dateFrom.setDate(cal.getTime());
            dateTo.setDate(new Date());
            timKiemPhieuDat();
        });
        
        quickFilterPanel.add(btnHomNay);
        quickFilterPanel.add(btnTuanNay);
        
        topBarPanel.add(quickFilterPanel, BorderLayout.EAST);
        
        panel.add(topBarPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {
            "Mã phiếu", "Khách hàng", "SĐT", "Ngày đặt", "Khung giờ",
            "Số người", "Tiền cọc", "Trạng thái", "Ghi chú"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablePhieuDat = new JTable(tableModel);
        tablePhieuDat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablePhieuDat.setRowHeight(35);
        tablePhieuDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhieuDat.setShowGrid(true);
        tablePhieuDat.setGridColor(new Color(230, 230, 230));
        
        // Header style
        JTableHeader header = tablePhieuDat.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(MAIN_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Column widths
        tablePhieuDat.getColumnModel().getColumn(0).setPreferredWidth(100);  // Mã phiếu
        tablePhieuDat.getColumnModel().getColumn(1).setPreferredWidth(150);  // Khách hàng
        tablePhieuDat.getColumnModel().getColumn(2).setPreferredWidth(110);  // SĐT
        tablePhieuDat.getColumnModel().getColumn(3).setPreferredWidth(140);  // Ngày đặt
        tablePhieuDat.getColumnModel().getColumn(4).setPreferredWidth(120);  // Khung giờ
        tablePhieuDat.getColumnModel().getColumn(5).setPreferredWidth(80);   // Số người
        tablePhieuDat.getColumnModel().getColumn(6).setPreferredWidth(110);  // Tiền cọc
        tablePhieuDat.getColumnModel().getColumn(7).setPreferredWidth(120);  // Trạng thái
        tablePhieuDat.getColumnModel().getColumn(8).setPreferredWidth(180);  // Ghi chú
        
        JScrollPane scrollPane = new JScrollPane(tablePhieuDat);
        tablePhieuDat.addMouseListener(this);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Tạo panel nút chức năng
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        btnXemChiTiet = createButton("Xem chi tiết", MAIN_COLOR);
        btnXemChiTiet.setPreferredSize(new Dimension(150, 40));
        
        btnXacNhan = createButton("Xác nhận phiếu", SUCCESS_COLOR);
        btnXacNhan.setPreferredSize(new Dimension(150, 40));
        
        btnHuyPhieu = createButton("Hủy phiếu", DANGER_COLOR);
        btnHuyPhieu.setPreferredSize(new Dimension(150, 40));
        
        panel.add(btnXemChiTiet);
        panel.add(btnXacNhan);
        panel.add(btnHuyPhieu);
        
        btnXemChiTiet.addActionListener(this);
        btnXacNhan.addActionListener(this);
        btnHuyPhieu.addActionListener(this);
        
        return panel;
    }
    
    /**
     * Thêm field vào form
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
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
     * Tạo text field với style đồng nhất
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
     * Tạo combo box với style đồng nhất
     */
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        return comboBox;
    }
    
    /**
     * Tạo button với style đồng nhất
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 35));
        
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
    
    /**
     * Load danh sách phiếu đặt từ database
     */
    private void loadDanhSachPhieuDat() {
        try {
            tableModel.setRowCount(0);
            
            // ✅ Lấy TẤT CẢ phiếu đặt
            List<PhieuDatBan> dsPhieu = phieuDatDAO.getAllPhieuDat();
            
            if (dsPhieu == null || dsPhieu.isEmpty()) {
                capNhatThongTin();
                return;
            }
            
            // ✅ Hiển thị tất cả phiếu đặt
            for (PhieuDatBan phieu : dsPhieu) {
                addPhieuToTable(phieu);
            }
            
            capNhatThongTin();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Lấy tên khung giờ từ ID
     */
    private String getTenKhungGio(int khungGio) {
        switch (khungGio) {
            case 1: return "Sáng (6-11h)";
            case 2: return "Trưa (11-14h)";
            case 3: return "Chiều (14-17h)";
            case 4: return "Tối (17-22h)";
            default: return "N/A";
        }
    }
    
    /**
     * Tìm kiếm phiếu đặt theo điều kiện
     */
    /**
     * Tìm kiếm phiếu đặt theo điều kiện
     */
    private void timKiemPhieuDat() {
        try {
            String maPhieu = txtMaPhieu.getText().trim();
            String sdt = txtSDTKhachHang.getText().trim();
            String tenKH = txtTenKhachHang.getText().trim();
            String trangThai = cboTrangThai.getSelectedItem().toString();
            int khungGio = cboKhungGio.getSelectedIndex(); // 0 = tất cả
            
            Date fromDate = dateFrom.getDate();
            Date toDate = dateTo.getDate();
            
            LocalDateTime fromDateTime = null;
            LocalDateTime toDateTime = null;
            
            if (fromDate != null) {
                // ✅ Set về 00:00:00 của ngày đó
                fromDateTime = new java.sql.Timestamp(fromDate.getTime())
                    .toLocalDateTime()
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);
            }
            if (toDate != null) {
                // ✅ Set về 23:59:59 của ngày đó
                toDateTime = new java.sql.Timestamp(toDate.getTime())
                    .toLocalDateTime()
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59);
            }
            
            // Lấy tất cả phiếu
            List<PhieuDatBan> dsPhieu = phieuDatDAO.getAllPhieuDat();
            
            if (dsPhieu == null) {
                dsPhieu = new ArrayList<>();
            }
            
            // Lọc theo điều kiện
            final LocalDateTime finalFromDate = fromDateTime;
            final LocalDateTime finalToDate = toDateTime;
            
            List<PhieuDatBan> ketQua = dsPhieu.stream().filter(phieu -> {
                boolean match = true;
                
                // Lọc mã phiếu
                if (!maPhieu.isEmpty()) {
                    match = match && phieu.getMaPhieuDat().toLowerCase().contains(maPhieu.toLowerCase());
                }
                
                // Lọc SĐT
                if (!sdt.isEmpty() && phieu.getKhachHang() != null) {
                    match = match && phieu.getKhachHang().getSdt().contains(sdt);
                }
                
                // Lọc tên khách hàng
                if (!tenKH.isEmpty() && phieu.getKhachHang() != null) {
                    match = match && phieu.getKhachHang().getHoTen().toLowerCase()
                        .contains(tenKH.toLowerCase());
                }
                
                // Lọc trạng thái
                if (!trangThai.equals("-- Tất cả --")) {
                    match = match && phieu.getTrangThai().equals(trangThai);
                }
                
                // Lọc khung giờ
                if (khungGio > 0) {
                    match = match && phieu.getKhungGio() == khungGio;
                }
                
                // Lọc ngày
                if (finalFromDate != null) {
                    match = match && !phieu.getNgayDat().isBefore(finalFromDate);
                }
                if (finalToDate != null) {
                    match = match && !phieu.getNgayDat().isAfter(finalToDate);
                }
                
                return match;
            }).collect(Collectors.toList());
            
            // Cập nhật bảng
            tableModel.setRowCount(0);
            for (PhieuDatBan phieu : ketQua) {
                addPhieuToTable(phieu);
            }
            
            capNhatThongTin();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void addPhieuToTable(PhieuDatBan phieu) {
        String tenKhachHang = phieu.getKhachHang() != null 
            ? phieu.getKhachHang().getHoTen() 
            : "Khách vãng lai";
            
        String sdt = phieu.getKhachHang() != null 
            ? phieu.getKhachHang().getSdt() 
            : "N/A";
            
        String ngayDat = phieu.getNgayDat().format(dtf);
        String khungGio = getTenKhungGio(phieu.getKhungGio());
        String tienCoc = currencyFormat.format(phieu.getSoTienCoc()) + "đ";
        
        tableModel.addRow(new Object[]{
            phieu.getMaPhieuDat(),
            tenKhachHang,
            sdt,
            ngayDat,
            khungGio,
            phieu.getSoNguoi(),
            tienCoc,
            phieu.getTrangThai(),
            phieu.getGhiChu() != null ? phieu.getGhiChu() : ""
        });
    }

	/**
     * Làm mới form
     */
    private void lamMoiForm() {
        // Reset các trường tìm kiếm
        txtMaPhieu.setText("");
        txtSDTKhachHang.setText("");
        txtTenKhachHang.setText("");
        cboTrangThai.setSelectedIndex(0);
        cboKhungGio.setSelectedIndex(0);
        
        // ✅ Reset date picker về null
        dateFrom.setDate(null);
        dateTo.setDate(null);
        
        // ✅ Load lại TẤT CẢ phiếu đặt
        loadDanhSachPhieuDat();
    }
    
    
    /**
     * Xem chi tiết phiếu đặt
     */
    private void xemChiTietPhieuDat() {
        int selectedRow = tablePhieuDat.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một phiếu đặt để xem chi tiết!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maPhieu = tableModel.getValueAt(selectedRow, 0).toString();
        
        try {
            PhieuDatBan phieu = phieuDatDAO.timPhieuTheoMaPhieuDat(maPhieu);
            
            if (phieu == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy thông tin phiếu đặt!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<String> danhSachBan = phieuDatDAO.getDanhSachBanTheoPhieuDat(maPhieu);
            
            if (danhSachBan == null || danhSachBan.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Phiếu đặt này không có thông tin bàn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            dao.BanAnDAO banAnDAO = new dao.BanAnDAO();
            entity.BanAn banDauTien = banAnDAO.getBanTheoMa(danhSachBan.get(0));
            
            if (banDauTien == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy thông tin bàn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            DialogChiTietPhieuDat dialog = new DialogChiTietPhieuDat(
                (Frame) SwingUtilities.getWindowAncestor(this),
                banDauTien,
                phieu,
                danhSachBan
            );
            
            dialog.setVisible(true);
            loadDanhSachPhieuDat();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xem chi tiết: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xác nhận phiếu đặt
     * TODO: Sẽ xử lý logic sau
     */
    private void xacNhanPhieu() {
        int selectedRow = tablePhieuDat.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn phiếu đặt cần xác nhận!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String trangThai = tableModel.getValueAt(selectedRow, 7).toString();
        
        if (!"Chờ xác nhận".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                "Chỉ có thể xác nhận phiếu ở trạng thái 'Chờ xác nhận'!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Chức năng xác nhận phiếu sẽ được triển khai sau!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Hủy phiếu đặt
     */
    private void huyPhieu() {
        int selectedRow = tablePhieuDat.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn phiếu đặt cần hủy!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maPhieu = tableModel.getValueAt(selectedRow, 0).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 7).toString();
        
        if ("Đã hủy".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                "Phiếu đặt này đã bị hủy trước đó!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if ("Khách không đến".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                "Phiếu đặt này đã được xử lý 'Khách không đến'!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận hủy phiếu đặt?\n\n" +
            "Mã phiếu: " + maPhieu + "\n" +
            "⚠️ Hành động này không thể hoàn tác!",
            "Xác nhận hủy", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Chức năng hủy phiếu sẽ được triển khai sau!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Cập nhật thông tin thống kê
     */
    private void capNhatThongTin() {
        int tongSo = tableModel.getRowCount();
        int choXacNhan = 0;
        int daXacNhan = 0;
        int daHuy = 0;
        int khachKhongDen = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String trangThai = tableModel.getValueAt(i, 7).toString();
            
            switch (trangThai) {
                case "Chờ xác nhận":
                    choXacNhan++;
                    break;
                case "Đã xác nhận":
                    daXacNhan++;
                    break;
                case "Đã hủy":
                    daHuy++;
                    break;
                case "Khách không đến":
                    khachKhongDen++;
                    break;
            }
        }
        
        lblThongTin.setText(String.format(
            "Tổng số phiếu: %d  |  Chờ xác nhận: %d  |  Đã xác nhận: %d  |  Đã hủy: %d  |  Khách không đến: %d",
            tongSo, choXacNhan, daXacNhan, daHuy, khachKhongDen
        ));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if (o == btnTimKiem) {
            timKiemPhieuDat();
        } else if (o == btnLamMoi) {
            lamMoiForm();
        } else if (o == btnXemChiTiet) {
            xemChiTietPhieuDat();
        } else if (o == btnXacNhan) {
            xacNhanPhieu();
        } else if (o == btnHuyPhieu) {
            huyPhieu();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Double click để xem chi tiết
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            xemChiTietPhieuDat();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}