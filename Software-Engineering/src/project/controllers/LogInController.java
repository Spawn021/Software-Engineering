/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.base.DBUtil;
import project.base.user.Admin;
import project.base.user.Bartender;
import project.base.user.Cashier;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static project.LogIn.monitor;


/**
 * FXML Controller class
 *
 * @author Mr Pham Truong
 */
public class LogInController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    @FXML
    public void onEnter(ActionEvent e) throws SQLException, ClassNotFoundException, IOException {
        String command = String.format("SELECT tendangnhap, chucvu FROM nhanvien WHERE tendangnhap = '%s' AND matkhau = '%s';",
                username.getText(), password.getText());
        ResultSet result = DBUtil.dbExecuteQuery(command);
        if (!result.next()) {
            JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            String tendangnhap = result.getString(1);
            String chucvu = result.getString(2);
            System.out.println("Tên đăng nhập: " + tendangnhap);
            System.out.println("Chức vụ: " + chucvu);

            // move to main screen
            if (Objects.equals(chucvu, "Quan Ly")) {
                monitor.newSession(new Admin(result.getString(1)));
            } else if (Objects.equals(chucvu, "Thu Ngan")) {
                monitor.newSession(new Cashier(result.getString(1)));
            } else if (Objects.equals(chucvu, "Pha Che")) {
                monitor.newSession(new Bartender(result.getString(1)));
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/Base.fxml")));
            Stage window = (Stage) username.getScene().getWindow();
            window.setScene(new Scene(root));
        }
    }

    @FXML
    public void loginPressedBtn(ActionEvent e) throws SQLException, ClassNotFoundException, IOException {
        String command = String.format("SELECT tendangnhap, chucvu FROM nhanvien WHERE tendangnhap = '%s' AND matkhau = '%s';",
                username.getText(), password.getText());
        ResultSet result = DBUtil.dbExecuteQuery(command);
        if (!result.next()) {
            JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            String tendangnhap = result.getString(1);
            String chucvu = result.getString(2);
            System.out.println("Tên đăng nhập: " + tendangnhap);
            System.out.println("Chức vụ: " + chucvu);

            // move to main screen
            if (Objects.equals(chucvu, "Quan Ly")) {
                monitor.newSession(new Admin(result.getString(1)));
            } else if (Objects.equals(chucvu, "Thu Ngan")) {
                monitor.newSession(new Cashier(result.getString(1)));
            } else if (Objects.equals(chucvu, "Pha Che")) {
                monitor.newSession(new Bartender(result.getString(1)));
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/Base.fxml")));
            Stage window = (Stage) username.getScene().getWindow();
            window.setScene(new Scene(root));
        }
    }

    @FXML
    public void forgetPasswordButton(ActionEvent e) {
        JOptionPane.showMessageDialog(new JFrame(), "Hãy đến gặp quản lí để được cấp lại mật khẩu");
    }
}