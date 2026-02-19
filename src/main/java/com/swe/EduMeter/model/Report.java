package com.swe.EduMeter.model;

public class Report {
    private Integer id;
    private String issuerHash;
    private int reviewId;

    public Report() {}

    public Report(Integer id, String issuerHash, int reviewId)
    {
        this.id = id;
        this.issuerHash = issuerHash;
        this.reviewId = reviewId;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIssuerHash() { return issuerHash; }
    public void setIssuerHash(String issuerHash) { this.issuerHash = issuerHash; }

    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }

    public String toString() {
        return "Report{" +
                "id=" + id +
                "issuer=" + issuerHash + '\'' +
                ", review=" + reviewId +
                "}";
    }
}
