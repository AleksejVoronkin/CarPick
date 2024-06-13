package com.CarPick1.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToPartsService {

    public List<String> getToParts(String modelSeries, String engineType) throws IOException {
        String fileName = String.format("parts_database/%s%s.txt", modelSeries, engineType);

        ClassPathResource resource = new ClassPathResource(fileName);

        List<String> parts = new ArrayList<>();
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parts.add(line);
            }
        } catch (IOException e) {
            System.err.println("Failed to open file: " + fileName);
            e.printStackTrace();
            throw e;
        }

        return parts;
    }
}