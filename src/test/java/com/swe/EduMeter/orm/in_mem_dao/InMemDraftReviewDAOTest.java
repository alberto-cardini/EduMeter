package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.DraftReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemDraftReviewDAOTest {

    private InMemDraftReviewDAO draftReviewDAO;
    private Map<Integer, DraftReview> store;
    private final LocalDate testDate = LocalDate.of(2026, 3, 2);

    @BeforeEach
    public void setup() {
        store = new HashMap<>();
        DraftReview initialDraft = new DraftReview(
                0, "user_hash_1", "Initial thoughts", testDate,
                4, 3, "UniFi", "Computer Engineering",
                "Algorithms", "Prof. Rossi"
        );
        store.put(0, initialDraft);

        draftReviewDAO = new InMemDraftReviewDAO(store);
    }

    @Test
    public void testUpdate_ExistingReview_Success() {
        String newComment = "Actually, it was harder";
        int newEnjoyment = 3;
        int newDifficulty = 5;
        String newCourse = "Advanced Algorithms";

        DraftReview updated = store.get(0);
        updated.setComment(newComment);
        updated.setEnjoyment(newEnjoyment);
        updated.setDifficulty(newDifficulty);
        updated.setRawCourse(newCourse);

        draftReviewDAO.update(updated);

        assertEquals(newComment, store.get(0).getComment());
        assertEquals(newEnjoyment, store.get(0).getEnjoyment());
        assertEquals(newDifficulty, store.get(0).getDifficulty());
        assertEquals(newCourse, store.get(0).getRawCourse());
    }

    @Test
    public void testUpdate_NonExistentReview_DoesNothing() {
        int prevSize = store.size();
        DraftReview ghostReview = new DraftReview(
                prevSize, "h", "c", testDate, 1, 1, "S", "D", "C", "P"
        );

        draftReviewDAO.update(ghostReview);

        assertFalse(store.containsKey(prevSize));
        assertEquals(prevSize, store.size());
    }

    @Test
    public void testAdd() {
        String comment = "New Draft";
        DraftReview newDraft = new DraftReview(
                null, "user_hash_2", comment, testDate,
                5, 2, "School X", "Degree Y", "Course Z", "Prof W"
        );

        int generatedId = draftReviewDAO.add(newDraft);

        assertEquals(store.size() - 1, generatedId);
        assertTrue(store.containsKey(generatedId));
        assertEquals(comment, store.get(generatedId).getComment());
    }

    @Test
    public void testDelete() {
        draftReviewDAO.delete(0);
        assertTrue(store.isEmpty());
    }

    @Test
    public void testGet_Found() {
        int validId = 0;

        Optional<DraftReview> result = draftReviewDAO.get(validId);
        assertTrue(result.isPresent());
        assertEquals("Prof. Rossi", result.get().getRawProfessor());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = store.size();

        Optional<DraftReview> result = draftReviewDAO.get(invalidId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGet_All() {
        List<DraftReview> reviews = draftReviewDAO.getAll();

        assertEquals(reviews.size(), store.size());
    }
}