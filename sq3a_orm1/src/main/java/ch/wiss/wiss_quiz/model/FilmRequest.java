package ch.wiss.wiss_quiz.model;

import java.time.LocalDate;
import java.util.Set;

public class FilmRequest {
    private String title;
    private Long directorId;
    private Long producerId;
    private Long genreId;
    private LocalDate releaseDate;
    private Set<Long> actorIds;

    // Getter und Setter

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Long getProducerId() {
        return producerId;
    }

    public void setProducerId(Long producerId) {
        this.producerId = producerId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<Long> getActorIds() {
        return actorIds;
    }

    public void setActorIds(Set<Long> actorIds) {
        this.actorIds = actorIds;
    }

	public Object getDirector() {
		// TODO Auto-generated method stub
		return null;
	}
}
