package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.UtilJDBC;

import java.sql.SQLException;
import java.util.logging.Logger;


public final class HibernateRunner extends AbstractRunner {

    private static final UserService SERVICE = new UserServiceImpl(new UserDaoHibernateImpl()); // для Hibernate
    private static final Logger LOGGER = UtilJDBC.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {
        processing(SERVICE, LOGGER, 1);
    }
}


