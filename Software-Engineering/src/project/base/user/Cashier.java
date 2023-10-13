package project.base.user;

import project.base.functional.CashierInterface;
import project.base.order.Invoice;
import project.base.order.OneCall;

import java.util.Arrays;

public class Cashier extends User implements CashierInterface {
    public Cashier(String username) {
        super(username);
    }
    public static void main(String[] args) throws Exception {
        Cashier cashier = new Cashier("hungpham");
        Invoice invoice = new Invoice();
        invoice.addCall("Trà sữa Hai Nắng", 'M', 0.5, 0.5, new String[]{"Hạt ngọc trai", "Hạt châu " +
                "xanh"});
        invoice.addCall("Trà sữa Hạt Sen", 'L', 0.25, 0.8, new String[]{"Sen Vàng", "Caramen", "Hạt ngọc trai"});
        OneCall call0 = invoice.getCall(0);
        OneCall call1 = invoice.getCall(1);
        call0.increase_amount();
        call0.increase_amount();
        call1.increase_amount();
        call1.increase_amount();
        call1.decrease_amount();

        System.out.printf("Call %d, total: %d, %s, size %s, topping: %s\n", call0.id, call0.get_money(),
                call0.drink_name, call0.size, Arrays.toString(call0.toppings));
        System.out.printf("Call %d, total: %d, %s, size %s, topping: %s\n", call1.id, call1.get_money(),
                call1.drink_name, call1.size, Arrays.toString(call1.toppings));
        System.out.println("Total: "+ invoice.getBill());
        invoice.pay(300, "Nga Phương");
        cashier.confirm_new_invoice(cashier.getUsername(), invoice);
    }
}
