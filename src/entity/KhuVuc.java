/*
 * @ (#) KhuVuc.java   1.0   9/18/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package entity;

public class KhuVuc {
    private String maKhuVuc;
    private String tenKhuVuc;
    private String viTri; // Trong nha, Ngoai troi, Ban cong

    public KhuVuc() {
    }
    public KhuVuc(String maKhuVuc) {
        this.maKhuVuc = maKhuVuc;
    }
    public KhuVuc(String maKhuVuc, String tenKhuVuc, String viTri) {
        this.maKhuVuc = maKhuVuc;
        this.tenKhuVuc = tenKhuVuc;
        this.viTri = viTri;
    }
    public String getMaKhuVuc() {
        return maKhuVuc;
    }
    public void setMaKhuVuc(String maKhuVuc) {
        this.maKhuVuc = maKhuVuc;
    }
    public String getTenKhuVuc() {
        return tenKhuVuc;
    }
    public void setTenKhuVuc(String tenKhuVuc) {
        this.tenKhuVuc = tenKhuVuc;
    }
    public String getViTri() {
        return viTri;
    }
    public void setViTri(String viTri) {
        this.viTri = viTri;
    }


}
