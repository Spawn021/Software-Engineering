package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.base.user.Admin;
import project.base.user.Bartender;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static project.LogIn.monitor;

public class ThemNguyenLieuController {

    private Stage stage;


    private String sta;

    @FXML
    private Button imageBtn;

    @FXML
    private Text uploadTxt;

    @FXML
    private TextField tenNguyenLieu;

    @FXML
    private TextField nhaCungCap;

    @FXML
    private ToggleGroup status;
    @FXML
    public Button backBtn;

    @FXML
    void uploadImageBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../screen/ThemNguyenLieu.fxml"));
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
        Image img = new Image(String.valueOf(file));
        ImageView view = new ImageView(img);
        view.setFitHeight(200);
        view.setFitWidth(200);
        // Hide text
        uploadTxt.setVisible(false);
        uploadTxt.setText(String.valueOf(file));
        imageBtn.setGraphic(view);

        System.out.println(imageBtn.getText());

    }

    @FXML
    void addNewBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println(uploadTxt.getText());
        if (tenNguyenLieu.getText().equals("") || nhaCungCap.getText().equals("") || uploadTxt.getText().equals("Upload image")) {
            // Notification
            JOptionPane.showMessageDialog(null, "Bạn phải điền đầy đủ thông tin nguyên liệu !", "Denial", 0);
        }

        else {
            // Check trạng thái
            if (String.valueOf(status.getSelectedToggle()).contains("Còn hàng")) {
                sta = "Con hang";
            }
            else if (String.valueOf(status.getSelectedToggle()).contains("Hết hàng")) {
                sta = "Het hang";
            }
            else {
                sta = null;
            }

            // Check file ảnh
            String imgPath = uploadTxt.getText();
            imgPath = imgPath.substring(imgPath.lastIndexOf("\\") + 1);
            if(imgPath.length() > 20) {
                imgPath = imgPath.substring(imgPath.length() - 20);
            }

            if (monitor.getBartender() != null) {
                Bartender user2 = monitor.getBartender();
                user2.add_ingredient(user2.getUsername(), tenNguyenLieu.getText(), nhaCungCap.getText(), imgPath, sta);
            }
            else if (monitor.getAdmin() != null){
                Admin user2 = monitor.getAdmin();
                user2.add_ingredient(user2.getUsername(), tenNguyenLieu.getText(), nhaCungCap.getText(), imgPath, sta);
            }

            else if (monitor.getCashier() != null){
                JOptionPane.showMessageDialog(null, "Chỉ có Quản Lý và Pha Chế được phép thêm đồ uống", "No Permission", 0);
            }

            tenNguyenLieu.setText("");
            nhaCungCap.setText("");


            // Notification
            JOptionPane.showMessageDialog(null, "Topping đã được thêm thành công", "Notification", 1);
        }
    }

    @FXML
    void backBtnOnClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/Kho.fxml")));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
