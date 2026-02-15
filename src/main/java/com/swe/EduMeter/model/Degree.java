package com.swe.EduMeter.model;

enum Degree_Type{
    Bachelor,
    Master
}

public class Degree {
    private String name;
    private Degree_Type type;
    private School school;
    private int id;

    public Degree() {}

    public Degree(Degree_Type type, School school, int id) {
        this.type = type;
        this.school = school;
        this.id = id;
    }

    public Degree_Type getType() { return type; }

    public School getSchool() { return school; }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setType(Degree_Type type) { this.type = type; }

    public void setSchool(School school) { this.school = school; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public String toString() { return "Degree{ type=" + type + ", school=" + school + "}"; }
}

