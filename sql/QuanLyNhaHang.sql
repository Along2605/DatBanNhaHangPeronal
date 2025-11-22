-- Tạo cơ sở dữ liệu
CREATE DATABASE QLNH;
GO

USE QLNH;
GO

-- Bảng KhuVuc
CREATE TABLE KhuVuc (
    maKhuVuc VARCHAR(10) PRIMARY KEY,
    tenKhuVuc NVARCHAR(50),
    viTri NVARCHAR(50)
);
GO

-- Bảng NhanVien
CREATE TABLE NhanVien (
    maNV VARCHAR(10) PRIMARY KEY,
    hoTen NVARCHAR(50),
    ngaySinh DATE,
    email NVARCHAR(50),
    soDienThoai VARCHAR(15),
    gioiTinh BIT,
    chucVu NVARCHAR(50),
    ngayVaoLam DATE,
    trangThai BIT,
	anhDaiDien NVARCHAR(255)
);
GO

-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    userName NVARCHAR(50) PRIMARY KEY,
    passWord NVARCHAR(50),
    maNV VARCHAR(10),
    quyenTruyCap NVARCHAR(20),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);
GO

-- Bảng CaLamViec
CREATE TABLE CaLamViec (
    maCa VARCHAR(10) PRIMARY KEY,
	tenCa NVARCHAR(50),
    gioVaoLam TIME,
    gioTanLam TIME,
    trangThai BIT
);
GO

-- Bảng LichLamViec
CREATE TABLE LichLamViec (
    maLich VARCHAR(10) PRIMARY KEY,
    ngayLamViec DATE,
    maNV VARCHAR(10),
    maCa VARCHAR(10),
    trangThai BIT,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE,
    FOREIGN KEY (maCa) REFERENCES CaLamViec(maCa) ON DELETE CASCADE
);
GO



-- Bảng KhachHang (đã bỏ cột email)
CREATE TABLE KhachHang (
    maKH VARCHAR(10) PRIMARY KEY,
    hoTen NVARCHAR(50),
    gioiTinh BIT,
    sdt VARCHAR(15),
    diemTichLuy INT,
	ngayDangKy DATE DEFAULT GETDATE(),
    trangThai BIT DEFAULT 1
);
GO



-- Bảng LoaiMonAn
CREATE TABLE LoaiMonAn (
    maLoaiMon VARCHAR(10) PRIMARY KEY,
    tenLoaiMon NVARCHAR(50) NOT NULL
);



-- Bảng MonAn
CREATE TABLE MonAn (
    maMon VARCHAR(10) PRIMARY KEY,
    tenMon NVARCHAR(50),
    gia DECIMAL(10,2),
    donViTinh NVARCHAR(20),
    trangThai BIT,
    hinhAnh NVARCHAR(200),
    soLuong INT,
    moTa NVARCHAR(200),
    maLoaiMon VARCHAR(10),
	FOREIGN KEY (maLoaiMon) REFERENCES LoaiMonAn(maLoaiMon)
);
GO

-- Bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(10) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(50),
    phanTramGiam DECIMAL(5,2),
	soTienGiam DECIMAL(12,2),
    ngayBatDau DATETIME,
    ngayKetThuc DATETIME,
    loaiKhuyenMai NVARCHAR(50),
    trangThai BIT
);
GO


CREATE TABLE LoaiBanAn (
    maLoaiBan VARCHAR(10) PRIMARY KEY,
    tenLoaiBan NVARCHAR(40) NOT NULL
);
GO


CREATE TABLE BanAn (
    maBan VARCHAR(10) PRIMARY KEY,
    tenBan NVARCHAR(40) NOT NULL,
    soLuongCho INT NOT NULL,
    trangThai NVARCHAR(50) DEFAULT N'Trống',
    maKhuVuc VARCHAR(10),
    ghiChu NVARCHAR(200),
	 maLoaiBan VARCHAR(10),
    FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc) ON DELETE CASCADE,
    FOREIGN KEY (maLoaiBan) REFERENCES LoaiBanAn(maLoaiBan) ON DELETE CASCADE
);
GO


-- Bảng PhieuDatBan
CREATE TABLE PhieuDatBan (
    maPhieuDat VARCHAR(10) PRIMARY KEY,
    maKH VARCHAR(10),
    maNV VARCHAR(10),
    maBan VARCHAR(10),
    ngayDat DATETIME NOT NULL,
    soNguoi INT NOT NULL,
    soTienCoc DECIMAL(10,2) DEFAULT 0,
    ghiChu NVARCHAR(200),
    trangThai NVARCHAR(20) DEFAULT N'Đã đặt',
    
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE SET NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE SET NULL,
    FOREIGN KEY (maBan) REFERENCES BanAn(maBan) ON DELETE SET NULL
);

GO

-- Bảng ChiTietPhieuDat
CREATE TABLE ChiTietPhieuDat (
    maPhieuDat VARCHAR(10),
    maMon VARCHAR(10),
    soLuong INT,
    donGia DECIMAL(10,2),
    ghiChu NVARCHAR(200),
    PRIMARY KEY (maPhieuDat, maMon),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatBan(maPhieuDat) ON DELETE CASCADE,
    FOREIGN KEY (maMon) REFERENCES MonAn(maMon) ON DELETE CASCADE
);
GO

-- Bảng HoaDon
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(10) PRIMARY KEY,
    maBan VARCHAR(10),
    maKH VARCHAR(10),
    maNV VARCHAR(10),
	maPhieuDat VARCHAR(10),
    ngayLapHoaDon DATETIME,
    thueVAT DECIMAL(5,2),
    tongTien DECIMAL(12,2),
    maKhuyenMai VARCHAR(10),
    trangThai NVARCHAR(20) DEFAULT N'Chưa thanh toán',
	tienCoc DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (maBan) REFERENCES BanAn(maBan) ON DELETE SET NULL,
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE SET NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE SET NULL,
    FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai) ON DELETE SET NULL,
	FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatBan(maPhieuDat) ON DELETE SET NULL
);

GO


-- ---- Bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHoaDon VARCHAR(10),
    maMon VARCHAR(10),
    soLuong INT NOT NULL,
    donGia DECIMAL(12,2) NOT NULL,
    thanhTien DECIMAL(12,2) NOT NULL,
    ghiChu NVARCHAR(200),

	PRIMARY KEY (maHoaDon, maMon),
	FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    FOREIGN KEY (maMon) REFERENCES MonAn(maMon) ON DELETE CASCADE
)

-- Dữ liệu cho bảng LoaiMonAn
INSERT INTO LoaiMonAn (maLoaiMon, tenLoaiMon)
VALUES
  ('MC', N'Món chính'),
  ('MK', N'Món ăn kèm'),
  ('DO', N'Đồ uống')
GO
select * from LoaiMonAn

-- Dữ liệu cho bảng MonAn

INSERT INTO MonAn (maMon, tenMon, gia, donViTinh, trangThai, hinhAnh, soLuong, moTa, maLoaiMon)
VALUES
  ('MC01', N'Tokbokki truyền thống', 43000, N'phần', 1, 'img\\thucdon\\tokbokki_truyen_thong.PNG', 100, N'Bánh gạo cay truyền thống Hàn Quốc', 'MC'),
  ('MC02', N'Tokbokki phô mai', 62000, N'phần', 1, 'img\\thucdon\\tokbokki_haisan.PNG', 90, N'Bánh gạo mềm dẻo phủ phô mai tan chảy', 'MC'),
  ('MC03', N'Tokbokki hải sản', 79000, N'phần', 1, 'img\\thucdon\\tokbokki_haisan.PNG', 80, N'Bánh gạo cay kết hợp hải sản tươi', 'MC'),
  ('MC04', N'Tokbokki chả cá', 59000, N'phần', 1, 'img\\thucdon\\tokbokki_chaca.PNG', 70, N'Bánh gạo và chả cá sốt cay đặc trưng', 'MC'),
  ('MC05', N'Cơm trộn Hàn Quốc', 68000, N'phần', 1, 'img\\thucdon\\com_tron.PNG', 60, N'Cơm trộn rau củ, thịt bò và trứng lòng đào', 'MC'),
  ('MC06', N'Cơm cuộn kimbab thịt bò', 69000, N'phần', 1, 'img\\thucdon\\kimbab_bo.PNG', 70, N'Kimbab nhân thịt bò xào và rau củ', 'MC'),
  ('MC07', N'Cơm cuộn kimbab cá ngừ', 65000, N'phần', 1, 'img\\thucdon\\kimbab_cangu.PNG', 65, N'Kimbab nhân cá ngừ và sốt mayonnaise', 'MC'),
  ('MC08', N'Gà sốt cay Hàn Quốc', 75000, N'phần', 1, 'img\\thucdon\\ga_sot_cay.PNG', 60, N'Gà chiên giòn phủ sốt cay đậm đà', 'MC'),
  ('MC09', N'Gà phô mai kéo sợi', 85000, N'phần', 1, 'img\\thucdon\\ga_pho_mai.PNG', 55, N'Gà chiên giòn phủ phô mai tan chảy', 'MC'),
  ('MC10', N'Ba chỉ heo nướng', 79000, N'phần', 1, 'img\\thucdon\\ba_chi_sot_cay.jpg', 50, N'Thịt ba chỉ nướng ăn kèm kimchi và rau', 'MC'),
  ('MC11', N'Mì cay cấp độ 1', 55000, N'tô', 1, 'img\\thucdon\\mi_cay_1.PNG', 60, N'Mì cay vừa với chả cá và xúc xích', 'MC'),
  ('MC12', N'Mì cay hải sản', 69000, N'tô', 1, 'img\\thucdon\\mi_cay_haisan.PNG', 55, N'Mì cay hải sản với tôm và mực', 'MC'),
  ('MC13', N'Mì Udon gà sốt cay', 79000, N'tô', 1, 'img\\thucdon\\udon_ga.PNG', 50, N'Mì udon dai ăn kèm gà sốt cay', 'MC'),
  ('MC14', N'Mì tương đen Jajang', 69000, N'tô', 1, 'img\\thucdon\\mi_tuong_den.PNG', 50, N'Mì sốt tương đen đặc trưng Hàn Quốc', 'MC'),
  ('MC15', N'Miến trộn thịt bò', 79000, N'phần', 1, 'img\\thucdon\\mien_tron.PNG', 50, N'Miến trộn thịt bò, rau củ và trứng', 'MC'),
  ('MC16', N'Lẩu tokbokki', 139000, N'nồi', 1, 'img\\thucdon\\lau_tokbokki.PNG', 40, N'Lẩu bánh gạo cay kèm hải sản và rau', 'MC'),
  ('MC17', N'Lẩu kimchi bò', 149000, N'nồi', 1, 'img\\thucdon\\lau_kimchi_bo.PNG', 40, N'Lẩu kimchi đậm đà vị bò và nấm', 'MC'),
  ('MC18', N'Cơm gà sốt mật ong', 69000, N'phần', 1, 'img\\thucdon\\com_ga_mat_ong.PNG', 60, N'Cơm gà chiên giòn phủ sốt mật ong', 'MC'),
  ('MC19', N'Tokbokki trứng cút', 59000, N'phần', 1, 'img\\thucdon\\tokbokki_trung_cut.PNG', 70, N'Bánh gạo cay kèm trứng cút luộc', 'MC'),
  ('MC20', N'Mì lạnh Hàn Quốc', 65000, N'tô', 1, 'img\\thucdon\\mi_lanh.jpg', 50, N'Mì lạnh thanh mát ăn kèm trứng và dưa leo', 'MC'),
  ('MC21', N'Bulgogi bò', 88000, N'phần', 1, 'img\\thucdon\\bulgogi_bo.PNG', 50, N'Bò nướng sốt Hàn Quốc', 'MC'),
  ('MC22', N'Gà nướng mật ong', 75000, N'phần', 1, 'img\\thucdon\\ga_nuong_mat_ong.jpg', 60, N'Gà nướng phủ sốt mật ong thơm ngon', 'MC'),
  ('MC23', N'Ramen gà cay', 65000, N'tô', 1, 'img\\thucdon\\ramen_ga_cay.PNG', 50, N'Mì Ramen gà cay kiểu Hàn', 'MC'),
  ('MC24', N'Ramen hải sản', 78000, N'tô', 1, 'img\\thucdon\\ramen_hai_san.PNG', 50, N'Mì Ramen với tôm, mực và rau', 'MC'),
  ('MC25', N'Bibimbap chay', 68000, N'phần', 1, 'img\\thucdon\\bibimbap_chay.jpg', 40, N'Cơm trộn rau củ kiểu Hàn Quốc', 'MC'),
  ('MC26', N'Bò xào kim chi', 89000, N'phần', 1, 'img\\thucdon\\bo_xao_kimchi.jpg', 50, N'Bò xào kim chi cay ngon', 'MC'),
  ('MC27', N'Lẩu hải sản cay', 159000, N'nồi', 1, 'img\\thucdon\\lau_haisan.PNG', 40, N'Lẩu hải sản cay với rau tươi', 'MC'),
  ('MC28', N'Lẩu kim chi chay', 139000, N'nồi', 1, 'img\\thucdon\\lau_kimchi_chay.jpg', 40, N'Lẩu kim chi chay thanh mát', 'MC'),
  ('MC29', N'Gà xào tỏi', 72000, N'phần', 1, 'img\\thucdon\\ga_xao_toi.jpg', 60, N'Gà xào tỏi thơm ngon', 'MC'),
  ('MC30', N'Ba chỉ heo sốt cay', 79000, N'phần', 1, 'img\\thucdon\\ba_chi_sot_cay.jpg', 50, N'Ba chỉ heo sốt cay kiểu Hàn', 'MC'),
  ('MC31', N'Cá hồi nướng sốt Hàn', 150000, N'phần', 1, 'img\\thucdon\\ca_hoi_nuong.jpg', 40, N'Cá hồi nướng với sốt Hàn Quốc', 'MC'),
  ('MC32', N'Sườn bò nướng Hàn Quốc', 140000, N'phần', 1, 'img\\thucdon\\suon_bo_nuong.jpg', 40, N'Sườn bò nướng mềm ngon', 'MC'),
  ('MC33', N'Gà sốt chua ngọt', 70000, N'phần', 1, 'img\\thucdon\\ga_chua_ngot.jpg', 50, N'Gà chiên sốt chua ngọt', 'MC'),
  ('MC34', N'Bánh xèo Hàn Quốc', 65000, N'phần', 1, 'img\\thucdon\\banh_xeo_hq.jpg', 50, N'Bánh xèo giòn với hải sản', 'MC'),
  ('MC35', N'Mì cay phô mai', 78000, N'tô', 1, 'img\\thucdon\\mi_cay_pho_mai.jpg', 50, N'Mì cay phủ phô mai kéo sợi', 'MC');
  

INSERT INTO MonAn (maMon, tenMon, gia, donViTinh, trangThai, hinhAnh, soLuong, moTa, maLoaiMon)
VALUES
  ('MK01', N'Kimbab truyền thống', 39000, N'cuộn', 1, 'img\\thucdon\\kimbab_truyen_thong.jpg', 100, N'Kimbab cuộn rau củ và trứng', 'MK'),
  ('MK02', N'Kimbab trứng cuộn', 42000, N'cuộn', 1, 'img\\thucdon\\kimbab_trung.jpg', 90, N'Kimbab chiên giòn bọc trứng', 'MK'),
  ('MK03', N'Mandu chiên', 49000, N'đĩa', 1, 'img\\thucdon\\mandu_chien.jpg', 80, N'Há cảo Hàn Quốc chiên giòn', 'MK'),
  ('MK04', N'Mandu hấp', 49000, N'đĩa', 1, 'img\\thucdon\\mandu_hap.jpg', 80, N'Há cảo hấp mềm nhân thịt và rau', 'MK'),
  ('MK05', N'Salad Gimji', 45000, N'đĩa', 1, 'img\\thucdon\\salad_gimji.PNG', 70, N'Salad rau củ sốt mè rang', 'MK'),
  ('MK06', N'Khoai tây chiên', 39000, N'đĩa', 1, 'img\\thucdon\\khoai_chien.PNG', 90, N'Khoai tây chiên giòn vàng', 'MK'),
  ('MK07', N'Kimchi Hàn Quốc', 25000, N'phần', 1, 'img\\thucdon\\kimchi.jpg', 100, N'Kimchi cải thảo truyền thống', 'MK'),
  ('MK08', N'Bánh hành Hàn Quốc', 45000, N'phần', 1, 'img\\thucdon\\banh_hanh.jpg', 60, N'Bánh hành chiên thơm giòn, ăn kèm nước chấm', 'MK'),
  ('MK09', N'Kimchi chiên', 35000, N'phần', 1, 'img\\thucdon\\kimchi_chien.jpg', 80, N'Kimchi chiên giòn thơm ngon', 'MK'),
  ('MK10', N'Đậu hũ chiên', 40000, N'đĩa', 1, 'img\\thucdon\\dau_hu_chien.jpg', 70, N'Đậu hũ chiên vàng giòn', 'MK'),
  ('MK11', N'Xúc xích Hàn Quốc', 50000, N'phần', 1, 'img\\thucdon\\xuc_xich_hq.jpg', 60, N'Xúc xích nướng kiểu Hàn', 'MK'),
  ('MK12', N'Khoai tây nghiền phô mai', 45000, N'đĩa', 1, 'img\\thucdon\\khoai_nghien.jpg', 70, N'Khoai tây nghiền phủ phô mai', 'MK'),
  ('MK13', N'Salad rong biển', 48000, N'đĩa', 1, 'img\\thucdon\\salad_rongbien.jpg', 60, N'Salad rong biển tươi mát', 'MK'),
  ('MK14', N'Bánh gạo chiên', 43000, N'đĩa', 1, 'img\\thucdon\\banh_gao_chien.jpg', 60, N'Bánh gạo chiên giòn', 'MK'),
  ('MK15', N'Chả cá chiên', 55000, N'đĩa', 1, 'img\\thucdon\\cha_ca_chien.jpg', 50, N'Chả cá chiên giòn ăn kèm sốt', 'MK'),
  ('MK16', N'Bắp rang bơ', 35000, N'phần', 1, 'img\\thucdon\\bap_rang_bo.jpg', 80, N'Bắp rang bơ giòn ngon', 'MK'),
  ('MK17', N'Bánh gạo cay', 40000, N'đĩa', 1, 'img\\thucdon\\banh_gao_cay.png', 60, N'Bánh gạo cay kiểu Hàn', 'MK'),
  ('MK18', N'Mì trộn cay', 50000, N'đĩa', 1, 'img\\thucdon\\mi_tron_cay.jpg', 60, N'Mì trộn cay với rau và sốt đặc trưng', 'MK');
  

INSERT INTO MonAn (maMon, tenMon, gia, donViTinh, trangThai, hinhAnh, soLuong, moTa, maLoaiMon)
VALUES
  ('DO01', N'Nước suối', 15000, N'chai', 1, 'img\\thucdon\\nuoc_suoi.jpg', 200, N'Nước suối đóng chai', 'DO'),
  ('DO02', N'Nước ngọt Coca', 20000, N'chai', 1, 'img\\thucdon\\cola.png', 200, N'Coca-Cola 330ml', 'DO'),
  ('DO03', N'Nước ngọt Pepsi', 20000, N'chai', 1, 'img\\thucdon\\pepsi.png', 200, N'Pepsi 330ml', 'DO'),
  ('DO04', N'Soda chanh', 25000, N'ly', 1, 'img\\thucdon\\soda_chanh.png', 150, N'Soda pha chanh tươi', 'DO'),
  ('DO05', N'Soda việt quất', 27000, N'ly', 1, 'img\\thucdon\\soda_vietquat.png', 150, N'Soda vị việt quất mát lạnh', 'DO'),
  ('DO06', N'Trà đào cam sả', 30000, N'ly', 1, 'img\\thucdon\\tra_dao.png', 120, N'Trà đào tươi pha cam và sả', 'DO'),
  ('DO07', N'Trà lúa mạch Boricha', 25000, N'ly', 1, 'img\\thucdon\\boricha.jpg', 100, N'Trà lúa mạch rang thanh mát', 'DO'),
  ('DO08', N'Sikhye (nước gạo ngọt)', 25000, N'ly', 1, 'img\\thucdon\\sikhye.jpg', 100, N'Nước gạo ngọt truyền thống Hàn Quốc', 'DO'),
  ('DO09', N'Sữa chuối Binggrae', 35000, N'chai', 1, 'img\\thucdon\\sua_chuoi.png', 80, N'Sữa chuối nổi tiếng Hàn Quốc', 'DO'),
  ('DO10', N'Trà sữa Hàn Quốc', 32000, N'ly', 1, 'img\\thucdon\\tra_sua_hq.jpg', 120, N'Trà sữa hương vị Hàn Quốc', 'DO'),
  ('DO11', N'Nước ép cam', 30000, N'ly', 1, 'img\\thucdon\\nuoc_ep_cam.jpg', 120, N'Nước ép cam tươi nguyên chất', 'DO'),
  ('DO12', N'Nước ép táo', 30000, N'ly', 1, 'img\\thucdon\\nuoc_ep_tao.jpg', 120, N'Nước ép táo tươi', 'DO'),
  ('DO13', N'Nước chanh muối', 25000, N'ly', 1, 'img\\thucdon\\nuoc_chanh_muoi.jpg', 150, N'Nước chanh muối giải khát', 'DO'),
  ('DO14', N'Trà sữa matcha', 35000, N'ly', 1, 'img\\thucdon\\tra_sua_matcha.jpg', 100, N'Trà sữa hương matcha thơm ngon', 'DO'),
  ('DO15', N'Trà sữa socola', 35000, N'ly', 1, 'img\\thucdon\\tra_sua_socola.jpg', 100, N'Trà sữa socola béo ngậy', 'DO');


INSERT INTO NhanVien (maNV, hoTen, ngaySinh, email, soDienThoai, gioiTinh, chucVu, ngayVaoLam, trangThai)
VALUES 
('NV001', N'Minh Duc', '1990-01-01', 'a@example.com', '0909123456', 1, N'Quản lý', '2022-01-01', 1),
('NV002', N'Minh Hoang', '1995-05-10', 'b@example.com', '0909234567', 0, N'Nhân viên', '2023-03-15', 1);

INSERT INTO NhanVien (maNV, hoTen, ngaySinh, email, soDienThoai, gioiTinh, chucVu, ngayVaoLam, trangThai)
VALUES 
('NV003', N'Nguyễn Anh Long', '1990-05-12', N'nguyenlong@gmail.com', '0905123456', 1, N'Đầu bếp', '2020-03-15', 1),
('NV004', N'Lê Thị B', '1995-09-20', N'lethib@gmail.com', '0912987654', 0, N'Nhân viên phục vụ', '2021-07-10', 1),
('NV005', N'Trần Quốc Cường', '1988-01-30', N'tranquoccuong@gmail.com', '0987456123', 1, N'Kế toán', '2019-11-25', 1),
('NV006', N'Phạm Thị Dung', '1997-08-05', N'phamthidung@gmail.com', '0933557799', 0, N'Thủ kho', '2022-02-01', 0)


INSERT INTO TaiKhoan (userName, passWord, maNV, quyenTruyCap)
VALUES 
('admin', 'admin123', 'NV001', N'Quản lý'),
('nhanvien', 'nv123', 'NV002', N'Nhân viên');


INSERT INTO KhachHang (maKH, hoTen, gioiTinh, sdt, diemTichLuy, ngayDangKy, trangThai)
VALUES 
('KH001', N'Nguyễn Văn A', 1, '0901234567', 100, '2025-01-01', 1),
('KH002', N'Lê Thị B', 0, '0912345678', 250, '2025-01-01', 1),
('KH003', N'Trần Văn C', 1, '0923456789', 50, '2025-01-01', 0),
('KH004', N'Phạm Thị D', 0, '0934567890', 300, '2025-01-01', 0),
('KH005', N'Hoàng Văn E', 1, '0945678901', 150, '2025-01-01', 1),
('KH006', N'Nam', 0, '0901234561', 0, '2025-10-16', 1),
('KH007', N'Đức', 1, '0981922517', 0, '2025-10-16', 1),
('KH008', N'Phong', 1, '0981922518', 0, '2025-10-16', 1),
('KH009', N'Ngan', 1, '0981922515', 0, '2025-10-17', 1),
('KH010', N'Kim Ngan', 0, '0981922513', 0, '2025-10-17', 1),
('KH011', N'LyLy1', 0, '0981234512', 0, '2025-10-19', 0),
('KH012', N'Khang', 1, '0981922512', 0, '2025-10-19', 1),
('KH013', N'Long', 0, '0981922578', 0, '2025-10-19', 0),
('KH014', N'Khánh', 0, '0981234561', 0, '2025-10-19', 1),
('KH015', N'Nhân', 1, '0981722212', 0, '2025-10-20', 1),
('KH016', N'Văn Bình', 1, '0901000001', 0, '2025-10-21', 1),
('KH017', N'Thị Hoa', 0, '0901000002', 0, '2025-10-21', 1),
('KH018', N'Minh Tâm', 1, '0901000003', 0, '2025-10-21', 1),
('KH019', N'Anh Tuấn', 1, '0901000004', 0, '2025-10-21', 1),
('KH020', N'Lan Hương', 0, '0901000005', 0, '2025-10-21', 1),
('KH021', N'Quốc Huy', 1, '0901000006', 0, '2025-10-21', 1),
('KH022', N'Thảo Nhi', 0, '0901000007', 0, '2025-10-21', 1),
('KH023', N'Phương Nam', 1, '0901000008', 0, '2025-10-21', 1),
('KH024', N'Tuấn Kiệt', 1, '0901000009', 0, '2025-10-21', 1),
('KH025', N'Trần Thị Hoa', 0, '0907000008', 2, '2025-10-22', 1),
('KH026', N'Lê Quốc Bảo', 1, '0907000009', 1, '2025-10-22', 1),
('KH027', N'Vũ Quang Minh', 1, '0907000010', 0, '2025-10-22', 1);

INSERT INTO CaLamViec (maCa, tenCa, gioVaoLam, gioTanLam, trangThai) VALUES
('CA001', N'Ca sáng', '06:00:00', '14:00:00', 1),
('CA002', N'Ca chiều', '14:00:00', '22:00:00', 1),
('CA003', N'Ca đêm', '22:00:00', '06:00:00', 1),
('CA004', N'Ca cuối tuần', '10:00:00', '23:00:00', 1);


INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, viTri) VALUES
('KV001', N'Khu VIP', N'Tầng 2'),
('KV002', N'Khu gia đình', N'Tầng 1'),
('KV003', N'Khu couple', N'Tầng 3'),
('KV004', N'Khu BBQ ngoài trời', N'Sân thượng'),
('KV005', N'Phòng riêng Hanok', N'Tầng trệt');

INSERT INTO LoaiBanAn(maLoaiBan, tenLoaiBan)
VALUES
('LB001', N'VIP'),
('LB002', N'Gia đình'),
('LB003', N'Couple'),
('LB004', N'BBQ'),
('LB005', N'Phòng riêng');


INSERT INTO BanAn (maBan, tenBan, soLuongCho, trangThai, maKhuVuc, ghiChu, maLoaiBan)
VALUES
('B001', N'VIP Seoul', 8, N'Đang sử dụng', 'KV001', N'Bàn tròn, có karaoke riêng', 'LB001'),
('B002', N'VIP Busan', 6, N'Đang sử dụng', 'KV001', N'Bàn vuông, view đẹp', 'LB001'),
('B003', N'VIP Jeju', 10, N'Đã đặt', 'KV001', N'Bàn lớn, phòng riêng biệt', 'LB001'),
('B004', N'Family 1', 6, N'Đang dọn', 'KV002', N'Bàn tròn, ghế cao cho trẻ em', 'LB002'),
('B006', N'Family 3', 4, N'Đang sử dụng', 'KV002', N'Bàn vuông nhỏ', 'LB002'),
('B007', N'Family 4', 10, N'Trống', 'KV002', N'Bàn tròn lớn', 'LB002'),
('B008', N'Couple 1', 2, N'Đã đặt', 'KV003', N'Bàn nhỏ, ánh sáng lãng mạn', 'LB003'),
('B009', N'Couple 2', 2, N'Trống', 'KV003', N'Bàn tròn nhỏ, có nến', 'LB003'),
('B010', N'Couple 3', 2, N'Đang sử dụng', 'KV003', N'Bàn vuông, ghế sofa', 'LB003'),
('B011', N'BBQ Terrace 1', 6, N'Trống', 'KV004', N'Bàn nướng than hoa', 'LB004');


INSERT INTO PhieuDatBan (maPhieuDat, maKH, maNV, maBan, ngayDat, soNguoi, soTienCoc, ghiChu, trangThai)
VALUES
('PD1024001', 'KH007', 'NV002', 'B001', '2025-10-24 19:30:00', 7, 200000.00, N'', N'Đã xác nhận'),
('PD1024002', 'KH016', 'NV002', 'B009', '2025-10-24 19:31:00', 2, 150000.00, N'', N'Đã xác nhận'),
('PD1024003', 'KH017', 'NV002', 'BA016', '2025-10-25 21:11:00', 5, 235000.00, N'gggggggg', N'Đã hủy'),
('PD1024004', 'KH018', 'NV002', 'B017', '2025-10-24 21:34:47', 8, 0.00, N'', N'Đã hủy'),
('PD1024005', 'KH007', 'NV002', 'BA016', '2025-10-24 21:46:00', 7, 300000.00, N'xxxxxxx', N'Đã xác nhận'),
('PD1026001', 'KH019', 'NV002', 'BA016', '2025-10-27 23:43:00', 4, 130000.00, N'da coc', N'Đã xác nhận'),
('PD1027001', 'KH007', 'NV002', 'B011', '2025-10-27 10:37:00', 8, 150000.00, N'tiec sinh nhat', N'Đã xác nhận'),
('PD1027002', 'KH023', 'NV002', 'B007', '2025-10-27 11:26:00', 10, 210000.00, N'tiec hop lop', N'Đã xác nhận'),
('PD1027004', 'KH024', 'NV002', 'B009', '2025-10-27 15:56:00', 3, 140000.00, N'hppppppp', N'Đã xác nhận'),
('PD1027003', 'KH023', 'NV002', 'B007', '2025-10-27 13:43:00', 10, 210000.00, N'tiec hop lop', N'Đã xác nhận'),
('PD1027005', 'KH025', 'NV002', 'B001', '2025-10-27 17:52:00', 8, 0.00, N'', N'Đã xác nhận'),
('PD1027007', 'KH027', 'NV002', 'BA016', '2025-10-27 21:38:00', 8, 90000.00, N'', N'Đã xác nhận'),
('PD1027006', 'KH026', 'NV002', 'B016', '2025-10-27 17:58:00', 8, 90000.00, N'', N'Đã xác nhận');

INSERT INTO HoaDon 
(maHoaDon, maBan, maKH, maNV, maPhieuDat, ngayLapHoaDon, thueVAT, tongTien, maKhuyenMai, trangThai, tienCoc)
VALUES
('HD1024001', 'B001', 'KH007', 'NV002', 'PD1024001', '2025-10-24 19:33:09.810', 0.10, 0.00, NULL, N'Chưa thanh toán', 200000.00),
('HD1024002', 'B009', 'KH016', 'NV002', 'PD1024002', '2025-10-24 19:35:04.773', 0.10, 229229.00, NULL, N'Đã thanh toán', 150000.00),
('HD1026001', 'BA016', 'KH007', 'NV002', 'PD1024005', '2025-10-26 22:46:41.113', 0.10, 280280.00, NULL, N'Đã thanh toán', 300000.00),
('HD1026002', 'BA016', 'KH019', 'NV002', 'PD1026001', '2025-10-26 23:49:19.723', 0.10, 141141.00, NULL, N'Đã thanh toán', 130000.00),
('HD1027001', 'B011', 'KH007', 'NV002', 'PD1027001', '2025-10-27 10:11:34.037', 0.10, 155155.00, NULL, N'Đã thanh toán', 150000.00),
('HD1027002', 'B007', 'KH023', 'NV002', 'PD1027003', '2025-10-27 13:48:06.190', 0.10, 131131.00, NULL, N'Đã thanh toán', 210000.00),
('HD1027003', 'B009', 'KH024', 'NV002', 'PD1027004', '2025-10-27 15:56:56.123', 0.10, 0.00, NULL, N'Chưa thanh toán', 140000.00),
('HD1027004', 'B001', 'KH025', 'NV002', 'PD1027005', '2025-10-27 17:52:57.183', 0.10, 0.00, NULL, N'Chưa thanh toán', 0.00),
('HD1027005', 'B016', 'KH026', 'NV002', 'PD1027006', '2025-10-27 17:58:28.750', 0.10, 175175.00, NULL, N'Đã thanh toán', 90000.00),
('HD1027006', 'BA016', 'KH027', 'NV002', 'PD1027007', '2025-10-27 21:38:28.307', 0.10, 170170.00, NULL, N'Đã thanh toán', 90000.00);
GO

INSERT INTO ChiTietHoaDon (maHoaDon, maMon, soLuong, donGia, thanhTien, ghiChu)
VALUES
('HD1024001', 'MC01', 2, 55000, 110000, N''),
('HD1024001', 'DO02', 1, 20000, 20000, N''),
('HD1027001', 'MC03', 1, 79000, 79000, N''),
('HD1027001', 'MC08', 2, 75000, 150000, N''),
('HD1027002', 'DO01', 1, 18000, 18000, N''),
('HD1027002', 'MC09', 1, 95000, 95000, N''),
('HD1027003', 'MC04', 1, 89000, 89000, N''),
('HD1027004', 'MC02', 2, 65000, 130000, N''),
('HD1027005', 'MK05', 2, 40000, 80000, N''),
('HD1027006', 'MC03', 1, 79000, 79000, N''),
('HD1027006', 'MK06', 1, 39000, 39000, N'');


--Kiểm tra lại hết bảng
select * from KhuVuc
select * from NhanVien
select * from TaiKhoan
select * from CaLamViec
select * from KhachHang
select * from LoaiMonAn
select * from MonAn 
select * from LoaiBanAn
select * from BanAn
select * from PhieuDatBan
select * from ChiTietPhieuDat
select * from HoaDon
select * from ChiTietHoaDon

UPDATE MonAn
SET soluong = 0
WHERE maMon not LIKE 'DO%';

select * from KhachHang

