package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemPublishedReviewDAO implements PublishedReviewDAO {
    private final ConcurrentHashMap<Integer, PublishedReview> inMemPublishedReviewStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Set<String>> inMemUpvoteStorage= new ConcurrentHashMap<>();
    private int id = 0;
    @Override
    public Optional<PublishedReview> get(int id, String userHash) {
        return Optional.ofNullable(inMemPublishedReviewStorage.get(id))
                        .map(r -> addComputedProperties(r, userHash));
    }

    @Override
    public List<PublishedReview> search(Integer schoolId, Integer degreeId,
                                        Integer courseId, Integer professorId,
                                        String userHash) {

        return inMemPublishedReviewStorage.values()
                .stream()
                .filter(r -> {
                    if (schoolId == null && degreeId == null &&
                        courseId == null && professorId == null) return true;

                    TeachingDAO teachingDAO = new InMemDAOFactory().getTeachingDAO();
                    Teaching t = teachingDAO.get(r.getTeachingId())
                                            .orElseThrow(() -> new RuntimeException("Invalid teachingId"));

                    if (!t.getProfId().equals(professorId)) return false;
                    if (!t.getCourseId().equals(courseId)) return false;

                    if (degreeId != null) {
                        CourseDAO courseDAO = new InMemDAOFactory().getCourseDAO();
                        Course c = courseDAO.get(t.getCourseId())
                                            .orElseThrow(() -> new RuntimeException("Invalid courseId"));

                        if (!degreeId.equals(c.getDegreeId())) return false;

                        if (schoolId != null) {
                            DegreeDAO degreeDAO = new InMemDAOFactory().getDegreeDAO();
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
        review.setId(id);
        inMemPublishedReviewStorage.put(id, review);
        inMemUpvoteStorage.put(id, ConcurrentHashMap.newKeySet());

        return id++;
    }

    @Override
    public void update(PublishedReview review) {
        inMemPublishedReviewStorage.replace(review.getId(), review);
    }

    @Override
    public void toggleUpvote(int id, String userHash) {
        Set<String> upvotes = inMemUpvoteStorage.get(id);

        if (upvotes.contains(userHash)) {
            upvotes.remove(userHash);
        } else {
            upvotes.add(userHash);
        }
    }

    @Override
    public void delete(int id) {
        inMemPublishedReviewStorage.remove(id);
        inMemUpvoteStorage.remove(id);
    }

    private PublishedReview addComputedProperties(PublishedReview review, String userHash) {
        Set<String> upvotes = inMemUpvoteStorage.get(id);
        review.setUpvotes(upvotes.size());
        if (userHash != null) {
            review.setIsUpvotedByUser(upvotes.contains(userHash));
        }

        return review;
    }
}
