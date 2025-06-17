package com.example.university.campusmanagement.constants;

/**
 * Clase que contiene todas las constantes utilizadas en la aplicación
 * para evitar duplicación de literales y mejorar el mantenimiento
 */
public final class Constants {

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Mensajes de respuesta
    public static final class Messages {
        public static final String CAMPUS_ACTIVATED = "Campus activated successfully";
        public static final String CAMPUS_DEACTIVATED = "Campus deactivated successfully";
        public static final String CAMPUS_MAINTENANCE = "Campus put in maintenance mode";
        public static final String CAMPUS_RENOVATION = "Campus renovation started";
        public static final String ERROR = "error";

        private Messages() {}
    }

    // Campos de respuesta JSON
    public static final class ResponseFields {
        public static final String MESSAGE = "message";
        public static final String STATUS = "status";
        public static final String ERROR = "error";
        public static final String CAMPUS_NAME = "campusName";
        public static final String CURRENT_STATE = "currentState";
        public static final String STATUS_DESCRIPTION = "statusDescription";
        public static final String CAN_ACCEPT_STUDENTS = "canAcceptStudents";
        public static final String CAN_SCHEDULE_EVENTS = "canScheduleEvents";
        public static final String IS_ACTIVE = "isActive";

        private ResponseFields() {}
    }

    // Estados del campus
    public static final class States {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String MAINTENANCE = "MAINTENANCE";
        public static final String RENOVATION = "RENOVATION";

        private States() {}
    }

    // Mensajes de error
    public static final class ErrorMessages {
        public static final String CAMPUS_NOT_FOUND = "Campus not found";
        public static final String INVALID_ENTITY_TYPE = "Entity must be of type Campus";

        private ErrorMessages() {}
    }
}
