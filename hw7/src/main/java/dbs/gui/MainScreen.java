package dbs.gui;

import dbs.db.providers.CharacterProvider;
import dbs.db.providers.FightProvider;
import dbs.db.providers.UserProvider;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    private final CharacterProvider characterProvider;
    private final FightProvider fightProvider;
    private final UserProvider userProvider;

    public MainScreen(CharacterProvider characterProvider, FightProvider fightProvider, UserProvider userProvider) throws HeadlessException {
        this.characterProvider = characterProvider;
        this.fightProvider = fightProvider;
        this.userProvider = userProvider;
    }
}
