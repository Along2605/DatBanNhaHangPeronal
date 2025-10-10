package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class DatBan extends JPanel {

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
    
    private JTable tableBanTrong;
    private DefaultTableModel tableModel;
    
    private JButton btnTimKhachHang;
    private JButton btnTimBan;
    private JButton btnDatBan;
    private JButton btnLamMoi;
    
    // Colors
    private final Color MAU_CAM = new Color(214, 116, 76); // MAIN_COLOR
    private final Color MAU_CAM_SANG = new Color(234, 136, 96); // HOVER_COLOR
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    
    public DatBan() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
                
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);     
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
        cboKhuVuc = createComboBox(new String[]{"-- Tất cả --", "Tầng 1", "Tầng 2", "Sân thượng"});
        addFormField(formPanel, gbc, row++, "Khu vực:", cboKhuVuc);
        
        // Loại bàn
        cboLoaiBan = createComboBox(new String[]{"-- Tất cả --", "Bàn vuông", "Bàn tròn", "Bàn đôi"});
        addFormField(formPanel, gbc, row++, "Loại bàn:", cboLoaiBan);
        
        // Nút tìm bàn
        btnTimBan = createButton("Tìm bàn trống", MAU_CAM);
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
        return mainPanel;
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
            "Mã bàn", "Tên bàn", "Khu vực", 
            "Số chỗ", "Loại bàn", "Ghi chú"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
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
        
        tableBanTrong.getColumnModel().getColumn(0).setPreferredWidth(80);   // Mã bàn
        tableBanTrong.getColumnModel().getColumn(1).setPreferredWidth(120);  // Tên bàn
        tableBanTrong.getColumnModel().getColumn(2).setPreferredWidth(120);  // Khu vực
        tableBanTrong.getColumnModel().getColumn(3).setPreferredWidth(80);   // Số chỗ
        tableBanTrong.getColumnModel().getColumn(4).setPreferredWidth(100);  // Loại bàn
        tableBanTrong.getColumnModel().getColumn(5).setPreferredWidth(150);  // Ghi chú
        
        JScrollPane scrollPane = new JScrollPane(tableBanTrong);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(232, 245, 233));
        infoPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel lblTableInfo = new JLabel("✓ Chọn một bàn trong danh sách để đặt");
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
        
        panel.add(btnDatBan);
        panel.add(btnLamMoi);
        
        return panel;
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
    
        
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test - Đặt bàn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.add(new DatBan());
            frame.setVisible(true);
        });
    }
}