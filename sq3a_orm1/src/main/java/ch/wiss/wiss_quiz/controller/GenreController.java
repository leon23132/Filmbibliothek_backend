package ch.wiss.wiss_quiz.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
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

import ch.wiss.wiss_quiz.model.Genre;
import ch.wiss.wiss_quiz.model.GenreRepository;

@RestController
@RequestMapping("/genres")
public class GenreController {

	private final GenreRepository genreRepository;

	@Autowired
	public GenreController(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	// Methode zur Abfrage aller Genres
	@GetMapping("/")
	public @ResponseBody Iterable<Genre> getAllGenres() {
		return genreRepository.findAll();
	}

	// Methode zum Hinzufügen eines Genres
	@PostMapping("/add/{genre_name}")
	public ResponseEntity<String> addGenre(@PathVariable String genre_name) {

		if (!genre_name.matches("^[a-zA-ZäöüÄÖÜß]+$")) {
			return ResponseEntity.badRequest()
					.body("Ungültiger Genre-Name. Nur Buchstaben (inklusive Umlaute) sind erlaubt.");
		}

		int existingGenreCount = genreRepository.countByGenreName(genre_name);

		if (existingGenreCount > 0) {
			return ResponseEntity.badRequest().body("Genre mit dem Namen " + genre_name + " existiert bereits.");
		}

		Genre genre = new Genre();
		genre.setGenre_name(genre_name);

		try {
			genreRepository.save(genre);
			return ResponseEntity.ok("Genre gespeichert: " + genre_name);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Speichern des Genres");
		}
	}

	// Methode zum Löschen eines Genres
	@DeleteMapping("/delete/{genre_id}")
	public ResponseEntity<String> deleteGenre(@PathVariable int genre_id) {

		if (!genreRepository.existsById(genre_id)) {
			return ResponseEntity.badRequest().body("Die ID " + genre_id + " existiert nicht.");
		}

		try {

			genreRepository.deleteById(genre_id);
			return ResponseEntity.ok("Die ID " + genre_id + " wurde erfolgreich gelöscht.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Löschen der ID " + genre_id);
		}
	}

	// Methode zur Aktualisierung eines Genres
	@PutMapping("/update/{genre_id}")
	public ResponseEntity<String> updateGenre(@PathVariable int genre_id, @RequestBody Genre updatedGenre) {

		if (!genreRepository.existsById(genre_id)) {
			return ResponseEntity.badRequest().body("Genre mit der ID " + genre_id + " existiert nicht.");
		}

		try {
			Genre existingGenre = genreRepository.findById(genre_id).orElse(null);

			if (existingGenre != null) {

				if (isValidGenreName(updatedGenre.getGenre_name())) {
					existingGenre.setGenre_name(updatedGenre.getGenre_name());
					genreRepository.save(existingGenre);
					return ResponseEntity.ok("Genre mit der ID " + genre_id + " erfolgreich aktualisiert.");
				} else {
					return ResponseEntity.badRequest().body("Ungültige Genre-Bezeichnung.");
				}
			} else {
				return ResponseEntity.badRequest().body("Fehler beim Aktualisieren des Genres mit der ID " + genre_id);
			}
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Aktualisieren des Genres mit der ID " + genre_id);
		}
	}

	// Hilfsmethode zur Überprüfung der Gültigkeit des Genre-Namens
	private boolean isValidGenreName(String genreName) {

		String regex = "^[\\p{L} \\s]+";
		return Pattern.matches(regex, genreName);
	}

}
