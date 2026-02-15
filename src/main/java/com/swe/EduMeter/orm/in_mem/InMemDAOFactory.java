package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.*;

public class InMemDAOFactory implements DAOFactory {

    private final UserDAO userDAO = new InMemUserDAO();
    private final AdminDAO adminDAO = new InMemAdminDAO();
    private final SchoolDAO schoolDAO = new InMemSchoolDAO();
    private final DegreeDAO degreeDAO = new InMemDegreeDAO();
    private final CourseDAO courseDAO = new InMemCourseDAO();

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
