package ru.alishev.springcourse.Project2Boot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.Project2Boot.models.Book;
import ru.alishev.springcourse.Project2Boot.models.Person;
import ru.alishev.springcourse.Project2Boot.repositories.BookRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> findAll(int page, int itemsPerPage, boolean sortByYear){
        if (sortByYear){
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year")));
        } else
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage));

    }

    public List<Book> findAll(boolean sortByYear){
        if (sortByYear){
            return bookRepository.findAll(Sort.by("year"));
        } else
            return bookRepository.findAll();
    }


    public Book findOne(int id){
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book){
        Book booToBeUpdated = bookRepository.findById(id).get();
        book.setId(id);
        book.setOwner(booToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    public Person getBookOwner(int id){
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id){
        bookRepository.findById(id).ifPresent(
            book -> {
                book.setOwner(null);
                book.setTimeOfTaking(null);
            });
    }

    @Transactional
    public void assign(int id, Person person){
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(person);
                    book.setTimeOfTaking(new Date());
                });
    }

    public List<Book> search(String startingWith){
        return bookRepository.findByNameStartingWith(startingWith);
    }


}
