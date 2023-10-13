package project.base;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import project.controllers.DatDoUongController;
import project.controllers.DoanhThuController;
import project.controllers.HoaDonController;
import project.controllers.MenuController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBListener extends ScheduledService {
    private Connection conn;
    private org.postgresql.PGConnection pgconn;
    private DatDoUongController datDoUongController;
    private HoaDonController hoaDonController;
    private MenuController menuController;
    private DoanhThuController doanhThuController;

    public DBListener(Connection conn,
                      DatDoUongController datDoUongController,
                      HoaDonController hoaDonController,
                      MenuController menuController,
                      DoanhThuController doanhThuController
    ) throws SQLException {
        this.datDoUongController = datDoUongController;
        this.hoaDonController = hoaDonController;
        this.menuController = menuController;
        this.doanhThuController = doanhThuController;
        this.conn = conn;
        this.pgconn = conn.unwrap(org.postgresql.PGConnection.class);
        Statement stmt = conn.createStatement();
        stmt.execute("LISTEN \"DatDoUong\";");
        stmt.execute("LISTEN \"HoaDon\";");
        stmt.close();
    }
//    public DBListener(Connection conn, HoaDonController hoaDonController) throws SQLException {
//        this.hoaDonController = hoaDonController;
//        this.conn = conn;
//        this.pgconn = conn.unwrap(org.postgresql.PGConnection.class);
//        Statement stmt = conn.createStatement();
//        stmt.close();
//    }
    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() {
                Platform.runLater(() -> {
                    try {
                        org.postgresql.PGNotification notifications[] = pgconn.getNotifications();

                        // If this thread is the only one that uses the connection, a timeout can be used to
                        // receive notifications immediately:
                        // org.postgresql.PGNotification notifications[] = pgconn.getNotifications(10000);

                        if (notifications != null) {
                            StringBuilder notifyString = new StringBuilder();
                            for (org.postgresql.PGNotification notification : notifications)
                                notifyString.append(notification.getName());
                            System.out.println("Got notification: "+ notifyString);
                            if (notifyString.toString().equals("DatDoUong")) {
                                System.out.println("Refresh dat do uong va menu");
                                datDoUongController.refresh_data();
                                menuController.initialize();
                            } else if (notifyString.toString().equals("HoaDon")) {
                                System.out.println("Refresh hoadon va doanhthu");
                                hoaDonController.initialize();
                                doanhThuController.refresh_data();
                            } else {
                                System.out.println("Khong nhan");
                            }
                        }

                        // wait a while before checking again for new
                        // notifications

//                            Thread.sleep(500);
                    } catch(SQLException | InterruptedException sqle){
                        sqle.printStackTrace();
                    } catch(Exception e){
                        throw new RuntimeException(e);
                    }
                });
                return null;
            }
        };
    }
}
