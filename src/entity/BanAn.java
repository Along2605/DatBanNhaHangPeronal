/*
 * @ (#) BanAn.java   1.0   9/18/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

public class BanAn {
    private String maBan;
    private String tenBan;
    private int soLuongCho;
    private LoaiBan loaiBan; // Thuong, VIP
    private String trangThai; // Trong, Dang su dung, Da dat truoc
    private KhuVuc khuVuc;
    private String ghiChu;

    public BanAn() {
    }
    public BanAn(String maBan) {
        this.maBan = maBan;
    }
	public BanAn(String maBan, String tenBan, int soLuongCho, LoaiBan loaiBan, String trangThai, KhuVuc khuVuc,
			String ghiChu) {
		this.maBan = maBan;
		this.tenBan = tenBan;
		this.soLuongCho = soLuongCho;
		this.loaiBan = loaiBan;
		this.trangThai = trangThai;
		this.khuVuc = khuVuc;
		this.ghiChu = ghiChu;
	}
	public String getMaBan() {
		return maBan;
	}
	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}
	public String getTenBan() {
		return tenBan;
	}
	public void setTenBan(String tenBan) {
		this.tenBan = tenBan;
	}
	public int getSoLuongCho() {
		return soLuongCho;
	}
	public void setSoLuongCho(int soLuongCho) {
		this.soLuongCho = soLuongCho;
	}
	public LoaiBan getLoaiBan() {
		return loaiBan;
	}
	public void setLoaiBan(LoaiBan loaiBan) {
		this.loaiBan = loaiBan;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public KhuVuc getKhuVuc() {
		return khuVuc;
	}
	public void setKhuVuc(KhuVuc khuVuc) {
		this.khuVuc = khuVuc;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	

}

