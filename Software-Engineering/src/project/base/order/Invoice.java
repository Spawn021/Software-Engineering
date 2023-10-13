package project.base.order;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.base.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Invoice {
    private final ObservableList<OneCall> inFo = FXCollections.observableArrayList();
    public int soorder;
    public SimpleIntegerProperty khachdua = new SimpleIntegerProperty(0);
    public String tenkhachhang;
    private int buy_id = 0;
    public String id;
    public SimpleIntegerProperty bill = new SimpleIntegerProperty(0);
    public Timestamp createdAt = new Timestamp(0);
    public String trangthai;


    public Invoice(String mahoadon) throws Exception {
        // load invoice từ database
        String command = String.format("select * from hoadon where mahoadon = '%s';",mahoadon);
        ResultSet hoadonResult = DBUtil.dbExecuteQuery(command);
        hoadonResult.next();
        this.id = hoadonResult.getString("mahoadon");
        this.khachdua.setValue(hoadonResult.getInt("khachdua"));
        this.soorder = hoadonResult.getInt("soorder");
        this.createdAt = Timestamp.valueOf(hoadonResult.getTimestamp("thoigian").toLocalDateTime());
        this.trangthai = hoadonResult.getString("trangthai");
        this.tenkhachhang = hoadonResult.getString("tenkhachhang");

        //load all calls info
        String query = String.format("select * from thanhphanhoadon inner join DoUong DU on ThanhPhanHoaDon.idDoUong " +
                "= DU.idDoUong where mahoadon='%s';", mahoadon);
        ResultSet resultSet = DBUtil.dbExecuteQuery(query);
        while (resultSet.next()) {
            int buyId = resultSet.getInt("buyid");
            String invoiceCode = resultSet.getString("mahoadon");
            String drinkName = resultSet.getString("tendouong");
            char size = resultSet.getString("size").charAt(0);
            double da = resultSet.getDouble("da");
            double duong = resultSet.getDouble("duong");
            int soLuong = resultSet.getInt("soluong");

            String queryTopping = String.format("select tentopping from toppingtronghoadon inner join topping t on " +
                    "ToppingTrongHoaDon.idTopping = t.idtopping where mahoadon = '%s' " +
                    "and buyid = %d;", invoiceCode, buyId);
            ResultSet resultSet1 = DBUtil.dbExecuteQuery(queryTopping);
            ArrayList<String> toppingList = new ArrayList<String>();
            while (resultSet1.next()) {
                String topping = resultSet1.getString("tentopping");
                toppingList.add(topping);
            }
            String[] topppingArray = Arrays.copyOf(toppingList.toArray(), toppingList.size(), String[].class);
            this.inFo.add(new OneCall(buyId, drinkName, size, duong, da, topppingArray, soLuong));
        }
        this.updateBill();
        this.buy_id = this.getInFo().size();
    }
    public Invoice() {
        //tam thoi random so order
        soorder = ThreadLocalRandom.current().nextInt(0, 50 + 1);
        id = UUID.randomUUID().toString(); //Generates random UUID.
    };
    public ObservableList<OneCall> getInFo() {
        return inFo;
    }

    public void addCall(String drink_name, char size, double sugar, double ice, String[] toppings) throws Exception {
        this.buy_id += 1;
        OneCall oneCall = new OneCall(buy_id, drink_name, size, sugar, ice, toppings);
        oneCall.getAmountProperty().addListener((observableValue, number, t1) -> {
            try {
                updateInvoice();
                updateBill();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        this.inFo.add(oneCall);
        updateBill();
    }

    public OneCall getCall(int index) {
        return this.inFo.get(index);
    }

    public void updateBill() throws SQLException, ClassNotFoundException {
        int total = 0;
        for (OneCall call : inFo) {
            total += call.get_money();
        }
        bill.setValue(total);
    }

    public int getBill() {
        return bill.getValue();
    }
    public int getPaid() {
        return khachdua.getValue();
    }
    public void updateInvoice(){
        this.getInFo().removeIf(o -> o.get_ammount() == 0);
    }
    public SimpleIntegerProperty getBillProperty(){return bill;}
    public SimpleIntegerProperty getPaidProperty(){return khachdua;}

    public void pay(int amount, String tenkhachhang) throws Exception {
        int total = this.getBill();
        if (amount < total) {
            System.out.println("Chưa đủ số tiền cần trả");
            throw new Exception("Chưa đủ số tiền cần trả");
        } else {
            this.khachdua.setValue(amount);
            this.tenkhachhang = tenkhachhang;
            System.out.printf("Khách hàng %s thanh toán thành công. Tổng: %d, đã trả: %d, còn dư: %d\n", tenkhachhang
                    , total, amount, amount - total);
        }
    }

    public boolean check_payment() throws SQLException, ClassNotFoundException {
        return (khachdua.getValue() >= this.getBill());
    }

    public boolean check_availability() throws SQLException, ClassNotFoundException {
        boolean avail = true;
        for (OneCall call : inFo) {
            avail = avail && call.check_availability();
        }
        return avail;
    }

    public static void main(String[] args) throws Exception {
        Invoice invoice = new Invoice("5e7c1583-bb5b-4e6a-ab41-97fde6bb6edd");
        String formattedTime = new SimpleDateFormat("HH:mm").format(invoice.createdAt);
        System.out.println(formattedTime);
    }
}