package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ch.wiss.wiss_quiz.controller.ActorController;
import ch.wiss.wiss_quiz.model.Actor;
import ch.wiss.wiss_quiz.model.ActorRepository;

class ActorControllerTest {

	private ActorRepository actorRepository;
	private ActorController actorController;

	@BeforeEach
	void setUp() {
		actorRepository = mock(ActorRepository.class);
		actorController = new ActorController(actorRepository);
	}

	@Test
	void getAllActors() {
		// Mock-Daten vorbereiten
		Actor actor1 = new Actor();
		actor1.setActor_id(1);
		actor1.setActor_name("Actor1");

		Actor actor2 = new Actor();
		actor2.setActor_id(2);
		actor2.setActor_name("Actor2");

		when(actorRepository.findAll()).thenReturn(Arrays.asList(actor1, actor2));

		// Test durchführen
		Iterable<Actor> actors = actorController.getAllActors();

		// Ergebnisse überprüfen
		assertNotNull(actors);
		assertEquals(2, ((List<Actor>) actors).size());
	}

	@Test
	void addActor_ValidName() {
		// Mock-Daten vorbereiten
		String actorName = "NewActor";

		// Test durchführen
		ResponseEntity<String> response = actorController.addActor(actorName);

		// Ergebnisse überprüfen
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Actor gespeichert: " + actorName, response.getBody());
	}

	@Test
	void addActor_InvalidName() {
		// Mock-Daten vorbereiten
		String actorName = "123"; // Ungültiger Name

		// Test durchführen
		ResponseEntity<String> response = actorController.addActor(actorName);

		// Ergebnisse überprüfen
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("Ungültiger Actor-Name"));
	}

	@Test
	void deleteActor_ExistingId() {
		// Mock-Daten vorbereiten
		int actorId = 1;

		// Test durchführen
		ResponseEntity<String> response = actorController.deleteActor(actorId);

		// Ergebnisse überprüfen
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Die ID " + actorId + " wurde erfolgreich gelöscht.", response.getBody());
	}

	@Test
	void deleteActor_NonExistingId() {
		// Mock-Daten vorbereiten
		int actorId = 99; // Nicht existierende ID

		// Test durchführen
		ResponseEntity<String> response = actorController.deleteActor(actorId);

		// Ergebnisse überprüfen
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("Die ID " + actorId + " existiert nicht."));
	}

	@Test
	void updateActor_ValidName() {
		// Mock-Daten vorbereiten
		int actorId = 1;
		Actor updatedActor = new Actor();
		updatedActor.setActor_name("UpdatedActor");

		when(actorRepository.existsById(actorId)).thenReturn(true);
		when(actorRepository.findById(actorId)).thenReturn(Optional.of(new Actor()));

		// Test durchführen
		ResponseEntity<String> response = actorController.updateActor(actorId, updatedActor);

		// Ergebnisse überprüfen
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Schauspieler mit der ID " + actorId + " erfolgreich aktualisiert.", response.getBody());
	}

	@Test
	void updateActor_InvalidName() {
		// Mock-Daten vorbereiten
		int actorId = 1;
		Actor updatedActor = new Actor();
		updatedActor.setActor_name("123"); // Ungültiger Name

		when(actorRepository.existsById(actorId)).thenReturn(true);
		when(actorRepository.findById(actorId)).thenReturn(Optional.of(new Actor()));

		// Test durchführen
		ResponseEntity<String> response = actorController.updateActor(actorId, updatedActor);

		// Ergebnisse überprüfen
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("Ungültiger Schauspielername"));
	}
}
