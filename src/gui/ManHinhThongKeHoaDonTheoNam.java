package gui;

import dao.HoaDonDAO;
import entity.HoaDon;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import util.InHoaDonPDF;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManHinhThongKeHoaDonTheoNam extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel lblDoanhThu_TongTien, lblHoaDonTaiBan_SoLuong, lblHoaDonMangDi_SoLuong;
    private JTextField textFieldTongSoHoaDon, textFieldTongTienHoaDon;
    private DefaultTableModel model;
    private JTable table;
    private JPanel panelBieuDo;

    private JComboBox<Integer> comboNam;
    private int namHienTai = LocalDate.now().getYear();
    private Map<Integer, double[]> mapHienTai; // tháng -> [số HĐ, tổng tiền]

    public ManHinhThongKeHoaDonTheoNam() {
        setPreferredSize(new Dimension(1366, 768));
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(null);
        add(contentPane, BorderLayout.CENTER);

        // === TIÊU ĐỀ ===
        JLabel lblTieuDe = new JLabel("THỐNG KÊ HÓA ĐƠN THEO NĂM", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 27));
        lblTieuDe.setBounds(460, 0, 470, 59);
        contentPane.add(lblTieuDe);

        // === PANEL LỌC THEO NĂM ===
        JPanel panelLocNam = new JPanel();
        panelLocNam.setBounds(49, 57, 281, 95);
        panelLocNam.setLayout(null);
        panelLocNam.setBorder(BorderFactory.createTitledBorder("Lọc dữ liệu"));
        contentPane.add(panelLocNam);

        JLabel lblNam = new JLabel("Năm");
        lblNam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNam.setBounds(10, 20, 100, 21);
        panelLocNam.add(lblNam);

        comboNam = new JComboBox<>();
        for (int y = 2020; y <= LocalDate.now().getYear() + 1; y++) {
            comboNam.addItem(y);
        }
        comboNam.setSelectedItem(LocalDate.now().getYear());
        comboNam.setBounds(10, 45, 215, 25);
        panelLocNam.add(comboNam);

        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThongKe.setBounds(150, 70, 100, 25);
        panelLocNam.add(btnThongKe);

        // === CÁC PANEL THỐNG KÊ ===
        lblDoanhThu_TongTien = createStatPanel(contentPane, 373, "Doanh thu", "Theo năm", "VNĐ");
        lblHoaDonTaiBan_SoLuong = createStatPanel(contentPane, 678, "Hóa đơn tại bàn", "Theo năm", "đơn");
        lblHoaDonMangDi_SoLuong = createStatPanel(contentPane, 983, "Hóa đơn mang đi", "Theo năm", "đơn");

        // === BIỂU ĐỒ ===
        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBounds(49, 174, 620, 357);
        panelBieuDo.setBorder(BorderFactory.createTitledBorder("Biểu đồ doanh thu"));
        contentPane.add(panelBieuDo);

        // === NÚT ===
        JButton btnXemChiTiet = createButton("Xem chi tiết", 129, 542, e -> xemChiTiet());
        contentPane.add(btnXemChiTiet);

        JButton btnXuatExcel = createButton("Xuất file Excel", 373, 542, e -> xuatExcel());
        btnXuatExcel.setBackground(new Color(46, 204, 113));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFocusPainted(false);
        contentPane.add(btnXuatExcel);

        // === TỔNG HỢP ===
        JLabel lblTongHD = new JLabel("Tổng số hóa đơn");
        lblTongHD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongHD.setBounds(718, 174, 115, 20);
        contentPane.add(lblTongHD);

        JLabel lblTongTien = new JLabel("Tổng tiền hóa đơn");
        lblTongTien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongTien.setBounds(718, 205, 115, 20);
        contentPane.add(lblTongTien);

        textFieldTongSoHoaDon = createTextField(863, 174);
        textFieldTongTienHoaDon = createTextField(863, 205);
        contentPane.add(textFieldTongSoHoaDon);
        contentPane.add(textFieldTongTienHoaDon);

        // === NÚT IN HÓA ĐƠN ===
        JButton btnInHoaDon = new JButton("In hóa đơn");
        btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnInHoaDon.setBounds(1043, 174, 150, 51);
        btnInHoaDon.setBackground(new Color(231, 76, 60));
        btnInHoaDon.setForeground(Color.WHITE);
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.addActionListener(e -> hienThiDialogChonHoaDon());
        contentPane.add(btnInHoaDon);

        // === BẢNG ===
        JPanel panelTable = new JPanel();
        panelTable.setBounds(688, 236, 631, 357);
        panelTable.setLayout(null);
        panelTable.setBorder(BorderFactory.createTitledBorder("Chi tiết theo tháng"));
        contentPane.add(panelTable);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 20, 611, 326);
        panelTable.add(scrollPane);

        String[] cols = {"Tháng", "Tổng số HĐ", "Tổng tiền HĐ"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        scrollPane.setViewportView(table);

        // === SỰ KIỆN ===
        btnThongKe.addActionListener(e -> {
            int nam = (Integer) comboNam.getSelectedItem();
            this.namHienTai = nam;
            capNhatToanBo(nam);
        });

        // Khởi tạo mặc định
        capNhatToanBo(namHienTai);
    }

    // ============================== IN HÓA ĐƠN ==============================
    private void hienThiDialogChonHoaDon() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tháng trong bảng!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int thang = (Integer) model.getValueAt(row, 0);

        List<HoaDon> dsHD = HoaDonDAO.layDanhSachHoaDonTheoThang(thang, namHienTai)
                .stream()
                .filter(hd -> hd.getTrangThai().equalsIgnoreCase("Đã thanh toán"))
                .toList();

        if (dsHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có hóa đơn nào trong tháng này!");
            return;
        }

        new DialogChonHoaDonInTheoNam(dsHD, thang, namHienTai).setVisible(true);
    }

    // ============================== XEM CHI TIẾT ==============================
    private void xemChiTiet() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tháng trong bảng!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int thang = (Integer) model.getValueAt(row, 0);

        // Lấy tất cả hóa đơn đã thanh toán của tháng đó
        List<HoaDon> dsHoaDonThang = HoaDonDAO.layDanhSachHoaDonTheoThang(thang, namHienTai)
                .stream()
                .filter(hd -> "Đã thanh toán".equalsIgnoreCase(hd.getTrangThai()))
                .toList();

        if (dsHoaDonThang.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không có hóa đơn nào trong tháng " + thang + "/" + namHienTai + "!", 
                "Thông tin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tạo map: ngày -> danh sách hóa đơn trong ngày đó
        Map<LocalDate, List<HoaDon>> mapTheoNgay = new HashMap<>();
        for (HoaDon hd : dsHoaDonThang) {
            LocalDate ngay = hd.getNgayLapHoaDon().toLocalDate();
            mapTheoNgay.computeIfAbsent(ngay, k -> new ArrayList<>()).add(hd);
        }

        // Tạo dialog chọn ngày
        JDialog dialogChonNgay = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Chọn ngày xem chi tiết - Tháng " + thang + "/" + namHienTai);
        dialogChonNgay.setSize(400, 500);
        dialogChonNgay.setLocationRelativeTo(this);
        dialogChonNgay.setModal(true);
        dialogChonNgay.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listNgay = new JList<>(listModel);
        listNgay.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Đổ dữ liệu ngày có hóa đơn
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy ");
        mapTheoNgay.keySet().stream()
            .sorted()
            .forEach(ngay -> {
                int soHD = mapTheoNgay.get(ngay).size();
                double tongTien = mapTheoNgay.get(ngay).stream().mapToDouble(HoaDon::getTongTien).sum();
                String text = String.format("%s - %d hóa đơn - %, .0f VNĐ", 
                    ngay.format(fmt), soHD, tongTien);
                listModel.addElement(text);
            });

        JScrollPane scroll = new JScrollPane(listNgay);
        scroll.setBorder(BorderFactory.createTitledBorder("Chọn ngày để xem chi tiết hóa đơn"));
        dialogChonNgay.add(scroll, BorderLayout.CENTER);

        JPanel panelBtn = new JPanel(new FlowLayout());
        JButton btnXem = new JButton("Xem chi tiết");
        JButton btnHuy = new JButton("Đóng");

        btnXem.addActionListener(e -> {
            int selected = listNgay.getSelectedIndex();
            if (selected == -1) {
                JOptionPane.showMessageDialog(dialogChonNgay, "Vui lòng chọn một ngày!");
                return;
            }
            LocalDate ngayChon = mapTheoNgay.keySet().stream().sorted().toList().get(selected);
            List<HoaDon> dsNgayChon = mapTheoNgay.get(ngayChon);

            // DÙNG LẠI HOÀN TOÀN DIALOG CỦA THEO NGÀY
            new DialogChiTietHoaDonTheoNgay(dsNgayChon).setVisible(true);
            dialogChonNgay.dispose();
        });

        btnHuy.addActionListener(e -> dialogChonNgay.dispose());

        panelBtn.add(btnXem);
        panelBtn.add(btnHuy);
        dialogChonNgay.add(panelBtn, BorderLayout.SOUTH);

        dialogChonNgay.setVisible(true);
    }

    // ============================== CÁC HÀM HỖ TRỢ ==============================
    private JLabel createStatPanel(JPanel parent, int x, String title, String sub, String unit) {
        JPanel panel = new JPanel();
        panel.setBounds(x, 57, 281, 95);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setBounds(10, 11, 150, 21);
        panel.add(lblTitle);

        JLabel lblValue = new JLabel("0");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setBounds(10, 35, 200, 21);
        panel.add(lblValue);

        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSub.setBounds(218, 77, 63, 14);
        panel.add(lblSub);

        JLabel lblUnit = new JLabel(unit);
        lblUnit.setForeground(new Color(0, 0, 0, 150));
        lblUnit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUnit.setBounds(10, 65, 63, 14);
        panel.add(lblUnit);

        parent.add(panel);
        return lblValue;
    }

    private JButton createButton(String text, int x, int y, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBounds(x, y, 179, 49);
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        return btn;
    }

    private JTextField createTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBounds(x, y, 150, 20);
        return tf;
    }

    private void capNhatToanBo(int nam) {
        List<HoaDon> dsCaNam = HoaDonDAO.layDanhSachHoaDonTheoNam(nam);
        List<HoaDon> dsThanhToan = dsCaNam.stream()
                .filter(hd -> hd.getTrangThai().equalsIgnoreCase("Đã thanh toán"))
                .toList();

        Map<Integer, double[]> ketQua = dsThanhToan.isEmpty() ? new HashMap<>() : tinhToanTheoThang(dsThanhToan);
        this.mapHienTai = ketQua;

        capNhatThongKe(dsThanhToan);
        capNhatBieuDo(ketQua, nam);
        capNhatBang(ketQua);
    }

    private Map<Integer, double[]> tinhToanTheoThang(List<HoaDon> ds) {
        Map<Integer, double[]> map = new HashMap<>();
        for (int i = 1; i <= 12; i++) map.put(i, new double[]{0, 0});

        for (HoaDon hd : ds) {
            int thang = hd.getNgayLapHoaDon().getMonthValue();
            double[] v = map.get(thang);
            v[0]++;
            v[1] += hd.getTongTien();
        }
        return map;
    }

    private void capNhatThongKe(List<HoaDon> ds) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        double tongTien = ds.stream().mapToDouble(HoaDon::getTongTien).sum();
        int mangDi = (int) ds.stream().filter(hd -> hd.getKhachHang() == null).count();
        int taiBan = ds.size() - mangDi;

        lblDoanhThu_TongTien.setText(df.format(tongTien));
        lblHoaDonTaiBan_SoLuong.setText(String.valueOf(taiBan));
        lblHoaDonMangDi_SoLuong.setText(String.valueOf(mangDi));
        textFieldTongSoHoaDon.setText(String.valueOf(ds.size()));
        textFieldTongTienHoaDon.setText(df.format(tongTien) + " VNĐ");
    }

    private void capNhatBieuDo(Map<Integer, double[]> map, int nam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] tenThang = {"", "1", "2", "3", "4", "5", "6",
                             "7", "8", "9", "10", "11", "12"};

        for (int thang = 1; thang <= 12; thang++) {
            double[] v = map.getOrDefault(thang, new double[]{0, 0});
            double doanhThuTrieu = v[1] / 1_000_000.0;
            dataset.addValue(doanhThuTrieu, "Doanh thu", tenThang[thang]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "DOANH THU THEO THÁNG - NĂM " + nam,
                "Tháng", "Doanh thu (triệu VNĐ)", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

        ChartPanel chartPanel = new ChartPanel(chart);
        panelBieuDo.removeAll();
        panelBieuDo.add(chartPanel, BorderLayout.CENTER);
        panelBieuDo.revalidate();
        panelBieuDo.repaint();
    }

    private void capNhatBang(Map<Integer, double[]> map) {
        model.setRowCount(0);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        for (int thang = 1; thang <= 12; thang++) {
            double[] v = map.getOrDefault(thang, new double[]{0, 0});
            model.addRow(new Object[]{thang, (int) v[0], df.format(v[1]) + " VNĐ"});
        }
    }

    private void xuatExcel() {
        if (mapHienTai == null || mapHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất Excel!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser("note");
        String fileName = "ThongKe_HoaDon_Nam_" + namHienTai + ".xlsx";
        fc.setSelectedFile(new File(fileName));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Thong ke");
            XSSFFont titleFont = (XSSFFont) wb.createFont();
            titleFont.setFontName("Segoe UI Semibold"); titleFont.setFontHeightInPoints((short) 16); titleFont.setBold(true);

            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(titleFont); titleStyle.setAlignment(HorizontalAlignment.CENTER);

            Row row0 = sheet.createRow(0);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("BÁO CÁO DOANH THU NĂM " + namHienTai);
            cell0.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            Row header = sheet.createRow(2);
            String[] cols = {"Tháng", "Số HĐ", "Doanh thu"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
            }

            int r = 3;
            for (int thang = 1; thang <= 12; thang++) {
                double[] v = mapHienTai.getOrDefault(thang, new double[]{0, 0});
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(thang);
                row.createCell(1).setCellValue((int) v[0]);
                row.createCell(2).setCellValue(v[1]);
            }

            double tong = mapHienTai.values().stream().mapToDouble(v -> v[1]).sum();
            Row totalRow = sheet.createRow(r);
            totalRow.createCell(1).setCellValue("TỔNG CỘNG");
            totalRow.createCell(2).setCellValue(tong);

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                wb.write(out);
            }
            JOptionPane.showMessageDialog(this, "Xuất Excel thành công!\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xuất Excel: " + ex.getMessage());
        }
    }
}

// ============================== DIALOG CHỌN HÓA ĐƠN IN THEO NĂM ==============================
class DialogChonHoaDonInTheoNam extends JDialog {
    private List<HoaDon> danhSachHoaDon;
    private DefaultTableModel modelDialog;
    private JTable tableDialog;

    public DialogChonHoaDonInTheoNam(List<HoaDon> dsHD, int thang, int nam) {
        this.danhSachHoaDon = dsHD;
        setTitle("Chọn hóa đơn cần in - Tháng " + thang + "/" + nam);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel();
        panelTop.setBackground(new Color(52, 152, 219));
        JLabel lblTieuDe = new JLabel("CHỌN HÓA ĐƠN CẦN IN");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTieuDe.setForeground(Color.WHITE);
        panelTop.add(lblTieuDe);
        add(panelTop, BorderLayout.NORTH);

        String[] columns = {"Chọn", "Mã HĐ", "Ngày", "Thời gian", "Khách hàng", "Tổng tiền"};
        modelDialog = new DefaultTableModel(columns, 0) {
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : String.class; }
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
        };

        tableDialog = new JTable(modelDialog);
        tableDialog.setRowHeight(30);
        tableDialog.getColumnModel().getColumn(0).setMaxWidth(60);

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        for (HoaDon hd : dsHD) {
            String kh = hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "Mang đi";
            modelDialog.addRow(new Object[]{
                    false,
                    hd.getMaHoaDon(),
                    hd.getNgayLapHoaDon().format(dateFmt),
                    hd.getNgayLapHoaDon().format(timeFmt),
                    kh,
                    df.format(hd.getTongTien()) + " VNĐ"
            });
        }

        add(new JScrollPane(tableDialog), BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBottom.setBackground(Color.WHITE);

        JButton btnChonTatCa = new JButton("Chọn tất cả");
        btnChonTatCa.addActionListener(e -> {
            for (int i = 0; i < modelDialog.getRowCount(); i++) modelDialog.setValueAt(true, i, 0);
        });

        JButton btnBoChon = new JButton("Bỏ chọn tất cả");
        btnBoChon.addActionListener(e -> {
            for (int i = 0; i < modelDialog.getRowCount(); i++) modelDialog.setValueAt(false, i, 0);
        });

        JButton btnIn = new JButton("In hóa đơn đã chọn");
        btnIn.setBackground(new Color(231, 76, 60));
        btnIn.setForeground(Color.WHITE);
        btnIn.addActionListener(e -> inHoaDonDaChon());

        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());

        panelBottom.add(btnChonTatCa);
        panelBottom.add(btnBoChon);
        panelBottom.add(btnIn);
        panelBottom.add(btnDong);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void inHoaDonDaChon() {
        java.util.List<HoaDon> dsIn = new java.util.ArrayList<>();
        for (int i = 0; i < modelDialog.getRowCount(); i++) {
            if (Boolean.TRUE.equals(modelDialog.getValueAt(i, 0))) {
                dsIn.add(danhSachHoaDon.get(i));
            }
        }
        if (dsIn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một hóa đơn!");
            return;
        }
        dsIn.forEach(InHoaDonPDF::xuatHoaDon);
        JOptionPane.showMessageDialog(this, "Đã in thành công " + dsIn.size() + " hóa đơn!");
        dispose();
    }
}