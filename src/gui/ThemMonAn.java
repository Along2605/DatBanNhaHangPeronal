package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ThemMonAn extends JPanel {
    private JButton btnLamMoi;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
    private JTextField txtGia;
    private JTextField txtTenMA;
    private JComboBox cbLoaiMA;
    private JComboBox cbTrangThai;
    private JTextField txtSoLuong;
    private JComboBox cbDVT;
    private JButton btnChonHinh;
    private JTextArea txtMota;
    private JLabel lblAnh;
    private JTextField txtMaMA;

    public ThemMonAn() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#EAF1F9"));

        Box boxTop = Box.createVerticalBox();

        // Tiêu đề
        JLabel lblTitle = new JLabel("THÊM MÓN ĂN");
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
        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(new JLabel("Mã món ăn "), gbc);
        gbc.gridx = 1;
        txtMaMA = new JTextField(15);
        txtMaMA.setEditable(false);
        pnForm.add(txtMaMA, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Tên món ăn: "), gbc);
        gbc.gridx = 3;
        txtTenMA = new JTextField(15);
        pnForm.add(txtTenMA, gbc);

        // Hàng 2
        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(new JLabel("Loại món ăn"), gbc);
        gbc.gridx = 1;
        cbLoaiMA = new JComboBox<>(new String[] {});
        pnForm.add(cbLoaiMA, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Trạng thái"), gbc);
        gbc.gridx = 3;
        cbTrangThai = new JComboBox<>(new String[] {"Còn phục vụ", "Hết phục vụ"});
        pnForm.add(cbTrangThai, gbc);

        // Hàng 3
        gbc.gridx = 0; gbc.gridy = 2;
        pnForm.add(new JLabel("Số lượng "), gbc);
        gbc.gridx = 1;
        txtSoLuong = new JTextField(15);
        pnForm.add(txtSoLuong, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Đơn vị tính"), gbc);
        gbc.gridx = 3;
        cbDVT = new JComboBox<>(new String[] {});
        pnForm.add(cbDVT, gbc);

        // Hàng 4: Giá (trái) & Hình ảnh (phải)
        gbc.gridx = 0; gbc.gridy = 3;
        pnForm.add(new JLabel("Giá "), gbc);
        gbc.gridx = 1;
        txtGia = new JTextField(15);
        pnForm.add(txtGia, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        gbc.gridheight = 2; // Hình ảnh chiếm 2 hàng
        pnForm.add(new JLabel("Hình ảnh"), gbc);

        gbc.gridx = 3; gbc.gridy = 3;
        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(200, 200));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        pnForm.add(lblAnh, gbc);
        gbc.gridheight = 1; // reset về mặc định

        // Sự kiện chọn ảnh
        lblAnh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chonFile = new JFileChooser();
                int kq = chonFile.showOpenDialog(null);
                if (kq == JFileChooser.APPROVE_OPTION) {
                    File file = chonFile.getSelectedFile();
                    // Xóa ảnh cũ
                    if (lblAnh.getIcon() != null) {
                        lblAnh.setIcon(null);
                    }
                    ImageIcon iconAnh = new ImageIcon(file.getAbsolutePath());
                    Image anh = iconAnh.getImage();
                    Image anhMA = anh.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    iconAnh = new ImageIcon(anhMA);
                    lblAnh.setIcon(iconAnh);

                    pnForm.revalidate();
                    pnForm.repaint();
                }
            }
        });

        // Hàng 5: Mô tả (bên trái, dưới giá)
        gbc.gridx = 0; gbc.gridy = 4;
        pnForm.add(new JLabel("Mô tả "), gbc);
        gbc.gridx = 1;
        txtMota = new JTextArea(3, 20);
        JScrollPane scrollMota = new JScrollPane(txtMota);
        pnForm.add(scrollMota, gbc);

        // Các nút chức năng
        gbc.gridy = 5;
        gbc.gridx = 0;
        ImageIcon iconSua = new ImageIcon("img/edit.png");
        Image imgSua = iconSua.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        btnSua = new JButton("Sửa", new ImageIcon(imgSua));
        btnSua.setPreferredSize(new Dimension(120, 30));
        pnForm.add(btnSua, gbc);

        gbc.gridx = 1;
        ImageIcon iconThem = new ImageIcon("img/add.png");
        Image imgThem = iconThem.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        btnThem = new JButton("Thêm", new ImageIcon(imgThem));
        btnThem.setPreferredSize(new Dimension(120, 30));
        pnForm.add(btnThem, gbc);

        gbc.gridx = 2;
        ImageIcon iconXoa = new ImageIcon("img/delete.png");
        Image imgXoa = iconXoa.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        btnXoa = new JButton("Xóa", new ImageIcon(imgXoa));
        btnXoa.setPreferredSize(new Dimension(120, 30));
        pnForm.add(btnXoa, gbc);

        gbc.gridx = 3;
        ImageIcon iconLamMoi = new ImageIcon("img/refresh.png");
        Image imgLamMoi = iconLamMoi.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        btnLamMoi = new JButton("Làm mới", new ImageIcon(imgLamMoi));
        btnLamMoi.setPreferredSize(new Dimension(120, 30));
        pnForm.add(btnLamMoi, gbc);
    }
}