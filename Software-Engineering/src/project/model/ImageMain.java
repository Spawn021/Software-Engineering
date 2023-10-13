package project.model;

public class ImageMain {
    private String thumbSrc;
    private String name;
    private String trangThai;
    private String idNguyenLieu;

    public String getIdNguyenLieu() { return idNguyenLieu; }
    public void setIdNguyenLieu(String id) { this.idNguyenLieu = id; }
    public String getThumbSrc() {
        return thumbSrc;
    }

    public void setThumbSrc(String thumbSrc) {
        this.thumbSrc = thumbSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

