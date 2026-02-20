package com.swe.EduMeter.business_logic;

import com.swe.EduMeter.orm.*;
import com.swe.EduMeter.orm.in_mem.InMemDAOFactory;
import com.swe.EduMeter.orm.postgres.PostgreDAOFactory;
import jakarta.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class MainApplication extends ResourceConfig {
    public static final String IN_MEM_DB = "IN_MEM_DB";

    public MainApplication() {
        // where to find controllers
        packages("com.swe.EduMeter.business_logic");

        // register types for injection
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                DAOFactory backendFactory;
                if (System.getenv(IN_MEM_DB) != null) {
                    backendFactory = new InMemDAOFactory();
                }
                else {
                    backendFactory = new PostgreDAOFactory();
                }

                bind(backendFactory.getUserDAO()).to(UserDAO.class);
                bind(backendFactory.getAdminDAO()).to(AdminDAO.class);
                bind(backendFactory.getProfDAO()).to(ProfDAO.class);
                bind(backendFactory.getReportDAO()).to(ReportDAO.class);
                bind(backendFactory.getReviewDAO()).to(ReviewDAO.class);
                bind(backendFactory.getSchoolDAO()).to(SchoolDAO.class);
                bind(backendFactory.getDegreeDAO()).to(DegreeDAO.class);
                bind(backendFactory.getCourseDAO()).to(CourseDAO.class);
            }
        });
    }
}
