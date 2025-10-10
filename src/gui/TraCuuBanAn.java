package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import dao.BanAnDAO;
import entity.BanAn;

public class TraCuuBanAn extends JPanel implements ActionListener, MouseListener{

    private static final long serialVersionUID = 1L;
    
    // Components
    private JTextField txtMaBan;
    private JTextField txtTenBan;
    private JComboBox<String> cboKhuVuc;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboLoaiBan;
    private JTextField txtSoLuongCho;
    
    private JTable tableBanAn;
    private DefaultTableModel tableModel;
    
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXemChiTiet;
    
    BanAnDAO dao = new BanAnDAO();
    
    // Colors
    private final Color MAIN_COLOR = new Color(214, 116, 76);
    private final Color HOVER_COLOR = new Color(234, 136, 96);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);

	private JLabel lblThongTin;

	private JButton btnTaoHoaDon;
    
    public TraCuuBanAn() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSearchPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
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
        cboKhuVuc = createComboBox(new String[]{"-- Tất cả --", "Tầng 1", "Tầng 2", "Sân thượng"});
        addFormField(formPanel, gbc, 2, "Khu vực:", cboKhuVuc);
        
        // Trạng thái
        cboTrangThai = createComboBox(new String[]{"-- Tất cả --", "Trống", "Đang sử dụng", "Đã đặt", "Đang dọn"});
        addFormField(formPanel, gbc, 3, "Trạng thái:", cboTrangThai);
        
        // Loại bàn
        cboLoaiBan = createComboBox(new String[]{"-- Tất cả --", "Bàn vuông", "Bàn tròn", "Bàn đôi"});
        addFormField(formPanel, gbc, 4, "Loại bàn:", cboLoaiBan);
        
        // Số lượng chỗ
        addFormField(formPanel, gbc, 5, "Số lượng chỗ:", txtSoLuongCho = createTextField());
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        btnTimKiem = createButton("Tìm kiếm", MAIN_COLOR);
        btnLamMoi = createButton("Làm mới", new Color(100, 100, 100));
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
//        // Add action listeners
//        addEventListeners();
        
        return mainPanel;
    }
    
    /**
     * Tạo panel bảng dữ liệu
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        lblThongTin= new JLabel("Tổng số bàn: 0");
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(lblThongTin);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
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
        
        btnTaoHoaDon = createButton("Tạo hóa đơn", new Color(76, 175, 80));
        btnTaoHoaDon.setPreferredSize(new Dimension(150, 40));
        
        panel.add(btnXemChiTiet);
        panel.add(btnTaoHoaDon);
        
        btnXemChiTiet.addActionListener(this);
        btnTaoHoaDon.addActionListener(this);
        
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
			
			List<BanAn> dsBan= dao.getAllBanAn();
			for (BanAn ban : dsBan) {
	            tableModel.addRow(new Object[] {
	                ban.getMaBan(),
	                ban.getTenBan(),
	                ban.getSoLuongCho(),
	                ban.getLoaiBan(),
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
    
    
    private void taoHoaDon() {
        int selectedRow = tableBanAn.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một bàn để tạo hóa đơn!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
        BanAn banAn = dao.getBanTheoMa(maBan);
        
        if (banAn == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy thông tin bàn!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mở dialog tạo hóa đơn
        new DialogTaoHoaDon(
            (Frame) SwingUtilities.getWindowAncestor(this),
            banAn,
            () -> loadDanhSachBanAn() // Callback để refresh lại bảng
        ).setVisible(true);
    }
    
    
   
    private void timKiemBanAn() {
        // Lấy điều kiện tìm kiếm
        String maBan = txtMaBan.getText().trim();
        String tenBan = txtTenBan.getText().trim();
        String khuVuc = (String) cboKhuVuc.getSelectedItem();
        String trangThai = (String) cboTrangThai.getSelectedItem();
        String loaiBan = (String) cboLoaiBan.getSelectedItem();
        String soLuongCho = txtSoLuongCho.getText().trim();
        
        
        JOptionPane.showMessageDialog(this, 
            "Chức năng tìm kiếm sẽ được cài đặt sau!\n\n" +
            "Điều kiện tìm kiếm:\n" +
            "- Mã bàn: " + (maBan.isEmpty() ? "Tất cả" : maBan) + "\n" +
            "- Tên bàn: " + (tenBan.isEmpty() ? "Tất cả" : tenBan) + "\n" +
            "- Khu vực: " + khuVuc + "\n" +
            "- Trạng thái: " + trangThai + "\n" +
            "- Loại bàn: " + loaiBan + "\n" +
            "- Số chỗ: " + (soLuongCho.isEmpty() ? "Tất cả" : soLuongCho),
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
        
       
        loadDanhSachBanAn();
    }
    
    /**
     * Xem chi tiết bàn ăn được chọn
     * TODO: Hiển thị dialog chi tiết hoặc chuyển sang panel chi tiết
     */
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
        
        // Lấy mã bàn từ dòng được chọn
        String maBan = tableModel.getValueAt(selectedRow, 0).toString();
        
        // Lấy thông tin chi tiết từ database
        
        BanAn banAn= dao.getBanTheoMa(maBan);
        if (banAn == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy thông tin bàn!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new DialogChiTietBanAn((Frame)SwingUtilities.getWindowAncestor(this), banAn).setVisible(true);
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
                case "Còn trống":
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
                "Tổng số bàn: %d  |  Trống: %d  |  Đang dùng: %d  |  Đã đặt: %d  |  Đang dọn: %d",
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
		else if(o== btnTaoHoaDon) {
			taoHoaDon();
		}
		
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