package com.swe.EduMeter.model;

public class Course {
    private Integer id;
    private String name;
    private int degreeId;

    public Course() {}

    public Course(Integer id, String name, int degreeId) {
        this.id = id;
        this.name = name;
        this.degreeId = degreeId;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDegreeId() { return degreeId; }
    public void setDegree(int degreeId) { this.degreeId = degreeId; }

    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", degreeId=" + degreeId +
                "}";
    }
}
