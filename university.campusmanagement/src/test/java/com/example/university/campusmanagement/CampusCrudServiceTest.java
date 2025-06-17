package com.example.university.campusmanagement;

import com.example.university.campusmanagement.model.Campus;
import com.example.university.campusmanagement.repository.CampusRepository;
import com.example.university.campusmanagement.service.CampusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CampusService
 * Verifica operaciones CRUD básicas y funcionalidad del patrón State
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Campus CRUD Service Tests")
class CampusCrudServiceTest {

    @InjectMocks
    private CampusService campusCrudService;

    @Mock
    private CampusRepository campusRepository;

    private Campus campus;

    @BeforeEach
    void setUp() {
        campus = new Campus();
        campus.setId("1");
        campus.setName("Campus Central");
        campus.setAddress("Ciudad Principal");
        campus.setCity("Test City");
        campus.setTelephone("123456789");
        campus.setActive(true);
    }

    @Test
    @DisplayName("Should create campus successfully")
    void testCreate() {
        // Given
        when(campusRepository.save(any(Campus.class))).thenReturn(campus);

        // When
        Object result = campusCrudService.create(campus);

        // Then
        assertNotNull(result);
        assertEquals(campus, result);
        verify(campusRepository, times(1)).save(campus);
    }

    @Test
    @DisplayName("Should update campus successfully")
    void testUpdate() {
        // Given
        when(campusRepository.findById("1")).thenReturn(Optional.of(campus));
        when(campusRepository.save(any(Campus.class))).thenReturn(campus);

        // When
        Object result = campusCrudService.update("1", campus);

        // Then
        assertNotNull(result);
        assertInstanceOf(Campus.class, result);
        verify(campusRepository, times(1)).findById("1");
        verify(campusRepository, times(1)).save(any(Campus.class));
    }

    @Test
    @DisplayName("Should find campus by ID successfully")
    void testFindById() {
        // Given
        when(campusRepository.findById("1")).thenReturn(Optional.of(campus));

        // When
        Object result = campusCrudService.findById("1");

        // Then
        assertNotNull(result);
        assertEquals(campus, result);
        verify(campusRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Should return all campuses")
    void testFindAll() {
        // Given
        List<Campus> campusList = List.of(campus);
        when(campusRepository.findAll()).thenReturn(campusList);

        // When
        List<Object> result = campusCrudService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        // Verificar que el primer elemento es un Campus
        assertTrue(result.get(0) instanceof Campus);
        Campus resultCampus = (Campus) result.get(0);
        assertEquals(campus.getId(), resultCampus.getId());
        assertEquals(campus.getName(), resultCampus.getName());
        verify(campusRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should soft delete campus by deactivating it")
    void testDelete() {
        // Given
        when(campusRepository.findById("1")).thenReturn(Optional.of(campus));
        when(campusRepository.save(any(Campus.class))).thenReturn(campus);

        // When
        campusCrudService.delete("1");

        // Then
        verify(campusRepository, times(1)).findById("1");
        verify(campusRepository, times(1)).save(any(Campus.class));
    }

    @Test
    @DisplayName("Should throw exception when campus not found by ID")
    void testFindByIdThrowsException() {
        // Given
        when(campusRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> campusCrudService.findById("999"));

        assertTrue(exception.getMessage().contains("Campus not found with id: 999"));
        verify(campusRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Should throw exception when trying to delete non-existent campus")
    void testDeleteThrowsException() {
        // Given
        when(campusRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> campusCrudService.delete("999"));

        assertTrue(exception.getMessage().contains("Campus not found with id: 999"));
        verify(campusRepository, times(1)).findById("999");
        verify(campusRepository, never()).save(any(Campus.class));
    }

    @Test
    @DisplayName("Should throw exception for invalid entity type in create")
    void testCreateWithInvalidEntity() {
        // Given
        String invalidEntity = "Not a Campus";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> campusCrudService.create(invalidEntity));

        assertEquals("Entity must be of type Campus", exception.getMessage());
        verify(campusRepository, never()).save(any(Campus.class));
    }

    @Test
    @DisplayName("Should throw exception for invalid entity type in update")
    void testUpdateWithInvalidEntity() {
        // Given
        String invalidEntity = "Not a Campus";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> campusCrudService.update("1", invalidEntity));

        assertEquals("Entity must be of type Campus", exception.getMessage());
        verify(campusRepository, never()).findById(anyString());
        verify(campusRepository, never()).save(any(Campus.class));
    }

    @Test
    @DisplayName("Should set default state when creating campus without state")
    void testCreateSetsDefaultState() {
        // Given
        Campus newCampus = new Campus();
        newCampus.setName("New Campus");
        newCampus.setCurrentState(null); // Sin estado inicial

        Campus savedCampus = new Campus();
        savedCampus.setName("New Campus");
        savedCampus.setCurrentState("ACTIVE");

        when(campusRepository.save(any(Campus.class))).thenReturn(savedCampus);

        // When
        Object result = campusCrudService.create(newCampus);

        // Then
        assertNotNull(result);
        Campus resultCampus = (Campus) result;
        assertEquals("ACTIVE", resultCampus.getCurrentState());
        verify(campusRepository, times(1)).save(any(Campus.class));
    }

    @Test
    @DisplayName("Should return empty list when findAll returns empty")
    void testFindAllEmpty() {
        // Given
        when(campusRepository.findAll()).thenReturn(List.of());

        // When
        List<Object> result = campusCrudService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(campusRepository, times(1)).findAll();
    }
}