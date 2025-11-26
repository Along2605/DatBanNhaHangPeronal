package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.BanAnDAO;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.PhieuDatBanDAO;
import entity.BanAn;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhuyenMai;
import util.Session;

import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DialogChiTietHoaDon extends JDialog {
    
    private static final long serialVersionUID = 1L;
    private static final Color MAIN_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    
    private BanAn banAn;
    private JTable tableMonAn;
    private DefaultTableModel tableModel;
    
    private JLabel lblMaHoaDon, lblNgayLap, lblNhanVien, lblBan, lblKhuVuc;
    private JLabel lblKhachHang, lblSdt, lblDiemTichLuy;
    // Labels để cập nhật giá trị
    private JLabel lblTongTienMonValue;
    private JLabel lblVATValue;
    private JLabel lblGiamGiaValue;
    private JLabel lblTienCocValue;
    private JLabel lblThanhToanValue;
    
    private JComboBox<String> cboKhuyenMai;
    private JButton btnThemMon;
    private JButton btnXoaMon;
	private HoaDonDAO hoaDonDAO;
	private ChiTietHoaDonDAO chiTietHoaDonDAO;
	
	private HoaDon hoaDon;
	private boolean daThanhToan = false;
	public boolean isDaThanhToan() { return daThanhToan; }
	
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private JTextArea txtGhiChu;
    
	// =================== CONSTRUCTOR CHÍNH – DÙNG CHO MỌI TRƯỜNG HỢP ===================
    public DialogChiTietHoaDon(Frame parent, HoaDon hoaDon) {
        super(parent, "Chi tiết hóa đơn", true);
        this.hoaDon = hoaDon;
        this.banAn = (hoaDon != null) ? hoaDon.getBanAn() : null;
        initUIAndLoadData(parent);
    }

    // =================== CONSTRUCTOR CŨ – MỞ TỪ SƠ ĐỒ BÀN ===================
    public DialogChiTietHoaDon(Frame parent, BanAn banAn) {
        super(parent, "Chi tiết hóa đơn", true);
        this.banAn = banAn;
        initUIAndLoadData(parent);
    }

    // =================== KHỞI TẠO GIAO DIỆN + LOAD DỮ LIỆU (TRÁNH LỖI NULL) ===================
    private void initUIAndLoadData(Frame parent) {
        setSize(1050, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents();           // Tạo toàn bộ UI trước (rất quan trọng!)
        loadThongTinHoaDon();       // Sau đó mới load dữ liệu → an toàn 100%
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Tiêu đề + trạng thái
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(MAIN_COLOR);
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        JLabel lblStatus = new JLabel("ĐANG PHỤC VỤ");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBackground(SUCCESS_COLOR);
        lblStatus.setOpaque(true);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 20),
            new EmptyBorder(5, 15, 5, 15)
        ));
        titlePanel.add(lblStatus, BorderLayout.EAST);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createThongTinPanel(), BorderLayout.NORTH);
        contentPanel.add(createMonAnPanel(), BorderLayout.CENTER);     // ← ĐÃ BỎ loadDanhSachMonAn() ở đây
        contentPanel.add(createTinhTienPanel(), BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    /**
     * Tạo panel thông tin hóa đơn
     */
    private JPanel createThongTinPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(MAIN_COLOR, 2),
                "Thông tin hóa đơn",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                MAIN_COLOR
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
     // Khởi tạo các DAO
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO= new ChiTietHoaDonDAO();
        
        // Dòng 1:
        JPanel row1 = new JPanel(new GridLayout(1, 5, 15, 0));
        row1.setBackground(Color.WHITE);
        
        lblMaHoaDon = new JLabel("...");
        lblNgayLap = new JLabel("...");
        lblNhanVien = new JLabel("...");
        lblBan = new JLabel("..."); 
        lblKhuVuc = new JLabel("..."); 
        
        row1.add(createInfoField("Mã hóa đơn:", lblMaHoaDon));
        row1.add(createInfoField("Ngày lập:", lblNgayLap));
        row1.add(createInfoField("Nhân viên:", lblNhanVien));
        row1.add(createInfoField("Bàn:", lblBan));
        row1.add(createInfoField("Khu vực:", lblKhuVuc));
        
        // Dòng 2: Khởi tạo các JLabel
        JPanel row2 = new JPanel(new GridLayout(1, 5, 15, 0));
        row2.setBackground(Color.WHITE);
        
        lblKhachHang = new JLabel("...");
        lblSdt = new JLabel("...");
        lblDiemTichLuy = new JLabel("...");
       
        
        row2.add(createInfoField("Khách hàng:", lblKhachHang));
        row2.add(createInfoField("SĐT:", lblSdt));
        row2.add(createInfoField("Điểm tích lũy:", lblDiemTichLuy));
       
        
        panel.add(row1);
        panel.add(row2);
        
        return panel;
    }
    
   
//    Tạo panel danh sách món ăn
     
 // =================== TẠO CÁC PANEL (giữ nguyên, chỉ bỏ loadDanhSachMonAn() trong createMonAnPanel) ===================
    private JPanel createMonAnPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(new LineBorder(MAIN_COLOR, 2), "Danh sách món ăn",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), MAIN_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.WHITE);

        btnThemMon = createSmallButton("Thêm món", SUCCESS_COLOR);
        btnThemMon.addActionListener(e -> themMon());

        btnXoaMon = createSmallButton("Xóa món", DANGER_COLOR);
        btnXoaMon.addActionListener(e -> xoaMon());

        topPanel.add(btnThemMon);
        topPanel.add(btnXoaMon);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Trạng thái", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tableMonAn = new JTable(tableModel);
        // ... cấu hình table như cũ ...

        JScrollPane scrollPane = new JScrollPane(tableMonAn);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

     
        return panel;
    }
    
    
//    Load danh sách món ăn từ ChiTietHoaDon vào table   
    private void loadDanhSachMonAn() {
        tableModel.setRowCount(0); 
        
        if (hoaDon == null) {
            return;
        }
        
        try {
            // Lấy danh sách chi tiết hóa đơn từ database
            List<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.layDanhSachTheoHoaDon(hoaDon.getMaHoaDon());
            
            int stt = 1;
            double tongTien = 0;
            
            for (ChiTietHoaDon cthd : danhSachChiTiet) {
                Object[] row = new Object[7];
                row[0] = stt++;
                row[1] = cthd.getMonAn().getTenMon();
                row[2] = formatCurrency(cthd.getDonGia());
                row[3] = cthd.getSoLuong();
                row[4] = formatCurrency(cthd.getThanhTien());
                row[5] = "Đã phục vụ"; // Hoặc lấy từ trường trạng thái nếu có
                row[6] = cthd.getGhiChu() != null ? cthd.getGhiChu() : "";
                
                tableModel.addRow(row);
                tongTien += cthd.getThanhTien();
            }
            
            // Cập nhật tổng tiền
            capNhatTongTien(tongTien);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi load danh sách món: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // format số tiền
    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount) + "đ";
    }

    /**
     * Cập nhật tổng tiền hiển thị trên UI
     */
    private void capNhatTongTien(double tongTienMon) {
        if (lblTongTienMonValue == null) return; // Bảo vệ thêm

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        lblTongTienMonValue.setText(currencyFormat.format(tongTienMon) + "đ");

        double phanTramVAT = hoaDon != null ? hoaDon.getThueVAT() : 0.1;
        double tienVAT = tongTienMon * phanTramVAT;
        lblVATValue.setText(currencyFormat.format(tienVAT) + "đ");

        double tongSauVAT = tongTienMon + tienVAT;
        double tienGiamGia = 0;
        String km = (String) cboKhuyenMai.getSelectedItem();
        if (km != null && !km.contains("Không áp dụng")) {
            if (km.contains("10%")) tienGiamGia = tongSauVAT * 0.1;
            else if (km.contains("20%")) tienGiamGia = tongSauVAT * 0.2;
            else if (km.contains("50,000")) tienGiamGia = 50000;
        }
        lblGiamGiaValue.setText("- " + currencyFormat.format(tienGiamGia) + "đ");

        double tongCong = tongSauVAT - tienGiamGia;
        double tienCoc = hoaDon != null ? hoaDon.getTienCoc() : 0;
        lblTienCocValue.setText(tienCoc > 0 ? "- " + currencyFormat.format(tienCoc) + "đ" : "0đ");

        double thanhToanThucTe = tongCong - tienCoc;
        lblThanhToanValue.setText(currencyFormat.format(thanhToanThucTe) + "đ");

        if (hoaDon != null) hoaDon.setTongTien(tongCong);
    }

	/**
     * Tạo panel tính tiền
     */
    private JPanel createTinhTienPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Panel bên trái: Khuyến mãi và ghi chú
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        leftPanel.setBackground(Color.WHITE);
        
        // Khuyến mãi
        JPanel khuyenMaiPanel = new JPanel(new BorderLayout(10, 5));
        khuyenMaiPanel.setBackground(Color.WHITE);
        khuyenMaiPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(WARNING_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblKhuyenMai = new JLabel("🎁 Áp dụng khuyến mãi:");
        lblKhuyenMai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        khuyenMaiPanel.add(lblKhuyenMai, BorderLayout.NORTH);
        
        cboKhuyenMai = new JComboBox<>(new String[]{
            "-- Không áp dụng --",
            "Giảm 10% - Khách hàng thân thiết",
            "Giảm 50,000đ - Hóa đơn trên 500k",
            "Giảm 20% - Sinh nhật"
        });
        cboKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboKhuyenMai.addActionListener(e -> tinhTongTien());
        khuyenMaiPanel.add(cboKhuyenMai, BorderLayout.CENTER);
        
        // Ghi chú
        JPanel ghiChuPanel = new JPanel(new BorderLayout(5, 5));
        ghiChuPanel.setBackground(Color.WHITE);
        ghiChuPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ghiChuPanel.add(lblGhiChu, BorderLayout.NORTH);
        
        txtGhiChu = new JTextArea(2, 30);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setBorder(null);
        ghiChuPanel.add(scrollGhiChu, BorderLayout.CENTER);
        
        leftPanel.add(khuyenMaiPanel);
        leftPanel.add(ghiChuPanel);
        
        // Panel bên phải: Tính tiền
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 250, 255));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAIN_COLOR, 3),
            new EmptyBorder(20, 25, 20, 25)
        ));
        rightPanel.setPreferredSize(new Dimension(400, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Tổng tiền món
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        JLabel lblTongTienMon = new JLabel("Tổng tiền món:");
        lblTongTienMon.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        rightPanel.add(lblTongTienMon, gbc);
        
        gbc.gridx = 1;
        lblTongTienMonValue = new JLabel("850,000đ");
        lblTongTienMonValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTienMonValue.setHorizontalAlignment(JLabel.RIGHT);
        rightPanel.add(lblTongTienMonValue, gbc);
        
        // VAT (10%)
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblVAT = new JLabel("VAT (10%):");
        lblVAT.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        rightPanel.add(lblVAT, gbc);
        
        gbc.gridx = 1;
        lblVATValue = new JLabel("85,000đ");
        lblVATValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblVATValue.setHorizontalAlignment(JLabel.RIGHT);
        rightPanel.add(lblVATValue, gbc);
        
        // Giảm giá
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblGiamGia = new JLabel("Giảm giá:");
        lblGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblGiamGia.setForeground(DANGER_COLOR);
        rightPanel.add(lblGiamGia, gbc);
       
        
        gbc.gridx = 1;
        lblGiamGiaValue = new JLabel("- 0đ");
        lblGiamGiaValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGiamGiaValue.setHorizontalAlignment(JLabel.RIGHT);
        lblGiamGiaValue.setForeground(DANGER_COLOR);
        rightPanel.add(lblGiamGiaValue, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3; // Dòng mới
        JLabel lblTienCoc = new JLabel("Tiền cọc:");
        lblTienCoc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTienCoc.setForeground(new Color(52, 152, 219)); // Màu xanh dương
        rightPanel.add(lblTienCoc, gbc);
        
        gbc.gridx = 1;
        lblTienCocValue = new JLabel("- 0đ");
        lblTienCocValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTienCocValue.setHorizontalAlignment(JLabel.RIGHT);
        lblTienCocValue.setForeground(new Color(52, 152, 219));
        rightPanel.add(lblTienCocValue, gbc);
        // Separator
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 0, 12, 0);
        JSeparator separator = new JSeparator();
        separator.setForeground(MAIN_COLOR);
        separator.setPreferredSize(new Dimension(0, 2));
        rightPanel.add(separator, gbc);
        
        // Thành tiền
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 0);
        JLabel lblThanhToan = new JLabel("THÀNH TIỀN:");
        lblThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblThanhToan.setForeground(MAIN_COLOR);
        rightPanel.add(lblThanhToan, gbc);
        
        gbc.gridx = 1;
        lblThanhToanValue = new JLabel("935,000đ");
        lblThanhToanValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblThanhToanValue.setHorizontalAlignment(JLabel.RIGHT);
        lblThanhToanValue.setForeground(new Color(211, 47, 47));
        rightPanel.add(lblThanhToanValue, gbc);
        
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        return mainPanel;
    }
    
    /**
     * Tạo panel nút
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);
        
        JButton btnThanhToan = createButton("Thanh toán", SUCCESS_COLOR);
        btnThanhToan.setPreferredSize(new Dimension(180, 45));
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnThanhToan.addActionListener(e -> thanhToan());
        
        JButton btnInTamTinh = createButton("In tạm tính", new Color(103, 58, 183));
        btnInTamTinh.setPreferredSize(new Dimension(180, 45));
//        btnInTamTinh.addActionListener(e -> inTamTinh());
        
        JButton btnChuyenBan = createButton("Chuyển bàn", WARNING_COLOR);
        btnChuyenBan.setPreferredSize(new Dimension(180, 45));
//        btnChuyenBan.addActionListener(e -> chuyenBan());
        
        JButton btnDong = createButton("Đóng", new Color(100, 100, 100));
        btnDong.setPreferredSize(new Dimension(180, 45));
        btnDong.addActionListener(e -> dispose());
        
        panel.add(btnThanhToan);
        panel.add(btnInTamTinh);
        panel.add(btnChuyenBan);
        panel.add(btnDong);
        
        return panel;
    }
    
    /**
     * Tạo field thông tin
     */
    private JPanel createInfoField(String label, Component valueComponent) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLabel.setForeground(Color.GRAY);

        // Set font chuẩn cho component giá trị
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        panel.add(lblLabel, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Tạo button
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
    
    /**
     * Tạo button nhỏ
     */
    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 32));
        return button;
    }
    
 // =================== LOAD THÔNG TIN HÓA ĐƠN – HỖ TRỢ CẢ MANG VỀ ===================
    private void loadThongTinHoaDon() {
        if (hoaDon == null && banAn != null) {
            // Tìm hóa đơn chưa thanh toán theo bàn
            hoaDon = hoaDonDAO.getHoaDonChuaThanhToanTheoBan(banAn.getMaBan());
            if (hoaDon == null) {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Bàn " + banAn.getTenBan() + " chưa có hóa đơn.\nTạo mới?",
                    "Tạo hóa đơn", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    hoaDon = taoHoaDonMoiChoBan(banAn);
                }
                if (hoaDon == null) {
                    dispose();
                    return;
                }
            }
        }

        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this, "Không có hóa đơn để hiển thị!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // === HIỂN THỊ THÔNG TIN ===
        lblMaHoaDon.setText(hoaDon.getMaHoaDon());
        lblNgayLap.setText(hoaDon.getNgayLapHoaDon().format(dtf));
        lblNhanVien.setText(hoaDon.getNhanVien() != null 
            ? hoaDon.getNhanVien().getHoTen() + " - " + hoaDon.getNhanVien().getMaNV() : "N/A");

        if (banAn != null) {
            lblBan.setText(banAn.getTenBan());
            lblKhuVuc.setText(banAn.getKhuVuc() != null ? banAn.getKhuVuc().getTenKhuVuc() : "—");
        } else {
            lblBan.setText("MANG VỀ");
            lblKhuVuc.setText("—");
        }

        if (hoaDon.getKhachHang() != null) {
            lblKhachHang.setText(hoaDon.getKhachHang().getHoTen());
            lblSdt.setText(hoaDon.getKhachHang().getSdt());
            lblDiemTichLuy.setText(hoaDon.getKhachHang().getDiemTichLuy() + " điểm");
        } else {
            lblKhachHang.setText("Khách vãng lai");
            lblSdt.setText("—");
            lblDiemTichLuy.setText("0 điểm");
        }

        // === BÂY GIỜ MỚI LOAD MÓN + TÍNH TIỀN ===
        loadDanhSachMonAn();
        tinhTongTien();
    }
    
    
    
    
    /**
     * Tính tổng tiền
     */
    private void tinhTongTien() {
        if (hoaDon == null) {
            return;
        }
        
        try {
            // ✅ Lấy danh sách chi tiết từ database
            List<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.layDanhSachTheoHoaDon(hoaDon.getMaHoaDon());
            
            double tongTienMon = 0;
            for (ChiTietHoaDon cthd : danhSachChiTiet) {
                tongTienMon += cthd.getThanhTien();
            }
            
            // Gọi method capNhatTongTien đã có
            capNhatTongTien(tongTienMon);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi thì dùng giá trị mặc định
            capNhatTongTien(0);
        }
    }

    
    
 // Sửa method themMon() để có callback
    private void themMon() {
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy hóa đơn!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialogDatMon = new JDialog(this, "Đặt món", true);
        dialogDatMon.setSize(1200, 700);
        dialogDatMon.setLocationRelativeTo(this);
        
        // ← TẠO CALLBACK ĐÚNG CÁCH - Sử dụng reference tới DialogChiTietHoaDon.this
        Runnable callback = () -> {
            SwingUtilities.invokeLater(() -> {
                loadDanhSachMonAn();
                tinhTongTien();
            });
        };
        
        ManHinhDatMon manHinhDatMon = new ManHinhDatMon(hoaDon, callback);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setBackground(new Color(158, 158, 158));
        btnDong.setForeground(Color.WHITE);
        btnDong.setPreferredSize(new Dimension(150, 40));
        btnDong.setFocusPainted(false);
        btnDong.addActionListener(e -> {
            // ← CẬP NHẬT LẠI KHI ĐÓNG DIALOG (PHÒNG TRƯỜNG HỢP CALLBACK BỊ LỖI)
            loadDanhSachMonAn();
            tinhTongTien();
            dialogDatMon.dispose();
        });
        
        buttonPanel.add(btnDong);
        
        dialogDatMon.setLayout(new BorderLayout());
        dialogDatMon.add(manHinhDatMon, BorderLayout.CENTER);
        dialogDatMon.add(buttonPanel, BorderLayout.SOUTH);
        
        dialogDatMon.setVisible(true);
    }
    
    /**
     * Xóa món
     * TODO: Xóa món đã chọn
     */
    private void xoaMon() {
        int selectedRow = tableMonAn.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn món cần xóa!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy hóa đơn!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String tenMon = tableModel.getValueAt(selectedRow, 1).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 5).toString();
        
        // Kiểm tra trạng thái
        if ("Đã phục vụ".equals(trangThai)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Món '" + tenMon + "' đã được phục vụ.\n" +
                "Bạn có chắc chắn muốn xóa không?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        try {
            // ✅ LẤY DANH SÁCH CHI TIẾT ĐỂ TÌM MÃ MÓN
            List<ChiTietHoaDon> dsChiTiet = chiTietHoaDonDAO.layDanhSachTheoHoaDon(hoaDon.getMaHoaDon());
            
            // Tìm chi tiết theo tên món (vì table không lưu mã món)
            ChiTietHoaDon cthdCanXoa = null;
            for (ChiTietHoaDon cthd : dsChiTiet) {
                if (cthd.getMonAn().getTenMon().equals(tenMon)) {
                    cthdCanXoa = cthd;
                    break;
                }
            }
            
            if (cthdCanXoa == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy món cần xóa!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // ✅ XÓA TỪ DATABASE
            boolean success = chiTietHoaDonDAO.xoaChiTietHoaDon(
                hoaDon.getMaHoaDon(), 
                cthdCanXoa.getMonAn().getMaMon()
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Xóa món thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload lại
                loadDanhSachMonAn();
                tinhTongTien();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Xóa món thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xóa món: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Thanh toán
     * TODO: Mở dialog thanh toán
     */
    
    /**
     * Thanh toán
     * TODO: Mở dialog thanh toán
//     */
//    private void thanhToan() {
//        // Kiểm tra còn món chưa phục vụ
//        for (int i = 0; i < tableModel.getRowCount(); i++) {
//            String trangThai = tableModel.getValueAt(i, 5).toString();
//            if (!"Đã phục vụ".equals(trangThai)) {
//                int confirm = JOptionPane.showConfirmDialog(this,
//                    "Vẫn còn món chưa được phục vụ!\n" +
//                    "Bạn có muốn tiếp tục thanh toán không?",
//                    "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//                
//                if (confirm != JOptionPane.YES_OPTION) {
//                    return;
//                }
//                break;
//            }
//        }
//        
//        // Lấy số tiền cần thanh toán
//        String thanhTienStr = lblThanhToanValue.getText().replace("đ", "").replace(".", "").replace(",", "").trim();
//        double thanhTien = 0;
//        try {
//            thanhTien = Double.parseDouble(thanhTienStr);
//        } catch (NumberFormatException e) {
//            thanhTien = 935000; // Giá trị mặc định
//        }
//        
//        // Tạo dialog thanh toán
//        JDialog dialogThanhToan = new JDialog(this, "Thanh toán", true);
//        dialogThanhToan.setSize(500, 400);
//        dialogThanhToan.setLocationRelativeTo(this);
//        dialogThanhToan.setLayout(new BorderLayout(10, 10));
//        
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//        mainPanel.setBackground(Color.WHITE);
//        
//        // Title
//        JLabel lblTitle = new JLabel("THANH TOÁN HÓA ĐƠN", JLabel.CENTER);
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblTitle.setForeground(MAIN_COLOR);
//        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
//        mainPanel.add(lblTitle, BorderLayout.NORTH);
//        
//        // Content panel
//        JPanel contentPanel = new JPanel(new GridBagLayout());
//        contentPanel.setBackground(Color.WHITE);
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(10, 10, 10, 10);
//        
//        // Tổng tiền
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 0.4;
//        JLabel lblTongTienLabel = new JLabel("Tổng tiền:");
//        lblTongTienLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
//        contentPanel.add(lblTongTienLabel, gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 0.6;
//        JLabel lblTongTienVal = new JLabel(lblThanhToanValue.getText());
//        lblTongTienVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblTongTienVal.setForeground(DANGER_COLOR);
//        lblTongTienVal.setHorizontalAlignment(JLabel.RIGHT);
//        contentPanel.add(lblTongTienVal, gbc);
//        
//        // Phương thức thanh toán
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        JLabel lblPhuongThucLabel = new JLabel("Phương thức:");
//        lblPhuongThucLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
//        contentPanel.add(lblPhuongThucLabel, gbc);
//        
//        gbc.gridx = 1;
//        JComboBox<String> cboPhuongThuc = new JComboBox<>(new String[]{
//            "Tiền mặt", "Chuyển khoản", "Thẻ", "Ví điện tử"
//        });
//        cboPhuongThuc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        contentPanel.add(cboPhuongThuc, gbc);
//        
//        // Tiền khách đưa
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        JLabel lblTienKhachLabel = new JLabel("Tiền khách đưa:");
//        lblTienKhachLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
//        contentPanel.add(lblTienKhachLabel, gbc);
//        
//        gbc.gridx = 1;
//        JTextField txtTienKhach = new JTextField();
//        txtTienKhach.setFont(new Font("Segoe UI", Font.PLAIN, 15));
//        txtTienKhach.setBorder(BorderFactory.createCompoundBorder(
//            new LineBorder(new Color(200, 200, 200)),
//            new EmptyBorder(8, 10, 8, 10)
//        ));
//        contentPanel.add(txtTienKhach, gbc);
//        
//        // Tiền thừa
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        JLabel lblTienThuaLabel = new JLabel("Tiền thừa:");
//        lblTienThuaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
//        contentPanel.add(lblTienThuaLabel, gbc);
//        
//        gbc.gridx = 1;
//        JLabel lblTienThuaVal = new JLabel("0đ");
//        lblTienThuaVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        lblTienThuaVal.setForeground(SUCCESS_COLOR);
//        lblTienThuaVal.setHorizontalAlignment(JLabel.RIGHT);
//        contentPanel.add(lblTienThuaVal, gbc);
//        
//        // Tính tiền thừa khi nhập
//        final double finalThanhTien = thanhTien;
//        txtTienKhach.addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyReleased(java.awt.event.KeyEvent evt) {
//                try {
//                    String input = txtTienKhach.getText().replace(",", "").replace(".", "").trim();
//                    if (!input.isEmpty()) {
//                        double tienKhach = Double.parseDouble(input);
//                        double tienThua = tienKhach - finalThanhTien;
//                        
//                        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
//                        if (tienThua >= 0) {
//                            lblTienThuaVal.setText(currencyFormat.format(tienThua) + "đ");
//                            lblTienThuaVal.setForeground(SUCCESS_COLOR);
//                        } else {
//                            lblTienThuaVal.setText(currencyFormat.format(Math.abs(tienThua)) + "đ (Thiếu)");
//                            lblTienThuaVal.setForeground(DANGER_COLOR);
//                        }
//                    } else {
//                        lblTienThuaVal.setText("0đ");
//                        lblTienThuaVal.setForeground(SUCCESS_COLOR);
//                    }
//                } catch (NumberFormatException e) {
//                    lblTienThuaVal.setText("Nhập sai!");
//                    lblTienThuaVal.setForeground(DANGER_COLOR);
//                }
//            }
//        });
//        
//        // Nút tiền nhanh
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        gbc.gridwidth = 2;
//        JPanel quickMoneyPanel = new JPanel(new GridLayout(2, 3, 5, 5));
//        quickMoneyPanel.setBackground(Color.WHITE);
//        quickMoneyPanel.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createTitledBorder("Tiền nhanh"),
//            new EmptyBorder(5, 5, 5, 5)
//        ));
//        
//        String[] quickAmounts = {"500,000", "1,000,000", "2,000,000", "5,000,000", "Đủ tiền", "Xóa"};
//        for (String amount : quickAmounts) {
//            JButton btnQuick = new JButton(amount);
//            btnQuick.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//            btnQuick.setBackground(new Color(240, 240, 240));
//            btnQuick.setFocusPainted(false);
//            btnQuick.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            
//            btnQuick.addActionListener(e -> {
//                if ("Xóa".equals(amount)) {
//                    txtTienKhach.setText("");
//                    lblTienThuaVal.setText("0đ");
//                    lblTienThuaVal.setForeground(SUCCESS_COLOR);
//                } else if ("Đủ tiền".equals(amount)) {
//                    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
//                    txtTienKhach.setText(currencyFormat.format(finalThanhTien));
//                    txtTienKhach.postActionEvent(); // Trigger key listener
//                } else {
//                    txtTienKhach.setText(amount.replace(",", ""));
//                    txtTienKhach.postActionEvent();
//                }
//            });
//            
//            quickMoneyPanel.add(btnQuick);
//        }
//        contentPanel.add(quickMoneyPanel, gbc);
//        
//        mainPanel.add(contentPanel, BorderLayout.CENTER);
//        
//        // Button panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        buttonPanel.setBackground(Color.WHITE);
//        
//        JButton btnXacNhan = createButton("Xác nhận thanh toán", SUCCESS_COLOR);
//        btnXacNhan.setPreferredSize(new Dimension(180, 40));
//        btnXacNhan.addActionListener(e -> {
//            // Validate
//            String phuongThuc = (String) cboPhuongThuc.getSelectedItem();
//            String tienKhachStr = txtTienKhach.getText().replace(",", "").replace(".", "").trim();
//            
//            if (tienKhachStr.isEmpty() && "Tiền mặt".equals(phuongThuc)) {
//                JOptionPane.showMessageDialog(dialogThanhToan,
//                    "Vui lòng nhập số tiền khách đưa!",
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            
//            // Kiểm tra tiền đủ
//            if ("Tiền mặt".equals(phuongThuc)) {
//                try {
//                    double tienKhach = Double.parseDouble(tienKhachStr);
//                    if (tienKhach < finalThanhTien) {
//                        JOptionPane.showMessageDialog(dialogThanhToan,
//                            "Số tiền khách đưa chưa đủ!",
//                            "Lỗi", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(dialogThanhToan,
//                        "Số tiền không hợp lệ!",
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//            }
//            
//            // Confirm
//            int confirm = JOptionPane.showConfirmDialog(dialogThanhToan,
//                "Xác nhận thanh toán?\n\n" +
//                "Phương thức: " + phuongThuc + "\n" +
//                "Tổng tiền: " + lblTongTienVal.getText() + "\n" +
//                (lblTienThuaVal.getText().contains("Thiếu") ? "" : "Tiền thừa: " + lblTienThuaVal.getText()),
//                "Xác nhận", JOptionPane.YES_NO_OPTION);
//            
//            if (confirm == JOptionPane.YES_OPTION) {
//                // TODO: Cập nhật database
//                // hoaDonDAO.thanhToanHoaDon(maHoaDon, phuongThuc, tienKhach);
//                // banAnDAO.capNhatTrangThai(banAn.getMaBan(), "Trống");
//                
//                JOptionPane.showMessageDialog(dialogThanhToan,
//                    "Thanh toán thành công!\n\n" +
//                    "Cảm ơn quý khách và hẹn gặp lại!",
//                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
//                
//                dialogThanhToan.dispose();
//                dispose(); // Đóng dialog hóa đơn
//            }
//        });
//        
//        JButton btnHuy = createButton("Hủy", new Color(100, 100, 100));
//        btnHuy.setPreferredSize(new Dimension(100, 40));
//        btnHuy.addActionListener(e -> dialogThanhToan.dispose());
//        
//        buttonPanel.add(btnXacNhan);
//        buttonPanel.add(btnHuy);
//        
//        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//        
//        dialogThanhToan.add(mainPanel);
//        dialogThanhToan.setVisible(true);
//    }
    
    // Sửa lại method thanhToan() - GỌI DIALOG MỚI
private void thanhToan() {
    // Kiểm tra món chưa phục vụ
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        String trangThai = tableModel.getValueAt(i, 5).toString();
        if (!"Đã phục vụ".equals(trangThai)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Vẫn còn món chưa được phục vụ!\n" +
                "Bạn có muốn tiếp tục thanh toán không?",
                "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            break;
        }
    }
    
    // Lấy số tiền cần thanh toán
    String thanhTienStr = lblThanhToanValue.getText()
        .replace("đ", "").replace(".", "").replace(",", "").trim();
    double thanhTien = 0;
    try {
        thanhTien = Double.parseDouble(thanhTienStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "Lỗi khi đọc số tiền thanh toán!",
            "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
   
    
   

        // Kiểm tra còn món chưa phục vụ
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String trangThai = tableModel.getValueAt(i, 5).toString();
            if (!"Đã phục vụ".equals(trangThai)) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Vẫn còn món chưa được phục vụ!\n" +
                    "Bạn có muốn tiếp tục thanh toán không?",
                    "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                break;
            }
        }

        // Lấy số tiền cần thanh toán
        String ttstr = lblThanhToanValue.getText().replace("đ", "").replace(".", "").replace(",", "").trim();
        double tt = 0;
        try {
            tt = Double.parseDouble(ttstr);
        } catch (NumberFormatException e) {
            tt = 935000; // Giá trị mặc định
        }

        // Tạo dialog thanh toán
        JDialog dialogThanhToan = new JDialog(this, "Thanh toán", true);
        dialogThanhToan.setSize(500, 400);
        dialogThanhToan.setLocationRelativeTo(this);
        dialogThanhToan.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("THANH TOÁN HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(MAIN_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Tổng tiền
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        JLabel lblTongTienLabel = new JLabel("Tổng tiền:");
        lblTongTienLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentPanel.add(lblTongTienLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JLabel lblTongTienVal = new JLabel(lblThanhToanValue.getText());
        lblTongTienVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTienVal.setForeground(DANGER_COLOR);
        lblTongTienVal.setHorizontalAlignment(JLabel.RIGHT);
        contentPanel.add(lblTongTienVal, gbc);

        // Phương thức thanh toán
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblPhuongThucLabel = new JLabel("Phương thức:");
        lblPhuongThucLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentPanel.add(lblPhuongThucLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> cboPhuongThuc = new JComboBox<>(new String[]{
            "Tiền mặt", "Chuyển khoản", "Thẻ", "Ví điện tử"
        });
        cboPhuongThuc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(cboPhuongThuc, gbc);

        // Tiền khách đưa
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblTienKhachLabel = new JLabel("Tiền khách đưa:");
        lblTienKhachLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentPanel.add(lblTienKhachLabel, gbc);

        gbc.gridx = 1;
        JTextField txtTienKhach = new JTextField();
        txtTienKhach.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTienKhach.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        contentPanel.add(txtTienKhach, gbc);

        // Tiền thừa
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblTienThuaLabel = new JLabel("Tiền thừa:");
        lblTienThuaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentPanel.add(lblTienThuaLabel, gbc);

        gbc.gridx = 1;
        JLabel lblTienThuaVal = new JLabel("0đ");
        lblTienThuaVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTienThuaVal.setForeground(SUCCESS_COLOR);
        lblTienThuaVal.setHorizontalAlignment(JLabel.RIGHT);
        contentPanel.add(lblTienThuaVal, gbc);

        // Tính tiền thừa khi nhập
        final double finalThanhTien = thanhTien;
        txtTienKhach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    String input = txtTienKhach.getText().replace(",", "").replace(".", "").trim();
                    if (!input.isEmpty()) {
                        double tienKhach = Double.parseDouble(input);
                        double tienThua = tienKhach - finalThanhTien;

                        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                        if (tienThua >= 0) {
                            lblTienThuaVal.setText(currencyFormat.format(tienThua) + "đ");
                            lblTienThuaVal.setForeground(SUCCESS_COLOR);
                        } else {
                            lblTienThuaVal.setText(currencyFormat.format(Math.abs(tienThua)) + "đ (Thiếu)");
                            lblTienThuaVal.setForeground(DANGER_COLOR);
                        }
                    } else {
                        lblTienThuaVal.setText("0đ");
                        lblTienThuaVal.setForeground(SUCCESS_COLOR);
                    }
                } catch (NumberFormatException e) {
                    lblTienThuaVal.setText("Nhập sai!");
                    lblTienThuaVal.setForeground(DANGER_COLOR);
                }
            }
        });

        // Nút tiền nhanh
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel quickMoneyPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        quickMoneyPanel.setBackground(Color.WHITE);
        quickMoneyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Tiền nhanh"),
            new EmptyBorder(5, 5, 5, 5)
        ));

        String[] quickAmounts = {"500,000", "1,000,000", "2,000,000", "5,000,000", "Đủ tiền", "Xóa"};
        for (String amount : quickAmounts) {
            JButton btnQuick = new JButton(amount);
            btnQuick.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnQuick.setBackground(new Color(240, 240, 240));
            btnQuick.setFocusPainted(false);
            btnQuick.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnQuick.addActionListener(e -> {
                if ("Xóa".equals(amount)) {
                    txtTienKhach.setText("");
                    lblTienThuaVal.setText("0đ");
                    lblTienThuaVal.setForeground(SUCCESS_COLOR);
                } else if ("Đủ tiền".equals(amount)) {
                    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                    txtTienKhach.setText(currencyFormat.format(finalThanhTien));
                    txtTienKhach.postActionEvent(); // Trigger key listener
                } else {
                    txtTienKhach.setText(amount.replace(",", ""));
                    txtTienKhach.postActionEvent();
                }
            });

            quickMoneyPanel.add(btnQuick);
        }
        contentPanel.add(quickMoneyPanel, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnXacNhan = createButton("Xác nhận thanh toán", SUCCESS_COLOR);
        btnXacNhan.setPreferredSize(new Dimension(180, 40));
        btnXacNhan.addActionListener(e -> {
            // Validate
            String phuongThuc = (String) cboPhuongThuc.getSelectedItem();
            String tienKhachStr = txtTienKhach.getText().replace(",", "").replace(".", "").trim();

            if (tienKhachStr.isEmpty() && "Tiền mặt".equals(phuongThuc)) {
                JOptionPane.showMessageDialog(dialogThanhToan,
                    "Vui lòng nhập số tiền khách đưa!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra tiền đủ
            if ("Tiền mặt".equals(phuongThuc)) {
                try {
                    double tienKhach = Double.parseDouble(tienKhachStr);
                    if (tienKhach < finalThanhTien) {
                        JOptionPane.showMessageDialog(dialogThanhToan,
                            "Số tiền khách đưa chưa đủ!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialogThanhToan,
                        "Số tiền không hợp lệ!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Confirm
            int confirm = JOptionPane.showConfirmDialog(dialogThanhToan,
                "Xác nhận thanh toán?\n\n" +
                "Phương thức: " + phuongThuc + "\n" +
                "Tổng tiền: " + lblTongTienVal.getText() + "\n" +
                (lblTienThuaVal.getText().contains("Thiếu") ? "" : "Tiền thừa: " + lblTienThuaVal.getText()),
                "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    HoaDonDAO hoaDonDAO = new HoaDonDAO();
                    ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
                    BanAnDAO banAnDAO = new BanAnDAO();
                    KhachHangDAO khDAO = new KhachHangDAO();
                    PhieuDatBanDAO phieuDatDAO = new PhieuDatBanDAO();

                    // 1️. TÍNH LẠI TỔNG TIỀN
                    double tongTienMon = 0;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        Object val = tableModel.getValueAt(i, 4); // cột Thành tiền (giả sử là cột 4)
                        if (val != null) {
                            try {
                                // Bỏ ký tự định dạng tiền tệ
                                String str = val.toString().replace("đ", "").replace(".", "").replace(",", "").trim();
                                tongTienMon += Double.parseDouble(str);
                            } catch (Exception ignored) {}
                        }
                    }


                    double tienVAT = tongTienMon * (hoaDon.getThueVAT());
                    double tongSauVAT = tongTienMon + tienVAT;

                    // Giảm giá
                    double tienGiamGia = 0;
                    if (hoaDon.getKhuyenMai() != null) {
                        KhuyenMai km = hoaDon.getKhuyenMai();
                        if (km.getSoTienGiam() > 0)
                            tienGiamGia = km.getSoTienGiam();
                        else
                            tienGiamGia = tongSauVAT * (km.getPhanTramGiam() / 100.0);
                    }

                    double tongCong = tongSauVAT - tienGiamGia;
                    double tienCoc = hoaDon.getTienCoc();
                    double daThanhToan = tongCong - tienCoc;

                    // 2️⃣ CẬP NHẬT HÓA ĐƠN
                    hoaDonDAO.capNhatTongTien(hoaDon.getMaHoaDon(), tongCong);
                    hoaDonDAO.capNhatTrangThai(hoaDon.getMaHoaDon(), "Đã thanh toán", Session.getMaNhanVienDangNhap());

                    if(banAn != null) {
                    banAnDAO.capNhatTrangThaiBan(banAn.getMaBan(), "Trống");
                    }
                    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                    // THÔNG BÁO
                    JOptionPane.showMessageDialog(dialogThanhToan,
                        "Thanh toán thành công!\n\n" +
                        "Tổng hóa đơn: " + currencyFormat.format(tongCong) + "đ\n" +
                        "Tiền cọc: " + currencyFormat.format(tienCoc) + "đ\n" +
                        "Khách cần trả thêm: " + currencyFormat.format(daThanhToan) + "đ\n\n" +
                        "Cảm ơn quý khách và hẹn gặp lại!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    dialogThanhToan.dispose();
                    dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialogThanhToan,
                        "Lỗi khi thanh toán: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        JButton btnHuy = createButton("Hủy", new Color(100, 100, 100));
        btnHuy.setPreferredSize(new Dimension(100, 40));
        btnHuy.addActionListener(e -> dialogThanhToan.dispose());

        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialogThanhToan.add(mainPanel);
        dialogThanhToan.setVisible(true);
    }
    
    /**
     * Tạo hóa đơn mới cho bàn nếu bàn chưa có hóa đơn
     * Dùng khi nhân viên bấm vào bàn trống trên sơ đồ bàn
     */
    private HoaDon taoHoaDonMoiChoBan(BanAn ban) {
        try {
            HoaDonDAO hdDAO = new HoaDonDAO();
            BanAnDAO banDAO = new BanAnDAO();

            HoaDon hd = new HoaDon();
            hd.setMaHoaDon(hdDAO.taoMaHoaDonTuDong());
            hd.setBanAn(ban);
            hd.setNhanVien(Session.getNhanVienDangNhap()); // cần có class Session đang lưu nhân viên đăng nhập
            hd.setNgayLapHoaDon(LocalDateTime.now());
            hd.setThueVAT(0.1); // 10%
            hd.setTongTien(0);
            hd.setTrangThai("Chưa thanh toán");
            hd.setTienCoc(0);
            hd.setKhachHang(null); // khách vãng lai
            hd.setKhuyenMai(null);

            boolean success = hdDAO.themHoaDon(hd, Session.getMaNhanVienDangNhap());
            if (success) {
                // Cập nhật trạng thái bàn thành "Đang sử dụng"
                banDAO.capNhatTrangThaiBan(ban.getMaBan(), "Đang sử dụng");
                JOptionPane.showMessageDialog(this, 
                    "Tạo hóa đơn mới thành công cho " + ban.getTenBan() + "!", 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                hd.setDsChiTiet(new ArrayList<ChiTietHoaDon>()); // khởi tạo danh sách chi tiết rỗng
                return hd;
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi tạo hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * In tạm tính
     * TODO: In hóa đơn tạm tính
     */
    private void inTamTinh() {
        JOptionPane.showMessageDialog(this,
            "Chức năng in tạm tính sẽ được cài đặt sau!\n\n" +
            "Sẽ in ra:\n" +
            "- Danh sách món ăn\n" +
            "- Tổng tiền tạm tính\n" +
            "- Chưa thanh toán",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Chuyển bàn
     * TODO: Mở dialog chọn bàn mới
     */
    private void chuyenBan() {
        // Lấy danh sách bàn trống
        String[] banTrong = {"Bàn 3 - Tầng 1", "Bàn 5 - Tầng 2", "Bàn 7 - Sân thượng"};
        
        String banMoi = (String) JOptionPane.showInputDialog(
            this,
            "Chọn bàn muốn chuyển đến:",
            "Chuyển bàn",
            JOptionPane.QUESTION_MESSAGE,
            null,
            banTrong,
            banTrong[0]
        );
        
        if (banMoi != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận chuyển từ " + banAn.getTenBan() + " sang " + banMoi + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: Cập nhật database
                // hoaDonDAO.chuyenBan(maHoaDon, maBanMoi);
                // banAnDAO.capNhatTrangThai(banAn.getMaBan(), "Trống");
                // banAnDAO.capNhatTrangThai(maBanMoi, "Đang sử dụng");
                
                JOptionPane.showMessageDialog(this,
                    "Chuyển bàn thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            }
        }
    } 
    
}