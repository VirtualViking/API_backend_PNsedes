package com.example.university.campusmanagement.state;

import com.example.university.campusmanagement.model.Campus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Estado activo del campus - Campus completamente operativo
 */
public class ActiveState implements CampusState {

    private static final Logger logger = LoggerFactory.getLogger(ActiveState.class);

    @Override
    public void activate(Campus campus) {
        logger.info("Campus {} is already active", campus.getName());
    }

    @Override
    public void deactivate(Campus campus) {
        logger.info("Deactivating campus {}", campus.getName());
        campus.setState(new InactiveState());
    }

    @Override
    public void maintenance(Campus campus) {
        logger.info("Putting campus {} into maintenance", campus.getName());
        campus.setState(new MaintenanceState());
    }

    @Override
    public String getStatusDescription() {
        return "Campus is fully operational and ready for activities";
    }

    @Override
    public boolean canAcceptStudents() {
        return true;
    }

    @Override
    public boolean canScheduleEvents() {
        return true;
    }

    @Override
    public String getStateName() {
        return "ACTIVE";
    }
}