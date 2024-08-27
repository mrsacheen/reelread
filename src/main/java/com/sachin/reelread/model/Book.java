package com.sachin.reelread.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;

    private boolean inReadlist;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;


    // Timestamp for when the book was moved to "Currently Reading"
    private LocalDateTime startedReadingAt;

    // Timestamp for when the book was marked as "Read"
    private LocalDateTime finishedReadingAt;
}
