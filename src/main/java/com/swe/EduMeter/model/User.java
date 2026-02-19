package com.swe.EduMeter.model;

public class User {
    private String hash;
    private Boolean banned;

    public User(String hash, boolean banned) {
        this.hash = hash;
        this.banned = banned;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "hash='" + hash + '\'' +
                ", banned=" + banned +
                '}';
    }
}
