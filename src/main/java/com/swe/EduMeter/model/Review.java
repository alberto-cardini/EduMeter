package com.swe.EduMeter.model;

import java.time.LocalDate;

public class Review {
    private Integer id = 0;
    private String creatorHash;

    private Integer professorId;
    private Integer courseId;

    private String rawProfessor;
    private String rawCourse;
    private String rawSchool;
    private String rawDegree;

    private String comment;
    private LocalDate date;
    private int enjoyment;
    private int difficulty;
    private Integer upVote = 0;
    private ReviewStatus status;

    public Review() {}

    public Review(String creatorHash,
                  Integer professorId,
                  Integer courseId,
                  String comment,
                  LocalDate date,
                  int enjoyment,
                  int difficulty,
                  ReviewStatus status) {
        this.creatorHash = creatorHash;
        this.professorId = professorId;
        this.courseId = courseId;
        this.comment = comment;
        this.date = date;
        this.enjoyment = enjoyment;
        this.difficulty = difficulty;
        this.status = status;
    }

    public boolean isFullyStructured() {
        return  courseId != null && professorId != null &&
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

    public String getCreator() { return creatorHash; }
    public void setCreator(String creatorHash) { this.creatorHash = creatorHash; }

    public Integer getProfessor() { return professorId; }
    public void setProfessor(Integer professorId) { this.professorId = professorId; }

    public Integer getCourse() { return courseId; }
    public void setCourse(Integer course_id) { this.courseId = course_id; }

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

    public Integer getUp_vote() { return upVote; }
    public void setUp_vote(int up_vote) { this.upVote = up_vote; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public String toString() {
        return "Review { " +
                "id:" + id +
                ", creator id: " + creatorHash +
                ", professor id: " + professorId +
                ", course id: " + courseId +
                ", comment: " + comment +
                ", date: " + date +
                ", enjoyment: " + enjoyment +
                ", difficulty: " + difficulty +
                ", up_vote: " + upVote +
                "}"; }
}
