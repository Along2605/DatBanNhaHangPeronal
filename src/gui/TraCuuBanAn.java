package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JDateChooser;

import java.awt.Frame;
import javax.swing.SwingUtilities;

import dao.BanAnDAO;
import dao.KhuVucDAO;
import dao.LoaiBanDAO;
import dao.PhieuDatBanDAO;
import entity.BanAn;
import entity.LoaiBan;
import entity.PhieuDatBan;

public class TraCuuBanAn extends JPanel implements ActionListener, MouseListener{

    private static final long serialVersionUID = 1L;
    
    // Thêm biến để callback khi đặt bàn thành công
    private Runnable onDatBanSuccess;
    private Runnable refreshCallback;
    
    // Components
    private JTextField txtMaBan;
    private JTextField txtTenBan;
    private JComboBox<String> cboKhuVuc;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboLoaiBan;
    private JTextField txtSoLuongCho;
    
    private JDateChooser dateFilter;
    private JComboBox<String> cboTimeFilter;
    
    
    // Hằng số khung giờ
    private static final int KHUNG_SANG = 1;
    private static final int KHUNG_TRUA = 2;
    private static final int KHUNG_CHIEU = 3;
    private static final int KHUNG_TOI = 4;

    
    private JTable tableBanAn;
    private DefaultTableModel tableModel;
    
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXemChiTiet;
    
    BanAnDAO dao = new BanAnDAO();
    private KhuVucDAO khuVucDAO = new KhuVucDAO();
    private LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
    
    // Colors
    private final Color MAIN_COLOR = new Color(214, 116, 76);
    private final Color HOVER_COLOR = new Color(234, 136, 96);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);

	private JLabel lblThongTin;

//	private JButton btnTaoHoaDon;
    
	// Sửa constructor để nhận callback (OPTIONAL - nếu cần refresh từ màn hình chính)
	public TraCuuBanAn() {
	    this(null);
	}

	public TraCuuBanAn(Runnable onDatBanSuccess) {
	    this.onDatBanSuccess = onDatBanSuccess;
	    this.refreshCallback = onDatBanSuccess;
	    setLayout(new BorderLayout(10, 10));
	    setBackground(BACKGROUND_COLOR);
	    setBorder(new EmptyBorder(20, 20, 20, 20));

	    add(createHeaderPanel(), BorderLayout.NORTH);
	    add(createSearchPanel(), BorderLayout.WEST);
	    add(createTablePanel(), BorderLayout.CENTER);
	    add(createButtonPanel(), BorderLayout.SOUTH);

	    loadDanhSachBanAn();

	    btnTimKiem.addActionListener(e -> timKiemBanAn());
	    btnLamMoi.addActionListener(e -> lamMoiForm());
	}
    /**
     * Tạo panel tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel lblTitle = new JLabel("TRA CỨU BÀN ĂN");
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
        
        // Mã bàn
        addFormField(formPanel, gbc, 0, "Mã bàn:", txtMaBan = createTextField());
        
        // Tên bàn
        addFormField(formPanel, gbc, 1, "Tên bàn:", txtTenBan = createTextField());
        
        // Khu vực
        cboKhuVuc = createComboBox(new String[]{"-- Tất cả --"});
        loadKhuVucData();
        addFormField(formPanel, gbc, 2, "Khu vực:", cboKhuVuc);
              
        // Trạng thái (4 trạng thái chuẩn)
        cboTrangThai = createComboBox(new String[]{"-- Tất cả --", "Trống", "Đã đặt", "Đang sử dụng", "Bảo trì"});
        addFormField(formPanel, gbc, 3, "Trạng thái:", cboTrangThai);
        
        // Loại bàn
        cboLoaiBan = createComboBox(new String[]{"-- Tất cả --"});
        loadLoaiBanData();
        addFormField(formPanel, gbc, 4, "Loại bàn:", cboLoaiBan);

        // Số lượng chỗ
        addFormField(formPanel, gbc, 5, "Số lượng chỗ:", txtSoLuongCho = createTextField());        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        

        btnTimKiem = createButton("Tìm kiếm", MAIN_COLOR);
        btnTimKiem.addActionListener(e -> timKiemBanAn());
        btnLamMoi = createButton("Làm mới", new Color(100, 100, 100));
        btnLamMoi.addActionListener(e -> lamMoiForm());
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
//        // Add action listeners
//        addEventListeners();
        
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

	/**
     * Tạo panel bảng dữ liệu
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        JPanel topBarPanel = new JPanel(new BorderLayout(10, 0));
        topBarPanel.setBackground(Color.WHITE);
        topBarPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        
        
        lblThongTin= new JLabel("Tổng số bàn: 0");
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topBarPanel.add(lblThongTin, BorderLayout.WEST);
        // lọc ngày và giờ
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(Color.WHITE);
        
        JLabel lblNgay = new JLabel("Ngày xem:");
        lblNgay.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        dateFilter = new JDateChooser();
        dateFilter.setDateFormatString("dd/MM/yyyy");
        dateFilter.setDate(new Date()); // Mặc định hôm nay
        dateFilter.setPreferredSize(new Dimension(130, 30));
        
        cboTimeFilter = new JComboBox<>(new String[]{"Sáng (6-11h)", "Trưa (11-14h)", "Chiều (14-17h)", "Tối (17-22h)"});
        cboTimeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboTimeFilter.setPreferredSize(new Dimension(120, 30));
        
        // Tự động chọn khung giờ hiện tại
        int currentHour = java.time.LocalTime.now().getHour();
        cboTimeFilter.setSelectedIndex(xacDinhKhungGioIndex(currentHour));

        // ✅ Tự động lọc khi thay đổi ngày hoặc khung giờ
        dateFilter.addPropertyChangeListener("date", e -> loadDanhSachBanAn());
        cboTimeFilter.addActionListener(e -> loadDanhSachBanAn());
        
        filterPanel.add(lblNgay);
        filterPanel.add(dateFilter);
        filterPanel.add(cboTimeFilter);
        
        
        topBarPanel.add(filterPanel, BorderLayout.EAST);
        
        panel.add(topBarPanel, BorderLayout.NORTH);
        
        
        
        // Table
        String[] columns = {
        	"Mã bàn", "Tên bàn", "Số chỗ", "Loại bàn", "Trạng thái", "Khu vực","Ghi chú"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
           
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };
        
        tableBanAn = new JTable(tableModel);
        tableBanAn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableBanAn.setRowHeight(30);
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
        
        tableBanAn.getColumnModel().getColumn(0).setPreferredWidth(80);   
        tableBanAn.getColumnModel().getColumn(1).setPreferredWidth(120);  
        tableBanAn.getColumnModel().getColumn(2).setPreferredWidth(120); 
        tableBanAn.getColumnModel().getColumn(3).setPreferredWidth(80);   
        tableBanAn.getColumnModel().getColumn(4).setPreferredWidth(100);  
        tableBanAn.getColumnModel().getColumn(5).setPreferredWidth(120);  
        tableBanAn.getColumnModel().getColumn(6).setPreferredWidth(200);  
        
        JScrollPane scrollPane = new JScrollPane(tableBanAn);
        tableBanAn.addMouseListener(this);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Tạo panel nút chức năng dưới cùng
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        btnXemChiTiet = createButton("Xem chi tiết", MAIN_COLOR);
        btnXemChiTiet.setPreferredSize(new Dimension(150, 40));
        
        // ✅ THÊM NÚT ĐẶT BÀN NHANH
        
        
//        btnTaoHoaDon = createButton("Tạo hóa đơn", new Color(76, 175, 80));
//        btnTaoHoaDon.setPreferredSize(new Dimension(150, 40));
        
        panel.add(btnXemChiTiet);
//        panel.add(btnTaoHoaDon);
           
        btnXemChiTiet.addActionListener(this);
//        btnTaoHoaDon.addActionListener(this);
        
        return panel;
    }
   

	/**
     * Thêm field vào form
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(label, gbc);
        
        // Component
        gbc.gridx = 1;
        gbc.weightx = 0.7;
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
    
    // chức năng cho các nút
//    private void addEventListeners() {
//       
//    }
    
 
    
    
    private void loadDanhSachBanAn() {
    	try {
			tableModel.setRowCount(0);
			
			Date date = dateFilter.getDate();
	        if (date == null) date = new Date();
	        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
	        
	        int khungGio = cboTimeFilter.getSelectedIndex() + 1;
	        List<BanAn> dsBan = dao.getDanhSachBanTheoNgayGio(localDate, khungGio);
			for (BanAn ban : dsBan) {
	            tableModel.addRow(new Object[] {
	                ban.getMaBan(),
	                ban.getTenBan(),
	                ban.getSoLuongCho(),
	                ban.getLoaiBan().getTenLoaiBan(),
	                ban.getTrangThai(),
	                ban.getKhuVuc() != null ? ban.getKhuVuc().getTenKhuVuc() : "",
	                ban.getGhiChu()
	            });
	        }
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
		}
    	
    	capNhatThongTin();       
    }
    
    
    
    
//    private void taoHoaDon() {
//        int selectedRow = tableBanAn.getSelectedRow();
//        
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this,
//                "Vui lòng chọn một bàn để tạo hóa đơn!",
//                "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        
//        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
//        BanAn banAn = dao.getBanTheoMa(maBan);
//        
//        if (banAn == null) {
//            JOptionPane.showMessageDialog(this,
//                "Không tìm thấy thông tin bàn!",
//                "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        // Mở dialog tạo hóa đơn
//        new DialogTaoHoaDon(
//            (Frame) SwingUtilities.getWindowAncestor(this),
//            banAn,
//            () -> loadDanhSachBanAn() // Callback để refresh lại bảng
//        ).setVisible(true);
//    }
    
    
   
    private void timKiemBanAn() {
        try {

            String maBan = txtMaBan.getText().trim();
            String tenBan = txtTenBan.getText().trim();
            String khuVuc = cboKhuVuc.getSelectedItem().toString();
            String trangThai = cboTrangThai.getSelectedItem().toString();
            String loaiBan = cboLoaiBan.getSelectedItem().toString();
            String soLuongCho = txtSoLuongCho.getText().trim();
            
            // 🔥 SỬA: Lấy danh sách bàn theo ngày và khung giờ đã chọn
            Date date = dateFilter.getDate();
            if (date == null) date = new Date();
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
            int khungGio = cboTimeFilter.getSelectedIndex() + 1;
            
            List<BanAn> dsBan = dao.getDanhSachBanTheoNgayGio(localDate, khungGio);
            if (dsBan == null) {
                dsBan = new ArrayList<>();
            }

            List<BanAn> ketQua = dsBan.stream().filter(ban -> {
                boolean match = true;

                if (!maBan.isEmpty()) {
                    match = match && ban.getMaBan().toLowerCase().contains(maBan.toLowerCase());
                }

                if (!tenBan.isEmpty()) {
                    match = match && ban.getTenBan().toLowerCase().contains(tenBan.toLowerCase());
                }

                if (!khuVuc.equals("-- Tất cả --")) {
                    match = match && (ban.getKhuVuc() != null && ban.getKhuVuc().getTenKhuVuc().equals(khuVuc));
                }

                if (!trangThai.equals("-- Tất cả --")) {
                    match = match && ban.getTrangThai().equals(trangThai);
                }

                if (!loaiBan.equals("-- Tất cả --")) {
                    match = match && ban.getLoaiBan().getTenLoaiBan().equals(loaiBan);
                }

                // Lọc theo số lượng chỗ
                if (!soLuongCho.isEmpty()) {
                    try {
                        int soCho = Integer.parseInt(soLuongCho);
                        match = match && ban.getSoLuongCho() == soCho;
                    } catch (NumberFormatException ex) {
                        // Nếu số lượng chỗ không phải số, bỏ qua tiêu chí này
                        match = false;
                    }
                }

                return match;
            }).collect(Collectors.toList());

            // Cập nhật bảng
            tableModel.setRowCount(0);
            for (BanAn ban : ketQua) {
                tableModel.addRow(new Object[] {
                    ban.getMaBan(),
                    ban.getTenBan(),
                    ban.getSoLuongCho(),
                    ban.getLoaiBan().getTenLoaiBan(),
                    ban.getTrangThai(),
                    ban.getKhuVuc() != null ? ban.getKhuVuc().getTenKhuVuc() : "",
                    ban.getGhiChu()
                });
            }

            // Cập nhật thông tin thống kê
            capNhatThongTin();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
     
    /**
     * Làm mới form tìm kiếm
     */
    private void lamMoiForm() {
        txtMaBan.setText("");
        txtTenBan.setText("");
        txtSoLuongCho.setText("");
        cboKhuVuc.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        cboLoaiBan.setSelectedIndex(0);
//        dateChooserNgay.setDate(new Date());
//        cboKhungGio.setSelectedIndex(0);
        loadDanhSachBanAn();
    }
    
    /**
     * Xem chi tiết bàn ăn được chọn
     */
    private void xemChiTietBanAn() {
        int selectedRow = tableBanAn.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một bàn để xem chi tiết!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
        String trangThaiHienThi = tableModel.getValueAt(selectedRow, 4).toString();
        
        // Lấy thông tin chi tiết từ database
        BanAn banAn = dao.getBanTheoMa(maBan);
        
        if (banAn == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy thông tin bàn!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ✅ GHI ĐÈ TRẠNG THÁI
        banAn.setTrangThai(trangThaiHienThi);
        
        try {
            Date date = dateFilter.getDate();
            if (date == null) date = new Date();
            LocalDate ngayXem = new java.sql.Date(date.getTime()).toLocalDate();
            int khungGio = cboTimeFilter.getSelectedIndex() + 1;
            
            // ✅ TẠO DIALOG VỚI CALLBACK REFRESH
            DialogChiTietBanAn dialog = new DialogChiTietBanAn(
                (Frame) SwingUtilities.getWindowAncestor(this), 
                banAn,
                ngayXem,
                khungGio,
                () -> {
                    // Callback: Refresh sau khi đặt bàn/thay đổi
                    loadDanhSachBanAn();
                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                }
            );
            
            dialog.setVisible(true);
            
            // Refresh sau khi đóng dialog
            loadDanhSachBanAn();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi mở chi tiết bàn: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private void capNhatThongTin() {     
    	int tongSoBan = tableModel.getRowCount();
      
        int banTrong = 0;
        int banDangSuDung = 0;
        int banDaDat = 0;
        int banDangDon = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String trangThai = tableModel.getValueAt(i, 4).toString();
            
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
                case "Bảo trì":
                    banDangDon++;
                    break;
            }
        }
        
       
        lblThongTin.setText(String.format(
                "Tổng số bàn: %d  |  Trống: %d  |  Đang dùng: %d  |  Đã đặt: %d  |  Bảo trì: %d",
                tongSoBan, banTrong, banDangSuDung, banDaDat, banDangDon
        ));
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o= e.getSource();
		if(o.equals(btnXemChiTiet)) {
			xemChiTietBanAn();
		}
//		else if(o== btnTaoHoaDon) {
//			taoHoaDon();
//		}
		
	}

	@Override
	// Double click vào bảng để xem chi tiết
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		// Kiểm tra nếu số lần click là 2 (double-click)
	    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
	        xemChiTietBanAn();
	    }
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
    
    
	
	// Helper: Map giờ hiện tại sang index của ComboBox
	private int xacDinhKhungGioIndex(int gio) {
	    if (gio >= 6 && gio < 11) return 0; // Sáng
	    if (gio >= 11 && gio < 14) return 1; // Trưa
	    if (gio >= 14 && gio < 17) return 2; // Chiều
	    return 3; // Tối
	}

	
    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Test - Tra cứu bàn ăn");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1200, 700);
//            frame.setLocationRelativeTo(null);
//            frame.add(new TraCuuBanAn());
//            frame.setVisible(true);
//        });
//    }
}