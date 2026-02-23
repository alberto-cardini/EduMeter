package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.orm.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreDAOFactory implements DAOFactory {

    private final UserDAO userDAO = new PostgreUserDAO();
    private final AdminDAO adminDAO = new PostgreAdminDAO();
    private final PublishedReviewDAO publishedReviewDAO = new PostgrePublishedReviewDAO();
    private final DraftReviewDAO draftReviewDAO = new PostgreDraftReviewDAO();
    private final ReportDAO reportDAO = new PostgreReportDAO();
    private final ProfDAO profDAO = new PostgreProfDAO();
    private final SchoolDAO schoolDAO = new PostgreSchoolDAO();
    private final DegreeDAO degreeDAO = new PostgreDegreeDAO();
    private final CourseDAO courseDAO = new PostgreCourseDAO();
    private final TeachingDAO teachingDAO = new PostgreTeachingDAO();
    private final PinChallengeDAO pinChallengeDAO = new PostgrePinChallengeDAO();

    public PostgreDAOFactory() {
        initDatabase();
    }

    private void initDatabase() {
        try (InputStream initFile = getClass().getClassLoader().getResourceAsStream("init.sql")) {
            if (initFile == null) {
                throw new RuntimeException("Could not find init.sql in the resources folder.");
            }

            String initQuery = new String(initFile.readAllBytes(), StandardCharsets.UTF_8);

            try (Connection conn = DatabaseManager.getInstance().getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.execute(initQuery);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading init.sql file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error executing initialization script", e);
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public AdminDAO getAdminDAO() {
        return adminDAO;
    }

    @Override
    public PublishedReviewDAO getPublishedReviewDAO() {
        return publishedReviewDAO;
    }

    @Override
    public DraftReviewDAO getDraftReviewDAO() {
        return draftReviewDAO;
    }

    @Override
    public ReportDAO getReportDAO() {
        return reportDAO;
    }

    @Override
    public ProfDAO getProfDAO() {
        return profDAO;
    }

    @Override
    public SchoolDAO getSchoolDAO() {
        return schoolDAO;
    }

    @Override
    public DegreeDAO getDegreeDAO() {
        return degreeDAO;
    }

    @Override
    public CourseDAO getCourseDAO() {
        return courseDAO;
    }

    @Override
    public TeachingDAO getTeachingDAO() {
        return teachingDAO;
    }

    @Override
    public PinChallengeDAO getPinDAO() { return pinChallengeDAO; }
}
