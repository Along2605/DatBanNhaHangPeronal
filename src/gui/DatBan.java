package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;

import dao.BanAnDAO;
import dao.KhachHangDAO;
import dao.KhuVucDAO;
import dao.LoaiBanDAO;
import dao.PhieuDatBanDAO;
import entity.BanAn;
import entity.KhachHang;
import entity.LoaiBan;
import entity.NhanVien;
import entity.PhieuDatBan;
import util.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DatBan extends JPanel implements ActionListener, MouseListener{

    private static final long serialVersionUID = 1L;
   
    private JTextField txtMaPhieuDat;
    private JTextField txtSDTKhachHang;
    private JTextField txtTenKhachHang;
    private JTextField txtSoNguoi;
    private JTextField txtSoTienCoc;
    private JDateChooser dateChooserNgayDat;
    private JSpinner spinnerGioDat;
    private JComboBox<String> cboKhuVuc;
    private JComboBox<String> cboLoaiBan;
    private JTextArea txtGhiChu;
    
    private List<String> danhSachBanDaChon = new ArrayList<>(); // Lưu mã bàn
    private JTextArea txtDanhSachBanDaChon; // Hiển thị danh sách bàn đã chọn
    
    private JTable tableBanTrong;
    private DefaultTableModel tableModel;
    
    private JButton btnTimKhachHang;
    private JButton btnTimBan;
    private JButton btnDatBan;
    private JButton btnLamMoi;
    
    private PhieuDatBanDAO phieuDatDAO;
    private KhachHangDAO khachHangDAO;
    
    private String maKhachHang = null;
    
    BanAnDAO banAnDAO = new BanAnDAO();
    private KhuVucDAO khuVucDAO = new KhuVucDAO();
    private LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
    // Colors
    private final Color MAU_CAM = new Color(214, 116, 76); // MAIN_COLOR
    private final Color MAU_CAM_SANG = new Color(234, 136, 96); // HOVER_COLOR
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    
    private static final int KHUNG_SANG = 1;    // 06:00 - 11:00
    private static final int KHUNG_TRUA = 2;    // 11:00 - 14:00
    private static final int KHUNG_CHIEU = 3;   // 14:00 - 17:00
    private static final int KHUNG_TOI = 4;     // 17:00 - 22:00

	private JButton btnDatMon;

//	private String maNhanVienDangDangNhap;
    
    public DatBan() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
                
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);     
        
        phieuDatDAO= new PhieuDatBanDAO();
        banAnDAO= new BanAnDAO();
        khachHangDAO= new KhachHangDAO();
        loadDanhSachBanTrong();
        
        taoMaPhieuDatTuDong();
        
    }
    
    

	private void taoMaPhieuDatTuDong() {
		try {
	        String maPhieuDat = phieuDatDAO.taoMaPhieuDatTuDong();
	        txtMaPhieuDat.setText(maPhieuDat);
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Nếu lỗi, tạo mã random
	        String prefix = "PD";
	        String dateStr = new java.text.SimpleDateFormat("yyMMdd").format(new Date());
	        String stt = String.format("%03d", (int)(Math.random() * 1000));
	        txtMaPhieuDat.setText(prefix + dateStr + stt);
	    }
		
		
	}



	// panel tiêu đề
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel lblTitle = new JLabel("ĐẶT BÀN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(MAU_CAM);
        panel.add(lblTitle);
        
        return panel;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0)); //1 dòng, 2 cột
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel bên trái: Thông tin đặt bàn
        panel.add(createFormPanel());
        
        // Panel bên phải: Danh sách bàn trống
        panel.add(createTablePanel());
        
        return panel;
    }
    
    // form nhập tt đặt bàn
    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAU_CAM, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Title
        JLabel lblFormTitle = new JLabel("Thông tin đặt bàn");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(MAU_CAM);
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblFormTitle, BorderLayout.NORTH);
        
        // Form panel with scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // Mã phiếu đặt (tự động)
        txtMaPhieuDat = createTextField();
       
        txtMaPhieuDat.setEditable(false);
        txtMaPhieuDat.setBackground(new Color(240, 240, 240));
        addFormField(formPanel, gbc, row++, "Mã phiếu đặt:", txtMaPhieuDat);
        
        // Số điện thoại khách hàng + nút tìm
        txtSDTKhachHang = createTextField();
        btnTimKhachHang = createSmallButton("", "img\\searchIcon.png");
        
        btnTimKhachHang.setToolTipText("Tìm khách hàng");
        
        JPanel sdtPanel = new JPanel(new BorderLayout(5, 0));
        sdtPanel.setBackground(Color.WHITE);
        sdtPanel.add(txtSDTKhachHang, BorderLayout.CENTER);
        sdtPanel.add(btnTimKhachHang, BorderLayout.EAST);
        
        btnTimKhachHang.addActionListener(this);
        addFormField(formPanel, gbc, row++, "SĐT khách hàng: *", sdtPanel);
        
        // Tên khách hàng (hiển thị sau khi tìm)
        txtTenKhachHang = createTextField();
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setBackground(new Color(240, 240, 240));
        addFormField(formPanel, gbc, row++, "Tên khách hàng:", txtTenKhachHang);
        
        // Ngày đặt
        dateChooserNgayDat = new JDateChooser();
        dateChooserNgayDat.setDateFormatString("dd/MM/yyyy");
        dateChooserNgayDat.setMinSelectableDate(new Date()); // Chỉ chọn từ hôm nay
        dateChooserNgayDat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateChooserNgayDat.setPreferredSize(new Dimension(0, 30));
        addFormField(formPanel, gbc, row++, "Ngày đặt: *", dateChooserNgayDat);
        
        // Giờ đặt
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        spinnerGioDat = new JSpinner(spinnerModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerGioDat, "HH:mm");
        spinnerGioDat.setEditor(editor);
        
        // Set giờ mặc định 08:00
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        spinnerGioDat.setValue(cal.getTime());
        
        spinnerGioDat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addFormField(formPanel, gbc, row++, "Giờ đặt: *", spinnerGioDat);
        
        // Số người
        txtSoNguoi = createTextField();
        addFormField(formPanel, gbc, row++, "Số người: *", txtSoNguoi);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JSeparator separator1 = new JSeparator();
        separator1.setBorder(new EmptyBorder(10, 0, 10, 0));
        formPanel.add(separator1, gbc);
        gbc.gridwidth = 1;
        
        // Tiêu đề tìm bàn
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel lblTimBan = new JLabel("Tiêu chí tìm bàn");
        lblTimBan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTimBan.setForeground(MAU_CAM);
        formPanel.add(lblTimBan, gbc);
        gbc.gridwidth = 1;
        
        // Khu vực
        cboKhuVuc = createComboBox(new String[]{"-- Tất cả --"});
        loadKhuVucData();
        addFormField(formPanel, gbc, row++, "Khu vực:", cboKhuVuc);
        
        // Loại bàn
        cboLoaiBan = createComboBox(new String[]{"-- Tất cả --"});
        loadLoaiBanData();
        addFormField(formPanel, gbc, row++, "Loại bàn:", cboLoaiBan);
        
        // Nút tìm bàn
        btnTimBan = createButton("Tìm bàn trống", MAU_CAM);
        btnTimBan.addActionListener(e -> {
            // Kiểm tra đã chọn ngày giờ chưa
            if (dateChooserNgayDat.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ngày đặt trước!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            loadDanhSachBanTrong(); // Load lại với khung giờ mới
        });
        
        btnTimBan.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(btnTimBan, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JSeparator separator2 = new JSeparator();
        separator2.setBorder(new EmptyBorder(10, 0, 10, 0));
        formPanel.add(separator2, gbc);
        gbc.gridwidth = 1;
        
        // Số tiền cọc
        txtSoTienCoc = createTextField();
        txtSoTienCoc.setText("0");
        addFormField(formPanel, gbc, row++, "Số tiền cọc:", txtSoTienCoc);
        
     // Danh sách bàn đã chọn
        txtDanhSachBanDaChon = new JTextArea(3, 20);
        txtDanhSachBanDaChon.setEditable(false);
        txtDanhSachBanDaChon.setBackground(new Color(240, 240, 240));
        txtDanhSachBanDaChon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDanhSachBanDaChon.setLineWrap(true);
        txtDanhSachBanDaChon.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane scrollBanDaChon = new JScrollPane(txtDanhSachBanDaChon);
        scrollBanDaChon.setPreferredSize(new Dimension(0, 70));

        addFormField(formPanel, gbc, row++, "Bàn đã chọn:", scrollBanDaChon);
        
        
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
        scrollGhiChu.setPreferredSize(new Dimension(0, 70));
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGhiChu.setVerticalAlignment(JLabel.TOP);
        formPanel.add(lblGhiChu, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(scrollGhiChu, gbc);
        
        // Wrap formPanel in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
     // ✅ Tự động load bàn khi thay đổi ngày
        dateChooserNgayDat.addPropertyChangeListener("date", evt -> {
            if (dateChooserNgayDat.getDate() != null) {
                loadDanhSachBanTrong();
            }
        });

        // ✅ Tự động load bàn khi thay đổi giờ
        spinnerGioDat.addChangeListener(e -> {
            if (dateChooserNgayDat.getDate() != null) {
                loadDanhSachBanTrong();
            }
        });

        // ✅ Tự động load bàn khi thay đổi khu vực
        cboKhuVuc.addActionListener(e -> {
            if (dateChooserNgayDat.getDate() != null && 
                cboKhuVuc.getSelectedIndex() > 0) {
                loadDanhSachBanTrong();
            }
        });

        // ✅ Tự động load bàn khi thay đổi loại bàn
        cboLoaiBan.addActionListener(e -> {
            if (dateChooserNgayDat.getDate() != null && 
                cboLoaiBan.getSelectedIndex() > 0) {
                loadDanhSachBanTrong();
            }
        });

        
        return mainPanel;
    }
    
    private void loadLoaiBanData() {
        try {
            List<LoaiBan> dsLoaiBan = loaiBanDAO.getAllLoaiBan();
            for (LoaiBan lb : dsLoaiBan) {
                cboLoaiBan.addItem(lb.getTenLoaiBan());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải danh sách loại bàn: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void loadKhuVucData() {
        try {
            ArrayList<String> dsKhuVuc = khuVucDAO.layTenKhuVuc();
            for (String tenKhuVuc : dsKhuVuc) {
                cboKhuVuc.addItem(tenKhuVuc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải danh sách khu vực: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



	// bảng danh sách bàn trống
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAU_CAM, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Title with info
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel lblTableTitle = new JLabel("Danh sách bàn trống");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTableTitle.setForeground(MAU_CAM);
        titlePanel.add(lblTableTitle, BorderLayout.WEST);
        
        JLabel lblInfo = new JLabel("(Chọn bàn để đặt)");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        titlePanel.add(lblInfo, BorderLayout.EAST);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {
            "Mã bàn", "Tên bàn", "Số lượng chỗ", 
            "Loại bàn", "Khu vực", "Ghi chú"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableBanTrong = new JTable(tableModel);
        tableBanTrong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableBanTrong.setRowHeight(30);
        tableBanTrong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBanTrong.setShowGrid(true);
        tableBanTrong.setGridColor(new Color(230, 230, 230));
        
        // Header style
        JTableHeader header = tableBanTrong.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(MAU_CAM);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        // Column widths
        
        tableBanTrong.getColumnModel().getColumn(0).setPreferredWidth(80);   
        tableBanTrong.getColumnModel().getColumn(1).setPreferredWidth(120);  
        tableBanTrong.getColumnModel().getColumn(2).setPreferredWidth(120);  
        tableBanTrong.getColumnModel().getColumn(3).setPreferredWidth(80);   
        tableBanTrong.getColumnModel().getColumn(4).setPreferredWidth(100);  
        tableBanTrong.getColumnModel().getColumn(5).setPreferredWidth(150);  
        
        JScrollPane scrollPane = new JScrollPane(tableBanTrong);
        tableBanTrong.addMouseListener(this);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(232, 245, 233));
        infoPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel lblTableInfo = new JLabel("Click vào bàn để thêm/bỏ chọn (Có thể chọn nhiều bàn)");
        lblTableInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTableInfo.setForeground(new Color(46, 125, 50));
        infoPanel.add(lblTableInfo);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
      
    //Tạo panel chứa các nút chức năng     
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        btnDatBan = createButton("Đặt bàn", SUCCESS_COLOR);
        btnDatBan.setPreferredSize(new Dimension(150, 40));
        btnDatBan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        btnLamMoi = createButton("Làm mới", new Color(100, 100, 100));
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        
        
        btnDatMon = createButton("Đặt món", MAU_CAM);
        btnDatMon.setPreferredSize(new Dimension(150, 40));
        btnDatMon.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        panel.add(btnDatBan);
        panel.add(btnLamMoi);
        panel.add(btnDatMon);
        
        btnDatBan.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnDatMon.addActionListener(this);
        
        return panel;
    }
    
    
    /**
     * Tải danh sách bàn trống theo ngày và giờ đã chọn.
     * Chỉ hiển thị bàn khả dụng trong khung giờ cụ thể.
     */
    private void loadDanhSachBanTrong() {
        try {
            tableModel.setRowCount(0);
            
            Date ngayDat = dateChooserNgayDat.getDate();
            Date gioDat = (Date) spinnerGioDat.getValue();

            List<BanAn> dsBanTrong;

            if (ngayDat != null && gioDat != null) {
                Calendar calNgay = Calendar.getInstance();
                calNgay.setTime(ngayDat);

                Calendar calGio = Calendar.getInstance();
                calGio.setTime(gioDat);

                calNgay.set(Calendar.HOUR_OF_DAY, calGio.get(Calendar.HOUR_OF_DAY));
                calNgay.set(Calendar.MINUTE, calGio.get(Calendar.MINUTE));
                calNgay.set(Calendar.SECOND, 0);

                LocalDateTime ngayGioDat = new java.sql.Timestamp(
                    calNgay.getTimeInMillis()).toLocalDateTime();

                int khungGio = xacDinhKhungGio(ngayGioDat.getHour());

                // ✅ Lọc theo khu vực và loại bàn nếu đã chọn
                String khuVucChon = cboKhuVuc.getSelectedItem().toString();
                String loaiBanChon = cboLoaiBan.getSelectedItem().toString();

                dsBanTrong = banAnDAO.getDSBanTrongTheoKhungGio(
                    ngayGioDat.toLocalDate(), khungGio
                );
                
                // Lọc thêm theo tiêu chí
                if (!khuVucChon.equals("-- Tất cả --")) {
                    dsBanTrong = dsBanTrong.stream()
                        .filter(ban -> ban.getKhuVuc() != null && 
                                       ban.getKhuVuc().getTenKhuVuc().contains(khuVucChon))
                        .collect(Collectors.toList());
                }
                
                if (!loaiBanChon.equals("-- Tất cả --")) {
                    dsBanTrong = dsBanTrong.stream()
                        .filter(ban -> ban.getLoaiBan().getTenLoaiBan().contains(loaiBanChon))
                        .collect(Collectors.toList());
                }
            } else {
                dsBanTrong = banAnDAO.getDSBanTrong();
            }

            if (dsBanTrong == null || dsBanTrong.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có bàn trống trong khung giờ này!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (BanAn ban : dsBanTrong) {
                tableModel.addRow(new Object[]{
                    ban.getMaBan(),
                    ban.getTenBan(),
                    ban.getSoLuongCho(),
                    ban.getLoaiBan().getTenLoaiBan(),
                    ban.getKhuVuc() != null ? ban.getKhuVuc().getTenKhuVuc() : "",
                    ban.getGhiChu()
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách bàn: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // them field vào form
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
                              String labelText, JComponent component) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        // field bat buoc nhap
        if (labelText.contains("*")) {
            label.setForeground(new Color(183, 28, 28));
        }
        panel.add(label, gbc);
        
        // Component
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(component, gbc);
    }
    
    // text field với style đồng nhất
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }
    
    // combo box với style đồng nhất
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        return comboBox;
    }
    
    
    
    // button với style đồng nhất
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        // xóa viền nút
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 35));
        
        // đổi màu nền khi di chuột
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
      
    
    // tạo nút nhỏ
    private JButton createSmallButton(String text, String iconPath) {
    	JButton button;

        // Nếu có iconPath, thì tạo JButton với icon (và text nếu có)
        if (iconPath != null && !iconPath.isEmpty()) {
            // Tải icon từ file hoặc từ classpath
            ImageIcon icon = new ImageIcon(iconPath);

            // Resize icon 
            Image scaledImage = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Tạo button với icon + text (nếu text không rỗng)
            button = (text == null || text.isEmpty())
                    ? new JButton(scaledIcon)
                    : new JButton(text, scaledIcon);
        } else {
            // Không có icon, chỉ tạo button với text
            button = new JButton(text);
        }

        button.setPreferredSize(new Dimension(40, 30));
        button.setBackground(MAU_CAM);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o= e.getSource();
		if(o== btnDatBan) {
			datBan();
		}
		else if(o== btnTimKhachHang) {
			timKhachHang();
		}
		else if(o== btnLamMoi) {
			lamMoiForm();
		}
		else if(o== btnDatMon) {
//			DialogChonMonAn dialog = new DialogChonMonAn((Frame) SwingUtilities.getWindowAncestor(btnDatMon));
//	        dialog.setVisible(true); // Mở dialog
		}
		
	}

	private void timKhachHang() {
		// TODO Auto-generated method stub
		String sdt = txtSDTKhachHang.getText().trim();
	    
	    // Validate
	    if (sdt.isEmpty()) {
	        JOptionPane.showMessageDialog(this,
	            "Vui lòng nhập số điện thoại!",
	            "Thông báo", JOptionPane.WARNING_MESSAGE);
	        txtSDTKhachHang.requestFocus();
	        return;
	    }
	    
	    // Kiểm tra format SĐT
	    if (!sdt.matches("^0\\d{9,10}$")) {
	        JOptionPane.showMessageDialog(this,
	            "Số điện thoại không hợp lệ!\n" +
	            "Vui lòng nhập số điện thoại 10-11 số, bắt đầu bằng 0",
	            "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtSDTKhachHang.requestFocus();
	        return;
	    }
	    try {
	        // Tìm khách hàng từ database
	        entity.KhachHang kh = khachHangDAO.timKhachHangTheoSDT(sdt);
	        
	        if (kh != null) {
	            // Tìm thấy khách hàng
	            maKhachHang = kh.getMaKH();
	            txtTenKhachHang.setText(kh.getHoTen());
	            
	            JOptionPane.showMessageDialog(this,
	                "Tìm thấy khách hàng!\n\n" +
	                "Họ tên: " + kh.getHoTen() + "\n" +
	                "Điểm tích lũy: " + kh.getDiemTichLuy(),
	                "Thành công", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            // Không tìm thấy
	            maKhachHang = null;
	            txtTenKhachHang.setText("");
	            
	            int choice = JOptionPane.showConfirmDialog(this,
	                "Không tìm thấy khách hàng với SĐT: " + sdt + "\n\n" +
	                "Bạn có muốn thêm khách hàng mới không?",
	                "Thông báo", JOptionPane.YES_NO_OPTION);
	            
	            if (choice == JOptionPane.YES_OPTION) {
	                // Mở form thêm khách hàng
	                moFormThemKhachHang(sdt);
	            }
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this,
	            "Lỗi khi tìm khách hàng!\n" + e.getMessage(),
	            "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
		
	}



	private void moFormThemKhachHang(String sdt) {
		// TODO Auto-generated method stub
		// Tạo dialog đơn giản để thêm khách hàng
	    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
	                                 "Thêm khách hàng mới", true);
	    dialog.setSize(400, 300);
	    dialog.setLocationRelativeTo(this);
	    dialog.setLayout(new BorderLayout(10, 10));
	    
	    JPanel mainPanel = new JPanel(new GridBagLayout());
	    mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
	    mainPanel.setBackground(Color.WHITE);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(8, 5, 8, 5);
	    
	    // Họ tên
	    gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
	    mainPanel.add(new JLabel("Họ tên: *"), gbc);
	    
	    gbc.gridx = 1; gbc.weightx = 0.7;
	    JTextField txtHoTen = createTextField();
	    mainPanel.add(txtHoTen, gbc);
	    
	    // SĐT (đã có sẵn)
	    gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
	    mainPanel.add(new JLabel("SĐT:"), gbc);
	    
	    gbc.gridx = 1; gbc.weightx = 0.7;
	    JTextField txtSDT = createTextField();
	    txtSDT.setText(sdt);
	    txtSDT.setEditable(false);
	    txtSDT.setBackground(new Color(240, 240, 240));
	    mainPanel.add(txtSDT, gbc);
	    
	    // Giới tính
	    gbc.gridx = 0; gbc.gridy = 2;
	    mainPanel.add(new JLabel("Giới tính:"), gbc);
	    
	    gbc.gridx = 1;
	    JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
	    mainPanel.add(cboGioiTinh, gbc);
	    
	    dialog.add(mainPanel, BorderLayout.CENTER);
	    
	    // Panel nút
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.setBackground(Color.WHITE);
	    
	    JButton btnLuu = createButton("Lưu", SUCCESS_COLOR);
	    btnLuu.setPreferredSize(new Dimension(100, 35));
	    btnLuu.addActionListener(e -> {
	        String hoTen = txtHoTen.getText().trim();
	        
	        if (hoTen.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog,
	                "Vui lòng nhập họ tên!",
	                "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        try {
	            // Thêm khách hàng mới
	            String maKH = khachHangDAO.taoMaKhachHangTuDong();
	            boolean gioiTinh = cboGioiTinh.getSelectedIndex() == 0; // true = Nam
	            
	            entity.KhachHang khMoi = new entity.KhachHang();
	            khMoi.setMaKH(maKH);
	            khMoi.setHoTen(hoTen);
	            khMoi.setSdt(sdt);
	            khMoi.setGioiTinh(gioiTinh);
	            khMoi.setDiemTichLuy(0);
	            
	            boolean success = khachHangDAO.themKhachHang(khMoi, Session.getMaNhanVienDangNhap());
	            
	            if (success) {
	                maKhachHang = maKH;
	                txtTenKhachHang.setText(hoTen);
	                
	                JOptionPane.showMessageDialog(dialog,
	                    "Thêm khách hàng thành công!",
	                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                
	                dialog.dispose();
	            } else {
	                JOptionPane.showMessageDialog(dialog,
	                    "Thêm khách hàng thất bại!",
	                    "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog,
	                "Lỗi khi thêm khách hàng!\n" + ex.getMessage(),
	                "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    
	    JButton btnHuy = createButton("Hủy", new Color(100, 100, 100));
	    btnHuy.setPreferredSize(new Dimension(100, 35));
	    btnHuy.addActionListener(e -> dialog.dispose());
	    
	    buttonPanel.add(btnLuu);
	    buttonPanel.add(btnHuy);
	    
	    dialog.add(buttonPanel, BorderLayout.SOUTH);
	    dialog.setVisible(true);
		
		
	}


	// ================================
	// FIX CHO FILE DatBan.java
	// Thêm validation trước khi lưu phiếu đặt
	// ================================

	private void datBan() {
	    // 1. KIỂM TRA CƠ BẢN
	    if (danhSachBanDaChon.isEmpty()) {
	        JOptionPane.showMessageDialog(this,
	            "Vui lòng chọn ít nhất một bàn để đặt!",
	            "Thông báo", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    try {
	        // 2. XỬ LÝ NGÀY GIỜ
	        Date ngayDat = dateChooserNgayDat.getDate();
	        Date gioDat = (Date) spinnerGioDat.getValue();
	        
	        if (ngayDat == null) {
	            JOptionPane.showMessageDialog(this, 
	                "Vui lòng chọn ngày đặt!", 
	                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        java.util.Calendar calNgay = java.util.Calendar.getInstance();
	        calNgay.setTime(ngayDat);
	        java.util.Calendar calGio = java.util.Calendar.getInstance();
	        calGio.setTime(gioDat);

	        calNgay.set(java.util.Calendar.HOUR_OF_DAY, calGio.get(java.util.Calendar.HOUR_OF_DAY));
	        calNgay.set(java.util.Calendar.MINUTE, calGio.get(java.util.Calendar.MINUTE));
	        calNgay.set(java.util.Calendar.SECOND, 0);

	        LocalDateTime ngayGioDat = new java.sql.Timestamp(calNgay.getTimeInMillis()).toLocalDateTime();
	        int khungGio = xacDinhKhungGio(ngayGioDat.getHour());

	        // 3. KIỂM TRA KHÔNG ĐẶT QUÁ KHỨ
	        if (ngayGioDat.isBefore(LocalDateTime.now())) {
	            JOptionPane.showMessageDialog(this, 
	                "Thời gian đặt bàn không hợp lệ!\nKhông thể đặt cho thời điểm trong quá khứ.",
	                "Lỗi thời gian", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // ============================================
	        // 🔥 THÊM: KIỂM TRA BÀN CÓ KHẢ DỤNG KHÔNG
	        // ============================================
	        List<String> banKhongKhaDung = new ArrayList<>();
	        for (String maBan : danhSachBanDaChon) {
	            if (!banAnDAO.kiemTraBanKhaDung(maBan, ngayGioDat.toLocalDate(), khungGio)) {
	                String tenBan = timTenBanTheoMa(maBan);
	                banKhongKhaDung.add(tenBan + " (" + maBan + ")");
	            }
	        }

	        if (!banKhongKhaDung.isEmpty()) {
	            JOptionPane.showMessageDialog(this,
	                "Các bàn sau đã bị đặt trong khung giờ này:\n\n" + 
	                String.join("\n", banKhongKhaDung) + "\n\n" +
	                "Vui lòng chọn bàn khác hoặc khung giờ khác!",
	                "Bàn không khả dụng", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        // 4. VALIDATE SỐ NGƯỜI
	        String soNguoiStr = txtSoNguoi.getText().trim();
	        if (soNguoiStr.isEmpty() || !soNguoiStr.matches("\\d+")) {
	            JOptionPane.showMessageDialog(this, 
	                "Vui lòng nhập số người hợp lệ!", 
	                "Lỗi", JOptionPane.ERROR_MESSAGE);
	            txtSoNguoi.requestFocus();
	            return;
	        }
	        int soNguoi = Integer.parseInt(soNguoiStr);

	        // Tính tổng số chỗ
	        int tongSoCho = 0;
	        StringBuilder dsBanText = new StringBuilder();
	        for (String maBan : danhSachBanDaChon) {
	            String tenBan = timTenBanTheoMa(maBan);
	            int soCho = laySoChoTheoMaBan(maBan);
	            tongSoCho += soCho;
	            dsBanText.append("- ").append(tenBan).append(" (").append(soCho).append(" chỗ)").append("\n");
	        }

	        // Cảnh báo quá tải
	        if (soNguoi > tongSoCho) {
	            int confirmCho = JOptionPane.showConfirmDialog(this,
	                "Cảnh báo: Số người (" + soNguoi + ") vượt quá tổng số chỗ (" + tongSoCho + ")!\n" +
	                "Bạn có chắc chắn muốn tiếp tục không?",
	                "Cảnh báo quá tải", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	            if (confirmCho != JOptionPane.YES_OPTION) return;
	        }

	        // 5. XỬ LÝ TIỀN CỌC
	        String soTienText = txtSoTienCoc.getText().trim().replaceAll("[^0-9.]", "");
	        double soTienCoc = 0;
	        if (!soTienText.isEmpty()) {
	            try {
	                soTienCoc = Double.parseDouble(soTienText);
	            } catch (NumberFormatException e) {
	                JOptionPane.showMessageDialog(this, 
	                    "Tiền cọc không hợp lệ!", 
	                    "Lỗi", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	        }

	        String ghiChu = txtGhiChu.getText().trim();

	        // 6. KIỂM TRA KHÁCH HÀNG
	        KhachHang kh = (maKhachHang != null) ? new KhachHang(maKhachHang) : null;
	        if (kh == null) {
	            JOptionPane.showMessageDialog(this, 
	                "Vui lòng chọn khách hàng!", 
	                "Thông báo", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        // 7. XÁC NHẬN CUỐI CÙNG
	        int confirm = JOptionPane.showConfirmDialog(this,
	            "XÁC NHẬN ĐẶT BÀN?\n\n" +
	            "Khách hàng: " + txtTenKhachHang.getText() + "\n" +
	            "Thời gian: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(calNgay.getTime()) + "\n" +
	            "Số người: " + soNguoi + "\n" +
	            "Tiền cọc: " + String.format("%,.0f", soTienCoc) + " VNĐ\n" +
	            "Danh sách bàn (" + danhSachBanDaChon.size() + " bàn):\n" + dsBanText.toString(),
	            "Xác nhận", JOptionPane.YES_NO_OPTION);

	        if (confirm != JOptionPane.YES_OPTION) return;

	        // 8. LẤY NHÂN VIÊN
	        NhanVien nhanVienDangNhap = util.Session.getNhanVienDangNhap();
	        if (nhanVienDangNhap == null) {
	            JOptionPane.showMessageDialog(this, 
	                "Phiên đăng nhập đã hết hạn!", 
	                "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // 9. TẠO PHIẾU ĐẶT
	        PhieuDatBan phieuDat = new PhieuDatBan();
	        phieuDat.setMaPhieuDat(txtMaPhieuDat.getText());
	        phieuDat.setKhachHang(kh);
	        phieuDat.setNhanVien(nhanVienDangNhap);
	        phieuDat.setNgayDat(ngayGioDat);
	        phieuDat.setKhungGio(khungGio); // 🔥 THÊM: Lưu khung giờ
	        phieuDat.setSoNguoi(soNguoi);
	        phieuDat.setSoTienCoc(soTienCoc);
	        phieuDat.setGhiChu(ghiChu);
	        phieuDat.setTrangThai("Chờ xác nhận"); // Trạng thái mới tạo

	        // 10. LƯU VÀO DATABASE (TRANSACTION)
	        boolean taoPhieuThanhCong = phieuDatDAO.taoPhieuDat(phieuDat);

	        if (taoPhieuThanhCong) {
	            boolean luuChiTietThanhCong = true;
	            
	            // 🔥 Lưu danh sách bàn vào ChiTietDatBan
	            for (String maBan : danhSachBanDaChon) {
	                boolean themBan = phieuDatDAO.themBanVaoPhieuDat(
	                    txtMaPhieuDat.getText(), 
	                    maBan, 
	                    ""
	                );
	                
	                if (!themBan) {
	                    luuChiTietThanhCong = false;
	                    break;
	                }
	            }
	            
	            if (luuChiTietThanhCong) {
	                // 🔥 CẬP NHẬT TRẠNG THÁI BÀN SANG "ĐÃ ĐẶT"
	                for (String maBan : danhSachBanDaChon) {
	                    banAnDAO.capNhatTrangThaiBan(maBan, "Đã đặt");
	                }
	                
	                JOptionPane.showMessageDialog(this,
	                    "Đặt bàn thành công!\n" +
	                    "Mã phiếu: " + txtMaPhieuDat.getText() + "\n" +
	                    "Vui lòng nhắc khách đến đúng giờ.",
	                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                
	                loadDanhSachBanTrong(); 
	                lamMoiForm();
	            } else {
	                JOptionPane.showMessageDialog(this,
	                    "Đã tạo phiếu nhưng có lỗi khi lưu danh sách bàn!",
	                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	            }
	        } else {
	            JOptionPane.showMessageDialog(this,
	                "Lỗi: Không thể tạo phiếu đặt bàn!",
	                "Thất bại", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this,
	            "Lỗi hệ thống khi đặt bàn:\n" + e.getMessage(),
	            "Lỗi ngoại lệ", JOptionPane.ERROR_MESSAGE);
	    }
	}


	private int laySoChoTheoMaBan(String maBan) {
	    for (int i = 0; i < tableModel.getRowCount(); i++) {
	        if (tableModel.getValueAt(i, 0).toString().equals(maBan)) {
	            return Integer.parseInt(tableModel.getValueAt(i, 2).toString());
	        }
	    }
	    return 0;
	}



	private void lamMoiForm() {
		// TODO Auto-generated method stub
		taoMaPhieuDatTuDong();
		txtSDTKhachHang.setText("");
		txtTenKhachHang.setText("");
		txtSoNguoi.setText("");
		txtSoTienCoc.setText("0");
		txtGhiChu.setText("");
		
		dateChooserNgayDat.setDate(null);
		spinnerGioDat.setValue(new Date());
		
		cboKhuVuc.setSelectedIndex(0);
	    cboLoaiBan.setSelectedIndex(0);
	    
	    tableBanTrong.clearSelection();
	    maKhachHang = null;
	    
	    txtSDTKhachHang.requestFocus();
	    
	 // Xóa danh sách bàn đã chọn
	    danhSachBanDaChon.clear();
	    txtDanhSachBanDaChon.setText("Chưa chọn bàn nào");
	}



	@Override
	public void mouseClicked(MouseEvent e) {
	    if (e.getSource() == tableBanTrong && e.getClickCount() == 1) {
	        int row = tableBanTrong.getSelectedRow();
	        if (row != -1) {
	            String maBan = tableModel.getValueAt(row, 0).toString();
	            String tenBan = tableModel.getValueAt(row, 1).toString();
	            
	            // Kiểm tra xem bàn đã được chọn chưa
	            if (danhSachBanDaChon.contains(maBan)) {
	                // Nếu đã chọn -> Bỏ chọn
	                danhSachBanDaChon.remove(maBan);
	                JOptionPane.showMessageDialog(this, 
	                    "Đã bỏ chọn: " + tenBan,
	                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                // Nếu chưa chọn -> Thêm vào danh sách
	                danhSachBanDaChon.add(maBan);
	                JOptionPane.showMessageDialog(this, 
	                    "Đã thêm: " + tenBan,
	                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            }
	            
	            // Cập nhật hiển thị
	            capNhatHienThiBanDaChon();
	        }
	    }
	}
	
	private void capNhatHienThiBanDaChon() {
	    if (danhSachBanDaChon.isEmpty()) {
	        txtDanhSachBanDaChon.setText("Chưa chọn bàn nào");
	        return;
	    }
	    
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < danhSachBanDaChon.size(); i++) {
	        String maBan = danhSachBanDaChon.get(i);
	        
	        // Tìm tên bàn từ bảng
	        String tenBan = timTenBanTheoMa(maBan);
	        
	        sb.append((i + 1)).append(". ").append(tenBan)
	          .append(" (").append(maBan).append(")");
	        
	        if (i < danhSachBanDaChon.size() - 1) {
	            sb.append("\n");
	        }
	    }
	    
	    txtDanhSachBanDaChon.setText(sb.toString());
	}

	private String timTenBanTheoMa(String maBan) {
	    for (int i = 0; i < tableModel.getRowCount(); i++) {
	        if (tableModel.getValueAt(i, 0).toString().equals(maBan)) {
	            return tableModel.getValueAt(i, 1).toString();
	        }
	    }
	    return maBan;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	// Xác định khung giờ dựa trên giờ trong ngày
	private int xacDinhKhungGio(int gio) {
	    if (gio >= 6 && gio < 11) return KHUNG_SANG;
	    if (gio >= 11 && gio < 14) return KHUNG_TRUA;
	    if (gio >= 14 && gio < 17) return KHUNG_CHIEU;
	    return KHUNG_TOI; // 17:00 - 22:00
	}



	public void tuDongDienThongTin(String maBanChon, LocalDate ngayDat, int khungGio) {
	    try {
	        // 1. Set ngày đặt
	        Calendar cal = Calendar.getInstance();
	        cal.set(ngayDat.getYear(), ngayDat.getMonthValue() - 1, ngayDat.getDayOfMonth());
	        dateChooserNgayDat.setDate(cal.getTime());
	        
	        // 2. Set giờ đặt theo khung giờ
	        Calendar calGio = Calendar.getInstance();
	        switch (khungGio) {
	            case KHUNG_SANG: // 6-11h -> đặt 8h
	                calGio.set(Calendar.HOUR_OF_DAY, 8);
	                break;
	            case KHUNG_TRUA: // 11-14h -> đặt 11h30
	                calGio.set(Calendar.HOUR_OF_DAY, 11);
	                calGio.set(Calendar.MINUTE, 30);
	                break;
	            case KHUNG_CHIEU: // 14-17h -> đặt 14h
	                calGio.set(Calendar.HOUR_OF_DAY, 14);
	                break;
	            case KHUNG_TOI: // 17-22h -> đặt 18h
	                calGio.set(Calendar.HOUR_OF_DAY, 18);
	                break;
	        }
	        calGio.set(Calendar.SECOND, 0);
	        spinnerGioDat.setValue(calGio.getTime());
	        
	        // 3. Load danh sách bàn trống theo khung giờ
	        loadDanhSachBanTrong();
	        
	        // 4. Tự động chọn bàn trong bảng
	        for (int i = 0; i < tableModel.getRowCount(); i++) {
	            String maBan = tableModel.getValueAt(i, 0).toString();
	            if (maBan.equals(maBanChon)) {
	                tableBanTrong.setRowSelectionInterval(i, i);
	                tableBanTrong.scrollRectToVisible(
	                    tableBanTrong.getCellRect(i, 0, true)
	                );
	                
	                // 5. Tự động thêm vào danh sách đã chọn
	                String tenBan = tableModel.getValueAt(i, 1).toString();
	                danhSachBanDaChon.add(maBanChon);
	                capNhatHienThiBanDaChon();
	                
	                // 6. Tự động điền số người dựa vào số chỗ
	                int soCho = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
	                txtSoNguoi.setText(String.valueOf(soCho));
	                
	                // Thông báo
	                JOptionPane.showMessageDialog(this,
	                    "Đã tự động chọn: " + tenBan + "\n" +
	                    "Ngày: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(calGio.getTime()) + "\n" +
	                    "Vui lòng nhập SĐT khách hàng để tiếp tục!",
	                    "Thông tin đặt bàn", JOptionPane.INFORMATION_MESSAGE);
	                
	                // Focus vào ô SĐT
	                txtSDTKhachHang.requestFocus();
	                break;
	            }
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this,
	            "Lỗi khi tự động điền thông tin: " + e.getMessage(),
	            "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}
    
        
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Test - Đặt bàn");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1400, 800);
//            frame.setLocationRelativeTo(null);
//            frame.add(new DatBan());
//            frame.setVisible(true);
//        });
//    }
}