package ch.wiss.wiss_quiz.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActorRepository extends JpaRepository<Actor, Integer>{

    @Query("SELECT COUNT(a) FROM Actor a WHERE a.actor_name = :actorName")
    int countByActorName(@Param("actorName") String actorName);
}
