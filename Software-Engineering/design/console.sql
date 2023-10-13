/*
drop database if exists hustmilktea;
create database hustmilktea;
*/

drop table if exists ToppingTrongHoaDon;
drop table if exists ThanhPhanHoaDon;
drop table if exists HoaDon;
drop table if exists ThanhPhanTopping;
drop table if exists Topping;
drop table if exists ThanhPhanDoUong;
drop table if exists GiaDoUong;
drop table if exists DoUong;
drop table if exists lichsudatnguyenlieu;
drop table if exists NguyenLieu;
drop table if exists LichSuLamViec;
drop table if exists NhanVien;
drop table if exists TienLuong;

create table TienLuong (
    chucVu      varchar(20) not null,
    caLam       varchar(20) not null,
    tienCaLam   int         not null,
    constraint pk_tienluong primary key (chucVu, caLam),
    constraint check_chucvu check ( chucVu = 'Quan Ly' or chucVu = 'Thu Ngan' or chucVu = 'Pha Che' ),
    constraint check_calam check ( caLam = 'Sang' or caLam = 'Chieu' )
);

create table NhanVien (
    tenDangNhap     varchar(20) not null,
    tenNhanVien     varchar(20) not null,
    sdt             varchar(10) not null,
    matKhau         varchar(20) not null,
    anhDaiDien      varchar(20) not null,
    chucVu          varchar(20) not null,
    caLam           varchar(20) not null,
    constraint pk_nhanvien primary key (tenDangNhap),
    constraint fk_nhanvien_tienluong foreign key (chucVu, caLam) references TienLuong(chucVu, caLam)
);

create table LichSuLamViec (
    tenDangNhap     varchar(20) not null,
    ngayLam         date        not null,
    daTraLuong      bool        not null,
    constraint pk_lichsulamviec primary key (tenDangNhap, ngayLam),
    constraint fk_lichsulamviec_nhanvien foreign key (tenDangNhap) references NhanVien(tenDangNhap)
);

create table NguyenLieu (
    idNguyenLieu   varchar(20) not null,
    tenNguyenLieu   varchar(20) not null,
    nhaCungCap      varchar(20) not null,
    trangThai       varchar(20) not null,
    constraint pk_nguyenlieu primary key (idNguyenLieu),
    constraint check_trangthai check (trangThai = 'Con hang' or trangThai = 'Sap het' or trangThai = 'Het hang' )
);

create table DoUong (
    idDoUong    varchar(20) not null,
    tenDoUong   varchar(20) not null,
    anh         varchar(20) not null,
    constraint pk_douong primary key (idDoUong)
);

create table GiaDoUong (
    idDoUong   varchar(20) not null,
    size        varchar(1)  not null,
    giaDoUong   int,
    constraint pk_giadouong primary key (idDoUong, size),
    constraint fk_giadouong_douong foreign key (idDoUong) references DoUong(idDoUong),
    constraint check_size check ( size = 'M' or size = 'L' )
);

create table ThanhPhanDoUong (
    idDoUong       varchar(20) not null,
    idNguyenLieu   varchar(20) not null,
    constraint pk_thanhphandouong primary key (idDoUong, idNguyenLieu),
    constraint fk_thanhphandouong_douong foreign key (idDoUong) references DoUong(idDoUong),
    constraint fk_thanhphandouong_nguyenlieu foreign key (idNguyenLieu) references NguyenLieu(idNguyenLieu)
);

create table Topping (
    idTopping  varchar(20) not null,
    tenTopping  varchar(20) not null,
    giaTopping  int,
    anh         varchar(20) not null,
    constraint pk_topping primary key (idTopping)
);

create table ThanhPhanTopping (
    idTopping      varchar(20) not null,
    idNguyenLieu   varchar(20) not null,
    constraint pk_thanhphantopping primary key (idTopping, idNguyenLieu),
    constraint fk_thanhphantopping_topping foreign key (idTopping) references Topping(idTopping),
    constraint fk_thanhphantopping_nguyenlieu foreign key (idNguyenLieu) references NguyenLieu(idNguyenLieu)
);

create table HoaDon (
    maHoaDon        varchar(36) not null,
    tenKhachHang    varchar(20) not null,
    tenDangNhap     varchar(20) not null,
    soOrder         int         not null,
    khachDua        int         not null,
    trangThai       varchar(20) not null,
    thoigian        date        not null,
    constraint pk_hoadon primary key (maHoaDon),
    constraint fk_hoadon_nhanvien foreign key (tenDangNhap) references NhanVien(tenDangNhap),
   constraint check_trangthai check ( trangThai = 'Dang chuan bi' or trangThai = 'Da giao' or trangThai= 'Huy')
);

create table ThanhPhanHoaDon (
    maHoaDon        varchar(36) not null,
    buyID           SERIAL not null,
    idDoUong       varchar(20) not null,
    size            varchar(1)  not null,
    da              int,
    duong           int,
    soLuong         int         not null,
    constraint pk_thanhphanhoadon primary key (maHoaDon, buyID),
    constraint fk_thanhphanhoadon_hoadon foreign key (maHoaDon) references HoaDon(maHoaDon),
    constraint fk_thanhphanhoadon_giadouong foreign key (idDoUong, size) references GiaDoUong(idDoUong, size)
);

create table ToppingTrongHoaDon (
    maHoaDon        varchar(36) not null,
    buyID           int not null,
    idTopping      varchar(20),
    constraint pk_toppingtronghoadon primary key (maHoaDon, buyID, idTopping),
    constraint fk_toppingtronghoadon_thanhphanhoadon foreign key (maHoaDon, buyID) references ThanhPhanHoaDon(maHoaDon, buyID),
    constraint fk_toppingtronghoadon_topping foreign key (idTopping) references Topping(idTopping)
);
create table LichSuDatNguyenLieu (
    idNguyenLieu    varchar(20) not null,
    thoigian        timestamp without time zone,
    tenDangNhap     varchar(20) not null,
    soLuong         int not null,
    constraint fk_hoadon_nhanvien foreign key (tenDangNhap) references NhanVien(tenDangNhap),
    constraint fk_lichsudatnguyenlieu_nguyenlieu foreign key (idNguyenLieu) references NguyenLieu(idNguyenLieu)
);

TRUNCATE Table lichsudatnguyenlieu, lichsulamviec, nhanvien, tienluong, hoadon, thanhphanhoadon, toppingtronghoadon;

INSERT INTO tienluong(chucvu, calam, tiencalam)
VALUES ('Quan Ly', 'Sang', 80),
       ('Quan Ly', 'Chieu', 100),
       ('Thu Ngan', 'Sang', 20),
       ('Thu Ngan', 'Chieu', 40),
       ('Pha Che', 'Sang', 30),
       ('Pha Che', 'Chieu', 50)
       ;

ALTER TABLE hoadon ALTER COLUMN mahoadon TYPE varchar(36);
Alter table nhanvien alter column tennhanvien type varchar(40);
ALTER TABLE thanhphanhoadon ALTER COLUMN mahoadon TYPE varchar(36);
ALTER TABLE toppingtronghoadon ALTER COLUMN mahoadon TYPE varchar(36);
ALTER TABLE thanhphanhoadon ALTER COLUMN da TYPE real;
ALTER TABLE thanhphanhoadon ALTER COLUMN duong TYPE real;
ALTER TABLE nguyenlieu ADD COLUMN anh varchar(20);
ALTER TABLE douong ADD COLUMN onmenu boolean;
ALTER TABLE topping ADD COLUMN onmenu boolean;
ALTER TABLE nhanvien ADD COLUMN active boolean;
ALTER TABLE nguyenlieu ADD COLUMN dongia int;


ALTER TABLE hoadon ALTER Column thoigian type timestamp without time zone;
ALTER TABLE hoadon ALTER Column thoigian SET DEFAULT NOW() AT TIME ZONE 'WAST';
ALTER TABLE lichsudatnguyenlieu ALTER Column thoigian SET DEFAULT NOW() AT TIME ZONE 'WAST';

select date(thi.thoigian), sum(thanhtien) from (
                                                   select hoadon.thoigian, (sum(giaTopping) + giaDoUong)*soluong as thanhtien from hoadon
                                                                                                                                       inner join thanhphanhoadon t on hoadon.maHoaDon = t.mahoadon
                                                                                                                                       inner join giadouong GDU on GDU.idDoUong = t.idDoUong and GDU.size = t.size
                                                                                                                                       inner join toppingtronghoadon t2 on t.buyID = t2.buyid and t.maHoaDon = t2.maHoaDon
                                                                                                                                       inner join topping t3 on t2.idTopping = t3.tentopping
                                                   group by hoadon.maHoaDon, t.buyID, giaDoUong, soluong
                                               ) as thi group by date(thi.thoigian);




select extract(month from thi.thoigian) as thang, sum(thanhtien) as tong from (
                                                                                  select hoadon.thoigian, (sum(giaTopping) + giaDoUong)*soluong as thanhtien from hoadon
                                                                                                                                                                      inner join thanhphanhoadon t on hoadon.maHoaDon = t.mahoadon
                                                                                                                                                                      inner join giadouong GDU on GDU.idDoUong = t.idDoUong and GDU.size = t.size
                                                                                                                                                                      inner join toppingtronghoadon t2 on t.buyID = t2.buyid and t.maHoaDon = t2.maHoaDon
                                                                                                                                                                      inner join topping t3 on t2.idTopping = t3.idTopping
                                                                                  group by hoadon.maHoaDon, t.buyID, giaDoUong, soluong
                                                                              ) as thi group by extract(month from thi.thoigian) having extract(month from thi.thoigian) >= extract(month from current_date) -1
order by thang desc;

select DU.tenDoUong, sum(soluong) as tong from thanhphanhoadon
                                                inner join HoaDon HD on HD.maHoaDon = ThanhPhanHoaDon.maHoaDon
                                       inner join DoUong DU on ThanhPhanHoaDon.idDoUong = DU.idDoUong
where thoigian <= ('now'::timestamp) at time zone 'utc' at time zone 'wast' and thoigian > ('now'::timestamp - '1 week'::interval) at time zone 'utc' at time zone 'wast'
  and trangThai = 'Da giao'
group by DU.idDoUong
order by sum(soluong) desc;

CREATE OR REPLACE FUNCTION datdouong_notify()
    RETURNS trigger AS
$BODY$
BEGIN
    PERFORM pg_notify('DatDoUong', 'fired by FUNCTION');
    --NOTIFY mymessage, 'fired by NOTIFY';
    RETURN NULL;
END;
$BODY$
    LANGUAGE plpgsql VOLATILE
                     COST 100;

CREATE OR REPLACE FUNCTION hoadon_notify()
    RETURNS trigger AS
$BODY$
BEGIN
    PERFORM pg_notify('HoaDon', 'fired by FUNCTION');
    --NOTIFY mymessage, 'fired by NOTIFY';
    RETURN NULL;
END;
$BODY$
    LANGUAGE plpgsql VOLATILE
                     COST 100;

CREATE TRIGGER douong_notify
    AFTER INSERT OR UPDATE
    ON douong
    EXECUTE PROCEDURE datdouong_notify();

drop trigger douong_notify on douong;

CREATE TRIGGER topping_notify
    AFTER INSERT OR UPDATE
    ON topping
EXECUTE PROCEDURE datdouong_notify();

CREATE TRIGGER nguyenlieu_notify
    AFTER INSERT OR UPDATE
    ON nguyenlieu
EXECUTE PROCEDURE datdouong_notify();

CREATE TRIGGER hoadon_notify
    AFTER INSERT OR UPDATE
    ON hoadon
EXECUTE PROCEDURE hoadon_notify();

select 6*count(*) from lichsulamviec where tendangnhap = 'doubleK24' and ngaylam >= date_trunc('month', CURRENT_DATE);
select * from hoadon where thoigian::date = '2022-07-11' order by thoigian;


select sum(giatopping) + giadouong from giadouong g, topping, douong d
               where g.idDoUong = d.iddouong and
                     tenTopping in ('') and
                     d.tendouong = 'Trà sữa Hạt Sen' and size = 'M'
                                   group by g.idDoUong, size;

select * from toppingtronghoadon inner join topping t on ToppingTrongHoaDon.idTopping = t.idtopping
-- select n.tennguyenlieu, tenTopping from thanhphantopping inner join nguyenlieu n on ThanhPhanTopping.tenNguyenLieu = n.tennguyenlieu
-- where tenTopping in ('Hạt ngọc trai', 'Hạt châu xanh') and n.trangThai = 'Het hang'
-- union
-- select n2.tennguyenlieu, tenDoUong from thanhphandouong inner join nguyenlieu n2 on ThanhPhanDoUong.tenNguyenLieu = n2.tennguyenlieu
-- where tendouong = 'Trà sữa Hai Nắng' and n2.trangThai = 'Het hang';
-- ;
-- INSERT INTO nhanvien(tendangnhap, tennhanvien, matkhau, sdt, anhdaidien, chucvu, calam)
-- VALUES ('hungpham', 'Phạm Thành Hưng', '123456','0971169255','hung.png', 'Quan Ly', 'Sang');
--
-- INSERT INTO nguyenlieu(tenNguyenLieu, nhaCungCap, trangThai)
-- VALUES ('Trân Châu', 'CTy Trân Châu Hà Nội', 'Con hang');

-- SELECT * FROM nhanvien WHERE tendangnhap = 'hung' AND matkhau = 'u2ouofs';






