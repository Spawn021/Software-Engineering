package project.base;

import project.base.user.Admin;
import project.base.user.Bartender;
import project.base.user.Cashier;
import project.base.user.User;

import java.io.IOException;
import java.sql.SQLException;

public class Monitor {
    private Cashier cashier;
    private Bartender bartender;
    private Admin admin;

    public Monitor() throws SQLException, ClassNotFoundException {
        DBUtil.dbConnect();
    }

    public void newSession(User user){
        System.out.println(user instanceof Cashier);

    }
    public void newSession(Cashier cashier){
        System.out.println("Lần đăng nhập mới");
        this.cashier = cashier;
        this.bartender = null;
        this.admin = null;
    }

    public void newSession(Bartender bartender){
        this.cashier = null;
        this.bartender = bartender;
        this.admin = null;
    }
    public void newSession(Admin admin){
        this.cashier = null;
        this.bartender = null;
        this.admin = admin;
    }
    public void logout(){
        System.out.println("User đã đăng xuất");
        this.cashier = null;
        this.bartender = null;
        this.admin = null;
    }

    public Cashier getCashier() {
        return this.cashier;
    }
    public Bartender getBartender() {
        return this.bartender;
    }
    public Admin getAdmin() {
        return this.admin;
    }

    public User getActiveUser() {
        if (this.cashier != null)  return this.cashier;
        if (this.bartender != null) return this.bartender;
        if (this.admin != null) return this.admin;
        return null;
    }

    public static void main(String[] args) throws IOException {

    }

}
