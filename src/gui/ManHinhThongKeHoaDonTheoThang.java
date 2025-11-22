package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.HoaDonDAO;
import entity.HoaDon;
// === THƯ VIỆN APACHE POI ===
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ManHinhThongKeHoaDonTheoThang extends JPanel {
    private static final long serialVersionUID = 1L;

    // === CÁC BIẾN GIAO DIỆN ===
    private JTextField textFieldTongSoHoaDon;
    private JTextField textFieldTongTienHoaDon;
    private DefaultTableModel model;
    private JLabel lblDoanhThu_TongTien, lblHoaDonTaiBan_SoLuong, lblHoaDonMangDi_SoLuong;
    private JTable table;
    private JPanel panelBieuDo;
    private JComboBox<Integer> comboThang, comboNam;

    // === BIẾN DỮ LIỆU HIỆN TẠI ===
    private Map<LocalDate, double[]> mapHienTai; // Lưu dữ liệu đang hiển thị để xuất Excel

    public ManHinhThongKeHoaDonTheoThang() {
        setPreferredSize(new Dimension(1366, 768));
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(null);
        add(contentPane, BorderLayout.CENTER);

        // === TIÊU ĐỀ ===
        JLabel lblTieuDe = new JLabel("THỐNG KÊ HÓA ĐƠN THEO THÁNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 27));
        lblTieuDe.setBounds(460, 0, 470, 59);
        contentPane.add(lblTieuDe);

        // === PANEL LỌC THEO THÁNG ===
        JPanel panelLocThang = new JPanel();
        panelLocThang.setBounds(49, 57, 281, 95);
        panelLocThang.setLayout(null);
        contentPane.add(panelLocThang);

        JLabel lblThang_TieuDe = new JLabel("Tháng");
        lblThang_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblThang_TieuDe.setBounds(10, 10, 100, 21);
        panelLocThang.add(lblThang_TieuDe);

        JLabel lblNam_TieuDe = new JLabel("Năm");
        lblNam_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNam_TieuDe.setBounds(150, 10, 100, 21);
        panelLocThang.add(lblNam_TieuDe);

        // ComboBox tháng
        comboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) comboThang.addItem(i);
        comboThang.setBounds(10, 40, 100, 25);
        panelLocThang.add(comboThang);

        // ComboBox năm
        comboNam = new JComboBox<>();
        for (int y = 2020; y <= LocalDate.now().getYear() + 1; y++) comboNam.addItem(y);
        comboNam.setBounds(150, 40, 100, 25);
        panelLocThang.add(comboNam);

        // Nút Thống kê
        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.setBounds(10, 70, 100, 25);
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelLocThang.add(btnThongKe);

        // === PANEL DOANH THU ===
        JPanel panelDoanhThu = new JPanel();
        panelDoanhThu.setBounds(373, 57, 281, 95);
        panelDoanhThu.setLayout(null);
        contentPane.add(panelDoanhThu);

        JLabel lblDoanhThu_TieuDe = new JLabel("Doanh thu");
        lblDoanhThu_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoanhThu_TieuDe.setBounds(10, 11, 91, 21);
        panelDoanhThu.add(lblDoanhThu_TieuDe);

        lblDoanhThu_TongTien = new JLabel();
        lblDoanhThu_TongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblDoanhThu_TongTien.setBounds(10, 35, 150, 21);
        panelDoanhThu.add(lblDoanhThu_TongTien);

        JLabel lblDoanhThu_TheoThang = new JLabel("Theo tháng");
        lblDoanhThu_TheoThang.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDoanhThu_TheoThang.setBounds(218, 77, 63, 14);
        panelDoanhThu.add(lblDoanhThu_TheoThang);

        JLabel labelDoanhThu_VND = new JLabel("Vnđ");
        labelDoanhThu_VND.setForeground(new Color(0, 0, 0, 150));
        labelDoanhThu_VND.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelDoanhThu_VND.setBounds(10, 65, 46, 14);
        panelDoanhThu.add(labelDoanhThu_VND);

        // === PANEL HÓA ĐƠN TẠI BÀN ===
        JPanel panelHoaDonTaiBan = new JPanel();
        panelHoaDonTaiBan.setBounds(678, 57, 281, 95);
        panelHoaDonTaiBan.setLayout(null);
        contentPane.add(panelHoaDonTaiBan);

        JLabel lblHoaDonTaiBan_TieuDe = new JLabel("Hóa đơn tại bàn");
        lblHoaDonTaiBan_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHoaDonTaiBan_TieuDe.setBounds(10, 11, 123, 21);
        panelHoaDonTaiBan.add(lblHoaDonTaiBan_TieuDe);

        lblHoaDonTaiBan_SoLuong = new JLabel();
        lblHoaDonTaiBan_SoLuong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHoaDonTaiBan_SoLuong.setBounds(10, 35, 112, 21);
        panelHoaDonTaiBan.add(lblHoaDonTaiBan_SoLuong);

        JLabel lblTheoThang1 = new JLabel("Theo tháng");
        lblTheoThang1.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblTheoThang1.setBounds(218, 76, 63, 14);
        panelHoaDonTaiBan.add(lblTheoThang1);

        JLabel labelHoaDonTaiBan_Don = new JLabel("đơn");
        labelHoaDonTaiBan_Don.setForeground(new Color(0, 0, 0, 150));
        labelHoaDonTaiBan_Don.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelHoaDonTaiBan_Don.setBounds(10, 65, 63, 14);
        panelHoaDonTaiBan.add(labelHoaDonTaiBan_Don);

        // === PANEL HÓA ĐƠN MANG ĐI ===
        JPanel panelHoaDonMangDi = new JPanel();
        panelHoaDonMangDi.setBounds(983, 57, 281, 95);
        panelHoaDonMangDi.setLayout(null);
        contentPane.add(panelHoaDonMangDi);

        JLabel lblHoaDonMangDi_TieuDe = new JLabel("Hóa đơn mang đi");
        lblHoaDonMangDi_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHoaDonMangDi_TieuDe.setBounds(10, 11, 123, 21);
        panelHoaDonMangDi.add(lblHoaDonMangDi_TieuDe);

        lblHoaDonMangDi_SoLuong = new JLabel();
        lblHoaDonMangDi_SoLuong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHoaDonMangDi_SoLuong.setBounds(10, 35, 112, 21);
        panelHoaDonMangDi.add(lblHoaDonMangDi_SoLuong);

        JLabel lblTheoThang2 = new JLabel("Theo tháng");
        lblTheoThang2.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblTheoThang2.setBounds(218, 76, 63, 14);
        panelHoaDonMangDi.add(lblTheoThang2);

        JLabel labelHoaDonMangDi_Don = new JLabel("đơn");
        labelHoaDonMangDi_Don.setForeground(new Color(0, 0, 0, 150));
        labelHoaDonMangDi_Don.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelHoaDonMangDi_Don.setBounds(10, 65, 63, 14);
        panelHoaDonMangDi.add(labelHoaDonMangDi_Don);

        // === BIỂU ĐỒ ===
        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBounds(49, 174, 620, 357);
        contentPane.add(panelBieuDo);

        // === NÚT XEM CHI TIẾT ===
        JButton btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXemChiTiet.setBounds(129, 542, 179, 49);
        contentPane.add(btnXemChiTiet);

        // === NÚT XUẤT EXCEL ===
        JButton btnXuatFileExcel = new JButton("Xuất file Excel");
        btnXuatFileExcel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatFileExcel.setBackground(new Color(46, 204, 113));
        btnXuatFileExcel.setForeground(Color.WHITE);
        btnXuatFileExcel.setFocusPainted(false);
        btnXuatFileExcel.setBounds(373, 542, 179, 49);
        contentPane.add(btnXuatFileExcel);

        // === BẢNG THỐNG KÊ ===
        JLabel lblTongSoHoaDon = new JLabel("Tổng số hóa đơn");
        lblTongSoHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongSoHoaDon.setBounds(718, 174, 115, 20);
        contentPane.add(lblTongSoHoaDon);

        JLabel lblTongTienHoaDon = new JLabel("Tổng tiền hóa đơn");
        lblTongTienHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongTienHoaDon.setBounds(718, 205, 115, 20);
        contentPane.add(lblTongTienHoaDon);

        textFieldTongSoHoaDon = new JTextField();
        textFieldTongSoHoaDon.setEditable(false);
        textFieldTongSoHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textFieldTongSoHoaDon.setBounds(863, 174, 150, 20);
        contentPane.add(textFieldTongSoHoaDon);

        textFieldTongTienHoaDon = new JTextField();
        textFieldTongTienHoaDon.setEditable(false);
        textFieldTongTienHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textFieldTongTienHoaDon.setBounds(863, 205, 150, 20);
        contentPane.add(textFieldTongTienHoaDon);

        JPanel panelTable = new JPanel();
        panelTable.setBounds(688, 236, 631, 357);
        panelTable.setLayout(null);
        contentPane.add(panelTable);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 5, 611, 341);
        panelTable.add(scrollPane);

        String[] tableHeader = {"Ngày", "Tổng số HĐ", "Tổng tiền HĐ"};
        model = new DefaultTableModel(tableHeader, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollPane.setViewportView(table);

        // === SỰ KIỆN NÚT ===
        btnThongKe.addActionListener(e -> {
            int thang = (int) comboThang.getSelectedItem();
            int nam = (int) comboNam.getSelectedItem();
            capNhatToanBo(thang, nam);
        });

        btnXemChiTiet.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngày!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int day = (int) model.getValueAt(row, 0);
            int nam = (int) comboNam.getSelectedItem();
            int thang = (int) comboThang.getSelectedItem();
            LocalDate date = LocalDate.of(nam, thang, day);
            List<HoaDon> ds = HoaDonDAO.layDanhSachHoaDonTheoNgay(date);
            if (ds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có hóa đơn trong ngày " + day + "/" + thang + "!", "Thông tin", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            new DialogChiTietHoaDonTheoNgay(ds).setVisible(true);
        });

        btnXuatFileExcel.addActionListener(e -> {
            if (mapHienTai == null || mapHienTai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để xuất!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int thang = (int) comboThang.getSelectedItem();
            int nam = (int) comboNam.getSelectedItem();
            xuatFileExcel(mapHienTai, thang, nam);
        });

        // === KHỞI TẠO MẶC ĐỊNH ===
        comboThang.setSelectedItem(LocalDate.now().getMonthValue());
        comboNam.setSelectedItem(LocalDate.now().getYear());
        capNhatToanBo(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }

    // === CẬP NHẬT TOÀN BỘ: BIỂU ĐỒ + BẢNG + LABEL + DỮ LIỆU ===
    private void capNhatToanBo(int thang, int nam) {
        List<HoaDon> dsTheoThang = HoaDonDAO.layDanhSachHoaDonTheoThang(thang, nam);
        Map<LocalDate, double[]> mapTheoThang = tinhToanDuLieu(dsTheoThang);

        this.mapHienTai = mapTheoThang; // Lưu dữ liệu hiện tại

        capNhatDuLieu(dsTheoThang, mapTheoThang, thang, nam);
        capNhatBieuDo(mapTheoThang, thang, nam);
    }

    // === CẬP NHẬT BIỂU ĐỒ ===
    private void capNhatBieuDo(Map<LocalDate, double[]> map, int thang, int nam) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "BIỂU ĐỒ THỐNG KÊ DOANH THU THEO THÁNG " + thang + " NĂM " + nam,
                "Ngày", "Doanh thu (triệu VND)",
                taoDataset(map, nam, thang),
                PlotOrientation.VERTICAL, true, true, false
        );
        barChart.getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        barChart.getCategoryPlot().getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        panelBieuDo.removeAll();
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(620, 343));
        panelBieuDo.add(chartPanel, BorderLayout.CENTER);
        panelBieuDo.revalidate();
        panelBieuDo.repaint();
    }

    // === CẬP NHẬT BẢNG + LABEL ===
    private void capNhatDuLieu(List<HoaDon> ds, Map<LocalDate, double[]> map, int thang, int nam) {
        int tongHD = ds.size();
        double tongTien = ds.stream().mapToDouble(HoaDon::getTongTien).sum();
        int taiBan = (int) ds.stream().filter(hd -> hd.getBanAn() != null).count();
        int mangDi = tongHD - taiBan;
        DecimalFormat df = new DecimalFormat("#,###");

        lblDoanhThu_TongTien.setText(df.format(tongTien));
        lblHoaDonTaiBan_SoLuong.setText(String.valueOf(taiBan));
        lblHoaDonMangDi_SoLuong.setText(String.valueOf(mangDi));
        textFieldTongSoHoaDon.setText(String.valueOf(tongHD));
        textFieldTongTienHoaDon.setText(df.format(tongTien) + " VNĐ");

        model.setRowCount(0);
        int maxDay = LocalDate.of(nam, thang, 1).lengthOfMonth();
        for (int d = 1; d <= maxDay; d++) {
            LocalDate date = LocalDate.of(nam, thang, d);
            double[] v = map.getOrDefault(date, new double[]{0, 0});
            model.addRow(new Object[]{d, (int)v[0], df.format(v[1]) + " VNĐ"});
        }
    }

    // === TÍNH TOÁN DỮ LIỆU THEO NGÀY ===
    private Map<LocalDate, double[]> tinhToanDuLieu(List<HoaDon> danhSachHoaDon) {
        Map<LocalDate, double[]> map = new HashMap<>();
        for (HoaDon hd : danhSachHoaDon) {
            LocalDate date = hd.getNgayLapHoaDon().toLocalDate();
            map.putIfAbsent(date, new double[]{0, 0});
            double[] v = map.get(date);
            v[0]++; // Số hóa đơn
            v[1] += hd.getTongTien(); // Tổng tiền
        }
        return map;
    }

    // === TẠO DATASET CHO BIỂU ĐỒ ===
    private DefaultCategoryDataset taoDataset(Map<LocalDate, double[]> map, int nam, int thang) {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        int maxDay = LocalDate.of(nam, thang, 1).lengthOfMonth();
        for (int d = 1; d <= maxDay; d++) {
            LocalDate date = LocalDate.of(nam, thang, d);
            double v = map.getOrDefault(date, new double[]{0, 0})[1] / 1_000_000;
            data.addValue(v, "Doanh thu", String.valueOf(d));
        }
        return data;
    }

    // === XUẤT FILE EXCEL ===
    private void xuatFileExcel(Map<LocalDate, double[]> ketQuaTheoNgay, int thang, int nam) {
        JFileChooser fileChooser = new JFileChooser("note");
        fileChooser.setDialogTitle("Lưu file Excel");
        fileChooser.setSelectedFile(new File("ThongKe_HoaDon_Thang" + thang + "_Nam" + nam + ".xlsx"));

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
        	Sheet sheet = workbook.createSheet("Thong ke");

            // === FONT CHUNG ===
            XSSFFont titleFont = (XSSFFont) workbook.createFont();
            titleFont.setFontName("Segoe UI Semibold");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            XSSFFont headerFont = (XSSFFont) workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontName("Segoe UI");
            headerFont.setFontHeightInPoints((short) 12);

            XSSFFont dataFont = (XSSFFont) workbook.createFont();
            dataFont.setFontName("Segoe UI");
            dataFont.setFontHeightInPoints((short) 11);

            // === STYLE TIÊU ĐỀ ===
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // === STYLE HEADER ===
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // === STYLE DATA ===
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(dataFont);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // === STYLE DÒNG XEN KẼ ===
            CellStyle altDataStyle = workbook.createCellStyle();
            altDataStyle.cloneStyleFrom(dataStyle);
            altDataStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            altDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // === STYLE TIỀN TỆ ===
            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.cloneStyleFrom(dataStyle);
            DataFormat fmt = workbook.createDataFormat();
            moneyStyle.setDataFormat(fmt.getFormat("#,##0 \"VNĐ\""));
            moneyStyle.setAlignment(HorizontalAlignment.RIGHT);

            CellStyle altMoneyStyle = workbook.createCellStyle();
            altMoneyStyle.cloneStyleFrom(altDataStyle);
            altMoneyStyle.setDataFormat(fmt.getFormat("#,##0 \"VNĐ\""));
            altMoneyStyle.setAlignment(HorizontalAlignment.RIGHT);

            // === STYLE TỔNG CỘNG ===
            CellStyle totalStyle = workbook.createCellStyle();
            totalStyle.cloneStyleFrom(headerStyle);
            totalStyle.setFont(headerFont);
            totalStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // === TIÊU ĐỀ CHÍNH ===
            String titleText = "BÁO CÁO DOANH THU THÁNG " + thang + " NĂM " + nam;
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(28);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titleText);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            // === NGÀY TẠO BÁO CÁO ===
            Row infoRow = sheet.createRow(1);
            Cell infoCell = infoRow.createCell(0);
            infoCell.setCellValue("Ngày tạo báo cáo: " + LocalDate.now().toString());
            CellStyle infoStyle = workbook.createCellStyle();
            infoStyle.setFont(dataFont);
            infoStyle.setAlignment(HorizontalAlignment.LEFT);
            infoCell.setCellStyle(infoStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            // === HEADER ===
            Row header = sheet.createRow(3);
            header.setHeightInPoints(22);
            String[] cols = {"Ngày", "Số HĐ", "Doanh thu"};
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            // === DỮ LIỆU ===
            int r = 4;
            int maxDay = LocalDate.of(nam, thang, 1).lengthOfMonth();
            for (int day = 1; day <= maxDay; day++) {
                LocalDate date = LocalDate.of(nam, thang, day);
                double[] d = ketQuaTheoNgay.getOrDefault(date, new double[]{0, 0});
                Row row = sheet.createRow(r);
                row.setHeightInPoints(20);

                boolean isAlt = day % 2 == 0;

                Cell cellNgay = row.createCell(0);
                cellNgay.setCellValue(day);
                cellNgay.setCellStyle(isAlt ? altDataStyle : dataStyle);

                Cell cellSoHD = row.createCell(1);
                cellSoHD.setCellValue((int) d[0]);
                cellSoHD.setCellStyle(isAlt ? altDataStyle : dataStyle);

                Cell cellDoanhThu = row.createCell(2);
                cellDoanhThu.setCellValue(d[1]);
                cellDoanhThu.setCellStyle(isAlt ? altMoneyStyle : moneyStyle);

                r++;
            }

            // === TỔNG CỘNG ===
            double tongDoanhThu = ketQuaTheoNgay.values().stream()
                    .mapToDouble(v -> v[1])
                    .sum();
            Row totalRow = sheet.createRow(r);
            totalRow.setHeightInPoints(22);
            Cell totalLabel = totalRow.createCell(1);
            totalLabel.setCellValue("TỔNG CỘNG");
            totalLabel.setCellStyle(totalStyle);
            Cell totalValue = totalRow.createCell(2);
            totalValue.setCellValue(tongDoanhThu);
            totalValue.setCellStyle(moneyStyle);

            // === AUTO SIZE & CĂN LỀ ===
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 500);
            }

            sheet.setMargin(Sheet.LeftMargin, 0.5);
            sheet.setMargin(Sheet.RightMargin, 0.5);
            sheet.setMargin(Sheet.TopMargin, 0.75);
            sheet.setMargin(Sheet.BottomMargin, 0.75);

            // === GHI FILE ===
            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }

            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thành công!\n" + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}