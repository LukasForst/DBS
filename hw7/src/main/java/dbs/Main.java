package dbs;

import dbs.db.model.Character;
import dbs.db.model.Fight;

import javax.persistence.*;
import java.sql.Timestamp;
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

        System.out.println("\n\nFights:");
        TypedQuery<Fight> tq = em.createQuery(
                "SELECT f FROM Fight AS f",
                Fight.class
        );

        List<Fight> fightList = tq.getResultList();
        for (Fight g : fightList) {
            System.out.println("ID: " + g.getId() + ", Place: " + g.getPlace() + ", Char.No: " + g.getCharactersInFight().size());
        }

        System.out.println("\n\nCharacters:");
        TypedQuery<Character> charactersTq = em.createQuery(
                "SELECT f FROM Character AS f",
                Character.class
        );

        EntityTransaction et = em.getTransaction();
        List<Character> characters = charactersTq.getResultList();
        for (Character g : characters) {
            System.out.println("ID: " + g.getId() + ", Name: " + g.getName() + ", Fights.No.: " + g.getFights().size());
            if(g.getFights().size() == 0){
                et.begin();
                g.addFight(fightList.get(fightList.size() - 1));
                et.commit();
            }
        }

        et.begin();
        Fight toAdd = new Fight();
        toAdd.setDateTime(new Timestamp(2018,1,1, 10, 10, 10, 10));
        toAdd.setPlace("Another");
        em.persist(toAdd);
        et.commit();

        em.close();
        emf.close();
    }
}
