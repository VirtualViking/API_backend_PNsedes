package com.example.university.campusmanagement.state;

import com.example.university.campusmanagement.model.Campus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Estado de mantenimiento del campus - Campus con acceso limitado
 */
public class MaintenanceState implements CampusState {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceState.class);

    @Override
    public void activate(Campus campus) {
        logger.info("Maintenance completed. Activating campus {}", campus.getName());
        campus.setState(new ActiveState());
    }

    @Override
    public void deactivate(Campus campus) {
        logger.info("Stopping maintenance and deactivating campus {}", campus.getName());
        campus.setState(new InactiveState());
    }

    @Override
    public void maintenance(Campus campus) {
        logger.info("Campus {} is already in maintenance", campus.getName());
    }

    @Override
    public String getStatusDescription() {
        return "Campus is under maintenance with limited access";
    }

    @Override
    public boolean canAcceptStudents() {
        return false;
    }

    @Override
    public boolean canScheduleEvents() {
        return false;
    }

    @Override
    public String getStateName() {
        return "MAINTENANCE";
    }
}