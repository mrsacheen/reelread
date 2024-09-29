package com.sachin.reelread.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sachin.reelread.model.Book;
import com.sachin.reelread.service.BookService;
import com.sachin.reelread.service.GoogleBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private GoogleBooksService googleBooksService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Book> updateBookStatus(@PathVariable Long id, @RequestParam String status) {
        Book updatedBook = bookService.updateBookStatus(id, status);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public List<Book> getBooksByStatus(@PathVariable String status) {
        return bookService.getBooksByStatus(status);
    }
    @PutMapping("/update-posters")
    public ResponseEntity<Void> updateBooksWithPosters() {
        bookService.updateBooksWithPosterUrl();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        JsonNode searchResults = googleBooksService.searchBooks(query);
        List<Book> books = new ArrayList<>();

        if (searchResults != null && searchResults.has("items")) {
            for (JsonNode item : searchResults.get("items")) {
                JsonNode volumeInfo = item.get("volumeInfo");
                Book book = new Book();
                book.setTitle(volumeInfo.has("title") ? volumeInfo.get("title").asText() : "Unknown Title");

                // Handle missing authors
                if (volumeInfo.has("authors")) {
                    JsonNode authorsNode = volumeInfo.get("authors");
                    if (authorsNode.isArray() && authorsNode.size() > 0) {
                        book.setAuthor(authorsNode.get(0).asText());
                    } else {
                        book.setAuthor("Unknown Author");
                    }
                } else {
                    book.setAuthor("Unknown Author");
                }

                book.setDescription(volumeInfo.has("description") ? volumeInfo.get("description").asText() : "No Description");

                // Handle posterUrl safely
                if (volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail")) {
                    book.setPosterUrl(volumeInfo.get("imageLinks").get("thumbnail").asText());
                } else {
                    book.setPosterUrl(null);
                }

                books.add(book);
            }
        }

        return ResponseEntity.ok(books);
    }

}
