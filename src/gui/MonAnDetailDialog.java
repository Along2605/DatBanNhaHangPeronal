// File: gui/MonAnDetailDialog.java
package gui;

import entity.MonAn;
import dao.MonAnDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MonAnDetailDialog extends JDialog {

    public MonAnDetailDialog(Frame owner, MonAn mon) {
        super(owner, "Chi tiết món ăn: " + mon.getTenMon(), true);
        setSize(620, 760);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBorder(new EmptyBorder(20, 30, 20, 30));
        main.setBackground(Color.WHITE);

        // ==================== ẢNH MÓN ĂN ====================
        JLabel lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(560, 300));
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        lblAnh.setBackground(Color.WHITE);
        lblAnh.setOpaque(true);

        if (mon.getHinhAnh() != null && !mon.getHinhAnh().trim().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(mon.getHinhAnh());
                Image img = icon.getImage().getScaledInstance(560, 300, Image.SCALE_SMOOTH);
                lblAnh.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblAnh.setText("Không tải được ảnh");
                lblAnh.setForeground(Color.GRAY);
                lblAnh.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            }
        } else {
            lblAnh.setText("Chưa có ảnh món ăn");
            lblAnh.setForeground(Color.GRAY);
            lblAnh.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        }

        // ==================== THÔNG TIN CHÍNH - CHIA 2 CỘT ====================
        JPanel infoContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        infoContainer.setBackground(Color.WHITE);

        JPanel leftPanel = createInfoColumn(mon, true);
        JPanel rightPanel = createInfoColumn(mon, false);

        infoContainer.add(leftPanel);
        infoContainer.add(rightPanel);

        infoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                " Thông tin chi tiết ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(52, 152, 219)
        ));

        // ==================== MÔ TẢ ====================
        JTextArea txtMoTa = new JTextArea(mon.getMoTa() != null && !mon.getMoTa().trim().isEmpty()
                ? mon.getMoTa() : "Không có mô tả cho món ăn này.");
        txtMoTa.setEditable(false);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMoTa.setBackground(new Color(250, 252, 255));
        txtMoTa.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setPreferredSize(new Dimension(0, 100));
        scrollMoTa.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                " Mô tả món ăn ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(80, 80, 80)
        ));

        // ==================== GOM LẠI ====================
        main.add(lblAnh, BorderLayout.NORTH);
        main.add(infoContainer, BorderLayout.CENTER);
        main.add(scrollMoTa, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        // ==================== NÚT ĐÓNG ====================
        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setBackground(new Color(231, 76, 60));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setPreferredSize(new Dimension(110, 38));
        btnDong.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(10, 0, 15, 25));
        bottom.add(btnDong);

        add(bottom, BorderLayout.SOUTH);
    }

    // Tạo 1 cột thông tin (trái hoặc phải)
    private JPanel createInfoColumn(MonAn mon, boolean isLeft) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.insets = new Insets(10, 15, 10, 10);
        gbcLabel.gridx = 0;
        gbcLabel.weightx = 0.5;

        GridBagConstraints gbcValue = new GridBagConstraints();
        gbcValue.anchor = GridBagConstraints.WEST;
        gbcValue.insets = new Insets(10, 0, 10, 15);
        gbcValue.gridx = 1;
        gbcValue.weightx = 0.5;
        gbcValue.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        MonAnDAO dao = new MonAnDAO();
        String tenLoai = "Không xác định";
        try {
            tenLoai = dao.chuyenDoiMaLoaiSangTen(mon.getLoaiMon());
        } catch (Exception ignored) {}

        if (isLeft) {
            // Cột trái
            addInfoRow(panel, gbcLabel, gbcValue, "Mã món ăn:", mon.getMaMon(), labelFont, valueFont);
            addInfoRow(panel, gbcLabel, gbcValue, "Tên món ăn:", mon.getTenMon(), labelFont, valueFont);
            addInfoRow(panel, gbcLabel, gbcValue, "Giá bán:", String.format("%,.0f ₫", mon.getGia()), labelFont, valueFont);
            addInfoRow(panel, gbcLabel, gbcValue, "Đơn vị tính:", mon.getDonViTinh(), labelFont, valueFont);
        } else {
            // Cột phải
            addInfoRow(panel, gbcLabel, gbcValue, "Loại món:", tenLoai, labelFont, valueFont);

            JLabel lblTrangThai = new JLabel(mon.isTrangThai() ? "Đang bán" : "Ngừng bán");
            lblTrangThai.setFont(valueFont);
            lblTrangThai.setForeground(mon.isTrangThai() ? new Color(0, 130, 0) : new Color(200, 0, 0));
            addInfoRow(panel, gbcLabel, gbcValue, "Trạng thái:", lblTrangThai, labelFont);

            addInfoRow(panel, gbcLabel, gbcValue, "Tồn kho:", mon.getSoLuong() + " " + mon.getDonViTinh(), labelFont, valueFont);
        }

        return panel;
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbcLabel, GridBagConstraints gbcValue,
                            String labelText, String valueText, Font labelFont, Font valueFont) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(labelFont);
        JLabel val = new JLabel(valueText);
        val.setFont(valueFont);

        gbcLabel.gridy = GridBagConstraints.RELATIVE;
        gbcValue.gridy = GridBagConstraints.RELATIVE;

        panel.add(lbl, gbcLabel);
        panel.add(val, gbcValue);
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbcLabel, GridBagConstraints gbcValue,
                            String labelText, JLabel valueLabel, Font labelFont) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(labelFont);

        gbcLabel.gridy = GridBagConstraints.RELATIVE;
        gbcValue.gridy = GridBagConstraints.RELATIVE;

        panel.add(lbl, gbcLabel);
        panel.add(valueLabel, gbcValue);
    }
}