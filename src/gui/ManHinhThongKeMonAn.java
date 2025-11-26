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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ManHinhThongKeMonAn extends JPanel {

    private JComboBox<String> comboTieuChi;
    private JComboBox<String> comboLoaiMon;
    private JPanel panelBieuDo;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel panelTop10Container;

    private String tieuChi = "Phổ biến nhất";
    private String loaiMonLoc = "Tất cả";

    private List<Map<String, Object>> duLieuThongKe = new ArrayList<>();
    private Map<String, String> tenMonToMaMon = new HashMap<>();

    public ManHinhThongKeMonAn() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        add(taoHeader(), BorderLayout.NORTH);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(245, 245, 245));
        main.setBorder(new EmptyBorder(20, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel left = taoPanelBieuDoVaBang();
        gbc.gridx = 0; gbc.weightx = 0.65; gbc.weighty = 1.0;
        main.add(left, gbc);

        panelTop10Container = taoPanelTop10();
        gbc.gridx = 1; gbc.weightx = 0.35;
        main.add(panelTop10Container, gbc);

        add(main, BorderLayout.CENTER);

        capNhatDuLieu();
    }

    private JPanel taoHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245, 245, 245));
        p.setBorder(new EmptyBorder(20, 30, 15, 30));

        JLabel title = new JLabel("THỐNG KÊ MÓN ĂN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 60, 100));
        p.add(title, BorderLayout.NORTH);

        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        control.setBackground(new Color(245, 245, 245));

        // Tiêu chí thống kê
        control.add(new JLabel("Thống kê theo:"));
        String[] tieuChiArr = {"Phổ biến nhất", "Doanh thu cao nhất", "Ít đặt nhất"};
        comboTieuChi = new JComboBox<>(tieuChiArr);
        comboTieuChi.setPreferredSize(new Dimension(230, 38));
        comboTieuChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        control.add(comboTieuChi);

        // Loại món ăn
        control.add(new JLabel("   Loại món:"));
        comboLoaiMon = new JComboBox<>();
        comboLoaiMon.addItem("Tất cả");
        try {
            MonAnDAO dao = new MonAnDAO();
            for (String tenLoai : dao.layRaLoaiMonAn()) {
                comboLoaiMon.addItem(tenLoai);
            }
        } catch (Exception e) {
            comboLoaiMon.addItem("Món chính");
            comboLoaiMon.addItem("Món ăn kèm");
            comboLoaiMon.addItem("Đồ uống");
        }
        comboLoaiMon.setPreferredSize(new Dimension(180, 38));
        comboLoaiMon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        control.add(comboLoaiMon);

        // Nút xuất báo cáo
        JButton btnXuat = new JButton("XUẤT BÁO CÁO");
        btnXuat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuat.setBackground(new Color(46, 204, 113));
        btnXuat.setForeground(Color.WHITE);
        btnXuat.setPreferredSize(new Dimension(160, 38));
        btnXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXuat.addActionListener(e -> xuatBaoCao());

        control.add(Box.createHorizontalGlue());
        control.add(btnXuat);

        p.add(control, BorderLayout.SOUTH);

        comboTieuChi.addActionListener(e -> {
            tieuChi = (String) comboTieuChi.getSelectedItem();
            capNhatDuLieu();
        });

        comboLoaiMon.addActionListener(e -> {
            loaiMonLoc = (String) comboLoaiMon.getSelectedItem();
            capNhatDuLieu();
        });

        return p;
    }

    private JPanel taoPanelBieuDoVaBang() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel title = new JLabel("TOP 3 MÓN ĂN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        title.setBorder(new EmptyBorder(15, 0, 0, 0));
        p.add(title, BorderLayout.NORTH);

        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBackground(Color.WHITE);
        p.add(panelBieuDo, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"STT", "TÊN MÓN ĂN", "SỐ LƯỢNG", "TỶ LỆ (%)"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(0, 150));
        p.add(scroll, BorderLayout.SOUTH);

        return p;
    }

    private JPanel taoPanelTop10() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel title = new JLabel("TOP 10 MÓN ĂN", SwingConstants.CENTER);
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

    // ==================== DỮ LIỆU + LỌC THEO LOẠI MÓN ====================
    private void capNhatDuLieu() {
        layDuLieuThongKeTuDB();
        capNhatBieuDo();
        capNhatBang();
        capNhatTop10();
    }

    private void layDuLieuThongKeTuDB() {
        duLieuThongKe.clear();
        tenMonToMaMon.clear();

        MonAnDAO monAnDAO = new MonAnDAO();
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();

        List<HoaDon> dsHoaDon = new HoaDonDAO().findAll();

        int tongSoLuong = 0;
        Map<String, Integer> soLuongMap = new HashMap<>();
        Map<String, Double> doanhThuMap = new HashMap<>();

        for (HoaDon hd : dsHoaDon) {
            List<ChiTietHoaDon> dsCT = cthdDAO.layDanhSachTheoHoaDon(hd.getMaHoaDon());
            for (ChiTietHoaDon ct : dsCT) {
                String maMon = ct.getMonAn().getMaMon();
                int sl = ct.getSoLuong();

                MonAn mon = monAnDAO.layMonAnTheoMa(maMon);
                if (mon == null) continue;

                // === LỌC THEO LOẠI MÓN ===
                if (!"Tất cả".equals(loaiMonLoc)) {
                    String tenLoaiCuaMon;
                    try {
                        tenLoaiCuaMon = monAnDAO.chuyenDoiMaLoaiSangTen(mon.getLoaiMon());
                    } catch (Exception e) { continue; }
                    if (!loaiMonLoc.equals(tenLoaiCuaMon)) continue;
                }

                double doanhThu = sl * mon.getGia();

                soLuongMap.put(maMon, soLuongMap.getOrDefault(maMon, 0) + sl);
                doanhThuMap.put(maMon, doanhThuMap.getOrDefault(maMon, 0.0) + doanhThu);
                tongSoLuong += sl;

                tenMonToMaMon.put(mon.getTenMon(), maMon);
            }
        }

        for (String maMon : soLuongMap.keySet()) {
            MonAn mon = monAnDAO.layMonAnTheoMa(maMon);
            if (mon == null) continue;

            int sl = soLuongMap.get(maMon);
            double dt = doanhThuMap.get(maMon);
            double tyLe = tongSoLuong > 0 ? (sl * 100.0 / tongSoLuong) : 0;

            Map<String, Object> item = new HashMap<>();
            item.put("tenMon", mon.getTenMon());
            item.put("maMon", maMon);
            item.put("soLuong", sl);
            item.put("doanhThu", dt);
            item.put("tyLe", tyLe);

            duLieuThongKe.add(item);
        }
    }

    private void capNhatBieuDo() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<Map<String, Object>> sorted = new ArrayList<>(duLieuThongKe);

        switch (tieuChi) {
            case "Phổ biến nhất" -> sorted.sort((a,b) -> Integer.compare((int)b.get("soLuong"), (int)a.get("soLuong")));
            case "Doanh thu cao nhất" -> sorted.sort((a,b) -> Double.compare((double)b.get("doanhThu"), (double)a.get("doanhThu")));
            case "Ít đặt nhất" -> sorted.sort((a,b) -> Integer.compare((int)a.get("soLuong"), (int)b.get("soLuong")));
        }

        int limit = Math.min(3, sorted.size());
        for (int i = 0; i < limit; i++) {
            Map<String, Object> m = sorted.get(i);
            String ten = (String) m.get("tenMon");
            if (ten.length() > 18) ten = ten.substring(0, 15) + "...";
            dataset.setValue(ten + " (" + m.get("soLuong") + ")", (double) m.get("tyLe"));
        }
        if (dataset.getItemCount() == 0) dataset.setValue("Chưa có dữ liệu", 100);

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("Segoe UI", Font.BOLD, 13));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\n{2}"));

        Color[] colors = {new Color(231,76,60), new Color(52,152,219), new Color(241,196,15)};
        int i = 0;
        for (Object k : dataset.getKeys()) {
            plot.setSectionPaint(k.toString(), colors[i++ % 3]);
        }

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);

        panelBieuDo.removeAll();
        panelBieuDo.add(cp, BorderLayout.CENTER);
        panelBieuDo.revalidate();
        panelBieuDo.repaint();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> sorted = new ArrayList<>(duLieuThongKe);
        switch (tieuChi) {
            case "Phổ biến nhất" -> sorted.sort((a,b) -> Integer.compare((int)b.get("soLuong"), (int)a.get("soLuong")));
            case "Doanh thu cao nhất" -> sorted.sort((a,b) -> Double.compare((double)b.get("doanhThu"), (double)a.get("doanhThu")));
            case "Ít đặt nhất" -> sorted.sort((a,b) -> Integer.compare((int)a.get("soLuong"), (int)b.get("soLuong")));
        }

        int stt = 1;
        for (Map<String, Object> m : sorted) {
            tableModel.addRow(new Object[]{
                stt++,
                m.get("tenMon"),
                m.get("soLuong"),
                String.format("%.2f%%", m.get("tyLe"))
            });
        }
    }

    private void capNhatTop10() {
        JPanel container = (JPanel) ((JScrollPane) panelTop10Container.getComponent(1)).getViewport().getView();
        container.removeAll();

        List<Map<String, Object>> sorted = new ArrayList<>(duLieuThongKe);
        switch (tieuChi) {
            case "Phổ biến nhất" -> sorted.sort((a,b) -> Integer.compare((int)b.get("soLuong"), (int)a.get("soLuong")));
            case "Doanh thu cao nhất" -> sorted.sort((a,b) -> Double.compare((double)b.get("doanhThu"), (double)a.get("doanhThu")));
            case "Ít đặt nhất" -> sorted.sort((a,b) -> Integer.compare((int)a.get("soLuong"), (int)b.get("soLuong")));
        }

        int rank = 1;
        for (Map<String, Object> m : sorted) {
            if (rank > 10) break;
            container.add(taoItemTop10(rank++, m));
            container.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        container.revalidate();
        container.repaint();
    }

    private JPanel taoItemTop10(int rank, Map<String, Object> m) {
        JPanel p = new JPanel(new BorderLayout(15, 0));
        p.setPreferredSize(new Dimension(350, 88));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 88));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        // Ảnh thật
        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(70, 70));
        lblImg.setHorizontalAlignment(JLabel.CENTER);
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        String maMon = (String) m.get("maMon");
        MonAn mon = new MonAnDAO().layMonAnTheoMa(maMon);
        if (mon != null && mon.getHinhAnh() != null && !mon.getHinhAnh().trim().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(mon.getHinhAnh());
                Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                lblImg.setText("Lỗi");
            }
        } else {
            lblImg.setText("No image");
        }

        p.add(lblImg, BorderLayout.WEST);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 0, 5, 0);

        JLabel lblRank = new JLabel(rank + ".");
        lblRank.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblRank.setForeground(rank <= 3 ? new Color(255, 170, 0) : new Color(70, 70, 70));
        c.gridx = 0; c.gridy = 0;
        info.add(lblRank, c);

        JLabel lblTen = new JLabel((String) m.get("tenMon"));
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        c.gridx = 1; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        info.add(lblTen, c);

        JLabel lblInfo = new JLabel(m.get("soLuong") + " lượt • " + String.format("%.1f%%", m.get("tyLe")));
        lblInfo.setForeground(Color.GRAY);
        c.gridx = 1; c.gridy = 1;
        info.add(lblInfo, c);

        JButton btnXem = new JButton("Xem chi tiết");
        btnXem.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnXem.setPreferredSize(new Dimension(90, 26));
        btnXem.setBackground(new Color(0, 122, 255));
        btnXem.setForeground(Color.WHITE);
        btnXem.setFocusPainted(false);
        btnXem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXem.addActionListener(e -> hienThiChiTietMonAn(maMon));

        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.EAST;
        info.add(btnXem, c);

        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private void hienThiChiTietMonAn(String maMon) {
        MonAnDAO dao = new MonAnDAO();
        MonAn mon = dao.layMonAnTheoMa(maMon);
        if (mon != null) {
            new MonAnDetailDialog((Frame) SwingUtilities.getWindowAncestor(this), mon).setVisible(true);
        }
    }

    private void xuatBaoCao() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất báo cáo Excel sẽ được thêm sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}