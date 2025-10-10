/*
 * @ (#) LichLamViec.java   1.0   9/20/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

import java.time.LocalDate;

public class LichLamViec {
    private String maLich;
    private LocalDate ngayLamViec;
    private NhanVien nhanVien;
    private CaLamViec caLamViec;
    private boolean trangThai; // co di lam hay khong

    public LichLamViec() {
    }
    public LichLamViec(String maLich) {
        this.maLich = maLich;
    }
    public LichLamViec(String maLich, LocalDate ngayLamViec, NhanVien nhanVien, CaLamViec caLamViec, boolean trangThai) {
        this.maLich = maLich;
        this.ngayLamViec = ngayLamViec;
        this.nhanVien = nhanVien;
        this.caLamViec = caLamViec;
        this.trangThai = trangThai;
    }

    public CaLamViec getCaLamViec() {
        return caLamViec;
    }

    public String getMaLich() {
        return maLich;
    }

    public LocalDate getNgayLamViec() {
        return ngayLamViec;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }

    public void setMaLich(String maLich) {
        this.maLich = maLich;
    }

    public void setNgayLamViec(LocalDate ngayLamViec) {
        this.ngayLamViec = ngayLamViec;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
