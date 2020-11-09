package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import org.springframework.stereotype.Service;

@Service
public class SetupService {

    public String createTempFolder(RunRequest request) {
        return "folder_name";
    }
}
