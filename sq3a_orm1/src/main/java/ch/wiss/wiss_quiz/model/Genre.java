package ch.wiss.wiss_quiz.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "genres")  // Assuming the table name is "genres"
public class Genre {
    @Id
    private int genre_id;
    private String genre_name;

    // Constructors, getters, and setters

    public Integer getGenre_id() {
        return genre_id;
    }

    public String getGenre_name() {
        return genre_name;
    }
    
    public void setGenre_name(String genre_name) {
		this.genre_name = genre_name;
	}
    
    public void setGenre_id(int genre_id) {
    	this.genre_id = genre_id;
		
	}
}
