package com.swe.EduMeter.orm.dao;

public interface DAOFactory {
    UserDAO getUserDAO();
    AdminDAO getAdminDAO();
    SchoolDAO getSchoolDAO();
    DegreeDAO getDegreeDAO();
    CourseDAO getCourseDAO();
    ProfDAO getProfDAO();
    ReportDAO getReportDAO();
    TeachingDAO getTeachingDAO();
    PublishedReviewDAO getPublishedReviewDAO();
    DraftReviewDAO getDraftReviewDAO();
    PinChallengeDAO getPinDAO();
}