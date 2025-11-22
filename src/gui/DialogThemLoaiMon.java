
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogThemLoaiMon extends JDialog {
    private JTextField txtMaLoai, txtTenLoai;
    private JButton btnLuu, btnHuy;
    private boolean saved = false;
    private String maLoai, tenLoai;

    public DialogThemLoaiMon(Frame parent) {
        super(parent, "Thêm loại món ăn", true);
        setSize(360, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel pnMain = new JPanel(new GridBagLayout());
        pnMain.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnMain.add(new JLabel("Mã loại:"), gbc);
        gbc.gridx = 1;
        txtMaLoai = new JTextField(15);
        pnMain.add(txtMaLoai, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnMain.add(new JLabel("Tên loại:"), gbc);
        gbc.gridx = 1;
        txtTenLoai = new JTextField(15);
        pnMain.add(txtTenLoai, gbc);

        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnButton.add(btnLuu);
        pnButton.add(btnHuy);

        add(pnMain, BorderLayout.CENTER);
        add(pnButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> luu());
        btnHuy.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saved = false;
            }
        });
    }

    private void luu() {
        maLoai = txtMaLoai.getText().trim();
        tenLoai = txtTenLoai.getText().trim();
        if (maLoai.isEmpty() || tenLoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ!");
            return;
        }
        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }
    public String getMaLoai() { return maLoai; }
    public String getTenLoai() { return tenLoai; }
}