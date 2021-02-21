package com.rocketden.tester.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;

import com.rocketden.tester.service.generators.DriverGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverFileService {

    private final Map<String, DriverGeneratorService> driverGeneratorServiceMap;

    @Autowired
    public DriverFileService(List<DriverGeneratorService> driverGeneratorServiceList) {
        // Create the map from the DriverGeneratorService list.
        this.driverGeneratorServiceMap = new HashMap<>();
        driverGeneratorServiceList.forEach(driverGeneratorService ->
            this.driverGeneratorServiceMap.put(driverGeneratorService.getClass().getSimpleName(), driverGeneratorService)
        );
    }

    public void writeDriverFile(String fileDirectory, Language language, Problem problem) {
        DriverGeneratorService driverGeneratorService = driverGeneratorServiceMap.get(language.getDriverGeneratorName());
        driverGeneratorService.writeDriverFile(fileDirectory, problem);
    }
}
