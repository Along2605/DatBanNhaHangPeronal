package gui;


import javax.swing.*;
import javax.swing.border.*;

import entity.BanAn;

import java.awt.*;

public class DialogChiTietBanAn extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color MAIN_COLOR = new Color(41, 128, 185);

    public DialogChiTietBanAn(Frame parent, BanAn banAn) {
        super(parent, "Chi tiết bàn ăn", true);
        setSize(650, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // === Panel chính ===
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // === Tiêu đề ===
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT BÀN ĂN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(MAIN_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // === Panel thông tin ===
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        addInfoRow(infoPanel, gbc, 0, "Mã bàn:", banAn.getMaBan());
        addInfoRow(infoPanel, gbc, 1, "Tên bàn:", banAn.getTenBan());
        addInfoRow(infoPanel, gbc, 2, "Số lượng chỗ:", String.valueOf(banAn.getSoLuongCho()));
        addInfoRow(infoPanel, gbc, 3, "Loại bàn:", banAn.getLoaiBan().getTenLoaiBan() != null ? banAn.getLoaiBan().getTenLoaiBan() : "Chưa xác định");
        addInfoRow(infoPanel, gbc, 4, "Khu vực:",
                banAn.getKhuVuc() != null && banAn.getKhuVuc().getTenKhuVuc() != null
                        ? banAn.getKhuVuc().getTenKhuVuc()
                        : "Chưa xác định");

        // === Trạng thái ===
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel lblTrangThaiLabel = new JLabel("Trạng thái:");
        lblTrangThaiLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(lblTrangThaiLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel lblTrangThaiValue = new JLabel(
                banAn.getTrangThai() != null ? banAn.getTrangThai() : "Chưa xác định");
        lblTrangThaiValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTrangThaiValue.setOpaque(true);
        lblTrangThaiValue.setBorder(new EmptyBorder(5, 10, 5, 10));

        switch (banAn.getTrangThai()) {
            case "Trống":
                lblTrangThaiValue.setBackground(new Color(76, 175, 80));
                lblTrangThaiValue.setForeground(Color.WHITE);
                break;
            case "Đang sử dụng":
                lblTrangThaiValue.setBackground(new Color(244, 67, 54));
                lblTrangThaiValue.setForeground(Color.WHITE);
                break;
            case "Đã đặt":
                lblTrangThaiValue.setBackground(new Color(255, 152, 0));
                lblTrangThaiValue.setForeground(Color.WHITE);
                break;
            case "Đang dọn":
                lblTrangThaiValue.setBackground(new Color(158, 158, 158));
                lblTrangThaiValue.setForeground(Color.WHITE);
                break;
            default:
                lblTrangThaiValue.setBackground(Color.WHITE);
        }
        infoPanel.add(lblTrangThaiValue, gbc);

        // === Ghi chú ===
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        JLabel lblGhiChuLabel = new JLabel("Ghi chú:");
        lblGhiChuLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGhiChuLabel.setVerticalAlignment(JLabel.TOP);
        infoPanel.add(lblGhiChuLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.gridheight = 2;
        JTextArea txtGhiChu = new JTextArea(banAn.getGhiChu() != null ? banAn.getGhiChu() : "");
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setEditable(false);
        txtGhiChu.setBackground(new Color(245, 245, 245));
        txtGhiChu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setPreferredSize(new Dimension(0, 60));
        infoPanel.add(scrollGhiChu, gbc);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // === Panel nút ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnDong = createButton("Đóng", new Color(100, 100, 100));
        btnDong.addActionListener(e -> dispose());
        buttonPanel.add(btnDong);

        // Nút chức năng tùy theo trạng thái
        if ("Trống".equals(banAn.getTrangThai())) {
            JButton btnDatBan = createButton("Đặt bàn", MAIN_COLOR);
            btnDatBan.addActionListener(e -> {
                dispose(); // đóng dialog hiện tại
                
                JFrame frameDatBan = new JFrame("Đặt bàn");
                frameDatBan.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameDatBan.setSize(1000, 700);
                frameDatBan.setLocationRelativeTo(parent);

                // Thêm JPanel DatBan vào frame
                frameDatBan.setContentPane(new DatBan());

                frameDatBan.setVisible(true);
            });
            buttonPanel.add(btnDatBan, 0);
        } else if ("Đang sử dụng".equals(banAn.getTrangThai())) {
            JButton btnXemHoaDon = createButton("Xem hóa đơn", MAIN_COLOR);
            btnXemHoaDon.addActionListener(e -> {
                dispose();                
                new DialogChiTietHoaDon(parent, banAn).setVisible(true);
            });
            
            buttonPanel.add(btnXemHoaDon, 0);
        } else if ("Đã đặt".equals(banAn.getTrangThai())) {
            JButton btnXemPhieuDat = createButton("Xem phiếu đặt", MAIN_COLOR);
            btnXemPhieuDat.addActionListener(e -> {
                dispose();
//                JOptionPane.showMessageDialog(parent,
//                        "Chức năng xem phiếu đặt sẽ được cài đặt sau!",
//                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                new DialogChiTietPhieuDat(parent, banAn).setVisible(true);
            });
            buttonPanel.add(btnXemPhieuDat, 0);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
    
    
    // thêm 1 dòng thông tin vào panel
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(val, gbc);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
