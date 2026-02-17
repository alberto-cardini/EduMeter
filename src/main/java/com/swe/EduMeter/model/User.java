package com.swe.EduMeter.model;

public class User {
    private Integer id;
    private String hash;
    private Boolean banned;

    public User(int id, String hash, boolean banned) {
        this.id = id;
        this.hash = hash;
        this.banned = banned;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", banned=" + banned +
                '}';
    }
}
