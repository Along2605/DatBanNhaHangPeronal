package gui;

import javax.swing.*;
import java.awt.event.*;
import connectDB.ConnectDB;

public abstract class BaseFrame extends JFrame {
    public BaseFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    BaseFrame.this,
                    "Bạn có chắc muốn thoát không?",
                    "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    ConnectDB.getInstance().close();  // đóng kết nối DB
                    System.exit(0); // thoát ứng dụng
                }
            }
        });
    }
}
