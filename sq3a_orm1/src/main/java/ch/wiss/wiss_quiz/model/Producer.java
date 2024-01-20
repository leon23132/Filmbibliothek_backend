package ch.wiss.wiss_quiz.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "producers")  // Assuming the table name is "genres"
public class Producer {
    @Id
    private int producer_id;
    private String firstname;
    private String lastname;

    // Constructors, getters, and setters

    public Integer getProducer_id() {
        return producer_id;
    }

    public String getfirstname() {
        return firstname;
    }
    public String getlastname() {
        return lastname;
    }
    public void setProducer_firstname(String firstname) {
		this.firstname = firstname;
	}
    public void setProducer_lastname(String lastname) {
		this.lastname = lastname;
	}

	public void setDirector_firstname(String firstname) {
		this.firstname = lastname;
		
	}

	public void setDirector_lastname(String lastname) {
		this.lastname = lastname;
		
	}
}
