package ru.alishev.springcourse.Project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.Project2Boot.models.Person;

import java.util.Optional;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    // book.getPerson()
//    Person findByBooks(Book book);

    Optional<Person> findByName(String name);
}