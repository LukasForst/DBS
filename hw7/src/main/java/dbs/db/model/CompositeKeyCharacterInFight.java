package dbs.db.model;

import javax.persistence.Column;
import java.io.Serializable;

public class CompositeKeyCharacterInFight implements Serializable {
    @Column(name = "character_id")
    public long characterId;

    @Column(name = "fight_id")
    public long fightId;
}
