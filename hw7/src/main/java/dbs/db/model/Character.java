package dbs.db.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Character {

    @Id
    private long id;


    private String name;

    @Column(name = "user_id")
    private long userId;

    @ManyToMany
    @JoinTable(
            name="character_in_fight",
            joinColumns=@JoinColumn(name="character_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="fight_id", referencedColumnName="id")
    )
    private List<Fight> fights;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Collection<Fight> getFights(){
        return new ArrayList<Fight>(fights);
    }
}
