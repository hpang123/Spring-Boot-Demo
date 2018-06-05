package hpang.springboot.respository.person;

import hpang.springboot.entity.person.Person;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Optional<Person> findById(Long id);
	
	void deleteById(Long id);
}
