package ch.wiss.wiss_quiz.controller;

import java.util.stream.Collectors;
import java.util.Set;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.wiss.wiss_quiz.model.Actor;
import ch.wiss.wiss_quiz.model.Film;
import ch.wiss.wiss_quiz.model.FilmRepository;

@RestController
@RequestMapping("/films")
public class FilmController {

	private final FilmRepository filmRepository;

	@Autowired
	public FilmController(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	// Methode zur Abfrage aller Filme
	@GetMapping("/")
	public @ResponseBody Iterable<Film> getAllFilms() {
		return filmRepository.findAll();

	}

	// Methode zur Filterung von Filmen basierend auf verschiedenen Kriterien
	@GetMapping("/selection/{director_id},{genre_id},{producer_id},{actor_id},{releaseYear}")
	public @ResponseBody Iterable<Film> getSelection(@PathVariable(required = false) Integer director_id,
			@PathVariable(required = false) Integer producer_id, @PathVariable(required = false) Integer actor_id,
			@PathVariable(required = false) Integer releaseYear, @PathVariable(required = false) Integer genre_id) {
		return filmRepository.findAll().stream()
				.filter(film -> (director_id == 0 || director_id == film.getDirector().getdirector_id())
						&& (genre_id == 0 || genre_id == film.getGenre().getGenre_id())
						&& (producer_id == 0 || producer_id == film.getProducer().getProducer_id())
						&& (actor_id == 0
								|| film.getActors().stream().anyMatch(actor -> actor.getActor_id() == actor_id))
						&& (releaseYear == 0 || film.getReleaseDate().getYear() >= releaseYear
								&& film.getReleaseDate().getYear() < releaseYear + 10))
				.collect(Collectors.toList());
	}

	// Methode zum Hinzufügen eines Films
	@PostMapping("/add")
	public @ResponseBody String createFilm(@Valid @RequestBody Film film) {
		try {

			filmRepository.save(film);

			return "Saved";
		} catch (ConstraintViolationException e) {

			return "Validation Error: " + e.getMessage();
		} catch (DataIntegrityViolationException e) {

			return "Data Integrity Violation: " + e.getMessage();
		} catch (Exception e) {

			return "An error occurred: " + e.getMessage();
		}
	}

	// Methode zum Löschen eines Films
	@DeleteMapping("/delete/{filmId}")
	public ResponseEntity<String> deleteFilm(@PathVariable Long filmId) {
		try {

			if (!filmRepository.existsById(filmId)) {
				return ResponseEntity.badRequest().body("Film mit der ID " + filmId + " existiert nicht.");
			}

			filmRepository.deleteById(filmId);
			return ResponseEntity.ok("Film mit der ID " + filmId + " erfolgreich gelöscht.");
		} catch (Exception ex) {
			ex.printStackTrace(); // Loggen Sie den Stack-Trace für Debugging-Zwecke
			return ResponseEntity.badRequest()
					.body("Fehler beim Löschen des Films mit der ID " + filmId + ": " + ex.getMessage());
		}
	}

	// Methode zur Aktualisierung eines Films
	@PutMapping("/update/{filmId}")
	public ResponseEntity<String> updateFilm(@PathVariable Long filmId, @RequestBody Film updatedFilm) {
		if (!filmRepository.existsById(filmId)) {
			return ResponseEntity.badRequest().body("Film mit der ID " + filmId + " existiert nicht.");
		}

		try {
			Film existingFilm = filmRepository.findById(filmId).orElse(null);

			if (existingFilm != null) {

				String updatedTitle = updatedFilm.getTitle();
				if (isValidTitle(updatedTitle)) {
					existingFilm.setTitle(updatedTitle);

					existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
					existingFilm.setDirector(updatedFilm.getDirector());
					existingFilm.setProducer(updatedFilm.getProducer());
					existingFilm.setGenre(updatedFilm.getGenre());

					Set<Actor> updatedActors = updatedFilm.getActors();
					if (updatedActors != null) {
						existingFilm.getActors().clear();
						existingFilm.getActors().addAll(updatedActors);
					}

					filmRepository.save(existingFilm);

					return ResponseEntity.ok("Film mit der ID " + filmId + " erfolgreich aktualisiert.");
				} else {
					return ResponseEntity.badRequest()
							.body("Ungültiger Titel. Der Titel darf nur Buchstaben und Umlaute enthalten.");
				}
			} else {
				return ResponseEntity.badRequest().body("Fehler beim Aktualisieren des Films mit der ID " + filmId);
			}
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Aktualisieren des Films mit der ID " + filmId);
		}
	}

	// Hilfsmethode zur Überprüfung der Gültigkeit des Titels
	private boolean isValidTitle(String title) {
		String regex = "^[\\p{L}]+$";
		return title != null && title.matches(regex);
	}

}
