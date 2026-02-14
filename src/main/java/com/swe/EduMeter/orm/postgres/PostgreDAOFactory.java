package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.orm.AdminDAO;
import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.orm.UserDAO;

public class PostgreDAOFactory implements DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return null;
    }
    public SchoolDAO getSchoolDAO() {return null;}
    public AdminDAO getAdminDAO() {return null;}
}
