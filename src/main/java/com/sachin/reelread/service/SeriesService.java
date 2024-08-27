package com.sachin.reelread.service;

import com.sachin.reelread.model.Series;
import com.sachin.reelread.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }

    public Series getSeriesById(Long id) {
        return seriesRepository.findById(id).orElse(null);
    }

    public Series saveSeries(Series series) {
        return seriesRepository.save(series);
    }

    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }
}
