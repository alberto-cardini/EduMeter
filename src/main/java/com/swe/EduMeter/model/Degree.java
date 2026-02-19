package com.swe.EduMeter.model;

public class Degree {
    private Integer id;
    private String name;
    private Degree.Type type;
    private int schoolId;

    public Degree() {}

    public Degree(Integer id, String name, Degree.Type type, int schoolId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.schoolId = schoolId;
    }

    public Degree.Type getType() { return type; }

    public int getSchoolId() { return schoolId; }

    public Integer getId() { return id; }

    public String getName() { return name; }

    public void setType(Degree.Type type) { this.type = type; }

    public void setSchoolId(int schoolId) { this.schoolId = schoolId; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public String toString() {
        return "Degree{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", schoolId=" + schoolId +
                "}";
    }

    public enum Type{
        Bachelor,
        Master
    }
}

