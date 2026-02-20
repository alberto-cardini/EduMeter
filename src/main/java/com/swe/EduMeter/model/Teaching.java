package com.swe.EduMeter.model;

public class Teaching {
    private Integer id;
    private Integer courseId;
    private Integer profId;

    public Teaching(Integer id, Integer courseId, Integer profId) {
        this.id = id;
        this.courseId = courseId;
        this.profId = profId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getProfId() {
        return profId;
    }

    public void setProfId(Integer profId) {
        this.profId = profId;
    }

    @Override
    public String toString() {
        return "Teaching{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", profId=" + profId +
                '}';
    }
}
