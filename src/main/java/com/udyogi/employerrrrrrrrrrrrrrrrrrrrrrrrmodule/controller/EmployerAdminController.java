package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.controller;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AddJobPostDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AdminSignUp;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services.EmployerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employer")
public class EmployerAdminController {
    private final EmployerService employerService;
    private static final Logger log = LoggerFactory.getLogger(EmployerAdminController.class);

    public EmployerAdminController(EmployerService employerService) {
        this.employerService = employerService;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> addEmployer(@Valid @RequestBody AdminSignUp adminSignUp) {
        try {
            String msg = employerService.addEmployer(adminSignUp);
            log.info("Employer added successfully: {}", adminSignUp.getCompanyName());
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while adding employer", e);
            return new ResponseEntity<>("Error occurred while adding employer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    this is under development currently not woking
    @PostMapping("/login")
    public ResponseEntity<String> loginEmployer(@RequestParam String email, @RequestParam String password) {
        try {
             employerService.loginEmployer(email, password);
            log.info("Employer logged in successfully: {}", email);
            return new ResponseEntity<>("Employer logged in successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while logging in employer", e);
            return new ResponseEntity<>("Error occurred while logging in employer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-jobpost/{id}")
    public ResponseEntity<String> addJobPost(@RequestBody AddJobPostDto jobPost ,@RequestParam Long id) {
        try {
            String msg=employerService.addJobPost(jobPost,id);
            log.info("Job post added successfully");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while adding job post", e);
            return new ResponseEntity<>("Error occurred while adding job post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
