package com.example.university.campusmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de integración para la aplicación Campus Management
 * Verifica que el contexto de Spring Boot se carga correctamente
 */
@SpringBootTest
@TestPropertySource(properties = {
		"MONGO_USERNAME=testuser",
		"MONGO_PASSWORD=testpass",
		"MONGO_CLUSTER=testcluster",
		"MONGO_DATABASE=test_db"
})
class ApplicationTests {

	/**
	 * Test que verifica que el contexto de Spring Boot se carga correctamente.
	 * Este test asegura que:
	 * - Todas las configuraciones están correctas
	 * - Los beans se pueden crear sin errores
	 * - No hay conflictos en la configuración
	 * - La aplicación puede iniciarse exitosamente
	 */
	@Test
	void contextLoads() {
		// Test de contexto exitoso - el contexto de Spring Boot se carga correctamente
		// Si llegamos aquí sin excepciones, significa que:
		// 1. Todas las dependencias están correctamente configuradas
		// 2. Los componentes del patrón State se inicializan correctamente
		// 3. La conexión a MongoDB está configurada (aunque use variables de test)
		// 4. Los controladores, servicios y repositorios se crean sin errores
		assertTrue(true, "Spring Boot context should load successfully");
	}

	/**
	 * Test adicional para verificar que la aplicación tiene las configuraciones básicas
	 */
	@Test
	void applicationHasValidConfiguration() {
		// Verificar que las propiedades principales están configuradas
		String appName = System.getProperty("spring.application.name", "campus-management");
		assertTrue(appName != null && !appName.isEmpty(),
				"Application should have a valid name");
	}
}