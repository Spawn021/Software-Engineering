package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

public class ChinhSuaToppingController {

    @FXML public Button backBtn;

    @FXML
    private Pane paneGiaTopping;

    @FXML
    private RadioButton doUong;


    @FXML
    private ToggleGroup loai;

    @FXML
    private Circle anhDoUongTopping;

    @FXML
    private HBox hBoxNguyenLieu;

    @FXML
    private TextField ten;

    @FXML
    private RadioButton topping;

    @FXML
    private TextField giaTopping;

    @FXML public Button imageBtn;

    ArrayList<String> nguyenLieuHienTai = new ArrayList<>();
    public String old;

    public TextField getTen() {
        return ten;
    }

    public void setTen(TextField ten) {
        this.ten = ten;
    }

    public TextField getGiaTopping() {
        return giaTopping;
    }

    public void setGiaTopping(TextField giaTopping) {
        this.giaTopping = giaTopping;
    }

    @FXML
    void doUongBtn(ActionEvent event) {

    }

    @FXML
    void toppingBtn(ActionEvent event) {

    }

    @FXML
    void apDungBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        String command2 = String.format("UPDATE topping SET tentopping = '%s', giatopping = '%s', anh = '%s' WHERE " +
                "idtopping = '%s'",ten.getText(), giaTopping.getText(),anh,old);
        DBUtil.dbExecuteUpdate(command2);

        String command1 = String.format("SELECT * FROM thanhphantopping WHERE idtopping = '%s'",old);
        ResultSet result1 = DBUtil.dbExecuteQuery(command1);
        while (result1.next()) {
            nguyenLieuHienTai.add(result1.getString(2));
            String command3 = String.format("DELETE FROM thanhphantopping WHERE idtopping = '%s'",old);
            DBUtil.dbExecuteUpdate(command3);
        }

        for (Node n: hBoxNguyenLieu.getChildren()){
            RadioButton radioBtn = (RadioButton) n;
            if (radioBtn.isSelected()) {
                ResultSet result3 = DBUtil.dbExecuteQuery(String.format("SELECT * FROM nguyenlieu WHERE tennguyenlieu = '%s'",radioBtn.getText()));
                if (result3.next()){
                    String command4 = String.format("INSERT INTO thanhphantopping VALUES ('%s','%s')",old, result3.getString("idnguyenlieu"));
                    DBUtil.dbExecuteUpdate(command4);
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Topping đã được chỉnh sửa thành công", "Notification", 1);
        backBtn.fire();
    }

    public void open() throws SQLException, ClassNotFoundException{
        hBoxNguyenLieu.getChildren().clear();
        nguyenLieuHienTai = new ArrayList<>();
        String command1 = String.format("SELECT * FROM nguyenlieu");
        ResultSet result1 = DBUtil.dbExecuteQuery(command1);

        String command2 = String.format("SELECT * FROM thanhphantopping WHERE idtopping = '%s'",old);
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

        String command3 = String.format("SELECT * FROM topping WHERE tentopping = '%s'",old);
        ResultSet result3 = DBUtil.dbExecuteQuery(command3);

        while (result3.next()) {
            anhDoUongTopping.setStroke(Color.SEAGREEN);
            Image im;
            try {
                im = new Image("project/resources/image/Topping/" + result3.getString("anh"));
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
    void xoaBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        String command1 = String.format("DELETE FROM topping WHERE tentopping = '%s'",old);
        DBUtil.dbExecuteUpdate(command1);
        String command2 = String.format("DELETE FROM thanhphantopping WHERE  tentopping = '%s'",old);
        DBUtil.dbExecuteUpdate(command2);

        JOptionPane.showMessageDialog(null, "Topping đã được xóa thành công", "Notification", 1);
    }

    private Stage stage;
    static String anh;
    @FXML
    void uploadImageBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../screen/ChinhSuaNhanVien.fxml"));
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
