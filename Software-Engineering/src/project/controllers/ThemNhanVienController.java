package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.base.functional.AdminInterface;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ThemNhanVienController implements AdminInterface {
    private NhanSuController nhanSuController;

    public void setNhanSuController(NhanSuController nhanSuController) {
        this.nhanSuController = nhanSuController;
    }

    private Scene scene;
    private Stage stage;
    private Parent root;
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
    public Button backBtn;

    @FXML
    public Button imageBtn;

    static String anh = "null";



    @FXML
    void apDungBtn(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        String tenNhanVien = hoVaTen.getText();
        String sdt = soDienThoai.getText();
        String tenDangNhap = username.getText();
        String mk = matKhau.getText();
        String avatar = "images.jpeg";
        RadioButton cv0 = (RadioButton) chucVu.getSelectedToggle();
        String cv = cv0.getText();
        String cv1;
        if (cv == "Thu ngân"){
            cv1 = "Thu Ngan";
        } else if (cv == "Pha chế"){
            cv1 = "Pha Che";
        } else {
            cv1 = "Quan Ly";
        }

        RadioButton cl0 = (RadioButton) caLam.getSelectedToggle();
        String cl = cl0.getText();
        String cl1;
        if (cl == "Sáng"){
            cl1 = "Sang";
        } else {
            cl1 = "Chieu";
        }

        create_new_user("Tam",tenDangNhap,tenNhanVien,mk,sdt,avatar,cv1,cl1);
        JOptionPane.showMessageDialog(null, "Nhân viên đã được thêm thành công", "Notification", 1);
        hoVaTen.setText("");
        soDienThoai.setText("");
        username.setText("");
        matKhau.setText("");
        backBtn.fire();
        nhanSuController.initialize();
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
    }
}

