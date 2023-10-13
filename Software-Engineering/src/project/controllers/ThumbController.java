package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.base.DBUtil;
import project.model.ImageMain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ThumbController {
    private String idnguyenlieu;
    @FXML
    private Button trangThai;
    @FXML
    private Label tenNguyenLieu;
    @FXML
    private ImageView anhNguyenLieu;
    @FXML
    private ImageView checkMark;

    @FXML
    void trangThaiBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (trangThai.getText().equals("Hết Hàng")) {
            trangThai.setText("Còn Hàng");
            String command = String.format("UPDATE nguyenlieu SET trangthai = 'Con hang' WHERE idnguyenlieu = '%s'", idnguyenlieu);
            ResultSet result = DBUtil.dbExecuteUpdate(command);
        } else if (trangThai.getText().equals("Còn Hàng")) {
            trangThai.setText("Sắp Hết");
            String command = String.format("UPDATE nguyenlieu SET trangthai = 'Sap het' WHERE idnguyenlieu = '%s'", idnguyenlieu);
            ResultSet result = DBUtil.dbExecuteUpdate(command);
        } else if (trangThai.getText().equals("Sắp Hết")) {
            trangThai.setText("Hết Hàng");
            String command = String.format("UPDATE nguyenlieu SET trangthai = 'Het hang' WHERE idnguyenlieu = '%s'", idnguyenlieu);
            ResultSet result = DBUtil.dbExecuteUpdate(command);
        }
    }

    public void setData(ImageMain image) {
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(image.getThumbSrc())), 163, 122, false, false);
            anhNguyenLieu.setImage(img);
        } catch (Exception e) {
//            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../project/resources/image/icons/default-image.jpg")), 163, 122, false, false);
//            anhNguyenLieu.setImage(img);
        }
        trangThai.setText(image.getTrangThai());
        tenNguyenLieu.setText(image.getName());
        idnguyenlieu = image.getIdNguyenLieu();
    }

    @FXML
    public void imgOnClicked() {
        if (!checkMark.isVisible()) {
            KhoController.thumbList.add(tenNguyenLieu.getText());
            checkMark.setVisible(true);
        } else if (checkMark.isVisible()) {
            KhoController.thumbList.remove(tenNguyenLieu.getText());
            checkMark.setVisible(false);
        }
        System.out.println(String.format("Trạng thái chọn nguyên liệu: %s", KhoController.thumbList));
    }
}