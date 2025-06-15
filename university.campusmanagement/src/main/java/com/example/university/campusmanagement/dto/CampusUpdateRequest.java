package com.example.university.campusmanagement.dto;

/**
 * DTO para actualizar un campus existente
 */
public class CampusUpdateRequest {
    private String name;
    private String address;
    private String city;
    private String telephone;
    private Boolean active;

    // Constructor por defecto
    public CampusUpdateRequest() {
        // Constructor por defecto
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getTelephone() { return telephone; }
    public Boolean getActive() { return active; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setActive(Boolean active) { this.active = active; }
}