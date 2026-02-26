package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.AdminDAO;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.DraftReviewDAO;
import com.swe.EduMeter.orm.PinChallengeDAO;
import com.swe.EduMeter.orm.ProfDAO;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.ReportDAO;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.orm.TeachingDAO;
import com.swe.EduMeter.orm.UserDAO;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemDAOFactory implements DAOFactory {
    private static final Map<Integer, Admin> inMemAdminStore = new ConcurrentHashMap<>();
    private static final Map<String, User> inMemUserStore = new ConcurrentHashMap<>();
    private static final Map<Integer, School> inMemSchoolStore = new ConcurrentHashMap<>();
    private static final Map<Integer, Degree> inMemDegreeStore = new ConcurrentHashMap<>();
    private static final Map<Integer, Course> inMemCourseStore = new ConcurrentHashMap<>();
    private static final Map<Integer, Professor> inMemProfStore = new ConcurrentHashMap<>();
    private static final Map<Integer, Report> inMemReportStore = new ConcurrentHashMap<>();
    private static final Map<Integer, Teaching> inMemTeachingStore = new ConcurrentHashMap<>();
    private static final Map<Integer, PublishedReview> inMemPublishedReviewStore = new ConcurrentHashMap<>();
    private static final Map<Integer, Set<String>> inMemUpvoteStore = new ConcurrentHashMap<>();
    private static final Map<Integer, DraftReview> inMemDraftReviewStore = new ConcurrentHashMap<>();
    private static final Map<Integer, PinChallenge> inMemPinChallengeStore = new ConcurrentHashMap<>();

    private final AdminDAO adminDAO = new InMemAdminDAO(inMemAdminStore);
    private final UserDAO userDAO = new InMemUserDAO(inMemUserStore);
    private final SchoolDAO schoolDAO = new InMemSchoolDAO(inMemSchoolStore, getDegreeDAO());
    private final DegreeDAO degreeDAO = new InMemDegreeDAO(inMemDegreeStore, getCourseDAO(), getSchoolDAO());
    private final CourseDAO courseDAO = new InMemCourseDAO(inMemCourseStore, getTeachingDAO(), getDegreeDAO());
    private final ProfDAO profDAO = new InMemProfDAO(inMemProfStore, getTeachingDAO());
    private final ReportDAO reportDAO = new InMemReportDAO(inMemReportStore, getPublishedReviewDAO());
    private final TeachingDAO teachingDAO = new InMemTeachingDAO(inMemTeachingStore, getCourseDAO(),
                                                                 getProfDAO(), getPublishedReviewDAO());
    private final PublishedReviewDAO publishedReviewDAO = new InMemPublishedReviewDAO(inMemPublishedReviewStore, inMemUpvoteStore,
                                                                                      getTeachingDAO(), getCourseDAO(),
                                                                                      getDegreeDAO(), getReportDAO());
    private final DraftReviewDAO draftReviewDAO = new InMemDraftReviewDAO(inMemDraftReviewStore);
    private final PinChallengeDAO pinChallengeDAO = new InMemPinChallengeDAO(inMemPinChallengeStore);

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
    public PinChallengeDAO getPinDAO() { return pinChallengeDAO; }
}
