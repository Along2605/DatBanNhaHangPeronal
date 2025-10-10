package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

public class TraCuuMonAn extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public TraCuuMonAn() {
        setLayout(new BorderLayout(10,10));
        setBackground(Color.decode("#EAF1F9"));
        JLabel lblTitle = new JLabel("DANH SÁCH MÓN ĂN ĐANG KINH DOANH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(16,0,8,0));
        add(lblTitle, BorderLayout.NORTH);

        // Tạo bảng
        String[] cols = {"Hình ảnh", "Mã món", "Tên món", "Loại", "Giá", "Trạng thái", "Số lượng", "ĐVT", "Mô tả"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class;
                if (column == 4) return Double.class;
                if (column == 6) return Integer.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(90);  // Để hình ảnh lớn và rõ

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        // Ô dữ liệu style
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(174, 214, 241));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 1));
        add(scrollPane, BorderLayout.CENTER);

        // ---- VỊ TRÍ LOAD DỮ LIỆU MÓN ĂN TỪ DATABASE ----
        // List<MonAn> dsMonAn = ... // lấy từ database
        // for (MonAn mon : dsMonAn) {
        //     model.addRow(new Object[]{
        //         mon.hinhAnh, mon.ma, mon.ten, mon.loai, mon.gia, mon.trangThai, mon.soLuong, mon.dvt, mon.moTa
        //     });
        // }

        // ---- Dưới đây là ví dụ dữ liệu mẫu (có thể xóa) ----
        // ImageIcon img = new ImageIcon("img/pho.png");
        // ImageIcon imgScaled = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        // model.addRow(new Object[]{imgScaled, "MA001", "Phở bò", "Món nước", 45000.0, "Còn phục vụ", 100, "Tô", "Phở bò truyền thống"});
    }
}