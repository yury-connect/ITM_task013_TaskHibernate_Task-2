package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.UtilJDBC;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JdbcRunner {

    private static final UserService SERVICE = new UserServiceImpl(new UserDaoJDBCImpl()); // для JDBC
    private static final Logger LOGGER = UtilJDBC.getLogger(JdbcRunner.class);

    public static void main(String[] args) throws SQLException {
        SERVICE.createUsersTable();
        System.out.println("\n\tВ базе данных 'ITM_task012_TaskJDBC_Task_1_db'. Создана таблица 'users'.");
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "В базе данных 'ITM_task012_TaskJDBC_Task_1_db'. Создана таблица 'users'.: finished");
        printAllUsersFromDB();

        User user1 = new User("Ivan", "Ivanov", (byte) 11);
        SERVICE.saveUser(user1.getName(), user1.getLastName(), user1.getAge());
        System.out.println("\n\n\tВ таблицу 'users' добавлен 1 новый пользователь:\n" + user1);
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "В таблицу 'users' добавлен 1 новый пользователь: finished");
        printAllUsersFromDB();

        User user2 = new User("Petr", "Petrov", (byte) 22);
        SERVICE.saveUser(user2.getName(), user2.getLastName(), user2.getAge());
        System.out.println("\n\n\tВ таблицу 'users' добавлен ещё 1 новый пользователь:\n" + user2);
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "В таблицу 'users' добавлен ещё 1 новый пользователь: finished");
        printAllUsersFromDB();

        User user3 = new User("Sidor", "Sidorov", (byte) 33);
        User user4 = new User("Vladimir", "Lenin", (byte) 44);
        User user5 = new User("Vasiliy", "Vasilev", (byte) 55);
        SERVICE.saveUser(user3.getName(), user3.getLastName(), user3.getAge());
        SERVICE.saveUser(user4.getName(), user4.getLastName(), user4.getAge());
        SERVICE.saveUser(user5.getName(), user5.getLastName(), user5.getAge());
        System.out.printf("\n\n\tВ таблицу 'users' добавлен ещё 3 новый пользовател:\n%s\n%s\n%s\n", user3, user4, user5);
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "В таблицу 'users' добавлен ещё 3 новый пользовател: finished");
        printAllUsersFromDB();

        long deleteUserId1 = 2;
        SERVICE.removeUserById(deleteUserId1);
        System.out.println("\n\n\tИз таблицы удален пользователь с id:\t" + deleteUserId1);
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "Из таблицы удален пользователь с id = " + deleteUserId1 + ": finished");
        printAllUsersFromDB();

        long deleteUserId2 = 5;
        SERVICE.removeUserById(deleteUserId2);
        System.out.println("\n\n\tИз таблицы удален пользователь с id:\t" + deleteUserId2);
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "Из таблицы удален пользователь с id = " + deleteUserId2 + ": finished");
        printAllUsersFromDB();

        SERVICE.cleanUsersTable();
        System.out.println("\n\n\tПроизведена очистка таблицы от содержимого:");
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "Произведена очистка таблицы от содержимого: finished");
        printAllUsersFromDB();

        SERVICE.dropUsersTable();
        System.out.println("\n\n\tПроизведено удаление самой таблицы.");
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "Произведено удаление самой таблицы. finished");

        UtilJDBC.closeConnection();
        LOGGER.log(Level.INFO, "main(String[] args): " +
                "Освобождены ресурсы и закончена работа приложения: finished");
    }


    private static void printAllUsersFromDB() {
        System.out.println("\n\tВывожу всех пользователей из таблицы 'users':");
        SERVICE.getAllUsers().stream().forEach(System.out::println);
        pause(1);
    }

    private static void pause(int seconds) {
        System.out.print("\nПауза на '" + seconds + "' секунд: ");
        try {
            for (int i = 0; i < seconds * 10; i++) {
                System.out.print("*");
                Thread.sleep(100);
            }
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
