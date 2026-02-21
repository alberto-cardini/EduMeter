package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Pin;

import java.util.Optional;

public interface PinDAO {
    /**
     * Returns the last Pin sent to the user, if not
     * expired.
     *
     * @param userHash The user to search the sent Pin for.
     * @param isAdmin  Filter for user Pins or admin Pins.
     * @return         The Pin
     */
    Optional<Pin> get(String userHash, boolean isAdmin);
    void add(Pin pin);

    /**
     * After Pin authentication, remove the Pin entry,
     * to free up storage. This does not prevent storage
     * cluttering as unsuccessful attempts to login will
     * still take up space.
     * @param id The Pin instance to delete.
     */
    void delete(int id);
}
