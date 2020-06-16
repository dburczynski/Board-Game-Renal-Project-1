package service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    private Connection con;

    public ConnectionClass() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        }
        catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        this.con = null;
    }

    public Connection getCon() {
        return this.con;
    }

    public void openConnection() {
        String db = "jdbc:hsqldb:hsql://localhost/;";
        String user = "SA";
        String password = "";
        try {
            this.con = DriverManager.getConnection(db, user, password);
            System.out.println("Connection established.");

        }
        catch (SQLException e)  {
            System.out.println("Connection failed.");

        }
    }

    public void closeConnection() {
        try {
            this.con.close();
            System.out.println("Connection closed.");
        }
        catch (SQLException e) {
            System.out.println("Failed to close connection.");
        }
    }
}
