package com.swe.EduMeter.model;

public class Report {
    private Integer id;
    private User issuer;
    private Review review;

    public Report() {}

    public Report(User issuer,
                  Review review)
    {
        this.issuer = issuer;
        this.review = review;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getIssuer() { return issuer; }
    public void setIssuer(User issuer) { this.issuer = issuer; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }

    public String toString()
    {
        return "Report { issuer:" + issuer.toString()
                + ", review:" + review.toString()
                + "}";
    }
}
