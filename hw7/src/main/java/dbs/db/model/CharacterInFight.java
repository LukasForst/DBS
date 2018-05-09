package dbs.db.model;

import javax.persistence.*;

/**
 * POJO class for character_in_fight db table
 * */
@Entity
@Table(name = "character_in_fight")
@IdClass(CompositeKeyCharacterInFight.class)
public class CharacterInFight {

    @Id
    @Column(name = "character_id")
    private long characterId;

    @Id
    @Column(name = "fight_id")
    private long fightId;


    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }


    public long getFightId() {
        return fightId;
    }

    public void setFightId(long fightId) {
        this.fightId = fightId;
    }

}
