package project.UI;

import java.sql.Timestamp;

public class HoaDon {
    private String mahoadon;
    private Timestamp date;
    private String tenkhach;

    public String getTenkhach() {
        return tenkhach;
    }

    public void setTenkhach(String tenkhach) {
        this.tenkhach = tenkhach;
    }

    public String getSoorder() {
        return soorder;
    }

    public void setSoorder(String soorder) {
        this.soorder = soorder;
    }

    private String soorder;
    public String getMahoadon() {
        return mahoadon;
    }

    public void setMahoadon(String mahoadon) {
        this.mahoadon = mahoadon;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
