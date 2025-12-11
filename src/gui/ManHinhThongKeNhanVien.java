package gui;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManHinhThongKeNhanVien extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JLabel lblTongNhanVien, lblNhanVienNangXuat, lblNhanVienThuDong;
    private JLabel lblPhanTramNangXuat, lblPhanTramThuDong;
    private DefaultTableModel modelTable;
    private JTable table;
    private JPanel panelBieuDo;
    private JComboBox<String> cboChucVu;
    private JComboBox<String> cboNgay, cboThang, cboNam;
    
    public ManHinhThongKeNhanVien() {
        setPreferredSize(new Dimension(1366, 768));
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(null);
        add(contentPane, BorderLayout.CENTER);
        
        // === TIÊU ĐỀ ===
        JLabel lblTieuDe = new JLabel("THỐNG KÊ NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 27));
        lblTieuDe.setBounds(460, 0, 470, 59);
        contentPane.add(lblTieuDe);
        
        // === PANEL LỌC ===
        JPanel panelLoc = new JPanel();
        panelLoc.setBounds(49, 57, 264, 138);
        panelLoc.setLayout(null);
        panelLoc.setBackground(new Color(220, 220, 220));
        panelLoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        contentPane.add(panelLoc);
        
        JLabel lblChucVu = new JLabel("Chức vụ");
        lblChucVu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblChucVu.setBounds(15, 15, 80, 25);
        panelLoc.add(lblChucVu);
        
        cboChucVu = new JComboBox<>(new String[]{"Tất cả", "Quản lý", "Phục vụ", "Thu ngân", "Bếp"});
        cboChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboChucVu.setBounds(100, 15, 145, 25);
        panelLoc.add(cboChucVu);
        
        JLabel lblNgay = new JLabel("Ngày");
        lblNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNgay.setBounds(15, 55, 50, 25);
        panelLoc.add(lblNgay);
        
        cboNgay = new JComboBox<>();
        cboNgay.addItem("Tất cả");
        for (int i = 1; i <= 31; i++) {
            cboNgay.addItem(String.valueOf(i));
        }
        cboNgay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboNgay.setBounds(15, 82, 65, 25);
        panelLoc.add(cboNgay);
        
        JLabel lblThang = new JLabel("Tháng");
        lblThang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblThang.setBounds(90, 55, 60, 25);
        panelLoc.add(lblThang);
        
        cboThang = new JComboBox<>();
        cboThang.addItem("Tất cả");
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(String.valueOf(i));
        }
        cboThang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboThang.setBounds(90, 82, 70, 25);
        panelLoc.add(cboThang);
        
        JLabel lblNam = new JLabel("Năm");
        lblNam.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNam.setBounds(170, 55, 50, 25);
        panelLoc.add(lblNam);
        
        cboNam = new JComboBox<>();
        cboNam.addItem("Tất cả");
        for (int i = 2020; i <= 2030; i++) {
            cboNam.addItem(String.valueOf(i));
        }
        cboNam.setSelectedItem(String.valueOf(LocalDate.now().getYear()));
        cboNam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboNam.setBounds(170, 82, 75, 25);
        panelLoc.add(cboNam);
        
        // === CÁC PANEL THỐNG KÊ ===
        createStatPanelWithIcon(contentPane, 355, "Tổng số nhân viên", "36", 
                                new Color(52, 152, 219), "img/employees/tong_nhan_vien.png");
        
        createStatPanelNangXuat(contentPane, 660);
        
        createStatPanelThuDong(contentPane, 965);
        
        // === BIỂU ĐỒ TOP 10 ===
        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBounds(49, 205, 545, 360);
        panelBieuDo.setBackground(new Color(220, 220, 220));
        panelBieuDo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        contentPane.add(panelBieuDo);
        
        JLabel lblTieuDeBieuDo = new JLabel("Top 10 nhân viên có", SwingConstants.CENTER);
        lblTieuDeBieuDo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTieuDeBieuDo.setBounds(0, 8, 545, 22);
        
        JLabel lblTieuDeBieuDo2 = new JLabel("lương cao nhất", SwingConstants.CENTER);
        lblTieuDeBieuDo2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTieuDeBieuDo2.setBounds(0, 30, 545, 22);
        
        JPanel headerPanel = new JPanel(null);
        headerPanel.setPreferredSize(new Dimension(545, 60));
        headerPanel.setBackground(new Color(220, 220, 220));
        headerPanel.add(lblTieuDeBieuDo);
        headerPanel.add(lblTieuDeBieuDo2);
        
        panelBieuDo.add(headerPanel, BorderLayout.NORTH);
        
        // === BẢNG CHI TIẾT ===
        JPanel panelTable = new JPanel();
        panelTable.setBounds(618, 205, 670, 360);
        panelTable.setLayout(null);
        panelTable.setBackground(new Color(220, 220, 220));
        panelTable.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        contentPane.add(panelTable);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 20, 650, 330);
        panelTable.add(scrollPane);
        
        String[] cols = {"Mã NV", "Họ tên", "Chức vụ", "Lương cơ bản", "Số giờ làm", "Số hóa đơn", "Tỉ lệ năng suất"};
        modelTable = new DefaultTableModel(cols, 0) {
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        table = new JTable(modelTable);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        scrollPane.setViewportView(table);
        
        // === NÚT XUẤT BÁO CÁO VÀ XEM THÔNG TIN ===
        JButton btnXuatBaoCao = new JButton("Xuất báo cáo");
        btnXuatBaoCao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatBaoCao.setBackground(new Color(46, 204, 113));
        btnXuatBaoCao.setForeground(Color.WHITE);
        btnXuatBaoCao.setBounds(795, 580, 150, 40);
        btnXuatBaoCao.setFocusPainted(false);
        btnXuatBaoCao.addActionListener(e -> xuatBaoCao());
        contentPane.add(btnXuatBaoCao);
        
        JButton btnXemThongTin = new JButton("Xem thông tin nhân viên");
        btnXemThongTin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXemThongTin.setBackground(new Color(200, 200, 200));
        btnXemThongTin.setBounds(1000, 580, 190, 40);
        btnXemThongTin.setFocusPainted(false);
        btnXemThongTin.addActionListener(e -> xemThongTinNhanVien());
        contentPane.add(btnXemThongTin);
        
        // Khởi tạo dữ liệu mẫu
        initDuLieuMau();
    }
    
    private void xemThongTinNhanVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên trong bảng!", 
                "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy thông tin nhân viên từ bảng
        String maNV = (String) modelTable.getValueAt(row, 0);
        String hoTen = (String) modelTable.getValueAt(row, 1);
        String chucVu = (String) modelTable.getValueAt(row, 2);
        String luongCoBan = (String) modelTable.getValueAt(row, 3);
        String soGioLam = (String) modelTable.getValueAt(row, 4);
        String soHoaDon = (String) modelTable.getValueAt(row, 5);
        String tiLeNangXuat = (String) modelTable.getValueAt(row, 6);
        
        // Hiển thị dialog
        new DialogThongTinNhanVien(maNV, hoTen, chucVu, luongCoBan, 
                                   soGioLam, soHoaDon, tiLeNangXuat).setVisible(true);
    }
    
    private void xuatBaoCao() {
        JFileChooser fc = new JFileChooser("note");
        String fileName = "BaoCao_NhanVien_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".xlsx";
        fc.setSelectedFile(new File(fileName));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        
        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }
        
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Bao cao nhan vien");
            
            // Font cho tiêu đề
            XSSFFont titleFont = (XSSFFont) wb.createFont();
            titleFont.setFontName("Segoe UI Semibold");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Tiêu đề
            Row row0 = sheet.createRow(0);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("BÁO CÁO THỐNG KÊ NHÂN VIÊN");
            cell0.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            
            // Thông tin tổng hợp
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("Ngày xuất báo cáo: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
            
            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("Tổng số nhân viên: " + lblTongNhanVien.getText());
            row2.createCell(3).setCellValue("Nhân viên năng suất: " + lblNhanVienNangXuat.getText() + " (" + lblPhanTramNangXuat.getText() + ")");
            
            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("Nhân viên thu động: " + lblNhanVienThuDong.getText() + " (" + lblPhanTramThuDong.getText() + ")");
            
            // Header bảng
            CellStyle headerStyle = wb.createCellStyle();
            XSSFFont headerFont = (XSSFFont) wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            Row header = sheet.createRow(5);
            String[] cols = {"Mã NV", "Họ tên", "Chức vụ", "Lương cơ bản", "Số giờ làm", "Số hóa đơn", "Tỉ lệ năng suất"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }
            
            // Dữ liệu từ bảng
            int r = 6;
            for (int i = 0; i < modelTable.getRowCount(); i++) {
                Row row = sheet.createRow(r++);
                for (int j = 0; j < modelTable.getColumnCount(); j++) {
                    Object value = modelTable.getValueAt(i, j);
                    row.createCell(j).setCellValue(value != null ? value.toString() : "");
                }
            }
            
            // Auto size columns
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }
            
            try (FileOutputStream out = new FileOutputStream(file)) {
                wb.write(out);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Xuất báo cáo thành công!\n" + file.getAbsolutePath(),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi xuất báo cáo: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createStatPanelWithIcon(JPanel parent, int x, String title, String value, Color color, String iconPath) {
        JPanel panel = new JPanel();
        panel.setBounds(x, 57, 281, 138);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        
        JLabel lblIcon = new JLabel();
        lblIcon.setBounds(15, 25, 70, 70);
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Load và hiển thị ảnh
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcon.setText("🖼");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 45));
        }
        panel.add(lblIcon);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setBounds(105, 35, 150, 25);
        panel.add(lblTitle);
        
        lblTongNhanVien = new JLabel(value);
        lblTongNhanVien.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTongNhanVien.setBounds(105, 65, 150, 35);
        panel.add(lblTongNhanVien);
        
        parent.add(panel);
    }
    
    private void createStatPanelNangXuat(JPanel parent, int x) {
        JPanel panel = new JPanel();
        panel.setBounds(x, 57, 281, 138);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        
        JLabel lblIcon = new JLabel();
        lblIcon.setBounds(15, 25, 70, 70);
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Load và hiển thị ảnh
        try {
            ImageIcon icon = new ImageIcon("img/employees/nhan_vien_nang_xuat.png");
            Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcon.setText("✓");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 45));
            lblIcon.setForeground(new Color(46, 204, 113));
        }
        panel.add(lblIcon);
        
        JLabel lblTitle1 = new JLabel("Nhân viên");
        lblTitle1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle1.setBounds(105, 25, 150, 25);
        panel.add(lblTitle1);
        
        JLabel lblTitle2 = new JLabel("năng suất");
        lblTitle2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle2.setBounds(105, 43, 150, 25);
        panel.add(lblTitle2);
        
        lblNhanVienNangXuat = new JLabel("18");
        lblNhanVienNangXuat.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNhanVienNangXuat.setBounds(105, 68, 80, 35);
        panel.add(lblNhanVienNangXuat);
        
        lblPhanTramNangXuat = new JLabel("50%");
        lblPhanTramNangXuat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPhanTramNangXuat.setBounds(210, 105, 60, 25);
        panel.add(lblPhanTramNangXuat);
        
        parent.add(panel);
    }
    
    private void createStatPanelThuDong(JPanel parent, int x) {
        JPanel panel = new JPanel();
        panel.setBounds(x, 57, 281, 138);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        
        JLabel lblIcon = new JLabel();
        lblIcon.setBounds(15, 25, 70, 70);
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Load và hiển thị ảnh
        try {
            ImageIcon icon = new ImageIcon("img/employees/nhan_vien_thu_dong.png");
            Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcon.setText("✗");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 45));
            lblIcon.setForeground(new Color(231, 76, 60));
        }
        panel.add(lblIcon);
        
        JLabel lblTitle1 = new JLabel("Nhân viên");
        lblTitle1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle1.setBounds(105, 25, 150, 25);
        panel.add(lblTitle1);
        
        JLabel lblTitle2 = new JLabel("thu động");
        lblTitle2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle2.setBounds(105, 43, 150, 25);
        panel.add(lblTitle2);
        
        lblNhanVienThuDong = new JLabel("18");
        lblNhanVienThuDong.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNhanVienThuDong.setBounds(105, 68, 80, 35);
        panel.add(lblNhanVienThuDong);
        
        lblPhanTramThuDong = new JLabel("50%");
        lblPhanTramThuDong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPhanTramThuDong.setBounds(210, 105, 60, 25);
        panel.add(lblPhanTramThuDong);
        
        parent.add(panel);
    }
    
    private void initDuLieuMau() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String[] nhanVien = {
            "Nguyễn Văn Trời", "Nguyễn Thị Trà", "Lê Văn An", "Trần Thị Bình",
            "Phạm Văn Cường", "Hoàng Thị Dung", "Đỗ Văn Em", "Ngô Thị Phượng",
            "Bùi Văn Giang", "Vũ Thị Hoa"
        };

        double[] luong = {
            15092000, 14500000, 13800000, 13200000,
            12900000, 12500000, 12000000, 11800000,
            11500000, 11200000
        };

        for (int i = 0; i < nhanVien.length; i++) {
            dataset.addValue(luong[i], "Lương", nhanVien[i]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
            null, null, null, dataset,
            PlotOrientation.HORIZONTAL, false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setRangeGridlinesVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(255, 100, 150));
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setItemMargin(0.25);

        // Giới hạn trục để cột ngắn đẹp
        plot.getRangeAxis().setRange(0, 18_000_000);
        plot.getRangeAxis().setVisible(false);

        // Định dạng tiền Việt Nam chuẩn
        java.text.DecimalFormat vnFormat = new java.text.DecimalFormat("#,##0 VNĐ");

        renderer.setBaseItemLabelGenerator(new CategoryItemLabelGenerator() {
            @Override
            public String generateLabel(CategoryDataset dataset, int row, int column) {
                Number value = dataset.getValue(row, column);
                return value == null ? "" : vnFormat.format(value.longValue());
            }
            @Override public String generateRowLabel(CategoryDataset dataset, int row) { return null; }
            @Override public String generateColumnLabel(CategoryDataset dataset, int column) { return null; }
        });

     // Thay thế phần code từ dòng "renderer.setBaseItemLabelsVisible(true);" 
     // đến hết phần ItemLabelPosition trong method initDuLieuMau()

     renderer.setBaseItemLabelsVisible(true);
     renderer.setBaseItemLabelFont(new Font("Segoe UI", Font.BOLD, 13));
     renderer.setBaseItemLabelPaint(Color.BLACK);

     // GIẢI PHÁP: Đặt label BÊN TRONG cột, căn giữa theo chiều dọc
     ItemLabelPosition insidePosition = new ItemLabelPosition(
         ItemLabelAnchor.CENTER,      // Neo ở giữa cột
         TextAnchor.CENTER_LEFT       // Chữ căn trái từ điểm neo (hiển thị bên trong cột)
     );

     // Áp dụng cho tất cả các cột
     renderer.setSeriesPositiveItemLabelPosition(0, insidePosition);

     chart.setBackgroundPaint(new Color(220, 220, 220));

   
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(new Color(220, 220, 220));
        panelBieuDo.add(chartPanel, BorderLayout.CENTER);

        // Bảng dữ liệu giữ nguyên
        String[][] duLieu = {
            {"NV001", "Nguyễn Văn A", "Phục vụ", "8,000,000", "160", "45", "85%"},
            {"NV002", "Trần Thị B", "Thu ngân", "9,000,000", "168", "120", "92%"},
            {"NV003", "Lê Văn C", "Bếp", "10,000,000", "155", "80", "78%"},
            {"NV004", "Phạm Thị D", "Phục vụ", "8,500,000", "170", "52", "88%"},
            {"NV005", "Hoàng Văn E", "Quản lý", "15,000,000", "175", "0", "95%"},
            {"NV006", "Đỗ Thị F", "Phục vụ", "8,200,000", "162", "48", "82%"},
            {"NV007", "Ngô Văn G", "Bếp", "10,500,000", "158", "75", "80%"},
            {"NV008", "Bùi Thị H", "Thu ngân", "9,200,000", "165", "115", "90%"},
            {"NV009", "Vũ Văn I", "Phục vụ", "8,300,000", "163", "50", "84%"},
            {"NV010", "Đinh Thị K", "Phục vụ", "8,100,000", "159", "46", "81%"}
        };

        for (String[] row : duLieu) {
            modelTable.addRow(row);
        }
    }
}

