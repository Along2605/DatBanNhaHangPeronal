package entity;

/**
 * Interface cho các Entity cần ghi log
 * Mỗi entity implement interface này để hỗ trợ logging tự động
 */
public interface Loggable {
    
    /**
     * Lấy mã định danh của đối tượng (maHoaDon, maKH, maNV,...)
     */
    String getMaDoiTuong();
    
    /**
     * Lấy tên bảng trong database
     */
    String getTenBang();
    
    /**
     * Chuyển đối tượng thành chuỗi JSON để lưu log
     */
    String toLogString();
}