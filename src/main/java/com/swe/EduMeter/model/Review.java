package com.swe.EduMeter.model;

import java.time.LocalDate;

public abstract class Review {
    private Integer id = 0;
    private String creatorHash;

    private String comment;
    private LocalDate date;

    /** Overall enjoyment of the course, 0 <= enj <= 100 */
    private int enjoyment;

    /** Perceived difficulty of the course, 0 <= diff <= 100 */
    private int difficulty;

    public Review() {}

    public Review(Integer id, String creatorHash,
                  String comment, LocalDate date,
                  int enjoyment, int difficulty) {
        this.id = id;
        this.creatorHash = creatorHash;
        this.comment = comment;
        this.date = date;
        this.enjoyment = enjoyment;
        this.difficulty = difficulty;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCreatorHash() { return creatorHash; }
    public void setCreatorHash(String creatorHash) { this.creatorHash = creatorHash; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getEnjoyment() { return enjoyment; }
    public void setEnjoyment(int enjoyment) {
        if (enjoyment < 0) {
            this.enjoyment = 0;
            return;
        }
        else if (enjoyment > 100) {
            this.enjoyment = 100;
            return;
        }

        this.enjoyment = enjoyment;
    }

    public Integer getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) {
        if (difficulty < 0) {
            this.difficulty = 0;
            return;
        }
        else if (difficulty > 100) {
            this.difficulty = 100;
            return;
        }

        this.difficulty = difficulty;
    }

    public String toString() {
        return "Review{" +
                "id=" + id +
                ", creator id='" + creatorHash + '\'' +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", enjoyment=" + enjoyment +
                ", difficulty=" + difficulty +
                "}";
    }
}
