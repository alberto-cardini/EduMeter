package com.swe.EduMeter.model;

public class Course {
    private int id;
    private String name;
    private Degree degree;

    public Course() {}

    public Course(int id, String name, Degree degree) {
        this.id = id;
        this.name = name;
        this.degree = degree;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Degree getDegree() { return degree; }
    public void setDegree(Degree degree) { this.degree = degree; }

    public String toString() { return "Degree {id: " + id + ", name: " + name + ", degree: " + degree + "}"; }
}
