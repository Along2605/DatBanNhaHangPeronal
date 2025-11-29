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

public class ManHinhThongKeHoaDonTheoNam extends JPanel {
    private static final long serialVersionUID = 1L;

    // === BIẾN GIAO DIỆN ===
    private JLabel lblDoanhThu_TongTien, lblHoaDonTaiBan_SoLuong, lblHoaDonMangDi_SoLuong;
    private JTextField textFieldTongSoHoaDon, textFieldTongTienHoaDon;
    private DefaultTableModel model;
    private JTable table;
    private JPanel panelBieuDo;
    private JComboBox<Integer> comboNam;

    // === BIẾN DỮ LIỆU HIỆN TẠI ===
    private Map<Integer, double[]> mapHienTai; // month (1-12) -> [số HĐ, tổng tiền]

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
        contentPane.add(panelLocNam);

        JLabel lblNam = new JLabel("Năm");
        lblNam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNam.setBounds(10, 10, 100, 21);
        panelLocNam.add(lblNam);

        comboNam = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = 2020; y <= currentYear + 1; y++) {
            comboNam.addItem(y);
        }
        comboNam.setSelectedItem(currentYear);
        comboNam.setBounds(10, 40, 100, 25);
        panelLocNam.add(comboNam);

        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThongKe.setBounds(150, 40, 100, 25);
        panelLocNam.add(btnThongKe);

        // === PANEL DOANH THU ===
        lblDoanhThu_TongTien = createStatPanel(contentPane, 373, "Doanh thu", "Theo năm", "Vnđ");

        // === PANEL HÓA ĐƠN TẠI BÀN ===
        lblHoaDonTaiBan_SoLuong = createStatPanel(contentPane, 678, "Hóa đơn tại bàn", "Theo năm", "đơn");

        // === PANEL HÓA ĐƠN MANG ĐI ===
        lblHoaDonMangDi_SoLuong = createStatPanel(contentPane, 983, "Hóa đơn mang đi", "Theo năm", "đơn");

        // === BIỂU ĐỒ ===
        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBounds(49, 174, 620, 357);
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

        // === BẢNG ===
        JPanel panelTable = new JPanel();
        panelTable.setBounds(688, 236, 631, 357);
        panelTable.setLayout(null);
        contentPane.add(panelTable);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 5, 611, 341);
        panelTable.add(scrollPane);

        String[] cols = {"Tháng", "Tổng số HĐ", "Tổng tiền HĐ"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        scrollPane.setViewportView(table);

        // === SỰ KIỆN ===
        btnThongKe.addActionListener(e -> {
            int nam = (int) comboNam.getSelectedItem();
            capNhatToanBo(nam);
        });

        // === KHỞI TẠO MẶC ĐỊNH ===
        capNhatToanBo(currentYear);
    }

    // === CẬP NHẬT TOÀN BỘ ===
    private void capNhatToanBo(int nam) {
        List<HoaDon> ds = HoaDonDAO.layDanhSachHoaDonTheoNam(nam);
        Map<Integer, double[]> ketQua = ds.isEmpty() ? new HashMap<>() : tinhToanTheoThang(ds);

        this.mapHienTai = ketQua;

        capNhatThongKe(ds, ketQua);
        capNhatBieuDo(ketQua, nam);
        capNhatBang(ketQua);
    }

 // === TÍNH TOÁN THEO 12 THÁNG TRONG NĂM ===
    private Map<Integer, double[]> tinhToanTheoThang(List<HoaDon> ds) {
        Map<Integer, double[]> map = new HashMap<>();
        
        // Khởi tạo 12 tháng với giá trị 0
        for (int i = 1; i <= 12; i++) {
            map.put(i, new double[]{0, 0});
        }

        // Cập nhật dữ liệu thực tế
        for (HoaDon hd : ds) {
            int month = hd.getNgayLapHoaDon().getMonthValue();
            double[] v = map.get(month);
            v[0]++; // số hóa đơn
            v[1] += hd.getTongTien(); // tổng tiền
        }
        return map;
    }

    // === CẬP NHẬT THỐNG KÊ ===
    private void capNhatThongKe(List<HoaDon> ds, Map<Integer, double[]> map) {
        DecimalFormat df = new DecimalFormat("#,###");
        double tongTien = ds.stream().mapToDouble(HoaDon::getTongTien).sum();
        int taiBan = (int) ds.stream().filter(hd -> hd.getBanAn() != null).count();
        int mangDi = ds.size() - taiBan;
      

        lblDoanhThu_TongTien.setText(df.format(tongTien));
        lblHoaDonTaiBan_SoLuong.setText(String.valueOf(taiBan));
        lblHoaDonMangDi_SoLuong.setText(String.valueOf(mangDi));
        textFieldTongSoHoaDon.setText(String.valueOf(ds.size()));
        textFieldTongTienHoaDon.setText(df.format(tongTien) + " VNĐ");
    }

 // === CẬP NHẬT BIỂU ĐỒ (12 CỘT THÁNG) ===
    private void capNhatBieuDo(Map<Integer, double[]> map, int nam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 1; i <= 12; i++) {
            double[] v = map.get(i);
            if (v == null) {
                v = new double[]{0, 0}; // Khởi tạo nếu null
            }
            dataset.addValue(v[1] / 1_000_000, "Doanh thu", i +"");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "DOANH THU THEO THÁNG TRONG NĂM " + nam,
                "Tháng", "Doanh thu (triệu VNĐ)",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(620, 343));
        panelBieuDo.removeAll();
        panelBieuDo.add(chartPanel, BorderLayout.CENTER);
        panelBieuDo.revalidate();
        panelBieuDo.repaint();
    }

 // === CẬP NHẬT BẢNG (12 DÒNG THÁNG) ===
    private void capNhatBang(Map<Integer, double[]> map) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for (int i = 1; i <= 12; i++) {
            double[] v = map.get(i);
            if (v == null) v = new double[]{0, 0}; // Bảo vệ
            model.addRow(new Object[]{
                i,  // Cột 0: Integer (1, 2, 3...)
                (int) v[0],
                df.format(v[1]) + " VNĐ"
            });
        }
    }

    // === XEM CHI TIẾT ===
 // === XEM CHI TIẾT ===
    private void xemChiTiet() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tháng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // LẤY GIÁ TRỊ CỘT 0 DƯỚI DẠNG Integer
        int thang = (int) model.getValueAt(row, 0);  // ← ĐÃ SỬA
        int nam = (int) comboNam.getSelectedItem();

        List<HoaDon> ds = HoaDonDAO.layDanhSachHoaDonTheoThang(thang, nam);
        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không có hóa đơn trong tháng " + thang + " năm " + nam + "!", 
                "Thông tin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new DialogChiTietHoaDonTheoThang(ds).setVisible(true);
    }

    // === XUẤT EXCEL ===
    private void xuatExcel() {
        if (mapHienTai == null || mapHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nam = (int) comboNam.getSelectedItem();
        JFileChooser fc = new JFileChooser("note");
        fc.setSelectedFile(new File("ThongKe_HoaDon_Nam_" + nam + ".xlsx"));

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Thong ke");

            // === FONTS & STYLES ===
            XSSFFont titleFont = (XSSFFont) wb.createFont();
            titleFont.setFontName("Segoe UI Semibold"); titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true); titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            XSSFFont headerFont = (XSSFFont) wb.createFont();
            headerFont.setBold(true); headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontName("Segoe UI"); headerFont.setFontHeightInPoints((short) 12);

            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(titleFont); titleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(headerFont); headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER); headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN); headerStyle.setBorderLeft(BorderStyle.THIN); headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER); dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN); dataStyle.setBorderLeft(BorderStyle.THIN); dataStyle.setBorderRight(BorderStyle.THIN);

            CellStyle moneyStyle = wb.createCellStyle();
            moneyStyle.cloneStyleFrom(dataStyle);
            moneyStyle.setDataFormat(wb.createDataFormat().getFormat("#,##0 \"VNĐ\""));
            moneyStyle.setAlignment(HorizontalAlignment.RIGHT);

            // === TIÊU ĐỀ ===
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(28);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO DOANH THU THEO THÁNG - NĂM " + nam);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            // === HEADER ===
            Row header = sheet.createRow(2);
            header.setHeightInPoints(22);
            String[] cols = {"Tháng", "Số HĐ", "Doanh thu"};
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            // === DỮ LIỆU ===
            int r = 3;
            for (int i = 1; i <= 12; i++) {
                double[] v = mapHienTai.get(i);
                Row row = sheet.createRow(r++);
                row.setHeightInPoints(20);
                row.createCell(0).setCellValue("Tháng " + i);
                row.createCell(1).setCellValue((int) v[0]);
                Cell cell = row.createCell(2);
                cell.setCellValue(v[1]);
                cell.setCellStyle(moneyStyle);
            }

            // === TỔNG CỘNG ===
            double tong = mapHienTai.values().stream().mapToDouble(v -> v[1]).sum();
            Row totalRow = sheet.createRow(r);
            totalRow.setHeightInPoints(22);
            Cell totalLabel = totalRow.createCell(1);
            totalLabel.setCellValue("TỔNG CỘNG");
            totalLabel.setCellStyle(headerStyle);
            Cell totalValue = totalRow.createCell(2);
            totalValue.setCellValue(tong);
            totalValue.setCellStyle(moneyStyle);

            // === ĐIỀU CHỈNH CỘT ===
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 400);
            }

            // === GHI FILE ===
            try (FileOutputStream out = new FileOutputStream(file)) {
                wb.write(out);
            }

            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thành công!\n" + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === HÀM TẠO PANEL THỐNG KÊ ===
    private JLabel createStatPanel(JPanel parent, int x, String title, String sub, String unit) {
        JPanel panel = new JPanel();
        panel.setBounds(x, 57, 281, 95);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setBounds(10, 11, 150, 21);
        panel.add(lblTitle);

        JLabel lblValue = new JLabel("0");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setBounds(10, 35, 180, 21);
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
}