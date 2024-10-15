package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.util.UtilJDBC;

import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AbstractRunner {

    protected static void processing(final UserService service, final Logger logger, int timeOutSeconds) {
        service.createUsersTable();
        System.out.println("\n\tВ базе данных 'ITM_task012_TaskJDBC_Task_1_db'. Создана таблица 'users'.");
        logger.log(Level.INFO, "main(String[] args): " +
                "В базе данных 'ITM_task012_TaskJDBC_Task_1_db'. Создана таблица 'users'.: finished");
        printAllUsersFromDB(service, timeOutSeconds);

        User user1 = new User("Ivan", "Ivanov", (byte) 11);
        service.saveUser(user1.getName(), user1.getLastName(), user1.getAge());
        System.out.println("\n\n\tВ таблицу 'users' добавлен 1 новый пользователь:\n" + user1);
        logger.log(Level.INFO, "main(String[] args): " +
                "В таблицу 'users' добавлен 1 новый пользователь: finished");
        printAllUsersFromDB(service, timeOutSeconds);

        User user2 = new User("Petr", "Petrov", (byte) 22);
        service.saveUser(user2.getName(), user2.getLastName(), user2.getAge());
        System.out.println("\n\n\tВ таблицу 'users' добавлен ещё 1 новый пользователь:\n" + user2);
        logger.log(Level.INFO, "main(String[] args): " +
                "В таблицу 'users' добавлен ещё 1 новый пользователь: finished");
        printAllUsersFromDB(service, timeOutSeconds);

        User user3 = new User("Sidor", "Sidorov", (byte) 33);
        User user4 = new User("Vladimir", "Lenin", (byte) 44);
        User user5 = new User("Vasiliy", "Vasilev", (byte) 55);
        service.saveUser(user3.getName(), user3.getLastName(), user3.getAge());
        service.saveUser(user4.getName(), user4.getLastName(), user4.getAge());
        service.saveUser(user5.getName(), user5.getLastName(), user5.getAge());
        System.out.printf("\n\n\tВ таблицу 'users' добавлен ещё 3 новый пользовател:\n%s\n%s\n%s\n", user3, user4, user5);
        logger.log(Level.INFO, "main(String[] args): " +
                "В таблицу 'users' добавлен ещё 3 новый пользовател: finished");
        printAllUsersFromDB(service, timeOutSeconds);

        long deleteUserId1 = 2;
        service.removeUserById(deleteUserId1);
        System.out.println("\n\n\tИз таблицы удален пользователь с id:\t" + deleteUserId1);
        logger.log(Level.INFO, "main(String[] args): " +
                "Из таблицы удален пользователь с id = " + deleteUserId1 + ": finished");
        printAllUsersFromDB(service, timeOutSeconds);

        long deleteUserId2 = 5;
        service.removeUserById(deleteUserId2);
        System.out.println("\n\n\tИз таблицы удален пользователь с id:\t" + deleteUserId2);
        logger.log(Level.INFO, "main(String[] args): " +
                "Из таблицы удален пользователь с id = " + deleteUserId2 + ": finished");
        printAllUsersFromDB(service, timeOutSeconds);

        service.cleanUsersTable();
        System.out.println("\n\n\tПроизведена очистка таблицы от содержимого:");
        logger.log(Level.INFO, "main(String[] args): " +
                "Произведена очистка таблицы от содержимого: finished");
        printAllUsersFromDB(service, timeOutSeconds);

        service.dropUsersTable();
        System.out.println("\n\n\tПроизведено удаление самой таблицы.");
        logger.log(Level.INFO, "main(String[] args): " +
                "Произведено удаление самой таблицы. finished");

        UtilJDBC.closeConnection();
        logger.log(Level.INFO, "main(String[] args): " +
                "Освобождены ресурсы и закончена работа приложения: finished");
    }


    private static void printAllUsersFromDB(UserService service, int pauseSeconds) {
        System.out.println("\n\tВывожу всех пользователей из таблицы 'users':");
        service.getAllUsers().stream().forEach(System.out::println);
        pause(pauseSeconds);
    }

    private static void pause(int pauseSeconds) {
        System.out.print("\nПауза на '" + pauseSeconds + "' секунд: ");
        try {
            for (int i = 0; i < pauseSeconds * 10; i++) {
                System.out.print("*");
                Thread.sleep(100);
            }
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
