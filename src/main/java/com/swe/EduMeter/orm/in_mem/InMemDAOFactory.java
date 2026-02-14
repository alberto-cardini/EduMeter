package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.orm.UserDAO;

public class InMemDAOFactory implements DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new InMemUserDAO();
    }
    public SchoolDAO getSchoolDAO() {return new InMemSchoolDAO();}
}
