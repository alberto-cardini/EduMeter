package com.swe.EduMeter.model;

import java.time.Instant;

public class PinChallenge {
    private Integer id;
    private String pin;
    private String userHash;
    private Instant expiresAt;
    private boolean admin;

    public PinChallenge() {}

    public PinChallenge(Integer id, String pin, String userHash, Instant expiresAt, boolean admin) {
        this.id = id;
        this.pin = pin;
        this.userHash = userHash;
        this.expiresAt = expiresAt;
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    @Override
    public String toString() {
        return "PinChallenge{" +
                "id=" + id +
                ", pin='" + pin + '\'' +
                ", userHash='" + userHash + '\'' +
                ", expiresAt=" + expiresAt +
                ", admin=" + admin +
                '}';
    }
}
