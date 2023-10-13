package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import project.UI.NhanVien;
import project.base.DBUtil;

import java.io.IOException;
import java.sql.SQLException;

public class NhanVienController {
    @FXML private Button chinhSuaNhanVien;

    @FXML private Text luongChuaTra;

    private String tendangnhap;

    @FXML
    private Button traLuong;

    @FXML
    private Circle anhNhanVien;

    @FXML
    private Text caLam;

    @FXML
    private Text thongTin;

    @FXML
    private CheckBox active;

    private BaseController baseController;

    public void setBaseController(BaseController baseController) {
        this.baseController = baseController;
    }

    @FXML
    void traLuongBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        luongChuaTra.setText("0");
        String command1 = String.format("UPDATE lichsulamviec set datraluong = '%b' WHERE tendangnhap = '%s'",true,thongTin.getText().substring(thongTin.getText().indexOf("\n")+12));
        DBUtil.dbExecuteUpdate(command1);
    }
    @FXML
    void onActiveBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        traLuong.setDisable(!active.isSelected());
        String command2 = String.format("UPDATE nhanvien SET active = '%s' WHERE tendangnhap = '%s'",
                active.isSelected(), tendangnhap);
        DBUtil.dbExecuteUpdate(command2);
    }

    @FXML
    void chinhSuaNhanVienBtn(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        baseController.toggleScreen(baseController.chinhsuaNVScreen);

        ChinhSuaNhanVienController chinhSuaNhanVienController = baseController.mainEditEmplController;
        int nIndex = thongTin.getText().indexOf("\n");
        chinhSuaNhanVienController.getHoVaTen().setText(thongTin.getText().substring(0,nIndex));
        chinhSuaNhanVienController.getSoDienThoai().setText(thongTin.getText().substring(nIndex+1,nIndex+11));
        switch (caLam.getText().substring(0,caLam.getText().indexOf("\n"))){
            case ("Pha Che"):
                chinhSuaNhanVienController.getChucVu().selectToggle(chinhSuaNhanVienController.getPhaCheBtn());
            case ("Thu Ngan"):
                chinhSuaNhanVienController.getChucVu().selectToggle(chinhSuaNhanVienController.getThuNganBtn());
            case ("Quan Ly"):
                chinhSuaNhanVienController.getChucVu().selectToggle(chinhSuaNhanVienController.getQuanLyBtn());
        }
        if (caLam.getText().substring(caLam.getText().indexOf("\n")) == "Sang"){
            chinhSuaNhanVienController.getCaLam().selectToggle(chinhSuaNhanVienController.getSangBtn());
        }else{
            chinhSuaNhanVienController.getCaLam().selectToggle(chinhSuaNhanVienController.getChieuBtn());
        }
        chinhSuaNhanVienController.getUsername().setText(thongTin.getText().substring(nIndex+12));
        chinhSuaNhanVienController.open();
    }

    public void setData(NhanVien nhanVien){
        anhNhanVien.setStroke(Color.SEAGREEN);
        Image im;
        try {
            im = new Image(nhanVien.getAnhNhanVienSrc());
        } catch (Exception e){
            im = new Image("/project/resources/image/icons/default-image.jpg");
        }
        anhNhanVien.setFill(new ImagePattern(im));
        anhNhanVien.setEffect(new DropShadow(+25d,0d,+2d,Color.DARKSEAGREEN));

        luongChuaTra.setText(nhanVien.getLuongChuaTra());
        caLam.setText(nhanVien.getCaLam());
        thongTin.setText(nhanVien.getThongTin());
        active.setSelected(nhanVien.getActive());
        tendangnhap = nhanVien.getTendangnhap();
        traLuong.setDisable(!active.isSelected());
    }
}
