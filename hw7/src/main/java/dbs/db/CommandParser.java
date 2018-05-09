package dbs.db;

import dbs.db.model.Character;
import dbs.db.model.Fight;
import dbs.db.model.User;
import dbs.db.providers.CharacterProvider;
import dbs.db.providers.FightProvider;
import dbs.db.providers.UserProvider;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides possibility to interpret given commands from GUI.
 * <p>
 * This is just pure java, no db handling there. For interpreting commands that uses database are there providers which handle database queries.
 */
@SuppressWarnings("IfCanBeSwitch")
public class CommandParser {
    private Logger logger = Logger.getLogger(getClass().getName());

    private final CharacterProvider characterProvider;
    private final FightProvider fightProvider;
    private final UserProvider userProvider;

    public CommandParser(CharacterProvider characterProvider, FightProvider fightProvider, UserProvider userProvider) {
        this.characterProvider = characterProvider;
        this.fightProvider = fightProvider;
        this.userProvider = userProvider;
    }

    /**
     * Evaluates given commands and returns pair of boolean and message.
     * <p>
     * Boolean says whether was evaluating ok.
     */
    public Pair<Boolean, String> evaluateCommands(String commands) {
        return evalSafe(commands);
    }

    //wrapper function
    private Pair<Boolean, String> fail(String reason) {
        return new Pair<>(false, reason);
    }

    //wrapper function
    private Pair<Boolean, String> success() {
        return new Pair<>(true, "Success.");
    }

    private Pair<Boolean, String> evalSafe(String commands) {
        //try to perform query
        try {
            String[] data = commands.split(" ");
            if (data.length < 2) return fail("Wrong arguments!");
            String command = data[0].toLowerCase().trim();
            String entity = data[1].toLowerCase().trim();

            switch (command) {
                case "add":
                    return evalAdd(entity, data);
                case "update":
                    return evalUpdate(entity, data);
                case "remove":
                    return evalRemove(entity, data);
                default:
                    return fail("Unrecognized command!");
            }
        } catch (Exception e) { //catch exception from providers
            logger.log(Level.WARNING, "Query failed.");
            logger.log(Level.WARNING, e.getMessage());
            return new Pair<>(false, e.getMessage());
        }
    }

    private Pair<Boolean, String> evalAdd(String entity, String[] data) {
        switch (entity) {
            case "user":
                return evalAddUser(data);
            case "fight":
                return evalAddFight(data);
            case "character":
                return evalAddCharacter(data);
            default:
                return new Pair<>(false, "Unrecognized entity!");
        }
    }

    private Pair<Boolean, String> evalAddUser(String[] data) {
        String username = "";
        String password = "";

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("username") || cmp.equals("u") || cmp.equals("name")) {
                username = data[i + 1].replace("\"", "");
            } else if (cmp.equals("password") || cmp.equals("p")) {
                password = data[i + 1].replace("\"", "");
            }
        }

        if (username.equals("") || password.equals("")) {
            return fail("Wrong usage! You must user -u (or --username) \"<some username>\" -p (or --password) \"<some password>\"");
        }

        userProvider.addNew(username, password);
        return success();
    }

    private Pair<Boolean, String> evalAddCharacter(String[] data) {
        String username = "";
        long userId = -1;
        String name = "";

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("u") || cmp.equals("username")) {
                username = data[i + 1].replace("\"", "");
            } else if (cmp.equals("userid") || cmp.equals("id") || cmp.equals("i")) {
                userId = Long.parseLong(data[i + 1].replace("\"", ""));
            } else if (cmp.equals("name") || cmp.equals("charactername") || cmp.equals("n")) {
                name = data[i + 1].replace("\"", "");
            }
        }

        if ((userId == -1 && username.equals("")) || name.equals("")) {
            return fail("You must specify --username \"<some username>\" or -userId <id> and -characterName \"<some name>\"!");
        }

        User user = userId != -1 ? userProvider.get(userId) : userProvider.get(username);
        if (user == null) {
            return fail("User with this username or id does not exists!");
        }

        characterProvider.addNew(user.getId(), name);
        return success();
    }

    private Pair<Boolean, String> evalAddFight(String[] data) {
        String place = "";
        String dateTimeInString = "";

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("place") || cmp.equals("p")) {
                place = data[i + 1].replace("\"", "");
            } else if (cmp.equals("date") || cmp.equals("d") || cmp.equals("datetime") || cmp.equals("time")) {
                dateTimeInString = data[i + 1].replace("\"", "");
            }
        }

        if (place.equals("") || dateTimeInString.equals("")) {
            return fail("Wrong usage! You must user -u (or --username) \"<some username>\" -p (or --password) \"<some password>\"\n");
        }

        Date date;
        try {
            // DD/MM/YYYY
            String[] dateStrings = dateTimeInString.split("/");
            date = new Date(Integer.parseInt(dateStrings[2]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[0]));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return fail("Could not parse date value!\nCorrect format is DD/MM/YYYY");
        }

        fightProvider.addNew(place, date);
        return success();
    }

    private Pair<Boolean, String> evalUpdate(String entity, String[] data) {
        switch (entity) {
            case "user":
                return evalUpdateUser(data);
            case "fight":
                return evalUpdateFight(data);
            case "character":
                return evalUpdateCharacter(data);
            default:
                return new Pair<>(false, "Unrecognized entity!");
        }
    }

    private Pair<Boolean, String> evalUpdateUser(String[] data) {
        String username = "";
        long userId = -1;
        String newUsername = "";
        String newPassword = "";

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("newusername") || cmp.equals("nu") || cmp.equals("newname")) {
                newUsername = data[i + 1].replace("\"", "");
            } else if (cmp.equals("newpassword") || cmp.equals("np")) {
                newPassword = data[i + 1].replace("\"", "");
            } else if (cmp.equals("u") || cmp.equals("username")) {
                username = data[i + 1].replace("\"", "");
            } else if (cmp.equals("userid") || cmp.equals("id") || cmp.equals("i")) {
                userId = Long.parseLong(data[i + 1].replace("\"", ""));
            }
        }

        if (username.equals("") && userId == -1) {
            return fail("Wrong usage! You must specify either username (--username or -u) or user id (--userId or -id).\n");
        }

        User user = userId != -1 ? userProvider.get(userId) : userProvider.get(username);
        if (user == null) {
            return fail("User with this username of id does not exists!");
        }

        if (!newUsername.equals("")) {
            userProvider.changeUsername(user.getId(), newUsername);
        }

        if (!newPassword.equals("")) {
            userProvider.changePassword(user.getId(), newPassword);
        }

        return success();
    }

    private Pair<Boolean, String> evalUpdateCharacter(String[] data) {
        String name = "";
        long id = -1;
        String newName = "";

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("newname") || cmp.equals("nn")) {
                newName = data[i + 1].replace("\"", "");
            } else if (cmp.equals("n") || cmp.equals("name")) {
                name = data[i + 1].replace("\"", "");
            } else if (cmp.equals("id") || cmp.equals("i")) {
                id = Long.parseLong(data[i + 1].replace("\"", ""));
            }
        }

        if (name.equals("") && id == -1) {
            return fail("Wrong usage! You must specify either name (--name or -n) or character id (--id or -i).\n");
        }

        Character character = id != -1 ? characterProvider.get(id) : characterProvider.get(name);
        if (character == null) {
            return fail("Character with this name or id does not exists!");
        }

        if (!newName.equals("")) {
            characterProvider.changeName(character.getId(), newName);
        }

        return success();
    }

    private Pair<Boolean, String> evalUpdateFight(String[] data) {
        long id = -1;
        List<Long> addIds = new LinkedList<>();
        List<Long> removeIds = new LinkedList<>();
        List<String> addNames = new LinkedList<>();
        List<String> removeNames = new LinkedList<>();

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("addIds") || cmp.equals("ai")) {
                String[] strIds = data[i + 1]
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");
                Arrays.stream(strIds).forEach(x -> addIds.add(Long.parseLong(x)));
            } else if (cmp.equals("removeIds") || cmp.equals("ri")) {
                String[] strIds = data[i + 1]
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");
                Arrays.stream(strIds).forEach(x -> removeIds.add(Long.parseLong(x)));
            } else if (cmp.equals("addNames") || cmp.equals("an")) {
                String[] strIds = data[i + 1]
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");
                addNames.addAll(Arrays.asList(strIds));
            } else if (cmp.equals("removeNames") || cmp.equals("rn")) {
                String[] strIds = data[i + 1]
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");
                removeNames.addAll(Arrays.asList(strIds));
            } else if (cmp.equals("id") || cmp.equals("i")) {
                id = Long.parseLong(data[i + 1].replace("\"", ""));
            }
        }

        if (id == -1) {
            return fail("Wrong usage! You must specify either id (--id or -i).\n");
        }

        Fight fight = fightProvider.get(id);
        if (fight == null) {
            return fail("Fight with this id does not exists!");
        }

        if (!areAllUserInDb(addIds, addNames) && areAllUserInDb(removeIds, removeNames)) {
            return fail("Some of given characters don't exist in database!");
        }

        Collection<Character> users = characterProvider.getAll();
        Map<String, Long> nameMap = new HashMap<>();
        users.forEach(x -> nameMap.put(x.getName(), x.getId()));

        for (long idToRemove : removeIds) {
            fightProvider.removeCharacterFromFight(id, idToRemove);
        }

        for (String nameToRemove : removeNames) {
            fightProvider.removeCharacterFromFight(id, nameMap.get(nameToRemove));
        }

        for (long addId : addIds) {
            fightProvider.addCharacterToFight(id, addId);
        }

        for (String nameToAdd : addNames) {
            fightProvider.addCharacterToFight(id, nameMap.get(nameToAdd));
        }


        return success();
    }

    private boolean areAllUserInDb(List<Long> ids, List<String> names) {
        Collection<Character> users = characterProvider.getAll();
        HashSet<Long> usIds = new HashSet<>();
        HashSet<String> usNames = new HashSet<>();
        users.forEach(x -> {
            usIds.add(x.getId());
            usNames.add(x.getName());
        });

        return usIds.containsAll(ids) && names.containsAll(usNames);
    }

    private Pair<Boolean, String> evalRemove(String entity, String[] data) {
        switch (entity) {
            case "user":
                return evalRemoveUser(data);
            case "fight":
                return evalRemoveFight(data);
            case "character":
                return evalRemoveCharacter(data);
            default:
                return new Pair<>(false, "Unrecognized entity!");
        }
    }

    private Pair<Boolean, String> evalRemoveUser(String[] data) {
        String username = "";
        long userId = -1;

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("username") || cmp.equals("u") || cmp.equals("name")) {
                username = data[i + 1].replace("\"", "");
            } else if (cmp.equals("userid") || cmp.equals("id") || cmp.equals("i")) {
                userId = Long.parseLong(data[i + 1].replace("\"", ""));
            }
        }

        if (username.equals("") && userId == -1) {
            return fail("Wrong usage! You must specify username or user id -u (or --username) \"<some username>\" -i (or --id) <id>\n");
        }

        User user = userId != -1 ? userProvider.get(userId) : userProvider.get(username);
        if (user == null) {
            return fail("User with this username or id does not exists!");
        }

        userProvider.remove(user.getId());
        return success();
    }

    private Pair<Boolean, String> evalRemoveCharacter(String[] data) {
        String name = "";
        long id = -1;

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("name") || cmp.equals("n")) {
                name = data[i + 1].replace("\"", "");
            } else if (cmp.equals("id") || cmp.equals("i")) {
                id = Long.parseLong(data[i + 1].replace("\"", ""));
            }
        }

        if (name.equals("") && id == -1) {
            return fail("Wrong usage! You must specify character id or name -n (or --name) \"<some character name>\" -i (or --id) <id>\n");
        }

        Character user = id != -1 ? characterProvider.get(id) : characterProvider.get(name);
        if (user == null) {
            return fail("Character with this name or id does not exists!");
        }

        characterProvider.remove(user.getId());
        return success();
    }

    private Pair<Boolean, String> evalRemoveFight(String[] data) {
        long id = -1;

        for (int i = 2; i + 1 < data.length; i++) {
            String cmp = data[i].toLowerCase().replace("-", "");
            if (cmp.equals("id") || cmp.equals("i")) {
                id = Long.parseLong(data[i + 1].replace("\"", ""));
            }
        }

        if (id == -1) {
            return fail("Wrong usage! You must specify id of fight -i (or --id) <id>\n");
        }

        Fight fight = fightProvider.get(id);
        if (fight == null) {
            return fail("Fight with this id does not exists!");
        }

        fightProvider.remove(fight.getId());
        return success();
    }
}
