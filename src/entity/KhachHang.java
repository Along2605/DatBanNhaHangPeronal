package entity;

import java.time.LocalDate;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private boolean gioiTinh; // true: Nam, false: Nữ
    private String sdt;
    private int diemTichLuy;
    private LocalDate ngayDangKy;
    private boolean trangThai;

    public KhachHang() {
    }
    public KhachHang(String maKH) {
        this.maKH = maKH;
    }
    public KhachHang(String maKH, String hoTen, boolean gioiTinh, String sdt, int diemTichLuy, LocalDate ngayDangKy, boolean trangThai) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.diemTichLuy = diemTichLuy;
        this.ngayDangKy= ngayDangKy;
        this.trangThai= trangThai;
    }
    
	public String getMaKH() {
		return maKH;
	}
	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public boolean isGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	public int getDiemTichLuy() {
		return diemTichLuy;
	}
	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}
	public LocalDate getNgayDangKy() {
		return ngayDangKy;
	}
	public void setNgayDangKy(LocalDate ngayDangKy) {
		this.ngayDangKy = ngayDangKy;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	
	
	
//	@Override
//	public String toString() {
//		return "KhachHang [maKH=" + maKH + ", hoTen=" + hoTen + ", gioiTinh=" + gioiTinh + ", sdt=" + sdt
//				+ ", diemTichLuy=" + diemTichLuy + "]";
//	}
    
    
}

