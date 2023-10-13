package project.base.user;

import project.base.functional.BartenderInterface;

import java.sql.SQLException;

public class Bartender extends User implements BartenderInterface {
    public Bartender(String username) {
        super(username);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Bartender s = new Bartender("hthientam");
//        s.add_ingredient(s.getUsername(), "Hạt Sen", "Cty Hoa Sen", "sen.png", "Con hang");
//        s.add_ingredient(s.getUsername(), "Sữa", "Cty Bò sữa VN", "sua.png", "Con hang");
//        s.add_ingredient(s.getUsername(), "Trà", "Cty Nước Nôi VN", "tra.png", "Con hang");
//        s.add_ingredient(s.getUsername(), "Khúc Bạch", "Cty Nước Nôi VN", "khucbach.png", "Con hang");
//        s.add_ingredient(s.getUsername(), "Đường", "Cty Nước Nôi VN", "duong.png", "Con hang");
//        s.add_drink(s.getUsername(), "Trà sữa Hạt Sen","hatsen.png",new HashMap<>() {{put('M',40);put('L',48);}},
//                new String[]{"Trân Châu", "Đường", "Trà", "Sữa", "Hạt Sen"});
//        s.add_topping(s.getUsername(),"Hạt ngọc trai", "ngoctrai.png", 5, new String[] {"Trân Châu", "Khúc Bạch"});
//        s.add_topping(s.getUsername(),"Sen Bột Lọc", "senbotloc.png", 10, new String[] {"Hạt Sen", "Trân Châu"});
//        s.add_topping(s.getUsername(),"Caramen", "caramen.png", 20, new String[] {"Đường", "Sữa", "Pudding"});
        s.order_ingredient(s.getUsername(), "Đường", 500);
        s.order_ingredient(s.getUsername(), "Trân Châu", 300);
    }
}
