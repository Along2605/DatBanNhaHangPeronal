/*
 * @ (#) MonAn.java   1.0   9/18/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

public class MonAn {
    private String maMon;
    private  String tenMon;
    private double gia;
    private String donViTinh;
    private boolean trangThai;
    private String hinhAnh;
    private int soLuong;
    private String moTa;
    private String loaiMon;

    public MonAn() {
    }
    public MonAn(String maMon) {
        this.maMon = maMon;
    }
    public MonAn(String maMon, String tenMon, double gia, String donViTinh, boolean trangThai, String hinhAnh, int soLuong, String moTa, String loaiMon) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.gia = gia;
        this.donViTinh = donViTinh;
        this.trangThai = trangThai;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.loaiMon = loaiMon;
    }
    public String getMaMon() {
        return maMon;
    }
    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }
    public String getTenMon() {
        return tenMon;
    }
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }
    public double getGia() {
        return gia;
    }
    public void setGia(double gia) {
        this.gia = gia;
    }
    public String getDonViTinh() {
        return donViTinh;
    }
    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }
    public boolean isTrangThai() {
        return trangThai;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    public String getHinhAnh() {
        return hinhAnh;
    }
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public String getMoTa() {
        return moTa;
    }
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    public String getLoaiMon() {
        return loaiMon;
    }
    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
    }
    

}
