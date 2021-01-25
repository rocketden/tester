package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SetupServiceTests {

    @Mock
    List<DriverGeneratorService> test;

    @Spy
    @InjectMocks
    private SetupService setupService;

    private static final String CODE = "print('hi')";
    private static final Language LANGUAGE = Language.PYTHON;

    @Test
    public void createTempFolderSuccess() {
        RunRequest request = new RunRequest();
        request.setCode(CODE);
        request.setLanguage(LANGUAGE);
        request.setProblem(new Problem());

        String folder = setupService.createTempFolder(request);

        // TODO: assert two files are created

        assertTrue(folder.contains("src/main/java/com/rocketden/tester/temp"));

        // Cleanup any files created (note: this will not always be reached if failures occur)
        setupService.deleteTempFolder(folder);
    }

    @Test
    public void deleteTempFolderSuccess() {

    }

    @Test
    public void deleteTempFolderInvalidFolder() {

    }
}
