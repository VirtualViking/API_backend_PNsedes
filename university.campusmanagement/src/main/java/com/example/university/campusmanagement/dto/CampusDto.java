package com.example.university.campusmanagement.dto;

import com.example.university.campusmanagement.model.Campus;

/**
 * DTO básico para Campus (sin exponer entidad JPA)
 */
public class CampusDto {
    private String id;
    private String name;
    private String address;
    private String city;
    private String telephone;
    private boolean active;
    private String currentState;

    // Constructor por defecto
    public CampusDto() {
        // Constructor por defecto
    }

    // Factory method para crear DTO desde entidad
    public static CampusDto fromCampus(Campus campus) {
        CampusDto dto = new CampusDto();
        dto.id = campus.getId();
        dto.name = campus.getName();
        dto.address = campus.getAddress();
        dto.city = campus.getCity();
        dto.telephone = campus.getTelephone();
        dto.active = campus.isActive();
        dto.currentState = campus.getCurrentState();
        return dto;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getTelephone() { return telephone; }
    public boolean isActive() { return active; }
    public String getCurrentState() { return currentState; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setActive(boolean active) { this.active = active; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
}
