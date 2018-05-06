package dbs;

import dbs.db.model.Character;
import dbs.db.model.Fight;
import dbs.db.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("DBSApp");
        EntityManager em = emf.createEntityManager();
        Character b1 = em.find(Character.class, 1L);
        System.out.println(
                (b1 == null) ? "NULL" : "Id: " + b1.getId() + " | username: " + b1.getName()
        );

        TypedQuery<Fight> tq = em.createQuery(
                "SELECT f FROM Fight AS f",
                Fight.class
        );

        List<Fight> fightList = tq.getResultList();
        for (Fight g : fightList) {
            System.out.println(g.getId() + " - " + g.getPlace());
        }

        TypedQuery<User> tq1 = em.createQuery(
                "SELECT f FROM User AS f",
                User.class
        );

        List<User> userList = tq1.getResultList();
        for (User g : userList) {
            System.out.println(g.getId() + " - " + g.getUsername());
        }

    }
}
