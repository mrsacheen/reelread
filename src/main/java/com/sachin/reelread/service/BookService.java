package com.sachin.reelread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sachin.reelread.model.Book;
import com.sachin.reelread.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            Set<String> seenBooks = new HashSet<>();
            for (JsonNode item : result.get("items")) {
                JsonNode volumeInfo = item.get("volumeInfo");

                // Check if title exists before calling asText()
                String title = volumeInfo.has("title") ? volumeInfo.get("title").asText() : "Unknown Title";

                // Check if authors exist before calling asText()
                String author = volumeInfo.has("authors") && volumeInfo.get("authors").isArray() && volumeInfo.get("authors").size() > 0
                        ? volumeInfo.get("authors").get(0).asText()
                        : "Unknown Author";

                String uniqueIdentifier = title.toLowerCase() + "-" + author.toLowerCase();

                if (!seenBooks.contains(uniqueIdentifier)) {
                    seenBooks.add(uniqueIdentifier);

                    // Update the book's metadata
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setDescription(volumeInfo.has("description") ? volumeInfo.get("description").asText() : "No description available");
                    book.setPosterUrl(volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail")
                            ? volumeInfo.get("imageLinks").get("thumbnail").asText()
                            : null);

                    break;  // Stop after the first matching book is found
                }
            }
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
    public void updateBooksWithPosterUrl() {
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            // Fetch updated metadata, including the poster URL
            saveBook(book);
        }
    }

    public List<Book> getBooksByStatus(String status) {
        return bookRepository.findByStatus(status);
    }
}
