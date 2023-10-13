package project.base;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
    //Declare JDBC Driver
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    //Connection
    public static Connection conn = null;

    //Connection String
    //String connStr = "jdbc:postgresql://host/database
    //Username=HR, Password=HR, IP=localhost, IP=1521, SID=xe
    private static final String user = "ycftweud";
    private static final String pass = "hvdEIVLY901iezCnu-FIulM9cdroaOAE";
    private static final String connStr = "jdbc:postgresql://tiny.db.elephantsql.com/ycftweud";


    //Connect to DB
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        if (conn != null){
            return;
        }
        //Setting Postgres JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Postgres JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

        System.out.println("Postgres JDBC Driver Registered!");

        //Establish the Postgres Connection using Connection String
        try {
            conn = DriverManager.getConnection(connStr, user, pass);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Postgres Connection)
            if (conn == null || conn.isClosed()) {
                dbConnect();
            }
            System.out.println("Select statement: " + queryStmt + "\n");

            //Create statement
            stmt = conn.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
//            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static ResultSet dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Postgres Connection)
            if (conn == null || conn.isClosed()) {
                dbConnect();
            }
            System.out.println("Update statement: " + sqlStmt + "\n");
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
//            dbDisconnect();
        }
        return null;
    }
}
