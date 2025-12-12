package dao;

import connectDB.ConnectDB;
import entity.BanAn;
import entity.KhuVuc;
import entity.LoaiBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BanAnDAO {
	
	private KhuVucDAO khuVucDAO = new KhuVucDAO();
    private LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
    public List<BanAn> getAllBanAn() {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    
	    String sql = """
	    	    SELECT b.*, k.tenKhuVuc, k.viTri, l.tenLoaiBan
	    	    FROM BanAn b
	    	    LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	    	    LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	    	    ORDER BY b.maBan
	    	""";
	    Connection con = ConnectDB.getConnection();
	    try (
	         PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);

	            String trangThai = rs.getString("trangThai");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            String viTri = rs.getString("viTri"); 

	           
	          
	            KhuVuc khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, viTri);
	            
	            String ghiChu = rs.getString("ghiChu");
	            
	            BanAn ban = new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	            dsBanAn.add(ban);
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy danh sách bàn ăn:");
	        e.printStackTrace();
	    }
	    
	    return dsBanAn;
	}
	
	public BanAn getBanTheoMa(String maBan) {
		String sql = """
			SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	        WHERE b.maBan = ?
        """;
		
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement(sql);){
			stmt.setString(1, maBan);
			ResultSet rs= stmt.executeQuery();
			
			if (rs.next()) {				
				
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            
	            String trangThai = rs.getString("trangThai");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            
	          
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null); // viTri không được lấy trong truy vấn này
	            }
	            
	            String ghiChu = rs.getString("ghiChu");
  
	            BanAn ban= new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	         	return ban;	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	
	public List<BanAn> getBanTheoTen(String tenBan) {
		List<BanAn> dsTimThay = new ArrayList<>();
		String sql = """
			SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	        WHERE b.tenBan = ?
        """;
		
		Connection con= ConnectDB.getConnection();
		try (PreparedStatement stmt= con.prepareStatement(sql);){
			stmt.setString(1, tenBan);
			ResultSet rs= stmt.executeQuery();
			
			while (rs.next()) {				
				
	            String maBan = rs.getString("maBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            
	            String trangThai = rs.getString("trangThai");
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            
	          
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null); // viTri không được lấy trong truy vấn này
	            }
	            
	            String ghiChu = rs.getString("ghiChu");
  
	            BanAn ban= new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	         	dsTimThay.add(ban);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return dsTimThay;
	}
	
	public List<BanAn> getDSBanTrong() {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    String sql = """
	         SELECT b.*, k.tenKhuVuc, l.tenLoaiBan
	    	 FROM BanAn b
	    	 LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	    	 LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	         WHERE b.trangThai = N'Trống'
	        """;
	    Connection con = ConnectDB.getConnection();
	   
	    try (PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        int count = 0;
	        while (rs.next()) {
	            count++;
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            KhuVuc khuVuc = null;
	            if (maKhuVuc != null) {
	                khuVuc = new KhuVuc(maKhuVuc, tenKhuVuc, null);
	            }
	            String ghiChu = rs.getString("ghiChu");
	            BanAn ban= new BanAn(maBan, tenBan, soLuongCho, loaiBan, "Trống", khuVuc, ghiChu);
	            dsBanAn.add(ban);
	        }
	        System.out.println("Found " + count + " available tables");
	    } catch (SQLException e) {
	        System.err.println("❌ Error fetching available tables: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return dsBanAn;
	}

	public boolean capNhatTrangThaiBan(String maBan, String trangThai) {
		String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, trangThai);
			ps.setString(2, maBan);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean themBan(BanAn banAn) {
	    String sql = """
	        INSERT INTO BanAn (maBan, tenBan, soLuongCho, maLoaiBan, trangThai, maKhuVuc, ghiChu)
	        VALUES (?, ?, ?, ?, ?, ?, ?)
	        """;

	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, banAn.getMaBan());
	        ps.setString(2, banAn.getTenBan());
	        ps.setInt(3, banAn.getSoLuongCho());
	        ps.setString(4, banAn.getLoaiBan().getMaLoaiBan()); 
	        ps.setString(5, banAn.getTrangThai());
	        if (banAn.getKhuVuc() != null) {
	            ps.setString(6, banAn.getKhuVuc().getMaKhuVuc());
	        } else {
	            ps.setNull(6, java.sql.Types.VARCHAR);
	        }
	        ps.setString(7, banAn.getGhiChu());

	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi thêm bàn ăn mới:");
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean xoaBan(String maBan) {
	    String sql = "DELETE FROM BanAn WHERE maBan = ?";
	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maBan);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi xóa bàn ăn có mã: " + maBan);
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	public String generateMaBanMoi() {
		Connection con = ConnectDB.getConnection();
		String maMoi = "B001";
		PreparedStatement stmt = null;
		try{
			String sql = "SELECT MAX(CAST(SUBSTRING(maBan, 3, LEN(maBan)-2) AS INT)) AS maxSo FROM BanAn";
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int maxSo = rs.getInt("maxSo");
				int soMoi = maxSo + 1;
				maMoi = String.format("B%03d", soMoi);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maMoi;
	}
	
	
	public boolean chuyenBan(String maBanNguon, String maBanDich) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql1 = "UPDATE BanAn SET trangThai = N'Trống' WHERE maBan = ?";
			String sql2 = "UPDATE BanAn SET trangThai = N'Đang sử dụng' WHERE maBan = ?";

			PreparedStatement ps1 = con.prepareStatement(sql1);
			ps1.setString(1, maBanNguon);
			ps1.executeUpdate();

			PreparedStatement ps2 = con.prepareStatement(sql2);
			ps2.setString(1, maBanDich);
			ps2.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String layMaHDTuBan(String maBan) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement ps = null;
		try {
			String sql = "SELECT TOP 1 maHoaDon "
					+ "FROM HoaDon hd "
					+ "Join ChiTietDatBan ct "
					+ "On hd.maPhieuDat = ct.maPhieuDat "
					+ "WHERE hd.trangThai = N'Chưa thanh toán' And (hd.maBan = ? or ct.maBan = ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, maBan);
			ps.setString(2, maBan);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("maHoaDon");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean suaThongTinBan(BanAn ban) {
	    String sql = """
	        UPDATE BanAn 
	        SET tenBan = ?, soLuongCho = ?, maLoaiBan = ?, trangThai = ?, maKhuVuc = ?, ghiChu = ?
	        WHERE maBan = ?
	        """;

	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, ban.getTenBan());
	        ps.setInt(2, ban.getSoLuongCho());

	        if (ban.getLoaiBan() != null)
	            ps.setString(3, ban.getLoaiBan().getMaLoaiBan());
	        else
	            ps.setNull(3, java.sql.Types.VARCHAR);

	        ps.setString(4, ban.getTrangThai());

	        if (ban.getKhuVuc() != null)
	            ps.setString(5, ban.getKhuVuc().getMaKhuVuc());
	        else
	            ps.setNull(5, java.sql.Types.VARCHAR);

	        ps.setString(6, ban.getGhiChu());
	        ps.setString(7, ban.getMaBan());

	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi cập nhật bàn ăn:");
	        e.printStackTrace();
	        return false;
	    }
	}	
	
	
	 // Lấy danh sách bàn trống theo ngày và khung giờ cụ thể	
	public List<BanAn> getDSBanTrongTheoKhungGio(java.time.LocalDate ngayDat, int khungGio) {
	    List<BanAn> dsBan = new ArrayList<>();
	    
	    String sql = """
	        SELECT DISTINCT b.maBan, b.tenBan, b.soLuongCho, b.maLoaiBan, b.maKhuVuc, 
	               b.ghiChu, b.trangThai, l.tenLoaiBan, k.tenKhuVuc
	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	        WHERE b.trangThai NOT IN (N'Bảo trì', N'Hỏng') 
	        AND b.maBan NOT IN (
	            SELECT ctdb.maBan
	            FROM ChiTietDatBan ctdb
	            JOIN PhieuDatBan pdb ON ctdb.maPhieuDat = pdb.maPhieuDat
	            WHERE CAST(pdb.ngayDat AS DATE) = ?
	            AND pdb.khungGio = ?
	            AND pdb.trangThai IN (N'Chờ xác nhận', N'Đã xác nhận')
	        )
	        ORDER BY b.maBan
	    """;
	    
	    Connection con = ConnectDB.getConnection();
	    
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setDate(1, java.sql.Date.valueOf(ngayDat));
	        ps.setInt(2, khungGio);
	        
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            String maBan = rs.getString("maBan");
	            String tenBan = rs.getString("tenBan");
	            int soLuongCho = rs.getInt("soLuongCho");
	            String trangThai = rs.getString("trangThai");
	            String ghiChu = rs.getString("ghiChu");
	            
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            LoaiBan loaiBan = new LoaiBan(maLoaiBan, tenLoaiBan);
	            
	            String maKhuVuc = rs.getString("maKhuVuc");
	            String tenKhuVuc = rs.getString("tenKhuVuc");
	            KhuVuc khuVuc = (maKhuVuc != null) ? new KhuVuc(maKhuVuc, tenKhuVuc, null) : null;
	            
	            BanAn ban = new BanAn(maBan, tenBan, soLuongCho, loaiBan, trangThai, khuVuc, ghiChu);
	            dsBan.add(ban);
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy danh sách bàn trống theo khung giờ:");
	        e.printStackTrace();
	    }
	    
	    return dsBan;
	}
	



	/**
	 * Lấy danh sách bàn theo ngày và khung giờ với trạng thái chuẩn hóa.
	 * 
	 * TRẠNG THÁI BÀN (4 loại):
	 * - Trống: Bàn không có phiếu đặt, không có hóa đơn
	 * - Đã đặt: Bàn có phiếu đặt "Chờ xác nhận" hoặc "Đã xác nhận"
	 * - Đang sử dụng: Bàn có hóa đơn "Chưa thanh toán"
	 * - Bảo trì: Bàn đang được bảo trì
	 */
	public List<BanAn> getDanhSachBanTheoNgayGio(java.time.LocalDate ngayXem, int khungGio) {
	    List<BanAn> dsBanAn = new ArrayList<>();
	    
	    String sql = """
	        SELECT b.maBan, b.tenBan, b.soLuongCho, b.maLoaiBan, b.maKhuVuc, b.ghiChu, 
	               l.tenLoaiBan, k.tenKhuVuc, b.trangThai AS trangThaiGoc,
	            CASE 
	                -- 1. Bàn bảo trì → Luôn hiển thị "Bảo trì"
	                WHEN b.trangThai = N'Bảo trì' THEN N'Bảo trì'
	                
	                -- 2. Bàn đang sử dụng (có hóa đơn chưa thanh toán hôm nay)
	                -- Case 2a: Hóa đơn vãng lai (maBan trực tiếp)
	                WHEN EXISTS (
	                    SELECT 1 
	                    FROM HoaDon hd
	                    WHERE hd.maBan = b.maBan 
	                    AND hd.trangThai = N'Chưa thanh toán'
	                    AND CAST(hd.ngayLapHoaDon AS DATE) = CAST(GETDATE() AS DATE)
	                ) THEN N'Đang sử dụng'
	                
	                -- Case 2b: Hóa đơn từ phiếu đặt (qua ChiTietDatBan)
	                WHEN EXISTS (
	                    SELECT 1
	                    FROM HoaDon hd
	                    INNER JOIN PhieuDatBan pdb ON hd.maPhieuDat = pdb.maPhieuDat
	                    INNER JOIN ChiTietDatBan ctdb ON pdb.maPhieuDat = ctdb.maPhieuDat
	                    WHERE ctdb.maBan = b.maBan
	                    AND hd.trangThai = N'Chưa thanh toán'
	                    AND CAST(hd.ngayLapHoaDon AS DATE) = CAST(GETDATE() AS DATE)
	                ) THEN N'Đang sử dụng'
	                
	                -- 3. Bàn có phiếu đặt ĐÃ XÁC NHẬN trong khung giờ này → "Đang sử dụng"
	                WHEN EXISTS (
	                    SELECT 1 
	                    FROM ChiTietDatBan ctdb
	                    JOIN PhieuDatBan pdb ON ctdb.maPhieuDat = pdb.maPhieuDat
	                    WHERE ctdb.maBan = b.maBan 
	                    AND pdb.trangThai = N'Đã xác nhận'
	                    AND CAST(pdb.ngayDat AS DATE) = ?
	                    AND pdb.khungGio = ?
	                ) THEN N'Đang sử dụng'
	                
	                -- 4. Bàn có phiếu đặt CHỜ XÁC NHẬN trong khung giờ này → "Đã đặt"
	                WHEN EXISTS (
	                    SELECT 1 
	                    FROM ChiTietDatBan ctdb
	                    JOIN PhieuDatBan pdb ON ctdb.maPhieuDat = pdb.maPhieuDat
	                    WHERE ctdb.maBan = b.maBan 
	                    AND pdb.trangThai = N'Chờ xác nhận'
	                    AND CAST(pdb.ngayDat AS DATE) = ?
	                    AND pdb.khungGio = ?
	                ) THEN N'Đã đặt'
	                
	                -- 5. Các trường hợp còn lại → "Trống"
	                ELSE N'Trống'
	            END AS trangThaiHienThi

	        FROM BanAn b
	        LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc
	        LEFT JOIN LoaiBanAn l ON b.maLoaiBan = l.maLoaiBan
	        ORDER BY b.maBan
	    """;

	    Connection con = ConnectDB.getConnection();
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        // Tham số 1,2: Cho phiếu "Đã xác nhận" → "Đang sử dụng"
	        stmt.setDate(1, java.sql.Date.valueOf(ngayXem));
	        stmt.setInt(2, khungGio);
	        // Tham số 3,4: Cho phiếu "Chờ xác nhận" → "Đã đặt"
	        stmt.setDate(3, java.sql.Date.valueOf(ngayXem));
	        stmt.setInt(4, khungGio);
	        
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            BanAn ban = new BanAn();
	            ban.setMaBan(rs.getString("maBan"));
	            ban.setTenBan(rs.getString("tenBan"));
	            ban.setSoLuongCho(rs.getInt("soLuongCho"));
	            ban.setGhiChu(rs.getString("ghiChu"));
	            ban.setTrangThai(rs.getString("trangThaiHienThi")); 
	            
	            String maLoaiBan = rs.getString("maLoaiBan");
	            String tenLoaiBan = rs.getString("tenLoaiBan");
	            ban.setLoaiBan(new LoaiBan(maLoaiBan, tenLoaiBan));
	            
	            String maKhuVuc = rs.getString("maKhuVuc");
	            if (maKhuVuc != null) {
	                String tenKhuVuc = rs.getString("tenKhuVuc");
	                ban.setKhuVuc(new KhuVuc(maKhuVuc, tenKhuVuc, null));
	            }
	            
	            dsBanAn.add(ban);
	        }
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi lấy danh sách bàn theo ngày giờ:");
	        e.printStackTrace();
	    }
	    return dsBanAn;
	}
	
	public void resetTrangThaiBanHangNgay() {
	    Connection con = ConnectDB.getConnection();
	    
	    
	    // LOGIC CHO NHÀ HÀNG ĐÓNG CỬA TRONG NGÀY (KHÔNG QUA ĐÊM):
	    // Tìm các bàn đang "Đang sử dụng"
	    // Nhưng KHÔNG có hóa đơn nào của NGÀY HÔM NAY (GETDATE) đang mở
	    // -> Reset về "Trống" ngay lập tức.
	    String sql = """
	        UPDATE BanAn 
	        SET trangThai = N'Trống' 
	        WHERE trangThai = N'Đang sử dụng'
	        AND maBan NOT IN (
	            SELECT maBan FROM HoaDon 
	            WHERE trangThai = N'Chưa thanh toán' 
	            AND CAST(ngayLapHoaDon AS DATE) = CAST(GETDATE() AS DATE)
	        )
	    """;

	    try (PreparedStatement stmt= con.prepareStatement(sql)){   
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public BanAn mapResultSetToBanAn(ResultSet rs) throws SQLException {
        // 1. Lấy các cột từ bảng BanAn
        String maBan = rs.getString("maBan");
        String tenBan = rs.getString("tenBan");
        int soLuongCho = rs.getInt("soLuongCho");
        String trangThai = rs.getString("trangThai");
        String ghiChu = rs.getString("ghiChu");
        
        // 2. Lấy mã khóa ngoại
        String maLoaiBan = rs.getString("maLoaiBan");
        String maKhuVuc = rs.getString("maKhuVuc");
        
        // 3. Dùng DAO để lấy đối tượng đầy đủ
        // (Giả sử các DAO này có hàm tìm theo mã)
        LoaiBan loaiBan = null;
        if (maLoaiBan != null) {
            loaiBan = loaiBanDAO.layLoaiBanTheoMa(maLoaiBan);
        }
        
        KhuVuc khuVuc = null;
        if (maKhuVuc != null) {
            khuVuc = khuVucDAO.findById(maKhuVuc);
        }
        
        // 4. Tạo đối tượng BanAn (điều chỉnh constructor nếu cần)
        BanAn ban = new BanAn(
            maBan, 
            tenBan, 
            soLuongCho, 
            loaiBan, // Đối tượng LoaiBanAn
            trangThai, 
            khuVuc, // Đối tượng KhuVuc
            ghiChu
        );
        
        return ban;
    }
	
	
	private static class PhieuDatInfo {
	    LocalDateTime gioDat;
	    String trangThai;
	    
	    PhieuDatInfo(LocalDateTime gioDat, String trangThai) {
	        this.gioDat = gioDat;
	        this.trangThai = trangThai;
	    }
	}
	
	
	/**
	 * Kiểm tra bàn có khả dụng trong khung giờ không.
	 * Return true nếu bàn trống, false nếu đã có phiếu đặt.
	 */
	public boolean kiemTraBanKhaDung(String maBan, LocalDate ngayDat, int khungGio) {
	    String sql = """
	        SELECT COUNT(*) as soLuong
	        FROM ChiTietDatBan ctdb
	        JOIN PhieuDatBan pdb ON ctdb.maPhieuDat = pdb.maPhieuDat
	        WHERE ctdb.maBan = ?
	          AND CAST(pdb.ngayDat AS DATE) = ?
	          AND pdb.khungGio = ?
	          AND pdb.trangThai IN (N'Chờ xác nhận', N'Đã xác nhận')
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setString(1, maBan);
	        ps.setDate(2, java.sql.Date.valueOf(ngayDat));
	        ps.setInt(3, khungGio);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("soLuong") == 0; // 0 = khả dụng, >0 = đã đặt
	        }
	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi khi kiểm tra bàn khả dụng:");
	        e.printStackTrace();
	    }
	    return false; // Mặc định không khả dụng nếu lỗi
	}
	
	/**
	 * Xác định khung giờ từ giờ trong ngày.
	 */
	private int layKhungGio(LocalDateTime time) {
	    int hour = time.getHour();
	    if (hour >= 6 && hour < 11) return 1;   // Sáng
	    if (hour >= 11 && hour < 14) return 2;  // Trưa
	    if (hour >= 14 && hour < 17) return 3;  // Chiều
	    return 4;                                // Tối
	}

	
}
    