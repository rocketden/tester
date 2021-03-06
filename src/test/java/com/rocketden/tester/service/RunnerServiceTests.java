package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.util.ProblemTestMethods;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RunnerServiceTests {

    @Mock
    private SetupService setupService;

    @Mock
    private DockerService dockerService;

    @Spy
    @InjectMocks
    private RunnerService runnerService;

    private static final String TEMP_FOLDER = "test";
    private static final Language LANGUAGE = Language.PYTHON;
    private static final String CODE = "print('hi')";

    @Test
    public void runSuccess() {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);

        Problem problem = ProblemTestMethods.getFindMaxProblem(new String[]{"[]", ""});
        request.setProblem(problem);

        Mockito.doReturn(TEMP_FOLDER).when(setupService).createTempFolder();
        runnerService.run(request);

        verify(setupService).populateTempFolder(TEMP_FOLDER, request);
        verify(dockerService).spawnAndRun(TEMP_FOLDER, request.getLanguage(), problem);
        verify(setupService).deleteTempFolder(TEMP_FOLDER);
    }

    @Test
    public void runFails() {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);
        request.setProblem(ProblemTestMethods.getFindMaxProblem(new String[]{"[]", ""}));

        ApiError ERROR = DockerSetupError.WRITE_CODE_TO_DISK;

        Mockito.doReturn(TEMP_FOLDER).when(setupService).createTempFolder();
        Mockito.doThrow(new ApiException(ERROR)).when(setupService).populateTempFolder(TEMP_FOLDER, request);

        ApiException exception = assertThrows(ApiException.class, () -> runnerService.run(request));

        assertEquals(ERROR, exception.getError());
        // Despite the exception, the code cleanup step still occurs
        verify(setupService).deleteTempFolder(TEMP_FOLDER);
    }
}
