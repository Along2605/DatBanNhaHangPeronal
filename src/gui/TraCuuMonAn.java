package gui;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.MonAnDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.MonAn;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraCuuMonAn extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel panelCenterCenter;
    private MonAnDAO monAnDAO = new MonAnDAO();
    private ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();

    private DecimalFormat df = new DecimalFormat("#,##0 đ");

    private List<MonAn> danhSachMonAnHienTai;
    private List<MonAn> listGoc;

    // Lưu số lượng bán của từng món (tính từ ChiTietHoaDon)
    private Map<String, Integer> soLuongBanMap = new HashMap<>();

    public TraCuuMonAn() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) (screenSize.getHeight() - 75));
        setLayout(null);

        JLabel lblTieuDe = new JLabel("Danh sách các món ăn đang kinh doanh");
        lblTieuDe.setBounds(443, 35, 455, 32);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTieuDe);

        JPanel panelWest = new JPanel();
        panelWest.setBounds(10, 108, 246, 517);
        add(panelWest);
        panelWest.setLayout(null);

        JButton btnTatCaDanhMuc = new JButton("Tất cả danh mục");
        btnTatCaDanhMuc.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnTatCaDanhMuc.setBounds(39, 11, 170, 34);
        btnTatCaDanhMuc.addActionListener(e -> locTatCaDanhMuc());
        panelWest.add(btnTatCaDanhMuc);

        JButton btnMonAnChinh = new JButton("Món ăn chính");
        btnMonAnChinh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMonAnChinh.setBounds(77, 49, 129, 30);
        btnMonAnChinh.addActionListener(e -> locTheoMonAnChinh());
        panelWest.add(btnMonAnChinh);

        JButton btnTokbokki = new JButton("Tokbokki");
        btnTokbokki.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnTokbokki.setBounds(117, 82, 89, 23);
        btnTokbokki.addActionListener(e -> locTheoTuKhoa("Tokbokki"));
        panelWest.add(btnTokbokki);

        JButton btnCom = new JButton("Cơm");
        btnCom.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnCom.setBounds(117, 106, 89, 23);
        btnCom.addActionListener(e -> locTheoTuKhoa("Cơm"));
        panelWest.add(btnCom);

        JButton btnLau = new JButton("Lẩu");
        btnLau.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLau.setBounds(117, 132, 89, 23);
        btnLau.addActionListener(e -> locTheoTuKhoa("Lẩu"));
        panelWest.add(btnLau);

        JButton btnMì = new JButton("Mì");
        btnMì.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnMì.setBounds(117, 156, 89, 23);
        btnMì.addActionListener(e -> locTheoTuKhoa("Mì"));
        panelWest.add(btnMì);

        JButton btnKhac = new JButton("Khác");
        btnKhac.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnKhac.setBounds(117, 180, 89, 23);
        btnKhac.addActionListener(e -> locMonKhac("MC", List.of("Tokbokki", "Cơm", "Lẩu", "Mì")));
        panelWest.add(btnKhac);

        JButton btnDoUong = new JButton("Đồ uống");
        btnDoUong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDoUong.setBounds(77, 205, 129, 30);
        btnDoUong.addActionListener(e -> locTheoDoUong());
        panelWest.add(btnDoUong);

        JButton btnNuocSuoi = new JButton("Nước suối");
        btnNuocSuoi.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnNuocSuoi.setBounds(117, 237, 89, 23);
        btnNuocSuoi.addActionListener(e -> locTheoTuKhoa("Nước suối"));
        panelWest.add(btnNuocSuoi);

        JButton btnTraSua = new JButton("Trà sữa");
        btnTraSua.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnTraSua.setBounds(117, 262, 89, 23);
        btnTraSua.addActionListener(e -> locTheoTuKhoa("Trà sữa"));
        panelWest.add(btnTraSua);

        JButton btnRuou = new JButton("Rượu");
        btnRuou.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRuou.setBounds(117, 288, 89, 23);
        btnRuou.addActionListener(e -> locTheoTuKhoa("Rượu"));
        panelWest.add(btnRuou);

        JButton btnKhac_1 = new JButton("Khác");
        btnKhac_1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnKhac_1.setBounds(117, 317, 89, 23);
        btnKhac_1.addActionListener(e -> locMonKhac("DO", List.of("Nước suối", "Trà sữa", "Rượu")));
        panelWest.add(btnKhac_1);

        JButton btnMonAnKem = new JButton("Món ăn kèm");
        btnMonAnKem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMonAnKem.setBounds(77, 345, 129, 30);
        btnMonAnKem.addActionListener(e -> locTheoMonAnKem());
        panelWest.add(btnMonAnKem);

        JButton btnKimbab = new JButton("Kimbab");
        btnKimbab.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnKimbab.setBounds(117, 380, 89, 23);
        btnKimbab.addActionListener(e -> locTheoTuKhoa("Kimbab"));
        panelWest.add(btnKimbab);

        JButton btnMandu = new JButton("Mandu");
        btnMandu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnMandu.setBounds(117, 403, 89, 23);
        btnMandu.addActionListener(e -> locTheoTuKhoa("Mandu"));
        panelWest.add(btnMandu);

        JButton btnSalad = new JButton("Salad");
        btnSalad.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnSalad.setBounds(117, 427, 89, 23);
        btnSalad.addActionListener(e -> locTheoTuKhoa("Salad"));
        panelWest.add(btnSalad);

        JButton btnKhac_1_1 = new JButton("Khác");
        btnKhac_1_1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnKhac_1_1.setBounds(117, 453, 89, 23);
        btnKhac_1_1.addActionListener(e -> locMonKhac("MK", List.of("Kimbab", "Mandu", "Salad")));
        panelWest.add(btnKhac_1_1);

        JPanel panelCenter = new JPanel();
        panelCenter.setBounds(266, 78, 1100, 457);
        add(panelCenter);
        panelCenter.setLayout(null);

        JPanel panelCenterNorth = new JPanel();
        panelCenterNorth.setBounds(28, 11, 1044, 96);
        panelCenter.add(panelCenterNorth);
        panelCenterNorth.setLayout(null);

        JLabel lblSapXepTheo = new JLabel("Sắp xếp theo");
        lblSapXepTheo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSapXepTheo.setBounds(38, 34, 113, 22);
        panelCenterNorth.add(lblSapXepTheo);

        JButton btnPhoBien = new JButton("Phổ biến");
        btnPhoBien.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnPhoBien.setBounds(147, 25, 108, 43);
        btnPhoBien.addActionListener(e -> sapXepTheoDoPhoBien()); // ĐÃ SỬA CHUẨN
        panelCenterNorth.add(btnPhoBien);

        JButton btnMoiNhat = new JButton("Mới nhất");
        btnMoiNhat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnMoiNhat.setBounds(276, 26, 108, 43);
        btnMoiNhat.addActionListener(e -> sapXepTheoMaMonGiamDan());
        panelCenterNorth.add(btnMoiNhat);

        String comboBoxGiaData[] = { "Giá", "Giá: Thấp đến cao", "Giá: Cao đến thấp" };
        JComboBox<String> comboBoxGia = new JComboBox<>(comboBoxGiaData);
        comboBoxGia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBoxGia.setBounds(410, 25, 215, 43);
        comboBoxGia.addActionListener(e -> {
            String selected = (String) comboBoxGia.getSelectedItem();
            if ("Giá: Thấp đến cao".equals(selected)) {
                sapXepTheoGiaTangDan();
            } else if ("Giá: Cao đến thấp".equals(selected)) {
                sapXepTheoGiaGiamDan();
            }
        });
        panelCenterNorth.add(comboBoxGia);

        panelCenterCenter = new JPanel();
        panelCenterCenter.setLayout(new BoxLayout(panelCenterCenter, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelCenterCenter);
        scrollPane.setBounds(76, 118, 996, 379);
        panelCenter.add(scrollPane);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(276, 546, 1090, 77);
        add(panel_2);
        panel_2.setLayout(null);

        JTextArea textAreaTimKiem = new JTextArea();
        textAreaTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textAreaTimKiem.setBounds(73, 27, 348, 28);
        textAreaTimKiem.setBackground(new Color(245, 245, 250));
        textAreaTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        textAreaTimKiem.setLineWrap(true);
        textAreaTimKiem.setWrapStyleWord(true);
        panel_2.add(textAreaTimKiem);

        JButton btnTimKiem = new JButton();
        btnTimKiem.setBounds(431, 27, 47, 28);
        ImageIcon icon = new ImageIcon("img\\search.png");
        Image img = icon.getImage().getScaledInstance(btnTimKiem.getWidth()-5, btnTimKiem.getHeight()-5, Image.SCALE_SMOOTH);
        btnTimKiem.setIcon(new ImageIcon(img));
        btnTimKiem.addActionListener(e -> timKiemBangTuKhoa(textAreaTimKiem.getText()));
        panel_2.add(btnTimKiem);

        JButton btnThemMonAn = new JButton("Thêm món ăn");
        btnThemMonAn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnThemMonAn.setBounds(552, 28, 137, 28);
        btnThemMonAn.addActionListener(e -> moPanelThemMonAn());
        panel_2.add(btnThemMonAn);

        // TẢI DỮ LIỆU + TÍNH SỐ LƯỢNG BÁN (CHUẨN NHƯ THỐNG KÊ)
        loadMonAnFromDB();
        tinhSoLuongBanCacMon(); // Tính số lượng bán để sắp xếp "Phổ biến"
    }

    // TÍNH SỐ LƯỢNG BÁN CỦA TỪNG MÓN (GIỐNG HỆT ManHinhThongKeMonAn)
    private void tinhSoLuongBanCacMon() {
        soLuongBanMap.clear();
        List<HoaDon> dsHoaDon = hoaDonDAO.timHoaDonTheoTrangThai("Đã thanh toán");
        for (HoaDon hd : dsHoaDon) {
            for (ChiTietHoaDon ct : cthdDAO.layDanhSachTheoHoaDon(hd.getMaHoaDon())) {
                String maMon = ct.getMonAn().getMaMon();
                soLuongBanMap.merge(maMon, ct.getSoLuong(), Integer::sum);
            }
        }
    }

    // SỬA CHỈ PHẦN NÀY: "PHỔ BIẾN" = BÁN NHIỀU NHẤT
    private void sapXepTheoDoPhoBien() {
        if (danhSachMonAnHienTai != null) {
            danhSachMonAnHienTai.sort((a, b) -> {
                int slA = soLuongBanMap.getOrDefault(a.getMaMon(), 0);
                int slB = soLuongBanMap.getOrDefault(b.getMaMon(), 0);
                return Integer.compare(slB, slA); // Giảm dần → bán nhiều nhất đứng trước
            });
            themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
        }
    }

    // Các hàm còn lại giữ nguyên 100% như file gốc của bạn
    private void loadMonAnFromDB() {
        new Thread(() -> {
            List<MonAn> all = monAnDAO.getAllMonAn();
            listGoc = all;
            danhSachMonAnHienTai = new ArrayList<>(all);
            SwingUtilities.invokeLater(() -> themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai));
        }).start();
    }

    private void themDuLieuVaoPanelCenterCenter(List<MonAn> listMonAn) {
        panelCenterCenter.removeAll();
        for (MonAn mon : listMonAn) {
            JPanel panelMonAn = new JPanel();
            panelMonAn.setLayout(null);
            panelMonAn.setPreferredSize(new Dimension(996, 160));
            panelMonAn.setBorder(BorderFactory.createLineBorder(Color.black));

            JLabel lblHinh = new JLabel();
            try {
                String imgPath = mon.getHinhAnh();
                ImageIcon icon = new ImageIcon(imgPath);
                Image img = icon.getImage().getScaledInstance(235, 130, Image.SCALE_SMOOTH);
                lblHinh.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                lblHinh.setText("No Image");
            }
            lblHinh.setBorder(new LineBorder(Color.BLACK));
            lblHinh.setBounds(22, 11, 235, 130);
            panelMonAn.add(lblHinh);

            JLabel lblMaMon = new JLabel(mon.getMaMon());
            lblMaMon.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblMaMon.setBounds(307, 30, 56, 26);
            panelMonAn.add(lblMaMon);

            JLabel lblTenMon = new JLabel(mon.getTenMon());
            lblTenMon.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTenMon.setBounds(307, 59, 189, 26);
            panelMonAn.add(lblTenMon);

            JLabel lblGia = new JLabel(df.format(mon.getGia()));
            lblGia.setFont(new Font("Segoe UI", Font.BOLD, 23));
            lblGia.setBounds(309, 96, 150, 26);
            panelMonAn.add(lblGia);

            JLabel lblLoaiLabel = new JLabel("Loại:");
            lblLoaiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblLoaiLabel.setBounds(570, 30, 36, 26);
            panelMonAn.add(lblLoaiLabel);

            String tenLoai = monAnDAO.chuyenDoiMaLoaiSangTen(mon.getLoaiMon());
            JLabel lblLoaiData = new JLabel(tenLoai);
            lblLoaiData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblLoaiData.setBounds(617, 30, 150, 26);
            panelMonAn.add(lblLoaiData);

            JLabel lblSoLuongTon = new JLabel("Số lượng tồn:");
            lblSoLuongTon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSoLuongTon.setBounds(727, 30, 86, 26);

            JLabel lblSoLuongTonData = new JLabel(String.valueOf(mon.getSoLuong()));
            lblSoLuongTonData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblSoLuongTonData.setBounds(823, 30, 36, 26);

            if (mon.getSoLuong() != 0) {
                panelMonAn.add(lblSoLuongTon);
                panelMonAn.add(lblSoLuongTonData);
            }

            String moTa = mon.getMoTa() != null ? mon.getMoTa() : "Không có mô tả";
            JLabel lblMoTa = new JLabel("<html><i>" + moTa + "</i></html>");
            lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblMoTa.setForeground(Color.DARK_GRAY);
            lblMoTa.setBounds(570, 60, 400, 60);
            panelMonAn.add(lblMoTa);

            panelCenterCenter.add(panelMonAn);
        }
        panelCenterCenter.revalidate();
        panelCenterCenter.repaint();
    }

    private void filterAndDisplay(String loaiLoc, String giaTriLoc) {
        if (listGoc == null) return;
        List<MonAn> danhSachLoc = new ArrayList<>();
        for (MonAn mon : listGoc) {
            boolean hopLe = false;
            switch (loaiLoc) {
                case "tatca" -> hopLe = true;
                case "loaimon" -> hopLe = mon.getLoaiMon().equalsIgnoreCase(giaTriLoc);
                case "tukhoa" -> hopLe = mon.getTenMon().toLowerCase().contains(giaTriLoc.toLowerCase());
            }
            if (hopLe) {
                danhSachLoc.add(mon);
            }
        }
        danhSachMonAnHienTai = danhSachLoc;
        themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
    }

    private void timKiemBangTuKhoa(String keyword) {
        if (listGoc == null) return;
        List<MonAn> ketQuaTimKiem = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            ketQuaTimKiem.addAll(listGoc);
        } else {
            String tuKhoa = keyword.toLowerCase();
            for (MonAn mon : listGoc) {
                if (mon.getTenMon().toLowerCase().contains(tuKhoa)) {
                    ketQuaTimKiem.add(mon);
                }
            }
        }
        danhSachMonAnHienTai = ketQuaTimKiem;
        themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
    }

    private void sapXepTheoGiaTangDan() {
        if (danhSachMonAnHienTai != null) {
            danhSachMonAnHienTai.sort((a, b) -> Double.compare(a.getGia(), b.getGia()));
            themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
        }
    }

    private void sapXepTheoGiaGiamDan() {
        if (danhSachMonAnHienTai != null) {
            danhSachMonAnHienTai.sort((a, b) -> Double.compare(b.getGia(), a.getGia()));
            themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
        }
    }

    private void sapXepTheoMaMonGiamDan() {
        if (danhSachMonAnHienTai != null) {
            danhSachMonAnHienTai.sort((a, b) -> b.getMaMon().compareTo(a.getMaMon()));
            themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
        }
    }

    private void locTheoMonAnChinh() { filterAndDisplay("loaimon", "MC"); }
    private void locTheoDoUong() { filterAndDisplay("loaimon", "DO"); }
    private void locTheoMonAnKem() { filterAndDisplay("loaimon", "MK"); }
    private void locTatCaDanhMuc() { loadMonAnFromDB(); }
    private void locTheoTuKhoa(String keyword) { filterAndDisplay("tukhoa", keyword); }

    private void locMonKhac(String loai, List<String> tenMonDaCo) {
        if (listGoc == null) return;
        List<MonAn> ketQuaLoc = new ArrayList<>();
        for (MonAn mon : listGoc) {
            if (mon.getLoaiMon().equalsIgnoreCase(loai)) {
                boolean daCo = false;
                for (String ten : tenMonDaCo) {
                    if (mon.getTenMon().contains(ten)) {
                        daCo = true;
                        break;
                    }
                }
                if (!daCo) {
                    ketQuaLoc.add(mon);
                }
            }
        }
        danhSachMonAnHienTai = ketQuaLoc;
        themDuLieuVaoPanelCenterCenter(danhSachMonAnHienTai);
    }

    private void moPanelThemMonAn() {
        ThemMonAn themMonAnPanel = new ThemMonAn();
        ManHinhChinhQuanLy container = new ManHinhChinhQuanLy();
        container.showPanel(themMonAnPanel);
    }
}