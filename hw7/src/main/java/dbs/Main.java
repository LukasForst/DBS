package dbs;

import dbs.db.providers.CharacterProvider;
import dbs.db.providers.FightProvider;
import dbs.db.providers.UserProvider;
import dbs.gui.MainScreen;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DBSApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        initGui(entityManagerFactory, entityManager);
    }

    private static void initGui(final EntityManagerFactory entityManagerFactory, final EntityManager entityManager) {
        UserProvider userProvider = new UserProvider(entityManager);
        CharacterProvider characterProvider = new CharacterProvider(entityManager);
        FightProvider fightProvider = new FightProvider(entityManager);

        MainScreen mainScreen = new MainScreen(characterProvider, fightProvider, userProvider);
        mainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                entityManager.close();
                entityManagerFactory.close();
                super.windowClosing(e);
            }
        });

        mainScreen.setVisible(true);
    }
}
