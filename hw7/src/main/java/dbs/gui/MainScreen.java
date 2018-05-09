package dbs.gui;

import dbs.db.CommandParser;
import dbs.db.Pair;
import dbs.db.model.Character;
import dbs.db.model.Fight;
import dbs.db.model.User;
import dbs.db.providers.CharacterProvider;
import dbs.db.providers.FightProvider;
import dbs.db.providers.UserProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Main screen for GUI using swing.
 */
public class MainScreen extends JFrame {
    private JPanel panel1;
    private JTextArea commandResponse;
    private JTextField commandField;
    private JTabbedPane tabbedPane;
    private JTable userTable;
    private JTable characterTable;
    private JTable fightTable;

    private final CharacterProvider characterProvider;
    private final FightProvider fightProvider;
    private final UserProvider userProvider;
    private final CommandParser commandParser;

    public MainScreen(CharacterProvider characterProvider, FightProvider fightProvider, UserProvider userProvider, CommandParser commandParser) throws HeadlessException {
        super("MainScreen");
        super.setContentPane(panel1);
        super.pack();

        this.characterProvider = characterProvider;
        this.fightProvider = fightProvider;
        this.userProvider = userProvider;
        this.commandParser = commandParser;

        setUpListeners();
        reloadAll();
    }

    //set up listeners for actions in GUI
    private void setUpListeners() {
        commandField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) { //enter
                    commandResponse.append(commandField.getText() + "\n");
                    evaluateCommand(commandField.getText());
                    commandField.setText("");
                }
                super.keyPressed(e);
            }
        });
    }

    //evaluates whenever enter was hit
    private void evaluateCommand(String commands) {
        if (commands.equals("reload")) {
            reloadAll();
            return;
        } else if (commands.equals("?")) {
            showHelp();
        }

        Pair<Boolean, String> result = commandParser.evaluateCommands(commands);
        if (!result.getFirst()) {
            commandResponse.append("\n!!!!!!---------!!!!!!\n" + result.getSecond() + "\n!!!!!!---------!!!!!!\n");
        } else {
            reloadAll();
        }
    }

    //shows simple help for this app
    private void showHelp() {

    }

    //query database and display most recent data that are stored here
    private void reloadAll() {
        Collection<User> users = userProvider.getAll();
        Collection<Fight> fights = fightProvider.getAll();
        Collection<Character> characters = characterProvider.getAll();

        reloadFightTable(fights);
        reloadUserTable(users);
        reloadCharacterTable(characters, users);
    }

    //reload user table with givene data
    private void reloadUserTable(Collection<User> users) {
        userTable.removeAll();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("UserName");
        model.addColumn("Password");
        for (User user : users) {
            model.addRow(new Object[]{user.getId(), user.getUsername(), user.getPassword()});
        }
        userTable.setModel(model);
    }

    //reload character table with given data
    private void reloadCharacterTable(Collection<Character> characters, Collection<User> users) {
        characterTable.removeAll();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("[UserId : Username]");
        model.addColumn("Name");
        model.addColumn("Fights");

        Map<Long, String> idUsernameDict = new HashMap<>();
        users.forEach(x -> idUsernameDict.put(x.getId(), x.getUsername()));

        for (Character character : characters) {
            StringBuilder sb = new StringBuilder();
            character.getFights().forEach(x -> sb.append(x.getPlace()).append(", "));
            if (sb.length() > 0) {
                sb.delete(sb.length() - 2, sb.length());
            }

            model.addRow(new Object[]{
                    character.getId(),
                    "[" + character.getUserId() + " : " + idUsernameDict.get(character.getUserId()) + "]", character.getName(),
                    sb.toString()});
        }
        characterTable.setModel(model);
    }

    //reload fight table with given data
    private void reloadFightTable(Collection<Fight> fights) {
        fightTable.removeAll();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Place");
        model.addColumn("Date");
        model.addColumn("Characters");
        for (Fight fight : fights) {
            StringBuilder sb = new StringBuilder();
            fight.getCharactersInFight().forEach(x -> sb.append(x.getName()).append(", "));
            if (sb.length() > 0) {
                sb.delete(sb.length() - 2, sb.length());
            }

            model.addRow(new Object[]{
                    fight.getId(),
                    fight.getPlace(),
                    dateToString(new Date(fight.getDateTime().getTime())),
                    sb.toString()});
        }
        fightTable.setModel(model);
    }

    //change date to string
    private String dateToString(Date date) {
        return "" + date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
    }

}
