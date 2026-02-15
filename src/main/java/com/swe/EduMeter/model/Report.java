package com.swe.EduMeter.model;

enum Report_Status{
    Accepted,
    Rejected,
    Pending,
}

public class Report {
    private int id;
    private User issuer;
    private Review review;
    private Report_Status status;

    public Report() {}

    public Report(int id, User issuer, Review review, Report_Status status) {
        this.id = id;
        this.issuer = issuer;
        this.review = review;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getIssuer() { return issuer; }
    public void setIssuer(User issuer) { this.issuer = issuer; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }

    public Report_Status getStatus() { return status; }
    public void setStatus(Report_Status status) { this.status = status; }

    public String toString() { return "Report { issuer:" + issuer.toString()
                                                         + ", review:" + review.toString()
                                                         + ", status:" + status.toString()
                                                         + "}"; }
}
