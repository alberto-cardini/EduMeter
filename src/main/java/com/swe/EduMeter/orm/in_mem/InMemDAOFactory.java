package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.*;

public class InMemDAOFactory implements DAOFactory {

    private static final UserDAO userDAO = new InMemUserDAO();
    private static final AdminDAO adminDAO = new InMemAdminDAO();
    private static final ReviewDAO reviewDAO = new InMemReviewDAO();
    private static final SchoolDAO schoolDAO = new InMemSchoolDAO();
    private static final DegreeDAO degreeDAO = new InMemDegreeDAO();
    private static final CourseDAO courseDAO = new InMemCourseDAO();
    private static final ProfDAO profDAO = new InMemProfDAO();
    private static final ReportDAO reportDAO = new InMemReportDAO();

    @Override
    public UserDAO getUserDAO() { return userDAO; }

    @Override
    public AdminDAO getAdminDAO() { return adminDAO; }

    @Override
    public ReviewDAO getReviewDAO() { return reviewDAO; }

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

}
