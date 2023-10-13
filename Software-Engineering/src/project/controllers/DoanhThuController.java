/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.UI.InvoiceTreeView;
import project.base.DBUtil;
import project.base.order.Invoice;

import java.sql.ResultSet;

public class DoanhThuController {
    @FXML
    private JFXTreeView lichsuTree;
    @FXML private JFXButton rightBtn;
    @FXML private JFXButton leftBtn;
    @FXML private DatePicker datePicker;
    @FXML private Label trendDrinkLabel;
    @FXML private Label daySumLabel;
    @FXML private Label monthSumLabel;
    @FXML private ImageView monthImage;
    @FXML private ImageView dayImage;
    @FXML private Label dayComparisionLabel;
    @FXML private Label monthComparisionLabel;


    static int i = 0;

    void set_text(int i, String[] MONTH_ABBR){
        if (i < MONTH_ABBR.length - 1) {
            leftBtn.setText(MONTH_ABBR[i]);
            rightBtn.setText(MONTH_ABBR[i + 1]);
        } else if (i == MONTH_ABBR.length - 1) {
            leftBtn.setText(MONTH_ABBR[i]);
            rightBtn.setText(MONTH_ABBR[0]);
        }
    }
    private void reset_hoadon() throws Exception {
        TreeItem<InvoiceTreeView> rootNode = new TreeItem<>(new InvoiceTreeView(new Invoice(),true));
        rootNode.getChildren().clear();
        if (datePicker.getValue() != null){
            String command = String.format("select mahoadon from hoadon where thoigian::date = '%s' order by thoigian;",
                    datePicker.getValue());
            ResultSet result = DBUtil.dbExecuteQuery(command);
            while (result.next()){
                Invoice in = new Invoice(result.getString(1));
                TreeItem<InvoiceTreeView> invoiceline = new TreeItem<>(new InvoiceTreeView(in, true));
                invoiceline.getChildren().add(new TreeItem<>(new InvoiceTreeView(in, false)));
                rootNode.getChildren().add(invoiceline);
            }
        }
        lichsuTree.setRoot(rootNode);
    }
    @FXML
    private void initialize() throws Exception {
        String[] MONTH_ABBR = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        rightBtn.setOnAction(actionEvent -> {
            if (i < MONTH_ABBR.length - 1) { i += 1; }
            else if (i == MONTH_ABBR.length - 1) { i = 0; }
            set_text(i, MONTH_ABBR);
        });
        leftBtn.setOnAction(actionEvent -> {
            if (i > 0) { i -= 1; }
            else if (i == 0) { i = MONTH_ABBR.length-1; }
            set_text(i, MONTH_ABBR);
        });
        datePicker.valueProperty().addListener(((observableValue, localDate, t1) -> {
            try {
                reset_hoadon();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
        refresh_data();
    }
    public void refresh_data() throws Exception {
        reset_hoadon();
        //Get label info
        ResultSet monthresult = DBUtil.dbExecuteQuery("""
                select extract(month from thi.thoigian) as thang, sum(thanhtien) as tong from (
                                                                                  select hoadon.thoigian, (sum(giaTopping) + giaDoUong)*soluong as thanhtien from hoadon
                                                                                                                                                                      inner join thanhphanhoadon t on hoadon.maHoaDon = t.mahoadon
                                                                                                                                                                      inner join giadouong GDU on GDU.idDoUong = t.idDoUong and GDU.size = t.size
                                                                                                                                                                      inner join toppingtronghoadon t2 on t.buyID = t2.buyid and t.maHoaDon = t2.maHoaDon
                                                                                                                                                                      inner join topping t3 on t2.idTopping = t3.idTopping
                                                                                  group by hoadon.maHoaDon, t.buyID, giaDoUong, soluong
                                                                              ) as thi group by extract(month from thi.thoigian) having extract(month from thi.thoigian) >= extract(month from current_date) -1
                order by thang desc;
                """);
        if (monthresult.next()) {
            int this_profit = monthresult.getInt("tong");
            monthSumLabel.setText(String.format("%,8d,000đ", this_profit));
            if (monthresult.next() && monthresult.getInt("tong") != 0) {
                double ratio = (double) this_profit/monthresult.getInt("tong");
                if (ratio >= 1) {
                    monthImage.setImage(new Image(getClass().getResourceAsStream("../resources/image/icons/increase" +
                            ".png")));
                    monthComparisionLabel.setText(String.format("Tăng %.1f%% so với tháng %s", (ratio-1)*100,
                            monthresult.getString("thang").split("\\.")[0]));
                } else {
                    monthImage.setImage(new Image(getClass().getResourceAsStream("../resources/image/icons/decrease" +
                            ".png")));
                    monthComparisionLabel.setText(String.format("Giảm %.1f%% so với tháng %s", (1-ratio)*100,
                            monthresult.getString("thang").split("\\.")[0]));
                }
            } else {
                monthComparisionLabel.setText("No data");
            }
        } else {
            monthSumLabel.setText("No data");
        }

        ResultSet dayResult = DBUtil.dbExecuteQuery("""
                select date(thi.thoigian) as ngay, sum(thanhtien) as tong from (
                    select hoadon.thoigian, (sum(giaTopping) + giaDoUong)*soluong as thanhtien from hoadon
                    inner join thanhphanhoadon t on hoadon.maHoaDon = t.mahoadon
                    inner join giadouong GDU on GDU.idDoUong = t.idDoUong and GDU.size = t.size
                    inner join toppingtronghoadon t2 on t.buyID = t2.buyid and t.maHoaDon = t2.maHoaDon
                    inner join topping t3 on t2.idTopping = t3.idtopping
                    where trangThai = 'Da giao'
                    group by hoadon.maHoaDon, t.buyID, giaDoUong, soluong
                ) as thi group by date(thi.thoigian) having date(thi.thoigian) >= current_date - 1
                order by ngay desc;
                """);
        if (dayResult.next()) {
            int day_revenue = dayResult.getInt("tong");
            daySumLabel.setText(String.format("%,8d,000đ", day_revenue));
            if (dayResult.next() && dayResult.getInt("tong") != 0) {
                double day_ratio = (double) day_revenue/dayResult.getInt("tong");
                if (day_ratio >= 1) {
                    dayImage.setImage(new Image(getClass().getResourceAsStream("../resources/image/icons/increase" +
                            ".png")));
                    dayComparisionLabel.setText(String.format("Tăng %.1f%% so với hôm qua", (day_ratio-1)*100));
                } else {
                    dayImage.setImage(new Image(getClass().getResourceAsStream("../resources/image/icons/decrease" +
                            ".png")));
                    dayComparisionLabel.setText(String.format("Giảm %.1f%% so với hôm qua", (1-day_ratio)*100));
                }
            } else {
                dayComparisionLabel.setText("No data");
            }
        } else {
            daySumLabel.setText("No data");
        }

        ResultSet trendingDrinks = DBUtil.dbExecuteQuery("""
                select DU.tenDoUong, sum(soluong) as tong from thanhphanhoadon
                                                       inner join HoaDon HD on HD.maHoaDon = ThanhPhanHoaDon.maHoaDon
                                                       inner join DoUong DU on ThanhPhanHoaDon.idDoUong = DU.idDoUong
                where thoigian <= ('now'::timestamp) at time zone 'utc' at time zone 'wast' and thoigian > ('now'::timestamp - '1 week'::interval) at time zone 'utc' at time zone 'wast'
                  and trangThai = 'Da giao'
                group by DU.idDoUong
                order by sum(soluong) desc;
                """);
        StringBuilder trendingString = new StringBuilder("");
        for (int i=1;i<=3;i++){
            if (trendingDrinks.isLast()){
                break;
            }
            if (trendingDrinks.next()){
                trendingString.append(i).append(". ").append(trendingDrinks.getString("tendouong")).append("\n");
            } else {
                trendingString.append(i).append(". ").append("No data").append("\n");
            }
        }
        trendDrinkLabel.setText(trendingString.toString());
    }
}
