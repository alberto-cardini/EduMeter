package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.AdminDAO;
import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.orm.UserDAO;

public class InMemDAOFactory implements DAOFactory {

    private final UserDAO userDAO = new InMemUserDAO();
    private final SchoolDAO schoolDAO = new InMemSchoolDAO();
    private final AdminDAO adminDAO = new InMemAdminDAO();

    @Override
    public UserDAO getUserDAO() { return userDAO; }

    @Override
    public SchoolDAO getSchoolDAO() { return schoolDAO; }

    @Override
    public AdminDAO getAdminDAO() { return adminDAO; }
}
