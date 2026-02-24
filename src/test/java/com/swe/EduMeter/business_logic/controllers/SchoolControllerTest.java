package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.SchoolDAO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchoolControllerTest {
    @Mock
    private SchoolDAO schoolDAO;

    @InjectMocks
    private SchoolController schoolController;

    @Test
    public void testGetSchool_Found() {
        int id = 42;
        School school = new School(id, "School of Engineering");
        when(schoolDAO.get(id)).thenReturn(Optional.of(school));

        School gotSchool = schoolController.get(id);

        assertEquals(school, gotSchool);
    }

    @Test
    public void testGetSchool_NotFound() {
        int id = 42;
        when(schoolDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                                                  () -> schoolController.get(id));

        assertEquals("School not found", exception.getMessage());
    }

    @Test
    public void testSearchSchool_All() {
        String query = null;
        List<School> schools = List.of(
            new School(0, "School of Engineering"),
            new School(1, "School of Law"),
            new School(2, "School of Science")
        );
        when(schoolDAO.search(query)).thenReturn(schools);

        List<School> gotSchools = schoolController.search(query);

        assertEquals(schools.size(), gotSchools.size());
        assertEquals(schools.get(0).getName(), gotSchools.get(0).getName());
    }

    @Test
    public void testSearchSchool_Filtered() {
        String query = "Eng";
        List<School> schools = List.of(
                new School(0, "School of Engineering")
        );
        when(schoolDAO.search(query)).thenReturn(schools);

        List<School> gotSchools = schoolController.search(query);

        assertEquals(1, gotSchools.size());
    }

    @Test
    public void testSearchSchool_Empty() {
        String query = "INVALID";
        List<School> schools = List.of();
        when(schoolDAO.search(query)).thenReturn(schools);

        List<School> gotSchools = schoolController.search(query);

        assertEquals(0, gotSchools.size());
    }

    @Test
    public void testCreateSchool() {
        School school = new School(null, "School of Engineering");
        int expectedId = 42;
        when(schoolDAO.add(school)).thenReturn(expectedId);

        ApiObjectCreated response = schoolController.create(school);

        assertEquals(expectedId, response.id());
        assertEquals("School created", response.message());
        verify(schoolDAO, times(1)).add(school);
    }

    @Test
    public void testDeleteSchool_Found() {
        int id = 42;
        School school = new School(id, "School of Engineering");
        when(schoolDAO.get(id)).thenReturn(Optional.of(school));

        ApiOk response = schoolController.delete(id);

        assertEquals("School deleted", response.message());
        verify(schoolDAO, times(1)).delete(id);
    }

    @Test
    public void testDeleteSchool_NotFound() {
        int id = 42;
        when(schoolDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> schoolController.delete(id));

        assertEquals("School not found", exception.getMessage());
    }

    @Test
    public void testUpdateSchool_Found() {
        int id = 42;
        School school = new School(id, "School of Engineering");
        when(schoolDAO.get(id)).thenReturn(Optional.of(school));

        ApiOk response = schoolController.update(school);

        assertEquals("School updated", response.message());
        verify(schoolDAO, times(1)).update(school);
    }

    @Test
    public void testUpdateSchool_NotFound() {
        int id = 42;
        School school = new School(id, "School of Engineering");
        when(schoolDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> schoolController.update(school));

        assertEquals("School not found", exception.getMessage());
    }

    @Test
    public void testUpdateSchool_Invalid() {
        School school = new School(null, "School of Engineering");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> schoolController.update(school));

        assertEquals("Id must be set", exception.getMessage());
    }
}
