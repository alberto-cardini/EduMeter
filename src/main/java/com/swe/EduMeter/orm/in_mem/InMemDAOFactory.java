package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.*;

public class InMemDAOFactory implements DAOFactory {

    private static final UserDAO userDAO = new InMemUserDAO();
    private static final AdminDAO adminDAO = new InMemAdminDAO();
    private static final SchoolDAO schoolDAO = new InMemSchoolDAO();
    private static final DegreeDAO degreeDAO = new InMemDegreeDAO();
    private static final CourseDAO courseDAO = new InMemCourseDAO();
    private static final ProfDAO profDAO = new InMemProfDAO();
    private static final ReportDAO reportDAO = new InMemReportDAO();
    private static final TeachingDAO teachingDAO = new InMemTeachingDAO();
    private static final PublishedReviewDAO publishedReviewDAO = new InMemPublishedReviewDAO();
    private static final DraftReviewDAO draftReviewDAO = new InMemDraftReviewDAO();
    private static final PinChallengeDAO PIN_CHALLENGE_DAO = new InMemPinChallengeDAO();

    @Override
    public UserDAO getUserDAO() { return userDAO; }

    @Override
    public AdminDAO getAdminDAO() { return adminDAO; }

    @Override
    public ReportDAO getReportDAO() { return reportDAO; }

    @Override
    public ProfDAO getProfDAO() { return profDAO; }

    @Override
    public SchoolDAO getSchoolDAO() { return schoolDAO; }

    @Override
    public DegreeDAO getDegreeDAO() { return degreeDAO; }

    @Override
    public CourseDAO getCourseDAO() { return courseDAO; }

    @Override
    public TeachingDAO getTeachingDAO() { return teachingDAO; }

    @Override
    public PublishedReviewDAO getPublishedReviewDAO() { return publishedReviewDAO; }

    @Override
    public DraftReviewDAO getDraftReviewDAO() { return draftReviewDAO; }

    @Override
    public PinChallengeDAO getPinDAO() { return PIN_CHALLENGE_DAO; }
}
