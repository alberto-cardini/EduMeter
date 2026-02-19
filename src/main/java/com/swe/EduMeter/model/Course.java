package com.swe.EduMeter.model;

public class Course {
    private Integer id;
    private String name;
    private Integer degree_id;

    public Course() {}

    public Course(int id, String name, int degree_id) {
        this.id = id;
        this.name = name;
        this.degree_id = degree_id;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDegree() { return degree_id; }
    public void setDegree(Integer degree_id) { this.degree_id = degree_id; }

    public String toString()
    {
        return "Degree {id: " + id
                + ", name: " + name
                + ", degree: " + degree_id
                + "}";
    }
}
