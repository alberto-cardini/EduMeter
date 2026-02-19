package com.swe.EduMeter.model;

public class Degree {
    private String name;
    private Degree.Type type;
    private School school;
    private Integer id;

    public Degree() {}

    public Degree(String name, Degree.Type type, School school, int id) {
        this.name = name;
        this.type = type;
        this.school = school;
        this.id = id;
    }

    public Degree.Type getType() { return type; }

    public School getSchool() { return school; }

    public Integer getId() { return id; }

    public String getName() { return name; }

    public void setType(Degree.Type type) { this.type = type; }

    public void setSchool(School school) { this.school = school; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public String toString() { return "Degree{ type=" + type + ", school=" + school + "}"; }

    public enum Type{
        Bachelor,
        Master
    }
}

