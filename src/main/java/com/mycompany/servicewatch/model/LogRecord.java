package com.mycompany.servicewatch.model;

import java.sql.Timestamp;

public class LogRecord {

    private long id;
    private long applicationId;
    private Timestamp eventTimestamp;
    private Timestamp receiptTimestamp;
    private Severity severity;
    private String message;

    public LogRecord(
            long id,
            long applicationId,
            Timestamp eventTimestamp,
            Timestamp receiptTimestamp,
            Severity severity,
            String message
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.eventTimestamp = eventTimestamp;
        this.receiptTimestamp = receiptTimestamp;
        this.severity = severity;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public Timestamp getEventTimestamp() {
        return eventTimestamp;
    }

    public Timestamp getReceiptTimestamp() {
        return receiptTimestamp;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }
}