package project.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import project.UI.InvoiceView;
import project.UI.VBoxCell;
import project.base.DBUtil;
import project.base.order.Invoice;
import project.base.user.Cashier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static project.LogIn.monitor;


public class DatDoUongController {
    @FXML
    private ToggleGroup size;

    @FXML
    private JFXSlider daSlider;

    @FXML
    private JFXSlider duongSlider;

    @FXML
    private TextField khachTra;
    @FXML
    private TextField tenKhach;
    @FXML
    private JFXListView<VBoxCell> toppingList;
    @FXML
    private JFXListView<VBoxCell> douongList;
    @FXML
    private ScrollPane hoadonList;
    @FXML
    private Label totalBillLabel;
    @FXML
    private Text khachTraLabel;
    @FXML
    private Label soDuLabel;


    private static Invoice hoadon = new Invoice();
    private static String chosenDrink;
    private static String[] chosenTopping = {};
    public void refresh_data() throws SQLException, ClassNotFoundException {
        String command = "SELECT * FROM douong WHERE onmenu = True;";
        ResultSet result = DBUtil.dbExecuteQuery(command);
        List<VBoxCell> list = new ArrayList<>();
        while (result.next()) {
            list.add(new VBoxCell(result.getString("tendouong"),
                    result.getString("anh")));
        }
        ObservableList<VBoxCell> douong = FXCollections.observableList(list);
        douongList.setItems(douong);
        //topping
        String command2 = "SELECT * FROM topping WHERE onmenu = True;";
        ResultSet result2 = DBUtil.dbExecuteQuery(command2);
        List<VBoxCell> list2 = new ArrayList<>();
        while (result2.next()){
            list2.add(new VBoxCell(result2.getString("tentopping"),
                    result2.getString("anh"),
                    result2.getString("giatopping")));
        }
        ObservableList<VBoxCell> myObservableList = FXCollections.observableList(list2);
        toppingList.setItems(myObservableList);
        toppingList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<VBoxCell> call(ListView<VBoxCell> vBoxCellListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(VBoxCell item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                        if (item != null) {
                            setMouseTransparent(item.disable); //added this line
                            setFocusTraversable(!item.disable); //added this line
                            setDisable(item.disable);
                        }
                    }
                };
            }
        });
        douongList.setCellFactory(new Callback<ListView<VBoxCell>, ListCell<VBoxCell>>() {
            @Override
            public ListCell<VBoxCell> call(ListView<VBoxCell> vBoxCellListView) {
                return new ListCell<VBoxCell>() {
                    @Override
                    protected void updateItem(VBoxCell item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                        if (item != null) {
                            setMouseTransparent(item.disable); //added this line
                            setFocusTraversable(!item.disable); //added this line
                            setDisable(item.disable);
                        }
                    }
                };
            }
        });
    }
    void new_instance() throws SQLException, ClassNotFoundException {
        tenKhach.setText("");
        khachTra.setText("");
        douongList.getSelectionModel().clearSelection();
        toppingList.getSelectionModel().clearSelection();
        ObservableStringValue formattedBill = Bindings.createStringBinding(() ->
                "Tổng tiền: " + String.format("%d.000đ",hoadon.getBill()), hoadon.getBillProperty());
        totalBillLabel.textProperty().bind(formattedBill);

        hoadonList.setContent(new InvoiceView(hoadon));
        refresh_data();

        ObservableStringValue formattedPaid = Bindings.createStringBinding(() ->
                String.format("Khách trả: %d.000đ",hoadon.getPaid()), hoadon.getPaidProperty());
        khachTraLabel.textProperty().bind(formattedPaid);
        ObservableStringValue formattedOdd = Bindings.createStringBinding(() ->
                        String.format("Số dư: %d.000đ",hoadon.getPaid() - hoadon.getBill()), hoadon.getBillProperty(),
                hoadon.getPaidProperty());
        soDuLabel.textProperty().bind(formattedOdd);
    }

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
//        douongList.getItems().clear();
//        toppingList.getItems().clear();
        new_instance();
        douongList.getSelectionModel().selectedItemProperty().addListener((observableValue
                , old, n) -> {
            if (n != null) {
                chosenDrink = n.ten.getText();
                System.out.println(chosenDrink);
            } else { chosenDrink = null;}
        });

        toppingList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        toppingList.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            ObservableList<VBoxCell> selectedItems = toppingList.getSelectionModel().getSelectedItems();
            List<String> local = new ArrayList<>();
            for (VBoxCell v:selectedItems){
                local.add(v.ten.getText());
            }
            chosenTopping = local.toArray(new String[0]);
            System.out.println(Arrays.toString(chosenTopping));
        });
    }

    @FXML
    void AddPressedBtn(ActionEvent event) throws Exception {
        if (chosenDrink == null) {
            JOptionPane.showMessageDialog(new JFrame(), "Chưa chọn đồ uống hoặc topping");
        } else {
            double da = daSlider.getValue()/100;
            double duong = duongSlider.getValue()/100;
            RadioButton s = (RadioButton) size.getSelectedToggle();
            hoadon.addCall(chosenDrink, s.getText().charAt(s.getText().length()-1), duong, da, chosenTopping);
        }
    }

    @FXML
    void KhachTraPressedBtn(ActionEvent event) {
        if (tenKhach.getText().trim().isEmpty() || khachTra.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Empty Field", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        try {
            int i = Integer.parseInt(khachTra.getText());
            hoadon.pay(i, tenKhach.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Number", "ERROR!", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Chưa đủ số tiền cần trả", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void XacNhanPressedBtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (monitor.getCashier() != null){
            Cashier user = monitor.getCashier();
            String message = user.confirm_new_invoice(user.getUsername(), hoadon);
            JOptionPane.showMessageDialog(new JFrame(), message + ", Order number: " + hoadon.soorder);
            if (Objects.equals(message, "Success!")){
                hoadon = new Invoice();
                new_instance();
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid user, not cashier, pls log out and log in again!");
        }
    }
    @FXML
    void printButtonPressed(ActionEvent event){
        JOptionPane.showMessageDialog(new JFrame(), "Đã in ra hóa đơn", "Sucess", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws IOException {
        InputStream imagestream = DatDoUongController.class.getResourceAsStream(".." +
                "/screen/resources/image/Topping" +
                "/caramen.png");
        File input = new File("../screen/resources/image/Topping/caramen.jpg");
        BufferedImage bufferedImage = ImageIO.read(input);
        ImageFilter filter = new GrayFilter(true, 50);
        ImageProducer producer = new FilteredImageSource(bufferedImage.getSource(), filter);
        Image image = Toolkit.getDefaultToolkit().createImage(producer);
        System.out.println(imagestream);
    }
}
