package com.swe.EduMeter.business_logic;

import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.in_mem.InMemDAOFactory;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class MainApplication extends ResourceConfig {
    public MainApplication() {
        packages("com.swe.EduMeter.business_logic");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                DAOFactory backendFactory = new InMemDAOFactory();

                bind(backendFactory.getUserDAO()).to(UserDAO.class);
            }
        });
    }
}
