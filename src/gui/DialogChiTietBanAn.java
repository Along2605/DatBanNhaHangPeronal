package gui;

import javax.swing.*;

import javax.swing.border.*;
import dao.BanAnDAO;
import dao.PhieuDatBanDAO;
import entity.BanAn;
import entity.PhieuDatBan;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

//Callback để refresh màn hình gốc
interface RefreshCallback {
	void onRefresh();
}

public class DialogChiTietBanAn extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color MAIN_COLOR = new Color(41, 128, 185);

    private BanAnCallback chuyenBanCallback;
    private RefreshCallback refreshCallback;
    
    private LocalDate ngayXem;
    private int khungGio;

    
    public DialogChiTietBanAn(Frame parent, BanAn banAn, LocalDate ngayXem, int khungGio, 
    		RefreshCallback refreshCallback) {
    	this(parent, banAn, ngayXem, khungGio, null, refreshCallback);
    }

    public DialogChiTietBanAn(Frame parent, BanAn banAn, LocalDate ngayXem, int khungGio, BanAnCallback chuyenBanCallback, RefreshCallback refreshCallback) {
        super(parent, "Chi tiết bàn ăn", true);
        this.chuyenBanCallback = chuyenBanCallback;
        this.refreshCallback = refreshCallback;
        this.ngayXem = ngayXem;
        this.khungGio = khungGio;

        setSize(650, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT BÀN ĂN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(MAIN_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        addInfoRow(infoPanel, gbc, 0, "Mã bàn:", banAn.getMaBan());
        addInfoRow(infoPanel, gbc, 1, "Tên bàn:", banAn.getTenBan());
        addInfoRow(infoPanel, gbc, 2, "Số lượng chỗ:", String.valueOf(banAn.getSoLuongCho()));
        addInfoRow(infoPanel, gbc, 3, "Loại bàn:", 
            banAn.getLoaiBan().getTenLoaiBan() != null ? banAn.getLoaiBan().getTenLoaiBan() : "Chưa xác định");
        addInfoRow(infoPanel, gbc, 4, "Khu vực:", 
            banAn.getKhuVuc() != null && banAn.getKhuVuc().getTenKhuVuc() != null ? 
            banAn.getKhuVuc().getTenKhuVuc() : "Chưa xác định");

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
        JLabel lblTrangThaiLabel = new JLabel("Trạng thái:");
        lblTrangThaiLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(lblTrangThaiLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        JLabel lblTrangThaiValue = new JLabel(banAn.getTrangThai() != null ? banAn.getTrangThai() : "Chưa xác định");
        lblTrangThaiValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTrangThaiValue.setOpaque(true);
        lblTrangThaiValue.setBorder(new EmptyBorder(5, 10, 5, 10));

        switch (banAn.getTrangThai()) {
            case "Trống" -> { 
                lblTrangThaiValue.setBackground(new Color(76, 175, 80)); 
                lblTrangThaiValue.setForeground(Color.WHITE); 
            }
            case "Đang sử dụng" -> { 
                lblTrangThaiValue.setBackground(new Color(244, 67, 54)); 
                lblTrangThaiValue.setForeground(Color.WHITE); 
            }
            case "Đã đặt" -> { 
                lblTrangThaiValue.setBackground(new Color(255, 152, 0)); 
                lblTrangThaiValue.setForeground(Color.WHITE); 
            }
            case "Đang dọn", "Bảo trì" -> { 
                lblTrangThaiValue.setBackground(new Color(158, 158, 158)); 
                lblTrangThaiValue.setForeground(Color.WHITE); 
            }
            default -> lblTrangThaiValue.setBackground(Color.WHITE);
        }
        infoPanel.add(lblTrangThaiValue, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.3;
        JLabel lblGhiChuLabel = new JLabel("Ghi chú:");
        lblGhiChuLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGhiChuLabel.setVerticalAlignment(JLabel.TOP);
        infoPanel.add(lblGhiChuLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7; gbc.gridheight = 2;
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnDong = createButton("Đóng", new Color(100, 100, 100));
        btnDong.addActionListener(e -> dispose());
        buttonPanel.add(btnDong);

        String trangThai = banAn.getTrangThai();

        if ("Trống".equals(trangThai)) {
            JButton btnDatBan = createButton("Đặt bàn", MAIN_COLOR);
            btnDatBan.addActionListener(e -> {
                dispose();
//                JFrame frameDatBan = new JFrame("Đặt bàn - " + banAn.getTenBan());
//                frameDatBan.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                frameDatBan.setSize(1000, 700);
//                frameDatBan.setLocationRelativeTo(parent);
//                frameDatBan.setContentPane(new DatBan());
//                frameDatBan.setVisible(true);
                 new DialogDatBan(parent, banAn).setVisible(true);
            });
            buttonPanel.add(btnDatBan, 0);
        }

        else if ("Đang sử dụng".equals(trangThai)) {
            JButton btnXemHoaDon = createButton("Xem hóa đơn", MAIN_COLOR);
            btnXemHoaDon.addActionListener(e -> {
                dispose();
                new DialogChiTietHoaDon(parent, banAn).setVisible(true);
            });
            buttonPanel.add(btnXemHoaDon, 0);

            if (chuyenBanCallback != null) {
                JButton btnChuyenBan = createButton("Chuyển bàn từ đây", new Color(231, 76, 60));
                btnChuyenBan.addActionListener(e -> {
                    chuyenBanCallback.onBanSelected(banAn, null, true); //chọn được bàn
                    dispose();
                });
                buttonPanel.add(btnChuyenBan, 1);

                JButton btnGopBan = createButton("Gộp bàn vào đây", new Color(46, 125, 50));
                btnGopBan.addActionListener(e -> {
                    chuyenBanCallback.onBanSelected(banAn, null, true); //
                    dispose();
                });
                buttonPanel.add(btnGopBan, 2);
            }
        
        }
        
        // XỬ LÝ BÀN ĐÃ ĐẶT (Có phiếu đặt)
        else if ("Đã đặt".equals(trangThai)) {
            JButton btnXemPhieuDat = createButton("Xem phiếu đặt", MAIN_COLOR);
            btnXemPhieuDat.addActionListener(e -> {
                dispose();
                PhieuDatBanDAO phieuDatDAO = new PhieuDatBanDAO();
                
                PhieuDatBan phieuDat = null;
                List<String> dsBan = null;
                
                // Ưu tiên lấy phiếu theo ngày giờ
                if (ngayXem != null && khungGio > 0) {
                    phieuDat = phieuDatDAO.getPhieuDatTheoMaBan_NgayGio(
                        banAn.getMaBan(), ngayXem, khungGio
                    );
                }
                
                // Fallback: lấy phiếu hiện tại
                if (phieuDat == null) {
                    phieuDat = phieuDatDAO.getPhieuDatTheoBan(banAn.getMaBan());
                }
                
                if (phieuDat != null) {
                    dsBan = phieuDatDAO.getDanhSachBanTheoPhieuDat(phieuDat.getMaPhieuDat());
                    new DialogChiTietPhieuDat(parent, banAn, phieuDat, dsBan).setVisible(true);
                } else {
                    // Không tìm thấy phiếu đặt -> đồng bộ trạng thái
                    int choice = JOptionPane.showConfirmDialog(parent,
                        "⚠️ Không tìm thấy phiếu đặt cho bàn này.\n\n" +
                        "Bạn có muốn cập nhật trạng thái bàn về 'Trống' không?",
                        "Đồng bộ trạng thái",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (choice == JOptionPane.YES_OPTION) {
                        BanAnDAO banDAO = new BanAnDAO();
                        if (banDAO.capNhatTrangThaiBan(banAn.getMaBan(), "Trống")) {
                            JOptionPane.showMessageDialog(parent,
                                "✅ Đã cập nhật trạng thái bàn về 'Trống'.",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(parent,
                                "❌ Không thể cập nhật trạng thái bàn!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            buttonPanel.add(btnXemPhieuDat, 0);
        }
        
        // XỬ LÝ TRẠNG THÁI KHÁC
        else if (trangThai != null && trangThai.contains("(hôm nay)")) {
            JLabel lblNote = new JLabel("Bàn này đang bận hôm nay, sẽ trống vào ngày đã chọn");
            lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblNote.setForeground(new Color(100, 100, 100));
            JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            notePanel.setBackground(Color.WHITE);
            notePanel.add(lblNote);
            mainPanel.add(notePanel, BorderLayout.CENTER);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void moFormDatBan(String maBan) {
        try {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            
            // Tạo dialog đặt bàn
            JDialog dialog = new JDialog(parentFrame, "Đặt bàn", true);
            dialog.setSize(1200, 700);
            dialog.setLocationRelativeTo(parentFrame);
            
            // Tạo panel DatBan
            DatBan panelDatBan = new DatBan();
            
            // ✅ TỰ ĐỘNG ĐIỀN THÔNG TIN
            panelDatBan.tuDongDienThongTin(maBan, ngayXem, khungGio);
            
            dialog.add(panelDatBan);
            dialog.setVisible(true);
            
            // ✅ SAU KHI ĐÓNG DIALOG ĐẶT BÀN
            dispose(); // Đóng dialog chi tiết
            
            // Gọi callback refresh
            if (refreshCallback != null) {
                refreshCallback.onRefresh();
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi mở form đặt bàn: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

	// ===================================
    // Constructor cũ (backward compatible)
    // ===================================
    public DialogChiTietBanAn(Frame parent, BanAn banAn) {
        this(parent, banAn, null, 0, null, null);
    }

    public DialogChiTietBanAn(Frame parent, BanAn banAn, BanAnCallback callback) {
        this(parent, banAn, null, 0, callback, null);
    }
    
    public DialogChiTietBanAn(Frame parent, BanAn banAn, LocalDate ngayXem, int khungGio) {
        this(parent, banAn, ngayXem, khungGio, null, null);
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
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
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}