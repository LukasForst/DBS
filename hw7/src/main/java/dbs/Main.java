package dbs;

import dbs.db.CharacterProvider;
import dbs.db.FightProvider;
import dbs.db.UserProvider;
import dbs.db.model.Character;
import dbs.db.model.Fight;
import dbs.db.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("DBSApp");
        EntityManager em = emf.createEntityManager();

        UserProvider p = new UserProvider(em);
        Collection<User> users = p.getAll();
        System.out.println("Users:");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + ", Name: " + user.getUsername());
        }

        CharacterProvider c = new CharacterProvider(em);
        Collection<Character> characters = c.getAll();
        System.out.println("\n\nCharacters:");
        for (Character character : characters) {
            System.out.println("ID: " + character.getId() + ", Name: " + character.getName());
        }

        FightProvider f = new FightProvider(em);
        Collection<Fight> fights = f.getAll();
        System.out.println("\n\nFights:");
        for (Fight fight : fights) {
            System.out.println("ID: " + fight.getId() + ", Place: " + fight.getPlace() + ", Size: " + fight.getCharactersInFight().size());
        }

        em.close();
        emf.close();
    }
}
