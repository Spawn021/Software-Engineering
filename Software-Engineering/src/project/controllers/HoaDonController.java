package project.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import project.UI.HoaDon;
import project.base.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDonController {

    @FXML
    private Button billCancelButton;

    @FXML
    private Button billDoneButton;


    @FXML
    private HBox hBoxHoaDon;

    @FXML
    private AnchorPane mainHoadon;

    private List<HoaDon> invoiceList;

    @FXML
    public void initialize() throws Exception {
        hBoxHoaDon.getChildren().clear();
        invoiceList = new ArrayList<>(hoaDonList());

        for (HoaDon hoaDon : invoiceList) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/project/screen/HoaDonDetail.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            HoaDonDetailController hoaDonDetailController = fxmlLoader.getController();

            hoaDonDetailController.setHoaDonController(this);
            hoaDonDetailController.setData(hoaDon);

            hBoxHoaDon.getChildren().add(anchorPane);
        }
        hBoxHoaDon.setSpacing(10);
        Insets insets = new Insets(15);
        hBoxHoaDon.setPadding(insets);
    }


    private List<HoaDon> hoaDonList() throws SQLException, ClassNotFoundException {
        List<HoaDon> hoaDonList1 = new ArrayList<>();

        String query = "select * from hoadon where trangthai='Dang chuan bi';";
        ResultSet resultSet = DBUtil.dbExecuteQuery(query);

        while (resultSet.next()) {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMahoadon(resultSet.getString("mahoadon"));
            hoaDon.setDate(resultSet.getTimestamp("thoigian"));
            hoaDon.setSoorder(resultSet.getString("soorder"));
            hoaDon.setTenkhach(resultSet.getString("tenkhachhang"));
            hoaDonList1.add(hoaDon);
        }
        return hoaDonList1;
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String query = String.format("select mahoadon, thoigian, trangthai from hoadon where trangthai='Dang chuan bi';");
        ResultSet resultSet = DBUtil.dbExecuteQuery(query);
        while (resultSet.next()) {
            String mahoadon = resultSet.getString("thoigian");
            System.out.println(mahoadon);
            }
        }
}
//    private void initialize() throws Exception, SQLException {
//        String query = String.format("select mahoadon, trangthai from hoadon where trangthai='Dang chuan bi'");
//        ResultSet resultSet = DBUtil.dbExecuteQuery(query);
//
//        resultSet.next();
//        String mahoadon = resultSet.getString("mahoadon");
//        Invoice hoadon = new Invoice(mahoadon);
//        billPane1.getChildren().add(new InvoiceView(hoadon, false, false));
//
//
//        resultSet.next();
//        String mahoadon2 = resultSet.getString("mahoadon");
//        Invoice hoadon2 = new Invoice(mahoadon2);
//        billPane2.getChildren().add(new InvoiceView(hoadon2, false, false));
//
//        resultSet.next();
//        String mahoadon3 = resultSet.getString("mahoadon");
//        Invoice hoadon3 = new Invoice(mahoadon3);
//        billPane3.getChildren().add(new InvoiceView(hoadon3, false, false));
//    }
