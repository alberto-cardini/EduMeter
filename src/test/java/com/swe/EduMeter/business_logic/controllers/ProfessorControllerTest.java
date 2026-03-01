package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Professor;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.ProfDAO;
import com.swe.EduMeter.orm.TeachingDAO;
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
public class ProfessorControllerTest {

    @Mock
    private ProfDAO profDAO;

    @Mock
    private TeachingDAO teachingDAO;

    @InjectMocks
    private ProfessorController professorController;

    @Test
    public void testGetProfessor_Found() {
        int id = 42;
        // Assuming Professor has a basic constructor like (Integer id, String name)
        Professor prof = new Professor(id, "John", "Doe");
        when(profDAO.get(id)).thenReturn(Optional.of(prof));

        Professor gotProf = professorController.get(id);

        assertEquals(prof, gotProf);
    }

    @Test
    public void testGetProfessor_NotFound() {
        int id = 42;
        when(profDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> professorController.get(id));

        assertEquals("Professor not found", exception.getMessage());
    }

    @Test
    public void testSearchProfessor_All() {
        String query = null;
        Integer courseId = null;
        List<Professor> profs = List.of(
                new Professor(0, "Alan", "Turing"),
                new Professor(1, "Ada", "Lovelace")
        );
        when(profDAO.search(query, courseId)).thenReturn(profs);

        List<Professor> gotProfs = professorController.search(query, courseId);

        assertEquals(profs.size(), gotProfs.size());
        assertEquals(profs.get(0).getName(), gotProfs.get(0).getName()); // Assuming getName() exists
    }

    @Test
    public void testCreateProfessor() {
        Professor prof = new Professor(null, "John", "Doe");
        int expectedId = 42;
        when(profDAO.add(prof)).thenReturn(expectedId);

        ApiObjectCreated response = professorController.create(prof);

        assertEquals(expectedId, response.id());
        assertEquals("Professor created", response.message());
        verify(profDAO, times(1)).add(prof);
    }

    @Test
    public void testDeleteProfessor_Found() {
        int id = 42;
        Professor prof = new Professor(id, "John", "Doe");
        when(profDAO.get(id)).thenReturn(Optional.of(prof));

        ApiOk response = professorController.delete(id);

        assertEquals("Professor deleted", response.message());
        verify(profDAO, times(1)).delete(id);
    }

    @Test
    public void testDeleteProfessor_NotFound() {
        int id = 42;
        when(profDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> professorController.delete(id));

        assertEquals("Professor not found", exception.getMessage());
        verify(profDAO, never()).delete(anyInt());
    }

    @Test
    public void testUpdateProfessor_Found() {
        int id = 42;
        Professor prof = new Professor(id, "John", "Doe");
        when(profDAO.get(id)).thenReturn(Optional.of(prof));

        ApiOk response = professorController.update(prof);

        assertEquals("Professor updated", response.message());
        verify(profDAO, times(1)).update(prof);
    }

    @Test
    public void testUpdateProfessor_NotFound() {
        int id = 42;
        Professor prof = new Professor(id, "John", "Doe");
        when(profDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> professorController.update(prof));

        assertEquals("Professor not found", exception.getMessage());
        verify(profDAO, never()).update(any(Professor.class));
    }

    @Test
    public void testUpdateProfessor_Invalid() {
        Professor prof = new Professor(null, "John", "Doe");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> professorController.update(prof));

        assertEquals("Id must be set", exception.getMessage());
        verify(profDAO, never()).update(any(Professor.class));
    }

    @Test
    public void testListTeachings() {
        int profId = 42;
        List<Teaching> teachings = List.of(
                new Teaching(1, 101, profId),
                new Teaching(2, 102, profId)
        );
        when(teachingDAO.getByProf(profId)).thenReturn(teachings);

        List<Teaching> gotTeachings = professorController.listTeachings(profId);

        assertEquals(teachings.size(), gotTeachings.size());
        assertEquals(teachings.get(0).getCourseId(), gotTeachings.get(0).getCourseId()); // Assuming getCourseId() exists
    }
}