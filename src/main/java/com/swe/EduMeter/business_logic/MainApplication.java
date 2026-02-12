package com.swe.EduMeter.business_logic;

import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.in_mem.InMemDAOFactory;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class MainApplication extends ResourceConfig {
    public static final String IN_MEM_DB = "IN_MEM_DB";

    public MainApplication() {
        packages("com.swe.EduMeter.business_logic");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                DAOFactory backendFactory;
                if (System.getenv(IN_MEM_DB) != null) {
                    backendFactory = new InMemDAOFactory();
                }
                else {
                    throw new RuntimeException("Real DB is not yet implemented, set the "+ IN_MEM_DB + " env var");
                    //backendFactory = new PostgreDAOFactory();
                }

                bind(backendFactory.getUserDAO()).to(UserDAO.class);
            }
        });
    }
}
