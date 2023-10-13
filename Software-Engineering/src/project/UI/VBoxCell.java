package project.UI;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.base.DBUtil;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VBoxCell extends VBox {
    public boolean disable = false;
    ImageView imageView = new ImageView();
    public Label ten = new Label();
    Label gia = new Label();

    public VBoxCell(String ten, String tenanh) throws SQLException, ClassNotFoundException {
        super();
        InputStream imagestream = getClass().getResourceAsStream(String.format("../resources/image" +
                        "/TraSua/%s",
                tenanh));
        if (imagestream == null){
            imagestream = getClass().getResourceAsStream("../resources/image/icons/default-image.jpg");
        }
        Image background = new Image(imagestream, 100, 100, false, false);
        imageView.setImage(background);

        this.ten.setText(ten);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);


        ResultSet hethang =
                DBUtil.dbExecuteQuery(String.format("""
                select n2.trangthai from thanhphandouong inner join nguyenlieu n2 on ThanhPhanDoUong.idNguyenLieu = n2.idnguyenlieu
        inner join douong d on thanhphandouong.iddouong = d.iddouong
        where d.tendouong = '%s' and n2.trangThai = 'Het hang';
        """, ten));
        ResultSet saphet =
                DBUtil.dbExecuteQuery(String.format("""
                select n2.trangthai from thanhphandouong inner join nguyenlieu n2 on ThanhPhanDoUong.idNguyenLieu = n2.idnguyenlieu
        inner join douong d on thanhphandouong.iddouong = d.iddouong
        where d.tendouong = '%s' and n2.trangThai = 'Sap het';
        """, ten));
        if (hethang.next()){
            ColorAdjust desaturate = new ColorAdjust();
            desaturate.setSaturation(-1);
            this.imageView.setEffect(desaturate);
            this.getChildren().addAll(this.imageView, this.ten);
            this.disable = true;
        } else if (saphet.next()) {
            Label warning = new Label("Sắp hết");
            warning.setStyle("-fx-text-fill: #c50808");
            this.getChildren().addAll(warning, this.imageView, this.ten);
        } else {
            this.getChildren().addAll(this.imageView, this.ten);
        }
    }

    public VBoxCell(String tentopping, String tenanh, String giatien) throws SQLException, ClassNotFoundException {
        InputStream imagestream = getClass().getResourceAsStream(String.format("../resources/image" +
                        "/Topping/%s",
                tenanh));
        if (imagestream == null){
            imagestream = getClass().getResourceAsStream("../resources/image/icons/default-image.jpg");
        }
        Image background = new Image(imagestream, 100, 100, false, false);
        imageView.setImage(background);

        this.ten.setText(tentopping);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.gia.setText(giatien+".000đ");


        ResultSet hethang =
                DBUtil.dbExecuteQuery(String.format("""
                        select n.tennguyenlieu, t.tenTopping from thanhphantopping inner join nguyenlieu n on ThanhPhanTopping.idNguyenLieu = n.idnguyenlieu
        inner join topping t on thanhphantopping.idtopping = t.idtopping
        where t.tenTopping = '%s' and n.trangThai = 'Het hang';
        """, tentopping));
        ResultSet saphet =
                DBUtil.dbExecuteQuery(String.format("""
                        select n.tennguyenlieu, t.tenTopping from thanhphantopping inner join nguyenlieu n on ThanhPhanTopping.idNguyenLieu = n.idnguyenlieu
        inner join topping t on thanhphantopping.idtopping = t.idtopping
        where t.tenTopping = '%s' and n.trangThai = 'Sap het';
        """, tentopping));
        if (hethang.next()) {
            ColorAdjust desaturate = new ColorAdjust();
            desaturate.setSaturation(-1);
            this.imageView.setEffect(desaturate);
            this.getChildren().addAll(this.imageView, this.ten, this.gia);
            this.disable = true;
        } else if (saphet.next()) {
            Label warning = new Label("Sắp hết");
            warning.setStyle("-fx-text-fill: #c50808");
            this.getChildren().addAll(warning, this.imageView, this.ten, this.gia);
        } else {
            this.getChildren().addAll(this.imageView, this.ten, this.gia);
        }
    }
}
