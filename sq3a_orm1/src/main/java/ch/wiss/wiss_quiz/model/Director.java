package ch.wiss.wiss_quiz.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "directors")  // Assuming the table name is "genres"
public class Director {
    @Id
    private int director_id;
    private String firstname;
    private String lastname;

    // Constructors, getters, and setters

    public Integer getdirector_id() {
        return director_id;
    }

    public String getfirstname() {
        return firstname;
    }
    public String getlastname() {
        return lastname;
    }
    
    public void setDirector_firstname(String fistname) {
		this.firstname = fistname;
	}
    
    public void setDirector_lastname(String lastname) {
		this.lastname = lastname;
	}
    public void setDirector_id(int directorId) {
		this.director_id = directorId;
	}
}
