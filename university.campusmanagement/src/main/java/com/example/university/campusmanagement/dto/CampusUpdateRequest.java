package com.example.university.campusmanagement.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar un campus existente con validaciones
 * Los campos son opcionales para permitir actualizaciones parciales
 */
public class CampusUpdateRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String address;

    @Size(min = 2, max = 50, message = "La ciudad debe tener entre 2 y 50 caracteres")
    private String city;

    @Pattern(
            regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "El teléfono debe tener un formato válido (ej: +573012345678 o 3012345678)"
    )
    private String telephone;

    private Boolean active;

    // Constructor por defecto
    public CampusUpdateRequest() {
        // Constructor por defecto
    }

    // Constructor con parámetros para testing
    public CampusUpdateRequest(String name, String address, String city, String telephone, Boolean active) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
        this.active = active;
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

    @Override
    public String toString() {
        return String.format("CampusUpdateRequest{name='%s', city='%s', active=%s}", name, city, active);
    }
}