package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.*;

public class InMemDAOFactory implements DAOFactory {

    private static final UserDAO userDAO = new InMemUserDAO();
    private static final AdminDAO adminDAO = new InMemAdminDAO();
    private static final SchoolDAO schoolDAO = new InMemSchoolDAO();
    private static final DegreeDAO degreeDAO = new InMemDegreeDAO();
    private static final CourseDAO courseDAO = new InMemCourseDAO();

    @Override
    public UserDAO getUserDAO() { return userDAO; }

    @Override
    public AdminDAO getAdminDAO() { return adminDAO; }

    @Override
    public SchoolDAO getSchoolDAO() { return schoolDAO; }

    @Override
    public DegreeDAO getDegreeDAO() { return degreeDAO; }

    @Override
    public CourseDAO getCourseDAO() { return courseDAO; }

}
