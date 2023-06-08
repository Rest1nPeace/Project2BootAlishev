package ru.alishev.springcourse.Project2Boot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.Project2Boot.models.Book;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {


    // person.getBooks()
//    List<Book> findByOwner(Person owner);

    List<Book> findByNameStartingWith(String name);

}
