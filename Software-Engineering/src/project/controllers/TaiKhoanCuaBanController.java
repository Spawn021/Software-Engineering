/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import project.base.DBUtil;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

import static project.LogIn.monitor;

/**
 * FXML Controller class
 *
 * @author Mr Pham Truong
 */
public class TaiKhoanCuaBanController{
    @FXML
    public Button backBtn;
    @FXML
    private PasswordField editPassField;
    @FXML
    private Button editPassBtn;
    @FXML
    private Label hovatenLabel;
    @FXML
    private Text usernameText;
    @FXML
    private Text infoText;
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        ResultSet result =
                DBUtil.dbExecuteQuery(String.format("Select * from nhanvien where tendangnhap = '%s';",
                        monitor.getActiveUser().getUsername()));
        ResultSet result2 =
                DBUtil.dbExecuteQuery(String.format("select 6*count(*) from lichsulamviec where tendangnhap = '%s' " +
                                "and ngaylam >= date_trunc('month', CURRENT_DATE);",
                        monitor.getActiveUser().getUsername()));
        result.next();
        result2.next();
        hovatenLabel.setText(result.getString("tennhanvien"));
        usernameText.setText("Mã nhân viên: "+result.getString("tendangnhap"));
        HashMap<String, String> vietsubChucVu =
                new HashMap<>() {{put("Quan Ly", "Quản Lý");put("Pha Che", "Pha Chế");put("Thu Ngan",
                "Thu Ngân");}};
        HashMap<String, String> vietsubCaLam = new HashMap<>() {{put("Sang", "Sáng");put("Chieu", "Chiều");}};
        infoText.setText(String.format("""
                SĐT: %s
                Chức vụ: %s
                Ca làm: %s
                Số giờ làm việc trong tháng: %dh""",
                result.getString("sdt"),
                vietsubChucVu.get(result.getString("chucvu")),
                vietsubCaLam.get(result.getString("calam")),
                result2.getInt(1)));
    }
    @FXML
    void editPassPressed(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (Objects.equals(editPassBtn.getText(), "[Sửa]")){
            editPassBtn.setText("[Xong]");
            editPassField.setVisible(true);
        } else if (Objects.equals(editPassBtn.getText(), "[Xong]")) {
            int n = JOptionPane.showConfirmDialog(null, "Bạn có muốn đổi mật khẩu?", "Chắc chưa?",
                    JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                try {
                    DBUtil.dbExecuteUpdate(String.format("update nhanvien set matkhau = '%s' where tendangnhap = '%s'",
                            editPassField.getText(), monitor.getActiveUser().getUsername()));
                    editPassBtn.setText("[Sửa]");
                    editPassField.setText("");
                    editPassField.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Không thành công");
                }
            } else {
                editPassBtn.setText("[Sửa]");
                editPassField.setText("");
                editPassField.setVisible(false);
            }
        }
    }
}