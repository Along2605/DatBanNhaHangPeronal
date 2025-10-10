package gui;

import javax.swing.SwingUtilities;

import connectDB.ConnectDB;

public class App {
	public static void main(String[] args) {
		ConnectDB.getInstance().connect();
		SwingUtilities.invokeLater(() -> {
//            new DangNhap().setVisible(true);
			new ManHinhChinhNhanVien().setVisible(true);
        });
	} 
}
