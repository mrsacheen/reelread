package com.sachin.reelread.repository;

import com.sachin.reelread.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Find movies by title
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Find movies by genre
    List<Movie> findByGenre(String genre);

    // Find movies by watchlist status
    List<Movie> findByInWatchlist(boolean inWatchlist);
}
