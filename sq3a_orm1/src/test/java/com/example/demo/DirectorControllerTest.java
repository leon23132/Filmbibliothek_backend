package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import ch.wiss.wiss_quiz.controller.DirectorController;
import ch.wiss.wiss_quiz.model.Director;
import ch.wiss.wiss_quiz.model.DirectorRepository;

class DirectorControllerTest {

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorController directorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDirectors_shouldReturnListOfDirectors() {
        // Arrange
        List<Director> directors = Arrays.asList(new Director(), new Director());
        doReturn(directors).when(directorRepository).findAll();

        // Act
        Iterable<Director> result = directorController.getAllProducer();

        // Assert
        assertEquals(directors, result);
    }

    @Test
    void addDirector_withValidData_shouldReturnOkResponse() {
        // Arrange
        String firstname = "John";
        String lastname = "Doe";

        doReturn(0).when(directorRepository).countByFirstnameAndLastname(firstname, lastname);
        doReturn(null).when(directorRepository).save(any(Director.class));

        // Act
        ResponseEntity<String> response = directorController.addDirector(firstname, lastname);

        // Assert
        assertEquals("Regisseur gespeichert: John Doe", response.getBody());
    }

    @Test
    void addDirector_withInvalidData_shouldReturnBadRequestResponse() {
        // Arrange
        String firstname = "John";
        String lastname = "123";

        // Act
        ResponseEntity<String> response = directorController.addDirector(firstname, lastname);

        // Assert
        assertEquals("Ungültiger Vor- oder Nachname. Nur Buchstaben (inklusive Umlaute) sind erlaubt.", response.getBody());
    }

    // Restliche Tests bleiben unverändert
}
