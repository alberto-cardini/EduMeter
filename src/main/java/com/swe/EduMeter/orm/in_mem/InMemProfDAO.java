package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Professor;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.ProfDAO;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemProfDAO implements ProfDAO {
    private final ConcurrentHashMap<Integer, Professor> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemProfDAO() {
        add(new Professor(null, "Alberto", "Cardini"));
        add(new Professor(null, "Lorenzo", "Bellina"));
        add(new Professor(null, "Carolina", "Cecchi"));
        add(new Professor(null, "Marco", "Bertini"));
        add(new Professor(null, "Fabio", "Corradi"));
        add(new Professor(null, "Alessandro", "Piva"));
    }

    @Override
    public int add(Professor prof) {
        prof.setId(id);
        inMemStorage.put(id++, prof);
        return prof.getId();
    }

    @Override
    public Optional<Professor> get(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public void update(Professor prof) {
        inMemStorage.replace(prof.getId(), prof);
    }

    @Override
    public void delete(int id) {
        inMemStorage.remove(id);

        TeachingDAO teachingDAO = new InMemDAOFactory().getTeachingDAO();

        for (Teaching t: teachingDAO.getByProf(id)) {
            teachingDAO.delete(t.getId());
        }
    }

    @Override
    public List<Professor> search(String pattern, Integer courseId) {
        final List<Integer> allowProfessors;

        if (courseId != null) {
            TeachingDAO teachingDAO = new InMemDAOFactory().getTeachingDAO();

            // Get distinct professors which teach the course
            allowProfessors = teachingDAO.getByCourse(courseId)
                                          .stream()
                                          .map(Teaching::getProfId)
                                          .distinct()
                                          .collect(Collectors.toList());
        } else {
            allowProfessors = null;
        }

        return inMemStorage.values()
                .stream()
                .filter(p -> allowProfessors == null || allowProfessors.contains(p.getId()))
                .filter(p -> {
                    if (pattern == null) return true;

                    String[] patterns = pattern.toLowerCase().split(" ");

                    // Pattern matches the name
                    if (p.getName().toLowerCase().contains(patterns[0])) {
                        // Pattern matches only the name or even the surname matches
                        return patterns.length <= 1 || p.getSurname().toLowerCase().contains(patterns[1]);
                    }

                    // Pattern matches the surname
                    if (p.getSurname().toLowerCase().contains(patterns[0])) {
                        // Pattern matches only the surname or even the name matches
                        return patterns.length <= 1 || p.getName().toLowerCase().contains(patterns[1]);
                    }

                    return false;
                })
                .collect(Collectors.toList());
    }
}
