package dbs.gui;

import dbs.db.providers.CharacterProvider;
import dbs.db.providers.FightProvider;
import dbs.db.providers.UserProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainScreen extends JFrame {
    private JPanel panel1;
    private JTextArea commandResponse;
    private JTextField commandField;
    private JTabbedPane tabbedPane;

    private final CharacterProvider characterProvider;
    private final FightProvider fightProvider;
    private final UserProvider userProvider;

    public MainScreen() {
        this.characterProvider = null;
        this.fightProvider = null;
        this.userProvider = null;
    }

    public MainScreen(CharacterProvider characterProvider, FightProvider fightProvider, UserProvider userProvider) throws HeadlessException {
        super("MainScreen");
        super.setContentPane(panel1);
        super.pack();
        this.characterProvider = characterProvider;
        this.fightProvider = fightProvider;
        this.userProvider = userProvider;

        setUpListeners();
    }

    private void setUpListeners() {
        commandField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //enter
                if (e.getKeyCode() == 10) {
                    commandResponse.append(commandField.getText() + "\n");
                    commandField.setText("");
                }
                super.keyPressed(e);
            }
        });
    }
}
