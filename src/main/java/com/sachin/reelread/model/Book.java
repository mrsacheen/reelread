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

    private boolean inReadList;  // This is the correct field name

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    private LocalDateTime startedReadingAt;
    private LocalDateTime finishedReadingAt;

    private String posterUrl;
}