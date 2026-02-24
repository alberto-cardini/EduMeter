package com.swe.EduMeter.model;

import java.time.LocalDate;

public class PublishedReview extends Review {
    private Integer teachingId;
    private boolean upvotedByUser;

    private int upvotes = 0;

    public PublishedReview() {}

    public PublishedReview(Integer id, String creatorHash,
                           String comment, LocalDate date,
                           int enjoyment, int difficulty,
                           Integer teachingId, int upvotes) {
        super(id, creatorHash, comment, date, enjoyment, difficulty);
        this.teachingId = teachingId;
        this.upvotes = upvotes;
    }

    public Integer getTeachingId() {
        return teachingId;
    }

    public void setTeachingId(Integer teachingId) {
        this.teachingId = teachingId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public boolean isUpvotedByUser() {
        return upvotedByUser;
    }

    public void setIsUpvotedByUser(boolean upvotedByUser) {
        this.upvotedByUser = upvotedByUser;
    }

    @Override
    public String toString() {
        return "PublishedReview{" +
                "teachingId=" + teachingId +
                ", upvotes=" + upvotes +
                ", upvotedByUser=" + upvotedByUser +
                "} " + super.toString();
    }
}
