package com.example.university.campusmanagement;

import com.example.university.campusmanagement.model.Campus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests comprehensivos para el patrón State en Campus
 * Verifica todas las transiciones de estado posibles y restricciones
 */
@DisplayName("Campus State Pattern Tests")
class CampusStatePatternTest {

    private Campus campus;

    @BeforeEach
    void setUp() {
        campus = new Campus();
        campus.setName("Campus Test");
        campus.setAddress("Test Address");
        campus.setCity("Test City");
        campus.setTelephone("123456789");
    }

    @Nested
    @DisplayName("Initial State Tests")
    class InitialStateTests {

        @Test
        @DisplayName("Campus should start in ACTIVE state by default")
        void campusShouldStartInActiveState() {
            // Given & When - campus creado con constructor por defecto

            // Then
            assertEquals("ACTIVE", campus.getCurrentState());
            assertTrue(campus.canAcceptStudents());
            assertTrue(campus.canScheduleEvents());
            assertTrue(campus.isActive());
            assertNotNull(campus.getStatusDescription());
        }

        @Test
        @DisplayName("Campus should have valid status description in initial state")
        void campusShouldHaveValidStatusDescription() {
            // Given & When
            String description = campus.getStatusDescription();

            // Then
            assertNotNull(description);
            assertFalse(description.isEmpty());
            assertTrue(description.toLowerCase().contains("operational"));
        }
    }

    @Nested
    @DisplayName("Active State Behavior Tests")
    class ActiveStateTests {

        @Test
        @DisplayName("Active campus should allow all operations")
        void activeCampusShouldAllowAllOperations() {
            // Given - campus en estado ACTIVE

            // When & Then
            assertTrue(campus.canAcceptStudents());
            assertTrue(campus.canScheduleEvents());
            assertTrue(campus.isActive());
            assertTrue(campus.isOperational());
        }

        @Test
        @DisplayName("Active campus can transition to INACTIVE")
        void activeCampusCanTransitionToInactive() {
            // Given - campus en estado ACTIVE

            // When
            campus.deactivate();

            // Then
            assertEquals("INACTIVE", campus.getCurrentState());
            assertFalse(campus.canAcceptStudents());
            assertFalse(campus.canScheduleEvents());
            assertFalse(campus.isActive());
        }

        @Test
        @DisplayName("Active campus can transition to MAINTENANCE")
        void activeCampusCanTransitionToMaintenance() {
            // Given - campus en estado ACTIVE

            // When
            campus.putInMaintenance();

            // Then
            assertEquals("MAINTENANCE", campus.getCurrentState());
            assertFalse(campus.canAcceptStudents());
            assertFalse(campus.canScheduleEvents());
            assertFalse(campus.isActive());
        }

        @Test
        @DisplayName("Active campus should remain active when activated again")
        void activeCampusShouldRemainActiveWhenActivatedAgain() {
            // Given - campus en estado ACTIVE
            String initialState = campus.getCurrentState();

            // When
            campus.activate();

            // Then
            assertEquals(initialState, campus.getCurrentState());
            assertEquals("ACTIVE", campus.getCurrentState());
        }
    }

    @Nested
    @DisplayName("Inactive State Behavior Tests")
    class InactiveStateTests {

        @BeforeEach
        void setUpInactiveState() {
            campus.deactivate(); // Poner en estado INACTIVE
        }

        @Test
        @DisplayName("Inactive campus should not allow operations")
        void inactiveCampusShouldNotAllowOperations() {
            // Given - campus en estado INACTIVE

            // When & Then
            assertFalse(campus.canAcceptStudents());
            assertFalse(campus.canScheduleEvents());
            assertFalse(campus.isActive());
            assertFalse(campus.isOperational());
        }

        @Test
        @DisplayName("Inactive campus can transition to ACTIVE")
        void inactiveCampusCanTransitionToActive() {
            // Given - campus en estado INACTIVE

            // When
            campus.activate();

            // Then
            assertEquals("ACTIVE", campus.getCurrentState());
            assertTrue(campus.canAcceptStudents());
            assertTrue(campus.canScheduleEvents());
            assertTrue(campus.isActive());
        }

        @Test
        @DisplayName("Inactive campus cannot transition to MAINTENANCE")
        void inactiveCampusCannotTransitionToMaintenance() {
            // Given - campus en estado INACTIVE

            // When & Then
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> campus.putInMaintenance()
            );

            assertTrue(exception.getMessage().contains("Cannot put inactive campus into maintenance"));
            assertEquals("INACTIVE", campus.getCurrentState()); // Estado no debe cambiar
        }

        @Test
        @DisplayName("Inactive campus should remain inactive when deactivated again")
        void inactiveCampusShouldRemainInactiveWhenDeactivatedAgain() {
            // Given - campus en estado INACTIVE
            String initialState = campus.getCurrentState();

            // When
            campus.deactivate();

            // Then
            assertEquals(initialState, campus.getCurrentState());
            assertEquals("INACTIVE", campus.getCurrentState());
        }
    }

    @Nested
    @DisplayName("Maintenance State Behavior Tests")
    class MaintenanceStateTests {

        @BeforeEach
        void setUpMaintenanceState() {
            campus.putInMaintenance(); // Poner en estado MAINTENANCE
        }

        @Test
        @DisplayName("Maintenance campus should not allow operations")
        void maintenanceCampusShouldNotAllowOperations() {
            // Given - campus en estado MAINTENANCE

            // When & Then
            assertFalse(campus.canAcceptStudents());
            assertFalse(campus.canScheduleEvents());
            assertFalse(campus.isActive());
            assertFalse(campus.isOperational());
        }

        @Test
        @DisplayName("Maintenance campus can transition to ACTIVE")
        void maintenanceCampusCanTransitionToActive() {
            // Given - campus en estado MAINTENANCE

            // When
            campus.activate();

            // Then
            assertEquals("ACTIVE", campus.getCurrentState());
            assertTrue(campus.canAcceptStudents());
            assertTrue(campus.canScheduleEvents());
            assertTrue(campus.isActive());
        }

        @Test
        @DisplayName("Maintenance campus can transition to INACTIVE")
        void maintenanceCampusCanTransitionToInactive() {
            // Given - campus en estado MAINTENANCE

            // When
            campus.deactivate();

            // Then
            assertEquals("INACTIVE", campus.getCurrentState());
            assertFalse(campus.canAcceptStudents());
            assertFalse(campus.canScheduleEvents());
            assertFalse(campus.isActive());
        }

        @Test
        @DisplayName("Maintenance campus should remain in maintenance when maintenance called again")
        void maintenanceCampusShouldRemainInMaintenanceWhenMaintenanceCalledAgain() {
            // Given - campus en estado MAINTENANCE
            String initialState = campus.getCurrentState();

            // When
            campus.putInMaintenance();

            // Then
            assertEquals(initialState, campus.getCurrentState());
            assertEquals("MAINTENANCE", campus.getCurrentState());
        }

        @Test
        @DisplayName("Maintenance campus should have appropriate status description")
        void maintenanceCampusShouldHaveAppropriateStatusDescription() {
            // Given - campus en estado MAINTENANCE

            // When
            String description = campus.getStatusDescription();

            // Then
            assertNotNull(description);
            assertTrue(description.toLowerCase().contains("maintenance"));
            assertTrue(description.toLowerCase().contains("limited"));
        }
    }

    @Nested
    @DisplayName("State Transition Chain Tests")
    class StateTransitionChainTests {

        @Test
        @DisplayName("Should support full state cycle: ACTIVE -> INACTIVE -> ACTIVE")
        void shouldSupportActiveToInactiveToActive() {
            // Given - campus en estado ACTIVE
            assertEquals("ACTIVE", campus.getCurrentState());

            // When - ACTIVE -> INACTIVE
            campus.deactivate();
            assertEquals("INACTIVE", campus.getCurrentState());

            // When - INACTIVE -> ACTIVE
            campus.activate();

            // Then
            assertEquals("ACTIVE", campus.getCurrentState());
            assertTrue(campus.canAcceptStudents());
        }

        @Test
        @DisplayName("Should support full state cycle: ACTIVE -> MAINTENANCE -> ACTIVE")
        void shouldSupportActiveToMaintenanceToActive() {
            // Given - campus en estado ACTIVE
            assertEquals("ACTIVE", campus.getCurrentState());

            // When - ACTIVE -> MAINTENANCE
            campus.putInMaintenance();
            assertEquals("MAINTENANCE", campus.getCurrentState());

            // When - MAINTENANCE -> ACTIVE
            campus.activate();

            // Then
            assertEquals("ACTIVE", campus.getCurrentState());
            assertTrue(campus.canAcceptStudents());
        }

        @Test
        @DisplayName("Should support complex state transitions")
        void shouldSupportComplexStateTransitions() {
            // Given - campus en estado ACTIVE
            assertEquals("ACTIVE", campus.getCurrentState());

            // ACTIVE -> MAINTENANCE -> INACTIVE -> ACTIVE
            campus.putInMaintenance();
            assertEquals("MAINTENANCE", campus.getCurrentState());

            campus.deactivate();
            assertEquals("INACTIVE", campus.getCurrentState());

            campus.activate();
            assertEquals("ACTIVE", campus.getCurrentState());

            // Verificar que todas las capacidades están restauradas
            assertTrue(campus.canAcceptStudents());
            assertTrue(campus.canScheduleEvents());
            assertTrue(campus.isActive());
        }
    }

    @Nested
    @DisplayName("State Persistence Tests")
    class StatePersistenceTests {

        @Test
        @DisplayName("State should persist after setting currentState")
        void stateShouldPersistAfterSettingCurrentState() {
            // Given
            campus.setCurrentState("MAINTENANCE");

            // When
            String description = campus.getStatusDescription();
            boolean canAcceptStudents = campus.canAcceptStudents();

            // Then
            assertEquals("MAINTENANCE", campus.getCurrentState());
            assertFalse(canAcceptStudents);
            assertNotNull(description);
            assertTrue(description.toLowerCase().contains("maintenance"));
        }

        @Test
        @DisplayName("Should handle unknown state gracefully")
        void shouldHandleUnknownStateGracefully() {
            // Given
            campus.setCurrentState("UNKNOWN_STATE");

            // When - debería usar estado por defecto (ACTIVE)
            boolean canAcceptStudents = campus.canAcceptStudents();
            String description = campus.getStatusDescription();

            // Then
            assertTrue(canAcceptStudents); // Comportamiento de ACTIVE como fallback
            assertNotNull(description);
        }

        @Test
        @DisplayName("Should handle null state gracefully")
        void shouldHandleNullStateGracefully() {
            // Given
            campus.setCurrentState(null);

            // When - debería usar estado por defecto (ACTIVE)
            boolean canAcceptStudents = campus.canAcceptStudents();
            String description = campus.getStatusDescription();

            // Then
            assertTrue(canAcceptStudents); // Comportamiento de ACTIVE como fallback
            assertNotNull(description);
        }
    }

    @Nested
    @DisplayName("State Utility Methods Tests")
    class StateUtilityMethodsTests {

        @Test
        @DisplayName("isInState should work correctly for all states")
        void isInStateShouldWorkCorrectlyForAllStates() {
            // Test ACTIVE state
            assertTrue(campus.isInState("ACTIVE"));
            assertFalse(campus.isInState("INACTIVE"));
            assertFalse(campus.isInState("MAINTENANCE"));

            // Test INACTIVE state
            campus.deactivate();
            assertFalse(campus.isInState("ACTIVE"));
            assertTrue(campus.isInState("INACTIVE"));
            assertFalse(campus.isInState("MAINTENANCE"));

            // Test MAINTENANCE state
            campus.activate();
            campus.putInMaintenance();
            assertFalse(campus.isInState("ACTIVE"));
            assertFalse(campus.isInState("INACTIVE"));
            assertTrue(campus.isInState("MAINTENANCE"));
        }

        @Test
        @DisplayName("isOperational should reflect state capabilities")
        void isOperationalShouldReflectStateCapabilities() {
            // ACTIVE - should be operational
            assertTrue(campus.isOperational());

            // INACTIVE - should not be operational
            campus.deactivate();
            assertFalse(campus.isOperational());

            // MAINTENANCE - should not be operational
            campus.activate();
            campus.putInMaintenance();
            assertFalse(campus.isOperational());
        }

        @Test
        @DisplayName("getBasicInfo should include current state")
        void getBasicInfoShouldIncludeCurrentState() {
            // Given
            String basicInfo = campus.getBasicInfo();

            // Then
            assertNotNull(basicInfo);
            assertTrue(basicInfo.contains(campus.getName()));
            assertTrue(basicInfo.contains(campus.getCurrentState()));
        }
    }
}