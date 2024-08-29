package com.training.library.controller;

import com.training.library.model.Book;
import com.training.library.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class LibraryController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@Validated @RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, null, HttpStatus.CREATED);
    }

    // Get all books
    @GetMapping
    public ResponseEntity<Object> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return books.isEmpty() ? new ResponseEntity<>("No book has been added", HttpStatus.NOT_FOUND) : new ResponseEntity<>(books, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);

        return book.isPresent() ? new ResponseEntity<>(book, HttpStatus.OK) : new ResponseEntity<>("Book with id " + id + " is not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @Validated @RequestBody Book updatedBook) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {

            book.get().setId(updatedBook.getId());
            book.get().setTitle(updatedBook.getTitle());
            book.get().setAuthor(updatedBook.getAuthor());
            book.get().setPublishedYear(updatedBook.getPublishedYear());
            bookRepository.save(book.get());
            return new ResponseEntity<>(book, null, HttpStatus.OK);
        }

        return new ResponseEntity<>("Book with id " + id + " is not found", HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return new ResponseEntity<>("Book with id "+ id + " is successfully deleted", HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>("Book with id "+ id + " is not found", HttpStatus.NOT_FOUND));
    }
}
