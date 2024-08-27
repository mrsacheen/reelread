package com.sachin.reelread.controller;

import com.sachin.reelread.model.Series;
import com.sachin.reelread.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @GetMapping
    public List<Series> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/{id}")
    public Series getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }

    @PostMapping
    public Series createSeries(@RequestBody Series series) {
        return seriesService.saveSeries(series);
    }

    @PutMapping("/{id}")
    public Series updateSeries(@PathVariable Long id, @RequestBody Series series) {
        series.setId(id);
        return seriesService.saveSeries(series);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
        return ResponseEntity.ok().build();
    }
}
