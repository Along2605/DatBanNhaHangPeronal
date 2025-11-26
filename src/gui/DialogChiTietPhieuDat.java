package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.BanAnDAO;
import dao.ChiTietHoaDonDAO;
import dao.ChiTietPhieuDatDAO;
import dao.HoaDonDAO;
import dao.PhieuDatBanDAO;
import entity.BanAn;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDat;
import entity.HoaDon;
import entity.NhanVien;
import entity.PhieuDatBan;
import util.Session;

import java.awt.*;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DialogChiTietPhieuDat extends JDialog{
    
    private static final long serialVersionUID = 1L;
    private static final Color MAIN_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    
    private BanAn banAn;
    private JTable tableMonAn;
    private DefaultTableModel tableModel;
    
    
    private PhieuDatBan phieuDatBan;
    private PhieuDatBanDAO phieuDatBanDAO;
//    private List<ChiTietPhieuDat> dsChiTietMonAn;
    
    
    private JLabel lblMaPhieuValue, lblNgayDatValue, lblSoNguoiValue, lblTrangThaiValue;
    private JLabel lblKhachHangValue, lblSdtValue, lblBanValue, lblKhuVucValue;
    private JLabel lblTienCocValue, lblTongMonValue, lblTongTien;;
    private JTextArea txtGhiChu;
    
    // Formatters
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
   
    
    public DialogChiTietPhieuDat(Frame parent, BanAn banAn) {
        super(parent, "Chi tiết phiếu đặt bàn", true);
        this.banAn = banAn;
        this.phieuDatBanDAO= new PhieuDatBanDAO();
        
        setSize(960, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
        loadThongTinPhieuDat();
    }
    
    private void initComponents() {
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU ĐẶT BÀN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(MAIN_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel content (chia 2 phần: thông tin + món ăn)
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Phần thông tin phiếu đặt
        contentPanel.add(createThongTinPanel(), BorderLayout.NORTH);
        
        // Phần danh sách món ăn
        contentPanel.add(createMonAnPanel(), BorderLayout.CENTER);
        
        // Phần tổng tiền
        contentPanel.add(createTongTienPanel(), BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Panel nút
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Tạo panel thông tin phiếu đặt
     */
    private JPanel createThongTinPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(MAIN_COLOR, 2),
                "Thông tin phiếu đặt",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                MAIN_COLOR
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Dòng 1: Thông tin cơ bản
     
        JPanel row1 = new JPanel(new GridLayout(1, 4, 15, 0));
        row1.setBackground(Color.WHITE);
        
        lblMaPhieuValue = new JLabel();
        lblNgayDatValue = new JLabel();
        lblSoNguoiValue = new JLabel();
        lblTrangThaiValue = new JLabel();
        
        
        row1.add(createInfoField("Mã phiếu đặt:", lblMaPhieuValue, false));
        row1.add(createInfoField("Ngày đặt:", lblNgayDatValue, false));
        row1.add(createInfoField("Số người:", lblSoNguoiValue, false));
        row1.add(createInfoField("Trạng thái:", lblTrangThaiValue, true));
        
        // Dòng 2: Thông tin khách hàng & bàn
        JPanel row2 = new JPanel(new GridLayout(1, 4, 15, 0));
        row2.setBackground(Color.WHITE);
        
        lblKhachHangValue = new JLabel("...");
        lblSdtValue = new JLabel("...");
        lblBanValue = new JLabel(banAn.getTenBan());
        lblKhuVucValue = new JLabel(banAn.getKhuVuc() != null ? banAn.getKhuVuc().getTenKhuVuc() : "");
        
        row2.add(createInfoField("Khách hàng:", lblKhachHangValue, false));
        row2.add(createInfoField("SĐT:", lblSdtValue, false));
        row2.add(createInfoField("Bàn:", lblBanValue, false));
        row2.add(createInfoField("Khu vực:", lblKhuVucValue, false));
        
        panel.add(row1);
        panel.add(row2);
        
        return panel;
    }
    
    /**
     * Tạo panel danh sách món ăn
     */
    private JPanel createMonAnPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(MAIN_COLOR, 2),
                "Danh sách món đã đặt trước",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                MAIN_COLOR
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
           
        // Table món ăn
        String[] columns = {"STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Ghi chú"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableMonAn = new JTable(tableModel);
        tableMonAn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableMonAn.setRowHeight(35);
        tableMonAn.setShowGrid(true);
        tableMonAn.setGridColor(new Color(230, 230, 230));
        
        // Header style
        JTableHeader header = tableMonAn.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(MAIN_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Column widths
        tableMonAn.getColumnModel().getColumn(0).setPreferredWidth(50);   // STT
        tableMonAn.getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên món
        tableMonAn.getColumnModel().getColumn(2).setPreferredWidth(100);  // Đơn giá
        tableMonAn.getColumnModel().getColumn(3).setPreferredWidth(80);   // Số lượng
        tableMonAn.getColumnModel().getColumn(4).setPreferredWidth(120);  // Thành tiền
        tableMonAn.getColumnModel().getColumn(5).setPreferredWidth(200);  // Ghi chú
        
        JScrollPane scrollPane = new JScrollPane(tableMonAn);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Tạo panel tổng tiền
     */
    private JPanel createTongTienPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Panel bên trái: Ghi chú
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblGhiChuTitle = new JLabel("Ghi chú:");
        lblGhiChuTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        leftPanel.add(lblGhiChuTitle, BorderLayout.NORTH);
        
        txtGhiChu = new JTextArea(3, 30); 
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
//        txtGhiChu.setText("Khách yêu cầu bàn gần cửa sổ, view đẹp");
        txtGhiChu.setEditable(false);
        txtGhiChu.setBackground(new Color(245, 245, 245));
        txtGhiChu.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setBorder(new LineBorder(new Color(200, 200, 200)));
        leftPanel.add(scrollGhiChu, BorderLayout.CENTER);
        
        // Panel bên phải: Tiền cọc và tổng tiền
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 250, 255));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAIN_COLOR, 2),
            new EmptyBorder(0, 20, 0, 20)
        ));
//        rightPanel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Tiền cọc
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        JLabel lblTienCocLabel = new JLabel("Tiền cọc:");
        lblTienCocLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(lblTienCocLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        lblTienCocValue = new JLabel();
        lblTienCocValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTienCocValue.setHorizontalAlignment(JLabel.RIGHT);
        lblTienCocValue.setForeground(WARNING_COLOR);
        rightPanel.add(lblTienCocValue, gbc);
        
        // Tổng tiền món
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTongMonLabel = new JLabel("Tổng tiền món:");
        lblTongMonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(lblTongMonLabel, gbc);
        
        gbc.gridx = 1;
        lblTongMonValue = new JLabel("0đ");
        lblTongMonValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongMonValue.setHorizontalAlignment(JLabel.RIGHT);
        rightPanel.add(lblTongMonValue, gbc);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JSeparator separator = new JSeparator();
        separator.setForeground(MAIN_COLOR);
        rightPanel.add(separator, gbc);
        
        // Tổng cộng
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel lblTongCongLabel = new JLabel("TỔNG CỘNG:");
        lblTongCongLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongCongLabel.setForeground(MAIN_COLOR);
        rightPanel.add(lblTongCongLabel, gbc);
        
        gbc.gridx = 1;
        lblTongTien = new JLabel();
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setHorizontalAlignment(JLabel.RIGHT);
        lblTongTien.setForeground(new Color(211, 47, 47));
        rightPanel.add(lblTongTien, gbc);
        
        // Note nhỏ
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel lblNote = new JLabel("* Chưa bao gồm VAT và phí dịch vụ");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(Color.GRAY);
        rightPanel.add(lblNote, gbc);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Tạo panel nút
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);
        
        JButton btnXacNhan = createButton("Xác nhận phiếu", SUCCESS_COLOR);
        btnXacNhan.setPreferredSize(new Dimension(160, 40));
        btnXacNhan.addActionListener(e -> xacNhanPhieu());
        
        JButton btnChuyenBan = createButton("Chuyển bàn", new Color(52, 152, 219));
        btnChuyenBan.setPreferredSize(new Dimension(160, 40));
        btnChuyenBan.addActionListener(e-> chuyenBan());
              
        JButton btnHuyPhieu = createButton("Hủy phiếu", new Color(244, 67, 54));
        btnHuyPhieu.setPreferredSize(new Dimension(160, 40));
        btnHuyPhieu.addActionListener(e -> huyPhieu());
        
        JButton btnInPhieu = createButton("In phiếu", new Color(103, 58, 183));
        btnInPhieu.setPreferredSize(new Dimension(160, 40));
        btnInPhieu.addActionListener(e -> inPhieu());
        
        JButton btnDong = createButton("Đóng", new Color(100, 100, 100));
        btnDong.setPreferredSize(new Dimension(160, 40));
        btnDong.addActionListener(e -> dispose());
        
        panel.add(btnXacNhan);
        panel.add(btnChuyenBan);
        panel.add(btnHuyPhieu);
        panel.add(btnInPhieu);
        panel.add(btnDong);
        
        return panel;
    }
    
    private Object chuyenBan() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * Tạo field thông tin
     */
    private JPanel createInfoField(String label, JLabel valueComponent, boolean isStatus) {
        
    	JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLabel.setForeground(Color.GRAY);

        // Thiết lập font chung cho component giá trị
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        if (isStatus && valueComponent instanceof JLabel) {
            JLabel lblValue = (JLabel) valueComponent;
            lblValue.setOpaque(true);
            lblValue.setHorizontalAlignment(JLabel.CENTER);
            lblValue.setBorder(new EmptyBorder(5, 10, 5, 10));
        }
        
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
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
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
     * Load thông tin phiếu đặt
     * TODO: Load từ database
     */
    private void loadThongTinPhieuDat() {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        
        try {
			
			phieuDatBan = phieuDatBanDAO.getPhieuDatTheoBan(banAn.getMaBan());
			if (phieuDatBan == null) {
	            JOptionPane.showMessageDialog(this, "Bàn này không có phiếu đặt hoặc phiếu đã được xử lý.", "Thông báo", JOptionPane.WARNING_MESSAGE);
	            
	            lblMaPhieuValue.setText("Không có thông tin");
	           
	            return;
	        }
			
			if(phieuDatBan==null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt cho bàn này", "Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			lblMaPhieuValue.setText(phieuDatBan.getMaPhieuDat());
	        lblNgayDatValue.setText(phieuDatBan.getNgayDat().format(dtf));
	        lblSoNguoiValue.setText(String.valueOf(phieuDatBan.getSoNguoi()) + " người");
	        // Kiểm tra xem phiếu đặt có thông tin khách hàng hay không
	        if (phieuDatBan.getKhachHang() != null) {
	            // Nếu có, hiển thị thông tin bình thường
	            lblKhachHangValue.setText(phieuDatBan.getKhachHang().getHoTen());
	            lblSdtValue.setText(phieuDatBan.getKhachHang().getSdt());
	        } else {
	            // Nếu không, hiển thị thông tin cho khách vãng lai
	            lblKhachHangValue.setText("Khách vãng lai");
	            lblSdtValue.setText("Không có");
	        }
	        txtGhiChu.setText(phieuDatBan.getGhiChu());
	        
	        String trangThai = phieuDatBan.getTrangThai();
	        lblTrangThaiValue.setText(trangThai);
	        if ("Đã đặt".equals(trangThai)) {
	            lblTrangThaiValue.setBackground(WARNING_COLOR);
	            lblTrangThaiValue.setForeground(Color.WHITE);
	        }
	        else if ("Đã xác nhận".equals(trangThai)) {
	        	lblTrangThaiValue.setBackground(SUCCESS_COLOR);
	        	lblTrangThaiValue.setForeground(Color.WHITE);
	        } else if ("Đã hủy".equals(trangThai)) {
	        	lblTrangThaiValue.setBackground(new Color(158, 158, 158));
	        	lblTrangThaiValue.setForeground(Color.WHITE);
	        }
	        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
//	        lblTienCocValue.setText(currencyFormat.format(phieuDatBan.getSoTienCoc()) + "đ");

	        //Đổ dữ liệu món ăn vào bảng
			ChiTietPhieuDatDAO chiTietDAO = new ChiTietPhieuDatDAO();
			List<ChiTietPhieuDat> dsMonAn = chiTietDAO.getMonAnTheoPhieu(phieuDatBan.getMaPhieuDat());
			if (dsMonAn != null && !dsMonAn.isEmpty()) {
				// Đưa dữ liệu lên bảng
				int stt = 1;
				for (ChiTietPhieuDat chiTiet : dsMonAn) {
					

					String tenMon = chiTiet.getMonAn().getTenMon();
					String donGia = currencyFormat.format(chiTiet.getDonGia()) + "đ";
					int soLuong = chiTiet.getSoLuong();
					double thanhTien = chiTiet.getDonGia() * soLuong;
					String thanhTienStr = currencyFormat.format(thanhTien) + "đ";
					String ghiChu = chiTiet.getGhiChu() != null ? chiTiet.getGhiChu() : "";

					tableModel.addRow(new Object[] { stt++, tenMon, donGia, soLuong, thanhTienStr, ghiChu });
				}
				
				
	        } else {
	            // Không có món ăn đặt trước
	            JOptionPane.showMessageDialog(this,
	                "Phiếu đặt này chưa có món ăn đặt trước!",
	                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        }
			
			// Cập nhật tổng tiền
	        tinhTongTien(phieuDatBan);
			
		} catch (Exception e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(this,
	            "Lỗi khi load thông tin phiếu đặt!\n" + e.getMessage(),
	            "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
        
      
        
    }
    
    /**
     * Tính tổng tiền
     */
    private void tinhTongTien(PhieuDatBan phieuDat) {
    	try {
            double tongTienMon = 0;
            
//             Tính tổng tiền từ bảng
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String thanhTienStr = tableModel.getValueAt(i, 4).toString();
                // Xóa "đ" và dấu phẩy, chấm
                thanhTienStr = thanhTienStr.replace("đ", "")
                                           .replace(",", "")
                                           .replace(".", "")
                                           .trim();
                tongTienMon += Double.parseDouble(thanhTienStr);
            }
            
            // Lấy tiền cọc từ phiếu đặt
            double tienCoc = phieuDat.getSoTienCoc();
            
            double tongCong = tongTienMon + tienCoc;
            
           
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            
            lblTienCocValue.setText(currencyFormat.format(tienCoc) + "đ");
            lblTongMonValue.setText(currencyFormat.format(tongTienMon) + "đ");    
            lblTongTien.setText(currencyFormat.format(tongCong) + "đ");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi tính tổng tiền: " + e.getMessage());
        }
    }
    
    
//    Xác nhận phiếu đặt 
 
    private void xacNhanPhieu() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận phiếu đặt này?\n\n" +
            "Sau khi xác nhận, phiếu đặt sẽ được đảm bảo và không thể hủy!",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        String maPhieuDat = phieuDatBan.getMaPhieuDat();
        String maBan = phieuDatBan.getBanAn().getMaBan();
        String maKH = phieuDatBan.getKhachHang().getMaKH();
        String maNV = Session.getMaNhanVienDangNhap();

        PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
        BanAnDAO banDAO = new BanAnDAO();
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietPhieuDatDAO ctpdDAO = new ChiTietPhieuDatDAO();
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();

        try {
            // --- 1. Cập nhật trạng thái phiếu & bàn ---
            if (!phieuDAO.capNhatTrangThaiPhieu(maPhieuDat, "Đã xác nhận")
                    || !banDAO.capNhatTrangThaiBan(maBan, "Đang sử dụng")) {
                JOptionPane.showMessageDialog(this,
                    "Không thể cập nhật trạng thái phiếu hoặc bàn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- 2. Tạo hóa đơn mới ---
            String maHoaDon = hoaDonDAO.taoMaHoaDonTuDong();

            HoaDon hoaDonMoi = new HoaDon();
            hoaDonMoi.setMaHoaDon(maHoaDon);
            hoaDonMoi.setBanAn(phieuDatBan.getBanAn());
            hoaDonMoi.setKhachHang(phieuDatBan.getKhachHang());
            hoaDonMoi.setNhanVien(new NhanVien(maNV));
            hoaDonMoi.setNgayLapHoaDon(java.time.LocalDateTime.now());
            hoaDonMoi.setThueVAT(0.1);
            hoaDonMoi.setKhuyenMai(null);
            hoaDonMoi.setTrangThai("Chưa thanh toán");
            hoaDonMoi.setPhieuDat(phieuDatBan);

            // ✅ Lấy tiền cọc từ phiếu đặt và lưu vào hóa đơn
            double tienCoc = phieuDatBan.getSoTienCoc();
            hoaDonMoi.setTienCoc(tienCoc);

            // ✅ Tính tổng tiền món đã đặt
            List<ChiTietPhieuDat> dsCTPD = ctpdDAO.getChiTietTheoMaPhieu(maPhieuDat);
            double tongTienChua = 0;
            for (ChiTietPhieuDat ctpd : dsCTPD) {
                tongTienChua += ctpd.getSoLuong() * ctpd.getDonGia();
            }

            double tongTienSauVAT =0;
            if (tongTienChua > 0) {
                tongTienSauVAT = tongTienChua * (1 + hoaDonMoi.getThueVAT());
            }
            hoaDonMoi.setTongTien(tongTienSauVAT);
            
//            if (dsCTPD.isEmpty()) {
//                hoaDonMoi.setTongTien(0); // chưa có món nào, sẽ cập nhật sau
//            } else {
//                double tongTienChua = dsCTPD.stream()
//                    .mapToDouble(c -> c.getSoLuong() * c.getDonGia())
//                    .sum();
//                hoaDonMoi.setTongTien(tongTienChua * (1 + hoaDonMoi.getThueVAT()));
//            }


            // --- 3. Lưu hóa đơn ---
            if (!hoaDonDAO.themHoaDon(hoaDonMoi, Session.getMaNhanVienDangNhap())) {
                throw new Exception("Không thể tạo hóa đơn mới!");
            }

            // --- 4. Chuyển chi tiết phiếu đặt sang chi tiết hóa đơn ---
            for (ChiTietPhieuDat ctpd : dsCTPD) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setHoaDon(new HoaDon(maHoaDon));
                cthd.setMonAn(ctpd.getMonAn());
                cthd.setSoLuong(ctpd.getSoLuong());
                cthd.setDonGia(ctpd.getDonGia());
                cthd.setThanhTien(ctpd.getSoLuong() * ctpd.getDonGia());
                cthd.setGhiChu(ctpd.getGhiChu());

                if (!cthdDAO.themChiTietHoaDon(cthd)) {
                    throw new Exception("Không thể thêm chi tiết hóa đơn cho món: " 
                        + ctpd.getMonAn().getTenMon());
                }
            }

            // --- 5. Thông báo thành công ---
            JOptionPane.showMessageDialog(this,
                "✅ Xác nhận phiếu đặt thành công!\n"
                + "Đã tạo hóa đơn mã: " + maHoaDon
                + "\nTiền cọc: " + String.format("%,.0f VNĐ", tienCoc)
                + "\nTổng tiền (đã VAT): " + String.format("%,.0f VNĐ", tongTienSauVAT)
                + "\nBàn đã chuyển sang 'Đang sử dụng'.",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "❌ Lỗi khi xác nhận phiếu: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);

            // Rollback
            phieuDAO.capNhatTrangThaiPhieu(maPhieuDat, "Đã đặt");
            banDAO.capNhatTrangThaiBan(maBan, "Đã đặt");
        }
    }


       
//     Hủy phiếu đặt       
    private void huyPhieu() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xác nhận hủy phiếu đặt?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String maPhieu = phieuDatBan.getMaPhieuDat();
            String maBan = phieuDatBan.getBanAn().getMaBan();

            PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
            BanAnDAO banDAO = new BanAnDAO();

            boolean huyOK = phieuDAO.huyPhieuDat(maPhieu);
            boolean capNhatOK = banDAO.capNhatTrangThaiBan(maBan, "Trống");

            if (huyOK && capNhatOK) {
                JOptionPane.showMessageDialog(this, 
                    "Đã hủy phiếu đặt thành công.\nBàn đã trở về trạng thái 'Trống'.", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Làm mới lại danh sách bàn hoặc phiếu
//                loadDanhSachBan(); 
                
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể hủy phiếu. Vui lòng thử lại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
 
//     In phiếu đặt    
    private void inPhieu() {
        JOptionPane.showMessageDialog(this,
            "Chức năng in phiếu đặt sẽ được cài đặt sau!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

	
}