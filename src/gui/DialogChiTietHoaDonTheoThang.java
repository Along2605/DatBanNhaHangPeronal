package gui;

import dao.HoaDonDAO;
import entity.HoaDon;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;

public class DialogChiTietHoaDonTheoThang extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private List<HoaDon> dsHoaDon;
    private int thang, nam;

    public DialogChiTietHoaDonTheoThang(List<HoaDon> ds) {
        this.dsHoaDon = ds;
        if (ds.isEmpty()) return;

        this.thang = ds.get(0).getNgayLapHoaDon().getMonthValue();
        this.nam = ds.get(0).getNgayLapHoaDon().getYear();

        setTitle("CHI TIẾT HÓA ĐƠN - THÁNG " + thang + "/" + nam);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("THÁNG " + thang + " NĂM " + nam);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setOpaque(false);


        JButton btnLoc = createIconButton("Lọc", "filter.png", new Color(241, 196, 15));
        btnLoc.addActionListener(e -> locDuLieu());
        btnPanel.add(btnLoc);

        JButton btnRefresh = createIconButton("Làm mới", "refresh.png", new Color(155, 89, 182));
        btnRefresh.addActionListener(e -> lamMoi());
        btnPanel.add(btnRefresh);

        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // === BẢNG DỮ LIỆU ===
        String[] cols = {"Mã HĐ", "Ngày lập", "Nhân viên", "Bàn", "Tổng tiền", "Trạng thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(scroll, BorderLayout.CENTER);

        // === FOOTER - TỔNG HỢP ===
        JPanel footer = new JPanel(new GridLayout(1, 4, 15, 0));
        footer.setBorder(new EmptyBorder(10, 15, 15, 15));
        footer.setBackground(new Color(236, 240, 241));

        JLabel lblTongHD = createFooterLabel("Tổng HĐ: 0", new Color(52, 152, 219));
        JLabel lblTongTien = createFooterLabel("Doanh thu: 0 VNĐ", new Color(46, 204, 113));
        JLabel lblHuy = createFooterLabel("Hủy: 0", new Color(231, 76, 60));

        footer.add(lblTongHD);
        footer.add(lblTongTien);
        footer.add(lblHuy);
        add(footer, BorderLayout.SOUTH);

        // === TẢI DỮ LIỆU ===
        taiDuLieu(ds);

        // === CẬP NHẬT FOOTER ===
        capNhatFooter(lblTongHD, lblTongTien, ds);
    }

    // === TẢI DỮ LIỆU VÀO BẢNG ===
    private void taiDuLieu(List<HoaDon> ds) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,##0");
        for (HoaDon hd : ds) {
            model.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getNgayLapHoaDon().toLocalDate(),
                hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "—",
                hd.getBanAn() != null ? hd.getBanAn().getMaBan() : "Mang đi",
                df.format(hd.getTongTien()) + " VNĐ",
                hd.getTrangThai()
            });
        }
    }

    // === CẬP NHẬT FOOTER ===
    private void capNhatFooter(JLabel tongHD, JLabel tongTien, List<HoaDon> ds) {
        int soHD = ds.size();
        double tong = ds.stream().mapToDouble(HoaDon::getTongTien).sum();
        int soHuy = (int) ds.stream().filter(hd -> "Hủy".equals(hd.getTrangThai())).count();
        double tb = soHD > 0 ? tong / soHD : 0;

        DecimalFormat df = new DecimalFormat("#,##0");
        tongHD.setText("Tổng HĐ: " + soHD);
        tongTien.setText("Doanh thu: " + df.format(tong) + " VNĐ");
    }

    // === XUẤT EXCEL ===
    private void xuatExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("ChiTiet_Thang" + thang + "_Nam" + nam + ".xlsx"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx")) file = new File(file.getAbsolutePath() + ".xlsx");

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("HoaDon");

            // Tiêu đề
            Row title = sheet.createRow(0);
            title.createCell(0).setCellValue("CHI TIẾT HÓA ĐƠN THÁNG " + thang + "/" + nam);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            // Header
            Row header = sheet.createRow(2);
            String[] cols = {"Mã HĐ", "Ngày lập", "Nhân viên", "Bàn", "Tổng tiền", "Trạng thái"};
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                CellStyle style = wb.createCellStyle();
                style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                XSSFFont font = (XSSFFont) wb.createFont();
                font.setColor(IndexedColors.WHITE.getIndex());
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Dữ liệu
            int r = 3;
            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(r++);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                }
            }

            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                wb.write(out);
            }
            JOptionPane.showMessageDialog(this, "Xuất Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === IN HÓA ĐƠN (GIẢ LẬP) ===
    private void inHoaDon() {
        JOptionPane.showMessageDialog(this,
                "Đang gửi lệnh in đến máy in...\n" +
                "Tổng: " + dsHoaDon.size() + " hóa đơn\n" +
                "Doanh thu: " + new DecimalFormat("#,##0").format(
                        dsHoaDon.stream().mapToDouble(HoaDon::getTongTien).sum()) + " VNĐ",
                "In thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // === LỌC DỮ LIỆU ===
    private void locDuLieu() {
        String[] options = {"Tất cả", "Tại bàn", "Mang đi", "Hủy", "Hoàn thành"};
        JComboBox<String> combo = new JComboBox<>(options);
        JTextField txtTuKhoa = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Loại hóa đơn:"));
        panel.add(combo);
        panel.add(new JLabel("Từ khóa (Mã HĐ/NV):"));
        panel.add(txtTuKhoa);

        int result = JOptionPane.showConfirmDialog(this, panel, "Lọc hóa đơn", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String loai = (String) combo.getSelectedItem();
        String tuKhoa = txtTuKhoa.getText().trim().toLowerCase();

        List<HoaDon> filtered = dsHoaDon.stream()
                .filter(hd -> {
                    if (!tuKhoa.isEmpty()) who: {
                        if (hd.getMaHoaDon().toLowerCase().contains(tuKhoa)) break who;
                        if (hd.getNhanVien() != null && hd.getNhanVien().getMaNV().toLowerCase().contains(tuKhoa)) break who;
                        return false;
                    }

                    return switch (loai) {
                        case "Tại bàn" -> hd.getBanAn() != null;
                        case "Mang đi" -> hd.getBanAn() == null;
                        case "Hủy" -> "Hủy".equals(hd.getTrangThai());
                        case "Hoàn thành" -> "Hoàn thành".equals(hd.getTrangThai());
                        default -> true;
                    };
                })
                .toList();

        taiDuLieu(filtered);
        JOptionPane.showMessageDialog(this, "Đã lọc: " + filtered.size() + " hóa đơn", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
    }

    // === LÀM MỚI ===
    private void lamMoi() {
        dsHoaDon = HoaDonDAO.layDanhSachHoaDonTheoThang(thang, nam);
        taiDuLieu(dsHoaDon);
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // === TẠO NÚT CÓ ICON ===
    private JButton createIconButton(String text, String iconName, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tìm icon trong resources (hoặc dùng FontAwesome)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + iconName));
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btn.setText(text); // fallback
        }
        return btn;
    }

    // === TẠO LABEL FOOTER ===
    private JLabel createFooterLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(color);
        lbl.setBorder(BorderFactory.createLineBorder(color, 2, true));
        return lbl;
    }
}