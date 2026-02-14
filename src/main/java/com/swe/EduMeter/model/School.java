package com.swe.EduMeter.model;

public class School {

    private int id;
    private String name;

    // needed in order to let jersey-media-json-jackson map the JSON on a new School Entity
    public School() {}

    public School(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}";
    }
}
