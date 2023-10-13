package project.UI;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class IngreOrderView extends VBox {
    private ObservableList<String> chosenIngre;
    private List<TextField> savedTextField = new ArrayList();
    IngreOrderView(ObservableList<String> chosenIngre){
        this.chosenIngre = chosenIngre;
        this.chosenIngre.addListener((ListChangeListener<String>) change -> update());
    }
    private void update(){
        for (String ingre: chosenIngre){
            Label tenNguyenLieu = new Label(ingre);
            TextField soLuong = new TextField();
            savedTextField.add(soLuong);
            HBox hBox = new HBox(tenNguyenLieu, soLuong);
            this.getChildren().add(hBox);
        }
    }
    public boolean isEmpty(){
        boolean empty = false;
        for (TextField t: savedTextField) {
            empty = empty || t.getText().isBlank();
        }
        return empty;
    }
//    public get_total(){
//
//    }
}
