/*
 * @ (#) TaiKhoan.java   1.0   9/18/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

public class TaiKhoan {
    private String userName;
    private String passWord;
    private NhanVien nhanVien; // Mã nhân viên
    private String quyenTruyCap; // quanly, letan, phucvu

    public TaiKhoan() {
    }
    public TaiKhoan(String userName) {
        this.userName = userName;
    }
    public TaiKhoan(String userName, String passWord, NhanVien nhanVien, String quyenTruyCap) {
        this.userName = userName;
        this.passWord = passWord;
        this.nhanVien = nhanVien;
        this.quyenTruyCap = quyenTruyCap;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    public String getQuyenTruyCap() {
        return quyenTruyCap;
    }
    public void setQuyenTruyCap(String quyenTruyCap) {
        this.quyenTruyCap = quyenTruyCap;
    }


}
