package com.example.university.campusmanagement.state;

import com.example.university.campusmanagement.model.Campus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Estado inactivo del campus - Campus no operativo
 */
public class InactiveState implements CampusState {

    private static final Logger logger = LoggerFactory.getLogger(InactiveState.class);

    @Override
    public void activate(Campus campus) {
        logger.info("Activating campus {}", campus.getName());
        campus.setState(new ActiveState());
    }

    @Override
    public void deactivate(Campus campus) {
        logger.info("Campus {} is already inactive", campus.getName());
    }

    @Override
    public void maintenance(Campus campus) {
        logger.info("Cannot put inactive campus {} into maintenance. Activate first.", campus.getName());
        throw new IllegalStateException("Cannot put inactive campus into maintenance. Activate first.");
    }

    @Override
    public String getStatusDescription() {
        return "Campus is not operational and unavailable for activities";
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
        return "INACTIVE";
    }
}