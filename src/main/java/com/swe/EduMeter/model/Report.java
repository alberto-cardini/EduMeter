package com.swe.EduMeter.model;

public class Report {
    private Integer id;
    private String issuer_hash;
    private Integer review_id;

    public Report() {}

    public Report(String issuer_hash,
                  Integer review_id)
    {
        this.issuer_hash = issuer_hash;
        this.review_id = review_id;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIssuer() { return issuer_hash; }
    public void setIssuer(String issuer_hash) { this.issuer_hash = issuer_hash; }

    public Integer getReview() { return review_id; }
    public void setReview(Integer review_id) { this.review_id = review_id; }

    public String toString()
    {
        return "Report { issuer:" + issuer_hash
                + ", review:" + review_id
                + "}";
    }
}
