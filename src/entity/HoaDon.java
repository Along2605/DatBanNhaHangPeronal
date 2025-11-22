package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHoaDon;
    private BanAn banAn;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private LocalDateTime ngayLapHoaDon;
    private double thueVAT;
    private double tongTien;
    private KhuyenMai khuyenMai;
    private String trangThai;
    private PhieuDatBan phieuDat;
    private double tienCoc;
    private List<ChiTietHoaDon> dsChiTiet;

    public HoaDon() {
        dsChiTiet = new ArrayList<>();
    }

    public HoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public HoaDon(String maHoaDon, BanAn banAn, KhachHang khachHang, NhanVien nhanVien,
                  LocalDateTime ngayLapHoaDon, double thueVAT, double tongTien,
                  KhuyenMai khuyenMai, String trangThai, PhieuDatBan phieuDat, double tienCoc) {
        this.maHoaDon = maHoaDon;
        this.banAn = banAn;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.thueVAT = thueVAT;
        this.tongTien = tongTien;
        this.khuyenMai = khuyenMai;
        this.trangThai = trangThai;
        this.phieuDat= phieuDat;
        this.tienCoc= tienCoc;        
        this.dsChiTiet = new ArrayList<>();
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

    public LocalDateTime getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public void setNgayLapHoaDon(LocalDateTime ngayLapHoaDon) {
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = thueVAT;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    

    public PhieuDatBan getPhieuDat() {
		return phieuDat;
	}

	public void setPhieuDat(PhieuDatBan phieuDat) {
		this.phieuDat = phieuDat;
	}
	
	

	public double getTienCoc() {
		return tienCoc;
	}

	public void setTienCoc(double tienCoc) {
		this.tienCoc = tienCoc;
	}

	public List<ChiTietHoaDon> getDsChiTiet() {
        return dsChiTiet;
    }

    public void setDsChiTiet(List<ChiTietHoaDon> dsChiTiet) {
        this.dsChiTiet = dsChiTiet;
    }
}
