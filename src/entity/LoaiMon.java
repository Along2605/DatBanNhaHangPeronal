package entity;

public class LoaiMon {
    private String maLoaiMon;
    private String tenLoaiMon;

    public LoaiMon() {}

    public LoaiMon(String maLoaiMon, String tenLoaiMon) {
        this.maLoaiMon = maLoaiMon;
        this.tenLoaiMon = tenLoaiMon;
    }

    // Getters and Setters
    public String getMaLoaiMon() { return maLoaiMon; }
    public void setMaLoaiMon(String maLoaiMon) { this.maLoaiMon = maLoaiMon; }
    public String getTenLoaiMon() { return tenLoaiMon; }
    public void setTenLoaiMon(String tenLoaiMon) { this.tenLoaiMon = tenLoaiMon; }

    @Override
    public String toString() {
        return tenLoaiMon; // Hiển thị tên trong JList/JComboBox
    }

	public int toUpperCase() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}