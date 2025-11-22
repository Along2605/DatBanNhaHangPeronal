package entity;



public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private MonAn monAn;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String ghiChu;
    

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
    }

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn, int soLuong, double donGia, double thanhTien, String ghiChu) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien= thanhTien;
        this.ghiChu=ghiChu;
        
    }
    
    public ChiTietHoaDon(String maHoaDon, String maMon, int soLuong2, double gia) {
		this.hoaDon.setMaHoaDon(maHoaDon);
		this.monAn.setMaMon(maMon);
		this.soLuong = soLuong2;
		this.donGia = gia;
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

	public double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

    
}


