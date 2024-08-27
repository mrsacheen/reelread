package com.sachin.reelread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sachin.reelread.model.Book;
import com.sachin.reelread.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoogleBooksService googleBooksService;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        // Fetch metadata from Google Books API before saving
        JsonNode result = googleBooksService.searchBooks(book.getTitle());

        if (result != null && result.has("items")) {
            JsonNode item = result.get("items").get(0); // Get the first result
            JsonNode volumeInfo = item.get("volumeInfo");

            // Update the book's metadata
            book.setTitle(volumeInfo.get("title").asText());
            book.setAuthor(volumeInfo.get("authors").get(0).asText());
            book.setDescription(volumeInfo.get("description").asText());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBookStatus(Long id, String status) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            if ("Currently Reading".equals(status)) {
                book.setStartedReadingAt(LocalDateTime.now());
            } else if ("Read".equals(status)) {
                book.setFinishedReadingAt(LocalDateTime.now());
            }
            book.setStatus(status);
            return bookRepository.save(book);
        }
        return null;
    }

    public List<Book> getBooksByStatus(String status) {
        return bookRepository.findByStatus(status);
    }
}
