package com.swe.EduMeter.orm;

public interface DAOFactory {
    UserDAO getUserDAO();
    AdminDAO getAdminDAO();
    ReviewDAO getReviewDAO();
    SchoolDAO getSchoolDAO();
    DegreeDAO getDegreeDAO();
    CourseDAO getCourseDAO();
    ProfDAO getProfDAO();
    ReportDAO getReportDAO();
    TeachingDAO getTeachingDAO();
}