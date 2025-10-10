package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraCuuKhachHang extends JPanel {
    private JScrollPane scroll;
	private JTable tableKhachHang;
	private DefaultTableModel dataKH;
	private JLabel lblNhap;
	private JTextField txtTim;
	private JButton btnTim;
	private JButton btnLamMoi;

	public TraCuuKhachHang() {
        setLayout(new BorderLayout());

        // ✅ Tiêu đề màn hình
        JLabel lblTitle = new JLabel("TRA CỨU KHÁCH HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);
        
       String[] tieuDeKhachHang = {"Mã khách hàng", "Tên khách hàng", "Giới tính", "Số điện thoại", "Email", "Điểm tích lũy"};
       scroll = new JScrollPane(tableKhachHang = new JTable(dataKH = new DefaultTableModel(tieuDeKhachHang, 0)));
       scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
       
       add(scroll, BorderLayout.CENTER);
       
       
      JPanel pnTimKiem = new JPanel();
      add(pnTimKiem, BorderLayout.SOUTH);
      pnTimKiem.add(lblNhap = new JLabel("Nhập thông tin: "));
      pnTimKiem.add(txtTim = new JTextField(10));
      
      ImageIcon iconTim = new ImageIcon("img/search.png");
      Image imgTim = iconTim.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); //thu nhỏ icon
      btnTim = new JButton("Tìm", new ImageIcon(imgTim));
      btnTim.setPreferredSize(new Dimension(100, 30));
      btnTim.setIconTextGap(5);// Tạo khoảng cách giữa icon và chữ
      pnTimKiem.add(btnTim);
      
      ImageIcon iconLamMoi = new ImageIcon("img/refresh.png");
      Image imgLamMoi = iconLamMoi.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); //thu nhỏ icon
      btnLamMoi = new JButton("Làm mới", new ImageIcon(imgLamMoi));
      btnLamMoi.setPreferredSize(new Dimension(120, 30));
      btnLamMoi.setIconTextGap(5);// Tạo khoảng cách giữa icon và chữ
      pnTimKiem.add(btnLamMoi);
      
      
    }
}
