package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.ReportDAO;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemPublishedReviewDAO implements PublishedReviewDAO {
    private final Map<Integer, PublishedReview> reviewStore;
    private final Map<Integer, Set<String>> upvoteStore;
    private final TeachingDAO teachingDAO;
    private final CourseDAO courseDAO;
    private final DegreeDAO degreeDAO;
    private final ReportDAO reportDAO;
    private int id = 0;

    public InMemPublishedReviewDAO(Map<Integer, PublishedReview> reviewStore,
                                   Map<Integer, Set<String>> upvoteStore,
                                   TeachingDAO teachingDAO, CourseDAO courseDAO,
                                   DegreeDAO degreeDAO, ReportDAO reportDAO) {
        this.reviewStore = reviewStore;
        this.upvoteStore = upvoteStore;
        this.teachingDAO = teachingDAO;
        this.courseDAO = courseDAO;
        this.degreeDAO = degreeDAO;
        this.reportDAO = reportDAO;
    }

    @Override
    public Optional<PublishedReview> get(int id, String userHash) {
        return Optional.ofNullable(reviewStore.get(id))
                        .map(r -> addComputedProperties(r, userHash));
    }

    @Override
    public List<PublishedReview> search(Integer schoolId, Integer degreeId,
                                        Integer courseId, Integer professorId,
                                        String userHash) {

        return reviewStore.values()
                .stream()
                .filter(r -> {
                    if (schoolId == null && degreeId == null &&
                        courseId == null && professorId == null) return true;

                    Teaching t = teachingDAO.get(r.getTeachingId())
                                            .orElseThrow(() -> new RuntimeException("Invalid teachingId"));

                    if (!t.getProfId().equals(professorId)) return false;
                    if (!t.getCourseId().equals(courseId)) return false;

                    if (degreeId != null) {
                        Course c = courseDAO.get(t.getCourseId())
                                            .orElseThrow(() -> new RuntimeException("Invalid courseId"));

                        if (!degreeId.equals(c.getDegreeId())) return false;

                        if (schoolId != null) {
                            Degree d = degreeDAO.get(c.getDegreeId())
                                                .orElseThrow(() -> new RuntimeException("Invalid degreeId"));

                            if (!schoolId.equals(d.getSchoolId())) return false;
                        }
                    }

                    return false;
                })
                .map(r -> addComputedProperties(r, userHash))
                .collect(Collectors.toList());
    }

    @Override
    public int add(PublishedReview review) {
        teachingDAO
            .get(review.getTeachingId())
            .orElseThrow(() -> new RuntimeException("Invalid teachingId"));

        review.setId(id);
        reviewStore.put(id, review);
        upvoteStore.put(id, ConcurrentHashMap.newKeySet());

        return id++;
    }

    @Override
    public void update(PublishedReview review) {
        reviewStore.replace(review.getId(), review);
    }

    @Override
    public void toggleUpvote(int id, String userHash) {
        Set<String> upvotes = upvoteStore.get(id);

        if (upvotes.contains(userHash)) {
            upvotes.remove(userHash);
        } else {
            upvotes.add(userHash);
        }
    }

    @Override
    public void delete(int id) {
        reviewStore.remove(id);
        upvoteStore.remove(id);

        for (Report r: reportDAO.getAll()) {
            if (r.getReviewId().equals(id)) {
                reportDAO.delete(r.getId());
            }
        }
    }

    private PublishedReview addComputedProperties(PublishedReview review, String userHash) {
        Set<String> upvotes = upvoteStore.get(id);
        review.setUpvotes(upvotes.size());
        if (userHash != null) {
            review.setIsUpvotedByUser(upvotes.contains(userHash));
        }

        return review;
    }
}
