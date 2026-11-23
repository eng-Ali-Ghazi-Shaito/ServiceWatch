package com.mycompany.servicewatch.protocol;

public class ProtocolMessage {

    private MessageType type;
    private String serviceId;
    private String timestamp;
    private String severity;
    private String message;

    public ProtocolMessage() {
    }

    public ProtocolMessage(
            MessageType type,
            String serviceId,
            String timestamp,
            String severity,
            String message
    ) {
        this.type = type;
        this.serviceId = serviceId;
        this.timestamp = timestamp;
        this.severity = severity;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}