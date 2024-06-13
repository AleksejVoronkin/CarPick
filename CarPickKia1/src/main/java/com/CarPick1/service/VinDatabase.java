package com.CarPick1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;


@Component
public class VinDatabase {
    private final Map<String, Map<String, String>> database;
    private final ResourceLoader resourceLoader;
    @Autowired
    public VinDatabase(ResourceLoader resourceLoader,
                       @Value("classpath:vin_database/WMI.txt") Resource wmiResource,
                       @Value("classpath:vin_database/ModelSeries.txt") Resource modelSeriesResource,
                       @Value("classpath:vin_database/Configuration.txt") Resource configurationSeriesResource,
                       @Value("classpath:vin_database/BodyType.txt") Resource bodyTypeSeriesResource,
                       @Value("classpath:vin_database/Safety.txt") Resource safetySeriesResource,
                       //@Value("classpath:vin_database/EngineType.txt") Resource engineTypeSeriesResource,
                       @Value("classpath:vin_database/SteringAndTransmission.txt") Resource steringAndTransmissionSeriesResource,
                       @Value("classpath:vin_database/ModelYear.txt") Resource modelYearSeriesResource,
                       @Value("classpath:vin_database/PlaceOfProduction.txt") Resource placeOfProductionResource

    ) {
        this.resourceLoader = resourceLoader;

        if (resourceLoader == null || wmiResource == null || modelSeriesResource == null ||
                configurationSeriesResource == null || bodyTypeSeriesResource == null ||
                safetySeriesResource == null ||
                steringAndTransmissionSeriesResource == null || modelYearSeriesResource == null ||
                placeOfProductionResource == null) {
            throw new IllegalArgumentException("One or more resources are null");
        }

        database = new HashMap<>();
        database.put("WMI", loadDatabaseFromFile(wmiResource));
        database.put("ModelSeries", loadDatabaseFromFile(modelSeriesResource));
        database.put("Configuration", loadDatabaseFromFile(configurationSeriesResource));
        database.put("BodyType", loadDatabaseFromFile(bodyTypeSeriesResource));
        database.put("Safety", loadDatabaseFromFile(safetySeriesResource));
        //database.put("EngineType", loadDatabaseFromFile(engineTypeSeriesResource));
        database.put("SteringAndTransmission", loadDatabaseFromFile(steringAndTransmissionSeriesResource));
        database.put("ModelYear", loadDatabaseFromFile(modelYearSeriesResource));
        database.put("PlaceOfProduction", loadDatabaseFromFile(placeOfProductionResource));
    }

    /////
    // Метод для загрузки данных о типе двигателя
    public String getEngineTypeData(String modelSeries, String engineCode) {
        String path = "classpath:vin_database/EngineType/" + modelSeries;
        Resource resource = resourceLoader.getResource(path);
        Map<String, String> engineData = loadDatabaseFromFile(resource);
        return engineData.getOrDefault(engineCode, "Unknown Engine Type");
    }

    private Map<String, String> loadDatabaseFromFile(Resource resource) {
        Map<String, String> dataMap = new HashMap<>();
        if (resource != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        dataMap.put(parts[0].trim(), parts[1].trim());
                    }
                }
                Logger.getGlobal().info("Data loaded from resource: " + resource.getFilename());
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE, "Error loading data from resource: " + resource.getFilename(), e);
            }
        } else {
            Logger.getGlobal().severe("Resource is null");
            throw new IllegalArgumentException("Resource cannot be null");
        }
        return dataMap;
    }

    public String getInformation(String category, String code) {
        Map<String, String> categoryMap = database.get(category);
        if (categoryMap != null) {
            return categoryMap.getOrDefault(code, "Information not found");
        }
        return "Information category not found";
    }
}
