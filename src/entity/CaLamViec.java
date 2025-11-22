/*
 * @ (#) CaLamViec.java   1.0   9/20/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

import java.time.LocalTime;

public class CaLamViec {
    private String maCa;
    private String tenCa;
    private LocalTime gioVaoLam;
    private LocalTime gioTanLam;
    private boolean trangThai; // Dang hoat dong, Khong hoat dong
    public CaLamViec() {
    }
    public CaLamViec(String maCa) {
        this.maCa = maCa;
    }
	public CaLamViec(String maCa, String tenCa, LocalTime gioVaoLam, LocalTime gioTanLam, boolean trangThai) {
		this.maCa = maCa;
		this.tenCa = tenCa;
		this.gioVaoLam = gioVaoLam;
		this.gioTanLam = gioTanLam;
		this.trangThai = trangThai;
	}
	public String getMaCa() {
		return maCa;
	}
	public void setMaCa(String maCa) {
		this.maCa = maCa;
	}
	public String getTenCa() {
		return tenCa;
	}
	public void setTenCa(String tenCa) {
		this.tenCa = tenCa;
	}
	public LocalTime getGioVaoLam() {
		return gioVaoLam;
	}
	public void setGioVaoLam(LocalTime gioVaoLam) {
		this.gioVaoLam = gioVaoLam;
	}
	public LocalTime getGioTanLam() {
		return gioTanLam;
	}
	public void setGioTanLam(LocalTime gioTanLam) {
		this.gioTanLam = gioTanLam;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	
	
    
    
}
