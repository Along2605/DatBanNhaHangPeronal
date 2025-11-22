package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class KhuyenMai {

    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double phanTramGiam;   // Giảm theo %
    private double soTienGiam;     // Giảm theo số tiền
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String loaiKhuyenMai;  // "Phần trăm" hoặc "Số tiền"
    private boolean trangThai;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }
    public double tinhTienGiam(double tongTienMon) {
        // Nếu khuyến mãi hết hạn hoặc không hoạt động
        if (!this.trangThai || 
            (this.ngayKetThuc != null && this.ngayKetThuc.isBefore(LocalDateTime.now()))) {
            return 0;
        }

        // Nếu là loại giảm theo %
        if ("Phần trăm".equalsIgnoreCase(this.loaiKhuyenMai)) {
            return tongTienMon * (this.phanTramGiam / 100.0);
        }

        // Nếu là loại giảm theo số tiền
        if ("Số tiền".equalsIgnoreCase(this.loaiKhuyenMai)) {
            return Math.min(this.soTienGiam, tongTienMon); // Không giảm quá tổng tiền
        }

        // Nếu loại không hợp lệ
        return 0;
    }

    // --- Getters & Setters ---
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

    public double getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(double soTienGiam) {
        this.soTienGiam = soTienGiam;
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

  
    @Override
    public int hashCode() {
        return Objects.hash(maKhuyenMai);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        KhuyenMai other = (KhuyenMai) obj;
        return Objects.equals(maKhuyenMai, other.maKhuyenMai);
    }
}
