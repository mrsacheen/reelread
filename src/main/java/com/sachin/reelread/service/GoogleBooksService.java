package com.sachin.reelread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachin.reelread.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GoogleBooksService {

    @Value("${google.api.key}")
    private String apiKey;


    public JsonNode searchBooks(String query) {
        String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
        String url = API_URL + query + "&key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

}