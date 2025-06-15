package com.example.university.campusmanagement.service;

import com.example.university.campusmanagement.model.Campus;
import com.example.university.campusmanagement.repository.CampusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestión de campus con soporte para el patrón State
 */
@Service
public class CampusService implements CrudService {

    private static final Logger logger = LoggerFactory.getLogger(CampusService.class);

    // Constantes para evitar magic strings
    private static final String ACTIVE_STATE = "ACTIVE";
    private static final String CAMPUS_NOT_FOUND_MSG = "Campus not found with id: ";
    private static final String ENTITY_TYPE_ERROR = "Entity must be of type Campus";

    private final CampusRepository campusRepository;

    public CampusService(CampusRepository campusRepository) {
        this.campusRepository = Objects.requireNonNull(campusRepository, "CampusRepository cannot be null");
    }

    // ============ Implementación de CrudService (manteniendo tipos Object) ============

    @Override
    public Object create(Object entity) {
        Campus campus = validateAndCastToCampus(entity);

        // Asegurar que el campus tenga un estado inicial válido
        if (campus.getCurrentState() == null || campus.getCurrentState().isEmpty()) {
            campus.setCurrentState(ACTIVE_STATE);
        }

        logger.info("Creating new campus: {} with state: {}", campus.getName(), campus.getCurrentState());
        return campusRepository.save(campus);
    }

    @Override
    public List<Object> findAll() {
        List<Campus> campuses = campusRepository.findAll();
        logger.debug("Retrieved {} campuses from database", campuses.size());
        // CORREGIDO: Usar referencia de método en lugar de lambda
        return campuses.stream().map(Object.class::cast).toList();
    }

    @Override
    public Object update(String id, Object entity) {
        Campus campusUpdate = validateAndCastToCampus(entity);
        Campus existingCampus = findCampusById(id);

        updateCampusFields(existingCampus, campusUpdate);

        return campusRepository.save(existingCampus);
    }

    @Override
    public Object findById(String id) {
        Campus campus = findCampusById(id);
        logger.debug("Retrieved campus: {} with state: {}", campus.getName(), campus.getCurrentState());
        return campus; // Campus es compatible con Object
    }

    @Override
    public void delete(String id) {
        Campus campus = findCampusById(id);

        // En lugar de eliminar, desactivamos el campus (soft delete)
        campus.deactivate();
        campusRepository.save(campus);

        logger.info("Campus {} has been deactivated", campus.getName());
    }

    // ============ Métodos específicos de Campus (con tipos Campus para mayor claridad) ============

    /**
     * Obtiene todos los campus como lista tipada
     * @return lista de campus
     */
    public List<Campus> getAllCampuses() {
        List<Campus> campuses = campusRepository.findAll();
        logger.debug("Retrieved {} campuses from database", campuses.size());
        return campuses;
    }

    /**
     * Activa un campus por ID
     * @param id ID del campus a activar
     * @return Campus activado
     * @throws RuntimeException si el campus no existe
     */
    public Campus activateCampus(String id) {
        Campus campus = findCampusById(id);
        campus.activate();
        Campus savedCampus = campusRepository.save(campus);
        logger.info("Campus {} activated successfully", savedCampus.getName());
        return savedCampus;
    }

    /**
     * Desactiva un campus por ID
     * @param id ID del campus a desactivar
     * @return Campus desactivado
     * @throws RuntimeException si el campus no existe
     */
    public Campus deactivateCampus(String id) {
        Campus campus = findCampusById(id);
        campus.deactivate();
        Campus savedCampus = campusRepository.save(campus);
        logger.info("Campus {} deactivated successfully", savedCampus.getName());
        return savedCampus;
    }

    /**
     * Pone un campus en mantenimiento por ID
     * @param id ID del campus a poner en mantenimiento
     * @return Campus en mantenimiento
     * @throws RuntimeException si el campus no existe
     * @throws IllegalStateException si la transición de estado no es válida
     */
    public Campus putCampusInMaintenance(String id) {
        Campus campus = findCampusById(id);
        campus.putInMaintenance();
        Campus savedCampus = campusRepository.save(campus);
        logger.info("Campus {} put in maintenance mode", savedCampus.getName());
        return savedCampus;
    }

    /**
     * Busca campus por estado
     * @param stateName nombre del estado a buscar
     * @return lista de campus en el estado especificado
     */
    public List<Campus> findByState(String stateName) {
        if (stateName == null || stateName.trim().isEmpty()) {
            return List.of();
        }

        List<Campus> allCampuses = campusRepository.findAll();
        return allCampuses.stream()
                .filter(campus -> stateName.equalsIgnoreCase(campus.getCurrentState()))
                .toList();
    }

    /**
     * Busca campus que pueden aceptar estudiantes
     * @return lista de campus disponibles para estudiantes
     */
    public List<Campus> findAvailableForStudents() {
        List<Campus> allCampuses = campusRepository.findAll();
        return allCampuses.stream()
                .filter(Campus::canAcceptStudents)
                .toList();
    }

    // ============ Métodos Privados de Utilidad ============

    /**
     * Valida y convierte un objeto a Campus
     * @param entity objeto a validar
     * @return Campus validado
     * @throws IllegalArgumentException si el objeto no es un Campus
     */
    private Campus validateAndCastToCampus(Object entity) {
        if (!(entity instanceof Campus campus)) {
            throw new IllegalArgumentException(ENTITY_TYPE_ERROR);
        }
        return campus;
    }

    /**
     * Busca un campus por ID con manejo de excepción
     * @param id ID del campus
     * @return Campus encontrado
     * @throws RuntimeException si no se encuentra el campus
     */
    private Campus findCampusById(String id) {
        return campusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(CAMPUS_NOT_FOUND_MSG + id));
    }

    /**
     * Actualiza los campos de un campus existente
     * @param existingCampus campus a actualizar
     * @param campusUpdate campus con los nuevos datos
     */
    private void updateCampusFields(Campus existingCampus, Campus campusUpdate) {
        // Actualizar campos básicos (manteniendo compatibilidad)
        if (campusUpdate.getName() != null) {
            existingCampus.setName(campusUpdate.getName());
        }

        if (campusUpdate.getAddress() != null) {
            existingCampus.setAddress(campusUpdate.getAddress());
        }

        if (campusUpdate.getCity() != null) {
            existingCampus.setCity(campusUpdate.getCity());
        }

        if (campusUpdate.getTelephone() != null) {
            existingCampus.setTelephone(campusUpdate.getTelephone());
        }

        // Actualizar estado si ha cambiado
        updateCampusState(existingCampus, campusUpdate);

        // Mantener compatibilidad con el campo active
        existingCampus.setActive(campusUpdate.isActive());
    }

    /**
     * Actualiza el estado de un campus si es necesario
     * @param existingCampus campus existente
     * @param campusUpdate campus con posible nuevo estado
     */
    private void updateCampusState(Campus existingCampus, Campus campusUpdate) {
        String newState = campusUpdate.getCurrentState();
        String currentState = existingCampus.getCurrentState();

        if (newState != null && !newState.equals(currentState)) {
            existingCampus.setCurrentState(newState);
            logger.info("Updated campus {} state from {} to {}",
                    existingCampus.getName(), currentState, newState);
        }
    }
}