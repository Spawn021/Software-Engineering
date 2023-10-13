package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public  class TabDatNguyenLieuController {



    @FXML
    private VBox tabPane;

    @FXML
    public TextArea textAreaSum;

    @FXML
    public TextArea textAreaListItem;

    @FXML
    public Label daChonLb;

    @FXML
    void datNguyenLieuBtn(ActionEvent event) throws IOException {
        // Notification
        if (KhoController.thumbList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chưa có nguyên liệu nào được thêm", "Denial", 2);
        }
        else {
            JOptionPane.showMessageDialog(null, "Nguyên liêu đã được đặt hàng", "Notification", 1);
            // Reset màn hình
            KhoController.thumbList.clear();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/Kho.fxml")));
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
        }
    }
}

