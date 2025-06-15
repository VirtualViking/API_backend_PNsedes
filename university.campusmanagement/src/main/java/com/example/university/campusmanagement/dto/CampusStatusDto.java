package com.example.university.campusmanagement.dto;

import com.example.university.campusmanagement.model.Campus;

/**
 * DTO para el estado del campus
 */
public class CampusStatusDto {
    private String id;
    private String name;
    private String currentState;
    private String statusDescription;
    private boolean canAcceptStudents;
    private boolean canScheduleEvents;
    private boolean isActive;

    // Constructor privado para factory method
    private CampusStatusDto() {
        // Constructor privado
    }

    public static CampusStatusDto fromCampus(Campus campus) {
        CampusStatusDto dto = new CampusStatusDto();
        dto.id = campus.getId();
        dto.name = campus.getName();
        dto.currentState = campus.getCurrentState();
        dto.statusDescription = campus.getStatusDescription();
        dto.canAcceptStudents = campus.canAcceptStudents();
        dto.canScheduleEvents = campus.canScheduleEvents();
        dto.isActive = campus.isActive();
        return dto;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCurrentState() { return currentState; }
    public String getStatusDescription() { return statusDescription; }
    public boolean isCanAcceptStudents() { return canAcceptStudents; }
    public boolean isCanScheduleEvents() { return canScheduleEvents; }
    public boolean isActive() { return isActive; }

    // Setters (necesarios para serialización JSON)
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    public void setCanAcceptStudents(boolean canAcceptStudents) { this.canAcceptStudents = canAcceptStudents; }
    public void setCanScheduleEvents(boolean canScheduleEvents) { this.canScheduleEvents = canScheduleEvents; }
    public void setActive(boolean active) { isActive = active; }
}
