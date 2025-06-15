package com.example.university.campusmanagement.model;

import com.example.university.campusmanagement.state.ActiveState;
import com.example.university.campusmanagement.state.CampusState;
import com.example.university.campusmanagement.state.InactiveState;
import com.example.university.campusmanagement.state.MaintenanceState;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Entidad Campus que representa una sede universitaria.
 * Implementa el patrón State para manejar diferentes estados operativos.
 */
@Data
@Document(collection = "campuses")
public class Campus {

    // Constantes para los estados
    private static final String ACTIVE_STATE = "ACTIVE";
    private static final String INACTIVE_STATE = "INACTIVE";
    private static final String MAINTENANCE_STATE = "MAINTENANCE";

    @Id
    private String id;
    private String name;
    private String address;
    private String city;
    private String telephone;
    private boolean active;

    // Patrón State - campo para persistir el estado en BD
    private String currentState;

    // Estado actual (no se persiste en MongoDB) - Usar @Transient de Spring Data
    @Transient
    private CampusState state;

    /**
     * Constructor por defecto
     */
    public Campus() {
        initializeDefaultState();
    }

    /**
     * Constructor con parámetros básicos
     */
    public Campus(String name, String address, String city, String telephone) {
        this();
        this.name = name;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    /**
     * Inicializa el estado por defecto del campus
     */
    private void initializeDefaultState() {
        this.state = new ActiveState();
        this.currentState = ACTIVE_STATE;
        this.active = true;
    }

    // ============ Métodos del Patrón State ============

    /**
     * Activa el campus
     */
    public void activate() {
        ensureStateInitialized();
        state.activate(this);
        updatePersistedFields();
    }

    /**
     * Desactiva el campus
     */
    public void deactivate() {
        ensureStateInitialized();
        state.deactivate(this);
        updatePersistedFields();
    }

    /**
     * Pone el campus en mantenimiento
     */
    public void putInMaintenance() {
        ensureStateInitialized();
        state.maintenance(this);
        updatePersistedFields();
    }

    /**
     * Obtiene la descripción del estado actual
     */
    public String getStatusDescription() {
        ensureStateInitialized();
        return state.getStatusDescription();
    }

    /**
     * Verifica si puede aceptar estudiantes
     */
    public boolean canAcceptStudents() {
        ensureStateInitialized();
        return state.canAcceptStudents();
    }

    /**
     * Verifica si puede programar eventos
     */
    public boolean canScheduleEvents() {
        ensureStateInitialized();
        return state.canScheduleEvents();
    }

    /**
     * Establece un nuevo estado
     */
    public void setState(CampusState newState) {
        this.state = Objects.requireNonNull(newState, "State cannot be null");
        updatePersistedFields();
    }

    // ============ Métodos Privados de Soporte ============

    /**
     * Asegura que el estado esté inicializado (útil después de cargar desde BD)
     */
    private void ensureStateInitialized() {
        if (state == null) {
            restoreStateFromPersistedValue();
        }
    }

    /**
     * Restaura el estado desde el valor persistido en la base de datos
     */
    private void restoreStateFromPersistedValue() {
        String stateToRestore = currentState != null ? currentState : ACTIVE_STATE;

        this.state = createStateFromString(stateToRestore.toUpperCase());
    }

    /**
     * Factory method para crear estados desde string
     * @param stateName nombre del estado
     * @return instancia del estado correspondiente
     */
    private CampusState createStateFromString(String stateName) {
        return switch (stateName) {
            case ACTIVE_STATE -> new ActiveState();
            case INACTIVE_STATE -> new InactiveState();
            case MAINTENANCE_STATE -> new MaintenanceState();
            default -> new ActiveState(); // Estado por defecto seguro
        };
    }

    /**
     * Actualiza los campos que se persisten en la base de datos
     */
    private void updatePersistedFields() {
        if (state != null) {
            this.currentState = state.getStateName();
            this.active = state.canAcceptStudents();
        }
    }

    /**
     * Sincroniza el estado interno cuando se cambia el campo active externamente
     */
    private void synchronizeStateWithActiveField(boolean newActiveValue) {
        if (state != null && newActiveValue != state.canAcceptStudents()) {
            this.currentState = newActiveValue ? ACTIVE_STATE : INACTIVE_STATE;
            this.state = null; // Forzar re-inicialización en próximo acceso
        }
    }

    // ============ Getters y Setters Optimizados ============

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        synchronizeStateWithActiveField(active);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
        this.state = null; // El estado se restaurará cuando se necesite
    }

    // ============ Métodos de Object (equals, hashCode, toString) ============

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Campus campus = (Campus) obj;
        return Objects.equals(id, campus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Campus{id='%s', name='%s', currentState='%s', active=%s}",
                id, name, currentState, active);
    }

    // ============ Métodos de Utilidad ============

    /**
     * Verifica si el campus está en un estado específico
     * @param stateName nombre del estado a verificar
     * @return true si está en el estado especificado
     */
    public boolean isInState(String stateName) {
        return Objects.equals(this.currentState, stateName);
    }

    /**
     * Verifica si el campus está operativo (puede realizar actividades básicas)
     * @return true si está operativo
     */
    public boolean isOperational() {
        ensureStateInitialized();
        return state.canAcceptStudents() || state.canScheduleEvents();
    }

    /**
     * Obtiene información resumida del campus
     * @return string con información básica
     */
    public String getBasicInfo() {
        return String.format("%s - %s, %s (%s)", name, address, city, currentState);
    }
}