package ch.wiss.wiss_quiz.model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
	int countByFirstnameAndLastname(String firstname, String lastname);

}
