package gui;

import dao.MonAnDAO;
import entity.MonAn;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThemMonAn extends JPanel {

    private final MonAnDAO dao = new MonAnDAO();
    private List<MonAn> danhSachMonAn;
    private String selectedHinhAnhPath;
    private boolean dangTuDongPhatSinhMa = false;

    // UI
    private JTextField txtMaMA, txtTenMA, txtGia, txtSoLuong, txtTimKiem;
    private JComboBox<String> cbLoaiMA, cbTrangThai, cbDVT, cbTieuChiTimKiem;
    private JTextArea txtMota;
    private JLabel lblAnh;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnThemLoaiMon, btnThemHinhAnh;

    public ThemMonAn() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#EAF1F9"));

        add(taoTieuDe(), BorderLayout.NORTH);
        add(taoFormNhap(), BorderLayout.CENTER);
        add(taoBangVaTimKiem(), BorderLayout.EAST);
        add(taoNutChucNang(), BorderLayout.SOUTH);

        try {
            loadLoaiMonAn();
            loadDonViTinh();
            loadTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }

        btnThem.addActionListener(e -> themMonAn());
        btnSua.addActionListener(e -> suaMonAn());
        btnXoa.addActionListener(e -> xoaMonAn());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnThemLoaiMon.addActionListener(e -> themLoaiMonMoi());
        btnThemHinhAnh.addActionListener(e -> chonHinhAnh());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dayDuaLieuLenTable();
            }
        });

        // TÌM KIẾM TỰ ĐỘNG KHI CHỌN TIÊU CHÍ HOẶC NHẬP TỪ KHÓA
        cbTieuChiTimKiem.addActionListener(e -> timKiemTuDong());
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiemTuDong(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiemTuDong(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiemTuDong(); }
        });
    }

    private void timKiemTuDong() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String criteria = (String) cbTieuChiTimKiem.getSelectedItem();
        List<MonAn> result = new ArrayList<>();

        for (MonAn m : danhSachMonAn) {
            if (!m.isTrangThai()) continue;
            if (matches(m, keyword, criteria)) result.add(m);
        }
        capNhatBang(result);
    }

    private JPanel taoTieuDe() {
        JPanel pn = new JPanel();
        pn.setBackground(Color.decode("#EAF1F9"));
        JLabel lbl = new JLabel("QUẢN LÝ MÓN ĂN");
        lbl.setFont(new Font("Arial", Font.BOLD, 26));
        lbl.setForeground(Color.decode("#2C3E50"));
        pn.add(lbl);
        return pn;
    }

    private JPanel taoFormNhap() {
        JPanel pnForm = new JPanel(new GridBagLayout());
        pnForm.setBackground(Color.decode("#EAF1F9"));
        pnForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Thông tin món ăn",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                Color.DARK_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Mã + Tên
        gbc.gridx = 0; gbc.gridy = row;
        pnForm.add(new JLabel("Mã món ăn:"), gbc);
        gbc.gridx = 1;
        txtMaMA = new JTextField(); txtMaMA.setEditable(false);
        pnForm.add(txtMaMA, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Tên món ăn:"), gbc);
        gbc.gridx = 3;
        txtTenMA = new JTextField();
        pnForm.add(txtTenMA, gbc);

        row++;

        // Loại + Trạng thái
        gbc.gridx = 0; gbc.gridy = row;
        pnForm.add(new JLabel("Loại món:"), gbc);
        gbc.gridx = 1;
        cbLoaiMA = new JComboBox<>();
        cbLoaiMA.addActionListener(e -> {
            if (!dangTuDongPhatSinhMa && cbLoaiMA.getSelectedItem() != null) {
                String tenLoai = (String) cbLoaiMA.getSelectedItem();
                try {
                    String maLoai = dao.chuyenTenLoaiSangMa(tenLoai);
                    phatSinhMaMon(maLoai);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JPanel pnLoai = new JPanel(new BorderLayout());
        pnLoai.add(cbLoaiMA, BorderLayout.CENTER);
        btnThemLoaiMon = new JButton();
        btnThemLoaiMon.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        btnThemLoaiMon.setToolTipText("Thêm loại món mới");
        btnThemLoaiMon.setPreferredSize(new Dimension(30, 26));
        ImageIcon icon_them = new ImageIcon("img//thucdon//add_icon.png");
        Dimension size = btnThemLoaiMon.getPreferredSize();
        Image img = icon_them.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        btnThemLoaiMon.setIcon(new ImageIcon(img));
        pnLoai.add(btnThemLoaiMon, BorderLayout.EAST);
        pnForm.add(pnLoai, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 3;
        cbTrangThai = new JComboBox<>(new String[]{"Còn phục vụ", "Hết phục vụ"});
        pnForm.add(cbTrangThai, gbc);

        row++;

        // Số lượng + Đơn vị
        gbc.gridx = 0; gbc.gridy = row;
        pnForm.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        txtSoLuong = new JTextField();
        txtSoLuong.setText(0+"");
        pnForm.add(txtSoLuong, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Đơn vị tính:"), gbc);
        gbc.gridx = 3;
        cbDVT = new JComboBox<>();
        pnForm.add(cbDVT, gbc);

        row++;

        // Giá
        gbc.gridx = 0; gbc.gridy = row;
        pnForm.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        txtGia = new JTextField();
        pnForm.add(txtGia, gbc);

        row++;

        // Mô tả
        gbc.gridx = 0; gbc.gridy = row;
        pnForm.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtMota = new JTextArea(3, 35);
        txtMota.setLineWrap(true); txtMota.setWrapStyleWord(true);
        pnForm.add(new JScrollPane(txtMota), gbc);
        gbc.gridwidth = 1;

        row++;

        // Hình ảnh
        gbc.gridx = 0; gbc.gridy = row;
        pnForm.add(new JLabel("Hình ảnh:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        JPanel pnHinhAnh = new JPanel(new BorderLayout());
        pnHinhAnh.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(240, 240));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        lblAnh.setBackground(Color.WHITE);
        lblAnh.setOpaque(true);
        pnHinhAnh.add(lblAnh, BorderLayout.CENTER);

        btnThemHinhAnh = new JButton("Chọn hình ảnh");
        btnThemHinhAnh.setPreferredSize(new Dimension(240, 30));
        pnHinhAnh.add(btnThemHinhAnh, BorderLayout.SOUTH);

        pnForm.add(pnHinhAnh, gbc);
        gbc.gridwidth = 1;

        return pnForm;
    }

    private void chonHinhAnh() {
        JFileChooser fc = new JFileChooser("img//thucdon");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            selectedHinhAnhPath = f.getAbsolutePath();
            ImageIcon icon = new ImageIcon(selectedHinhAnhPath);
            Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
            lblAnh.setIcon(new ImageIcon(img));
        }
    }

    private JPanel taoBangVaTimKiem() {
        JPanel pnEast = new JPanel(new BorderLayout());
        pnEast.setPreferredSize(new Dimension(720, 0));
        pnEast.setBorder(BorderFactory.createTitledBorder("Danh sách món ăn"));

        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        pnSearch.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnSearch.add(new JLabel("Tìm theo:"));

        cbTieuChiTimKiem = new JComboBox<>(new String[]{
                "Tất cả", "Mã món", "Tên món", "Giá", "ĐVT", "Trạng thái", "SL", "Mô tả", "Loại"
        });
        pnSearch.add(cbTieuChiTimKiem);

        txtTimKiem = new JTextField(18);
        pnSearch.add(txtTimKiem);

        pnEast.add(pnSearch, BorderLayout.NORTH);

        String[] cols = {"Mã", "Tên món", "Giá", "DVT", "Trạng thái", "SL", "Mô tả", "Loại", "Ảnh"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(model);
        table.setRowHeight(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(180);
        pnEast.add(new JScrollPane(table), BorderLayout.CENTER);
        return pnEast;
    }

    private JPanel taoNutChucNang() {
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        pnSouth.setBackground(Color.decode("#EAF1F9"));
        btnThem = taoNut("Thêm", "img/add.png");
        btnSua = taoNut("Sửa", "img/edit.png");
        btnXoa = taoNut("Xóa", "img/delete.png");
        btnLamMoi = taoNut("Làm mới", "img/refresh.png");
        pnSouth.add(btnThem); pnSouth.add(btnSua); pnSouth.add(btnXoa); pnSouth.add(btnLamMoi);
        return pnSouth;
    }

    private JButton taoNut(String text, String iconPath) {
        JButton btn = new JButton(text, scaleIcon(iconPath, 18, 18));
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    private ImageIcon scaleIcon(String path, int w, int h) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) { return new ImageIcon(); }
    }

    // =================== LOAD DỮ LIỆU ===================
    private void loadLoaiMonAn() throws SQLException {
        ArrayList<String> dsTenLoai = dao.layRaLoaiMonAn();
        cbLoaiMA.removeAllItems();
        for (String ten : dsTenLoai) cbLoaiMA.addItem(ten);
        if (cbLoaiMA.getItemCount() > 0) cbLoaiMA.setSelectedIndex(0);
    }

    private void loadDonViTinh() throws SQLException {
        ArrayList<String> ds = dao.layRaDonViTinh();
        cbDVT.removeAllItems();
        for (String dvt : ds) cbDVT.addItem(dvt);
        if (cbDVT.getItemCount() > 0) cbDVT.setSelectedIndex(0);
    }

    private void phatSinhMaMon(String maLoai) {
        String maLonNhat = dao.layMaMonLonNhatTheoLoai(maLoai);
        String maMoi = maLonNhat == null ? maLoai + "01" :
                maLoai + String.format("%02d", Integer.parseInt(maLonNhat.substring(maLoai.length())) + 1);
        txtMaMA.setText(maMoi);
    }

    private void loadTable() throws SQLException {
        danhSachMonAn = dao.getAllMonAn();
        capNhatBang(danhSachMonAn);
    }

    private void capNhatBang(List<MonAn> ds) {
        model.setRowCount(0);
        for (MonAn m : ds) {
            if (!m.isTrangThai()) continue;
            String tenLoai = "Không xác định";
            try {
                tenLoai = dao.chuyenMaLoaiSangTen(m.getLoaiMon());
            } catch (SQLException e) { e.printStackTrace(); }

            model.addRow(new Object[]{
                    m.getMaMon(),
                    m.getTenMon(),
                    m.getGia(),
                    m.getDonViTinh(),
                    m.isTrangThai() ? "Còn phục vụ" : "Hết phục vụ",
                    m.getSoLuong(),
                    m.getMoTa(),
                    tenLoai,
                    m.getHinhAnh()
            });
        }
    }

    // =================== TÌM KIẾM TỰ ĐỘNG ===================
    private boolean matches(MonAn m, String k, String crit) {
        try {
            String tenLoai = dao.chuyenMaLoaiSangTen(m.getLoaiMon());
            return switch (crit) {
                case "Tất cả" -> contains(m.getMaMon(), k) || contains(m.getTenMon(), k) ||
                        String.valueOf(m.getGia()).contains(k) || contains(m.getDonViTinh(), k) ||
                        (m.isTrangThai() ? "còn" : "hết").contains(k) ||
                        String.valueOf(m.getSoLuong()).contains(k) ||
                        contains(m.getMoTa(), k) || contains(tenLoai, k);
                case "Mã món" -> contains(m.getMaMon(), k);
                case "Tên món" -> contains(m.getTenMon(), k);
                case "Giá" -> String.valueOf(m.getGia()).contains(k);
                case "ĐVT" -> contains(m.getDonViTinh(), k);
                case "Trạng thái" -> (m.isTrangThai() ? "còn" : "hết").contains(k);
                case "SL" -> String.valueOf(m.getSoLuong()).contains(k);
                case "Mô tả" -> contains(m.getMoTa(), k);
                case "Loại" -> contains(tenLoai, k);
                default -> false;
            };
        } catch (SQLException e) { return false; }
    }

    private boolean contains(String s, String k) {
        return s != null && s.toLowerCase().contains(k);
    }

    // =================== THÊM LOẠI MÓN ===================
    private void themLoaiMonMoi() {
        DialogThemLoaiMon dialog = new DialogThemLoaiMon((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                if (dao.themLoaiMon(dialog.getMaLoai(), dialog.getTenLoai())) {
                    JOptionPane.showMessageDialog(this, "Thêm loại món thành công!");
                    loadLoaiMonAn();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Mã loại có thể đã tồn tại.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    // =================== CRUD ===================
    private void themMonAn() {
        try {
            String tenMon = txtTenMA.getText().trim();
            if (tenMon.isEmpty()) { JOptionPane.showMessageDialog(this, "Tên món không được để trống!"); return; }

            String maMon = txtMaMA.getText().trim();
            double gia = Double.parseDouble(txtGia.getText().trim());
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            String dvt = (String) cbDVT.getSelectedItem();
            boolean trangThai = "Còn phục vụ".equals(cbTrangThai.getSelectedItem());
            String moTa = txtMota.getText().trim();
            String tenLoai = (String) cbLoaiMA.getSelectedItem();
            String loaiMon = dao.chuyenTenLoaiSangMa(tenLoai);

            String hinhAnh = "";
            if (selectedHinhAnhPath != null) {
                File src = new File(selectedHinhAnhPath);
                hinhAnh = src.getName();
                Files.copy(src.toPath(), new File("img/thucdon/" + hinhAnh).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            MonAn mon = new MonAn(maMon, tenMon, gia, dvt, trangThai, "img/thucdon/" + hinhAnh, soLuong, moTa, loaiMon);
            if (dao.themMonMoi(mon)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                lamMoiForm(); loadTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void suaMonAn() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn món để sửa!"); return; }
        try {
            String maMon = txtMaMA.getText().trim();
            String tenMon = txtTenMA.getText().trim();
            double gia = Double.parseDouble(txtGia.getText().trim());
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            String dvt = (String) cbDVT.getSelectedItem();
            boolean trangThai = "Còn phục vụ".equals(cbTrangThai.getSelectedItem());
            String moTa = txtMota.getText().trim();
            String tenLoai = (String) cbLoaiMA.getSelectedItem();
            String loaiMon = dao.chuyenTenLoaiSangMa(tenLoai);

            String hinhAnh = danhSachMonAn.get(row).getHinhAnh();
            if (selectedHinhAnhPath != null) {
                File src = new File(selectedHinhAnhPath);
                hinhAnh = "img/thucdon/" + src.getName();
                Files.copy(src.toPath(), new File(hinhAnh).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            MonAn mon = new MonAn(maMon, tenMon, gia, dvt, trangThai, hinhAnh, soLuong, moTa, loaiMon);
            if (dao.suaMonAn(mon)) {
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                lamMoiForm(); loadTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void xoaMonAn() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn món để xóa!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa món này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String maMon = (String) table.getValueAt(row, 0);
            try {
                if (dao.anMonAn(maMon)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    lamMoiForm(); loadTable();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void lamMoiForm() {
        txtMaMA.setText(""); txtTenMA.setText(""); txtGia.setText(""); txtSoLuong.setText("");
        txtMota.setText(""); lblAnh.setIcon(null); selectedHinhAnhPath = null;
        if (cbLoaiMA.getItemCount() > 0) cbLoaiMA.setSelectedIndex(0);
        if (cbDVT.getItemCount() > 0) cbDVT.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }

    private void dayDuaLieuLenTable() {
        dangTuDongPhatSinhMa = true;
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtMaMA.setText((String) model.getValueAt(row, 0));
        txtTenMA.setText((String) model.getValueAt(row, 1));
        txtGia.setText(model.getValueAt(row, 2).toString());
        cbDVT.setSelectedItem(model.getValueAt(row, 3));
        cbTrangThai.setSelectedItem(model.getValueAt(row, 4));
        txtSoLuong.setText(model.getValueAt(row, 5).toString());
        txtMota.setText((String) model.getValueAt(row, 6));

        String tenLoai = (String) model.getValueAt(row, 7);
        cbLoaiMA.setSelectedItem(tenLoai);

        String imgPath = (String) model.getValueAt(row, 8);
        if (imgPath != null && !imgPath.isEmpty()) {
            selectedHinhAnhPath = imgPath;
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
            lblAnh.setIcon(new ImageIcon(img));
        } else {
            lblAnh.setIcon(null);
        }
        dangTuDongPhatSinhMa = false;
    }
}