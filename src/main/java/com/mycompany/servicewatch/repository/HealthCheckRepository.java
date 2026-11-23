package com.mycompany.servicewatch.repository;

import com.mycompany.servicewatch.database.DataBaseConnection;
import com.mycompany.servicewatch.model.HealthCheck;
import com.mycompany.servicewatch.model.HealthOutcome;
import com.mycompany.servicewatch.model.MoniterApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class HealthCheckRepository {

    public long insert(HealthCheck healthCheck)
            throws SQLException {

        String sql = """
                INSERT INTO health_checks
                (
                    application_id,
                    checked_at,
                    outcome,
                    elapsed_ms,
                    failure_reason
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
            statement.setLong(
                    1,
                    healthCheck.getApplicationId()
            );

            statement.setTimestamp(
                    2,
                    healthCheck.getCheckedAt()
            );

            statement.setString(
                    3,
                    healthCheck.getOutcome().name()
            );

           statement.setLong(
                    4,
                    healthCheck.getElapsedMs()
            );

            statement.setString(
                    5,
                    healthCheck.getFailureReason()
            );

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException(
                "Health-check ID was not generated"
        );
    }
    
    
    
    
    public HealthCheck findLatestForApplication(
        long applicationId
) throws SQLException {

    String sql = """
            SELECT id,
                   application_id,
                   checked_at,
                   outcome,
                   elapsed_ms,
                   failure_reason
            FROM health_checks
            WHERE application_id = ?
            ORDER BY checked_at DESC, id DESC
            LIMIT 1
            """;

    try (
        Connection connection =
                DataBaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql)
    ) {
        statement.setLong(1, applicationId);

        try (ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return new HealthCheck(
                        result.getLong("id"),
                        result.getLong("application_id"),
                        result.getTimestamp("checked_at"),
                        HealthOutcome.valueOf(
                                result.getString("outcome")
                        ),
                        result.getLong("elapsed_ms"),
                        result.getString("failure_reason")
                );
            }
        }
    }

    return null;
}
    
    
    public void update(MoniterApplication application)
        throws SQLException {

    String sql = """
            UPDATE applications
            SET name = ?,
                host = ?,
                health_port = ?,
                monitoring_enabled = ?
            WHERE id = ?
            """;

    try (
        Connection connection =
                DataBaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql)
    ) {
        statement.setString(
                1,
                application.getName()
        );

        statement.setString(
                2,
                application.getHost()
        );

        statement.setInt(
                3,
                application.getHealthPort()
        );

        statement.setBoolean(
                4,
                application.isMonitoringEnabled()
        );

        statement.setLong(
                5,
                application.getId()
        );

        statement.executeUpdate();
    }
}
    
}