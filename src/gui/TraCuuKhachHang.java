package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.KhachHangDAO;
import entity.KhachHang;

public class TraCuuKhachHang extends JPanel implements ActionListener{
	private JScrollPane scroll;
	private DefaultTableModel dataKH;
	private JTable tableKhachHang;
	private JTextField txtMaKH, txtHoTenKH, txtSDT, txtEmail;
	private JComboBox<String> cbGioiTinh;
	private JButton btnThem, btnXoa, btnSua, btnLamMoi;

	private KhachHangDAO khDAO;
	private JButton btnTim;

	public TraCuuKhachHang() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#EAF1F9"));

		Box boxTop = Box.createVerticalBox();

		// Tiêu đề
		JLabel lblTitle = new JLabel("DANH SÁCH KHÁCH HÀNG");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		boxTop.add(Box.createVerticalStrut(10));
		boxTop.add(lblTitle);
		boxTop.add(Box.createVerticalStrut(10));
		add(boxTop, BorderLayout.NORTH);

		JPanel pnForm = new JPanel();
		pnForm.setLayout(new GridBagLayout());
		boxTop.add(pnForm);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 15, 10, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Hàng 1
		gbc.gridx = 0;
		gbc.gridy = 0;
		pnForm.add(new JLabel("Mã khách hàng "), gbc);
		gbc.gridx = 1;
		txtMaKH = new JTextField(15);
		txtMaKH.setEditable(false);
		pnForm.add(txtMaKH, gbc);

		gbc.gridx = 2;
		pnForm.add(new JLabel("Họ tên "), gbc);
		gbc.gridx = 3;
		txtHoTenKH = new JTextField(15);
		pnForm.add(txtHoTenKH, gbc);

		// Hàng 2
		gbc.gridx = 0;
		gbc.gridy = 1;
		pnForm.add(new JLabel("Giới tính"), gbc);
		gbc.gridx = 1;
		cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
		pnForm.add(cbGioiTinh, gbc);

		gbc.gridx = 2;
		pnForm.add(new JLabel("Số điện thoại"), gbc);
		gbc.gridx = 3;
		txtSDT = new JTextField(15);
		pnForm.add(txtSDT, gbc);

		
		

		// Hàng 4: các nút
		gbc.gridy = 3;
		gbc.gridx = 0;
		btnSua = new JButton("Sửa", resizeIcon("img/edit.png"));
		btnSua.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnSua, gbc);

		gbc.gridx = 1;
		btnTim = new JButton("Tìm", resizeIcon("img/add.png"));
		btnTim.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnTim, gbc);

		gbc.gridx = 2;
		btnXoa = new JButton("Xóa", resizeIcon("img/delete.png"));
		btnXoa.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnXoa, gbc);

		gbc.gridx = 3;
		btnLamMoi = new JButton("Làm mới", resizeIcon("img/refresh.png"));
		btnLamMoi.setPreferredSize(new Dimension(120, 30));
		pnForm.add(btnLamMoi, gbc);

		// Danh sách khách hàng
		String[] tieuDeKhachHang = { "Mã khách hàng", "Họ tên", "Giới tính", "Số điện thoại", "Điểm tích lũy",
				"Ngày đăng ký", "Trạng thái" };
		dataKH = new DefaultTableModel(tieuDeKhachHang, 0);
		tableKhachHang = new JTable(dataKH);
		scroll = new JScrollPane(tableKhachHang);
		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

		add(scroll, BorderLayout.CENTER);
		
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnTim.addActionListener(this);

		loadDSKhachHang();
		addTableMouseListener();
	}

	// Hàm resize icon cho nút
	private ImageIcon resizeIcon(String path) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	// Load danh sách khách hàng
	private void loadDSKhachHang() {
		khDAO = new KhachHangDAO();
		try {
			dataKH.setRowCount(0);
			for (KhachHang kh : khDAO.layDanhSachKhachHang()) {
				String gioiTinhStr = kh.isGioiTinh() ? "Nam" : "Nữ";
				dataKH.addRow(new Object[] { kh.getMaKH(), kh.getHoTen(), gioiTinhStr, kh.getSdt(),
						kh.getDiemTichLuy(), kh.getNgayDangKy(), kh.isTrangThai() ? "Hoạt động" : "Ngừng" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Thêm sự kiện click chuột vào bảng
	private void addTableMouseListener() {
		tableKhachHang.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableKhachHang.getSelectedRow();
				if (row != -1) {
					txtMaKH.setText(dataKH.getValueAt(row, 0).toString());
					txtHoTenKH.setText(dataKH.getValueAt(row, 1).toString());
					String gioiTinh = dataKH.getValueAt(row, 2).toString();
					cbGioiTinh.setSelectedItem(gioiTinh);
					txtSDT.setText(dataKH.getValueAt(row, 3).toString());
					
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o= e.getSource();
		if(o== btnSua) {
			suaKH();
		}
		
		else if(o== btnXoa) {
			xoaKH(); // chỉ chuyển khách hàng sang trạng thái 0, vẫn giữ trên database, không hiện trên bảng
		}
		else if(o== btnLamMoi) {
			lamMoi();
		}
		else if(o== btnTim) {
			timKhachHangTheoTen();
		}
		
	}
	
	private void suaKH() {
	    int row = tableKhachHang.getSelectedRow();
	    if (row == -1) {
	        javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
	        return;
	    }

	    try {
	        String maKH = txtMaKH.getText().trim();
	        String hoTen = txtHoTenKH.getText().trim();
	        boolean gioiTinh = cbGioiTinh.getSelectedItem().toString().equals("Nam");
	        String sdt = txtSDT.getText().trim();
	        
	        if (hoTen.isEmpty() || sdt.isEmpty()) {
	            javax.swing.JOptionPane.showMessageDialog(this, "Họ tên và số điện thoại không được để trống!");
	            return;
	        }
	        
	        KhachHang kh = new KhachHang(maKH, hoTen, gioiTinh, sdt, 0, null, true);
	        

	        if (khDAO.capNhatKhachHang(kh)) {
	            javax.swing.JOptionPane.showMessageDialog(this, "Sửa thông tin khách hàng thành công!");
	            loadDSKhachHang();
	        } else {
	            javax.swing.JOptionPane.showMessageDialog(this, "Sửa thất bại!");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi sửa khách hàng!");
	    }
	}

	// "Xóa" khách hàng (đổi trạng thái sang 0)
	private void xoaKH() {
	    int row = tableKhachHang.getSelectedRow();
	    if (row == -1) {
	        javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
	        return;
	    }

	    String maKH = dataKH.getValueAt(row, 0).toString();
	    int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
	            "Bạn có chắc muốn ngừng hoạt động khách hàng này?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION);

	    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
	        try {
	            if (khDAO.capNhatTrangThai(maKH, false)) { // false = trạng thái 0
	                javax.swing.JOptionPane.showMessageDialog(this, "Đã chuyển khách hàng sang trạng thái ngừng hoạt động.");
	                loadDSKhachHang();
	            } else {
	                javax.swing.JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng!");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi xóa khách hàng!");
	        }
	    }
	}

	// Làm mới form
	private void lamMoi() {
	    txtMaKH.setText("");
	    txtHoTenKH.setText("");
	    cbGioiTinh.setSelectedIndex(0);
	    txtSDT.setText("");
	    tableKhachHang.clearSelection();
	    
	    loadDSKhachHang();
	}
	
	private void timKhachHangTheoTen() {
	    String ten = txtHoTenKH.getText().trim();
	    if (ten.isEmpty()) {
	        javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng cần tìm!");
	        return;
	    }

	    try {
	        dataKH.setRowCount(0); // Xóa dữ liệu cũ trên bảng
	        for (KhachHang kh : khDAO.layDanhSachKhachHang()) {
	            if (kh.getHoTen().toLowerCase().contains(ten.toLowerCase())) {
	                String gioiTinhStr = kh.isGioiTinh() ? "Nam" : "Nữ";
	                dataKH.addRow(new Object[]{
	                    kh.getMaKH(), kh.getHoTen(), gioiTinhStr, kh.getSdt(),
	                    kh.getDiemTichLuy(), kh.getNgayDangKy(),
	                    kh.isTrangThai() ? "Hoạt động" : "Ngừng"
	                });
	            }
	        }

	        if (dataKH.getRowCount() == 0) {
	            javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào có tên chứa: " + ten);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi tìm khách hàng!");
	    }
	}

}
