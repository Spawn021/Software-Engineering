package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.base.DBUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChinhSuaDoUongController {
    @FXML
    private Pane paneGiaTopping;

    @FXML
    private Pane paneSize1;

    @FXML
    private TextField giaTopping;

    @FXML
    private Circle anhDoUongTopping;

    @FXML
    private TextField ten;

    @FXML
    private TextField giaSizeL;

    @FXML
    private TextField giaSizeM;

    @FXML public Button backBtn;

    @FXML public Button imageBtn;

    @FXML
    private HBox hBoxNguyenLieu;

    public String old;
    ArrayList<String> nguyenLieuHienTai = new ArrayList<>();

    public TextField getGiaTopping() {
        return giaTopping;
    }

    public TextField getTen() {
        return ten;
    }

    public TextField getGiaSizeL() {
        return giaSizeL;
    }

    public TextField getGiaSizeM() {
        return giaSizeM;
    }

    static String anh;

    public void open() throws SQLException, ClassNotFoundException{
        nguyenLieuHienTai = new ArrayList<>();
        hBoxNguyenLieu.getChildren().clear();

        String command1 = String.format("SELECT * FROM nguyenlieu");
        ResultSet result1 = DBUtil.dbExecuteQuery(command1);

        String command2 = String.format("SELECT * FROM thanhphandouong WHERE iddouong = '%s'",old);
        ResultSet result2 = DBUtil.dbExecuteQuery(command2);
        while (result2.next()) {
            nguyenLieuHienTai.add(result2.getString("idnguyenlieu"));
        }

        while (result1.next()) {
            String idNguyenLieu = result1.getString("idnguyenlieu");
            String tenNguyenLieu = result1.getString("tennguyenlieu");

            RadioButton nguyenLieuRadioBtn = new RadioButton();
            nguyenLieuRadioBtn.setText(tenNguyenLieu);
            nguyenLieuRadioBtn.setStyle("-fx-border-color:#000000");
            nguyenLieuRadioBtn.setStyle("-fx-border-width:3");
            nguyenLieuRadioBtn.setStyle("-fx-border-radius:20");
            nguyenLieuRadioBtn.setPadding(new Insets(5, 5, 5, 5));
            nguyenLieuRadioBtn.setSelected(nguyenLieuHienTai.contains(idNguyenLieu));
            nguyenLieuRadioBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);

            hBoxNguyenLieu.getChildren().add(nguyenLieuRadioBtn);
        }

        hBoxNguyenLieu.setPrefWidth(Region.USE_COMPUTED_SIZE);

        String command3 = String.format("SELECT * FROM douong WHERE iddouong = '%s'",old);
        ResultSet result3 = DBUtil.dbExecuteQuery(command3);

        if (result3.next()) {
            anhDoUongTopping.setStroke(Color.SEAGREEN);
            Image im;
            try {
                im = new Image("project/resources/image/TraSua/"+ result3.getString("anh"));
                anh = result3.getString("anh");
            } catch (Exception e){
                im = new Image("/project/resources/image/icons/default-image.jpg");
                anh = "default-image.jpg";
            }
            anhDoUongTopping.setFill(new ImagePattern(im));
            anhDoUongTopping.setEffect(new DropShadow(+25d,0d,+2d,Color.DARKSEAGREEN));
        }

        anhDoUongTopping.setVisible(true);

        Blend blend = new Blend();
        blend.setMode(BlendMode.DARKEN);
        imageBtn.setEffect(blend);
    }

    @FXML
    void doUongBtn(ActionEvent event) {

    }

    @FXML
    void toppingBtn(ActionEvent event) {

    }

    @FXML
    void apDungBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        DBUtil.dbExecuteUpdate(String.format("UPDATE giadouong set giadouong = '%s' where iddouong = '%s' AND size = 'L';",giaSizeL.getText(),old));
        DBUtil.dbExecuteUpdate(String.format("UPDATE giadouong set giadouong = '%s' where iddouong = '%s' AND size = 'M';",giaSizeM.getText(),old));
        DBUtil.dbExecuteUpdate(String.format("update douong set tendouong = '%s', anh = '%s' where iddouong = '%s';", ten.getText(),anh, old));
        DBUtil.dbExecuteUpdate(String.format("delete from thanhphandouong where iddouong = '%s';",old));

        for (Node n: hBoxNguyenLieu.getChildren()){
            RadioButton radioBtn = (RadioButton) n;
            if (radioBtn.isSelected()){
                ResultSet result3 = DBUtil.dbExecuteQuery(String.format("SELECT * FROM nguyenlieu WHERE tennguyenlieu = '%s'",radioBtn.getText()));
                if (result3.next()){
                    String command5 = String.format("INSERT INTO thanhphandouong VALUES ('%s','%s')", old, result3.getString("idnguyenlieu"));
                    DBUtil.dbExecuteUpdate(command5);
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Đồ uống đã được chỉnh sửa thành công", "Notification", 1);
        backBtn.fire();
    }

    @FXML
    void backBtn(ActionEvent event) {

    }

    @FXML
    void xoaBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        String command1 = String.format("DELETE FROM giadouong WHERE iddouong = '%s'",old);
        DBUtil.dbExecuteUpdate(command1);
        String command2 = String.format("DELETE FROM thanhphandouong WHERE iddouong = '%s'",old);
        DBUtil.dbExecuteUpdate(command2);
        String command3 = String.format("DELETE FROM douong WHERE iddouong = '%s'",old);
        DBUtil.dbExecuteUpdate(command3);

        JOptionPane.showMessageDialog(null, "Đồ uống đã được xóa thành công", "Notification", 1);
    }

    private Stage stage;
    @FXML
    void uploadImageBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
        FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilter3 = new FileChooser.ExtensionFilter("Image files (*.jpeg)","*jpeg");
        fileChooser.getExtensionFilters().addAll(extFilter, extFilter1, extFilter2, extFilter3);
        fileChooser.setTitle("Select Some Files");

        // Set thư mục bắt đầu khi mở FileChooser
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File file = fileChooser.showOpenDialog(stage);

        // Set image sau khi upload ảnh
        Image img = new Image(String.valueOf(file).substring(String.valueOf(file).indexOf("project")-1));
        ImageView view = new ImageView(img);
        view.setFitHeight(200);
        view.setFitWidth(200);
        // Hide text
        anh = String.valueOf(file).substring(String.valueOf(file).lastIndexOf("/")+1);
        imageBtn.setGraphic(view);

        anhDoUongTopping.setVisible(false);

        Blend blend = new Blend();
        blend.setMode(BlendMode.SRC_OVER);
        imageBtn.setEffect(blend);
    }
}
