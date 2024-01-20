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

import ch.wiss.wiss_quiz.model.Director;
import ch.wiss.wiss_quiz.model.DirectorRepository;

@RestController
@RequestMapping("/directors")
public class DirectorController {

	private final DirectorRepository directorRepository;

	@Autowired
	public DirectorController(DirectorRepository directorRepository) {
		this.directorRepository = directorRepository;
	}

	// Methode zur Abfrage aller Regisseure
	@GetMapping("/")
	public @ResponseBody Iterable<Director> getAllProducer() {
		return directorRepository.findAll();
	}

	// Methode zum Hinzufügen eines Regisseurs
	@PostMapping("/add/{firstname},{lastname}")
	public ResponseEntity<String> addDirector(@PathVariable("firstname") String firstname,
			@PathVariable("lastname") String lastname) {

		if (!firstname.matches("^[a-zA-ZäöüÄÖÜß]+$") || !lastname.matches("^[a-zA-ZäöüÄÖÜß]+$")) {
			return ResponseEntity.badRequest()
					.body("Ungültiger Vor- oder Nachname. Nur Buchstaben (inklusive Umlaute) sind erlaubt.");
		}

		String fullName = firstname + " " + lastname;

		int existingDirectorCount = directorRepository.countByFirstnameAndLastname(firstname, lastname);

		if (existingDirectorCount > 0) {
			return ResponseEntity.badRequest().body("Regisseur mit dem Namen " + fullName + " existiert bereits.");
		}

		Director director = new Director();
		director.setDirector_firstname(firstname);
		director.setDirector_lastname(lastname);

		try {
			directorRepository.save(director);
			return ResponseEntity.ok("Regisseur gespeichert: " + fullName);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Speichern des Regisseurs");
		}
	}

	// Methode zum Löschen eines Regisseurs anhand seiner ID
	@DeleteMapping("/delete/{director_id}")
	public ResponseEntity<String> deleteDirector(@PathVariable int director_id) {

		if (!directorRepository.existsById(director_id)) {
			return ResponseEntity.badRequest().body("Die ID " + director_id + " existiert nicht.");
		}

		try {

			directorRepository.deleteById(director_id);
			return ResponseEntity.ok("Die ID " + director_id + " wurde erfolgreich gelöscht.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Löschen der ID " + director_id);
		}
	}

	// Methode zum Aktualisieren eines Regisseurs anhand seiner ID
	@PutMapping("/update/{director_id}")
	public ResponseEntity<String> updateDirector(@PathVariable int director_id, @RequestBody Director updatedDirector) {
		if (!directorRepository.existsById(director_id)) {
			return ResponseEntity.badRequest().body("Regisseur mit der ID " + director_id + " existiert nicht.");
		}

		try {
			Director existingDirector = directorRepository.findById(director_id).orElse(null);

			if (existingDirector != null) {

				if (isValidName(updatedDirector.getfirstname()) && isValidName(updatedDirector.getlastname())) {
					existingDirector.setDirector_firstname(updatedDirector.getfirstname());
					existingDirector.setDirector_lastname(updatedDirector.getlastname());

					directorRepository.save(existingDirector);

					return ResponseEntity.ok("Regisseur mit der ID " + director_id + " erfolgreich aktualisiert.");
				} else {
					return ResponseEntity.badRequest().body("Ungültiger Vorname oder Nachname.");
				}
			} else {
				return ResponseEntity.badRequest()
						.body("Fehler beim Aktualisieren des Regisseurs mit der ID " + director_id);
			}
		} catch (Exception ex) {
			return ResponseEntity.badRequest()
					.body("Fehler beim Aktualisieren des Regisseurs mit der ID " + director_id);
		}
	}

	private boolean isValidName(String name) {

		String regex = "^[a-zA-ZäöüÄÖÜ\\s]+$";
		return Pattern.matches(regex, name);
	}

}
