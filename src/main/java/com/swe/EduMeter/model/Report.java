package com.swe.EduMeter.model;

import java.time.LocalDate;

public class Report {
    private Integer id;
    private String comment;
    private LocalDate date;
    private String issuerHash;
    private int reviewId;

    public Report() {}

    public Report(Integer id, String comment, LocalDate date, String issuerHash, int reviewId)
    {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.issuerHash = issuerHash;
        this.reviewId = reviewId;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getIssuerHash() { return issuerHash; }
    public void setIssuerHash(String issuerHash) { this.issuerHash = issuerHash; }

    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }

    public String toString() {
        return "Report{" +
                "id=" + id +
                "comment='" + id + '\'' +
                "date=" + date +
                "issuer=" + issuerHash + '\'' +
                ", review=" + reviewId +
                "}";
    }
}
