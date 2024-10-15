package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.UtilJDBC;

import java.util.logging.Logger;


public final class JdbcRunner extends AbstractRunner {

    public static void main(String[] args) {
        UserService service = new UserServiceImpl(new UserDaoJDBCImpl()); // для JDBC
        Logger logger = UtilJDBC.getLogger(JdbcRunner.class);
        int timeOutSeconds = 1; // в секундах, продолжительность паузы между шагами

        processing(service, logger, timeOutSeconds);
    }
}
