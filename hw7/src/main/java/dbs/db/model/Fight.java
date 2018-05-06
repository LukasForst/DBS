package dbs.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Fight {

  private String place;

  @Column(name = "date_time")
  private java.sql.Timestamp dateTime;

  @Id
  private long id;


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

}
