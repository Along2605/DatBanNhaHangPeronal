//package gui;
//
//import java.awt.*;
//import java.text.NumberFormat;
//import java.util.List;
//import java.util.Locale;
//
//import javax.swing.*;
//import javax.swing.border.*;
//import javax.swing.table.DefaultTableModel;
//
//import dao.LoaiMonAnDAO;
//import dao.MonAnDAO;
//import entity.LoaiMon;
//import entity.MonAn;
//
//public class DialogChonMonAn extends JDialog {
//    private final Color MAU_CAM = new Color(214, 116, 76);
//    private JList<LoaiMon> listLoaiMon;
//    private DefaultListModel<LoaiMon> listModelLoaiMon;
//    private JPanel pnlMonAnContainer;
//    private JTable tblMonDaChon;
//    private DefaultTableModel modelMonDaChon;
//    private JLabel lblTongTien;
//
//    private LoaiMonAnDAO loaiMonAnDAO;
//    private MonAnDAO monAnDAO;
//    
//    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//
//    public DialogChonMonAn(Frame parent) {
//        super(parent, "Chọn món ăn đặt trước", true);
//        setSize(1300, 750);
//        setLocationRelativeTo(parent);
//        setLayout(new BorderLayout(10, 10));
//
//        loaiMonAnDAO = new LoaiMonAnDAO();
//        monAnDAO = new MonAnDAO();
//
//        initComponents();
//        loadData();
//    }
//
//    private void initComponents() {
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//        mainPanel.setBackground(Color.WHITE);
//
//        mainPanel.add(createCategoryPanel(), BorderLayout.WEST);
//        mainPanel.add(createDishListPanel(), BorderLayout.CENTER);
//        mainPanel.add(createOrderSummaryPanel(), BorderLayout.EAST);
//        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//    
//    private JPanel createCategoryPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setPreferredSize(new Dimension(200, 0));
//        panel.setBorder(BorderFactory.createTitledBorder(
//            new LineBorder(MAU_CAM), "Loại món ăn", TitledBorder.LEFT, TitledBorder.TOP,
//            new Font("Segoe UI", Font.BOLD, 14), MAU_CAM));
//        panel.setBackground(Color.WHITE);
//
//        listModelLoaiMon = new DefaultListModel<>();
//        listLoaiMon = new JList<>(listModelLoaiMon);
//        listLoaiMon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        listLoaiMon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        
//        // SỬA LỖI 1: Thêm kiểm tra null để tránh NullPointerException
//        listLoaiMon.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                LoaiMon selected = listLoaiMon.getSelectedValue();
//                if (selected != null) { // Kiểm tra null
//                    if ("ALL".equals(selected.getMaLoaiMon())) {
//                        loadDsMonAn(null);
//                    } else {
//                        loadDsMonAn(selected.getMaLoaiMon());
//                    }
//                }
//            }
//        });
//        
//        JScrollPane scrollPane = new JScrollPane(listLoaiMon);
//        scrollPane.setBorder(null);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        return panel;
//    }
//
//    private JScrollPane createDishListPanel() {
//        pnlMonAnContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
//        pnlMonAnContainer.setBackground(Color.WHITE);
//
//        JScrollPane scrollPane = new JScrollPane(pnlMonAnContainer);
//        scrollPane.setBorder(BorderFactory.createTitledBorder(
//            new LineBorder(Color.LIGHT_GRAY), "Danh sách món", TitledBorder.LEFT, TitledBorder.TOP,
//            new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//        return scrollPane;
//    }
//
//    private JPanel createOrderSummaryPanel() {
//        JPanel panel = new JPanel(new BorderLayout(0, 10));
//        panel.setPreferredSize(new Dimension(380, 0));
//        panel.setBorder(BorderFactory.createTitledBorder(
//            new LineBorder(MAU_CAM), "Món đã chọn", TitledBorder.LEFT, TitledBorder.TOP,
//            new Font("Segoe UI", Font.BOLD, 14), MAU_CAM));
//        panel.setBackground(Color.WHITE);
//
//        String[] columns = {"Mã Món", "Tên món", "SL", "Đơn giá", "Thành tiền"};
//        modelMonDaChon = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        tblMonDaChon = new JTable(modelMonDaChon);
//        tblMonDaChon.setRowHeight(28);
//        
//        // Ẩn cột Mã Món nhưng vẫn giữ dữ liệu
//        tblMonDaChon.getColumnModel().getColumn(0).setMinWidth(0);
//        tblMonDaChon.getColumnModel().getColumn(0).setMaxWidth(0);
//        tblMonDaChon.getColumnModel().getColumn(0).setWidth(0);
//        
//        tblMonDaChon.getColumnModel().getColumn(1).setPreferredWidth(160);
//        tblMonDaChon.getColumnModel().getColumn(2).setPreferredWidth(30);
//
//        panel.add(new JScrollPane(tblMonDaChon), BorderLayout.CENTER);
//        
//        JPanel totalPanel = new JPanel(new BorderLayout());
//        totalPanel.setBorder(new EmptyBorder(10, 5, 5, 5));
//        totalPanel.setBackground(new Color(232, 245, 233));
//        
//        JLabel lblTongCong = new JLabel("TỔNG CỘNG:");
//        lblTongCong.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        totalPanel.add(lblTongCong, BorderLayout.WEST);
//
//        lblTongTien = new JLabel("0đ");
//        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblTongTien.setForeground(new Color(211, 47, 47));
//        totalPanel.add(lblTongTien, BorderLayout.EAST);
//        
//        panel.add(totalPanel, BorderLayout.SOUTH);
//        return panel;
//    }
//
//    private JPanel createButtonPanel() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
//        panel.setBorder(new EmptyBorder(5, 0, 0, 0));
//        panel.setBackground(Color.WHITE);
//        
//        JButton btnXacNhan = new JButton("Xác nhận");
//        btnXacNhan.setBackground(new Color(76, 175, 80));
//        btnXacNhan.setForeground(Color.WHITE);
//        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        btnXacNhan.setPreferredSize(new Dimension(150, 40));
//        btnXacNhan.addActionListener(e -> dispose());
//
//        JButton btnHuy = new JButton("Hủy");
//        btnHuy.setBackground(new Color(100, 100, 100));
//        btnHuy.setForeground(Color.WHITE);
//        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        btnHuy.setPreferredSize(new Dimension(150, 40));
//        btnHuy.addActionListener(e -> dispose());
//        
//        panel.add(btnXacNhan);
//        panel.add(btnHuy);
//        return panel;
//    }
//
//    private void loadData() {
//        listModelLoaiMon.addElement(new LoaiMon("ALL", "Tất cả món ăn"));
//        List<LoaiMon> dsLoai = loaiMonAnDAO.getDsLoaiMonAn();
//        for (LoaiMon loai : dsLoai) {
//            listModelLoaiMon.addElement(loai);
//        }
//        listLoaiMon.setSelectedIndex(0);
//        loadDsMonAn(null);
//    }
//
//    private void loadDsMonAn(String maLoaiMon) {
//        pnlMonAnContainer.removeAll();
//        List<MonAn> dsMonAn = monAnDAO.getDsMonAn(maLoaiMon);
//        for (MonAn mon : dsMonAn) {
//            pnlMonAnContainer.add(createMonAnCard(mon));
//        }
//        pnlMonAnContainer.revalidate();
//        pnlMonAnContainer.repaint();
//    }
//
//    private JPanel createMonAnCard(MonAn mon) {
//        JPanel card = new JPanel(new BorderLayout(5, 5));
//        card.setPreferredSize(new Dimension(160, 200));
//        card.setBorder(new LineBorder(Color.LIGHT_GRAY));
//        card.setBackground(Color.WHITE);
//
//        ImageIcon icon = new ImageIcon(mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty() 
//                                     ? mon.getHinhAnh() : "img/default-food.png");
//        Image scaledImage = icon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
//        card.add(new JLabel(new ImageIcon(scaledImage)), BorderLayout.NORTH);
//
//        JPanel infoPanel = new JPanel();
//        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
//        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//        infoPanel.setBackground(Color.WHITE);
//
//        infoPanel.add(new JLabel(mon.getTenMon()) {{
//            setFont(new Font("Segoe UI", Font.BOLD, 13));
//        }});
//        infoPanel.add(new JLabel(currencyFormat.format(mon.getGia())) {{
//            setFont(new Font("Segoe UI", Font.PLAIN, 12));
//            setForeground(MAU_CAM);
//        }});
//        card.add(infoPanel, BorderLayout.CENTER);
//
//        JButton btnThem = new JButton("Thêm");
//        btnThem.setBackground(MAU_CAM);
//        btnThem.setForeground(Color.WHITE);
//        btnThem.setFocusPainted(false);
//        
//        // CHỨC NĂNG MỚI: Thêm món vào giỏ hàng
//        btnThem.addActionListener(e -> themMonVaoGioHang(mon));
//        card.add(btnThem, BorderLayout.SOUTH);
//
//        return card;
//    }
//
//    // CHỨC NĂNG MỚI: Xử lý logic thêm món vào bảng
//    private void themMonVaoGioHang(MonAn mon) {
//        String maMon = mon.getMaMon();
//        // Kiểm tra xem món đã có trong bảng chưa
//        for (int i = 0; i < modelMonDaChon.getRowCount(); i++) {
//            if (modelMonDaChon.getValueAt(i, 0).equals(maMon)) {
//                // Nếu có, tăng số lượng
//                int soLuongHienTai = (int) modelMonDaChon.getValueAt(i, 2);
//                modelMonDaChon.setValueAt(soLuongHienTai + 1, i, 2);
//                
//                // Cập nhật thành tiền
//                double donGia = mon.getGia();
//                double thanhTienMoi = donGia * (soLuongHienTai + 1);
//                modelMonDaChon.setValueAt(thanhTienMoi, i, 4);
//                
//                capNhatTongTien();
//                return;
//            }
//        }
//        
//        // Nếu chưa có, thêm dòng mới
//        Object[] rowData = {
//            mon.getMaMon(),
//            mon.getTenMon(),
//            1, // Số lượng ban đầu
//            mon.getGia(),
//            mon.getGia() // Thành tiền ban đầu
//        };
//        modelMonDaChon.addRow(rowData);
//        capNhatTongTien();
//    }
//    
//    // CHỨC NĂNG MỚI: Cập nhật lại tổng tiền
//    private void capNhatTongTien() {
//        double tongTien = 0;
//        for (int i = 0; i < modelMonDaChon.getRowCount(); i++) {
//            tongTien += (double) modelMonDaChon.getValueAt(i, 4);
//        }
//        lblTongTien.setText(currencyFormat.format(tongTien));
//    }
//    
//    private static class WrapLayout extends FlowLayout {
//        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
//        @Override
//        public Dimension preferredLayoutSize(Container target) {
//            Dimension size = super.preferredLayoutSize(target);
//            if (target.getParent() instanceof JViewport) {
//                size.width = target.getParent().getWidth();
//            }
//            return size;
//        }
//    }
//}