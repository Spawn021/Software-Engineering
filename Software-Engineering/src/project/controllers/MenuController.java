package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import project.UI.DoUong;
import project.UI.Topping;
import project.base.DBUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuController {
    private BaseController baseController;

    public void setBaseController(BaseController baseController) {
        this.baseController = baseController;
    }

    @FXML
    public Button themDoUongToppingBtn;

    @FXML
    private VBox vBoxDoUong;
    @FXML
    private VBox vBoxTopping;

    private List<DoUong> doUongs;
    private List<Topping> toppings;

    @FXML
    public void initialize() throws IOException, SQLException, ClassNotFoundException{
        vBoxDoUong.getChildren().clear();
        vBoxTopping.getChildren().clear();

        doUongs = new ArrayList<>(doUongs());

        try {
            for (DoUong doUong : doUongs) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/screen/DoUong.fxml"));
                Pane box = fxmlLoader.load();
                DoUongController doUongController = fxmlLoader.getController();
                doUongController.setBaseController(baseController);
                doUongController.setData(doUong);

                vBoxDoUong.getChildren().add(box);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        toppings = new ArrayList<>(toppings());

        try {
            for (int i = 0; i < toppings.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/screen/Topping.fxml"));
                Pane box = fxmlLoader.load();
                ToppingController toppingController = fxmlLoader.getController();
                toppingController.setData(toppings.get(i));
                toppingController.setBaseController(baseController);

                vBoxTopping.getChildren().add(box);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        vBoxDoUong.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vBoxTopping.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }

    private List<DoUong> doUongs() throws SQLException, ClassNotFoundException, IOException {
        List<DoUong> ls = new ArrayList<>();

        String command1 = "SELECT * FROM douong";
        ResultSet result1 = DBUtil.dbExecuteQuery(command1);

        while (result1.next()) {
            String idDoUong = result1.getString("iddouong");
            String tenDoUong = result1.getString("tendouong");
            String anh = result1.getString("anh");
            String onMenuStr = result1.getString("onmenu");

            DoUong doUong = new DoUong();
            doUong.setId(idDoUong);
            doUong.setThongTinDoUong(tenDoUong);
            doUong.setAnhDoUong(anh);
            doUong.setThongTinDoUong(tenDoUong);
            doUong.setOnMenu(Boolean.parseBoolean(onMenuStr));

            String commandGiaMDoUong = String.format("SELECT * FROM giadouong WHERE iddouong = '%s' and size = 'M'",
                    idDoUong);
            ResultSet resultGiaMDoUong = DBUtil.dbExecuteQuery(commandGiaMDoUong);

            if (resultGiaMDoUong.next()){
                doUong.setGiaMDoUong(resultGiaMDoUong.getString(3));
            }

            String commandGiaLDoUong = String.format("SELECT * FROM giadouong WHERE iddouong = '%s' and size = 'L'",
                    idDoUong);
            ResultSet resultGiaLDoUong = DBUtil.dbExecuteQuery(commandGiaLDoUong);

            if (resultGiaLDoUong.next()){
                doUong.setGiaLDoUong(resultGiaLDoUong.getString(3));
            }

            ls.add(doUong);
        }

        return ls;
    }

    private List<Topping> toppings() throws SQLException, ClassNotFoundException, IOException {
        List<Topping> ls = new ArrayList<>();

        String command1 = "SELECT * FROM topping";
        ResultSet result1 = DBUtil.dbExecuteQuery(command1);

        while (result1.next()) {
            String idTopping = result1.getString("idtopping");
            String tenTopping = result1.getString("tentopping");
            String giaTopping = result1.getString("giatopping");
            String anh = result1.getString("anh");
            String onMenuStr = result1.getString("onmenu");

            Topping topping = new Topping();
            topping.setAnhTopping("project/resources/image/Topping/" + anh);
            topping.setThongTinTopping(String.format(tenTopping + " (%s)",idTopping));
            topping.setToppingOnMenu(Boolean.parseBoolean(onMenuStr));
            topping.setGiaTopping(giaTopping);

            ls.add(topping);
        }

        return ls;
    }

    @FXML
    void xoaBtn(ActionEvent event) {

    }
}

