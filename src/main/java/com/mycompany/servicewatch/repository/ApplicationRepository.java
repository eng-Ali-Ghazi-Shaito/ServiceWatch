
    /*
    insert application
find application by service ID
find all applications
update application
    */
package com.mycompany.servicewatch.repository;

import com.mycompany.servicewatch.database.DataBaseConnection;
import com.mycompany.servicewatch.model.MoniterApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

    public List<MoniterApplication> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       service_id,
                       name,
                       host,
                       health_port,
                       monitoring_enabled
                FROM applications
                ORDER BY id
                """;

        List<MoniterApplication> applications = new ArrayList<>();

        try (
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {

                MoniterApplication application = new MoniterApplication(
                        result.getLong("id"),
                        result.getString("service_id"),
                        result.getString("name"),
                        result.getString("host"),
                        result.getInt("health_port"),
                        result.getBoolean("monitoring_enabled")
                );

                applications.add(application);
            }
        }

        return applications;
    }
    
    public long insert(MoniterApplication application)
        throws SQLException {

    String sql = """
            INSERT INTO applications
            (
                service_id,
                name,
                host,
                health_port,
                monitoring_enabled
            )
            VALUES (?, ?, ?, ?, ?)
            """;

    try (
        Connection connection =
                DataBaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
    ) {
        statement.setString(
                1,
                application.getServiceId()
        );

        statement.setString(
                2,
                application.getName()
        );

        statement.setString(
                3,
                application.getHost()
        );

        statement.setInt(
                4,
                application.getHealthPort()
        );

        statement.setBoolean(
                5,
                application.isMonitoringEnabled()
        );

        statement.executeUpdate();

        try (ResultSet keys =
                     statement.getGeneratedKeys()) {

            if (keys.next()) {
                return keys.getLong(1);
            }
        }
    }

    throw new SQLException(
            "Application ID was not generated"
    );
}
    public MoniterApplication findByServiceId(
        String serviceId
) throws SQLException {

    String sql = """
            SELECT id,
                   service_id,
                   name,
                   host,
                   health_port,
                   monitoring_enabled
            FROM applications
            WHERE service_id = ?
            """;

    try (
        Connection connection =
                DataBaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql)
    ) {
        statement.setString(1, serviceId);

        try (ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return new MoniterApplication(
                        result.getLong("id"),
                        result.getString("service_id"),
                        result.getString("name"),
                        result.getString("host"),
                        result.getInt("health_port"),
                        result.getBoolean("monitoring_enabled")
                );
            }
        }
    }

    return null;
}
}

