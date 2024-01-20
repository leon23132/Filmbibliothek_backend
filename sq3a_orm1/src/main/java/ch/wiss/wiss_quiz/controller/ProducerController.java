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

import ch.wiss.wiss_quiz.model.Producer;
import ch.wiss.wiss_quiz.model.ProducerRepository;

@RestController
@RequestMapping("/producers")
public class ProducerController {

	private final ProducerRepository producerRepository;

	@Autowired
	public ProducerController(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	// Methode zur Abfrage aller Produzenten
	@GetMapping("/")
	public @ResponseBody Iterable<Producer> getAllPoducer() {
		return producerRepository.findAll();
	}

	// Methode zum Hinzufügen eines Produzenten
	@PostMapping("/add/{firstname},{lastname}")
	public ResponseEntity<String> addProducer(@PathVariable("firstname") String firstname,
			@PathVariable("lastname") String lastname) {

		if (!firstname.matches("^[a-zA-ZäöüÄÖÜß]+$") || !lastname.matches("^[a-zA-ZäöüÄÖÜß]+$")) {
			return ResponseEntity.badRequest()
					.body("Ungültiger Vor- oder Nachname. Nur Buchstaben (inklusive Umlaute) sind erlaubt.");
		}

		String fullName = firstname + " " + lastname;

		int existingProducerCount = producerRepository.countByFirstnameAndLastname(firstname, lastname);

		if (existingProducerCount > 0) {
			return ResponseEntity.badRequest().body("Produzent mit dem Namen " + fullName + " existiert bereits.");
		}

		Producer producer = new Producer();
		producer.setProducer_firstname(firstname);
		producer.setProducer_lastname(lastname);

		try {
			producerRepository.save(producer);
			return ResponseEntity.ok("Produzent gespeichert: " + fullName);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Speichern des Produzenten");
		}
	}

	// Methode zum Löschen eines Produzenten
	@DeleteMapping("/delete/{director_id}")
	public ResponseEntity<String> deleteActor(@PathVariable int director_id) {

		if (!producerRepository.existsById(director_id)) {
			return ResponseEntity.badRequest().body("Die ID " + director_id + " existiert nicht.");
		}

		try {

			producerRepository.deleteById(director_id);
			return ResponseEntity.ok("Die ID " + director_id + " wurde erfolgreich gelöscht.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Löschen der ID " + director_id);
		}
	}

	// Methode zur Aktualisierung eines Produzenten
	@PutMapping("/update/{producer_id}")
	public ResponseEntity<String> updateProducer(@PathVariable int producer_id, @RequestBody Producer updatedProducer) {
		if (!producerRepository.existsById(producer_id)) {
			return ResponseEntity.badRequest().body("Produzent mit der ID " + producer_id + " existiert nicht.");
		}

		try {
			Producer existingProducer = producerRepository.findById(producer_id).orElse(null);

			if (existingProducer != null) {
				if (isValidName(updatedProducer.getfirstname()) && isValidName(updatedProducer.getlastname())) {
					existingProducer.setProducer_firstname(updatedProducer.getfirstname());
					existingProducer.setProducer_lastname(updatedProducer.getlastname());

					producerRepository.save(existingProducer);

					return ResponseEntity.ok("Produzent mit der ID " + producer_id + " erfolgreich aktualisiert.");
				} else {
					return ResponseEntity.badRequest().body("Ungültiger Vorname oder Nachname.");
				}
			} else {
				return ResponseEntity.badRequest()
						.body("Fehler beim Aktualisieren des Produzenten mit der ID " + producer_id);
			}
		} catch (Exception ex) {
			return ResponseEntity.badRequest()
					.body("Fehler beim Aktualisieren des Produzenten mit der ID " + producer_id);
		}
	}

	// Hilfsmethode zur Überprüfung der Gültigkeit von Vor- und Nachnamen
	private boolean isValidName(String name) {

		String regex = "^[\\p{L}]+$";
		return Pattern.matches(regex, name);
	}

}
