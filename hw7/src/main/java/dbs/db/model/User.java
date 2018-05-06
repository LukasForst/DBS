package dbs.db.model;

import javax.persistence.*;

@Entity
@Table(name = "\"User\"")
public class User {

    private String username;

    @Id
    @SequenceGenerator(name = "\"User_id_seq\"",
            sequenceName = "\"User_id_seq\"",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "\"User_id_seq\"")
    private long id;


    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
