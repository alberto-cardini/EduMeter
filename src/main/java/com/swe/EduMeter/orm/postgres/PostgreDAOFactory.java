package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.orm.*;

public class PostgreDAOFactory implements DAOFactory {

    private final UserDAO userDAO = new PostgreUserDAO();
    private final AdminDAO adminDAO = new PostgreAdminDAO();
    /*private final SchoolDAO schoolDAO = new PostgreSchoolDAO();
    private final DegreeDAO degreeDAO = new PostgreDegreeDAO();
    private final CourseDAO courseDAO = new PostgreCourseDAO();
*/
    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }
    public AdminDAO getAdminDAO() {return adminDAO;}
    /*
    public SchoolDAO getSchoolDAO() {return schoolDAO;}
    public DegreeDAO getDegreeDAO() {return degreeDAO;}
    public CourseDAO getCourseDAO() {return courseDAO;}*/
}
