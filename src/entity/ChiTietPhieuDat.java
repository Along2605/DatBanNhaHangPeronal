/*
 * @ (#) ChiTietPhieuDat.java   1.0   9/20/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

public class ChiTietPhieuDat {
    private PhieuDatBan phieuDatBan;
    private MonAn monAn;
    private int soLuong;
    private double donGia;
    private String ghiChu;

    public ChiTietPhieuDat() {
    }

    public ChiTietPhieuDat(PhieuDatBan phieuDatBan, MonAn monAn) {
        this.phieuDatBan = phieuDatBan;
        this.monAn = monAn;
    }

    public ChiTietPhieuDat(PhieuDatBan phieuDatBan, MonAn monAn, int soLuong, double donGia, String ghiChu) {
        this.phieuDatBan = phieuDatBan;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
    }

    public PhieuDatBan getPhieuDatBan() {
        return phieuDatBan;
    }

    public void setPhieuDatBan(PhieuDatBan phieuDatBan) {
        this.phieuDatBan = phieuDatBan;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
