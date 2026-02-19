package com.swe.EduMeter.model;

import java.time.LocalDate;

public class Review {
    private Integer id;
    private Integer creator_id;

    private Integer professor_id;
    private Integer course_id;

    private String rawProfessor;
    private String rawCourse;
    private String rawSchool;
    private String rawDegree;

    private String comment;
    private LocalDate date;
    private int enjoyment;
    private int difficulty;
    private Integer up_vote;
    private ReviewStatus status;

    public Review() {}

    public boolean isFullyStructured() {
        return  course_id != null && professor_id != null &&
                rawSchool == null && rawDegree == null &&
                rawCourse == null && rawProfessor == null;
    }

    public String getRawProfessor() { return rawProfessor; }
    public void setRawProfessor(String rawProfessor) { this.rawProfessor = rawProfessor; }

    public String getRawCourse() { return rawCourse; }
    public void setRawCourse(String rawCourse) { this.rawCourse = rawCourse; }

    public String getRawDegree() { return rawDegree; }
    public void setRawDegree(String rawDegree) { this.rawDegree = rawDegree; }

    public String getRawSchool() { return rawSchool; }
    public void setRawSchool(String rawSchool) { this.rawSchool = rawSchool; }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getCreator() { return creator_id; }
    public void setCreator(Integer creator_id) { this.creator_id = creator_id; }

    public Integer getProfessor() { return professor_id; }
    public void setProfessor(Integer professor_id) { this.professor_id = professor_id; }

    public Integer getCourse() { return course_id; }
    public void setCourse(Integer course_id) { this.course_id = course_id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getEnjoyment() { return enjoyment; }
    public void setEnjoyment(int enjoyment) {
        if (enjoyment < 0 || enjoyment > 100) {
            this.enjoyment = 0;
        } else {
            this.enjoyment = enjoyment;
        }
    }

    public Integer getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) {
        if (difficulty < 0 || difficulty > 100) {
            this.difficulty = 0;
        }else{
            this.difficulty = difficulty;
        }
    }

    public Integer getUp_vote() { return up_vote; }
    public void setUp_vote(int up_vote) { this.up_vote = up_vote; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public String toString() {
        return "Review { " +
                "id:" + id +
                ", creator id: " + creator_id +
                ", professor id: " + professor_id +
                ", course id: " + course_id +
                ", comment: " + comment +
                ", date: " + date +
                ", enjoyment: " + enjoyment +
                ", difficulty: " + difficulty +
                ", up_vote: " + up_vote +
                "}"; }
}
