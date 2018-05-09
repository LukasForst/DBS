package dbs.db.providers;

import dbs.db.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserProvider provides access and view to the User entity.
 * <p>
 * Note that all functions will cause immediate query to the database.
 */
public class UserProvider extends AbstractProvider {
    private Logger logger = Logger.getLogger(getClass().getName());

    public UserProvider(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Gets all records from database.
     */
    public Collection<User> getAll() {
        TypedQuery<User> charactersTq = entityManager.createQuery(
                "SELECT u FROM User AS u",
                User.class
        );
        return charactersTq.getResultList();
    }

    /**
     * Adds new user to db.
     * */
    public User addNew(String username, String password) {
        User add = new User();
        add.setUsername(username);
        add.setPassword(password);

        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.persist(add);
        et.commit();

        return add;
    }

    /**
     * Returns user with given id or null when this user does not exists.
     */
    public User get(long userId) {
        return entityManager.find(User.class, userId);
    }

    /**
     * Returns user with given username or null when this user does not exists.
     */
    public User get(String username){
        TypedQuery<User> charactersTq = entityManager.createQuery(
                "SELECT u FROM User AS u WHERE u.username = \"" + username + "\"",
                User.class
        );
        List<User> users = charactersTq.getResultList();
        return users.size() == 0 ? null : users.get(0);
    }

    /**
     * Removes user with given id from database, returns true if user was removed, false otherwise.
     * */
    public boolean remove(long userId) {
        User user = get(userId);
        if (user == null) {
            logger.log(Level.SEVERE, "User with id: " + userId + " could not be found.");
            return false;
        }
        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.remove(user);
        et.commit();
        return true;
    }

    /**
     * Changes username for particular user.
     * */
    public User changeUsername(long userId, String newUserName) {
        EntityTransaction et = entityManager.getTransaction();
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            logger.log(Level.SEVERE, "User with id: " + userId + " could not be found.");
            return null;
        }
        et.begin();
        user.setUsername(newUserName);
        et.commit();
        return user;
    }

    /**
     * Changes password for particular user.
     * */
    public User changePassword(long userId, String newPassword) {
        User user = get(userId);
        if (user == null) {
            logger.log(Level.SEVERE, "User with id: " + userId + " could not be found.");
            return null;
        }
        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        user.setPassword(newPassword);
        et.commit();
        return user;
    }
}
