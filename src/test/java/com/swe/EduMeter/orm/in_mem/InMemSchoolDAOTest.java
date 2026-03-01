package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.SchoolDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemSchoolDAOTest {
    private SchoolDAO schoolDAO;
    private Map<Integer, School> schoolStore;
    @Mock
    private DegreeDAO degreeDAO;

    @BeforeEach
    public void setup() {
        schoolStore = new HashMap<>(Map.of(
                0, new School(0, "School of Engineering"),
                1, new School(1, "School of Law"),
                2, new School(2, "School of Medicine")
        ));
        schoolDAO = new InMemSchoolDAO(schoolStore, degreeDAO);
    }

    @Test
    public void TestGetSchool_Found() {
        int expectedId = 0;

        Optional<School> school = schoolDAO.get(expectedId);

        assertTrue(school.isPresent(), "School should be present");
        assertEquals(expectedId, school.get().getId());
    }

    @Test
    public void TestGetSchool_NotFound() {
        // Ids are 0 based, so size() + 1 is certainly
        // not present.
        // This applies because no element is deleted.
        int invalidId = schoolStore.size() + 1;

        Optional<School> school = schoolDAO.get(invalidId);

        assertTrue(school.isEmpty(), "School should not be present");
    }

    @Test
    public void TestSearchSchool_All() {
        String query = null;

        List<School> gotSchools = schoolDAO.search(query);

        assertEquals(schoolStore.size(), gotSchools.size());
    }

    @Test
    public void TestSearchSchool_Filter() {
        // School of Engineering
        //           ^  ^
        //           |  |
        //          10  13
        String firstSchoolName = schoolStore.get(0).getName();
        String query = firstSchoolName.substring(10, 13);

        List<School> gotSchools = schoolDAO.search(query);

        assertEquals(1, gotSchools.size());
        assertEquals(firstSchoolName, gotSchools.get(0).getName());
    }

    @Test
    public void TestSearchSchool_Empty() {
        String query = "This certainly doesn't match any school";

        List<School> gotSchools = schoolDAO.search(query);

        assertEquals(0, gotSchools.size());
    }

    @Test
    public void TestAddSchool() {
        String schoolName = "My new School TEST";
        School newSchool = new School(null, schoolName);

        // Progressive id is 0 based and no elements are deleted,
        // so we expect it to be equal to the number of inserted
        // elements.
        int expectedId = schoolStore.size();

        int gotId = schoolDAO.add(newSchool);
        Optional<School> gotSchool = schoolDAO.get(gotId);

        assertEquals(expectedId, gotId);
        assertTrue(gotSchool.isPresent(), "School should be inserted");
        assertEquals(gotId, gotSchool.get().getId());
        assertEquals(schoolName, gotSchool.get().getName());
    }

    @Test
    public void TestUpdateSchool_Found() {
        School school = schoolStore.get(0);

        String updatedName = "School of TEST UPDATE";
        school.setName(updatedName);

        schoolDAO.update(school);

        Optional<School> updatedSchool = schoolDAO.get(school.getId());

        assertTrue(updatedSchool.isPresent(), "School should be present");
        assertEquals(school.getId(), updatedSchool.get().getId());
        assertEquals(updatedName, updatedSchool.get().getName());
    }

    @Test
    public void TestUpdateSchool_NotFound() {
        // Ids are 0 based, so size() + 1 is certainly
        // not present.
        // This applies because no element is deleted.
        int invalidId = schoolStore.size() + 1;
        School school = new School(invalidId, "School with invalid ID");

        schoolDAO.update(school);

        Optional<School> gotSchool = schoolDAO.get(invalidId);

        assertTrue(gotSchool.isEmpty(), "School should not be present");
    }

    @Test
    public void TestDeleteSchool_Found() {
        School firstSchool = schoolStore.get(0);
        int prevSchoolCount = schoolStore.size();

        List<Degree> mockDegrees = List.of(
                new Degree(0, "Degree 1", Degree.Type.Master, firstSchool.getId()),
                new Degree(1, "Degree 2", Degree.Type.Master, firstSchool.getId()),
                new Degree(2, "Degree 3", Degree.Type.Master, firstSchool.getId())
        );
        when(degreeDAO.search(null, firstSchool.getId())).thenReturn(mockDegrees);

        schoolDAO.delete(firstSchool.getId());

        assertEquals(prevSchoolCount - 1, schoolStore.size());
        assertNull(schoolStore.get(0));
        verify(degreeDAO, times(1)).delete(0);
        verify(degreeDAO, times(1)).delete(1);
        verify(degreeDAO, times(1)).delete(2);
    }

    @Test
    public void TestDeleteSchool_NotFound() {
        // Ids are 0 based, so size() + 1 is certainly
        // not present.
        // This applies because no element has been deleted.
        int invalidId = schoolStore.size() + 1;
        int prevSchoolCount = schoolStore.size();

        schoolDAO.delete(invalidId);

        assertEquals(prevSchoolCount, schoolStore.size());
    }
}
