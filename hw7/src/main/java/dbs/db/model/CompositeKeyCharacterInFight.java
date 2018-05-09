package dbs.db.model;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Composite key representation in Java, we need it for representing character_in_fight class.
 * */
public class CompositeKeyCharacterInFight implements Serializable {
    @Column(name = "character_id")
    public long characterId;

    @Column(name = "fight_id")
    public long fightId;
}
