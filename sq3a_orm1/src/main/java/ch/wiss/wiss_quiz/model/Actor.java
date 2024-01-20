package ch.wiss.wiss_quiz.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "actors")
public class Actor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int actor_id;
	private String actor_name;
	
	 @ManyToMany(mappedBy = "actors")
	    private Set<Film> films = new HashSet<>();
	
	public Integer getActor_id() {
		return actor_id;
	}
	
	public String getActor_name() {
		return actor_name;
	}
	
	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}

	public void setActor_id(int actor_id) {
		this.actor_id = actor_id;
		
	}
}
