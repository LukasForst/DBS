package dbs.db.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Fight {
    @Id
    @SequenceGenerator(name = "fight_id_seq",
            sequenceName = "fight_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "fight_id_seq")
    private long id;

    @Column
    private String place;

    @Column(name = "date_time")
    private java.sql.Timestamp dateTime;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "character_in_fight",
            joinColumns = @JoinColumn(name = "fight_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id")
    )
    private List<Character> charactersInFight;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public java.sql.Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(java.sql.Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Collection<Character> getCharactersInFight() {
        return new ArrayList<Character>(charactersInFight);
    }

}
