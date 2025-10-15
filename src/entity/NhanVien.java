package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private LocalDate ngaySinh;
    private String email;
    private String soDienThoai;
    private boolean gioiTinh; // true: Nam, false: Nữ
    private String chucVu;
    private LocalDate ngayVaoLam;
    private boolean trangThai; // true: Đang làm việc, false: Đã nghỉ việc
    private List<LichLamViec> danhSachLichLam;
    
    
   
    public NhanVien(){
    	danhSachLichLam= new ArrayList<LichLamViec>();
    }
    public NhanVien(String maNV){
        this.maNV = maNV;
    }
    public NhanVien(String maNV, String hoTen, LocalDate ngaySinh, String email, String soDienThoai, boolean gioiTinh,
            String chucVu, LocalDate ngayVaoLam, boolean trangThai) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThai = trangThai;
        
    }

    public String getMaNV() {
        return maNV;
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public String getHoTen() {
        return hoTen;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    public String getChucVu() {
        return chucVu;
    }
    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }
    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }
    public boolean isTrangThai() {
        return trangThai;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
	public List<LichLamViec> getDanhSachLichLam() {
		return danhSachLichLam;
	}
	
	
    
    
//    @Override
//    public String toString() {
//        return "NhanVien [maNV=" + maNV + ", hoTen=" + hoTen + ", ngaySinh=" + ngaySinh + ", email=" + email
//                + ", soDienThoai=" + soDienThoai + ", gioiTinh=" + gioiTinh + ", chucVu=" + chucVu + ", ngayVaoLam="
//                + ngayVaoLam + ", trangThai=" + trangThai + "]";
//    }   
}

