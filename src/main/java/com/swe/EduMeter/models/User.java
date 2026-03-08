package com.swe.EduMeter.models;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return banned == user.banned && Objects.equals(hash, user.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, banned);
    }
}
