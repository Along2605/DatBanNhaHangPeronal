package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import javax.swing.border.MatteBorder;

public class ManHinhThucDon extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JMenuBar menuBar;
    private JPanel panelCenter;
    private JPanel panelEast;
    private JPanel panelCenterNorth;
    private JPanel panelCenterSouth;
    private JPanel panelCenterCenter;
    private JButton btnMonAnChinh;
    private JButton btnDoUong;
    private JButton btnNewButton_2;
    private JPanel[] panelMonAn = new JPanel[16];
    private JLabel[] lblHinhMonAn = new JLabel[16];
    private JPanel[] panelMonAn_Right = new JPanel[16];
    private JPanel[] panel_MonAn_Right_North = new JPanel[16];
    private JPanel[] panel_MonAn_Right_South = new JPanel[16];
    private JButton[] btnMonAn_them = new JButton[16];
    private JLabel[] lblMonAn_tieuDe = new JLabel[16];
    private JLabel[] lblMonAn_gia = new JLabel[16];
    private JLabel[] lblMonAn_tenHanQuoc = new JLabel[16];
    private JLabel[] lblMonAn_thanhPhan = new JLabel[16];
    private JPanel[] panelCenterCenter_line = new JPanel[6];
    private JPanel panel;
    private JLabel lblGiaTongTienSanPham;
    private JLabel lblGiamGia;
    private JTextField textFieldGiamGia;
    private JButton btnThanhToan;
    private JPanel panelEastTop;
    private JLabel lblDanhSachDatMon;
    private JRadioButton rdbtMangDi;
    private JTable tableDatMon;
    private DefaultTableModel tableModel;
    private JPanel panelEastCenter;
    private JPanel[] panelEastCenter_Line = new JPanel[8]; 
    private JLabel[] lblDanhSachDatMon_Mon_TieuDe = new JLabel[8];
    private JLabel[] lblDanhSachDatMon_Mon_Gia = new JLabel[8];
    private JButton[] btnDanhSachMonAn_Mon_Cong = new JButton[8];
    private JButton btnNewButton_1;
    private JLabel[] lblDanhSachDatMon_Mon_GiaTong = new JLabel[8];
    private JLabel lblImgThungRacIcon;
    private JButton[] btnImgThungRacIcon_Mon = new JButton[8];
    private JLabel lblNewLabel_2;
    private JButton btnXuatHoaDon;
    private JButton btnXoaTatCaMon;
    private JButton btnLuuHoaDon;
    private JButton[] btnDanhSachMonAn_Mon_Tru = new JButton[8];
    private JLabel[] lblDanhSachMonAn_Mon_SoLuong = new JLabel[8];
    private JButton btnDanhSachMonAn_MonAnChinh;
    private JButton btnDanhSachMonAn_DoUong;
    private JButton btnDanhSachMonAn_MonAnKem;
    private JPanel panelDoUong;
    private JPanel[] panelDoUong_line = new JPanel[3]; 
    private JPanel[] panelDoUong_Data = new JPanel[7]; 
    private JLabel[] lblHinhDoUong = new JLabel[7];
    private JPanel[] panelDoUong_Right = new JPanel[7];
    private JPanel[] panel_DoUong_Right_North = new JPanel[7];
    private JPanel[] panel_DoUong_Right_South = new JPanel[7];
    private JButton[] btnDoUong_them = new JButton[7];
    private JLabel[] lblDoUong_tieuDe = new JLabel[7];
    private JLabel[] lblDoUong_gia = new JLabel[7];
    private JLabel[] lblDoUong_tenHanQuoc = new JLabel[7];
    private JLabel[] lblDoUong_thanhPhan = new JLabel[7];

    private JPanel panelMonAnKem;
    private JPanel[] panelMonAnKem_line = new JPanel[3]; 
    private JPanel[] panelMonAnKem_Data = new JPanel[7]; 
    private JLabel[] lblHinhMonAnKem = new JLabel[7];
    private JPanel[] panelMonAnKem_Right = new JPanel[7];
    private JPanel[] panel_MonAnKem_Right_North = new JPanel[7];
    private JPanel[] panel_MonAnKem_Right_South = new JPanel[7];
    private JButton[] btnMonAnKem_them = new JButton[7];
    private JLabel[] lblMonAnKem_tieuDe = new JLabel[7];
    private JLabel[] lblMonAnKem_gia = new JLabel[7];
    private JLabel[] lblMonAnKem_tenHanQuoc = new JLabel[7];
    private JLabel[] lblMonAnKem_thanhPhan = new JLabel[7];

   
    private static class DishData {
        String name;
        String koreanName;
        String ingredients;
        String price;
        String imagePath;

        DishData(String name, String koreanName, String ingredients, String price, String imagePath) {
            this.name = name;
            this.koreanName = koreanName;
            this.ingredients = ingredients;
            this.price = price;
            this.imagePath = imagePath;
        }
    }

    private DishData[] mainDishesData = {
        null, 
        new DishData("Tokbokki truyền thống", "떡볶이", "Bánh gạo, Rau củ, Trứng", "43,000 ", "img\\thucdon\\tokbokki_truyen_thong.PNG"),
        new DishData("Kimbap chiên giòn", "김밥", "Cơm, Rong biển, Thịt", "35,000 VNĐ", "same"), 
        new DishData("Mì tương đen đặc biệt", "짜장면", "Mì, Tương đen, Rau củ", "50,000 VNĐ", "same"),
        new DishData("Bibimbap hỗn hợp", "비빔밥", "Cơm, Rau củ, Thịt bò", "55,000", "same"),
        new DishData("Samgyeopsal nướng", "삼겹살", "Thịt heo, Rau sống, Tương ớt", "60,000 VNĐ", "same"),
        new DishData("Kimchi jjigae cay", "김치찌개", "Kimchi, Đậu phụ, Thịt", "45,000 VNĐ", "same"),
        new DishData("Japchae xào rau củ", "잡채", "Mì kính, Rau củ, Thịt", "50,000đ", "same"),
        new DishData("Tteokguk canh năm mới", "떡국", "Bánh gạo, Nước dùng, Trứng", "40,000đ", "same"),
        new DishData("Sundubu jjigae hải sản", "순두부찌개", "Đậu phụ mềm, Hải sản, Rau củ", "48,000đ", "same"),
        new DishData("Bulgogi thịt bò nướng", "불고기", "Thịt bò, Nước sốt, Rau củ", "65,000đ", "same"),
        new DishData("Haemul Pajeon hải sản", "해물파전", "Hải sản, Hành lá, Bột mì", "42,000đ", "same"),
        new DishData("Jeyuk Bokkeum cay", "제육볶음", "Thịt heo, Tương ớt, Rau củ", "55,000đ", "same"),
        new DishData("Doenjang Jjigae đậu tương", "된장찌개", "Tương đậu, Đậu phụ, Rau củ", "45,000đ", "same"),
        new DishData("Món 14", "Korean14", "Thành phần 14", "50,000đ", "same"), 
        new DishData("Món 15", "Korean15", "Thành phần 15", "60,000đ", "same")  
    };

    private DishData[] drinksData = {
        null,
        new DishData("Soju truyền thống", "소주", "Rượu gạo, Nước", "20,000 VNĐ", "img\\thucdon\\cocacola_icon.png"),
        new DishData("Makgeolli sữa gạo", "막걸리", "Rượu gạo, Sữa", "25,000 VNĐ", "same"),
        new DishData("Bia Hàn Quốc", "맥주", "Bia, Lúa mạch", "30,000 VNĐ", "same"),
        new DishData("Trà xanh", "녹차", "Lá trà, Nước", "15,000 VNĐ", "same"),
        new DishData("Sữa chuối", "바나나 우유", "Sữa, Chuối", "10,000 VNĐ", "same"),
        new DishData("Nước gạo ngọt", "식혜", "Gạo, Đường", "10,000 VNĐ", "same")
    };

    private DishData[] sidesData = {
        null, 
        new DishData("Kimchi bắp cải", "김치", "Bắp cải, Ớt bột", "10,000 VNĐ", "img\\thucdon\\salad_icon.png"),
        new DishData("Rau trộn", "나물", "Rau củ, Gia vị", "8,000 VNĐ", "same"),
        new DishData("Miến trộn", "잡채", "Miến, Rau củ", "15,000 VNĐ", "same"),
        new DishData("Trứng hấp", "계란찜", "Trứng, Hành lá", "12,000 VNĐ", "same"),
        new DishData("Đậu hũ kho", "두부조림", "Đậu hũ, Nước tương", "10,000 VNĐ", "same"),
        new DishData("Dưa chuột trộn", "오이무침", "Dưa chuột, Gia vị", "8,000 VNĐ", "same")
    };

    private DishData[] orderedDishesData = {
        null, 
        new DishData("Kimbap chiên giòn", null, null, "35,000", null),
        new DishData("Tokbokki truyền thống", null, null, "43,000", null),
        new DishData("Mì tương đen đặc biệt", null, null, "50,000", null),
        new DishData("Bibimbap hỗn hợp", null, null, "55,000", null),
        new DishData("Samgyeopsal nướng", null, null, "60,000", null),
        new DishData("Kimchi jjigae cay", null, null, "45,000", null),
        new DishData("Japchae xào rau củ", null, null, "50,000", null)
    };

    private String[] orderedQuantities = {null, "2", "1", "2", "1", "2", "1", "1"};
    private String[] orderedTotalPrices = {null, "70,000 VNĐ", "43,000 VNĐ", "100,000 VNĐ", "55,000 VNĐ", "120,000 VNĐ", "45,000 VNĐ", "50,000 VNĐ"};

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManHinhThucDon frame = new ManHinhThucDon();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManHinhThucDon() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 10, 1200, 700);
        setTitle("Quản Lý Nhà Hàng");

        getContentPane().setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        menuBar.setBorder(new LineBorder(new Color(0, 0, 0)));
        menuBar.setBackground(new Color(214, 116, 76, 255));
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        setJMenuBar(menuBar);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setSize(new Dimension(60, 60));
        lblNewLabel.setBackground(new Color(214, 116, 76, 180));
        lblNewLabel.setOpaque(true);
        ImageIcon icon = new ImageIcon("img\\logo_nhahang.png");
        Image img = icon.getImage().getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), Image.SCALE_SMOOTH);
        lblNewLabel.setIcon(new ImageIcon(img));
        menuBar.add(lblNewLabel);
        menuBar.add(Box.createHorizontalStrut(5));

        MenuBuilderQuanLy menuBuilder = new MenuBuilderQuanLy();
        menuBar.add(menuBuilder.createHeThongMenu());
        menuBar.add(menuBuilder.createThucDonMenu());
        menuBar.add(menuBuilder.createKhuVucMenu());
        menuBar.add(menuBuilder.createCaLamMenu());
        menuBar.add(menuBuilder.createNhanVienMenu());
        menuBar.add(menuBuilder.createDoanhThuMenu());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout());
        getContentPane().add(contentPane, BorderLayout.CENTER);

        panelCenter = new JPanel();
        panelCenter.setPreferredSize(new Dimension(800, 600));
        contentPane.add(panelCenter, BorderLayout.WEST);
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        panelCenterNorth = new JPanel();
        panelCenterNorth.setMaximumSize(new Dimension(32767, 100));
        panelCenterNorth.setPreferredSize(new Dimension(800, 100));
        panelCenterNorth.setBackground(new Color(245, 245, 245));
        panelCenter.add(panelCenterNorth);
        panelCenterNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        class RoundedButton extends JButton {
            private int radius;

            public RoundedButton(String text, int radius) {
                super(text);
                this.radius = radius;
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.setColor(new Color(39, 174, 96));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                g2.dispose();
                super.paintComponent(g);
            }
        }

        btnDanhSachMonAn_MonAnChinh = new RoundedButton("MÓN ĂN CHÍNH", 20);
        btnDanhSachMonAn_MonAnChinh.setPreferredSize(new Dimension(200, 60));
        btnDanhSachMonAn_MonAnChinh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDanhSachMonAn_MonAnChinh.setForeground(new Color(255, 255, 255));
        btnDanhSachMonAn_MonAnChinh.setBackground(new Color(46, 204, 113));
        ImageIcon iconMain = new ImageIcon("img\\thucdon\\main_course_icon.png");
        Image imgMain = iconMain.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        btnDanhSachMonAn_MonAnChinh.setIcon(new ImageIcon(imgMain));
        btnDanhSachMonAn_MonAnChinh.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDanhSachMonAn_MonAnChinh.setIconTextGap(10);
        btnDanhSachMonAn_MonAnChinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnDanhSachMonAn_MonAnChinh.setBackground(new Color(39, 174, 96));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDanhSachMonAn_MonAnChinh.setBackground(new Color(46, 204, 113));
            }
        });
        btnDanhSachMonAn_MonAnChinh.setToolTipText("Món ăn chính");
        panelCenterNorth.add(btnDanhSachMonAn_MonAnChinh);

        btnDanhSachMonAn_DoUong = new RoundedButton("ĐỒ UỐNG", 20);
        btnDanhSachMonAn_DoUong.setPreferredSize(new Dimension(200, 60));
        btnDanhSachMonAn_DoUong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDanhSachMonAn_DoUong.setForeground(new Color(255, 255, 255));
        btnDanhSachMonAn_DoUong.setBackground(new Color(46, 204, 113));
        ImageIcon iconDrink = new ImageIcon("img\\thucdon\\soju_icon.png");
        Image imgDrink = iconDrink.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        btnDanhSachMonAn_DoUong.setIcon(new ImageIcon(imgDrink));
        btnDanhSachMonAn_DoUong.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDanhSachMonAn_DoUong.setIconTextGap(10);
        btnDanhSachMonAn_DoUong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnDanhSachMonAn_DoUong.setBackground(new Color(39, 174, 96));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDanhSachMonAn_DoUong.setBackground(new Color(46, 204, 113));
            }
        });
        btnDanhSachMonAn_DoUong.setToolTipText("Đồ uống");
        panelCenterNorth.add(btnDanhSachMonAn_DoUong);

        btnDanhSachMonAn_MonAnKem = new RoundedButton("MÓN ĂN KÈM", 20);
        btnDanhSachMonAn_MonAnKem.setPreferredSize(new Dimension(200, 60));
        btnDanhSachMonAn_MonAnKem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDanhSachMonAn_MonAnKem.setForeground(new Color(255, 255, 255));
        btnDanhSachMonAn_MonAnKem.setBackground(new Color(46, 204, 113));
        ImageIcon iconSide = new ImageIcon("img\\thucdon\\kim_chi_icon.png");
        Image imgSide = iconSide.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        btnDanhSachMonAn_MonAnKem.setIcon(new ImageIcon(imgSide));
        btnDanhSachMonAn_MonAnKem.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDanhSachMonAn_MonAnKem.setIconTextGap(10);
        btnDanhSachMonAn_MonAnKem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnDanhSachMonAn_MonAnKem.setBackground(new Color(39, 174, 96));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDanhSachMonAn_MonAnKem.setBackground(new Color(46, 204, 113));
            }
        });
        btnDanhSachMonAn_MonAnKem.setToolTipText("Món ăn kèm");
        panelCenterNorth.add(btnDanhSachMonAn_MonAnKem);

        panelCenterSouth = new JPanel();
        panelCenterSouth.setBackground(new Color(0, 255, 255));
        panelCenter.add(panelCenterSouth);
        panelCenterSouth.setLayout(new BoxLayout(panelCenterSouth, BoxLayout.Y_AXIS));

        panelCenterCenter = new JPanel();
        panelCenterCenter.setPreferredSize(new Dimension(780, 800));
        panelCenterCenter.setLayout(new BoxLayout(panelCenterCenter, BoxLayout.Y_AXIS));
        panelCenterCenter.setBackground(new Color(255, 255, 255));
        JScrollPane scrollPane = new JScrollPane(panelCenterCenter);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelCenter.add(scrollPane);

        initializeMainDishes(scrollPane);
        initializeDrinks(scrollPane);
        initializeSides(scrollPane);
        initializeOrderedItems();

        panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBackground(new Color(255, 255, 255));
        panel.setPreferredSize(new Dimension(10, 150));
        panelCenter.add(panel);
        panel.setLayout(null);

        JLabel lblTongTienSanPham = new JLabel("Tổng tiền sản phẩm:");
        lblTongTienSanPham.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTongTienSanPham.setPreferredSize(new Dimension(120, 50));
        lblTongTienSanPham.setBounds(44, 25, 198, 30);
        panel.add(lblTongTienSanPham);

        lblGiaTongTienSanPham = new JLabel("826,000 VNĐ");
        lblGiaTongTienSanPham.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGiaTongTienSanPham.setBounds(253, 28, 126, 22);
        panel.add(lblGiaTongTienSanPham);

        lblGiamGia = new JLabel("Giảm giá:");
        lblGiamGia.setPreferredSize(new Dimension(120, 50));
        lblGiamGia.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblGiamGia.setBounds(44, 78, 198, 30);
        panel.add(lblGiamGia);

        textFieldGiamGia = new JTextField();
        textFieldGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        textFieldGiamGia.setText("0%");
        textFieldGiamGia.setBounds(234, 66, 145, 35);
        panel.add(textFieldGiamGia);
        textFieldGiamGia.setColumns(10);
        textFieldGiamGia.setEditable(false);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(255, 255, 255));
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 11, 417, 115);
        panel.add(panel_1);

        JLabel lblTongCong = new JLabel("Tổng cộng:");
        lblTongCong.setPreferredSize(new Dimension(120, 50));
        lblTongCong.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTongCong.setBounds(466, 25, 110, 30);
        panel.add(lblTongCong);

        JLabel lblGiaTongTienSanPham_1 = new JLabel("826,000 VNĐ");
        lblGiaTongTienSanPham_1.setForeground(new Color(255, 0, 0));
        lblGiaTongTienSanPham_1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGiaTongTienSanPham_1.setBounds(600, 28, 126, 22);
        panel.add(lblGiaTongTienSanPham_1);

        JButton btHuy = new JButton("Hủy");
        btHuy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btHuy.setForeground(Color.WHITE);
        btHuy.setBackground(Color.BLACK);
        btHuy.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btHuy.setBounds(476, 69, 96, 49);
        panel.add(btHuy);

        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnThanhToan.setBackground(Color.RED);
        btnThanhToan.setForeground(new Color(255, 255, 255));
        btnThanhToan.setMaximumSize(new Dimension(51, 23));
        btnThanhToan.setPreferredSize(new Dimension(51, 23));
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setBounds(588, 65, 130, 56);
        panel.add(btnThanhToan);

        panelEast = new JPanel();
        panelEast.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelEast.setBackground(Color.WHITE);
        contentPane.add(panelEast, BorderLayout.CENTER);
        panelEast.setLayout(null);

        panelEastTop = new JPanel();
        panelEastTop.setBounds(35, 11, 299, 90);
        panelEast.add(panelEastTop);
        panelEastTop.setLayout(null);

        lblDanhSachDatMon = new JLabel("Danh sách đặt món");
        lblDanhSachDatMon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDanhSachDatMon.setBounds(10, 37, 180, 25);
        panelEastTop.add(lblDanhSachDatMon);

        JComboBox<String> comboBan = new JComboBox<>();
        for (int i = 1; i <= 9; i++) {
            comboBan.addItem("Bàn số " + i);
        }
        comboBan.setBackground(Color.WHITE);
        comboBan.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 80)));
        comboBan.setBounds(187, 37, 102, 30);
        panelEastTop.add(comboBan);

        ButtonGroup group = new ButtonGroup();

        JRadioButton rdbtnAnTaiBan = new JRadioButton("Ăn tại bàn");
        rdbtnAnTaiBan.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rdbtnAnTaiBan.setBounds(138, 7, 80, 20);
        panelEastTop.add(rdbtnAnTaiBan);
        group.add(rdbtnAnTaiBan);

        rdbtMangDi = new JRadioButton("Mang đi");
        rdbtMangDi.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rdbtMangDi.setBounds(220, 7, 80, 20);
        panelEastTop.add(rdbtMangDi);
        group.add(rdbtMangDi);

        panelEastCenter = new JPanel();
        panelEastCenter.setBorder(null);
        panelEastCenter.setBackground(Color.WHITE);
        panelEastCenter.setLayout(null);
        panelEastCenter.setPreferredSize(new Dimension(354, 66 * 7));

        JScrollPane eastCenterScrollPane = new JScrollPane(panelEastCenter);
        eastCenterScrollPane.setBounds(10, 104, 364, 317);
        eastCenterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        eastCenterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelEast.add(eastCenterScrollPane);

        for (int i = 1; i <= 7; i++) {
            initializeOrderedLine(i);
        }

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 432, 354, 157);
        panelEast.add(panel_2);
        panel_2.setLayout(null);

        JButton btnInBillChoBep = new JButton("In bill cho bếp");
        btnInBillChoBep.setBackground(new Color(255, 255, 255));
        btnInBillChoBep.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnInBillChoBep.setVerticalAlignment(SwingConstants.BOTTOM);
        ImageIcon iconDauBep = new ImageIcon("img\\thucdon\\dau_bep_icon.png");
        Image imgDauBep = iconDauBep.getImage().getScaledInstance(50, 40, Image.SCALE_SMOOTH);
        btnInBillChoBep.setIcon(new ImageIcon(imgDauBep));
        btnInBillChoBep.setHorizontalTextPosition(SwingConstants.CENTER);
        btnInBillChoBep.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnInBillChoBep.setBounds(43, 11, 131, 75);
        panel_2.add(btnInBillChoBep);

        btnXuatHoaDon = new JButton("Xuất hóa đơn");
        btnXuatHoaDon.setBackground(new Color(255, 255, 255));
        btnXuatHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatHoaDon.setVerticalAlignment(SwingConstants.BOTTOM);
        ImageIcon iconXuatHoaDon = new ImageIcon("img\\thucdon\\printer_icon.png");
        Image imgXuatHoaDon = iconXuatHoaDon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        btnXuatHoaDon.setIcon(new ImageIcon(imgXuatHoaDon));
        btnXuatHoaDon.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXuatHoaDon.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnXuatHoaDon.setIconTextGap(5);
        btnXuatHoaDon.setBounds(197, 11, 131, 75);
        panel_2.add(btnXuatHoaDon);

        btnXoaTatCaMon = new JButton("Xóa tất cả món");
        btnXoaTatCaMon.setBackground(new Color(192, 192, 192));
        btnXoaTatCaMon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoaTatCaMon.setBounds(43, 97, 131, 38);
        panel_2.add(btnXoaTatCaMon);

        btnLuuHoaDon = new JButton("Lưu hóa đơn");
        btnLuuHoaDon.setBackground(new Color(0, 128, 255));
        btnLuuHoaDon.setForeground(new Color(255, 255, 255));
        btnLuuHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuuHoaDon.setBounds(197, 97, 131, 38);
        panel_2.add(btnLuuHoaDon);

        btnDanhSachMonAn_MonAnChinh.addActionListener(e -> scrollPane.setViewportView(panelCenterCenter));
        btnDanhSachMonAn_DoUong.addActionListener(e -> scrollPane.setViewportView(panelDoUong));
        btnDanhSachMonAn_MonAnKem.addActionListener(e -> scrollPane.setViewportView(panelMonAnKem));
    }

    private void initializeMainDishes(JScrollPane scrollPane) {
        String lastImagePath = "";
        for (int line = 1; line <= 5; line++) {
            panelCenterCenter_line[line] = new JPanel();
            panelCenterCenter_line[line].setBorder(new LineBorder(new Color(0, 0, 0), 0));
            FlowLayout flowLayout = (FlowLayout) panelCenterCenter_line[line].getLayout();
            flowLayout.setHgap(10);
            panelCenterCenter_line[line].setBackground(new Color(255, 255, 255));
            panelCenterCenter.add(panelCenterCenter_line[line]);
            panelCenterCenter.add(Box.createVerticalStrut(10));

            int start = (line - 1) * 3 + 1;
            for (int i = 0; i < 3; i++) {
                int idx = start + i;
                if (idx > 15) break;
                DishData data = mainDishesData[idx];
                if ("same".equals(data.imagePath)) data.imagePath = lastImagePath;
                else lastImagePath = data.imagePath;

                panelMonAn[idx] = new JPanel();
                panelMonAn[idx].setPreferredSize(new Dimension(250, 130));
                panelCenterCenter_line[line].add(panelMonAn[idx]);
                panelMonAn[idx].setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

                ImageIcon icon = new ImageIcon(data.imagePath);
                Image img = icon.getImage().getScaledInstance(125, 110, Image.SCALE_SMOOTH);
                lblHinhMonAn[idx] = new JLabel("");
                lblHinhMonAn[idx].setIcon(new ImageIcon(img));
                lblHinhMonAn[idx].setPreferredSize(new Dimension(125, 110));
                panelMonAn[idx].add(lblHinhMonAn[idx]);

                panelMonAn_Right[idx] = new JPanel();
                panelMonAn_Right[idx].setPreferredSize(new Dimension(110, 110));
                panelMonAn[idx].add(panelMonAn_Right[idx]);
                panelMonAn_Right[idx].setLayout(new BorderLayout(0, 0));

                panel_MonAn_Right_North[idx] = new JPanel();
                panel_MonAn_Right_North[idx].setPreferredSize(new Dimension(10, 50));
                panelMonAn_Right[idx].add(panel_MonAn_Right_North[idx], BorderLayout.NORTH);
                panel_MonAn_Right_North[idx].setLayout(new BoxLayout(panel_MonAn_Right_North[idx], BoxLayout.Y_AXIS));

                lblMonAn_tieuDe[idx] = new JLabel(data.name);
                lblMonAn_tieuDe[idx].setPreferredSize(new Dimension(100, 48));
                lblMonAn_tieuDe[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMonAn_tieuDe[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_MonAn_Right_North[idx].add(lblMonAn_tieuDe[idx]);

                lblMonAn_tenHanQuoc[idx] = new JLabel(data.koreanName);
                lblMonAn_tenHanQuoc[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_MonAn_Right_North[idx].add(lblMonAn_tenHanQuoc[idx]);

                lblMonAn_thanhPhan[idx] = new JLabel(data.ingredients);
                lblMonAn_thanhPhan[idx].setFont(new Font("Segoe UI", Font.ITALIC, 8));
                lblMonAn_thanhPhan[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_MonAn_Right_North[idx].add(lblMonAn_thanhPhan[idx]);

                panel_MonAn_Right_South[idx] = new JPanel();
                FlowLayout fl = (FlowLayout) panel_MonAn_Right_South[idx].getLayout();
                fl.setAlignment(FlowLayout.RIGHT);
                panelMonAn_Right[idx].add(panel_MonAn_Right_South[idx], BorderLayout.SOUTH);

                btnMonAn_them[idx] = new JButton("Thêm");
                btnMonAn_them[idx].setForeground(new Color(64, 0, 0));
                btnMonAn_them[idx].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                btnMonAn_them[idx].setBackground(new Color(255, 128, 64));
                btnMonAn_them[idx].setContentAreaFilled(true);
                btnMonAn_them[idx].setOpaque(true);
                btnMonAn_them[idx].setFont(new Font("Segoe UI", Font.PLAIN, 10));
                btnMonAn_them[idx].setPreferredSize(new Dimension(60, 23));
                panel_MonAn_Right_South[idx].add(btnMonAn_them[idx]);

                lblMonAn_gia[idx] = new JLabel(data.price);
                lblMonAn_gia[idx].setPreferredSize(new Dimension(40, 30));
                lblMonAn_gia[idx].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 0, 0, 80)));
                lblMonAn_gia[idx].setBackground(new Color(64, 0, 0));
                lblMonAn_gia[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMonAn_gia[idx].setHorizontalAlignment(SwingConstants.CENTER);
                panelMonAn_Right[idx].add(lblMonAn_gia[idx], BorderLayout.CENTER);
            }
        }
    }

    private void initializeDrinks(JScrollPane scrollPane) {
        panelDoUong = new JPanel();
        panelDoUong.setPreferredSize(new Dimension(780, 300));
        panelDoUong.setLayout(new BoxLayout(panelDoUong, BoxLayout.Y_AXIS));
        panelDoUong.setBackground(new Color(255, 255, 255));

        String lastImagePath = "";
        for (int line = 1; line <= 2; line++) {
            panelDoUong_line[line] = new JPanel();
            panelDoUong_line[line].setBorder(new LineBorder(new Color(0, 0, 0), 0));
            FlowLayout flowLayout = (FlowLayout) panelDoUong_line[line].getLayout();
            flowLayout.setHgap(10);
            panelDoUong_line[line].setBackground(new Color(255, 255, 255));
            panelDoUong.add(panelDoUong_line[line]);
            panelDoUong.add(Box.createVerticalStrut(10));

            int start = (line - 1) * 3 + 1;
            for (int i = 0; i < 3; i++) {
                int idx = start + i;
                if (idx > 6) break;
                DishData data = drinksData[idx];
                if ("same".equals(data.imagePath)) data.imagePath = lastImagePath;
                else lastImagePath = data.imagePath;

                this.panelDoUong_Data[idx] = new JPanel();
                this.panelDoUong_Data[idx].setPreferredSize(new Dimension(250, 130));
                panelDoUong_line[line].add(this.panelDoUong_Data[idx]);
                this.panelDoUong_Data[idx].setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

                ImageIcon icon = new ImageIcon(data.imagePath);
                Image img = icon.getImage().getScaledInstance(125, 110, Image.SCALE_SMOOTH);
                lblHinhDoUong[idx] = new JLabel("");
                lblHinhDoUong[idx].setIcon(new ImageIcon(img));
                lblHinhDoUong[idx].setPreferredSize(new Dimension(125, 110));
                this.panelDoUong_Data[idx].add(lblHinhDoUong[idx]);

                panelDoUong_Right[idx] = new JPanel();
                panelDoUong_Right[idx].setPreferredSize(new Dimension(110, 110));
                this.panelDoUong_Data[idx].add(panelDoUong_Right[idx]);
                panelDoUong_Right[idx].setLayout(new BorderLayout(0, 0));

                panel_DoUong_Right_North[idx] = new JPanel();
                panel_DoUong_Right_North[idx].setPreferredSize(new Dimension(10, 50));
                panelDoUong_Right[idx].add(panel_DoUong_Right_North[idx], BorderLayout.NORTH);
                panel_DoUong_Right_North[idx].setLayout(new BoxLayout(panel_DoUong_Right_North[idx], BoxLayout.Y_AXIS));

                lblDoUong_tieuDe[idx] = new JLabel(data.name);
                lblDoUong_tieuDe[idx].setPreferredSize(new Dimension(100, 48));
                lblDoUong_tieuDe[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDoUong_tieuDe[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_DoUong_Right_North[idx].add(lblDoUong_tieuDe[idx]);

                lblDoUong_tenHanQuoc[idx] = new JLabel(data.koreanName);
                lblDoUong_tenHanQuoc[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_DoUong_Right_North[idx].add(lblDoUong_tenHanQuoc[idx]);

                lblDoUong_thanhPhan[idx] = new JLabel(data.ingredients);
                lblDoUong_thanhPhan[idx].setFont(new Font("Segoe UI", Font.ITALIC, 8));
                lblDoUong_thanhPhan[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_DoUong_Right_North[idx].add(lblDoUong_thanhPhan[idx]);

                panel_DoUong_Right_South[idx] = new JPanel();
                FlowLayout fl = (FlowLayout) panel_DoUong_Right_South[idx].getLayout();
                fl.setAlignment(FlowLayout.RIGHT);
                panelDoUong_Right[idx].add(panel_DoUong_Right_South[idx], BorderLayout.SOUTH);

                btnDoUong_them[idx] = new JButton("Thêm");
                btnDoUong_them[idx].setForeground(new Color(64, 0, 0));
                btnDoUong_them[idx].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                btnDoUong_them[idx].setBackground(new Color(255, 128, 64));
                btnDoUong_them[idx].setContentAreaFilled(true);
                btnDoUong_them[idx].setOpaque(true);
                btnDoUong_them[idx].setFont(new Font("Segoe UI", Font.PLAIN, 10));
                btnDoUong_them[idx].setPreferredSize(new Dimension(60, 23));
                panel_DoUong_Right_South[idx].add(btnDoUong_them[idx]);

                lblDoUong_gia[idx] = new JLabel(data.price);
                lblDoUong_gia[idx].setPreferredSize(new Dimension(40, 30));
                lblDoUong_gia[idx].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 0, 0, 80)));
                lblDoUong_gia[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDoUong_gia[idx].setHorizontalAlignment(SwingConstants.CENTER);
                panelDoUong_Right[idx].add(lblDoUong_gia[idx], BorderLayout.CENTER);
            }
        }
    }

    private void initializeSides(JScrollPane scrollPane) {
        panelMonAnKem = new JPanel();
        panelMonAnKem.setPreferredSize(new Dimension(780, 300));
        panelMonAnKem.setLayout(new BoxLayout(panelMonAnKem, BoxLayout.Y_AXIS));
        panelMonAnKem.setBackground(new Color(255, 255, 255));

        String lastImagePath = "";
        for (int line = 1; line <= 2; line++) {
            panelMonAnKem_line[line] = new JPanel();
            panelMonAnKem_line[line].setBorder(new LineBorder(new Color(0, 0, 0), 0));
            FlowLayout flowLayout = (FlowLayout) panelMonAnKem_line[line].getLayout();
            flowLayout.setHgap(10);
            panelMonAnKem_line[line].setBackground(new Color(255, 255, 255));
            panelMonAnKem.add(panelMonAnKem_line[line]);
            panelMonAnKem.add(Box.createVerticalStrut(10));

            int start = (line - 1) * 3 + 1;
            for (int i = 0; i < 3; i++) {
                int idx = start + i;
                if (idx > 6) break;
                DishData data = sidesData[idx];
                if ("same".equals(data.imagePath)) data.imagePath = lastImagePath;
                else lastImagePath = data.imagePath;

                this.panelMonAnKem_Data[idx] = new JPanel();
                this.panelMonAnKem_Data[idx].setPreferredSize(new Dimension(250, 130));
                panelMonAnKem_line[line].add(this.panelMonAnKem_Data[idx]);
                this.panelMonAnKem_Data[idx].setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

                ImageIcon icon = new ImageIcon(data.imagePath);
                Image img = icon.getImage().getScaledInstance(125, 110, Image.SCALE_SMOOTH);
                lblHinhMonAnKem[idx] = new JLabel("");
                lblHinhMonAnKem[idx].setIcon(new ImageIcon(img));
                lblHinhMonAnKem[idx].setPreferredSize(new Dimension(125, 110));
                this.panelMonAnKem_Data[idx].add(lblHinhMonAnKem[idx]);

                panelMonAnKem_Right[idx] = new JPanel();
                panelMonAnKem_Right[idx].setPreferredSize(new Dimension(110, 110));
                this.panelMonAnKem_Data[idx].add(panelMonAnKem_Right[idx]);
                panelMonAnKem_Right[idx].setLayout(new BorderLayout(0, 0));

                panel_MonAnKem_Right_North[idx] = new JPanel();
                panel_MonAnKem_Right_North[idx].setPreferredSize(new Dimension(10, 50));
                panelMonAnKem_Right[idx].add(panel_MonAnKem_Right_North[idx], BorderLayout.NORTH);
                panel_MonAnKem_Right_North[idx].setLayout(new BoxLayout(panel_MonAnKem_Right_North[idx], BoxLayout.Y_AXIS));

                lblMonAnKem_tieuDe[idx] = new JLabel(data.name);
                lblMonAnKem_tieuDe[idx].setPreferredSize(new Dimension(100, 48));
                lblMonAnKem_tieuDe[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMonAnKem_tieuDe[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_MonAnKem_Right_North[idx].add(lblMonAnKem_tieuDe[idx]);

                lblMonAnKem_tenHanQuoc[idx] = new JLabel(data.koreanName);
                lblMonAnKem_tenHanQuoc[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_MonAnKem_Right_North[idx].add(lblMonAnKem_tenHanQuoc[idx]);

                lblMonAnKem_thanhPhan[idx] = new JLabel(data.ingredients);
                lblMonAnKem_thanhPhan[idx].setFont(new Font("Segoe UI", Font.ITALIC, 8));
                lblMonAnKem_thanhPhan[idx].setAlignmentX(Component.LEFT_ALIGNMENT);
                panel_MonAnKem_Right_North[idx].add(lblMonAnKem_thanhPhan[idx]);

                panel_MonAnKem_Right_South[idx] = new JPanel();
                FlowLayout fl = (FlowLayout) panel_MonAnKem_Right_South[idx].getLayout();
                fl.setAlignment(FlowLayout.RIGHT);
                panelMonAnKem_Right[idx].add(panel_MonAnKem_Right_South[idx], BorderLayout.SOUTH);

                btnMonAnKem_them[idx] = new JButton("Thêm");
                btnMonAnKem_them[idx].setForeground(new Color(64, 0, 0));
                btnMonAnKem_them[idx].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                btnMonAnKem_them[idx].setBackground(new Color(255, 128, 64));
                btnMonAnKem_them[idx].setContentAreaFilled(true);
                btnMonAnKem_them[idx].setOpaque(true);
                btnMonAnKem_them[idx].setFont(new Font("Segoe UI", Font.PLAIN, 10));
                btnMonAnKem_them[idx].setPreferredSize(new Dimension(60, 23));
                panel_MonAnKem_Right_South[idx].add(btnMonAnKem_them[idx]);

                lblMonAnKem_gia[idx] = new JLabel(data.price);
                lblMonAnKem_gia[idx].setPreferredSize(new Dimension(40, 30));
                lblMonAnKem_gia[idx].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 0, 0, 80)));
                lblMonAnKem_gia[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMonAnKem_gia[idx].setHorizontalAlignment(SwingConstants.CENTER);
                panelMonAnKem_Right[idx].add(lblMonAnKem_gia[idx], BorderLayout.CENTER);
            }
        }
    }

    private void initializeOrderedLine(int idx) {
        panelEastCenter_Line[idx] = new JPanel();
        panelEastCenter_Line[idx].setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 120)));
        panelEastCenter_Line[idx].setBounds(0, (idx - 1) * 66 + 11, 344, 66);
        panelEastCenter.add(panelEastCenter_Line[idx]);
        panelEastCenter_Line[idx].setLayout(null);

        DishData data = orderedDishesData[idx];

        lblDanhSachDatMon_Mon_TieuDe[idx] = new JLabel(data.name);
        lblDanhSachDatMon_Mon_TieuDe[idx].setPreferredSize(new Dimension(100, 48));
        lblDanhSachDatMon_Mon_TieuDe[idx].setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDanhSachDatMon_Mon_TieuDe[idx].setAlignmentX(0.0f);
        lblDanhSachDatMon_Mon_TieuDe[idx].setBounds(0, 11, 102, 25);
        panelEastCenter_Line[idx].add(lblDanhSachDatMon_Mon_TieuDe[idx]);

        lblDanhSachDatMon_Mon_Gia[idx] = new JLabel(data.price);
        lblDanhSachDatMon_Mon_Gia[idx].setPreferredSize(new Dimension(40, 30));
        lblDanhSachDatMon_Mon_Gia[idx].setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachDatMon_Mon_Gia[idx].setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDanhSachDatMon_Mon_Gia[idx].setBackground(new Color(64, 0, 0));
        lblDanhSachDatMon_Mon_Gia[idx].setForeground(new Color(0, 0, 0, 180));
        lblDanhSachDatMon_Mon_Gia[idx].setBounds(49, 37, 62, 18);
        panelEastCenter_Line[idx].add(lblDanhSachDatMon_Mon_Gia[idx]);

        lblDanhSachMonAn_Mon_SoLuong[idx] = new JLabel(orderedQuantities[idx]);
        lblDanhSachMonAn_Mon_SoLuong[idx].setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachMonAn_Mon_SoLuong[idx].setBorder(new LineBorder(new Color(0, 0, 0)));
        lblDanhSachMonAn_Mon_SoLuong[idx].setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDanhSachMonAn_Mon_SoLuong[idx].setBounds(122, 11, 30, 44);
        panelEastCenter_Line[idx].add(lblDanhSachMonAn_Mon_SoLuong[idx]);

        btnDanhSachMonAn_Mon_Cong[idx] = new JButton("");
        btnDanhSachMonAn_Mon_Cong[idx].setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnDanhSachMonAn_Mon_Cong[idx].setMinimumSize(new Dimension(41, 27));
        btnDanhSachMonAn_Mon_Cong[idx].setMaximumSize(new Dimension(41, 27));
        btnDanhSachMonAn_Mon_Cong[idx].setPreferredSize(new Dimension(41, 27));
        btnDanhSachMonAn_Mon_Cong[idx].setBounds(149, 11, 30, 25);
        panelEastCenter_Line[idx].add(btnDanhSachMonAn_Mon_Cong[idx]);
        ImageIcon iconAdd = new ImageIcon("img\\thucdon\\add_icon.png");
        Image imgAdd = iconAdd.getImage().getScaledInstance(btnDanhSachMonAn_Mon_Cong[idx].getWidth(), btnDanhSachMonAn_Mon_Cong[idx].getHeight(), Image.SCALE_SMOOTH);
        btnDanhSachMonAn_Mon_Cong[idx].setIcon(new ImageIcon(imgAdd));

        btnDanhSachMonAn_Mon_Tru[idx] = new JButton("");
        btnDanhSachMonAn_Mon_Tru[idx].setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnDanhSachMonAn_Mon_Tru[idx].setMinimumSize(new Dimension(41, 27));
        btnDanhSachMonAn_Mon_Tru[idx].setMaximumSize(new Dimension(41, 27));
        btnDanhSachMonAn_Mon_Tru[idx].setPreferredSize(new Dimension(41, 27));
        btnDanhSachMonAn_Mon_Tru[idx].setBounds(149, 36, 30, 19);
        panelEastCenter_Line[idx].add(btnDanhSachMonAn_Mon_Tru[idx]);
        ImageIcon iconMinus = new ImageIcon("img\\thucdon\\minus_icon.png");
        Image imgMinus = iconMinus.getImage().getScaledInstance(btnDanhSachMonAn_Mon_Tru[idx].getWidth(), btnDanhSachMonAn_Mon_Tru[idx].getHeight(), Image.SCALE_SMOOTH);
        btnDanhSachMonAn_Mon_Tru[idx].setIcon(new ImageIcon(imgMinus));

        lblDanhSachDatMon_Mon_GiaTong[idx] = new JLabel(orderedTotalPrices[idx]);
        lblDanhSachDatMon_Mon_GiaTong[idx].setPreferredSize(new Dimension(40, 30));
        lblDanhSachDatMon_Mon_GiaTong[idx].setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachDatMon_Mon_GiaTong[idx].setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDanhSachDatMon_Mon_GiaTong[idx].setBorder(null);
        lblDanhSachDatMon_Mon_GiaTong[idx].setBackground(new Color(64, 0, 0));
        lblDanhSachDatMon_Mon_GiaTong[idx].setBounds(211, 20, 91, 27);
        panelEastCenter_Line[idx].add(lblDanhSachDatMon_Mon_GiaTong[idx]);

        btnImgThungRacIcon_Mon[idx] = new JButton("");
        btnImgThungRacIcon_Mon[idx].setBounds(304, 15, 30, 33);
        btnImgThungRacIcon_Mon[idx].setOpaque(true);
        ImageIcon iconThungRac = new ImageIcon("img\\thucdon\\thung_rac_icon.png");
        Image imgThungRac = iconThungRac.getImage().getScaledInstance(btnImgThungRacIcon_Mon[idx].getWidth(), btnImgThungRacIcon_Mon[idx].getHeight(), Image.SCALE_SMOOTH);
        btnImgThungRacIcon_Mon[idx].setIcon(new ImageIcon(imgThungRac));
        panelEastCenter_Line[idx].add(btnImgThungRacIcon_Mon[idx]);
    }

    private void initializeOrderedItems() {
        
    }

 
    private static class MenuBuilderQuanLy {
        private final Font menuFont = new Font("Segoe UI", Font.BOLD, 14);
        private final Font menuItemFont = new Font("Segoe UI", Font.PLAIN, 13);
        private final Dimension menuItemSize = new Dimension(200, 30);
        private final Color menuForeground = Color.WHITE;
        private final Color menuItemBackground = new Color(220, 220, 220);

        private JMenu createMenu(String title) {
            JMenu menu = new JMenu(title);
            menu.setFont(menuFont);
            menu.setForeground(menuForeground);
            menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return menu;
        }

        private JMenuItem createMenuItem(String title, ActionListener actionListener) {
            JMenuItem menuItem = new JMenuItem(title);
            menuItem.setFont(menuItemFont);
            menuItem.setPreferredSize(menuItemSize);
            menuItem.setBackground(menuItemBackground);
            if (actionListener != null) {
                menuItem.addActionListener(actionListener);
            }
            return menuItem;
        }

        public JMenu createHeThongMenu() {
            JMenu menu = createMenu("Hệ thống");
            menu.add(createMenuItem("Màn hình chính", null));
            menu.add(createMenuItem("Thoát", e -> System.exit(0)));
            return menu;
        }

        public JMenu createThucDonMenu() {
            JMenu menu = createMenu("Thực đơn");
            menu.add(createMenuItem("Thêm món ăn", null));
            menu.add(createMenuItem("Cập nhật", null));
            menu.add(createMenuItem("Tra cứu", null));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createKhuVucMenu() {
            JMenu menu = createMenu("Khu vực");
            menu.add(createMenuItem("Xem danh sách", null));
            menu.add(createMenuItem("Thêm khu vực mới", null));
            menu.add(createMenuItem("Cập nhật", null));
            return menu;
        }

        public JMenu createCaLamMenu() {
            JMenu menu = createMenu("Ca làm");
            menu.add(createMenuItem("Tra cứu", null));
            menu.add(createMenuItem("Phân ca", null));
            menu.add(createMenuItem("Thêm ca mới", null));
            return menu;
        }

        public JMenu createNhanVienMenu() {
            JMenu menu = createMenu("Nhân viên");
            menu.add(createMenuItem("Xem danh sách", null));
            menu.add(createMenuItem("Cập nhật", null));
            menu.add(createMenuItem("Phân quyền", null));
            menu.add(createMenuItem("Thống kê", null));
            return menu;
        }

        public JMenu createDoanhThuMenu() {
            JMenu menu = createMenu("Doanh thu");
            menu.add(createMenuItem("Thống kê trong ngày", null));
            menu.add(createMenuItem("Thống kê theo tuần", null));
            return menu;
        }
    }
}