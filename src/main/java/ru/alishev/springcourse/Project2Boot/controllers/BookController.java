package ru.alishev.springcourse.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.Project2Boot.models.Book;
import ru.alishev.springcourse.Project2Boot.models.Person;
import ru.alishev.springcourse.Project2Boot.service.BookService;
import ru.alishev.springcourse.Project2Boot.service.PeopleService;


@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    private final PeopleService peopleService;

    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }


    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear){

        if (page==null && itemsPerPage==null){
            model.addAttribute("book", bookService.findAll(sortByYear));

        } else
            model.addAttribute("book", bookService.findAll(page, itemsPerPage, sortByYear));

        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookService.findOne(id));

//        Optional<Person> bookOwner = Optional.ofNullable(peopleService.findByBooks(id));
        Person bookOwner = bookService.getBookOwner(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people",peopleService.findAll());
        return "book/show";
    }


    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "book/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", bookService.findOne(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            return "book/edit";
        }
        bookService.update(id, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookService.release(id);
        return "redirect:/book/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson){
        bookService.assign(id, selectedPerson);
        return "redirect:/book/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "book/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("query") String startingWith){
        model.addAttribute("book", bookService.search(startingWith));
        return "book/search";
    }

}
