// CampusController.java corregido con .toList() moderno
package com.example.university.campusmanagement.controller;

import com.example.university.campusmanagement.dto.CampusCreateRequest;
import com.example.university.campusmanagement.dto.CampusDto;
import com.example.university.campusmanagement.dto.CampusStatusDto;
import com.example.university.campusmanagement.dto.CampusUpdateRequest;
import com.example.university.campusmanagement.factory.CrudFactory;
import com.example.university.campusmanagement.model.Campus;
import com.example.university.campusmanagement.service.CampusService;
import com.example.university.campusmanagement.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de campus universitarios
 * Incluye endpoints para el patrón State y operaciones CRUD básicas
 */
@RestController
@RequestMapping("/api/campuses")
public class CampusController {

    private static final Logger logger = LoggerFactory.getLogger(CampusController.class);

    // Constante para evitar duplicación del mensaje de error
    private static final String CAMPUS_NOT_FOUND_MSG = "Campus not found";

    private final CrudService crudService;
    private final CampusService campusService;

    public CampusController(CrudFactory crudFactory) {
        this.crudService = crudFactory.createCrudService();
        this.campusService = (CampusService) this.crudService; // Safe cast ya que sabemos que es CampusService
    }

    // ============ CRUD Básico con DTOs ============

    @PostMapping
    public ResponseEntity<CampusDto> createCampus(@RequestBody CampusCreateRequest request) {
        logger.info("Creating new campus: {}", request.getName());

        // Convertir DTO request a entidad
        Campus campus = new Campus();
        campus.setName(request.getName());
        campus.setAddress(request.getAddress());
        campus.setCity(request.getCity());
        campus.setTelephone(request.getTelephone());

        Campus savedCampus = (Campus) crudService.create(campus);
        CampusDto responseDto = CampusDto.fromCampus(savedCampus);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampusDto> updateCampus(@PathVariable String id, @RequestBody CampusUpdateRequest request) {
        try {
            logger.info("Updating campus with id: {}", id);

            // Convertir DTO request a entidad
            Campus campus = new Campus();
            campus.setName(request.getName());
            campus.setAddress(request.getAddress());
            campus.setCity(request.getCity());
            campus.setTelephone(request.getTelephone());
            if (request.getActive() != null) {
                campus.setActive(request.getActive());
            }

            Campus updatedCampus = (Campus) crudService.update(id, campus);
            CampusDto responseDto = CampusDto.fromCampus(updatedCampus);

            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampusDto> getCampus(@PathVariable String id) {
        try {
            logger.debug("Retrieving campus with id: {}", id);
            Campus campus = (Campus) crudService.findById(id);
            CampusDto responseDto = CampusDto.fromCampus(campus);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> disableCampus(@PathVariable String id) {
        try {
            logger.info("Disabling campus with id: {}", id);
            crudService.delete(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Campus deactivated successfully");
            response.put("id", id);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", CAMPUS_NOT_FOUND_MSG);
            errorResponse.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<CampusDto>> getAllCampuses() {
        logger.debug("Retrieving all campuses");
        List<Object> campuses = crudService.findAll();

        // CORREGIDO: Usar .toList() en lugar de .collect(Collectors.toList())
        List<CampusDto> campusDtos = campuses.stream()
                .filter(Campus.class::isInstance)
                .map(Campus.class::cast)
                .map(CampusDto::fromCampus)
                .toList();

        return ResponseEntity.ok(campusDtos);
    }

    // ============ State Pattern Endpoints ============

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateCampus(@PathVariable String id) {
        try {
            Campus campus = campusService.activateCampus(id);
            Map<String, Object> response = createStateResponse(campus, "Campus activated successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return createErrorResponse(CAMPUS_NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateCampus(@PathVariable String id) {
        try {
            Campus campus = campusService.deactivateCampus(id);
            Map<String, Object> response = createStateResponse(campus, "Campus deactivated successfully");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return createErrorResponse(CAMPUS_NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/maintenance")
    public ResponseEntity<Map<String, Object>> putCampusInMaintenance(@PathVariable String id) {
        try {
            Campus campus = campusService.putCampusInMaintenance(id);
            Map<String, Object> response = createStateResponse(campus, "Campus put in maintenance mode");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return createErrorResponse(CAMPUS_NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<CampusStatusDto> getCampusStatus(@PathVariable String id) {
        try {
            Campus campus = (Campus) crudService.findById(id);
            CampusStatusDto status = CampusStatusDto.fromCampus(campus);
            return ResponseEntity.ok(status);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ============ Consultas por Estado (usando DTOs) ============

    @GetMapping("/state/{stateName}")
    public ResponseEntity<List<CampusDto>> getCampusesByState(@PathVariable String stateName) {
        List<Campus> campuses = campusService.findByState(stateName);
        // CORREGIDO: Usar .toList() en lugar de .collect(Collectors.toList())
        List<CampusDto> campusDtos = campuses.stream()
                .map(CampusDto::fromCampus)
                .toList();
        return ResponseEntity.ok(campusDtos);
    }

    @GetMapping("/available/students")
    public ResponseEntity<List<CampusDto>> getCampusesAvailableForStudents() {
        List<Campus> campuses = campusService.findAvailableForStudents();
        // CORREGIDO: Usar .toList() en lugar de .collect(Collectors.toList())
        List<CampusDto> campusDtos = campuses.stream()
                .map(CampusDto::fromCampus)
                .toList();
        return ResponseEntity.ok(campusDtos);
    }

    // ============ Métodos de Utilidad (extraídos para evitar duplicación) ============

    private Map<String, Object> createStateResponse(Campus campus, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("campusId", campus.getId());
        response.put("campusName", campus.getName());
        response.put("currentState", campus.getCurrentState());
        response.put("statusDescription", campus.getStatusDescription());
        response.put("canAcceptStudents", campus.canAcceptStudents());
        response.put("canScheduleEvents", campus.canScheduleEvents());
        return response;
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("status", status.value());
        return ResponseEntity.status(status).body(errorResponse);
    }
}