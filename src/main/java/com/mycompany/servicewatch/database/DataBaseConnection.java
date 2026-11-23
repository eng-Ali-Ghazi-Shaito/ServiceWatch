/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servicewatch.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Evech
 */
public class DataBaseConnection {
       //get connection method which created the connection with mysql server running locally by a cmd command
    
        public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/servicewatch";
        String username = "root";
        String password = System.getenv("SERVICEWATCH_DB_PASSWORD");
        return DriverManager.getConnection(url, username, password);
    }
        
        
        
}
