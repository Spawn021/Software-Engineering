package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.base.DBUtil;
import project.model.ImageMain;

import javax.swing.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KhoController {
    private BaseController baseController;
    @FXML public Button taoNguyenLieuBtn;
    public static ObservableList<String> thumbList = FXCollections.observableArrayList();
    public void setBaseController(BaseController baseController) {
        this.baseController = baseController;
    }
    @FXML
    private GridPane imageGrid;
    @FXML
    private VBox rightVBox;
    private List<ImageMain> images;
    @FXML
    private HBox bigHbox;
    @FXML
    private Label daChonLb;
    @FXML
    private Label tongTienLb;


    @FXML
    public void initialize() throws IOException {

        try {
            images = new ArrayList<>(images());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int columns = 0;
        int rows = 1;

        try {
            for (int i = 0; i < images.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/screen/thumb.fxml"));
                VBox box = fxmlLoader.load();
                ThumbController thumbController = fxmlLoader.getController();
                thumbController.setData(images.get(i));

                if (columns == 3) {
                    columns = 0;
                    ++ rows;
                }

                imageGrid.add(box, columns++, rows);
                GridPane.setMargin(box, new Insets(5));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thumbList.addListener((ListChangeListener<String>) change -> {
            StringBuilder daChon = new StringBuilder("Đã chọn:\n");
            int sum = 0;
            for (String s:thumbList) {
                try {
                daChon.append(String.format("  - %s\n ", s));

                String command = String.format("SELECT dongia FROM nguyenlieu WHERE tennguyenlieu = '%s'", s);
                ResultSet result = DBUtil.dbExecuteQuery(command);
                result.next();
                int dongia = result.getInt(1);
                sum += dongia;
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            daChonLb.setText(daChon.toString());

            StringBuilder tongTien = new StringBuilder("Tổng tiền:\n");
            tongTien.append(String.format("         %d đồng", sum*100));
            tongTienLb.setText(tongTien.toString());

            if (thumbList.isEmpty()) {
                daChonLb.setText("");
                tongTienLb.setText("");
            }
        });
    }

    private List<ImageMain> images() throws SQLException, ClassNotFoundException {
        List<ImageMain> ls = new ArrayList<>();

        ImageMain image;
        // Số lượng nguyên liệu trong db
        String countQuery = String.format(("select COUNT(*) as cnt from nguyenlieu"));
        ResultSet rs = DBUtil.dbExecuteQuery(countQuery);
        rs.next();
        int count = rs.getInt("cnt");

        // Các nguyên liệu trong db
        String query = String.format("select * from nguyenlieu");
        ResultSet resultSet = DBUtil.dbExecuteQuery(query);
        for(int i=0; i<count; i++) {
            resultSet.next();
            String idnguyenlieu = resultSet.getString("idnguyenlieu");
            String tennguyenlieu = resultSet.getString("tennguyenlieu");
            String trangthai = resultSet.getString("trangthai");
            if (trangthai.equals("Con hang")) {
                trangthai = "Còn Hàng";
            }
            if (trangthai.equals("Sap het")) {
                trangthai = "Sắp Hết";
            }
            if (trangthai.equals("Het hang")) {
                trangthai = "Hết Hàng";
            }
            String anh = resultSet.getString("anh");

            image = new ImageMain();
            image.setThumbSrc(String.format("/project/resources/image/NguyenLieu/%s", anh));
            image.setName(tennguyenlieu);
            image.setTrangThai(trangthai);
            image.setIdNguyenLieu(idnguyenlieu);

            System.out.println(image.getName());
            System.out.println(image.getTrangThai());
            System.out.println(image.getThumbSrc());
            System.out.println(image.getIdNguyenLieu());

            ls.add(image);
        }
        return ls;
    }

    @FXML
    void taoNguyenLieuOnPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/ThemNguyenLieu.fxml")));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void datNguyenLieuBtn(ActionEvent event) throws IOException {
        // Notification
        if (KhoController.thumbList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chưa có nguyên liệu nào được thêm", "Denial", 2);
        }
        else {
            JOptionPane.showMessageDialog(null, "Nguyên liệu đã được đặt hàng", "Notification", 1);
            // Reset màn hình
            KhoController.thumbList.clear();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/screen/Kho.fxml")));
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
        }
    }
}
