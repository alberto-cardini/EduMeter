package com.swe.EduMeter.model;

import java.time.LocalDate;

public class Review {
    private Integer id;
    private User creator;
    private Professor professor;
    private Course course;
    private Degree degree;
    private School school;
    private String comment;
    private LocalDate date;
    private Integer enjoyment;
    private Integer difficulty;
    private Integer up_vote;

    public Review() {}

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Degree getDegree() { return degree; }
    public void setDegree(Degree degree) { this.degree = degree; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

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

    public String toString() {
        return "Review { " +
                "id:" + id +
                ", creator: " + creator +
                ", professor: " + professor +
                ", course: " + course +
                ", degree: " + degree +
                ", school: " + school +
                ", comment: " + comment +
                ", date: " + date +
                ", enjoyment: " + enjoyment +
                ", difficulty: " + difficulty +
                ", up_vote: " + up_vote +
                "}"; }
}
