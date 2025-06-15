package com.example.university.campusmanagement.state;

import com.example.university.campusmanagement.model.Campus;

/**
 * Interface que define el comportamiento de los diferentes estados del campus.
 * Implementa el patrón State para manejar las transiciones de estado de manera controlada.
 */
public interface CampusState {

    /**
     * Activa el campus desde el estado actual
     * @param campus - Campus a activar
     */
    void activate(Campus campus);

    /**
     * Desactiva el campus desde el estado actual
     * @param campus - Campus a desactivar
     */
    void deactivate(Campus campus);

    /**
     * Pone el campus en mantenimiento desde el estado actual
     * @param campus - Campus a poner en mantenimiento
     */
    void maintenance(Campus campus);

    /**
     * Obtiene la descripción del estado actual
     * @return Descripción del estado
     */
    String getStatusDescription();

    /**
     * Verifica si el campus puede aceptar estudiantes en este estado
     * @return true si puede aceptar estudiantes, false en caso contrario
     */
    boolean canAcceptStudents();

    /**
     * Verifica si el campus puede programar eventos en este estado
     * @return true si puede programar eventos, false en caso contrario
     */
    boolean canScheduleEvents();

    /**
     * Obtiene el nombre del estado para persistencia
     * @return Nombre del estado
     */
    String getStateName();
}