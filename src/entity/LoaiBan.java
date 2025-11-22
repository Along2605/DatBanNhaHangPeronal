package entity;

import java.util.Objects;

public class LoaiBan {
	private String maLoaiBan;
	private String tenLoaiBan;

	public LoaiBan(String maLoaiBan, String tenLoaiBan) {
		this.maLoaiBan = maLoaiBan;
		this.tenLoaiBan = tenLoaiBan;
	}
	
	public LoaiBan(String maLoaiBan) {
		this.maLoaiBan = maLoaiBan;
	}
	
	
	public String getMaLoaiBan() {
		return maLoaiBan;
	}

	public void setMaLoaiBan(String maLoaiBan) {
		this.maLoaiBan = maLoaiBan;
	}

	public String getTenLoaiBan() {
		return tenLoaiBan;
	}

	public void setTenLoaiBan(String tenLoaiBan) {
		this.tenLoaiBan = tenLoaiBan;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    LoaiBan other = (LoaiBan) obj;
	    return Objects.equals(maLoaiBan, other.maLoaiBan);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(maLoaiBan);
	}



	@Override
	public String toString() {
	    return tenLoaiBan; // để JComboBox hiển thị tên loại bàn thay vì mã
	}
}
