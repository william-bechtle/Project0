package com.thevarungupta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionFactory {

    private static Connection connection = null;

    private ConnectionFactory(){}

    public static Connection getConnection() {
        if(connection == null){
            ResourceBundle bundle = ResourceBundle.getBundle("com/thevarungupta/dbConfig");
            String url = bundle.getString("url");
            String username= bundle.getString("username");
            String password = bundle.getString("password");
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return connection;
    }

}
