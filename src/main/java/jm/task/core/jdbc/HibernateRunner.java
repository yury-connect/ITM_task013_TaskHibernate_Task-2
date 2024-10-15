package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.UtilJDBC;

import java.util.logging.Logger;


public final class HibernateRunner extends AbstractRunner {

    public static void main(String[] args) {
        UserService service = new UserServiceImpl(new UserDaoHibernateImpl()); // для Hibernate
        Logger logger = UtilJDBC.getLogger(HibernateRunner.class);
        int timeOutSeconds = 5; // в секундах, продолжительность паузы между шагами

        performDemonstration(service, logger, timeOutSeconds);
    }
}

