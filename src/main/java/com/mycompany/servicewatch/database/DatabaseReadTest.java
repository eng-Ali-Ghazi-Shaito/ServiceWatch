
package com.mycompany.servicewatch.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseReadTest {

    public static void main(String[] args) {

        String sql = """
                SELECT id, service_id, name, host, health_port
                FROM applications
                """;

        try (
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            System.out.println("Applications stored in MySQL:");

            while (result.next()) {
                long id = result.getLong("id");
                String serviceId = result.getString("service_id");
                String name = result.getString("name");
                String host = result.getString("host");
                int healthPort = result.getInt("health_port");

                System.out.println(
                    id + " | " +
                    serviceId + " | " +
                    name + " | " +
                    host + " | " +
                    healthPort
                );
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}