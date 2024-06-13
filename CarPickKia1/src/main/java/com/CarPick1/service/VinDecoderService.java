package com.CarPick1.service;

import java.util.HashMap;
import java.util.Map;
//import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class VinDecoderService {
    private final Map<String, String> databaseMap;
    private final VinDatabase vinDatabase;

    public VinDecoderService(
            VinDatabase vinDatabase,
            @Value("classpath:vin_database/WMI.txt") Resource wmiResource,
            @Value("classpath:vin_database/ModelSeries.txt") Resource modelSeriesResource,
            @Value("classpath:vin_database/Configuration.txt") Resource configurationSeriesResource,
            @Value("classpath:vin_database/BodyType.txt") Resource bodyTypeSeriesResource,
            @Value("classpath:vin_database/Safety.txt") Resource safetySeriesResource,

            @Value("classpath:vin_database/SteringAndTransmission.txt") Resource steringAndTransmissionSeriesResource,
            @Value("classpath:vin_database/ModelYear.txt") Resource modelYearSeriesResource,
            @Value("classpath:vin_database/PlaceOfProduction.txt") Resource placeOfProductionResource
    ) {
        this.vinDatabase = vinDatabase;
        databaseMap = new HashMap<>();
        try {
            databaseMap.put("WMI", loadDatabaseFromFile(wmiResource));
            databaseMap.put("ModelSeries", loadDatabaseFromFile(modelSeriesResource));
            databaseMap.put("Configuration", loadDatabaseFromFile(configurationSeriesResource));
            databaseMap.put("BodyType", loadDatabaseFromFile(bodyTypeSeriesResource));
            databaseMap.put("Safety", loadDatabaseFromFile(safetySeriesResource));
            //databaseMap.put("EngineType", loadDatabaseFromFile(engineTypeSeriesResource));
            databaseMap.put("SteringAndTransmission", loadDatabaseFromFile(steringAndTransmissionSeriesResource));
            databaseMap.put("ModelYear", loadDatabaseFromFile(modelYearSeriesResource));
            databaseMap.put("PlaceOfProduction", loadDatabaseFromFile(placeOfProductionResource));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadDatabaseFromFile(Resource resource) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    public String decodeVIN(String vin) {
        String manufacturerIdentifier = vin.substring(0, 3);
        String decodedInformationWMI = vinDatabase.getInformation("WMI", manufacturerIdentifier);
        String modelSeries = vin.substring(3, 4);
        String decodedInformationModelSeries = vinDatabase.getInformation("ModelSeries", modelSeries);
        String configuration = vin.substring(4, 5);
        String decodedInformationConfiguration = vinDatabase.getInformation("Configuration", configuration);
        String bodyType = vin.substring(5, 6);
        String decodedInformationBodyType = vinDatabase.getInformation("BodyType", bodyType);
        String safety = vin.substring(6, 7);
        String decodedInformationSafety = vinDatabase.getInformation("Safety", safety);
        String engineType = vin.substring(7, 8);
        //String decodedInformationEngineType = vinDatabase.getInformation("EngineType", engineType);
        String decodedInformationEngineType = vinDatabase.getEngineTypeData(modelSeries, engineType);
        String steringAndTransmission = vin.substring(8, 9);
        String decodedInformationSteringAndTransmission = vinDatabase.getInformation("SteringAndTransmission", steringAndTransmission);
        String modelYear = vin.substring(9, 10);
        String decodedInformationModelYear = vinDatabase.getInformation("ModelYear", modelYear);
        String placeOfProduction = vin.substring(10, 11);
        String decodedInformationPlaceOfProduction = vinDatabase.getInformation("PlaceOfProduction", placeOfProduction);

        String combinedInformation = "ManufacturerIdentifier: " + decodedInformationWMI +
                ", ModelSeries: " + decodedInformationModelSeries +
                ", Configuration: " + decodedInformationConfiguration +
                ", BodyType: " + decodedInformationBodyType +
                ", Safety: " + decodedInformationSafety +
                ", EngineType: " + decodedInformationEngineType +
                ", SteringAndTransmission: " + decodedInformationSteringAndTransmission +
                ", ModelYear: " + decodedInformationModelYear +
                ", PlaceOfProduction: " + decodedInformationPlaceOfProduction;

        return combinedInformation;
    }
}