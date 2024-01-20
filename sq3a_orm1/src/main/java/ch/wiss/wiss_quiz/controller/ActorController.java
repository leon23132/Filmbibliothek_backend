package ch.wiss.wiss_quiz.controller;

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

import ch.wiss.wiss_quiz.model.Actor;
import ch.wiss.wiss_quiz.model.ActorRepository;

@RestController
@RequestMapping("/actors")
public class ActorController {

	private final ActorRepository actorRepository;

	@Autowired
	public ActorController(ActorRepository actorRepository) {
		this.actorRepository = actorRepository;
	}

	// Methode zur Abfrage aller Schauspieler
	@GetMapping("/")
	public @ResponseBody Iterable<Actor> getAllActors() {
		return actorRepository.findAll();
	}

	// Methode zum Hinzufügen eines Schauspielers
	@PostMapping("/add/{actor_name}")
	public ResponseEntity<String> addActor(@PathVariable String actor_name) {
		if (isValidName(actor_name)) {
			return ResponseEntity.badRequest()
					.body("Ungültiger Actor-Name. Nur Buchstaben (inklusive Umlaute) sind erlaubt.");
		}

		int existingActorCount = actorRepository.countByActorName(actor_name);

		if (existingActorCount > 0) {
			return ResponseEntity.badRequest().body("Actor mit dem Namen " + actor_name + " existiert bereits.");
		}

		Actor actor = new Actor();
		actor.setActor_name(actor_name);

		try {
			actorRepository.save(actor);
			return ResponseEntity.ok("Actor gespeichert: " + actor_name);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Speichern des Actors");
		}
	}

	// Methode zum Löschen eines Schauspielers anhand seiner ID
	@DeleteMapping("/delete/{actor_id}")
	public ResponseEntity<String> deleteActor(@PathVariable int actor_id) {

		if (!actorRepository.existsById(actor_id)) {
			return ResponseEntity.badRequest().body("Die ID " + actor_id + " existiert nicht.");
		}

		try {

			actorRepository.deleteById(actor_id);
			return ResponseEntity.ok("Die ID " + actor_id + " wurde erfolgreich gelöscht.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("Fehler beim Löschen der ID " + actor_id);
		}
	}

	// Methode zum Aktualisieren eines Schauspielers anhand seiner ID
	@PutMapping("/update/{actorId}")
	public ResponseEntity<String> updateActor(@PathVariable int actorId, @RequestBody Actor updatedActor) {
		if (!actorRepository.existsById(actorId)) {
			return ResponseEntity.badRequest().body("Schauspieler mit der ID " + actorId + " existiert nicht.");
		}

		try {
			Actor existingActor = actorRepository.findById(actorId).orElse(null);

			if (existingActor != null) {
				if (isValidName(updatedActor.getActor_name())) {

					existingActor.setActor_name(updatedActor.getActor_name());

					actorRepository.save(existingActor);

					return ResponseEntity.ok("Schauspieler mit der ID " + actorId + " erfolgreich aktualisiert.");
				} else {
					return ResponseEntity.badRequest()
							.body("Ungültiger Schauspielername. Nur Buchstaben sind erlaubt.");
				}
			} else {
				return ResponseEntity.badRequest()
						.body("Fehler beim Aktualisieren des Schauspielers mit der ID " + actorId);
			}
		} catch (Exception ex) {
			return ResponseEntity.badRequest()
					.body("Fehler beim Aktualisieren des Schauspielers mit der ID " + actorId);
		}
	}

	private boolean isValidName(String name) {

		String regex = "^[a-zA-ZäöüÄÖÜ\\s]+$";
		return name != null && !name.isEmpty() && name.matches(regex);
	}

}
