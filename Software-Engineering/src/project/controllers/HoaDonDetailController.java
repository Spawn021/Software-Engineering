package project.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import project.UI.HoaDon;
import project.UI.InvoiceView;
import project.base.DBUtil;
import project.base.order.Invoice;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class HoaDonDetailController {

    @FXML
    private Button billCancelButton;

    @FXML
    private Button billDoneButton;

    @FXML private Label soOrderLabel;
    @FXML private Label tenKhachLabel;
    @FXML
    private AnchorPane billPane;
    @FXML
    private Label timeStampLabel;

    @FXML
    private Label idHoaDon;
    private HoaDonController hoaDonController;
    private String mahoadon;

    private long mins, secs;
    private void change(Label text) {
        secs++;
        if(secs == 60) {
            mins++;
            secs = 0;
        }
        text.setText(String.format("Thời gian chờ: %02d:%02d", mins, secs));
//        text.setText((((mins/10) == 0) ? "0" : "") + mins + ":"
//                + (((secs/10) == 0) ? "0" : "") + secs);
    }

    public void setData(HoaDon hoaDon) throws Exception {
        this.mahoadon = hoaDon.getMahoadon();
        Invoice invoice = new Invoice(hoaDon.getMahoadon());
        idHoaDon.setText("Hóa Đơn " + this.mahoadon);
        idHoaDon.setMaxWidth(300);
        billPane.getChildren().add(new InvoiceView(invoice, false, false));
        tenKhachLabel.setText("Khách hàng: " + hoaDon.getTenkhach());
        soOrderLabel.setText("Số order: " + hoaDon.getSoorder());

        Instant start = Instant.now();
        LocalDateTime end = hoaDon.getDate().toLocalDateTime();
        Duration timeElapsed = Duration.between(end.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant(), start);
        mins = timeElapsed.toMinutes();
        secs = timeElapsed.toSecondsPart();
        timeStampLabel.setText(String.format(String.format("Thời gian chờ: %02d:%02d", timeElapsed.toMinutes(),
                timeElapsed.toSecondsPart())));
//        System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1),
                event -> change(timeStampLabel)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();
    }

    public void setHoaDonController(HoaDonController hoaDonController) {
        this.hoaDonController = hoaDonController;
    }

    @FXML
    void setBillDoneButton(ActionEvent event) throws Exception {
        String query = String.format("update hoadon set trangthai = 'Da giao' where mahoadon = '%s';", this.mahoadon );
        try {
            DBUtil.dbExecuteUpdate(query);
            JOptionPane.showMessageDialog(new JFrame(), "Giao hóa đơn thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Giao hóa đơn không thành công!");
        }

    }
    @FXML
    void setBillCancelButton(ActionEvent event) throws Exception {
        String query = String.format("update hoadon set trangthai = 'Huy' where mahoadon = '%s';", this.mahoadon );
        try {
            DBUtil.dbExecuteUpdate(query);
            JOptionPane.showMessageDialog(new JFrame(), "Hủy hóa đơn thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Hủy hóa đơn không thành công!");
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
