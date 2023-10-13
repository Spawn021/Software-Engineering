package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.base.DBListener;
import project.base.DBUtil;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static project.LogIn.monitor;

public class BaseController {
    @FXML
    private Button datdoUongBtn;
    @FXML
    private Button nguyenlieuBtn;
    @FXML
    private Button nhansuBtn;
    @FXML
    private Button menuBtn;
    @FXML
    private Button doanhthuBtn;
    @FXML
    private Label welcomeLabel;
    @FXML
    private AnchorPane initPane;
    private AnchorPane activeScreen;
    private AnchorPane prevScreen;


    public void toggleScreen(AnchorPane screen){
        activeScreen.setVisible(false);
        prevScreen = activeScreen;
        activeScreen = screen;
        activeScreen.setVisible(true);
    }
    @FXML
    private AnchorPane datdouongScreen;
    @FXML
    private AnchorPane khoNguyenLieuScreen;
    @FXML
    private AnchorPane nhansuScreen;
    @FXML
    private AnchorPane menuScreen;
    @FXML
    private AnchorPane doanhthuScreen;
    @FXML
    private AnchorPane infoScreen;
    @FXML
    private AnchorPane hoadonScreen;
    @FXML
    private AnchorPane themNvScreen;
    @FXML
    public AnchorPane themNguyenlieuScreen;
    @FXML
    public AnchorPane themDrinkToppingScreen;
    @FXML public AnchorPane chinhsuaDoUongScreen;
    @FXML public AnchorPane chinhsuaNVScreen;
    @FXML public AnchorPane chinhsuaToppingScreen;
    @FXML private NhanSuController mainNhanSuController;
    @FXML private HoaDonController mainHoadonController;
    @FXML private ThemNhanVienController mainAddStaffController;
    @FXML private TaiKhoanCuaBanController mainAccountController;
    @FXML private KhoController mainKhoNguyenLieuController;
    @FXML private ThemNguyenLieuController mainAddIngreController;
    @FXML private ThemDoUongToppingController mainAddDrTpController;
    @FXML private DatDoUongController mainDatDoUongController;
    @FXML private MenuController mainMenuController;
    @FXML public ChinhSuaNhanVienController mainEditEmplController;
    @FXML public ChinhSuaDoUongController mainModifyDrController;
    @FXML public ChinhSuaToppingController mainEditToppingController;
    @FXML private DoanhThuController mainDoanhthuController;



    @FXML
    private void initialize() throws SQLException, ClassNotFoundException, IOException {
        mainMenuController.setBaseController(this);
        mainNhanSuController.setBaseController(this);
        mainKhoNguyenLieuController.setBaseController(this);
        mainEditEmplController.setNhanSuController(mainNhanSuController);
        mainAddStaffController.setNhanSuController(mainNhanSuController);

        activeScreen = initPane;
        ResultSet result =
                DBUtil.dbExecuteQuery(String.format("Select tennhanvien from nhanvien where tendangnhap = '%s';",
                        monitor.getActiveUser().getUsername()));
        result.next();
        String hovaten = result.getString(1);
        welcomeLabel.setText(String.format("Chào mừng %s", hovaten));
        if (monitor.getCashier() != null) {
            datdoUongBtn.setOnAction(actionEvent -> toggleScreen(datdouongScreen));
            nguyenlieuBtn.setVisible(false);
            nhansuBtn.setVisible(false);
            menuBtn.setVisible(false);
            doanhthuBtn.setVisible(false);
        } else if (monitor.getBartender() != null) {
            datdoUongBtn.setText("Hóa đơn");
            datdoUongBtn.setOnAction(actionEvent -> toggleScreen(hoadonScreen));
            nhansuBtn.setVisible(false);
            menuBtn.setVisible(true);
            doanhthuBtn.setVisible(false);
        } else if (monitor.getAdmin() != null) {
            datdoUongBtn.setText("Hóa đơn");
            datdoUongBtn.setOnAction(actionEvent -> toggleScreen(hoadonScreen));
        }
        if (nguyenlieuBtn.isVisible())
            mainKhoNguyenLieuController.initialize();
        if (menuBtn.isVisible())
            mainMenuController.initialize();
        if (nhansuBtn.isVisible())
            mainNhanSuController.initialize();
        mainNhanSuController.themNvBtn.setOnAction(actionEvent -> toggleScreen(themNvScreen));
        mainAddStaffController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainAccountController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainAddIngreController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainAddDrTpController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainEditEmplController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainEditToppingController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainModifyDrController.backBtn.setOnAction(actionEvent -> toggleScreen(prevScreen));
        mainMenuController.themDoUongToppingBtn.setOnAction(actionEvent -> toggleScreen(themDrinkToppingScreen));
        mainKhoNguyenLieuController.taoNguyenLieuBtn.setOnAction(actionEvent -> toggleScreen(themNguyenlieuScreen));

        DBListener dbListener1 = new DBListener(DBUtil.conn, mainDatDoUongController, mainHoadonController,
                mainMenuController, mainDoanhthuController);
        dbListener1.setPeriod(Duration.millis(500));
        dbListener1.start();
//        DBListener dbListener2 = new DBListener(DBUtil.conn, mainHoadonController);
//        dbListener2.setPeriod(Duration.millis(500));
//        dbListener2.start();
    }


    @FXML
    void infBtn(ActionEvent event) {
        toggleScreen(infoScreen);
    }

    @FXML
    void nguyenlieuOnPressed(ActionEvent event) {
        toggleScreen(khoNguyenLieuScreen);
    }

    @FXML
    void nhansuOnPressed(ActionEvent event) {
        toggleScreen(nhansuScreen);
    }

    @FXML
    void menuOnPressed(ActionEvent event) {
        toggleScreen(menuScreen);
    }

    @FXML
    void doanhthuOnPressed(ActionEvent event) {
        toggleScreen(doanhthuScreen);
    }
    @FXML
    void pauseMedia(ActionEvent event) {
        String f = "src/project/resources/music/home.mp3";
        Media media = new Media(Paths.get(f).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if(mediaPlayer != null) {
            System.out.println("Pause !");
            mediaPlayer.setMute(true);
        }
    }

    // Music ON !!!
    @FXML
    void playMedia(ActionEvent event) {
        String f = "Software-Engineering/src/project/resources/music/home.mp3";
        Media media = new Media(Paths.get(f).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println("Play Music !");
        mediaPlayer.setAutoPlay(true);
    }
    @FXML
    void logoutPressed(ActionEvent event) throws IOException {
        int n = JOptionPane.showConfirmDialog(
                null,
                "Bạn có muốn đăng xuất không?",
                "Đăng xuất",
                JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            monitor.logout();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/LogIn.fxml")));
            Stage window = (Stage) datdoUongBtn.getScene().getWindow();
            window.setScene(new Scene(root));
        } else {
            event.consume();
        }
    }
}

