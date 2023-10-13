package project.UI;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import project.base.order.Invoice;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class InvoiceTreeView extends InvoiceView{
    public InvoiceTreeView(Invoice invoice, boolean line) throws SQLException, ClassNotFoundException {
        super(invoice);
        this.button = false;
        if (Objects.equals(invoice.trangthai, "Dang chuan bi")){
            this.setStyle("-fx-background-color: #f3f37f");
        } else if (Objects.equals(invoice.trangthai, "Da giao")){
            this.setStyle("-fx-background-color: #9bef9b");
        } else {
            this.setStyle("-fx-background-color: #f3989e");
        }
        if (line) {
            Label idLabel = new Label("Mã hóa đơn: "+invoice.id);
            idLabel.setMaxWidth(150);
            HBox.setHgrow(idLabel, Priority.ALWAYS);
            String formattedTime = new SimpleDateFormat("HH:mm").format(invoice.createdAt);
            HBox line1 = new HBox(idLabel, new Text(String.format("Tạo lúc: %s", formattedTime)));
            Label kh = new Label("KH: "+invoice.tenkhachhang);
            kh.setMaxWidth(130);
            HBox.setHgrow(kh, Priority.ALWAYS);
            HBox line2 = new HBox(kh, new Text(String.format("Tổng tiền: %d.000đ", invoice.getBill())));
            line1.setSpacing(20);
            line2.setSpacing(20);
            this.getChildren().addAll(line1, line2);
        } else {
            update();
        }
    }
}
