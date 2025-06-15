package com.example.university.campusmanagement.dto;



/**
 * DTO para crear un nuevo campus
 */
public class CampusCreateRequest {
    private String name;
    private String address;
    private String city;
    private String telephone;

    // Constructor por defecto
    public CampusCreateRequest() {
        // Constructor por defecto
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getTelephone() { return telephone; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
