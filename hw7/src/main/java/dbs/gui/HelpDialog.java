package dbs.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea textArea;

    public HelpDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        showText();
    }

    private void showText() {
        String help = "You can easily user command line to use application.\n" +
                "\n" +
                "Note that you can't use character - in all strings that you want to save to database.\n" +
                "\n" +
                "For add action (to insert new record to database) use command add\n" +
                "For remove action (to remove record from database) use command remove\n" +
                "For update action (to update record in database) use command update\n" +
                "\n" +
                "Next word has to be name of entity which should be updated\n" +
                "Supported entities are:\n" +
                "\t- user\n" +
                "\t- character\n" +
                "\t- fight\n" +
                "\n" +
                "Then comes additional parameters, every parameter is named and used with -\n" +
                "For example specifying id is then as follows: -i 10 //this will select id = 10\n" +
                "\n" +
                "To add user you must specify these parameters:\n" +
                "\t- -n //name of the new character\n" +
                "\t- -p //password\n" +
                "Example:\n" +
                "add user -n \"JohnDoe\" -p \"MySecretPassword\"\n" +
                "\n" +
                "To add fight you must specify these parameters:\n" +
                "\t- -p //place \n" +
                "\t- -d //date in format DD/MM/YYYY\n" +
                "Example:\n" +
                "add fight -p \"Mordor\" -d \"12/1/1000\"\n" +
                "\n" +
                "To add character you must specify these parameters:\n" +
                "\t- -u //username of user to which should be character given -> when -i is used this is optional parameter\n" +
                "\t- -i //id of user -> when -u is used this is optional parameter\n" +
                "\t- -n //character name for the new character\n" +
                "Example:\n" +
                "add character -u \"JohnDoe\" -n \"JohnDoesCharacter\"\n" +
                "add character -i 2 -n \"JohnDoesCharacter\"\n" +
                "\n" +
                "To remove record from db use command:\n" +
                "\tremove <entity name> -i <id>\n" +
                "Example: \n" +
                "remove fight -i 10\n" +
                "remove user -u \"JohnDoe\" //optionally you can use -u as username in user entity\n" +
                "\n" +
                "To to add character to fight use:\n" +
                "update fight -i <fight id> -ai \"<character id>, <character id>, ...\" //to add with id selector\n" +
                "update fight -i <fight id> -au \"<character names>, <character names>, ...\" //to add with username selector\n" +
                "update fight -i <fight id> -ri \"<character id>, <character id>, ...\" //to remove ids\n" +
                "update fight -i <fight id> -ru \"<character names>, <character names>, ...\" //to remove using character names\n" +
                "\n" +
                "Example:\n" +
                "update fight -i 2 -ai \"1, 2, 3, 4\" //this will add to fight with id 2 characters with id 1, 2, 3, 4\n" +
                "update fight -i 2 -ru \"Hella, JohnDoe, WhatEver\" //this will remove from fight with id 2 characters with names \"Hella\", \"JohnDoe\" and \"WhatEver\"\n" +
                "\n" +
                "To change username:\n" +
                "update user -i <user id> -nn \"<new username>\" //change username\n" +
                "update user -i <user id> -np \"<new password>\" //change password";
        textArea.append(help);
    }
}
