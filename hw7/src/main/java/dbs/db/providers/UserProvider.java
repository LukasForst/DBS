package dbs.db.providers;

import dbs.db.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProvider extends AbstractProvider {
    private Logger logger = Logger.getLogger(getClass().getName());

    public UserProvider(EntityManager entityManager) {
        super(entityManager);
    }

    public Collection<User> getAll() {
        TypedQuery<User> charactersTq = entityManager.createQuery(
                "SELECT u FROM User AS u",
                User.class
        );
        return charactersTq.getResultList();
    }

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

    public User get(long userId) {
        return entityManager.find(User.class, userId);
    }

    public boolean delete(long userId) {
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
