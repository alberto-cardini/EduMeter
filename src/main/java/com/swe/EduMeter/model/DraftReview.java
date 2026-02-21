package com.swe.EduMeter.model;

import java.time.LocalDate;

public class DraftReview extends Review {
    private String rawSchool;
    private String rawDegree;
    private String rawCourse;
    private String rawProfessor;

    public DraftReview(Integer id, String creatorHash,
                       String comment, LocalDate date,
                       int enjoyment, int difficulty,
                       String rawSchool, String rawDegree,
                       String rawCourse, String rawProfessor) {
        super(id, creatorHash, comment, date, enjoyment, difficulty);
        this.rawSchool = rawSchool;
        this.rawDegree = rawDegree;
        this.rawCourse = rawCourse;
        this.rawProfessor = rawProfessor;
    }

    public String getRawSchool() {
        return rawSchool;
    }

    public void setRawSchool(String rawSchool) {
        this.rawSchool = rawSchool;
    }

    public String getRawDegree() {
        return rawDegree;
    }

    public void setRawDegree(String rawDegree) {
        this.rawDegree = rawDegree;
    }

    public String getRawCourse() {
        return rawCourse;
    }

    public void setRawCourse(String rawCourse) {
        this.rawCourse = rawCourse;
    }

    public String getRawProfessor() {
        return rawProfessor;
    }

    public void setRawProfessor(String rawProfessor) {
        this.rawProfessor = rawProfessor;
    }

    @Override
    public String toString() {
        return "DraftReview{" +
                "rawSchool='" + rawSchool + '\'' +
                ", rawDegree='" + rawDegree + '\'' +
                ", rawCourse='" + rawCourse + '\'' +
                ", rawProfessor='" + rawProfessor + '\'' +
                "} " + super.toString();
    }
}
