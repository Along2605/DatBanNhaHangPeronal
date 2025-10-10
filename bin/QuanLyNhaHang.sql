-- Tạo cơ sở dữ liệu
CREATE DATABASE QuanLyNhaHang;
GO

USE QuanLyNhaHang;
GO

-- Bảng KhuVuc
CREATE TABLE KhuVuc (
    maKhuVuc NVARCHAR(10) PRIMARY KEY,
    tenKhuVuc NVARCHAR(50),
    viTri NVARCHAR(50)
);
GO

-- Bảng CaLamViec
CREATE TABLE CaLamViec (
    maCa NVARCHAR(10) PRIMARY KEY,
    gioVaoLam TIME,
    gioTanLam TIME,
    trangThai BIT
);
GO

-- Bảng KhachHang (đã bỏ cột email)
CREATE TABLE KhachHang (
    maKH NVARCHAR(10) PRIMARY KEY,
    hoTen NVARCHAR(50),
    gioiTinh BIT,
    sdt NVARCHAR(15),
    diemTichLuy INT,
	ngayDangKy DATETIME DEFAULT GETDATE(),
    trangThai BIT DEFAULT 1
);
GO

-- Bảng NhanVien
CREATE TABLE NhanVien (
    maNV NVARCHAR(10) PRIMARY KEY,
    hoTen NVARCHAR(50),
    ngaySinh DATE,
    email NVARCHAR(50),
    soDienThoai NVARCHAR(15),
    gioiTinh BIT,
    chucVu NVARCHAR(50),
    ngayVaoLam DATE,
    trangThai BIT
);
GO

-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    userName NVARCHAR(50) PRIMARY KEY,
    passWord NVARCHAR(50),
    maNV NVARCHAR(10),
    quyenTruyCap NVARCHAR(20),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);
GO

-- Bảng MonAn
CREATE TABLE MonAn (
    maMon NVARCHAR(10) PRIMARY KEY,
    tenMon NVARCHAR(50),
    gia DECIMAL(10,2),
    donViTinh NVARCHAR(20),
    trangThai BIT,
    hinhAnh NVARCHAR(200),
    soLuong INT,
    moTa NVARCHAR(200),
    loaiMon NVARCHAR(50)
);
GO

-- Bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKhuyenMai NVARCHAR(10) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(50),
    phanTramGiam DECIMAL(5,2),
    ngayBatDau DATETIME,
    ngayKetThuc DATETIME,
    loaiKhuyenMai NVARCHAR(50),
    trangThai BIT
);
GO

-- Bảng BanAn
CREATE TABLE BanAn (
    maBan NVARCHAR(10) PRIMARY KEY,
    tenBan NVARCHAR(40) NOT NULL,
    soLuongCho INT NOT NULL,
    loaiBan NVARCHAR(20),
    trangThai NVARCHAR(50) DEFAULT N'Trống',
    maKhuVuc NVARCHAR(10),
    ghiChu NVARCHAR(200),

    FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc) ON DELETE CASCADE
);
GO

-- Bảng LichLamViec
CREATE TABLE LichLamViec (
    maLich NVARCHAR(10) PRIMARY KEY,
    ngayLamViec DATE,
    maNV NVARCHAR(10),
    maCa NVARCHAR(10),
    trangThai BIT,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE,
    FOREIGN KEY (maCa) REFERENCES CaLamViec(maCa) ON DELETE CASCADE
);
GO

-- Bảng PhieuDatBan
CREATE TABLE PhieuDatBan (
    maPhieuDat NVARCHAR(10) PRIMARY KEY,
    maKH NVARCHAR(10),
    maNV NVARCHAR(10),
    maBan NVARCHAR(10),
    ngayDat DATETIME NOT NULL,
    soNguoi INT NOT NULL,
    soTienCoc DECIMAL(10,2) DEFAULT 0,
    ghiChu NVARCHAR(200),
    trangThai NVARCHAR(20) DEFAULT N'Chờ xác nhận',
    
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE CASCADE,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE,
    FOREIGN KEY (maBan) REFERENCES BanAn(maBan) ON DELETE CASCADE
);

GO

-- Bảng HoaDon
CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(10) PRIMARY KEY,
    maBan NVARCHAR(10),
    maKH NVARCHAR(10),
    maNV NVARCHAR(10),
    ngayLapHoaDon DATETIME,
    thueVAT DECIMAL(5,2),
    tongTien DECIMAL(12,2),
    maKhuyenMai NVARCHAR(10),
    trangThai NVARCHAR(20) DEFAULT N'Đã thanh toán',
    FOREIGN KEY (maBan) REFERENCES BanAn(maBan) ON DELETE CASCADE,
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE CASCADE,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE,
    FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai) ON DELETE SET NULL
);

GO

-- Bảng ChiTietPhieuDat
CREATE TABLE ChiTietPhieuDat (
    maPhieuDat NVARCHAR(10),
    maMon NVARCHAR(10),
    soLuong INT,
    donGia DECIMAL(10,2),
    ghiChu NVARCHAR(200),
    PRIMARY KEY (maPhieuDat, maMon),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatBan(maPhieuDat) ON DELETE CASCADE,
    FOREIGN KEY (maMon) REFERENCES MonAn(maMon) ON DELETE CASCADE
);
GO
DROP TABLE IF EXISTS ChiTietHoaDon;
---- Bảng ChiTietHoaDon
--CREATE TABLE ChiTietHoaDon (
--    maHoaDon NVARCHAR(10),
--    maMon NVARCHAR(10),
--    soLuong INT,
--    donGia DECIMAL(10,2),
--    maKhuyenMai NVARCHAR(10) NULL, -- cần cho phép NULL nếu dùng SET NULL

--    PRIMARY KEY (maHoaDon, maMon),

--    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
--    FOREIGN KEY (maMon) REFERENCES MonAn(maMon) ON DELETE NO ACTION,
--    FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai) ON DELETE SET NULL
--);

CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(10),
    maMon NVARCHAR(10),
    soLuong INT,
    donGia DECIMAL(12,2),
    PRIMARY KEY (maHoaDon, maMon),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    FOREIGN KEY (maMon) REFERENCES MonAn(maMon) ON DELETE CASCADE
);

INSERT INTO NhanVien (maNV, hoTen, ngaySinh, email, soDienThoai, gioiTinh, chucVu, ngayVaoLam, trangThai)
VALUES 
('NV001', N'Minh Duc', '1990-01-01', 'a@example.com', '0909123456', 1, N'Quản lý', '2022-01-01', 1),
('NV002', N'Minh Hoang', '1995-05-10', 'b@example.com', '0909234567', 0, N'Nhân viên', '2023-03-15', 1);



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
('KH005', N'Hoàng Văn E', 1, '0945678901', 150, '2025-01-01', 1);




