package io.springbox.recommendations.repositories;

import io.springbox.recommendations.domain.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface PersonRepository extends GraphRepository<Person> {
    Person findByUserName(String userName);
}
