/*
 * @ (#) KhuyenMai.java   1.0   9/20/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

import java.time.LocalDateTime;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double phanTramGiam;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String loaiKhuyenMai;
    private boolean trangThai; // Dang ap dung, Het han

    public KhuyenMai() {
    }
    public KhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }
    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double phanTramGiam, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, String loaiKhuyenMai, boolean trangThai) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.trangThai = trangThai;
    }
    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }
    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }
    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }
    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }
    public double getPhanTramGiam() {
        return phanTramGiam;
    }
    public void setPhanTramGiam(double phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }
    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }
    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }
    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }
    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
    public String getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }
    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }
    public boolean isTrangThai() {
        return trangThai;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }



}
