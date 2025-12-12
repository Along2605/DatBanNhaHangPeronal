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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    
    private List<String> danhSachBanTrongPhieu; 
    
    public DialogChiTietPhieuDat(Frame parent, BanAn banAn) {
//        super(parent, "Chi tiết phiếu đặt bàn", true);
//        this.banAn = banAn;
//        this.phieuDatBanDAO= new PhieuDatBanDAO();
//        
//        setSize(960, 700);
//        setLocationRelativeTo(parent);
//        setLayout(new BorderLayout(10, 10));
//        
//        initComponents();
//        loadThongTinPhieuDat();
    	this(parent, banAn, null, null);
	}

	public DialogChiTietPhieuDat(Frame parent, BanAn banAn, PhieuDatBan phieuDat, List<String> danhSachBan) {
		super(parent, "Chi tiết phiếu đặt bàn", true);
		this.banAn = banAn;
		this.phieuDatBan = phieuDat;
		this.danhSachBanTrongPhieu = danhSachBan;
		this.phieuDatBanDAO = new PhieuDatBanDAO();

		setSize(960, 700);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout(10, 10));

		initComponents();

// ✅ Nếu đã có phiếu đặt truyền vào, load luôn
		if (phieuDat != null) {
			loadThongTinPhieuDatDaCo();
		} else {
			loadThongTinPhieuDat();
		}
	}

	/**
	 * Load thông tin phiếu đặt khi đã có sẵn object PhieuDatBan
	 */
	private void loadThongTinPhieuDatDaCo() {
		tableModel.setRowCount(0);

		try {
			if (phieuDatBan == null) {
				JOptionPane.showMessageDialog(this, "Không có thông tin phiếu đặt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}

		// Hiển thị thông tin cơ bản
		lblMaPhieuValue.setText(phieuDatBan.getMaPhieuDat());
		
		// Hiển thị ngày và khung giờ
		int khungGio = phieuDatBan.getKhungGio();
		String tenKhungGio = getTenKhungGioFromID(khungGio);
		lblNgayDatValue.setText(phieuDatBan.getNgayDat().format(dtf) + " (" + tenKhungGio + ")");			lblSoNguoiValue.setText(String.valueOf(phieuDatBan.getSoNguoi()) + " người");

			if (phieuDatBan.getKhachHang() != null) {
				lblKhachHangValue.setText(phieuDatBan.getKhachHang().getHoTen());
				lblSdtValue.setText(phieuDatBan.getKhachHang().getSdt());
			} else {
				lblKhachHangValue.setText("Khách vãng lai");
				lblSdtValue.setText("Không có");
			}

			txtGhiChu.setText(phieuDatBan.getGhiChu());

			String trangThai = phieuDatBan.getTrangThai();
			lblTrangThaiValue.setText(trangThai);
			if ("Đã đặt".equals(trangThai)) {
				lblTrangThaiValue.setBackground(WARNING_COLOR);
				lblTrangThaiValue.setForeground(Color.WHITE);
			} else if ("Đã xác nhận".equals(trangThai)) {
				lblTrangThaiValue.setBackground(SUCCESS_COLOR);
				lblTrangThaiValue.setForeground(Color.WHITE);
			} else if ("Đã hủy".equals(trangThai)) {
				lblTrangThaiValue.setBackground(new Color(158, 158, 158));
				lblTrangThaiValue.setForeground(Color.WHITE);
			}

			NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

			// Load danh sách món ăn
			ChiTietPhieuDatDAO chiTietDAO = new ChiTietPhieuDatDAO();
			List<ChiTietPhieuDat> dsMonAn = chiTietDAO.getMonAnTheoPhieu(phieuDatBan.getMaPhieuDat());

			if (dsMonAn != null && !dsMonAn.isEmpty()) {
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
				JOptionPane.showMessageDialog(this, "Phiếu đặt này chưa có món ăn đặt trước!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}

			// Cập nhật tổng tiền
			tinhTongTien(phieuDatBan);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi load thông tin phiếu đặt!\n" + e.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String getTenKhungGio(int gio) {
		if (gio >= 6 && gio < 11) return "Sáng";
	    if (gio >= 11 && gio < 14) return "Trưa";
	    if (gio >= 14 && gio < 17) return "Chiều";
	    return "Tối";
	}
	
	/**
	 * Lấy tên khung giờ từ ID
	 * @param khungGioID 1=Sáng, 2=Trưa, 3=Chiều, 4=Tối
	 */
	private String getTenKhungGioFromID(int khungGioID) {
		switch (khungGioID) {
			case 1: return "Sáng (6h-11h)";
			case 2: return "Trưa (11h-14h)";
			case 3: return "Chiều (14h-17h)";
			case 4: return "Tối (17h-22h)";
			default: return "Không xác định";
		}
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
    	// gridlayout(0,1) tự động thêm hàng
        JPanel panel = new JPanel(new GridLayout(0,1,10,10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(MAIN_COLOR, 2),
                "Thông tin phiếu đặt",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                MAIN_COLOR
            ),
            new EmptyBorder(10,10,10,10)
        ));
        
        JPanel row1= new JPanel(new GridLayout(1,4,15,0));
        row1.setBackground(Color.WHITE);

        lblMaPhieuValue = new JLabel();
        lblNgayDatValue = new JLabel();
        lblSoNguoiValue = new JLabel();
        lblTrangThaiValue = new JLabel();
        
        row1.add(createInfoField("Mã phiếu đặt:", lblMaPhieuValue, false));
        row1.add(createInfoField("Ngày đặt:", lblNgayDatValue, false));
        row1.add(createInfoField("Số người:", lblSoNguoiValue, false));
        row1.add(createInfoField("Trạng thái:", lblTrangThaiValue, true));
        
        // row2 thong tin khach hang va ban
        JPanel row2= new JPanel(new GridLayout(1,4,15,0));
        row2.setBackground(Color.WHITE);
        
        lblKhachHangValue = new JLabel("...");
        lblSdtValue = new JLabel("...");
        lblBanValue = new JLabel(banAn != null ? banAn.getTenBan() : "");
        lblKhuVucValue = new JLabel(banAn != null && banAn.getKhuVuc() != null ? banAn.getKhuVuc().getTenKhuVuc() : "");
        
        row2.add(createInfoField("Khách hàng:", lblKhachHangValue, false));
        row2.add(createInfoField("SĐT:", lblSdtValue, false));
        row2.add(createInfoField("Bàn:", lblBanValue, false));
        row2.add(createInfoField("Khu vực:", lblKhuVucValue, false));
        
        panel.add(row1);
        panel.add(row2);
        

//        lblKhachHangValue = new JLabel("...");
//        lblSdtValue = new JLabel("...");
//        lblBanValue = new JLabel(banAn != null ? banAn.getTenBan() : "");
//        lblKhuVucValue = new JLabel(banAn != null && banAn.getKhuVuc() != null ? banAn.getKhuVuc().getTenKhuVuc() : "");
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(2, 8, 2, 8);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.gridy = 0;
//        gbc.weightx = 0.25;
//
//        gbc.gridx = 0;
//        panel.add(createInfoField("Mã phiếu đặt:", lblMaPhieuValue, false), gbc);
//
//        gbc.gridx = 1;
//        panel.add(createInfoField("Ngày đặt:", lblNgayDatValue, false), gbc);
//
//        gbc.gridx = 2;
//        panel.add(createInfoField("Số người:", lblSoNguoiValue, false), gbc);
//
//        gbc.gridx = 3;
//        panel.add(createInfoField("Trạng thái:", lblTrangThaiValue, true), gbc);
//
//        gbc.gridy = 1;
//        gbc.gridx = 0;
//        panel.add(createInfoField("Khách hàng:", lblKhachHangValue, false), gbc);
//
//        gbc.gridx = 1;
//        panel.add(createInfoField("SĐT:", lblSdtValue, false), gbc);
//
//        gbc.gridx = 2;
//        panel.add(createInfoField("Bàn:", lblBanValue, false), gbc);
//
//        gbc.gridx = 3;
//        panel.add(createInfoField("Khu vực:", lblKhuVucValue, false), gbc);
        
        // them dong 3: ds bàn (nếu đặt nhiều bàn)
        JPanel row3= null;
        
      

        if (danhSachBanTrongPhieu != null && danhSachBanTrongPhieu.size() > 1) {
            row3 = new JPanel(new BorderLayout(5, 5));
            row3.setBackground(new Color(255, 248, 225));
            
            row3.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(WARNING_COLOR, 2),
                new EmptyBorder(8, 10, 8, 10)
            ));

            JLabel lblDSBanTitle = new JLabel("Phiếu này đặt " + danhSachBanTrongPhieu.size() + " bàn:");
            lblDSBanTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblDSBanTitle.setForeground(new Color(191, 87, 0));

            JTextArea txtDanhSachBan = new JTextArea(2, 0);
            txtDanhSachBan.setEditable(false);
//            txtDanhSachBan.setOpaque(false);
            txtDanhSachBan.setBackground(new Color(255,248,225));
            txtDanhSachBan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            txtDanhSachBan.setLineWrap(true);  // tu dong xuong dong
            txtDanhSachBan.setWrapStyleWord(true); // ngắt dòng theo từ hoàn chỉnh (không cắt giữa từ)

            StringBuilder sb = new StringBuilder();
            BanAnDAO banDAO = new BanAnDAO();
            for (int i = 0; i < danhSachBanTrongPhieu.size(); i++) {
                String maBan = danhSachBanTrongPhieu.get(i);
                BanAn ban = banDAO.getBanTheoMa(maBan);
                if (ban != null) {
                    sb.append(ban.getTenBan()).append(" (").append(ban.getSoLuongCho()).append(" chỗ)");
                    if (i < danhSachBanTrongPhieu.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
            txtDanhSachBan.setText(sb.toString());

            row3.add(lblDSBanTitle, BorderLayout.NORTH);
            row3.add(txtDanhSachBan, BorderLayout.CENTER);  
            
            if(row3 != null) {
            	panel.add(row3);
            }
           
        }
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
        
        // Lấy trạng thái phiếu đặt
        String trangThai = phieuDatBan != null ? phieuDatBan.getTrangThai() : "Chờ xác nhận";
        
        // Nút Xác nhận (chỉ hiển thị khi trạng thái là "Chờ xác nhận")
        if ("Chờ xác nhận".equals(trangThai)) {
            JButton btnXacNhan = createButton("Xác nhận phiếu", SUCCESS_COLOR);
            btnXacNhan.setPreferredSize(new Dimension(160, 40));
            btnXacNhan.addActionListener(e -> xacNhanPhieu());
            panel.add(btnXacNhan);
            
            JButton btnKhachKhongDen = createButton("Khách không đến", new Color(244, 67, 54));
            btnKhachKhongDen.setPreferredSize(new Dimension(160, 40));
            btnKhachKhongDen.addActionListener(e -> xuLyKhachKhongDen());
            panel.add(btnKhachKhongDen);
        }
        
        // Nút Chuyển bàn (chỉ hiển thị khi còn hoạt động)
        if ("Chờ xác nhận".equals(trangThai) || "Đã xác nhận".equals(trangThai)) {
            JButton btnChuyenBan = createButton("Chuyển bàn", new Color(52, 152, 219));
            btnChuyenBan.setPreferredSize(new Dimension(160, 40));
            btnChuyenBan.addActionListener(e-> chuyenBan());
            panel.add(btnChuyenBan);
        }
        
        // Nút Hủy phiếu (chỉ hiển thị khi trạng thái là "Chờ xác nhận")
        if ("Chờ xác nhận".equals(trangThai)) {
            JButton btnHuyPhieu = createButton("Hủy phiếu", new Color(158, 158, 158));
            btnHuyPhieu.setPreferredSize(new Dimension(160, 40));
            btnHuyPhieu.addActionListener(e -> huyPhieu());
            panel.add(btnHuyPhieu);
        }
        
//        // Nút In phiếu
//        JButton btnInPhieu = createButton("In phiếu", new Color(103, 58, 183));
//        btnInPhieu.setPreferredSize(new Dimension(160, 40));
//        btnInPhieu.addActionListener(e -> inPhieu());
//        panel.add(btnInPhieu);
        
        // Nút Đóng
        JButton btnDong = createButton("Đóng", new Color(100, 100, 100));
        btnDong.setPreferredSize(new Dimension(160, 40));
        btnDong.addActionListener(e -> dispose());
        panel.add(btnDong);
        
        return panel;
    }
    
    /**
     * Xử lý khi khách không đến.
     * Phiếu chuyển sang trạng thái "Khách không đến", tiền cọc sẽ được giữ lại.
     */
    private void xuLyKhachKhongDen() {
        if (phieuDatBan == null) {
            JOptionPane.showMessageDialog(this,
                "Không có thông tin phiếu đặt!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double tienCoc = phieuDatBan.getSoTienCoc();
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận khách không đến?\n\n" +
            "Thông tin phiếu:\n" +
            "- Mã phiếu: " + phieuDatBan.getMaPhieuDat() + "\n" +
            "- Khách hàng: " + (phieuDatBan.getKhachHang() != null ? phieuDatBan.getKhachHang().getHoTen() : "Khách vãng lai") + "\n" +
            "- Tiền cọc: " + currencyFormat.format(tienCoc) + "đ\n\n" +
            "⚠️ Tiền cọc sẽ không được hoàn lại!\n" +
            "Phiếu sẽ chuyển sang trạng thái 'Khách không đến'.",
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        String maPhieuDat = phieuDatBan.getMaPhieuDat();
        PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
        BanAnDAO banDAO = new BanAnDAO();

        try {
            // 1. Cập nhật trạng thái phiếu sang "Khách không đến"
            if (!phieuDAO.capNhatTrangThaiPhieu(maPhieuDat, "Khách không đến")) {
                throw new Exception("Không thể cập nhật trạng thái phiếu!");
            }

            // 2. Cập nhật trạng thái tất cả bàn về "Trống"
            if (danhSachBanTrongPhieu != null && !danhSachBanTrongPhieu.isEmpty()) {
                for (String maBan : danhSachBanTrongPhieu) {
                    if (!banDAO.capNhatTrangThaiBan(maBan, "Trống")) {
                        System.err.println("⚠️ Không thể cập nhật bàn: " + maBan);
                    }
                }
            } else if (phieuDatBan.getBanAn() != null) {
                // Fallback: chỉ cập nhật bàn hiện tại
                String maBan = phieuDatBan.getBanAn().getMaBan();
                if (!banDAO.capNhatTrangThaiBan(maBan, "Trống")) {
                    throw new Exception("Không thể cập nhật trạng thái bàn!");
                }
            }

            // 3. Thông báo thành công
            JOptionPane.showMessageDialog(this,
                "✅ Đã xử lý phiếu đặt!\n\n" +
                "Trạng thái: Khách không đến\n" +
                "Tiền cọc giữ lại: " + currencyFormat.format(tienCoc) + "đ\n" +
                "Bàn đã được giải phóng.",
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "❌ Lỗi khi xử lý: " + e.getMessage(),
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
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
			
//			if(phieuDatBan==null) {
//				JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt cho bàn này", "Thông báo", JOptionPane.WARNING_MESSAGE);
//				return;
//			}
			
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
	        if ("Chờ xác nhận".equals(trangThai)) {
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
 
    /**
     * Xác nhận phiếu đặt: Chuyển phiếu sang "Đã xác nhận", tạo hóa đơn, cập nhật trạng thái bàn.
     * Luồng xử lý:
     * 1. Cập nhật phiếu đặt: "Đã đặt" → "Đã xác nhận"
     * 2. Cập nhật trạng thái bàn: "Đã đặt" → "Đang sử dụng"
     * 3. Tạo hóa đơn mới với thông tin từ phiếu đặt
     * 4. Chuyển chi tiết món ăn từ phiếu đặt sang hóa đơn
     * 5. Chuyển tiền cọc vào hóa đơn
     */
    private void xacNhanPhieu() {
        if (phieuDatBan == null) {
            JOptionPane.showMessageDialog(this,
                "Không có thông tin phiếu đặt!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra trạng thái phiếu
        if (!"Chờ xác nhận".equals(phieuDatBan.getTrangThai())) {
            JOptionPane.showMessageDialog(this,
                "Chỉ có thể xác nhận phiếu ở trạng thái 'Chờ xác nhận'!\n" +
                "Trạng thái hiện tại: " + phieuDatBan.getTrangThai(),
                "Không thể xác nhận", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Xác nhận với người dùng
        int confirm = JOptionPane.showConfirmDialog(this,
            "XÁC NHẬN PHIẾU ĐẶT?\n\n" +
            "Mã phiếu: " + phieuDatBan.getMaPhieuDat() + "\n" +
            "Khách hàng: " + (phieuDatBan.getKhachHang() != null ? phieuDatBan.getKhachHang().getHoTen() : "Khách vãng lai") + "\n" +
            "Số bàn: " + (danhSachBanTrongPhieu != null ? danhSachBanTrongPhieu.size() : 1) + " bàn\n\n" +
            "Sau khi xác nhận:\n" +
            "- Phiếu đặt: 'Chờ xác nhận' → 'Đã xác nhận'\n" +
            "- Bàn: 'Đã đặt' → 'Đang sử dụng'\n" +
            "- Tạo hóa đơn mới (Chưa thanh toán)\n" +
            "- Chuyển tiền cọc vào hóa đơn",
            "Xác nhận phiếu đặt", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        // Khởi tạo DAO
        PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
        BanAnDAO banDAO = new BanAnDAO();
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietPhieuDatDAO ctpdDAO = new ChiTietPhieuDatDAO();
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
        
        String maPhieuDat = phieuDatBan.getMaPhieuDat();
        String maNV = Session.getMaNhanVienDangNhap();

        try {
           
            // CẬP NHẬT PHIẾU ĐẶT
            
            if (!phieuDAO.capNhatTrangThaiPhieu(maPhieuDat, "Đã xác nhận")) {
                throw new Exception("Không thể cập nhật trạng thái phiếu đặt!");
            }
         
            //  CẬP NHẬT TRẠNG THÁI BÀN
           
            if (danhSachBanTrongPhieu != null && !danhSachBanTrongPhieu.isEmpty()) {
                // Cập nhật tất cả bàn trong phiếu
                for (String maBan : danhSachBanTrongPhieu) {
                    if (!banDAO.capNhatTrangThaiBan(maBan, "Đang sử dụng")) {
                        throw new Exception("Không thể cập nhật trạng thái bàn: " + maBan);
                    }
                }
            } else if (phieuDatBan.getBanAn() != null) {
                // Fallback: chỉ có một bàn
                String maBan = phieuDatBan.getBanAn().getMaBan();
                if (!banDAO.capNhatTrangThaiBan(maBan, "Đang sử dụng")) {
                    throw new Exception("Không thể cập nhật trạng thái bàn: " + maBan);
                }
            }
         
            // BƯỚC 3: TẠO HÓA ĐƠN MỚI        
            String maHoaDon = hoaDonDAO.taoMaHoaDonTuDong();
            double tienCoc = phieuDatBan.getSoTienCoc();

            HoaDon hoaDonMoi = new HoaDon();
            hoaDonMoi.setMaHoaDon(maHoaDon);
            hoaDonMoi.setBanAn(phieuDatBan.getBanAn());
            hoaDonMoi.setKhachHang(phieuDatBan.getKhachHang());
            hoaDonMoi.setNhanVien(new NhanVien(maNV));
            hoaDonMoi.setNgayLapHoaDon(java.time.LocalDateTime.now());
            hoaDonMoi.setThueVAT(0.1); // VAT 10%
            hoaDonMoi.setKhuyenMai(null);
            hoaDonMoi.setTrangThai("Chưa thanh toán");
            hoaDonMoi.setPhieuDat(phieuDatBan);
            hoaDonMoi.setTienCoc(tienCoc); // Chuyển tiền cọc vào hóa đơn

            // TÍNH TỔNG TIỀN TỪ MÓN ĐÃ ĐẶT            
            List<ChiTietPhieuDat> dsCTPD = ctpdDAO.getChiTietTheoMaPhieu(maPhieuDat);
            double tongTienMonAn = 0;
            for (ChiTietPhieuDat ctpd : dsCTPD) {
                tongTienMonAn += ctpd.getSoLuong() * ctpd.getDonGia();
            }

            // Tính tổng tiền bao gồm VAT
            double tongTienSauVAT = tongTienMonAn * (1 + hoaDonMoi.getThueVAT());
            hoaDonMoi.setTongTien(tongTienSauVAT);

            // Lưu hóa đơn vào database
            if (!hoaDonDAO.themHoaDon(hoaDonMoi, Session.getMaNhanVienDangNhap())) {
                throw new Exception("Không thể tạo hóa đơn mới!");
            }

           
            //CHUYỂN CHI TIẾT MÓN ĂN           
            for (ChiTietPhieuDat ctpd : dsCTPD) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setHoaDon(new HoaDon(maHoaDon));
                cthd.setMonAn(ctpd.getMonAn());
                cthd.setSoLuong(ctpd.getSoLuong());
                cthd.setDonGia(ctpd.getDonGia());
                cthd.setThanhTien(ctpd.getSoLuong() * ctpd.getDonGia());
                cthd.setGhiChu(ctpd.getGhiChu());

                if (!cthdDAO.themChiTietHoaDon(cthd, Session.getMaNhanVienDangNhap())) {
                    throw new Exception("Không thể thêm chi tiết hóa đơn cho món: " + ctpd.getMonAn().getTenMon());
                }
            }

            
            // THÔNG BÁO THÀNH CÔNG            
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            JOptionPane.showMessageDialog(this,
                "✅ XÁC NHẬN PHIẾU ĐẶT THÀNH CÔNG!\n\n" +
                "📋 Mã hóa đơn: " + maHoaDon + "\n" +
                "💰 Tiền cọc: " + currencyFormat.format(tienCoc) + "đ\n" +
                "🍽️ Tổng tiền món ăn: " + currencyFormat.format(tongTienMonAn) + "đ\n" +
                "📊 Tổng tiền (đã VAT): " + currencyFormat.format(tongTienSauVAT) + "đ\n\n" +
                "📍 Trạng thái phiếu: Đã xác nhận\n" +
                "🪑 Trạng thái bàn: Đang sử dụng",
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            
           
            // ROLLBACK KHI CÓ LỖI
            
            System.err.println("❌ LỖI KHI XÁC NHẬN PHIẾU - Đang rollback...");
            
            // Khôi phục trạng thái phiếu về "Chờ xác nhận"
            phieuDAO.capNhatTrangThaiPhieu(maPhieuDat, "Chờ xác nhận");
            
            // Khôi phục trạng thái bàn về "Đã đặt"
            if (danhSachBanTrongPhieu != null && !danhSachBanTrongPhieu.isEmpty()) {
                for (String maBan : danhSachBanTrongPhieu) {
                    banDAO.capNhatTrangThaiBan(maBan, "Đã đặt");
                }
            } else if (phieuDatBan.getBanAn() != null) {
                banDAO.capNhatTrangThaiBan(phieuDatBan.getBanAn().getMaBan(), "Đã đặt");
            }
            
            JOptionPane.showMessageDialog(this,
                "❌ LỖI KHI XÁC NHẬN PHIẾU!\n\n" +
                "Chi tiết lỗi: " + e.getMessage() + "\n\n" +
                "Hệ thống đã khôi phục trạng thái ban đầu.",
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Hủy phiếu đặt bàn.
     * Nếu hủy trước giờ đặt hoặc kịp thời, tiền cọc có thể được hoàn lại.
     * hoàn tiền cọc nếu hủy trước 2 giờ.
     */
    private void huyPhieu() {
        if (phieuDatBan == null) {
            JOptionPane.showMessageDialog(this,
                "Không có thông tin phiếu đặt!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double tienCoc = phieuDatBan.getSoTienCoc();
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        // Kiểm tra thời gian để quyết định hoàn cọc
        LocalDate ngayDat = phieuDatBan.getNgayDat().toLocalDate();
        int khungGio = phieuDatBan.getKhungGio();
        LocalDateTime gioHenDat = tinhGioHen(ngayDat, khungGio);
        LocalDateTime gioHienTai = LocalDateTime.now();
        
        boolean coHoanCoc = gioHienTai.isBefore(gioHenDat.minusHours(2));
        String lyDo = coHoanCoc 
            ? "Hủy trước 2 giờ → Hoàn tiền cọc"
            : "Hủy muộn → Không hoàn cọc";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận hủy phiếu đặt?\n\n" +
            "Mã phiếu: " + phieuDatBan.getMaPhieuDat() + "\n" +
            "Khách hàng: " + (phieuDatBan.getKhachHang() != null ? phieuDatBan.getKhachHang().getHoTen() : "Khách vãng lai") + "\n" +
            "Tiền cọc: " + currencyFormat.format(tienCoc) + "đ\n\n" +
            "⏰ " + lyDo,
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        String maPhieu = phieuDatBan.getMaPhieuDat();
        PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
        BanAnDAO banDAO = new BanAnDAO();

        try {
            // 1. Cập nhật trạng thái phiếu sang "Đã hủy"
            if (!phieuDAO.huyPhieuDat(maPhieu)) {
                throw new Exception("Không thể cập nhật trạng thái phiếu!");
            }

            // 2. Cập nhật trạng thái TẤT CẢ bàn về "Trống"
            if (danhSachBanTrongPhieu != null && !danhSachBanTrongPhieu.isEmpty()) {
                for (String maBan : danhSachBanTrongPhieu) {
                    if (!banDAO.capNhatTrangThaiBan(maBan, "Trống")) {
                        System.err.println("⚠️ Không thể cập nhật bàn: " + maBan);
                    }
                }
            } else if (phieuDatBan.getBanAn() != null) {
                // Fallback: chỉ cập nhật bàn hiện tại
                String maBan = phieuDatBan.getBanAn().getMaBan();
                if (!banDAO.capNhatTrangThaiBan(maBan, "Trống")) {
                    throw new Exception("Không thể cập nhật trạng thái bàn!");
                }
            }

            // 3. Thông báo kết quả
            String thongBao = "✅ Đã hủy phiếu đặt thành công!\n\n" +
                "Tất cả bàn đã trở về trạng thái 'Trống'.\n";
            
            if (coHoanCoc && tienCoc > 0) {
                thongBao += "💰 Hoàn tiền cọc: " + currencyFormat.format(tienCoc) + "đ";
            } else if (tienCoc > 0) {
                thongBao += "⚠️ Không hoàn tiền cọc (hủy muộn): " + currencyFormat.format(tienCoc) + "đ";
            }
            
            JOptionPane.showMessageDialog(this, 
                thongBao,
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "❌ Lỗi khi hủy phiếu: " + e.getMessage(),
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tính giờ hẹn dựa trên ngày đặt và khung giờ.
     */
    private LocalDateTime tinhGioHen(LocalDate ngayDat, int khungGio) {
        int gioStart = 0;
        switch (khungGio) {
            case 1: gioStart = 6; break;   // Sáng 6-11h
            case 2: gioStart = 11; break;  // Trưa 11-14h
            case 3: gioStart = 14; break;  // Chiều 14-17h
            case 4: gioStart = 17; break;  // Tối 17-22h
            default: gioStart = 12;
        }
        return ngayDat.atTime(gioStart, 0);
    }
    
 
//     In phiếu đặt    
    private void inPhieu() {
        JOptionPane.showMessageDialog(this,
            "Chức năng in phiếu đặt sẽ được cài đặt sau!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

	
}