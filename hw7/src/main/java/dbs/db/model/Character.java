package dbs.db.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Character {

    @Id
    @SequenceGenerator(name = "character_id_seq",
            sequenceName = "character_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "character_id_seq")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "character_in_fight",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fight_id", referencedColumnName = "id")
    )
    private List<Fight> fights;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Collection<Fight> getFights() {
        return new ArrayList<Fight>(fights);
    }

    public void addFight(Fight fightToAdd) {
        if (fights == null) {
            fights = new ArrayList<Fight>();
        }

        fights.add(fightToAdd);
    }
}
