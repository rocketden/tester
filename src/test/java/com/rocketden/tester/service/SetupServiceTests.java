package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SetupServiceTests {

    @Mock
    DriverFileService driverFileService;

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
        Problem problem = new Problem();
        request.setProblem(problem);

        String folder = setupService.createTempFolder(request);

        String driverFile = String.format("%s/Driver.%s", folder, LANGUAGE.getExtension());
        String solutionFile = String.format("%s/Solution.%s", folder, LANGUAGE.getExtension());

        verify(driverFileService).writeDriverFile(eq(driverFile), eq(LANGUAGE), eq(problem));
        assertTrue(new File(solutionFile).exists());

        assertTrue(folder.contains("src/main/java/com/rocketden/tester/temp"));

        // Cleanup any files created (note: this will not always be reached if failures occur)
        setupService.deleteTempFolder(folder);
    }

    @Test
    public void deleteTempFolderSuccess() {
        RunRequest request = new RunRequest();
        request.setCode(CODE);
        request.setLanguage(LANGUAGE);
        Problem problem = new Problem();
        request.setProblem(problem);

        String folder = setupService.createTempFolder(request);

        assertTrue(new File(folder).exists());

        setupService.deleteTempFolder(folder);

        assertFalse(new File(folder).exists());
    }

    @Test
    public void deleteTempFolderInvalidFolder() {
        String pwd = Paths.get("").toAbsolutePath().toString();
        String relativePath = "src/main/java/com/rocketden/tester/nontempfolder";

        String safeTestDeletePath = String.format("%s/%s", pwd, relativePath);
        ApiException exception = assertThrows(ApiException.class, () -> setupService.deleteTempFolder(safeTestDeletePath));

        assertEquals(DockerSetupError.DELETE_TEMP_FOLDER, exception.getError());
    }
}
