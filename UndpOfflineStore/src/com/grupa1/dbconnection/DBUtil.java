package com.grupa1.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static Connection connection;
    private static Statement statement;
    private static final String dbUrl = "jdbc:mysql://localhost:3306/";
    private static final String dbName = "undpofflinestore";
    private static final String username = "root";
    private static final String password = "";

    //metod za poovezivanje sa bazom
    public static Connection poveziDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
         DriverManager.getConnection(dbUrl + dbName, dbUrl, password);
         return connection;
    }

    //metod za iskljucivanje konekcije sa bazom
    public static void iskljuciDBkonekciju() throws SQLException {

        try {
            connection.close();
            System.out.println("Uspesno iskljucivanje konekcije");
        } catch (SQLException e) {
            System.out.println("Greska u iskljucivanju konekcije");
        }
    }

    //metod za izvrsavanje upita
    public static void izvrsiUpit(String query) throws SQLException {

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Greska");
        } finally {
            statement.close();
        }

    }
}
