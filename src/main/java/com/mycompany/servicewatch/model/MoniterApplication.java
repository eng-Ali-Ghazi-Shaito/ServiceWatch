/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servicewatch.model;

/**
 *
 * @author Evech
 */
public class MoniterApplication {
    private long id;
    private String serviceId;
    private String name;
    private String host;
    private int healthPort;
    private boolean monitoringEnabled;

    public MoniterApplication(
            long id,
            String serviceId,
            String name,
            String host,
            int healthPort,
            boolean monitoringEnabled
    ) {
        this.id = id;
        this.serviceId = serviceId;
        this.name = name;
        this.host = host;
        this.healthPort = healthPort;
        this.monitoringEnabled = monitoringEnabled;
    }

    public long getId() {
        return id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getHealthPort() {
        return healthPort;
    }

    public boolean isMonitoringEnabled() {
        return monitoringEnabled;
    }

    @Override
    public String toString() {
        return serviceId
                + " | "
                + name
                + " | "
                + host
                + " | port "
                + healthPort;
    }
    
}
