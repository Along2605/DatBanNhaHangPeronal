package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.List;

import dao.BanAnDAO;
import dao.HoaDonDAO;
import entity.BanAn;
import entity.HoaDon;

public class TraCuuHoaDon extends JPanel implements MouseListener{
    
   
    // Panel tìm kiếm
    private JTextField txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JButton btnTimKiem, btnLamMoi, btnXemChiTiet;
    
    // Bảng hiển thị
    private JTable tableHoaDon;
    private DefaultTableModel modelTable;
    
    // DAO
    private HoaDonDAO hoaDonDAO;
    
    // Format ngày giờ
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    // ========== CONSTRUCTOR ==========
    public TraCuuHoaDon() {
        hoaDonDAO = new HoaDonDAO();
        initComponents();
        loadDanhSachHoaDon();
    }
    
    // ========== KHỞI TẠO GIAO DIỆN ==========
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);
        
        // Panel tiêu đề
        add(taoPanelTieuDe(), BorderLayout.NORTH);
        
        // Panel tìm kiếm
        add(taoPanelTimKiem(), BorderLayout.CENTER);
        
        // Panel nút chức năng
        add(taoPanelNutChucNang(), BorderLayout.SOUTH);
    }
    
    // ========== TẠO PANEL TIÊU ĐỀ ==========
    private JPanel taoPanelTieuDe() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel lblTieuDe = new JLabel("TRA CỨU HÓA ĐƠN");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setForeground(Color.WHITE);
        panel.add(lblTieuDe);
        
        return panel;
    }
    
    // ========== TẠO PANEL TÌM KIẾM ==========
    private JPanel taoPanelTimKiem() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel điều khiển tìm kiếm (trên)
        JPanel panelDieuKhien = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelDieuKhien.setBackground(Color.WHITE);
        panelDieuKhien.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Tìm kiếm",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(41, 128, 185)
        ));
        
        // Tìm kiếm theo mã hóa đơn
        JLabel lblTimKiem = new JLabel("Mã hóa đơn:");
        lblTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        panelDieuKhien.add(lblTimKiem);
        
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        panelDieuKhien.add(txtTimKiem);
        
        // Lọc theo trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(new Font("Arial", Font.PLAIN, 14));
        panelDieuKhien.add(lblTrangThai);
        
        cboTrangThai = new JComboBox<>(new String[]{
            "Tất cả",
            "Chưa thanh toán",
            "Đã thanh toán"
        });
        cboTrangThai.setFont(new Font("Arial", Font.PLAIN, 14));
        cboTrangThai.setPreferredSize(new Dimension(150, 30));
        panelDieuKhien.add(cboTrangThai);
        
        // Nút tìm kiếm
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Arial", Font.BOLD, 14));
        btnTimKiem.setBackground(new Color(41, 128, 185));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setPreferredSize(new Dimension(120, 35));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelDieuKhien.add(btnTimKiem);
        
        // Nút làm mới
        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Arial", Font.BOLD, 14));
        btnLamMoi.setBackground(new Color(52, 152, 219));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setPreferredSize(new Dimension(120, 35));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelDieuKhien.add(btnLamMoi);
        
        mainPanel.add(panelDieuKhien, BorderLayout.NORTH);
        
        // Panel bảng dữ liệu (giữa)
        mainPanel.add(taoTableHoaDon(), BorderLayout.CENTER);
        
        // Xử lý sự kiện
        xuLySuKien();
        
        return mainPanel;
    }
    
    // ========== TẠO BẢNG HÓA ĐƠN ==========
    private JScrollPane taoTableHoaDon() {
        // Tạo model cho bảng
        String[] columnNames = {
            "Mã HĐ", 
            "Mã bàn", 
            "Khách hàng", 
            "Nhân viên", 
            "Ngày lập", 
            "Tổng tiền", 
            "Trạng thái"
        };
        
        modelTable = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa trực tiếp
            }
        };
        
        tableHoaDon = new JTable(modelTable);
        tableHoaDon.setFont(new Font("Arial", Font.PLAIN, 13));
        tableHoaDon.setRowHeight(30);
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableHoaDon.getTableHeader().setBackground(new Color(41, 128, 185));
        tableHoaDon.getTableHeader().setForeground(Color.WHITE);
        tableHoaDon.setSelectionBackground(new Color(184, 207, 229));
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableHoaDon.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã HĐ
        tableHoaDon.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Mã bàn
        tableHoaDon.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Trạng thái
        
        // Căn phải cột tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableHoaDon.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        // Thiết lập độ rộng cột
        tableHoaDon.getColumnModel().getColumn(0).setPreferredWidth(100);  // Mã HĐ
        tableHoaDon.getColumnModel().getColumn(1).setPreferredWidth(80);   // Mã bàn
        tableHoaDon.getColumnModel().getColumn(2).setPreferredWidth(150);  // Khách hàng
        tableHoaDon.getColumnModel().getColumn(3).setPreferredWidth(150);  // Nhân viên
        tableHoaDon.getColumnModel().getColumn(4).setPreferredWidth(150);  // Ngày lập
        tableHoaDon.getColumnModel().getColumn(5).setPreferredWidth(120);  // Tổng tiền
        tableHoaDon.getColumnModel().getColumn(6).setPreferredWidth(120);  // Trạng thái
        
        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
        tableHoaDon.addMouseListener(this);
        return scrollPane;
    }
    
    // ========== TẠO PANEL NÚT CHỨC NĂNG ==========
    private JPanel taoPanelNutChucNang() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);
        
        btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setFont(new Font("Arial", Font.BOLD, 14));
        btnXemChiTiet.setBackground(new Color(46, 204, 113));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setPreferredSize(new Dimension(150, 40));
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(btnXemChiTiet);
        
        return panel;
    }
    
    // ========== XỬ LÝ SỰ KIỆN ==========
    private void xuLySuKien() {
        // Nút tìm kiếm
        btnTimKiem.addActionListener(e -> timKiemHoaDon());
        
        // Enter trong ô tìm kiếm
        txtTimKiem.addActionListener(e -> timKiemHoaDon());
        
        // Thay đổi trạng thái
        cboTrangThai.addActionListener(e -> timKiemHoaDon());
        
        // Nút làm mới
        btnLamMoi.addActionListener(e -> lamMoi());
    }
    
   
    private void loadDanhSachHoaDon() {
        try {
            // Xóa dữ liệu cũ
            modelTable.setRowCount(0);
            
            // Lấy danh sách từ database
            List<HoaDon> dsHoaDon = hoaDonDAO.findAll();
            
            // Thêm vào bảng
            for (HoaDon hd : dsHoaDon) {
                Object[] row = {
                    hd.getMaHoaDon(),
                    hd.getBanAn() != null ? hd.getBanAn().getMaBan() : "N/A",
                    hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "Khách lẻ",
                    hd.getNhanVien() != null ? hd.getNhanVien().getHoTen() : "N/A",
                    hd.getNgayLapHoaDon() != null ? hd.getNgayLapHoaDon().format(dateFormatter) : "",
                    String.format("%,.0f đ", hd.getTongTien()),
                    hd.getTrangThai()
                };
                modelTable.addRow(row);
            }
            
            // Hiển thị tổng số hóa đơn
            JOptionPane.showMessageDialog(this, 
                "Đã tải " + dsHoaDon.size() + " hóa đơn",
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách hóa đơn: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
   
    private void timKiemHoaDon() {
        try {
            // Xóa dữ liệu cũ
            modelTable.setRowCount(0);
            
            String maHD = txtTimKiem.getText().trim();
            String trangThai = cboTrangThai.getSelectedItem().toString();
            
            List<HoaDon> dsHoaDon;
            
            // Tìm kiếm theo điều kiện
            if (!maHD.isEmpty()) {
                // Tìm theo mã hóa đơn
                HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHD);
                dsHoaDon = hd != null ? List.of(hd) : List.of();
            } else if (!trangThai.equals("Tất cả")) {
                // Lọc theo trạng thái
                dsHoaDon = hoaDonDAO.timHoaDonTheoTrangThai(trangThai);
            } else {
                // Lấy tất cả
                dsHoaDon = hoaDonDAO.findAll();
            }
            
            // Hiển thị kết quả
            for (HoaDon hd : dsHoaDon) {
                Object[] row = {
                    hd.getMaHoaDon(),
                    hd.getBanAn() != null ? hd.getBanAn().getMaBan() : "N/A",
                    hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "Khách lẻ",
                    hd.getNhanVien() != null ? hd.getNhanVien().getHoTen() : "N/A",
                    hd.getNgayLapHoaDon() != null ? hd.getNgayLapHoaDon().format(dateFormatter) : "",
                    String.format("%,.0f đ", hd.getTongTien()),
                    hd.getTrangThai()
                };
                modelTable.addRow(row);
            }
            
            // Thông báo kết quả
            if (dsHoaDon.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy hóa đơn!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void lamMoi() {
        txtTimKiem.setText("");
        cboTrangThai.setSelectedIndex(0);
        loadDanhSachHoaDon();
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row = tableHoaDon.getSelectedRow();
		if (row != -1) {
			txtTimKiem.setText(modelTable.getValueAt(row, 0).toString());
			
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
//            JFrame frame = new JFrame("Tra cứu hóa đơn");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1000, 600);
//            frame.setLocationRelativeTo(null);
//            
//            TraCuuHoaDon gui = new TraCuuHoaDon();
//            frame.add(gui);
//            
//            frame.setVisible(true);
//        });
//    }
}