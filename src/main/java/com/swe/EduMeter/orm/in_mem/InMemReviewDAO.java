package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.ReviewDAO;
import com.swe.EduMeter.orm.SchoolDAO;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemReviewDAO implements ReviewDAO {
    private final ConcurrentHashMap<Integer, Review> inMemStoragePub = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Review> inMemStorageDraft = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<User, Review> inMemStorageUpvote = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemReviewDAO() {
        add(new Review("PROVA1", 2, 0, "comment", LocalDate.now(), 8, 6, ReviewStatus.ACCEPTED));
        add(new Review("PROVA1", 3, 1, "comment", LocalDate.now(), 8, 6,ReviewStatus.ACCEPTED));
        add(new Review("PROVA1", 4, 2, "comment", LocalDate.now(), 8, 6, ReviewStatus.ACCEPTED));

        add(new Review("PROVA2", 1, 1, "comment", LocalDate.now(), 8, 6, ReviewStatus.ACCEPTED));
        add(new Review("PROVA2", 3, 1, "comment", LocalDate.now(), 8, 6, ReviewStatus.ACCEPTED));
        add(new Review("PROVA2", 2, 1, "comment", LocalDate.now(), 8, 6, ReviewStatus.ACCEPTED));

        add(new Review("PROVA3", 5, 1, "comment", LocalDate.now(), 8, 6, ReviewStatus.PENDING));
        add(new Review("PROVA3", 3, 1, "comment", LocalDate.now(), 8, 6, ReviewStatus.PENDING));
        add(new Review("PROVA3", 2, 1, "comment", LocalDate.now(), 8, 6, ReviewStatus.PENDING));
    }

    @Override
    public Optional<Review> get(int id, ReviewStatus status) {
        return (status == ReviewStatus.ACCEPTED) ? Optional.ofNullable(inMemStoragePub.get(id)) : Optional.ofNullable(inMemStorageDraft.get(id));
    }

    @Override
    public List<Review> list(ReviewStatus status) {
        return (status == ReviewStatus.ACCEPTED) ? new ArrayList<>(inMemStoragePub.values()) : new ArrayList<>(inMemStorageDraft.values());
    }

    @Override
    public List<Review> search(Integer schoolId, Integer degreeId, Integer courseId, Integer professorId) {
        CourseDAO courseDAO = new InMemDAOFactory().getCourseDAO();
        return inMemStoragePub.values()
                .stream()
                // 1. professor filter
                .filter(r -> professorId == null || Objects.equals(r.getProfessor(), professorId))

                // 2. course filter
                .filter(r -> courseId == null || Objects.equals(r.getCourse(), courseId))

                // 3. degree filter through course.
                .filter(r -> {
                    if (degreeId == null) return true;

                    Course c = courseDAO.get(r.getCourse())
                            .orElseThrow(() -> new RuntimeException("Invalid courseId in Review: " + r.getId()));

                    return Objects.equals(c.getDegreeId(), degreeId);
                })

                // 4. school filter through course->degree->school
                .filter(r -> {
                    if (schoolId == null) return true;

                    DegreeDAO degreeDAO = new InMemDAOFactory().getDegreeDAO();

                    Course c = courseDAO.get(r.getCourse())
                            .orElseThrow(() -> new RuntimeException("Invalid courseId in Review: " + r.getId()));

                    Degree d = degreeDAO.get(c.getDegreeId())
                            .orElseThrow(() -> new RuntimeException("Invalid degreeId in Course: " + c.getId()));

                    return Objects.equals(d.getSchoolId(), schoolId);
                })
                .collect(Collectors.toList());
    }

    @Override
    public int add(Review review) {
        review.setId(id);
        if (review.getStatus() == ReviewStatus.ACCEPTED) {
            inMemStoragePub.put(id++, review);
        } else {
            inMemStorageDraft.put(id++, review);
        }
        return id;
    }

    @Override
    public void updateStatus(int id, ReviewStatus status) {
    }

    @Override
    public void incrementVote(int id) {
    }

    @Override
    public void delete(int id) {
    }

    @Override
    public void update(Review review) {
    }

}
