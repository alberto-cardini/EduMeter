package com.swe.EduMeter.model;

public class Degree {
    private String name;
    private Degree.Type type;
    private Integer school_id;
    private Integer id;

    public Degree() {}

    public Degree(String name,
                  Degree.Type type,
                  Integer school_id,
                  int id)
    {
        this.name = name;
        this.type = type;
        this.school_id = school_id;
        this.id = id;
    }

    public Degree.Type getType() { return type; }

    public Integer getSchool() { return school_id; }

    public Integer getId() { return id; }

    public String getName() { return name; }

    public void setType(Degree.Type type) { this.type = type; }

    public void setSchool(int school_id) { this.school_id = school_id; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public String toString() { return "Degree{ type=" + type + ", school=" + school_id + "}"; }

    public enum Type{
        Bachelor,
        Master
    }
}

