package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ManHinhDSNhanVien extends JPanel {
    private JTextField txtTimKiem;
    private JComboBox<String> cbLocChucVu;
    private JComboBox<String> cbLocTrangThai;
    private JButton btnTimKiem;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JScrollPane scroll;

    public ManHinhDSNhanVien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.decode("#EAF1F9"));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
     

        // Title
        JLabel lblTitle = new JLabel("DANH SÁCH NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.decode("#333333"));
        add(lblTitle, BorderLayout.NORTH);

        // Filter panel
        JPanel pnFilter = new JPanel(new GridBagLayout());
        pnFilter.setBackground(Color.decode("#EAF1F9"));
        pnFilter.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
            "Tìm kiếm & Lọc",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.PLAIN, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Initialize filter components
        txtTimKiem = createTextField(true);
        cbLocChucVu = new JComboBox<>(new String[]{"Tất cả", "Quản lý", "Nhân viên phục vụ", "Nhân viên bếp", "Thu ngân"});
        cbLocTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang làm việc", "Nghỉ việc"});
        btnTimKiem = createButton("Tìm kiếm", "img/search.png");

        // Filter layout
        addFilterRow(pnFilter, gbc, 0, "Tìm kiếm:", txtTimKiem, "Chức vụ:", cbLocChucVu);
        addFilterRow(pnFilter, gbc, 1, "Trạng thái:", cbLocTrangThai, null, btnTimKiem);
       
       

        // Button panel
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnButtons.setBackground(Color.decode("#EAF1F9"));
        btnThem = createButton("Thêm", "img/add.png");
        btnSua = createButton("Sửa", "img/edit.png");
        btnXoa = createButton("Xóa", "img/delete.png");
        btnLamMoi = createButton("Làm mới", "img/refresh.png");
        pnButtons.add(btnThem);
        pnButtons.add(btnSua);
        pnButtons.add(btnXoa);
        pnButtons.add(btnLamMoi);

        add(pnButtons, BorderLayout.SOUTH);

        // Table
        String[] headers = {"Mã NV", "Họ tên", "Ngày sinh", "Email", "SĐT", "Giới tính", "Chức vụ", "Ngày vào làm", "Trạng thái"};
        scroll = new JScrollPane(new JTable(new DefaultTableModel(headers, 0)));
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
            "Danh sách nhân viên",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.PLAIN, 16)
        ));
        
        
        // Tạo panel chứa filter bên trái
        JPanel pnFilterWrapper = new JPanel(new BorderLayout());
        pnFilterWrapper.add(pnFilter, BorderLayout.NORTH); // giữ nguyên bố cục của filter

        // SplitPane chia trái phải
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnFilterWrapper, scroll);
        splitPane.setResizeWeight(0.3); // 30% cho panel trái, 70% cho panel phải
        splitPane.setDividerSize(4); // kích thước thanh chia
        splitPane.setBorder(null); // không viền

        add(splitPane, BorderLayout.CENTER);
        
    }

    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setEditable(editable);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(Color.decode("#4CAF50"));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        }
        return button;
    }
    


    private void addFilterRow(JPanel panel, GridBagConstraints gbc, int row, 
                            String label1, Component comp1, String label2, Component comp2) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl1 = new JLabel(label1);
        lbl1.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl1, gbc);

        gbc.gridx = 1;
        panel.add(comp1, gbc);

        if (label2 != null && comp2 != null) {
            gbc.gridx = 2;
            JLabel lbl2 = new JLabel(label2);
            lbl2.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(lbl2, gbc);

            gbc.gridx = 3;
            panel.add(comp2, gbc);
        }
    }
    
    
}