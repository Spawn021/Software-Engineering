package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.base.DBUtil;
import project.base.functional.BartenderInterface;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ThemDoUongToppingController implements BartenderInterface {

    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    public Button backBtn;

    @FXML
    private RadioButton radioButtonSize2;

    @FXML
    private Pane paneSize1;

    @FXML
    private Pane paneGiaTopping;

    @FXML
    private TextField ten;

    @FXML
    private TextField giaSizeM;

    @FXML
    private TextField giaSizeL;

    @FXML
    private TextField giaTopping;

    @FXML
    private ToggleGroup loai;

    @FXML
    private Circle anhDoUongTopping;

    @FXML
    public Button imageBtn;

    static String anh = "null";
    static File anhPath;

    @FXML
    private HBox hBoxNguyenLieu;

    @FXML
    public void initialize() throws IOException, SQLException, ClassNotFoundException{
        String command1 = String.format("SELECT * FROM nguyenlieu");
        ResultSet result1 = DBUtil.dbExecuteQuery(command1);

        while (result1.next()) {
            String tenNguyenLieu = result1.getString("tennguyenlieu");

            RadioButton nguyenLieuRadioBtn = new RadioButton();
            nguyenLieuRadioBtn.setText(tenNguyenLieu);
            nguyenLieuRadioBtn.setStyle("-fx-border-color:#000000");
            nguyenLieuRadioBtn.setStyle("-fx-border-width:3");
            nguyenLieuRadioBtn.setStyle("-fx-border-radius:20");
            nguyenLieuRadioBtn.setPadding(new Insets(5, 5, 5, 5));
            nguyenLieuRadioBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);

            hBoxNguyenLieu.getChildren().add(nguyenLieuRadioBtn);
        }

        hBoxNguyenLieu.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }


    @FXML
    void doUongBtn(ActionEvent event) {
        paneGiaTopping.setVisible(false);
        paneSize1.setVisible(true);
        radioButtonSize2.setVisible(true);
    }

    @FXML
    void toppingBtn(ActionEvent event) {
        paneGiaTopping.setVisible(true);
        paneSize1.setVisible(false);
        radioButtonSize2.setVisible(false);
    }

    @FXML
    void apDungBtn(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (ten.getText().isEmpty() || giaSizeM.getText().isEmpty() || giaSizeL.getText().isEmpty() || anh.equals("null")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin đồ uống/topping", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double giaM = Double.parseDouble(giaSizeM.getText());
            double giaL = Double.parseDouble(giaSizeL.getText());
            double giaToppingValue = 0.0;
            if (loai.getSelectedToggle() instanceof RadioButton) {
                RadioButton selectedRadioButton = (RadioButton) loai.getSelectedToggle();
                if (selectedRadioButton.getText().equals("Topping")) {
                    giaToppingValue = Double.parseDouble(giaTopping.getText());
                }
            }

            Files.copy(anhPath.toPath(), Path.of(anh));
            ArrayList<String> ar = new ArrayList<String>();
            for (Node n : hBoxNguyenLieu.getChildren()) {
                if (n instanceof RadioButton) {
                    RadioButton radioBtn = (RadioButton) n;
                    if (radioBtn.isSelected()) {
                        ar.add(radioBtn.getText());
                    }
                }
            }

            String[] nl = new String[ar.size()];
            nl = ar.toArray(nl);

            RadioButton loaiB = (RadioButton) loai.getSelectedToggle();
            if (loaiB != null && loaiB.getText().equals("Đồ uống")) {
                add_drink("Tam", ten.getText(), anh,
                        new HashMap<>() {{
                            put('M', (int) giaM);
                            put('L', (int) giaL);
                        }}, nl);
            } else {
                add_topping("Tam", ten.getText(), anh, (int) giaToppingValue, nl);
            }

            JOptionPane.showMessageDialog(null, "Đồ uống/topping đã được thêm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            ten.setText("");
            giaSizeL.setText("");
            giaSizeM.setText("");
            giaTopping.setText("");

            for (Node n : hBoxNguyenLieu.getChildren()) {
                if (n instanceof RadioButton) {
                    RadioButton radioBtn = (RadioButton) n;
                    if (radioBtn.isSelected()) {
                        radioBtn.setSelected(false);
                    }
                }
            }

            backBtn.fire();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá tiền phải là một số", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

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
        InputStream imagestream = new FileInputStream(file.getAbsolutePath());
        anhPath = file;

        // Set image sau khi upload ảnh
        Image img = new Image(imagestream, 200, 200, false, false);
        ImageView view = new ImageView(img);
        view.setFitHeight(200);
        view.setFitWidth(200);
        // Hide text
        anh = String.valueOf(file).substring(String.valueOf(file).lastIndexOf("/")+1);
        imageBtn.setGraphic(view);

        System.out.println(imageBtn.getText());

    }

    public static void main(String[] args) throws IOException {
//        Path b = new Path.of("./src/project/resources/image/TraSua/");
//        System.out.println(b.getCanonicalPath());
    }
}

