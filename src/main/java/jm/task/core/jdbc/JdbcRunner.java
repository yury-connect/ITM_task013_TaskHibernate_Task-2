package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.UtilJDBC;

import java.sql.SQLException;
import java.util.logging.Logger;


public final class JdbcRunner extends AbstractRunner {

    private static final UserService SERVICE = new UserServiceImpl(new UserDaoJDBCImpl()); // для JDBC
    private static final Logger LOGGER = UtilJDBC.getLogger(JdbcRunner.class);

    public static void main(String[] args) throws SQLException {
        processing(SERVICE, LOGGER, 1);
    }
}
