package com.rocketden.tester.controller.v1;

import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.service.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunnerController extends BaseRestController {

    private final RunnerService service;

    @Autowired
    public RunnerController(RunnerService service) {
        this.service = service;
    }

    @PostMapping("/runner")
    public ResponseEntity<RunDto> startGame(@RequestBody RunRequest request) {
        return new ResponseEntity<>(service.run(request), HttpStatus.OK);
    }
}
