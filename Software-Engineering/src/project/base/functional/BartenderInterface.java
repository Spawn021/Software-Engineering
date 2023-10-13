package project.base.functional;

import project.base.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public interface BartenderInterface {
    default void add_ingredient(String username,
                                String tennguyenlieu,
                                String nhacungcap,
                                String anh,
                                String trangthai
    ) throws SQLException, ClassNotFoundException {
        String command = String.format("INSERT INTO nguyenlieu(idNguyenLieu, tenNguyenLieu, nhaCungCap, trangThai, " +
                "anh) " +
                        "VALUES ('%s','%s', '%s', '%s', '%s');", tennguyenlieu,tennguyenlieu, nhacungcap, trangthai, anh);
        DBUtil.dbExecuteUpdate(command);
        System.out.printf("User %s đã thêm nguyên liệu %s, nhà cung cấp %s, trạng thái %s\n", username,
                tennguyenlieu, nhacungcap, trangthai);
    }

    default void add_drink(String username,
                           String tendouong,
                           String anh,
                           HashMap<Character, Integer> size_giatien,
                           String[] nhungnguyenlieu
                           ) throws SQLException, ClassNotFoundException {

        String command = String.format("INSERT INTO douong(iddouong, tendouong, anh, onmenu) " +
                "VALUES ('%s', '%s', '%s', true);",tendouong, tendouong, anh);
        DBUtil.dbExecuteUpdate(command);


        StringJoiner command2 = new StringJoiner(",", "INSERT INTO giadouong(iddouong, size, giadouong) VALUES ",";");
        for (Map.Entry<Character, Integer> entry : size_giatien.entrySet()) {
            command2.add(String.format("('%s','%s', %d)", tendouong, entry.getKey(), entry.getValue()));
        }
        DBUtil.dbExecuteUpdate(command2.toString());


        StringJoiner command3 = new StringJoiner(",", "INSERT INTO thanhphandouong(iddouong, idnguyenlieu) VALUES ",
                ";");
        for (String nguyenlieu: nhungnguyenlieu) {
            ResultSet idnguyenlieu = DBUtil.dbExecuteQuery(String.format("Select idnguyenlieu from nguyenlieu where " +
                    "tennguyenlieu = '%s';", nguyenlieu));
            idnguyenlieu.next();
            command3.add(String.format("('%s', '%s')", tendouong, idnguyenlieu.getString(1)));
        }
        DBUtil.dbExecuteUpdate(command3.toString());
        System.out.printf("User %s đã thêm đồ uống %s, giá size M: %s, giá size L: %s, nguyên liệu: %s\n",
                username, tendouong, size_giatien.get('M'), size_giatien.get('L'), Arrays.toString(nhungnguyenlieu));
    }
    default void add_topping(String username, String tentopping, String anh, int giatien, String[] nhungnguyenlieu) throws SQLException, ClassNotFoundException {
        String command = String.format("INSERT INTO topping(idtopping, tentopping, anh, giatopping) " +
                "VALUES ('%s', '%s', '%s', %d);", tentopping, tentopping, anh, giatien);
        DBUtil.dbExecuteUpdate(command);

        StringJoiner command2 = new StringJoiner(",", "INSERT INTO thanhphantopping(idtopping, idnguyenlieu) VALUES ",
                ";");
        for (String nguyenlieu: nhungnguyenlieu) {
            ResultSet idnguyenlieu = DBUtil.dbExecuteQuery(String.format("Select idnguyenlieu from nguyenlieu where " +
                    "tennguyenlieu = '%s';", nguyenlieu));
            idnguyenlieu.next();
            command2.add(String.format("('%s', '%s')", tentopping, idnguyenlieu.getString(1)));
        }
        DBUtil.dbExecuteUpdate(command2.toString());
        System.out.printf("User %s đã thêm topping %s, giá %d, nguyên liệu: %s\n", username, tentopping
                , giatien, Arrays.toString(nhungnguyenlieu));
    }
    default void order_ingredient(String username, String tennguyenlieu, int soluong
                                ) throws SQLException, ClassNotFoundException {
        ResultSet idnguyenlieu = DBUtil.dbExecuteQuery(String.format("Select idnguyenlieu from nguyenlieu where " +
                "tennguyenlieu = '%s';", tennguyenlieu));
        if (idnguyenlieu.next()){
            DBUtil.dbExecuteUpdate(String.format("Insert into lichsudatnguyenlieu(tendangnhap,idnguyenlieu,soluong) " +
                    "values ('%s', " +
                    "'%s', %d);", username, idnguyenlieu.getString(1), soluong));
            System.out.printf("User %s đã đặt nguyên liệu '%s', số lượng %d thành công!\n", username, tennguyenlieu ,
                    soluong);
        } else {
            throw new SQLException("Can not find ingredient with provided id");
        }
    }
}