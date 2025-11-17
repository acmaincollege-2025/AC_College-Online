/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hrkas
 */
public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/college_db"; // replace with your DB
    private static final String USER = "root"; // replace with your DB username
    private static final String PASSWORD = ""; // replace with your DB password
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver"; // use 'com.mysql.jdbc.Driver' for older MySQL com.mysql.cj.jdbc.Driver for new

    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            ex.getMessage();
            throw new RuntimeException("MySQL JDBC driver not found.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
