package gui;

import entity.HoaDon;
import entity.ChiTietHoaDon;
import dao.ChiTietHoaDonDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class DialogChiTietHoaDonTheoNgay extends JDialog {
    private static final long serialVersionUID = 1L;

    // Components
    private JTable tableHoaDon;
    private JTable tableChiTiet;
    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;
    private JLabel lblTongCong;
    private List<HoaDon> danhSachHoaDon;
    private List<HoaDon> danhSachGoc; // Lưu danh sách gốc để lọc

    // Lọc
    private JComboBox<String> cbTieuChi;
    private JTextField txtTimKiem;
    private TableRowSorter<DefaultTableModel> sorter;

    // Formatter
    private final DecimalFormat df = new DecimalFormat("#,###");

    // Fonts
    private static final Font FONT_VN = new Font("Arial", Font.PLAIN, 13);
    private static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 13);

    public DialogChiTietHoaDonTheoNgay(List<HoaDon> danhSachHoaDon) {
        this.danhSachHoaDon = new ArrayList<>(danhSachHoaDon);
        this.danhSachGoc = new ArrayList<>(danhSachHoaDon);

        setTitle("Chi tiết hóa đơn theo ngày");
        setSize(1250, 780);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        initComponents();
        loadHoaDonData(danhSachGoc);

        // Tự động chọn dòng đầu
        if (tableHoaDon.getRowCount() > 0) {
            tableHoaDon.setRowSelectionInterval(0, 0);
        }
    }

    private void initComponents() {
        // === THANH LỌC ===
        JPanel panelLoc = new JPanel(new BorderLayout(10, 5));
        panelLoc.setBackground(Color.WHITE);
        panelLoc.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTieuChi = new JLabel("Lọc theo:");
        lblTieuChi.setFont(FONT_HEADER);

        String[] tieuChi = {"Tất cả", "Mã HD", "Nhân viên", "Khách hàng", "Bàn", "Loại"};
        cbTieuChi = new JComboBox<>(tieuChi);
        cbTieuChi.setFont(FONT_VN);

        txtTimKiem = new JTextField(25);
        txtTimKiem.setFont(FONT_VN);

        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(FONT_VN);
        btnLamMoi.setBackground(new Color(46, 204, 113));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);

        panelLoc.add(lblTieuChi, BorderLayout.WEST);
        panelLoc.add(cbTieuChi, BorderLayout.CENTER);
        panelLoc.add(txtTimKiem, BorderLayout.EAST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(btnLamMoi);
        panelLoc.add(rightPanel, BorderLayout.SOUTH);

        // === PHẦN 1: DANH SÁCH HÓA ĐƠN ===
        JPanel panelHoaDon = createTitledPanel("DANH SÁCH HÓA ĐƠN", new Color(41, 128, 185));
        String[] colsHD = {"Mã HD", "Loại", "Nhân viên", "Khách hàng", "Bàn", "Tổng tiền", "Trạng thái"};
        modelHoaDon = new DefaultTableModel(colsHD, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHoaDon = new JTable(modelHoaDon);
        setupTable(tableHoaDon, new Color(41, 128, 185));

        // Sorter để lọc
        sorter = new TableRowSorter<>(modelHoaDon);
        tableHoaDon.setRowSorter(sorter);

        JScrollPane scrollHD = new JScrollPane(tableHoaDon);
        panelHoaDon.add(scrollHD, BorderLayout.CENTER);

        // === PHẦN 2: CHI TIẾT MÓN ĂN ===
        JPanel panelMonAn = createTitledPanel("CHI TIẾT MÓN ĂN", new Color(39, 174, 96));
        String[] colsCT = {"STT", "Món ăn", "SL", "Đơn giá", "Thành tiền", "Ghi chú"};
        modelChiTiet = new DefaultTableModel(colsCT, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet = new JTable(modelChiTiet);
        setupTable(tableChiTiet, new Color(39, 174, 96));
        JScrollPane scrollCT = new JScrollPane(tableChiTiet);

        lblTongCong = new JLabel("Tổng cộng: 0 VNĐ", SwingConstants.RIGHT);
        lblTongCong.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongCong.setForeground(new Color(39, 174, 96));
        lblTongCong.setBorder(BorderFactory.createEmptyBorder(8, 15, 12, 20));

        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.add(scrollCT, BorderLayout.CENTER);
        bottomWrapper.add(lblTongCong, BorderLayout.SOUTH);
        panelMonAn.add(bottomWrapper, BorderLayout.CENTER);

        // === GỘP TẤT CẢ ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(panelLoc, BorderLayout.NORTH);
        topPanel.add(panelHoaDon, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(panelMonAn);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // === SỰ KIỆN ===
        // Lọc khi gõ
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { locHoaDon(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { locHoaDon(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { locHoaDon(); }
        });

        // Lọc khi đổi tiêu chí
        cbTieuChi.addActionListener(e -> locHoaDon());

        // Làm mới
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            cbTieuChi.setSelectedIndex(0);
            loadHoaDonData(danhSachGoc);
            if (tableHoaDon.getRowCount() > 0) {
                tableHoaDon.setRowSelectionInterval(0, 0);
            }
        });

        // Chọn dòng → load chi tiết
        tableHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = tableHoaDon.getSelectedRow();
                if (viewRow != -1) {
                    int modelRow = tableHoaDon.convertRowIndexToModel(viewRow);
                    String maHD = (String) modelHoaDon.getValueAt(modelRow, 0);
                    loadChiTietHoaDon(maHD);
                }
            }
        });
    }

    private JPanel createTitledPanel(String title, Color titleColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(titleColor, 2),
                title,
                0, 0,
                new Font("Arial", Font.BOLD, 15),
                titleColor
        ));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private void setupTable(JTable table, Color headerColor) {
        table.setRowHeight(32);
        table.setFont(FONT_VN);
        table.getTableHeader().setFont(FONT_HEADER);
        table.getTableHeader().setBackground(headerColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadHoaDonData(List<HoaDon> danhSach) {
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : danhSach) {
            String loai = hd.getBanAn() == null ? "Mang đi" : "Tại bàn";
            String ban = hd.getBanAn() != null ? hd.getBanAn().getTenBan() : "—";
            String kh = hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "Khách lẻ";
            String nv = hd.getNhanVien() != null ? hd.getNhanVien().getHoTen() : "—";

            modelHoaDon.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    loai,
                    nv,
                    kh,
                    ban,
                    df.format(hd.getTongTien()) + " VNĐ",
                    hd.getTrangThai()
            });
        }
    }

    private void locHoaDon() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String tieuChi = (String) cbTieuChi.getSelectedItem();

        if (keyword.isEmpty() || "Tất cả".equals(tieuChi)) {
            loadHoaDonData(danhSachGoc);
        } else {
            List<HoaDon> filtered = danhSachGoc.stream()
                    .filter(hd -> {
                        String value = "";
                        switch (tieuChi) {
                            case "Mã HD":
                                value = hd.getMaHoaDon().toLowerCase();
                                break;
                            case "Nhân viên":
                                value = hd.getNhanVien() != null ? hd.getNhanVien().getHoTen().toLowerCase() : "";
                                break;
                            case "Khách hàng":
                                value = hd.getKhachHang() != null ? hd.getKhachHang().getHoTen().toLowerCase() : "khách lẻ";
                                break;
                            case "Bàn":
                                value = hd.getBanAn() != null ? hd.getBanAn().getTenBan().toLowerCase() : "mang đi";
                                break;
                            case "Loại":
                                value = hd.getBanAn() == null ? "mang đi" : "tại bàn";
                                break;
                        }
                        return value.contains(keyword);
                    })
                    .collect(Collectors.toList());
            loadHoaDonData(filtered);
        }

        // Tự động chọn dòng đầu nếu có
        if (tableHoaDon.getRowCount() > 0) {
            tableHoaDon.setRowSelectionInterval(0, 0);
        } else {
            modelChiTiet.setRowCount(0);
            lblTongCong.setText("Tổng cộng: 0 VNĐ");
        }
    }

    private void loadChiTietHoaDon(String maHoaDon) {
        modelChiTiet.setRowCount(0);
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> list = cthdDAO.layDanhSachTheoHoaDon(maHoaDon);

        double tong = 0;
        int stt = 1;
        for (ChiTietHoaDon cthd : list) {
            double thanhTien = cthd.getThanhTien();
            tong += thanhTien;
            modelChiTiet.addRow(new Object[]{
                    stt++,
                    cthd.getMonAn().getTenMon(),
                    cthd.getSoLuong(),
                    df.format(cthd.getDonGia()) + " VNĐ",
                    df.format(thanhTien) + " VNĐ",
                    cthd.getGhiChu() != null && !cthd.getGhiChu().trim().isEmpty() ? cthd.getGhiChu() : "—"
            });
        }

        lblTongCong.setText("Tổng cộng: " + df.format(tong) + " VNĐ");
    }
}