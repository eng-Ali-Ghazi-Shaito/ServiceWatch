package com.mycompany.servicewatch.model;

import java.sql.Timestamp;

public class HealthCheck {

    private long id;
    private long applicationId;
    private Timestamp checkedAt;
    private HealthOutcome outcome;
    private long elapsedMs;
    private String failureReason;

    public HealthCheck(
            long id,
            long applicationId,
            Timestamp checkedAt,
            HealthOutcome outcome,
            long elapsedMs,
            String failureReason
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.checkedAt = checkedAt;
        this.outcome = outcome;
        this.elapsedMs = elapsedMs;
        this.failureReason = failureReason;
    }

    public long getId() {
        return id;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public Timestamp getCheckedAt() {
        return checkedAt;
    }

    public HealthOutcome getOutcome() {
        return outcome;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public String getFailureReason() {
        return failureReason;
    }
}