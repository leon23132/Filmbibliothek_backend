package ch.wiss.wiss_quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.wiss.wiss_quiz.model.Actor;
import ch.wiss.wiss_quiz.model.ActorRepository;
import ch.wiss.wiss_quiz.model.Film;
import ch.wiss.wiss_quiz.model.FilmRepository;

import java.util.List;

@RestController
@RequestMapping("/film-actors")
public class FilmActorController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ActorRepository actorRepository;

    // Methode zur Abfrage aller Filme
    @GetMapping("/films/all")
    public List<Film> getAllFilms() {

        return filmRepository.findAll();
    }

    // Methode zur Abfrage aller Schauspieler
    @GetMapping("/actors/all")
    public List<Actor> getAllActors() {

        return actorRepository.findAll();
    }

}
