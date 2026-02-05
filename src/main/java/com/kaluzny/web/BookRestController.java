package com.kaluzny.web;

import com.kaluzny.domain.Book;
import com.kaluzny.domain.BookRepository;
import com.kaluzny.exception.BookNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class BookRestController {

    private final BookRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book saveBook(@Valid @RequestBody Book book) {
        log.info("saveBook() - start: book = {}", book);
        Book savedBook = repository.save(book);
        log.info("saveBook() - end: savedBook = {}", savedBook.getId());
        return savedBook;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> getAllBooks() {
        log.info("getAllBooks() - start");
        Collection<Book> collection = repository.findAll();
        log.info("getAllBooks() - end: found {} books", collection.size());
        return collection;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(@PathVariable Long id) {
        log.info("getBookById() - start: id = {}", id);
        Book receivedBook = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        log.info("getBookById() - end: book = {}", receivedBook.getId());
        return receivedBook;
    }

    @GetMapping(params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> findBookByName(@RequestParam(value = "name") String name) {
        log.info("findBookByName() - start: name = {}", name);
        Collection<Book> collection = repository.findByName(name);
        log.info("findBookByName() - end: found {} books", collection.size());
        return collection;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        log.info("updateBook() - start: id = {}, book = {}", id, book);
        Book updatedBook = repository.findById(id)
                .map(entity -> {
                    entity.setName(book.getName());
                    entity.setDescription(book.getDescription());
                    entity.setTags(book.getTags());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
        log.info("updateBook() - end: updatedBook = {}", updatedBook);
        return updatedBook;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookById(@PathVariable Long id) {
        log.info("removeBookById() - start: id = {}", id);
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("removeBookById() - end: id = {}", id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllBooks() {
        log.info("removeAllBooks() - start");
        repository.deleteAll();
        log.info("removeAllBooks() - end");
    }
}
