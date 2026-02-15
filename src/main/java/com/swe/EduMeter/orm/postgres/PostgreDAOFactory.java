package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.orm.*;

public class PostgreDAOFactory implements DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return null;
    }
    public AdminDAO getAdminDAO() {return null;}
    public SchoolDAO getSchoolDAO() {return null;}
    public DegreeDAO getDegreeDAO() {return null;}
    public CourseDAO getCourseDAO() {return null;}
}
