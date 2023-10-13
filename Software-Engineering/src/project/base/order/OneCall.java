package project.base.order;

import javafx.beans.property.SimpleIntegerProperty;
import project.base.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

public class OneCall {
    public int id;
    public String drink_name;
    public char size;
    public double sugar;
    public double ice;
    public String[] toppings;
    public SimpleIntegerProperty amount;

    public OneCall(int id, String drink_name, char size, double sugar, double ice, String[] toppings) throws Exception {
        if (0<=sugar && sugar<=1 && 0<=ice && ice<=1){
            this.id = id;
            this.drink_name = drink_name;
            this.size = size;
            this.sugar = sugar;
            this.ice = ice;
            this.toppings = toppings;
            this.amount = new SimpleIntegerProperty(1);
        } else {
            throw new Exception("Invalid sugar or ice rate");
        }
    }
    public OneCall(int id, String drink_name, char size, double sugar, double ice, String[] toppings, int ammount) throws Exception {
        this(id, drink_name, size, sugar, ice, toppings);
        this.amount.setValue(ammount);
    }

    public void increase_amount(){
        this.amount.setValue(this.amount.getValue() + 1);
    }
    public void decrease_amount(){
        if (this.amount.getValue() > 0) {
            this.amount.setValue(this.amount.getValue() - 1);
        } else {
            System.out.println("Not allow negative ammount");
        }
    }
    public int get_ammount(){return this.amount.getValue();}
    public SimpleIntegerProperty getAmountProperty(){return amount;}

    public int get_money() throws SQLException, ClassNotFoundException {
        int total;
        StringJoiner command;
        if (this.toppings.length != 0){
            command = new StringJoiner("', '",
                    "select sum(giatopping) + giadouong from giadouong g, topping, douong d\n" +
                            "               where g.idDoUong = d.iddouong and\n" +
                            "                     tenTopping in ('",
                    String.format("') and d.tendouong = '%s' and size = '%s' group by g.idDoUong, size;", this.drink_name,
                            this.size)
            );
            for (String topping: this.toppings) {
                command.add(topping);
            }
        } else {
            command = new StringJoiner("", String.format("select giadouong from giadouong inner join douong d on " +
                    "giadouong.iddouong = d.iddouong where tendouong = '%s' and size = '%s';", this.drink_name,
                    this.size), "");
        }
           ResultSet result = DBUtil.dbExecuteQuery(command.toString());
        if (result.next()) {
            total = result.getInt(1);
        } else {
            throw new SQLException("Invalid drink name or topping name");
        }
        //tra lại tổng số tiền trong 1 call, bao gồm tiền của đồ uống, phụ thuộc vào size + tiền của topping
        //viết lệnh sql query
        return total*this.amount.getValue();
    }
    public boolean check_availability() throws SQLException, ClassNotFoundException {
        StringJoiner command = new StringJoiner("', '", """
        select n.tennguyenlieu, t.tenTopping from thanhphantopping inner join nguyenlieu n on ThanhPhanTopping.idNguyenLieu = n.idnguyenlieu
        inner join topping t on thanhphantopping.idtopping = t.idtopping
        where t.tenTopping in ('""", String.format("""
        ') and n.trangThai = 'Het hang'
        union
        select n2.tennguyenlieu, tenDoUong from thanhphandouong inner join nguyenlieu n2 on ThanhPhanDoUong.idNguyenLieu = n2.idnguyenlieu
        inner join douong d on thanhphandouong.iddouong = d.iddouong
        where d.tendouong = '%s' and n2.trangThai = 'Het hang';
        """, this.drink_name));

        for (String topping: this.toppings) {
            command.add(topping);
        }
        ResultSet result = DBUtil.dbExecuteQuery(command.toString());
        return !result.next();
    }
}
