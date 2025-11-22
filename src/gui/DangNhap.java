package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import connectDB.ConnectDB;

public class DangNhap extends JFrame implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUser;
    private JPasswordField txtPass;
	private JButton btnLogin;
	private JButton btnDangNhapNhanh;

    public DangNhap() {
        setTitle("Đăng nhập - Seoul Cuisine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính chia 2 phần
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        add(mainPanel);

        // ===== Panel bên trái: hình ảnh và logo =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Logo giữa màn hình
        JLabel logo = new JLabel(new ImageIcon("img/logo_nhahang.png"));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(logo, BorderLayout.CENTER);

        mainPanel.add(leftPanel);

        // ===== Panel bên phải: form login =====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(200, 80, 60));
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nhãn xin chào
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel lblHello = new JLabel("XIN CHÀO", SwingConstants.CENTER);
        lblHello.setFont(new Font("SansSerif", Font.BOLD, 35));
        lblHello.setForeground(Color.WHITE);
        rightPanel.add(lblHello, gbc);

        // Nhãn phụ
        JLabel lblSub = new JLabel("Đăng nhập tài khoản của bạn", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 23));
        lblSub.setForeground(Color.WHITE);

        gbc.gridy = 1;
        rightPanel.add(lblSub, gbc);

        // User Name
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblUser = new JLabel("User Name");
        lblUser.setForeground(Color.WHITE);
        rightPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        txtUser = new JTextField(20);
        txtUser.setBackground(Color.LIGHT_GRAY);
        txtUser.setPreferredSize(new Dimension(200, 35));
        txtUser.setFont(new Font("SansSerif", Font.PLAIN, 18));
        rightPanel.add(txtUser, gbc);

        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblPass = new JLabel("Password");
        lblPass.setForeground(Color.WHITE);
        rightPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        txtPass = new JPasswordField(20);
        txtPass.setBackground(Color.LIGHT_GRAY);
        txtPass.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtPass.setPreferredSize(new Dimension(200, 35));

        // Load icons trực tiếp từ đường dẫn
        ImageIcon showIcon = new ImageIcon(new ImageIcon("img/view.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        ImageIcon hideIcon = new ImageIcon(new ImageIcon("img/hide.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        // Tạo nút với icon mặc định là "ẩn" (hide)
        JButton btnToggle = new JButton(hideIcon);
        btnToggle.setPreferredSize(new Dimension(30, 30));
        btnToggle.setBorderPainted(false);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setOpaque(false);

        // Trạng thái: có đang ẩn hay không
        final boolean[] isHidden = {true}; // dùng mảng để thay đổi bên trong lambda

        btnToggle.addActionListener((ActionEvent e) -> {
            if (isHidden[0]) {
                txtPass.setEchoChar((char) 0); // Hiện mật khẩu
                btnToggle.setIcon(showIcon);   // Đổi icon sang "show"
            } else {
                txtPass.setEchoChar('\u2022'); // Ẩn mật khẩu (dấu chấm tròn)
                btnToggle.setIcon(hideIcon);   // Đổi icon sang "hide"
            }
            isHidden[0] = !isHidden[0]; // Đảo trạng thái
        });

        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.add(txtPass, BorderLayout.CENTER);
        passPanel.add(btnToggle, BorderLayout.EAST);
        rightPanel.add(passPanel, gbc);

        // Nút đăng nhập
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(Color.GREEN);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 18));
        rightPanel.add(btnLogin, gbc);

     // ===== Nút đăng nhập nhanh =====
     gbc.gridy = 5;
     gbc.gridx = 1;

     btnDangNhapNhanh = new JButton("Đăng nhập nhanh");
     btnDangNhapNhanh.setBackground(Color.YELLOW);
     btnDangNhapNhanh.setFont(new Font("SansSerif", Font.BOLD, 16));
     rightPanel.add(btnDangNhapNhanh, gbc);

     btnDangNhapNhanh.addActionListener(e -> {
         txtUser.setText("admin");
         txtPass.setText("admin123");
         btnLogin.doClick();
     });

     btnLogin.addActionListener(this);
        mainPanel.add(rightPanel);
              
            
    }
    


    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(() -> new DangNhap().setVisible(true));
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== btnLogin) {
			String userName= txtUser.getText().trim();
			String passWord= new String(txtPass.getPassword()).trim();
			
			if(userName.isEmpty() || passWord.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ userName và passWord", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			try {
				String sql = "SELECT maNV, quyenTruyCap FROM TaiKhoan WHERE userName = ? AND passWord = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, userName);
                stmt.setString(2, passWord);
                ResultSet rs = stmt.executeQuery();
                
                
                if (rs.next()) {
                	String maNV= rs.getString("maNv");
                    String quyen = rs.getString("quyenTruyCap");
                    
                    
                    // ✅ LẤY THÔNG TIN NHÂN VIÊN VÀ LƯU VÀO SESSION
                    dao.NhanVienDAO nhanVienDAO = new dao.NhanVienDAO();
                    entity.NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(maNV);
                    
                    if (nhanVien != null) {
                        // Lưu vào Session
                        util.Session.setNhanVienDangNhap(nhanVien);
                        
                        System.out.println("✅ Đăng nhập thành công: " + nhanVien.getHoTen());
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Không tìm thấy thông tin nhân viên!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        rs.close();
                        stmt.close();
                        return;
                    }
                    
                    
                                   
                    // Đóng cửa sổ đăng nhập
                    this.dispose();
                    
                    // Chuyển hướng đến màn hình tương ứng
                    SwingUtilities.invokeLater(() -> {
                        if ("Quản lý".equalsIgnoreCase(quyen)) {
                            new ManHinhChinhQuanLy().setVisible(true);
                        } else if ("Nhân viên".equalsIgnoreCase(quyen)) {
                            new ManHinhChinhNhanVien().setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Quyền truy cập không hợp lệ!", 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }else {
                	JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
                rs.close();
                stmt.close();
			} 
			
			
			catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra đăng nhập: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
		
	}
}