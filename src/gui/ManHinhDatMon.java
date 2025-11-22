package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.MonAnDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
import util.Session;

public class ManHinhDatMon extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panelCenter;
    private JPanel panelEast;
    private JPanel panelCenterNorth;
    private JPanel panelCenterCenter;
    private JPanel panelDoUong;
    private JPanel panelMonAnKem;
    private JPanel panelEastCenter;

    private JLabel lblTongTienTamTinh;

    private JButton btnDanhSachMonAn_MonAnChinh;
    private JButton btnDanhSachMonAn_DoUong;
    private JButton btnDanhSachMonAn_MonAnKem;

    private JButton btnThanhToan;  // Chỉ giữ lại nút này

    private MonAnDAO monAnDAO = new MonAnDAO();
    private DecimalFormat df = new DecimalFormat("#,##0 VNĐ");
    private Runnable onMonAnUpdated; 

    private List<MonAn> listMonAnChinh = new ArrayList<>();
    private List<MonAn> listDoUong = new ArrayList<>();
    private List<MonAn> listMonAnKem = new ArrayList<>();
    private List<MonAn> danhSachMonAnDaDat = new ArrayList<>();

    private HoaDon hoaDonHienTai;
    
 // Constructor mới với callback
    public ManHinhDatMon(HoaDon hoaDonHienTai, Runnable onMonAnUpdated) {
        this.hoaDonHienTai = hoaDonHienTai;
        this.onMonAnUpdated = onMonAnUpdated;
        loadDataFromDB();
        initComponents();
    }
    
    public ManHinhDatMon(HoaDon hoaDonHienTai) {
        this.hoaDonHienTai = hoaDonHienTai;
        loadDataFromDB();
        initComponents();
    }

    public ManHinhDatMon() {
        this(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout());
        add(contentPane, BorderLayout.CENTER);

        // ===== PANEL TRÁI: DANH SÁCH MÓN ĂN =====
        panelCenter = new JPanel();
        panelCenter.setPreferredSize(new Dimension(800, 600));
        contentPane.add(panelCenter, BorderLayout.WEST);
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        // Thanh menu chọn loại món
        panelCenterNorth = new JPanel();
        panelCenterNorth.setMaximumSize(new Dimension(32767, 100));
        panelCenterNorth.setPreferredSize(new Dimension(800, 100));
        panelCenterNorth.setBackground(new Color(245, 245, 245));
        panelCenter.add(panelCenterNorth);
        panelCenterNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        // Custom button với bo góc
        class RoundedButton extends JButton {
            private int radius;

            public RoundedButton(String text, int radius) {
                super(text);
                this.radius = radius;
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.setColor(new Color(39, 174, 96));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                g2.dispose();
                super.paintComponent(g);
            }
        }

        // Nút Món ăn chính
        btnDanhSachMonAn_MonAnChinh = new RoundedButton("MÓN ĂN CHÍNH", 20);
        btnDanhSachMonAn_MonAnChinh.setPreferredSize(new Dimension(200, 60));
        btnDanhSachMonAn_MonAnChinh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDanhSachMonAn_MonAnChinh.setForeground(Color.WHITE);
        btnDanhSachMonAn_MonAnChinh.setBackground(new Color(46, 204, 113));
        panelCenterNorth.add(btnDanhSachMonAn_MonAnChinh);

        // Nút Đồ uống
        btnDanhSachMonAn_DoUong = new RoundedButton("ĐỒ UỐNG", 20);
        btnDanhSachMonAn_DoUong.setPreferredSize(new Dimension(200, 60));
        btnDanhSachMonAn_DoUong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDanhSachMonAn_DoUong.setForeground(Color.WHITE);
        btnDanhSachMonAn_DoUong.setBackground(new Color(46, 204, 113));
        panelCenterNorth.add(btnDanhSachMonAn_DoUong);

        // Nút Món ăn kèm
        btnDanhSachMonAn_MonAnKem = new RoundedButton("MÓN ĂN KÈM", 20);
        btnDanhSachMonAn_MonAnKem.setPreferredSize(new Dimension(200, 60));
        btnDanhSachMonAn_MonAnKem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDanhSachMonAn_MonAnKem.setForeground(Color.WHITE);
        btnDanhSachMonAn_MonAnKem.setBackground(new Color(46, 204, 113));
        panelCenterNorth.add(btnDanhSachMonAn_MonAnKem);

        // ScrollPane chứa danh sách món
        panelCenterCenter = new JPanel();
        panelCenterCenter.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(panelCenterCenter);
        scrollPane.setPreferredSize(new Dimension(800, 500));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelCenter.add(scrollPane);

        initializepnDanhMucs(scrollPane);

        // ===== PANEL PHẢI: GIỎ HÀNG =====
        panelEast = new JPanel(new BorderLayout());
        panelEast.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelEast.setBackground(Color.WHITE);
        contentPane.add(panelEast, BorderLayout.CENTER);

        // Header giỏ hàng + nút XÓA GIỎ HÀNG
        JPanel panelEastTop = new JPanel(new BorderLayout());
        panelEastTop.setBackground(Color.WHITE);
        panelEastTop.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("GIỎ HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(46, 204, 113));
        panelEastTop.add(lblTitle, BorderLayout.WEST);

        // NÚT XÓA GIỎ HÀNG – ĐƯA VÀO ĐÂY
        JButton btnXoaGioHang = new JButton("XÓA GIỎ HÀNG");
        btnXoaGioHang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnXoaGioHang.setBackground(new Color(149, 165, 166));
        btnXoaGioHang.setForeground(Color.WHITE);
        btnXoaGioHang.setFocusPainted(false);
        btnXoaGioHang.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaGioHang.setPreferredSize(new Dimension(140, 35));
        btnXoaGioHang.addActionListener(e -> {
            if (danhSachMonAnDaDat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả món?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                danhSachMonAnDaDat.clear();
                capNhatGioHang();
                capNhatTongTien();
            }
        });
        panelEastTop.add(btnXoaGioHang, BorderLayout.EAST);

        // Hiển thị thông tin bàn
        if (hoaDonHienTai != null && hoaDonHienTai.getBanAn() != null) {
            JLabel lblBan = new JLabel("Bàn: " + hoaDonHienTai.getBanAn().getTenBan());
            lblBan.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblBan.setForeground(new Color(52, 73, 94));
            panelEastTop.add(lblBan, BorderLayout.SOUTH);
        }

        panelEast.add(panelEastTop, BorderLayout.NORTH);

        // Danh sách món đã chọn
        panelEastCenter = new JPanel();
        panelEastCenter.setBackground(Color.WHITE);
        panelEastCenter.setLayout(new BoxLayout(panelEastCenter, BoxLayout.Y_AXIS));

        JScrollPane eastScrollPane = new JScrollPane(panelEastCenter);
        eastScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        eastScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        eastScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelEast.add(eastScrollPane, BorderLayout.CENTER);

        // Panel bottom: Tạm tính + 2 nút (Gửi bếp + Thanh toán)
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(Color.WHITE);
        panelBottom.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Tạm tính
        JPanel panelTongTien = new JPanel(new BorderLayout());
        panelTongTien.setBackground(new Color(236, 240, 241));
        panelTongTien.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 2),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTongTienLabel = new JLabel("TẠM TÍNH:");
        lblTongTienLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTienLabel.setForeground(new Color(52, 73, 94));
        panelTongTien.add(lblTongTienLabel, BorderLayout.WEST);

        lblTongTienTamTinh = new JLabel("0 VNĐ");
        lblTongTienTamTinh.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTienTamTinh.setForeground(new Color(231, 76, 60));
        lblTongTienTamTinh.setHorizontalAlignment(SwingConstants.RIGHT);
        panelTongTien.add(lblTongTienTamTinh, BorderLayout.EAST);

        panelBottom.add(panelTongTien, BorderLayout.NORTH);

        // 2 nút: Gửi bếp + Thanh toán (có icon)
        JPanel panelButtons = new JPanel(new GridLayout(1, 2, 15, 0));
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Nút GỬI VÀO BẾP – CÓ ICON
        JButton btnGuiVaoBep = new JButton();
        btnGuiVaoBep.setLayout(new BorderLayout());
        ImageIcon iconBep = new ImageIcon("img//thucdon//dau_bep_icon.png");
        Image imgBep = iconBep.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel lblIconBep = new JLabel(new ImageIcon(imgBep));
        JLabel lblTextBep = new JLabel("GỬI VÀO BẾP", SwingConstants.CENTER);
        lblTextBep.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTextBep.setForeground(Color.WHITE);

        JPanel panelBep = new JPanel(new BorderLayout());
        panelBep.setBackground(new Color(46, 204, 113));
        panelBep.add(lblIconBep, BorderLayout.WEST);
        panelBep.add(lblTextBep, BorderLayout.CENTER);
        btnGuiVaoBep.add(panelBep, BorderLayout.CENTER);

        btnGuiVaoBep.setPreferredSize(new Dimension(200, 60));
        btnGuiVaoBep.setFocusPainted(false);
        btnGuiVaoBep.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Trong btnGuiVaoBep listener, sau khi lưu thành công:
        btnGuiVaoBep.addActionListener(e -> {
            if (danhSachMonAnDaDat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống! Vui lòng chọn món.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (hoaDonHienTai != null) {
                    int soMonGui = danhSachMonAnDaDat.size();
                    luuDanhSachMonVaoHD(hoaDonHienTai);
                    
                    JOptionPane.showMessageDialog(this,
                        "Đã gửi " + soMonGui + " món vào bếp thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    
                    // ← GỌI CALLBACK ĐỂ CẬP NHẬT DIALOG
                    if (onMonAnUpdated != null) {
                        onMonAnUpdated.run();
                    }
                    return;
                }

                // ... phần tạo hóa đơn mang về giữ nguyên ...
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Khách mua mang về?\nTạo hóa đơn mới để order?",
                    "Tạo hóa đơn mang về", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) return;

                JTextField txtHoTen = new JTextField(20);
                JTextField txtSDT = new JTextField(15);

                JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
                panel.add(new JLabel("Họ tên khách hàng:"));
                panel.add(txtHoTen);
                panel.add(new JLabel("Số điện thoại:"));
                panel.add(txtSDT);

                int result = JOptionPane.showConfirmDialog(this, panel,
                    "Thông tin khách hàng (không bắt buộc)", JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION) return;

                String hoTen = txtHoTen.getText().trim();
                String sdt = txtSDT.getText().trim();

                KhachHang kh = null;
                if (!hoTen.isEmpty() || !sdt.isEmpty()) {
                    KhachHangDAO khDAO = new KhachHangDAO();
                    if (!sdt.isEmpty()) {
                        kh = khDAO.timKhachHangTheoSDT(sdt);
                    }
                    if (kh == null) {
                        kh = new KhachHang();
                        kh.setMaKH(khDAO.taoMaKhachHangTuDong());
                        kh.setHoTen(hoTen.isEmpty() ? "Khách lẻ" : hoTen);
                        kh.setSdt(sdt);
                        kh.setGioiTinh(true);
                        kh.setDiemTichLuy(0);
                        kh.setNgayDangKy(LocalDate.now());
                        kh.setTrangThai(true);
                        khDAO.themKhachHang(kh);
                    }
                }

                HoaDonDAO hdDAO = new HoaDonDAO();
                HoaDon hdMoi = new HoaDon();
                hdMoi.setMaHoaDon(hdDAO.taoMaHoaDonTuDong());
                hdMoi.setBanAn(null);
                hdMoi.setKhachHang(kh);
                hdMoi.setNhanVien(Session.getNhanVienDangNhap());
                hdMoi.setNgayLapHoaDon(LocalDateTime.now());
                hdMoi.setThueVAT(0.1);
                hdMoi.setTongTien(0);
                hdMoi.setTrangThai("Chưa thanh toán");
                hdMoi.setTienCoc(0);

                if (!hdDAO.themHoaDon(hdMoi)) {
                    JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int soMonGui = danhSachMonAnDaDat.size();
                luuDanhSachMonVaoHD(hdMoi);
                this.hoaDonHienTai = hdMoi;

                JOptionPane.showMessageDialog(this,
                    "Tạo hóa đơn mang về thành công!\nMã HD: " + hdMoi.getMaHoaDon() + 
                    "\nĐã gửi " + soMonGui + " món vào bếp!\nBạn có thể tiếp tục order thêm!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // ← GỌI CALLBACK
                if (onMonAnUpdated != null) {
                    onMonAnUpdated.run();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        panelButtons.add(btnGuiVaoBep);

        // Nút THANH TOÁN – CÓ ICON
        btnThanhToan = new JButton();
        btnThanhToan.setLayout(new BorderLayout());
        ImageIcon iconTT = new ImageIcon("img//thucdon//thanh_toan.png");
        Image imgTT = iconTT.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel lblIconTT = new JLabel(new ImageIcon(imgTT));
        JLabel lblTextTT = new JLabel("THANH TOÁN", SwingConstants.CENTER);
        lblTextTT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTextTT.setForeground(Color.WHITE);

        JPanel panelTT = new JPanel(new BorderLayout());
        panelTT.setBackground(new Color(231, 76, 60));
        panelTT.add(lblIconTT, BorderLayout.WEST);
        panelTT.add(lblTextTT, BorderLayout.CENTER);
        btnThanhToan.add(panelTT, BorderLayout.CENTER);

        btnThanhToan.setPreferredSize(new Dimension(200, 60));
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));
     // ====== BTN THANH TOÁN (HOÀN CHỈNH) ======
        btnThanhToan.addActionListener(e -> {
        	
        	if (danhSachMonAnDaDat.isEmpty() && (this.hoaDonHienTai == null)) {
                JOptionPane.showMessageDialog(this, "Chưa có món nào để thanh toán!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // NẾU CHƯA CÓ HÓA ĐƠN → TỰ ĐỘNG TẠO
            if (hoaDonHienTai == null) {
                btnGuiVaoBep.doClick();
                if (hoaDonHienTai == null) return;
            }
            
            

            // MỞ DIALOG CHI TIẾT HÓA ĐƠN
            Frame parent = JOptionPane.getFrameForComponent(ManHinhDatMon.this);
            DialogChiTietHoaDon dialog = new DialogChiTietHoaDon(parent, hoaDonHienTai);
            dialog.setVisible(true);

            // Nếu đã thanh toán → reset
            if (dialog.isDaThanhToan()) {
                danhSachMonAnDaDat.clear();
                capNhatGioHang();
                capNhatTongTien();
                this.hoaDonHienTai = null;
            }
        });
        panelButtons.add(btnThanhToan);

        panelBottom.add(panelButtons, BorderLayout.SOUTH);
        panelEast.add(panelBottom, BorderLayout.SOUTH);

        // Action cho các nút chọn loại món
        btnDanhSachMonAn_MonAnChinh.addActionListener(e -> scrollPane.setViewportView(panelCenterCenter));
        btnDanhSachMonAn_DoUong.addActionListener(e -> scrollPane.setViewportView(panelDoUong));
        btnDanhSachMonAn_MonAnKem.addActionListener(e -> scrollPane.setViewportView(panelMonAnKem));

        capNhatGioHang();
        capNhatTongTien();
    }

    private void loadDataFromDB() {
        List<MonAn> alldanhSachMon = monAnDAO.getAllMonAn();
        for (MonAn mon : alldanhSachMon) {
            String loaiMon = mon.getLoaiMon();
            if (loaiMon == null) continue;
            switch (loaiMon.toUpperCase()) {
                case "MC": listMonAnChinh.add(mon); break;
                case "DO": listDoUong.add(mon); break;
                case "MK": listMonAnKem.add(mon); break;
            }
        }
    }

    private void initializepnDanhMucs(JScrollPane scrollPane) {
        panelCenterCenter = taoDanhMucMonAn(listMonAnChinh);
        scrollPane.setViewportView(panelCenterCenter);
        panelDoUong = taoDanhMucMonAn(listDoUong);
        panelMonAnKem = taoDanhMucMonAn(listMonAnKem);
    }

    private JPanel taoDanhMucMonAn(List<MonAn> danhSachMon) {
        JPanel pnDanhMuc = new JPanel();
        pnDanhMuc.setLayout(new BoxLayout(pnDanhMuc, BoxLayout.Y_AXIS));
        pnDanhMuc.setBackground(Color.WHITE);
        int soMonMoiHang = 3;
        int soHang = (int) Math.ceil(danhSachMon.size() / 3.0);
        for (int row = 0; row < soHang; row++) {
            JPanel pnHang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            pnHang.setBackground(Color.WHITE);
            for (int col = 0; col < soMonMoiHang; col++) {
                int index = row * soMonMoiHang + col;
                if (index >= danhSachMon.size()) break;
                MonAn mon = danhSachMon.get(index);
                JPanel pnMon = taoPanelMonAn(mon);
                pnHang.add(pnMon);
            }
            pnDanhMuc.add(pnHang);
            pnDanhMuc.add(Box.createVerticalStrut(10));
        }
        return pnDanhMuc;
    }

    private JPanel taoPanelMonAn(MonAn mon) {
        JPanel panelMonAn = new JPanel();
        panelMonAn.setPreferredSize(new Dimension(250, 130));
        panelMonAn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelMonAn.setBackground(Color.WHITE);
        panelMonAn.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        JLabel lblHinh = new JLabel();
        try {
            String imgPath = mon.getHinhAnh();
            if (imgPath != null && !imgPath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imgPath);
                Image img = icon.getImage().getScaledInstance(125, 110, Image.SCALE_SMOOTH);
                lblHinh.setIcon(new ImageIcon(img));
            } else {
                lblHinh.setText("No Image");
                lblHinh.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception ex) {
            lblHinh.setText("No Image");
            lblHinh.setHorizontalAlignment(SwingConstants.CENTER);
        }
        lblHinh.setPreferredSize(new Dimension(125, 110));
        panelMonAn.add(lblHinh);

        JPanel panelRight = new JPanel(new BorderLayout(0, 5));
        panelRight.setPreferredSize(new Dimension(110, 110));
        panelRight.setBackground(Color.WHITE);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);

        JLabel lblTenMon = new JLabel(mon.getTenMon());
        lblTenMon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTenMon.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblTenMon);

        JLabel lblGia = new JLabel(df.format(mon.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGia.setForeground(new Color(231, 76, 60));
        lblGia.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblGia);

        panelRight.add(panelInfo, BorderLayout.NORTH);

        JButton btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(46, 204, 113));
        btnThem.setFocusPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.setPreferredSize(new Dimension(100, 30));
        btnThem.addActionListener(e -> themMonVaoGioHang(mon));
        panelRight.add(btnThem, BorderLayout.SOUTH);

        panelMonAn.add(panelRight);
        return panelMonAn;
    }

    private void themMonVaoGioHang(MonAn mon) {
        boolean exists = false;
        for (MonAn monDaDat : danhSachMonAnDaDat) {
            if (monDaDat.getMaMon().equals(mon.getMaMon())) {
                monDaDat.setSoLuong(monDaDat.getSoLuong() + 1);
                exists = true;
                break;
            }
        }
        if (!exists) {
            MonAn monMoi = new MonAn();
            monMoi.setMaMon(mon.getMaMon());
            monMoi.setTenMon(mon.getTenMon());
            monMoi.setGia(mon.getGia());
            monMoi.setSoLuong(1);
            danhSachMonAnDaDat.add(monMoi);
        }
        capNhatGioHang();
        capNhatTongTien();
    }

    private void capNhatGioHang() {
        panelEastCenter.removeAll();
        if (danhSachMonAnDaDat.isEmpty()) {
            JLabel lblEmpty = new JLabel("Giỏ hàng trống");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            lblEmpty.setBorder(new EmptyBorder(50, 20, 50, 20));
            panelEastCenter.add(lblEmpty);
        } else {
            for (MonAn mon : danhSachMonAnDaDat) {
                JPanel itemPanel = taoItemGioHang(mon);
                panelEastCenter.add(itemPanel);
                panelEastCenter.add(Box.createVerticalStrut(5));
            }
        }
        panelEastCenter.revalidate();
        panelEastCenter.repaint();
    }

    private JPanel taoItemGioHang(MonAn mon) {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(400, 70));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 70));
        panel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(189, 195, 199)));

        JLabel lblTen = new JLabel(mon.getTenMon());
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTen.setBounds(10, 10, 200, 20);
        panel.add(lblTen);

        JLabel lblGia = new JLabel(df.format(mon.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblGia.setForeground(new Color(127, 140, 141));
        lblGia.setBounds(10, 35, 150, 18);
        panel.add(lblGia);

        JLabel lblSoLuong = new JLabel(String.valueOf(mon.getSoLuong()));
        lblSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSoLuong.setHorizontalAlignment(SwingConstants.CENTER);
        lblSoLuong.setBorder(new LineBorder(new Color(149, 165, 166)));
        lblSoLuong.setBounds(220, 15, 35, 40);
        panel.add(lblSoLuong);

        // Nút TĂNG (icon +)
        JButton btnTang = new JButton();
        btnTang.setFocusPainted(false);
        btnTang.setBounds(260, 15, 30, 18);
        ImageIcon icon_tang = new ImageIcon("img//thucdon//add_icon.png");
        Image img_tang = icon_tang.getImage().getScaledInstance(30, 18, Image.SCALE_SMOOTH);
        btnTang.setIcon(new ImageIcon(img_tang));
        btnTang.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTang.addActionListener(e -> {
            mon.setSoLuong(mon.getSoLuong() + 1);
            capNhatGioHang();
            capNhatTongTien();
        });
        panel.add(btnTang);

        // Nút GIẢM (icon -)
        JButton btnGiam = new JButton();
        btnGiam.setFocusPainted(false);
        btnGiam.setBounds(260, 37, 30, 18);
        ImageIcon icon_giam = new ImageIcon("img//thucdon//minus_icon.png");
        Image img_giam = icon_giam.getImage().getScaledInstance(30, 18, Image.SCALE_SMOOTH);
        btnGiam.setIcon(new ImageIcon(img_giam));
        btnGiam.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGiam.addActionListener(e -> {
            if (mon.getSoLuong() > 1) {
                mon.setSoLuong(mon.getSoLuong() - 1);
            } else {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Xóa món \"" + mon.getTenMon() + "\" khỏi giỏ hàng?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    danhSachMonAnDaDat.remove(mon);
                }
            }
            capNhatGioHang();
            capNhatTongTien();
        });
        panel.add(btnGiam);

        JLabel lblThanhTien = new JLabel(df.format(mon.getGia() * mon.getSoLuong()));
        lblThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblThanhTien.setForeground(new Color(231, 76, 60));
        lblThanhTien.setHorizontalAlignment(SwingConstants.RIGHT);
        lblThanhTien.setBounds(300, 20, 130, 25);
        panel.add(lblThanhTien);

        // Nút XÓA (icon thùng rác)
        JButton btnXoa = new JButton();
        btnXoa.setFocusPainted(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoa.setBounds(440, 20, 40, 30);
        ImageIcon icon_xoa = new ImageIcon("img//thucdon//thung_rac_icon.png");
        Image img_xoa = icon_xoa.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);
        btnXoa.setIcon(new ImageIcon(img_xoa));
        btnXoa.setToolTipText("Xóa món");
        btnXoa.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Xóa món \"" + mon.getTenMon() + "\" khỏi giỏ hàng?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                danhSachMonAnDaDat.remove(mon);
                capNhatGioHang();
                capNhatTongTien();
            }
        });
        panel.add(btnXoa);

        return panel;
    }

    private void capNhatTongTien() {
        double tongTien = 0;
        for (MonAn mon : danhSachMonAnDaDat) {
            tongTien += mon.getGia() * mon.getSoLuong();
        }
        lblTongTienTamTinh.setText(df.format(tongTien));
    }

 // Sửa lại method luuDanhSachMonVaoHD - XÓA thông báo bên trong
    public void luuDanhSachMonVaoHD(HoaDon hoaDon) {
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (danhSachMonAnDaDat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có món nào được đặt!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
        try {
            for (MonAn mon : danhSachMonAnDaDat) {
                ChiTietHoaDon cthd = cthdDAO.timChiTietTheoHoaDonVaMon(
                    hoaDon.getMaHoaDon(),
                    mon.getMaMon()
                );
                if (cthd != null) {
                    int soLuongMoi = cthd.getSoLuong() + mon.getSoLuong();
                    double thanhTienMoi = soLuongMoi * mon.getGia();
                    cthd.setSoLuong(soLuongMoi);
                    cthd.setThanhTien(thanhTienMoi);
                    cthdDAO.capNhatChiTietHoaDon(cthd);
                } else {
                    cthd = new ChiTietHoaDon();
                    cthd.setHoaDon(hoaDon);
                    cthd.setMonAn(mon);
                    cthd.setSoLuong(mon.getSoLuong());
                    cthd.setDonGia(mon.getGia());
                    cthd.setThanhTien(mon.getGia() * mon.getSoLuong());
                    cthd.setGhiChu(null);
                    cthdDAO.themChiTietHoaDon(cthd);
                }
            }

            danhSachMonAnDaDat.clear();
            capNhatGioHang();
            capNhatTongTien();
            
            // ← XÓA DÒNG NÀY
            // JOptionPane.showMessageDialog(this, "Đã gửi món vào bếp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu món!\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<MonAn> getDanhSachMonAnDaDat() {
        return danhSachMonAnDaDat;
    }
}