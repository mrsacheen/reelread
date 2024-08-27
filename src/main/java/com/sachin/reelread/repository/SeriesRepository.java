package com.sachin.reelread.repository;

import com.sachin.reelread.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    // Find series by title
    List<Series> findByTitleContainingIgnoreCase(String title);

    // Find series by genre
    List<Series> findByGenre(String genre);

    // Find series by watchlist status
    List<Series> findByInWatchlist(boolean inWatchlist);
}
