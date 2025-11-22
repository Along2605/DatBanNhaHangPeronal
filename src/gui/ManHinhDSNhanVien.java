package gui;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.NhanVienDAO;
import entity.NhanVien;

public class ManHinhDSNhanVien extends JPanel {
    private JTextField txtTimKiem;
    private JComboBox<String> cbLocChucVu;
    private JComboBox<String> cbLocTrangThai;
    private JButton btnTimKiem;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JScrollPane scroll;
    private NhanVienDAO nhanVienDAO;
    private DefaultTableModel model;
    private JTable table;

    public ManHinhDSNhanVien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.decode("#EAF1F9"));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel pnTitle = new JPanel(new BorderLayout());
        pnTitle.setBackground(Color.decode("#EAF1F9"));
        JLabel lblTitle = new JLabel("DANH SÁCH NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.decode("#333333"));
        lblTitle.setBorder(new EmptyBorder(5, 0, 10, 0));
        pnTitle.add(lblTitle, BorderLayout.CENTER);
        add(pnTitle, BorderLayout.NORTH);

        // Center Panel (Filter + Table)
        JPanel pnCenter = new JPanel(new BorderLayout(0, 10));
        pnCenter.setBackground(Color.decode("#EAF1F9"));

        // Filter Panel - compact horizontal layout
        JPanel pnFilter = createFilterPanel();
        pnCenter.add(pnFilter, BorderLayout.NORTH);

        // Table Panel
        JPanel pnTable = createTablePanel();
        pnCenter.add(pnTable, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.CENTER);

        // Button Panel at bottom
        JPanel pnButtons = createButtonPanel();
        add(pnButtons, BorderLayout.SOUTH);

        // Load data
        loadDSNhanVien();
        
        addEventListeners();
    }

    private JPanel createFilterPanel() {
        JPanel pnFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnFilter.setBackground(Color.WHITE);
        pnFilter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
                "Tìm kiếm & Lọc",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.PLAIN, 14)
            ),
            new EmptyBorder(5, 5, 5, 5)
        ));

        // Search field
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem = createTextField(true);
        txtTimKiem.setPreferredSize(new Dimension(200, 30));

        // Position filter
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setFont(new Font("Arial", Font.PLAIN, 14));
        cbLocChucVu = new JComboBox<>(new String[]{"Tất cả", "Quản lý", "Nhân viên phục vụ", "Nhân viên bếp", "Thu ngân"});
        cbLocChucVu.setPreferredSize(new Dimension(150, 30));
        cbLocChucVu.setFont(new Font("Arial", Font.PLAIN, 14));

        // Status filter
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(new Font("Arial", Font.PLAIN, 14));
        cbLocTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang làm việc", "Nghỉ việc"});
        cbLocTrangThai.setPreferredSize(new Dimension(150, 30));
        cbLocTrangThai.setFont(new Font("Arial", Font.PLAIN, 14));

        // Search button
        btnTimKiem = createButton("Tìm kiếm", "img/search.png");

        // Add components
        pnFilter.add(lblTimKiem);
        pnFilter.add(txtTimKiem);
        pnFilter.add(Box.createHorizontalStrut(10));
        pnFilter.add(lblChucVu);
        pnFilter.add(cbLocChucVu);
        pnFilter.add(Box.createHorizontalStrut(10));
        pnFilter.add(lblTrangThai);
        pnFilter.add(cbLocTrangThai);
        pnFilter.add(Box.createHorizontalStrut(10));
        pnFilter.add(btnTimKiem);

        return pnFilter;
    }

    private JPanel createTablePanel() {
        JPanel pnTable = new JPanel(new BorderLayout());
        pnTable.setBackground(Color.WHITE);

        // Define column headers
        String[] headers = {"Mã NV", "Họ tên", "Ngày sinh", "Email", "SĐT", "Giới tính", "Chức vụ", "Ngày vào làm", "Trạng thái"};

        // Create table model (non-editable)
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.decode("#B3D9FF"));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(Color.decode("#DDDDDD"));
        table.setShowGrid(true);
        table.setAutoCreateRowSorter(true); // Enable sorting

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(Color.decode("#4CAF50"));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Set column widths
        int[] columnWidths = {80, 150, 100, 180, 100, 80, 140, 110, 120};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Center align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã NV
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Ngày sinh
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // SĐT
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Giới tính
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Ngày vào làm
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer); // Trạng thái

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(Color.decode("#F5F5F5"));
                    }
                }
                
                // Apply center alignment for specific columns
                if (column == 0 || column == 2 || column == 4 || column == 5 || column == 7 || column == 8) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });

        // Create scroll pane
        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
            "Danh sách nhân viên",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.PLAIN, 14)
        ));

        pnTable.add(scroll, BorderLayout.CENTER);

        return pnTable;
    }

    private JPanel createButtonPanel() {
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnButtons.setBackground(Color.decode("#EAF1F9"));

        btnThem = createButton("Thêm", "img/add.png");
        btnSua = createButton("Sửa", "img/edit.png");
        btnXoa = createButton("Xóa", "img/delete.png");
        btnLamMoi = createButton("Làm mới", "img/refresh.png");

        pnButtons.add(btnThem);
        pnButtons.add(btnSua);
        pnButtons.add(btnXoa);
        pnButtons.add(btnLamMoi);

        return pnButtons;
    }

    private void loadDSNhanVien() {
        model.setRowCount(0);
        try {
            nhanVienDAO = new NhanVienDAO();
            List<NhanVien> dsNhanVien = nhanVienDAO.layDanhSachNhanVien();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (NhanVien nv : dsNhanVien) {
                String gioiTinhStr = nv.isGioiTinh() ? "Nam" : "Nữ";
                String trangThaiStr = nv.isTrangThai() ? "Đang làm" : "Nghỉ việc";
                
                String ngaySinhStr = nv.getNgaySinh().format(formatter);
                String ngayVaoLamStr = nv.getNgayVaoLam().format(formatter);
                model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHoTen(),
                    ngaySinhStr,
                    nv.getEmail(),
                    nv.getSoDienThoai(),
                    gioiTinhStr,
                    nv.getChucVu(),
                    ngayVaoLamStr,
                    trangThaiStr
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load danh sách nhân viên: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setEditable(editable);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(130, 38));
        button.setBackground(Color.decode("#4CAF50"));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (iconPath != null) {
            try {
                ImageIcon icon = new ImageIcon(iconPath);
                Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                // Icon not found, continue without icon
            }
        }
        
        return button;
    }
    
    private void addEventListeners() {
        // Tìm kiếm khi nhấn nút
        btnTimKiem.addActionListener(e -> timKiemVaLoc());
        
        // Tìm kiếm khi nhấn Enter trong ô tìm kiếm
        txtTimKiem.addActionListener(e -> timKiemVaLoc());
        
        // Lọc khi thay đổi combo box
        cbLocChucVu.addActionListener(e -> timKiemVaLoc());
        cbLocTrangThai.addActionListener(e -> timKiemVaLoc());
        
        // Làm mới
        btnLamMoi.addActionListener(e -> lamMoi());
        
        btnSua.addActionListener(e-> suaThongTinNhanVien());
        btnThem.addActionListener(e-> themNhanVienMoi());
        btnXoa.addActionListener(e-> xoaNhanVien());
    }
    
    private void xoaNhanVien() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn nhân viên cần cập nhật trạng thái!", 
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin từ hàng đã chọn
        String maNV = table.getValueAt(selectedRow, 0).toString();
        String hoTen = table.getValueAt(selectedRow, 1).toString();
        String trangThaiHienTai = table.getValueAt(selectedRow, 8).toString();

        // Kiểm tra xem nhân viên đã nghỉ việc chưa
        if (trangThaiHienTai.equals("Nghỉ việc")) {
            JOptionPane.showMessageDialog(this, 
                "Nhân viên '" + hoTen + "' đã ở trạng thái 'Nghỉ việc'.", 
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Xác nhận trước khi "xóa"
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn chuyển trạng thái của nhân viên '" + hoTen + "' sang 'Nghỉ việc'?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {              
                nhanVienDAO = new NhanVienDAO();     
       
                boolean success = nhanVienDAO.capNhatTrangThaiNhanVien(maNV, false); // false = Nghỉ việc

                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Cập nhật trạng thái nhân viên thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Tải lại danh sách để cập nhật trạng thái trên bảng
                    loadDSNhanVien(); 
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Cập nhật trạng thái thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật trạng thái: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

	private void themNhanVienMoi() {      
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm nhân viên mới", true);       
        ManHinhThemNhanVien pnThemNhanVien = new ManHinhThemNhanVien();

        pnThemNhanVien.setOnSaveListener(() -> {
            loadDSNhanVien(); // Tải lại danh sách nhân viên trên màn hình này
            dialog.dispose(); // Đóng cửa sổ (dialog) thêm
        });

        
        dialog.setContentPane(pnThemNhanVien);

        
        dialog.setSize(1150, 600);
        dialog.setLocationRelativeTo(this);     
        dialog.setVisible(true);
    }

	private void suaThongTinNhanVien() {
		int selectedRow = table.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Lấy mã nhân viên từ dòng được chọn
		String maNV = table.getValueAt(selectedRow, 0).toString();

		try {
			nhanVienDAO = new NhanVienDAO();
			NhanVien nv = nhanVienDAO.getNhanVienTheoMa(maNV);

			if (nv == null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin nhân viên!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Tạo dialog chứa màn hình cập nhật
			JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật nhân viên", true);
			ManHinhCapNhatNhanVien pnCapNhat = new ManHinhCapNhatNhanVien();

			// Truyền dữ liệu vào màn hình cập nhật
			pnCapNhat.setNhanVien(nv);

			// Xử lý sự kiện khi lưu thành công
			pnCapNhat.setOnSaveListener(() -> {
				loadDSNhanVien(); // Reload lại danh sách
				dialog.dispose(); // Đóng dialog
			});

			dialog.setContentPane(pnCapNhat);
			dialog.setSize(1150, 600);
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin nhân viên: " + e.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void lamMoi() {       
        txtTimKiem.setText("");
        cbLocChucVu.setSelectedIndex(0);
        cbLocTrangThai.setSelectedIndex(0);
        
        
        loadDSNhanVien();
    }

	private void timKiemVaLoc() {
		// TODO Auto-generated method stub
		model.setRowCount(0);
		try {
			nhanVienDAO= new NhanVienDAO();
			List<NhanVien> dsNhanVien= nhanVienDAO.layDanhSachNhanVien();
			String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
            String chucVuLoc = cbLocChucVu.getSelectedItem().toString();
            String trangThaiLoc = cbLocTrangThai.getSelectedItem().toString();
            
            for(NhanVien nv: dsNhanVien) {
            	if (!chucVuLoc.equals("Tất cả") && !nv.getChucVu().equals(chucVuLoc)) {
                    continue;
                }
                
                // Kiểm tra trạng thái
                boolean trangThaiNV = nv.isTrangThai();
                if (trangThaiLoc.equals("Đang làm việc") && !trangThaiNV) {
                    continue;
                }
                if (trangThaiLoc.equals("Nghỉ việc") && trangThaiNV) {
                    continue;
                }
                
             // Tìm kiếm theo từ khóa (mã NV, họ tên, email, SĐT)
                if (!tuKhoa.isEmpty()) {
                    String maNV = nv.getMaNV().toLowerCase();
                    String hoTen = nv.getHoTen().toLowerCase();
                    String email = nv.getEmail().toLowerCase();
                    String sdt = nv.getSoDienThoai();
                    
                    if (!maNV.contains(tuKhoa) && 
                        !hoTen.contains(tuKhoa) && 
                        !email.contains(tuKhoa) && 
                        !sdt.contains(tuKhoa)) {
                        continue;
                    }
                }
             // Thêm vào bảng nếu đạt tất cả điều kiện
                String gioiTinhStr = nv.isGioiTinh() ? "Nam" : "Nữ";
                String trangThaiStr = nv.isTrangThai() ? "Đang làm" : "Nghỉ việc";
                
                model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHoTen(),
                    nv.getNgaySinh(),
                    nv.getEmail(),
                    nv.getSoDienThoai(),
                    gioiTinhStr,
                    nv.getChucVu(),
                    nv.getNgayVaoLam(),
                    trangThaiStr
                });
                
                int soKetQua= model.getRowCount();
                if(soKetQua==0) {
                	JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào phù hợp", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm"+ e.getMessage(),
					"lỗi", JOptionPane.ERROR_MESSAGE);
			
		}
	}

	public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
			// Tạo JFrame
			JFrame frame = new JFrame("Quản lý Bàn Ăn");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 600); 
			frame.setLocationRelativeTo(null); 

			// Tạo JPanel ThemBanAn
			ManHinhDSNhanVien panel = new ManHinhDSNhanVien();
			frame.setContentPane(panel); 
			frame.setVisible(true); 
		});
	}
}