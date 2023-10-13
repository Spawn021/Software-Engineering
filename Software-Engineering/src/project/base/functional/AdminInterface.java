package project.base.functional;

import project.base.DBUtil;

import java.sql.SQLException;

public interface AdminInterface {
    default void create_new_user(String parent_username,
                                 String username,
                                 String fullname,
                                 String password,
                                 String phone,
                                 String avatar,
                                 String position,
                                 String shift
    ) throws SQLException, ClassNotFoundException {
        String command = String.format("INSERT INTO nhanvien(tendangnhap, tennhanvien, matkhau, sdt, anhdaidien, " +
                        "chucvu, calam, active) " +
                        "VALUES ('%s', '%s', '%s','%s','%s', '%s', '%s', true);",
                username, fullname, password, phone, avatar, position, shift);
        try {
            DBUtil.dbExecuteUpdate(command);
        } catch (SQLException e) {
            System.out.println("Error occurred while INSERT operation: " + e);
            throw e;
        }
        System.out.printf("Admin %s đã tạo user mới:\nusername\t%s\nfull name\t%s\nposition\t%s\n", parent_username,
                username, fullname, position);
    }
}