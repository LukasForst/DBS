package dbs.db.providers;

import dbs.db.model.Character;
import dbs.db.model.Fight;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FightProvider provides access and view to the Fight entity.
 * <p>
 * Note that all functions will cause immediate query to the database.
 */
public class FightProvider extends AbstractProvider {
    private Logger logger = Logger.getLogger(getClass().getName());

    public FightProvider(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Gets all records from database.
     */
    public Collection<Fight> getAll() {
        TypedQuery<Fight> fights = entityManager.createQuery(
                "SELECT f FROM Fight AS f",
                Fight.class
        );
        return fights.getResultList();
    }

    /**
     * Returns fight with given id or null when this fight does not exists.
     */
    public Fight get(long fightId) {
        return entityManager.find(Fight.class, fightId);
    }

    /**
     * Adds new Fight to db.
     */
    public Fight addNew(String place, Date date) {
        return addNew(place, date, new LinkedList<>());
    }

    /**
     * Adds new Fight to db.
     */
    public Fight addNew(String place, Date date, Collection<Long> charactersInFight) {
        final Fight fight = new Fight();
        fight.setPlace(place);
        fight.setDateTime(new Timestamp(date.getTime()));

        if (charactersInFight != null && charactersInFight.size() != 0) {
            TypedQuery<Character> tq = entityManager.createQuery(
                    "SELECT c FROM Character AS c WHERE c.id in :charactersInFight",
                    Character.class
            );
            tq.getResultStream().forEach(fight::addCharacterToFight);
        }

        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.persist(fight);
        et.commit();

        return fight;
    }

    /**
     * Removes fight from database.
     */
    public boolean remove(long fightId) {
        final Fight fight = get(fightId);
        if (fight == null) {
            logger.log(Level.SEVERE, "Fight with id: " + fightId + " could not be found.");
            return false;
        }

        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.remove(fight);
        et.commit();
        return true;
    }

    /**
     * Add character to one fight.
     */
    public Fight addCharacterToFight(long fightId, long characterId) {
        final Fight fight = get(fightId);
        final Character character = entityManager.find(Character.class, characterId);
        if (fight == null || character == null) {
            logger.log(Level.SEVERE, "Fight or character could not be found.");
            return null;
        }

        if (fight.getCharactersInFight().stream().noneMatch(x -> x.getId() == characterId)) {
            EntityTransaction et = entityManager.getTransaction();
            et.begin();
            fight.addCharacterToFight(character);
            et.commit();
        }

        return fight;
    }

    /**
     * Removes character from fight.
     */
    public boolean removeCharacterFromFight(long fightId, long characterId) {
        final Fight fight = get(fightId);
        final Character character = entityManager.find(Character.class, characterId);
        if (fight == null || character == null) {
            logger.log(Level.SEVERE, "Fight or character could not be found.");
            return false;
        }

        boolean result = false;
        if (fight.getCharactersInFight().stream().anyMatch(x -> x.getId() == characterId)) {
            EntityTransaction et = entityManager.getTransaction();
            et.begin();
            result = fight.removeCharacterFromFight(characterId);
            et.commit();
        }

        return result;
    }
}
