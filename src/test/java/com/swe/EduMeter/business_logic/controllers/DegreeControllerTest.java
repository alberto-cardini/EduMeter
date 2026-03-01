package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.DegreeDAO;
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
public class DegreeControllerTest {

    @Mock
    private DegreeDAO degreeDAO;

    @InjectMocks
    private DegreeController degreeController;

    @Test
    public void testGetDegree_Found() {
        int id = 42;
        Degree degree = new Degree(id, "Computer Science", Degree.Type.Bachelor, 0);
        when(degreeDAO.get(id)).thenReturn(Optional.of(degree));

        Degree gotDegree = degreeController.get(id);

        assertEquals(degree, gotDegree);
    }

    @Test
    public void testGetDegree_NotFound() {
        int id = 42;
        when(degreeDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> degreeController.get(id));

        assertEquals("Degree not found", exception.getMessage());
    }

    @Test
    public void testSearchDegree_All() {
        String query = null;
        Integer schoolId = null;
        List<Degree> degrees = List.of(
                new Degree(0, "Computer Science", Degree.Type.Master, 0),
                new Degree(1, "Mechanical Engineering", Degree.Type.Bachelor, 1)
        );
        when(degreeDAO.search(query, schoolId)).thenReturn(degrees);

        List<Degree> gotDegrees = degreeController.search(query, schoolId);

        assertEquals(degrees.size(), gotDegrees.size());
        assertEquals(degrees.get(0).getName(), gotDegrees.get(0).getName());
    }

    @Test
    public void testSearchDegree_Filtered() {
        String query = "Comp";
        Integer schoolId = 1;
        List<Degree> degrees = List.of(
                new Degree(0, "Computer Science", Degree.Type.Bachelor, 1)
        );
        when(degreeDAO.search(query, schoolId)).thenReturn(degrees);

        List<Degree> gotDegrees = degreeController.search(query, schoolId);

        assertEquals(1, gotDegrees.size());
    }

    @Test
    public void testCreateDegree() {
        Degree degree = new Degree(null, "Computer Science", Degree.Type.Master, 0);
        int expectedId = 42;
        when(degreeDAO.add(degree)).thenReturn(expectedId);

        ApiObjectCreated response = degreeController.create(degree);

        assertEquals(expectedId, response.id());
        verify(degreeDAO, times(1)).add(degree);
    }

    @Test
    public void testDeleteDegree_Found() {
        int id = 42;
        Degree degree = new Degree(id, "Computer Science", Degree.Type.Master, 0);
        when(degreeDAO.get(id)).thenReturn(Optional.of(degree));

        ApiOk response = degreeController.delete(id);

        assertEquals("Degree deleted", response.message());
        verify(degreeDAO, times(1)).delete(id);
    }

    @Test
    public void testDeleteDegree_NotFound() {
        int id = 42;
        when(degreeDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> degreeController.delete(id));

        assertEquals("Degree not found", exception.getMessage());
        verify(degreeDAO, never()).delete(anyInt());
    }

    @Test
    public void testUpdateDegree_Found() {
        int id = 42;
        Degree degree = new Degree(id, "Computer Science", Degree.Type.Master, 0);
        when(degreeDAO.get(id)).thenReturn(Optional.of(degree));

        ApiOk response = degreeController.update(degree);

        assertEquals("Degree updated", response.message());
        verify(degreeDAO, times(1)).update(degree);
    }

    @Test
    public void testUpdateDegree_NotFound() {
        int id = 42;
        Degree degree = new Degree(id, "Computer Science", Degree.Type.Master, 0);
        when(degreeDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> degreeController.update(degree));

        assertEquals("Degree not found", exception.getMessage());
        verify(degreeDAO, never()).update(any(Degree.class));
    }

    @Test
    public void testUpdateDegree_Invalid() {
        Degree degree = new Degree(null, "Computer Science", Degree.Type.Master, 0);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> degreeController.update(degree));

        assertEquals("Id must be set", exception.getMessage());
        verify(degreeDAO, never()).update(any(Degree.class));
    }
}