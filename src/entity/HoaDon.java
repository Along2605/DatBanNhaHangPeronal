/*
 * @ (#) HoaDon.java   1.0   9/20/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHoaDon;
    private BanAn banAn;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private LocalDate ngayLapHoaDon;
    private double thueVAT;
    private KhuyenMai khuyenMai;
    private List<ChiTietHoaDon> dsChiTiet;

    public HoaDon() {
        dsChiTiet = new ArrayList<ChiTietHoaDon>();
    }



    public HoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
    public HoaDon(String maHoaDon, BanAn banAn, KhachHang khachHang, NhanVien nhanVien, LocalDate ngayLapHoaDon, double thueVAT, KhuyenMai khuyenMai) {
        this.maHoaDon = maHoaDon;
        this.banAn = banAn;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.thueVAT = thueVAT;
        this.khuyenMai = khuyenMai;
    }
    public String getMaHoaDon() {
        return maHoaDon;
    }
    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public BanAn getBanAn() {
        return banAn;
    }
    public void setBanAn(BanAn banAn) {
        this.banAn = banAn;
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

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public LocalDate getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public void setNgayLapHoaDon(LocalDate ngayLapHoaDon) {
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = thueVAT;
    }

    public List<ChiTietHoaDon> getDsChiTiet() {
        return dsChiTiet;
    }

}
