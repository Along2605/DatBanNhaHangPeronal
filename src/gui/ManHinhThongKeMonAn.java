package gui;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.MonAnDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.MonAn;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ManHinhThongKeMonAn extends JPanel {

    private JComboBox<String> comboLoaiThoiGian;
    private JComboBox<String> comboGiaTriThoiGian;
    private JComboBox<String> comboPhoBien;
    private JComboBox<String> comboDoanhThu;
    private JComboBox<String> comboLoaiMon;

    private JPanel panelBieuDo;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel panelTop5Container;

    private String loaiThoiGian = "Ngày";
    private String giaTriThoiGian = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    private String loaiMonLoc = "Tất cả";

    private List<Map<String, Object>> duLieuThongKe = new ArrayList<>();
    private List<Map<String, Object>> top5MonAn = new ArrayList<>();

    // Trạng thái sắp xếp: true = theo số lượng, false = theo doanh thu
    private boolean sortTheoSoLuong = true;
    private boolean giamDan = true;

    public ManHinhThongKeMonAn() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        add(taoHeader(), BorderLayout.NORTH);

        JPanel main = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel left = taoPanelBieuDoVaBang();
        gbc.gridx = 0; gbc.weightx = 0.65; gbc.weighty = 1.0;
        main.add(left, gbc);

        panelTop5Container = taoPanelTop5();
        gbc.gridx = 1; gbc.weightx = 0.35;
        main.add(panelTop5Container, gbc);

        add(main, BorderLayout.CENTER);

        capNhatComboGiaTriThoiGian();
        capNhatDuLieu();
    }

    private JPanel taoHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245, 245, 245));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("THỐNG KÊ MÓN ĂN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(30, 60, 100));
        p.add(title, BorderLayout.NORTH);

        JPanel controlWrapper = new JPanel(new GridLayout(2, 1, 0, 12));
        controlWrapper.setBackground(new Color(245, 245, 245));

        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        line1.setBackground(new Color(245, 245, 245));
        line1.add(new JLabel("Thống kê theo:"));

        String[] loaiTG = {"Ngày", "Tháng", "Năm"};
        comboLoaiThoiGian = new JComboBox<>(loaiTG);
        comboLoaiThoiGian.setPreferredSize(new Dimension(120, 40));
        line1.add(comboLoaiThoiGian);

        comboGiaTriThoiGian = new JComboBox<>();
        comboGiaTriThoiGian.setPreferredSize(new Dimension(180, 40));
        line1.add(comboGiaTriThoiGian);

        line1.add(new JLabel("   Loại món:"));
        comboLoaiMon = new JComboBox<>();
        comboLoaiMon.addItem("Tất cả");
        try {
            for (String loai : new MonAnDAO().layRaLoaiMonAn()) {
                comboLoaiMon.addItem(loai);
            }
        } catch (Exception ex) {
            String[] fallback = {"Món chính", "Món ăn kèm", "Đồ uống"};
            for (String s : fallback) comboLoaiMon.addItem(s);
        }
        comboLoaiMon.setPreferredSize(new Dimension(180, 40));
        line1.add(comboLoaiMon);

        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        line2.setBackground(new Color(245, 245, 245));
        line2.add(new JLabel("Phổ biến:"));

        String[] pb = {"Phổ biến nhất", "Kém phổ biến", "-- Chọn --"};
        comboPhoBien = new JComboBox<>(pb);
        comboPhoBien.setSelectedIndex(2);
        comboPhoBien.setPreferredSize(new Dimension(180, 40));
        line2.add(comboPhoBien);

        line2.add(new JLabel("   Doanh thu:"));
        String[] dt = {"Doanh thu cao nhất", "Doanh thu thấp nhất", "-- Chọn --"};
        comboDoanhThu = new JComboBox<>(dt);
        comboDoanhThu.setSelectedIndex(2);
        comboDoanhThu.setPreferredSize(new Dimension(200, 40));
        line2.add(comboDoanhThu);

        JButton btnXuat = new JButton("XUẤT BÁO CÁO");
        btnXuat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXuat.setBackground(new Color(46, 204, 113));
        btnXuat.setForeground(Color.WHITE);
        btnXuat.setPreferredSize(new Dimension(180, 40));
        btnXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXuat.addActionListener(e -> xuatBaoCao());
        line2.add(Box.createHorizontalGlue());
        line2.add(btnXuat);

        controlWrapper.add(line1);
        controlWrapper.add(line2);
        p.add(controlWrapper, BorderLayout.SOUTH);

        // ================== SỰ KIỆN SẮP XẾP - QUAN TRỌNG NHẤT ==================
        comboPhoBien.addActionListener(e -> {
            int index = comboPhoBien.getSelectedIndex();
            if (index == 0 || index == 1) {
                sortTheoSoLuong = true;
                giamDan = (index == 0); // 0 = Phổ biến nhất → giảm dần
                comboDoanhThu.setSelectedIndex(2); // reset combo doanh thu
                capNhatDuLieu();
            }
        });

        comboDoanhThu.addActionListener(e -> {
            int index = comboDoanhThu.getSelectedIndex();
            if (index == 0 || index == 1) {
                sortTheoSoLuong = false;
                giamDan = (index == 0); // 0 = Doanh thu cao nhất → giảm dần
                comboPhoBien.setSelectedIndex(2); // reset combo phổ biến
                capNhatDuLieu();
            }
        });

        comboLoaiThoiGian.addActionListener(e -> {
            loaiThoiGian = (String) comboLoaiThoiGian.getSelectedItem();
            capNhatComboGiaTriThoiGian();
        });

        comboGiaTriThoiGian.addActionListener(e -> {
            if (comboGiaTriThoiGian.getSelectedItem() != null) {
                giaTriThoiGian = comboGiaTriThoiGian.getSelectedItem().toString();
                capNhatDuLieu();
            }
        });

        comboLoaiMon.addActionListener(e -> {
            loaiMonLoc = (String) comboLoaiMon.getSelectedItem();
            capNhatDuLieu();
        });

        return p;
    }

    private void capNhatComboGiaTriThoiGian() {
        comboGiaTriThoiGian.removeAllItems();
        LocalDate today = LocalDate.now();

        if ("Ngày".equals(loaiThoiGian)) {
            for (int i = 29; i >= 0; i--) {
                LocalDate d = today.minusDays(i);
                comboGiaTriThoiGian.addItem(d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        } else if ("Tháng".equals(loaiThoiGian)) {
            for (int i = 11; i >= 0; i--) {
                YearMonth ym = YearMonth.from(today).minusMonths(i);
                comboGiaTriThoiGian.addItem(ym.getMonthValue() + "/" + ym.getYear());
            }
        } else {
            int year = today.getYear();
            for (int y = year; y >= year - 9; y--) {
                comboGiaTriThoiGian.addItem(String.valueOf(y));
            }
        }
        comboGiaTriThoiGian.setSelectedIndex(0);
        giaTriThoiGian = comboGiaTriThoiGian.getSelectedItem().toString();
        capNhatDuLieu();
    }

    private JPanel taoPanelBieuDoVaBang() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel title = new JLabel("BIỂU ĐỒ TOP 5 MÓN ĂN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(52, 73, 94));
        title.setBorder(new EmptyBorder(15, 0, 5, 0));
        p.add(title, BorderLayout.NORTH);

        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBackground(Color.WHITE);
        p.add(panelBieuDo, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"STT", "TÊN MÓN ĂN", "GIÁ BÁN", "SỐ LƯỢNG BÁN", "DOANH THU", "TỶ LỆ (%)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer(); center.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer(); right.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setCellRenderer(right);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(right);
        table.getColumnModel().getColumn(5).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(0, 220));
        p.add(scroll, BorderLayout.SOUTH);
        return p;
    }

    private JPanel taoPanelTop5() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel title = new JLabel("DANH SÁCH TOP MÓN ĂN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        title.setBorder(new EmptyBorder(15, 0, 10, 0));
        p.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void layDuLieuThongKeTuDB() {
        duLieuThongKe.clear();
        top5MonAn.clear();

        MonAnDAO monAnDAO = new MonAnDAO();
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
        HoaDonDAO hoaDonDAO = new HoaDonDAO();

        List<HoaDon> dsHoaDon = hoaDonDAO.timHoaDonTheoTrangThai("Đã thanh toán");
        if (dsHoaDon.isEmpty()) return;

        Map<String, Integer> slMap = new HashMap<>();
        Map<String, Double> dtMap = new HashMap<>();

        LocalDate start = null, end = null;
        if ("Ngày".equals(loaiThoiGian)) {
            LocalDate d = LocalDate.parse(giaTriThoiGian, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            start = d; end = d.plusDays(1);
        } else if ("Tháng".equals(loaiThoiGian)) {
            String[] p = giaTriThoiGian.split("/");
            int m = Integer.parseInt(p[0]), y = Integer.parseInt(p[1]);
            start = LocalDate.of(y, m, 1);
            end = YearMonth.of(y, m).atEndOfMonth().plusDays(1);
        } else {
            int y = Integer.parseInt(giaTriThoiGian);
            start = LocalDate.of(y, 1, 1);
            end = LocalDate.of(y + 1, 1, 1);
        }

        for (HoaDon hd : dsHoaDon) {
            if (hd.getNgayLapHoaDon() == null) continue;
            LocalDate ngay = hd.getNgayLapHoaDon().toLocalDate();
            if (start != null && (ngay.isBefore(start) || !ngay.isBefore(end))) continue;

            for (ChiTietHoaDon ct : cthdDAO.layDanhSachTheoHoaDon(hd.getMaHoaDon())) {
                String maMon = ct.getMonAn().getMaMon();
                MonAn mon = monAnDAO.layMonAnTheoMa(maMon);
                if (mon == null) continue;

                if (!"Tất cả".equals(loaiMonLoc)) {
                    String tenLoai = monAnDAO.chuyenDoiMaLoaiSangTen(mon.getLoaiMon());
                    if (!loaiMonLoc.equals(tenLoai)) continue;
                }

                int sl = ct.getSoLuong();
                double doanhThu = sl * mon.getGia();
                slMap.merge(maMon, sl, Integer::sum);
                dtMap.merge(maMon, doanhThu, Double::sum);
            }
        }

        int tongSL = slMap.values().stream().mapToInt(Integer::intValue).sum();
        for (String maMon : slMap.keySet()) {
            MonAn mon = monAnDAO.layMonAnTheoMa(maMon);
            if (mon == null) continue;

            int sl = slMap.get(maMon);
            double dt = dtMap.getOrDefault(maMon, 0.0);
            double tyLe = tongSL > 0 ? sl * 100.0 / tongSL : 0;

            Map<String, Object> item = new HashMap<>();
            item.put("maMon", maMon);
            item.put("tenMon", mon.getTenMon());
            item.put("giaBan", mon.getGia());
            item.put("soLuong", sl);
            item.put("doanhThu", dt);
            item.put("tyLe", tyLe);
            duLieuThongKe.add(item);
        }
    }

    private void capNhatDuLieu() {
        layDuLieuThongKeTuDB();
        capNhatBieuDo();
        capNhatBang();
    }

    private void capNhatBieuDo() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        top5MonAn.clear();

        if (duLieuThongKe.isEmpty()) {
            dataset.setValue("Chưa có dữ liệu", 100);
        } else {
            List<Map<String, Object>> sorted = new ArrayList<>(duLieuThongKe);
            sapXep(sorted);

            int limit = Math.min(5, sorted.size());
            for (int i = 0; i < limit; i++) {
                Map<String, Object> m = sorted.get(i);
                String ten = (String) m.get("tenMon");
                if (ten.length() > 20) ten = ten.substring(0, 17) + "...";
                int sl = (int) m.get("soLuong");
                dataset.setValue(ten + " (" + sl + " lượt)", (double) m.get("tyLe"));
                top5MonAn.add(m);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("Segoe UI", Font.BOLD, 13));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\n{2}"));

        Color[] colors = {new Color(52,152,219), new Color(231,76,60), new Color(241,196,15), new Color(46,204,113), new Color(155,89,182)};
        int i = 0;
        for (Object k : dataset.getKeys()) {
            plot.setSectionPaint(k.toString(), colors[i++ % 5]);
        }

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        panelBieuDo.removeAll();
        panelBieuDo.add(cp, BorderLayout.CENTER);
        panelBieuDo.revalidate();
        panelBieuDo.repaint();

        capNhatPanelTop5();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> sorted = new ArrayList<>(duLieuThongKe);
        sapXep(sorted);

        int stt = 1;
        for (Map<String, Object> m : sorted) {
            tableModel.addRow(new Object[]{
                    stt++,
                    m.get("tenMon"),
                    String.format("%,.0f đ", m.get("giaBan")),
                    m.get("soLuong"),
                    String.format("%,.0f đ", m.get("doanhThu")),
                    String.format("%.2f%%", m.get("tyLe"))
            });
        }
    }

    private void sapXep(List<Map<String, Object>> list) {
        list.sort((a, b) -> {
            int cmp = sortTheoSoLuong
                    ? Integer.compare((Integer) a.get("soLuong"), (Integer) b.get("soLuong"))
                    : Double.compare((Double) a.get("doanhThu"), (Double) b.get("doanhThu"));
            return giamDan ? -cmp : cmp;
        });
    }

    private void capNhatPanelTop5() {
        JPanel container = (JPanel) ((JScrollPane) panelTop5Container.getComponent(1)).getViewport().getView();
        container.removeAll();

        if (top5MonAn.isEmpty()) {
            JLabel lbl = new JLabel("Không có dữ liệu", SwingConstants.CENTER);
            lbl.setForeground(Color.GRAY);
            lbl.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            container.add(lbl);
        } else {
            int rank = 1;
            for (Map<String, Object> m : top5MonAn) {
                container.add(taoItemTop5(rank++, m));
                container.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        container.revalidate();
        container.repaint();
    }

    private JPanel taoItemTop5(int rank, Map<String, Object> m) {
        JPanel p = new JPanel(new BorderLayout(15, 0));
        p.setPreferredSize(new Dimension(350, 100));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(80, 80));
        lblImg.setHorizontalAlignment(JLabel.CENTER);
        lblImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        lblImg.setText("No image");

        String maMon = (String) m.get("maMon");
        MonAn mon = new MonAnDAO().layMonAnTheoMa(maMon);
        if (mon != null && mon.getHinhAnh() != null && !mon.getHinhAnh().trim().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(mon.getHinhAnh());
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(img));
                lblImg.setText("");
            } catch (Exception ignored) {}
        }
        p.add(lblImg, BorderLayout.WEST);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 0, 5, 0);
        c.anchor = GridBagConstraints.WEST;

        JLabel lblRank = new JLabel(rank + "");
        lblRank.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblRank.setForeground(new Color(255, 170, 0));
        c.gridx = 0; c.gridy = 0;
        info.add(lblRank, c);

        JLabel lblTen = new JLabel((String) m.get("tenMon"));
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        c.gridx = 1; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        info.add(lblTen, c);

        JLabel lblInfo = new JLabel(String.format("%,.0f đ • %,d lượt • %.1f%%",
                m.get("doanhThu"), m.get("soLuong"), m.get("tyLe")));
        lblInfo.setForeground(Color.GRAY);
        c.gridx = 1; c.gridy = 1;
        info.add(lblInfo, c);

        JButton btnXem = new JButton("Xem chi tiết");
        btnXem.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnXem.setPreferredSize(new Dimension(100, 28));
        btnXem.setBackground(new Color(0, 122, 255));
        btnXem.setForeground(Color.WHITE);
        btnXem.setFocusPainted(false);
        btnXem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXem.addActionListener(e -> hienThiChiTietMonAn(maMon));
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.EAST;
        info.add(btnXem, c);

        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private void hienThiChiTietMonAn(String maMon) {
        MonAn mon = new MonAnDAO().layMonAnTheoMa(maMon);
        if (mon != null) {
            new MonAnDetailDialog((Frame) SwingUtilities.getWindowAncestor(this), mon).setVisible(true);
        }
    }

    private void xuatBaoCao() {
        if (duLieuThongKe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất báo cáo!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser("note");
        chooser.setDialogTitle("Lưu báo cáo Excel");
        chooser.setFileFilter(new FileNameExtensionFilter("Excel Workbook (*.xlsx)", "xlsx"));
        chooser.setSelectedFile(new java.io.File("ThongKe_MonAn_" + giaTriThoiGian.replace("/", "-") + ".xlsx"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".xlsx")) {
            file = new java.io.File(file.getAbsolutePath() + ".xlsx");
        }

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Thống kê món ăn");

            CellStyle headerStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle titleStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle currencyStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0 đ"));

            CellStyle percentStyle = wb.createCellStyle();
            percentStyle.setDataFormat(format.getFormat("0.00%"));

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO THỐNG KÊ MÓN ĂN");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Thời gian: " + loaiThoiGian + " " + giaTriThoiGian + " | Loại món: " + loaiMonLoc);

            Row header = sheet.createRow(3);
            String[] columns = {"STT", "TÊN MÓN ĂN", "GIÁ BÁN", "SỐ LƯỢT BÁN", "DOANH THU", "TỶ LỆ (%)"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 4;
            int stt = 1;
            for (Map<String, Object> m : duLieuThongKe) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stt++);
                row.createCell(1).setCellValue((String) m.get("tenMon"));

                Cell cellGia = row.createCell(2);
                cellGia.setCellValue((Double) m.get("giaBan"));
                cellGia.setCellStyle(currencyStyle);

                row.createCell(3).setCellValue((Integer) m.get("soLuong"));

                Cell cellDT = row.createCell(4);
                cellDT.setCellValue((Double) m.get("doanhThu"));
                cellDT.setCellStyle(currencyStyle);

                Cell cellTL = row.createCell(5);
                cellTL.setCellValue((Double) m.get("tyLe") / 100.0);
                cellTL.setCellStyle(percentStyle);
            }

            Row tongRow = sheet.createRow(rowNum++);
            tongRow.createCell(2).setCellValue("TỔNG CỘNG:");
            tongRow.createCell(3).setCellValue(duLieuThongKe.stream().mapToInt(m -> (Integer)m.get("soLuong")).sum());
            Cell tongDT = tongRow.createCell(4);
            tongDT.setCellValue(duLieuThongKe.stream().mapToDouble(m -> (Double)m.get("doanhThu")).sum());
            tongDT.setCellStyle(currencyStyle);

            for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }

            JOptionPane.showMessageDialog(this,
                    "Xuất báo cáo thành công!\nĐã lưu tại: " + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}