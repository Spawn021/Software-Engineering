package project.base.functional;

import project.base.DBUtil;
import project.base.order.Invoice;
import project.base.order.OneCall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.StringJoiner;

public interface CashierInterface {
    default String confirm_new_invoice(String username, Invoice invoice) throws SQLException, ClassNotFoundException {
        if (invoice.check_availability()){
            if (invoice.check_payment()){
                String command = String.format("INSERT INTO hoadon(tendangnhap, soorder, khachdua, trangthai, " +
                                "mahoadon, tenkhachhang) VALUES ('%s', %d, %d, '%s', '%s', '%s');", username,
                        invoice.soorder, invoice.getPaid(), "Dang chuan bi", invoice.id, invoice.tenkhachhang);
                DBUtil.dbExecuteUpdate(command);

                StringJoiner command2 = new StringJoiner(",", "INSERT INTO thanhphanhoadon(iddouong, size, " +
                        "da, duong, soluong, mahoadon, buyid) VALUES ",";");
                for (OneCall call : invoice.getInFo()) {
                    ResultSet idDouong = DBUtil.dbExecuteQuery(String.format("select iddouong from douong where " +
                            "tendouong = '%s';",call.drink_name));
                    idDouong.next();
                    command2.add(String.format("('%s','%s', %.2f, %.2f, %d, '%s', %d)", idDouong.getString(1),
                            call.size, call.ice, call.sugar, call.get_ammount(), invoice.id, call.id));
                }
                DBUtil.dbExecuteUpdate(command2.toString());

                StringJoiner command3 = new StringJoiner(",", "INSERT INTO toppingtronghoadon(mahoadon, buyid, " +
                        "idtopping) VALUES ",";");
                String command3_cp = "INSERT INTO toppingtronghoadon(mahoadon, buyid, idtopping) VALUES ;";
                for (OneCall call : invoice.getInFo()) {
                    for (String topping: call.toppings){
                        ResultSet idTopping = DBUtil.dbExecuteQuery(String.format("select idtopping from topping " +
                                "where tentopping = '%s';",topping));
                        idTopping.next();
                        command3.add(String.format("('%s', %d, '%s')", invoice.id, call.id, idTopping.getString(1)));
                    }
                }
                if (!Objects.equals(command3.toString(), command3_cp))
                    DBUtil.dbExecuteUpdate(command3.toString());

                System.out.printf("User %s add new invoice successfully, id %s\n", username, invoice.id);
                return "Success!";
            } else {
                System.out.printf("User %s add new invoice unsuccessfully, unpaid customer\n",username);
                return "Khách chưa trả đủ tiền!";
            }
        } else {
            System.out.printf("User %s add new invoice unsuccessfully, out of ingredients\n",username);
            return "Out of ingredints!";
        }
    }
}
