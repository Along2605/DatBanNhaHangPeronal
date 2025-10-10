/*
 * @ (#) PhieuDatBan.java   1.0   9/18/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

import java.time.LocalDateTime;

public class PhieuDatBan {
    private String maPhieuDat;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private BanAn banAn;
    private LocalDateTime ngayDat;
    private int soNguoi;
    private double soTienCoc;
    private String ghiChu;
    private String trangThai; // Da den, Chua den, Huy

    public PhieuDatBan() {
    }
    public PhieuDatBan(String maPhieuDat) {
        this.maPhieuDat = maPhieuDat;
    }
    public PhieuDatBan(String maPhieuDat, KhachHang khachHang, NhanVien nhanVien, BanAn banAn, LocalDateTime ngayDat, int soNguoi, double soTienCoc, String ghiChu, String trangThai) {
        this.maPhieuDat = maPhieuDat;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.banAn = banAn;
        this.ngayDat = ngayDat;
        this.soNguoi = soNguoi;
        this.soTienCoc = soTienCoc;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public String getMaPhieuDat() {
        return maPhieuDat;
    }
    public void setMaPhieuDat(String maPhieuDat) {
        this.maPhieuDat = maPhieuDat;
    }
    public KhachHang getKhachHang() {
        return khachHang;
    }
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    public BanAn getBanAn() {
        return banAn;
    }
    public void setBanAn(BanAn banAn) {
        this.banAn = banAn;
    }
    public LocalDateTime getNgayDat() {
        return ngayDat;
    }
    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }
    public int getSoNguoi() {
        return soNguoi;
    }
    public void setSoNguoi(int soNguoi) {
    	if (soNguoi <= 0) {
            throw new IllegalArgumentException("Số người phải lớn hơn 0");
        }
        this.soNguoi = soNguoi;
    }
    public double getSoTienCoc() {
        return soTienCoc;
    }
    public void setSoTienCoc(double soTienCoc) {
        this.soTienCoc = soTienCoc;
    }
    public String getGhiChu() {
        return ghiChu;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
