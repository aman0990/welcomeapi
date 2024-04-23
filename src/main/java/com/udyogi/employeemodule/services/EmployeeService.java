package com.udyogi.employeemodule.services;

import com.udyogi.constants.UserConstants;
import com.udyogi.employeemodule.dtos.*;
import com.udyogi.employeemodule.entities.*;
import com.udyogi.employeemodule.mapper.EmployeeMapper;
import com.udyogi.employeemodule.repositories.EducationDetailsRepository;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.employeemodule.repositories.ExperienceDetailsRepository;
import com.udyogi.employeemodule.repositories.JobApplicationEntityRepository;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.JobPostRepo;
import com.udyogi.util.CustomIdGenerator;
import com.udyogi.util.EmailService;
import com.udyogi.util.FileStorageException;
import com.udyogi.util.UtilService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@AllArgsConstructor
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepo employeeRepo;
    private final JobPostRepo jobPostRepo;
    private final JobApplicationEntityRepository jobApplicationEntityRepository;
    private final EmailService emailService;
    private final UtilService utilService;
    private final CustomIdGenerator customIdGenerator;
    private final PasswordEncoder passwordEncoder;
    private final ExperienceDetailsRepository experienceDetailsRepository;
    private final EducationDetailsRepository educationDetailsRepository;

    /**
     * Service method to handle employee signup.
     *
     * @param signUpDto SignUpDto object containing user signup details
     * @return ResponseEntity indicating the outcome of the signup process
     */
    public ResponseEntity<String> signup(SignUpDto signUpDto) {
        try {
            if (signUpDto == null) {
                throw new IllegalArgumentException("SignUpDto is null");
            }

            EmployeeEntity existingEmployee = employeeRepo.findByEmail(signUpDto.getEmail());
            if (existingEmployee != null) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                        .body(UserConstants.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS);
            }

            EmployeeEntity employeeEntity = EmployeeMapper.mapSignUpDtoToEmployeeEntity(signUpDto);
            var otp = utilService.generateOtp();
            employeeEntity.setOtp(otp);
            employeeEntity.setVerified(false);
            employeeEntity.setRole("EMPLOYEE");
            if (Objects.equals(signUpDto.getGender(), "male")) employeeEntity.setGender("MALE");
            if (Objects.equals(signUpDto.getGender(), "female")) employeeEntity.setGender("FEMALE");
            if (Objects.equals(signUpDto.getGender(), "other")) employeeEntity.setGender("OTHER");
            employeeEntity.setCustomId(customIdGenerator.generateEmployeeId());
            employeeEntity.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            employeeRepo.save(employeeEntity);
            emailService.sendVerificationEmail(signUpDto.getEmail(), Math.toIntExact(otp));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserConstants.USER_ACCOUNT_CREATED_SUCCESSFULLY);
        } catch (IllegalArgumentException | DataAccessException e) {
            logger.error("Error occurred during employee signup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
        }
    }

    /**
     * Service method to verify employee's email with OTP.
     *
     * @param email Employee's email address
     * @param otp   One-time password (OTP) for verification
     * @return ResponseEntity indicating the outcome of the email verification process
     */
    public ResponseEntity<Boolean> verifyEmail(String email, Integer otp) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmail(email);

            if (employeeEntity != null && employeeEntity.getOtp().equals(otp)) {
                employeeEntity.setVerified(true);
                employeeRepo.save(employeeEntity);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Boolean.TRUE);
            } else if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Boolean.FALSE);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Boolean.FALSE);
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during employee email verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Boolean.FALSE);
        }
    }

    /**
     * Service method to handle employee login.
     *
     * @param loginDto loginDto object containing user login details
     * @return ResponseEntity indicating the outcome of the login process
     */
    public ResponseEntity<loginResponseDto> login(loginDto loginDto) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmail(loginDto.getEmail());
            if (employeeEntity != null && passwordEncoder.matches(loginDto.getPassword(), employeeEntity.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new loginResponseDto(employeeEntity, UserConstants.LOGIN_SUCCESSFUL, UserConstants.LOGIN_SUCCESSFUL_MESSAGE));
            } else if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new loginResponseDto(null, UserConstants.USER_NOT_FOUND, UserConstants.USER_NOT_FOUND_MESSAGE));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new loginResponseDto(null, UserConstants.INVALID_CREDENTIALS, UserConstants.INVALID_CREDENTIALS));
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during employee login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new loginResponseDto(null, UserConstants.FAILED_TO_PROCEED_LOGIN, UserConstants.FAILED_TO_PROCEED_LOGIN));
        }
    }

    /**
     * Service method to add experience details for an employee.
     *
     * @param id                    Employee ID
     * @param experienceDetailsDto ExperienceDetailsDto containing experience details
     * @return ResponseEntity indicating the outcome of adding experience details
     */
    public ResponseEntity<String> addExperienceDetails(Long id, ExperienceDetailsDto experienceDetailsDto) {
        try {
            if (experienceDetailsDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }

            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(id);

            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND + " " + id);
            } else {
                ExperienceDetails experienceDetails = EmployeeMapper.mapExperienceDetailsDtoToExperienceDetails(experienceDetailsDto);
                experienceDetails.setEmployee(employeeEntity);
                experienceDetailsRepository.save(experienceDetails);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(UserConstants.EXPERIENCE_DETAILS_ADDED_SUCCESSFULLY);
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during adding experience details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_EXPERIENCE_DETAILS);
        }
    }

    /**
     * Service method to add education details for an employee.
     *
     * @param id                   Employee ID
     * @param educationDetailsDto  EducationDetailsDto containing education details
     * @return ResponseEntity indicating the outcome of adding education details
     */
    public ResponseEntity<String> addEducationDetails(Long id, EducationDetailsDto educationDetailsDto) {
        try {
            if (educationDetailsDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }

            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(id);

            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND + " " + id);
            } else {
                EducationDetails educationDetails = EmployeeMapper.mapEducationDetailsDtoToEducationDetails(educationDetailsDto);
                educationDetails.setEmployee(employeeEntity);
                educationDetailsRepository.save(educationDetails);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(UserConstants.EDUCATION_DETAILS_ADDED_SUCCESSFULLY);
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during adding education details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_EDUCATION_DETAILS);
        }
    }

    /**
     * Service method to add profile picture for an employee.
     *
     * @param id   Employee ID
     * @param file MultipartFile containing the profile picture file
     * @return ResponseEntity indicating the outcome of adding the profile picture
     */
    public ResponseEntity<String> addProfilePic(Long id, MultipartFile file) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(id);

            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND + " " + id);
            } else {
                String fileName = utilService.storeFile(file);
                employeeEntity.setProfilePic(fileName.getBytes());
                employeeRepo.save(employeeEntity);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(UserConstants.PROFILE_PIC_ADDED_SUCCESSFULLY);
            }
        } catch (DataAccessException | FileStorageException e) {
            logger.error("Error occurred during adding profile pic", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_PROFILE_PIC);
        }
    }

    /**
     * Service method to add resume for an employee.
     *
     * @param id   Employee ID
     * @param file MultipartFile containing the resume file
     * @return ResponseEntity indicating the outcome of adding the resume
     */
    public ResponseEntity<String> addResume(Long id, MultipartFile file) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(id);

            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND + " " + id);
            } else {
                String fileName = utilService.storeFile(file);
                EducationDetails educationDetails = new EducationDetails();
                educationDetails.setResume(fileName.getBytes());
                educationDetailsRepository.save(educationDetails);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(UserConstants.RESUME_ADDED_SUCCESSFULLY);
            }
        } catch (DataAccessException | FileStorageException e) {
            logger.error("Error occurred during adding resume", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_RESUME);
        }
    }

    /**
     * Apply for a job by creating a job application entity.
     *
     * @param  jobId      The ID of the job post
     * @param  employeeId The ID of the employee
     * @return            ResponseEntity containing the job application entity or an error response
     */
    @Transactional
    public ResponseEntity<JobApplicationEntity> applyForJob(Long jobId, Long employeeId) {
        try {
            Optional<JobPost> jobPostOpt = jobPostRepo.findById(jobId);
            Optional<EmployeeEntity> employeeOpt = employeeRepo.findById(employeeId);
            if (jobPostOpt.isEmpty() || employeeOpt.isEmpty()) {
                String errorMessage = "Job post or employee not found with IDs: " + jobId + ", " + employeeId;
                logger.error(errorMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            JobPost jobPost = jobPostOpt.get();
            EmployeeEntity employeeEntity = employeeOpt.get();
           JobApplicationEntity job= jobApplicationEntityRepository.findByJobPostAndEmployeeEntity(jobPost, employeeEntity);
            if (job!=null){
                String errorMessage = "Employee with ID " + employeeId + " has already applied for job with ID " + jobId;
                logger.warn(errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            JobApplicationEntity jobApplicationEntity = new JobApplicationEntity();
            jobApplicationEntity.setJobPost(jobPost);
            jobApplicationEntity.setEmployeeEntity(employeeEntity);
            jobApplicationEntity.setApplyStatus(ApplicationStatus.APPLIED);
            jobApplicationEntityRepository.save(jobApplicationEntity);
            return ResponseEntity.status(HttpStatus.OK).body(jobApplicationEntity);
        } catch (Exception e) {
            String errorMessage = "Error applying for job: " + e.getMessage();
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<?> jobRecommendation(Long employeeId) {
        try {
            // Step 1: Retrieve the employee information from the database using the provided employeeId
            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(employeeId);
            // Step 2: Check if the employee exists
            if (employeeEntity == null) {
                // If the employee does not exist, return a 404 Not Found response
                /*return ResponseEntity.notFound().body("Employee not found with ID: " + employeeId);*/
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + employeeId);
            }
            // Step 3: Implement your job recommendation logic here
            // For example, you can query job recommendations based on the employee's skills, experience, etc.
            // This logic will vary based on your application requirements
            // For demonstration purposes, let's assume we generate job recommendations and store them in a list
            List<JobRecommendation> jobRecommendations = generateJobRecommendations(employeeEntity);
            // Step 4: Prepare a response indicating that job recommendations were successfully generated
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("Job recommendations for employee ID ").append(employeeId).append(":\n");
            for (JobRecommendation recommendation : jobRecommendations) {
                responseBuilder.append(recommendation.toString()).append("\n");
            }
            // Step 5: Return an OK response with the job recommendations
            return ResponseEntity.ok(responseBuilder.toString());
        } catch (DataAccessException e) {
            // Step 6: If an exception occurs while accessing the database, log the error and return an internal server error response
            logger.error("Error occurred during job recommendation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to recommend jobs due to a database error");
        }
    }

    public List<JobRecommendation> generateJobRecommendations(EmployeeEntity employeeEntity) {
        JobRecommendation jobRecommendation = new JobRecommendation();
        // Step 1: Retrieve relevant employee attributes for job recommendation
        List<String> preferredSkills = jobRecommendation.getPreferredSkills();
        List<String> preferredLocations = jobRecommendation.getPreferredLocations();
        List<String> preferredTypes = jobRecommendation.getPreferredTypes();
        List<String> preferredExp = Collections.singletonList(jobRecommendation.getPreferredExp());
        List<String> preferredWorkMode = jobRecommendation.getPreferredWorkMode();

        // Step 2: Implement job recommendation logic based on employee attributes
        List<JobRecommendation> jobRecommendations = new ArrayList<>();

        // Example: Query jobs from the repository based on employee preferences
        for (String skill : preferredSkills) {
            List<JobPost> jobsBySkill = jobPostRepo.findJobsBySkill(skill);
            for (JobPost job : jobsBySkill) {
                // Check if the job location, type, experience, and work mode match the employee preferences
                if (preferredLocations.contains(job.getLocation())
                        && preferredTypes.contains(job.getJobType())
                        && preferredExp.contains(job.getExperience())
                        && preferredWorkMode.contains(job.getWorkMode())) {
                    // Create a job recommendation based on matching job attributes
                    JobRecommendation recommendation = new JobRecommendation();
                    recommendation.setId(job.getId());
                    recommendation.setJobName(job.getJobTitle());
                    recommendation.setPreferredLocations(Collections.singletonList(job.getLocation()));
                    recommendation.setPreferredTypes(job.getJobType());
                    recommendation.setPreferredExp(job.getExperience());
                    recommendation.setPreferredWorkMode(job.getWorkMode());

                    // Add the job recommendation to the list
                    jobRecommendations.add(recommendation);
                }
            }
        }

        // Step 3: Return the list of job recommendations
        return jobRecommendations;
    }
}