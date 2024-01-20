package ch.wiss.wiss_quiz.model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

	@Query("SELECT COUNT(g) FROM Genre g WHERE g.genre_name = :genreName")
    int countByGenreName(@Param("genreName") String genre_name);
}
