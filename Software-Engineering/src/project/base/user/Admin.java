package project.base.user;

import project.base.functional.AdminInterface;
import project.base.functional.BartenderInterface;
import project.base.functional.CashierInterface;

import java.sql.SQLException;

public class Admin extends User implements AdminInterface,
        BartenderInterface,
        CashierInterface {
    public Admin(String username){
        super(username);
    }

    public void create_new_user(String username, String fullname, String password, String phone, String avatar, String position, String shift) throws SQLException, ClassNotFoundException {
        AdminInterface.super.create_new_user(this.getUsername(),username, fullname, password, phone, avatar, position, shift);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Admin s = new Admin("hungpham");
        // s.create_new_user("doubleK24", "Hoàng Văn Khánh", "123456", "019238", "khanh.png", "Pha Che","Chieu");
        // s.create_new_user("hthientam2402", "Hoàng Thiện Tâm", "123456", "0913887814", "khanh.png", "Pha Che","Chieu");
        // s.create_new_user("nhatdeptrai", "Minh Nhat", "123456", "0913887814", "nhat.png", "Quan Ly","Sang");
         s.create_new_user("lapga", "Quoc Lap", "123456", "0913887123", "lap.png", "Thu Ngan", "Sang");
        // s.create_new_user("bluezdot", "Thanh Trường", "123456", "0869886357", "truong.png", "Quan Ly", "Sang");
    }

}
