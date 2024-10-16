package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.UtilHibernate;
import jm.task.core.jdbc.util.UtilJDBC;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOGGER = UtilJDBC.getLogger(UserDaoHibernateImpl.class);


    public UserDaoHibernateImpl() {
        // NOP
    }


    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id BIGSERIAL PRIMARY KEY, "
                + "user_name VARCHAR(50) NOT NULL, "
                + "user_lastName VARCHAR(70) NOT NULL, "
                + "user_age SMALLINT NOT NULL"
                + ")";
        executeTransaction(session -> session.createNativeQuery(createTableSQL).executeUpdate());
        LOGGER.log(Level.FINE, "Таблица 'users' (если она еще не существовала) успешно создана.");
    }


    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS users";
        executeTransaction(session -> session.createNativeQuery(dropTableSQL).executeUpdate());
        LOGGER.log(Level.FINE, "Таблица 'users' (если существовала) - то успешно удалена.");
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = User.builder().name(name).lastName(lastName).age(age).build();
        executeTransaction(session -> session.persist(user)); // `persist` используется вместо `save` для JPA совместимости / т.к. он соответствует JPA-стандарту и позволяет обойтись без возвращаемого значения.
        LOGGER.log(Level.FINE, "Пользователь '" + name + "' успешно сохранен в DB.");
    }


    @Override
    public void removeUserById(long id) {
        executeTransaction(session -> {
            int deletedCount = session.createQuery("DELETE FROM User WHERE id = :userId") // Переписываем с использованием HQL
                    .setParameter("userId", id)
                    .executeUpdate();
            if (deletedCount > 0) {
                LOGGER.log(Level.FINE, "Пользователь с ID " + id + " успешно удален.");
            } else {
                LOGGER.log(Level.WARNING, "Пользователь с ID " + id + " не найден.");
            }
        });
    }


    @Override
    public List<User> getAllUsers() {
        String getAllHQL = "FROM User";  // Пишем с использованием HQL
        List<User> userList = executeQuery(session -> session.createQuery(getAllHQL, User.class).list());
        LOGGER.log(Level.FINE, "Получено " + userList.size() + " пользователей.");
        return userList;
    }


    @Override
    public void cleanUsersTable() {
        String cleanTableHQL = "DELETE FROM users";  // Пишем с использованием HQL
        executeTransaction(session -> session.createNativeQuery(cleanTableHQL).executeUpdate());
        LOGGER.log(Level.FINE, "Таблица 'users' очищена.");
    }





    // ***** services *****
    private void executeTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = UtilHibernate.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, "Ошибка при выполнении транзакции: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private <T> T executeQuery(Function<Session, T> query) {
        T result = null;
        try (Session session = UtilHibernate.getSessionFactory().openSession()) {
            result = query.apply(session);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
