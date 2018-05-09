package dbs.db.providers;

import dbs.db.model.Character;
import dbs.db.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CharacterProvider provides access and view to the Character entity.
 * <p>
 * All functions will cause immediate query to the database.
 */
public class CharacterProvider extends AbstractProvider {
    private Logger logger = Logger.getLogger(getClass().getName());

    public CharacterProvider(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Retrieves all characters from database.
     */
    public Collection<Character> getAll() {
        TypedQuery<Character> charactersTq = entityManager.createQuery(
                "SELECT c FROM Character AS c",
                Character.class
        );
        return charactersTq.getResultList();
    }

    /**
     * Gets character according to given id.
     */
    public Character get(long characterId) {
        return entityManager.find(Character.class, characterId);
    }

    /**
     * Gets character according to given character name.
     */
    public Character get(String name) {
        TypedQuery<Character> charactersTq = entityManager.createQuery(
                "SELECT c FROM Character AS c WHERE c.name = \"" + name + "\"",
                Character.class
        );

        List<Character> characters = charactersTq.getResultList();
        return characters.size() == 0 ? null : characters.get(0);
    }

    /**
     * Removes character from database
     */
    public boolean remove(long characterId) {
        Character character = get(characterId);
        if (character == null) {
            logger.log(Level.SEVERE, "Character with id: " + characterId + " could not be found.");
            return false;
        }

        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.remove(character);
        et.commit();
        return true;
    }

    /**
     * Adds new character to database.
     */
    public Character addNew(long userId, String name) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            logger.log(Level.SEVERE, "User with id: " + userId + " could not be found.");
            return null;
        }

        Character character = new Character();
        character.setUserId(userId);
        character.setName(name);

        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.persist(character);
        et.commit();
        return character;
    }

    /**
     * Changes name of existing character.
     */
    public Character changeName(long characterId, String newName) {
        Character character = get(characterId);
        if (character == null) {
            logger.log(Level.SEVERE, "Character with id: " + characterId + " could not be found.");
            return null;
        }

        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        character.setName(newName);
        et.commit();
        return character;
    }
}
