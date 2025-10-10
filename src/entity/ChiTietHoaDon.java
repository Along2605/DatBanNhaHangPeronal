package entity;

import entity.KhuyenMai;
import entity.MonAn;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private MonAn monAn;
    private int soLuong;
    private double donGia;
    private KhuyenMai khuyenMai;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
    }

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn, int soLuong, double donGia, KhuyenMai khuyenMai) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.khuyenMai = khuyenMai;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
}


