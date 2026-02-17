package com.swe.EduMeter.model;

public class Admin {
    private Integer id;
    private String email;

    public Admin() {}
    public Admin(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", email='" + email + '\'' +
                "}";
    }
}
