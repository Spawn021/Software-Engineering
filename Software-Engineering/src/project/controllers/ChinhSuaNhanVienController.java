package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.base.DBUtil;
import project.base.functional.AdminInterface;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChinhSuaNhanVienController implements AdminInterface {

    private Scene scene;
    private Stage stage;
    private Parent root;
    @FXML
    private Button pauseButton;

    @FXML public Button backBtn;

    @FXML
    private Button imageBtn;

    @FXML
    private ImageView avaImg;

    @FXML
    private VBox vboxLeft;

    private NhanSuController nhanSuController;

    public void setNhanSuController(NhanSuController nhanSuController) {
        this.nhanSuController = nhanSuController;
    }

    @FXML
    private Button playButton;

    @FXML
    private RadioButton thuNganBtn;
    @FXML
    private RadioButton phaCheBtn;
    @FXML
    private RadioButton quanLyBtn;
    @FXML
    private RadioButton sangBtn;
    @FXML
    private RadioButton chieuBtn;

    @FXML
    private TextField hoVaTen;
    @FXML
    private TextField soDienThoai;
    @FXML
    private TextField username;
    @FXML
    private PasswordField matKhau;
    @FXML
    private ToggleGroup chucVu;
    @FXML
    private ToggleGroup caLam;

    @FXML
    private Circle anhNhanVien;

    public RadioButton getThuNganBtn() {
        return thuNganBtn;
    }

    public RadioButton getPhaCheBtn() {
        return phaCheBtn;
    }

    public RadioButton getQuanLyBtn() {
        return quanLyBtn;
    }

    public RadioButton getSangBtn() {
        return sangBtn;
    }

    public RadioButton getChieuBtn() {
        return chieuBtn;
    }

    public TextField getHoVaTen() {
        return hoVaTen;
    }

    public TextField getSoDienThoai() {
        return soDienThoai;
    }

    public TextField getUsername() {
        return username;
    }

    public ToggleGroup getChucVu() {
        return chucVu;
    }

    public ToggleGroup getCaLam() {
        return caLam;
    }

    public void setMatKhau(PasswordField matKhau) {
        this.matKhau = matKhau;
    }

    public PasswordField getMatKhau() {
        return matKhau;
    }

    private String oldUsername;
    static String anh;

    public void open() throws SQLException, ClassNotFoundException {
        oldUsername = username.getText();
        String command2 = String.format("SELECT * FROM nhanvien WHERE tendangnhap = '%s'", oldUsername);
        ResultSet result2 = DBUtil.dbExecuteQuery(command2);

        if (result2.next()){
            matKhau.setText(result2.getString("matkhau"));
        }

        anhNhanVien.setStroke(Color.SEAGREEN);
        Image im;
        try {
            im = new Image("project/resources/image/Topping/" + result2.getString("anh"));
            anh = result2.getString("anh");
        } catch (Exception e){
            im = new Image("/project/resources/image/icons/default-image.jpg");
            anh = "default-image.jpg";
        }
        anhNhanVien.setFill(new ImagePattern(im));
        anhNhanVien.setEffect(new DropShadow(+25d,0d,+2d,Color.DARKSEAGREEN));

        anhNhanVien.setVisible(true);

        Blend blend = new Blend();
        blend.setMode(BlendMode.DARKEN);
        imageBtn.setEffect(blend);
    }

    @FXML
    void apDungBtn(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        String cv = "null";
        String cl;
        if (thuNganBtn.isSelected()) {
            cv = "Thu Ngan";
        }
        if (phaCheBtn.isSelected()) {
            cv = "Pha Che";
        }
        if (quanLyBtn.isSelected()) {
            cv = "Quan Ly";
        }
        if (sangBtn.isSelected()) {
            cl = "Sang";
        } else {
            cl = "Chieu";
        }
        String command2 = String.format("UPDATE nhanvien SET tendangnhap = '%s',tennhanvien = '%s', sdt = '%s', " +
                "matkhau = '%s', chucvu = '%s', calam = '%s', anhdaidien = '%s' WHERE tendangnhap = '%s'",
                username.getText(),hoVaTen.getText(),soDienThoai.getText(),matKhau.getText(),cv,cl,anh, oldUsername);
        DBUtil.dbExecuteUpdate(command2);

        JOptionPane.showMessageDialog(null, "Nhân viên đã được chỉnh sửa thành công", "Notification", 1);
        backBtn.fire();

        nhanSuController.initialize();
    }

    @FXML
    void backBtn(ActionEvent event) throws IOException {

    }
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

        anhNhanVien.setVisible(false);

        Blend blend = new Blend();
        blend.setMode(BlendMode.SRC_OVER);
        imageBtn.setEffect(blend);
    }
}

