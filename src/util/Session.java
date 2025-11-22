package util;

import entity.NhanVien;

public class Session {
    // Lưu thông tin nhân viên đang đăng nhập
    public static NhanVien nhanVienDangNhap = null;
    
    public static void setNhanVienDangNhap(NhanVien nv) {
        nhanVienDangNhap = nv;
    }
    
   
    public static NhanVien getNhanVienDangNhap() {
        return nhanVienDangNhap;
    }
    
    
    public static String getMaNhanVienDangNhap() {
        return nhanVienDangNhap != null ? nhanVienDangNhap.getMaNV() : null;
    }
    
   
   
    public static boolean isLoggedIn() {
        return nhanVienDangNhap != null;
    }
    
    
    public static void logout() {
        nhanVienDangNhap = null;
    }
}