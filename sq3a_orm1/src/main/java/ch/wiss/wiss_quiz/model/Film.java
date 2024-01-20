package ch.wiss.wiss_quiz.model;
import javax.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "film")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;

    private String title;

    @ManyToOne(optional = true)
    @JoinColumn(name = "director_id")
    private Director director;

    @ManyToOne(optional = true)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @ManyToOne(optional = true)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
        name = "film_actor",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors = new HashSet<>();
    
    // Getter und Setter
    public Long getFilmId() {
        return filmId;
    }

    public String getTitle() {
        return title;
    }

    public Director getDirector() {
        return director;
    }

    public Producer getProducer() {
        return producer;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public Set<Actor> getActors() {
        return actors;
    }

	public void setTitle(String title) {
		this.title = title;
		
	}

	public void setReleaseDate(LocalDate releaseDate) {
	this.releaseDate = releaseDate;
		
	}
	
    public void setDirector(Director director) {
        this.director = director;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

}
