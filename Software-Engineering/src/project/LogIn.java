/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import project.base.Monitor;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Mr Pham Truong
 */
public class LogIn extends Application {
    public static Monitor monitor;

    @Override
    public void start(Stage stage) throws IOException, Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/project/screen/LogIn.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Hust Milk Tea ♥");
            stage.getIcons().add(new Image("/project/resources/Logo/programIcon.png"));
            stage.show();

        }
        catch (IOException e){
            e.printStackTrace();
        }

        // Set X close confirmation
        stage.setOnCloseRequest(event -> {
            JFrame frame = new JFrame();
            int n = JOptionPane.showConfirmDialog(
                    frame,
                    "Bạn có muốn thoát chương trình không?",
                    "Thoát chương trình",
                    JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                // close application when user confirm "Yes"
                System.out.println(n);
                System.exit(0);
            } else {
                // cancel the application exit when user click "No"
                event.consume();
            }
        });
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        monitor = new Monitor();
        launch(args);
    }

}


