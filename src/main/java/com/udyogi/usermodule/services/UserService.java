package com.udyogi.usermodule.services;

import com.udyogi.usermodule.entities.UserEntity;
import com.vrpigroup.usermodule.annotations.email.EmailValidation;
import com.vrpigroup.usermodule.annotations.email.EmailValidationServiceImpl;
import com.vrpigroup.usermodule.constants.UserConstants;
import com.vrpigroup.usermodule.dto.*;
import com.vrpigroup.usermodule.entity.*;
import com.vrpigroup.usermodule.exception.EmailNotFoundException;
import com.vrpigroup.usermodule.exception.InvalidPasswordException;
import com.vrpigroup.usermodule.exception.UserAlreadyExistException;
import com.vrpigroup.usermodule.mapper.UserMapper;
import com.vrpigroup.usermodule.repo.ContactUsRepo;
import com.vrpigroup.usermodule.repo.EducationDetailsRepo;
import com.vrpigroup.usermodule.repo.EnrollmentRepository;
import com.vrpigroup.usermodule.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vrpigroup.usermodule.constants.UserConstants.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userModuleRepository;
    private final ContactUsRepo contactUsRepo;
    private final EmailValidationServiceImpl emailValidationService;
    private final EmailValidation emailValidation;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private  final EducationDetailsRepo educationDetailsRepo;



//    private final SecurityConfig securityConfig;


    public List<UserEntity> getAllUser() {
        try {
            return userModuleRepository.findAll();
        } catch (Exception e) {
            logger.error("Error while fetching all users", e);
            return Collections.emptyList();
        }
    }



    public UserDto createUser(UserDto userDto,MultipartFile profilePhoto,
                              MultipartFile aadharFront,MultipartFile aadharBack) {
            if (userModuleRepository.existsByEmail(userDto.getEmail())) {
                logger.warn(" email already exists. Cannot create a new user.");
                throw new UserAlreadyExistException(USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS+userDto.getEmail());
            }
            UserMapper userMapper = new UserMapper(passwordEncoder);
         boolean isValid=   emailValidation.isEmailValid(userDto.getEmail());
            UserEntity userEntity = new UserEntity();
            UserEntity user = userMapper.userDtoToUser(userEntity,userDto);
            try {
                if(isValid) {
                    if (profilePhoto != null && !profilePhoto.isEmpty()) {
                        user.setProfilePic(profilePhoto.getBytes());
                    }

                    if (aadharFront != null && !aadharFront.isEmpty()) {
                        user.setAadharFront(aadharFront.getBytes());
                    }
                    if (aadharBack != null && !aadharBack.isEmpty()) {
                        user.setAadharBack(aadharBack.getBytes());
                    }
                    userModuleRepository.save(user);
                    sendOtpByEmail(userDto.getEmail(), user.getOtp());
                    logger.info("User created successfully for email: {}", userDto.getEmail());
                    return userDto;
                };
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error while setting Stroring image/pdf for user", e);
            }
            return null;
    }

    private void sendOtpByEmail(String email, String otp) {
        emailValidationService.sendVerificationEmail(email, otp);
        logger.info("Sending OTP to {}: {}", email, otp);
    }


    public void deleteUser(Long id) {
        try {
            userModuleRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error while deleting user with ID: {}", id, e);
        }
    }


    public UserDetailsDto loginUser(LoginDto userModule) {
        Optional<UserEntity> userByEmail = userModuleRepository.findByEmail(userModule.getEmail());
        if(userByEmail.isEmpty()){
            throw new EmailNotFoundException("User with email not found");
        }
        if(verifyActive(userByEmail.get()) && verifyPassword(userByEmail.get(), userModule)){
            UserEntity user = userByEmail.get();
            Long userId = user.getId();
            Optional<EducationDetails> educationDetails = educationDetailsRepo.findByUserId(userId);
            // Fetch enrollments based on the user ID dynamically
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByUserId(userId);
            // Map enrollments to DTOs
            List<EnrollCourseListDto> enrolledCourses = enrollments.stream()
                    .map(enrollment -> {
                        EnrollCourseListDto dto = new EnrollCourseListDto();
                        dto.setId(enrollment.getCourse().getId());
                        dto.setCouseId(enrollment.getCourse().getLabel());
                        dto.setCourseName(enrollment.getCourse().getCourseName());
                        // Set any other fields as needed
                        return dto;
                    })
                    .collect(Collectors.toList());
            // Create UserDetailsDto based on fetched data
            UserDetailsDto userDetailsDto;
            userDetailsDto = educationDetails.map(details -> new UserDetailsDto(
                    UserMapper.userToUserDto(user, new UserDto()),
                    enrolledCourses,
                    UserMapper.educationDetailsToEducationDetailsDto(details),
                    UserConstants.HttpStatus_OK
            )).orElseGet(() -> new UserDetailsDto(
                    UserMapper.userToUserDto(user, new UserDto()),
                    enrolledCourses,
                    null,
                    UserConstants.HttpStatus_OK
            ));
            return userDetailsDto;
        }
        return null;
    }

    private boolean verifyActive(UserEntity user) {
        if (user.isActive()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyPassword(UserEntity user, LoginDto userModule) {
        if (passwordEncoder.matches(userModule.getCreatePassword(), user.getCreatePassword())) {
            return true;
        }else {
            throw new InvalidPasswordException(UserConstants.INVALID_CREDENTIALS);
        }
    }

    public void contactUs(ContactUs contactUs) {
        try {
            contactUsRepo.save(contactUs);
        } catch (Exception e) {
            logger.error("Error while processing contact us message", e);
        }
    }


    @Transactional
    public boolean verifyAccount(String email, String otp) {
        try {
            Optional<UserEntity> user = userModuleRepository.findByEmail(email);
            if (user.isPresent() && user.get().getOtp().equals(otp)) {
                UserEntity usr= user.get();
                usr.setActive(true);
                userModuleRepository.save(usr);
                logger.warn("Save data: {}", email);
                return true;
            }
            logger.warn("Failed to verify account. User not found for email: {}", email);
            return false;
        } catch (Exception e) {
            logger.error("Error while verifying account for email: {}", email, e);
            return false;
        }
    }

    public UpdateUserDto updateUserProfileAndDetails(Long id, UpdateUserDto updatedUser) {
        Optional<UserEntity> optionalUser = userModuleRepository.findById(id);
        UserMapper userMapper = new UserMapper(passwordEncoder);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setFathersName(updatedUser.getFathersName());
            user.setGender(updatedUser.getGender());
            user.setDateOfBirth(updatedUser.getDateOfBirth());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setAddress(updatedUser.getAddress());
            user.setEmail(updatedUser.getEmail());
            user.setCreatePassword(passwordEncoder.encode(updatedUser.getCreatePassword()));
            user.setOccupation(updatedUser.getOccupation());
            user.setAadharCardNumber(updatedUser.getAadharCardNumber());
            try {
               UserEntity e= userModuleRepository.save(user);
                return userMapper.updateUserProfileAndDetails(e, updatedUser);
            } catch (Exception e) {
                logger.error("Error while updating user ", e);
            }
        } else {
            logger.warn("Failed to update user profile and details. User not found for ID: {}", id);
        }
        return null;
    }





    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
    }

    public byte[] getImage(Long id, String field) {
        Optional<UserEntity> userOptional = userModuleRepository.findById(id);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            byte[] imageData = null;

            switch (field) {
                case "profilePic":
                    imageData = user.getProfilePic();
                    break;
                case "aadharFront":
                    imageData = user.getAadharFront();
                    break;
                case "aadharBack":
                    imageData = user.getAadharBack();
                    break;
                case "incomeCert":
                    imageData = user.getIncomeCert();
                    break;
                default:
                    logger.error("Invalid field type: {}", field);
                    // You might throw an IllegalArgumentException or return a default image here
            }

            if (imageData != null) {
                return imageData;
            } else {
                logger.warn("{} photo is null for user with ID: {}", field, id);
                // You might return a default image here
                return null;
            }
        } else {
            logger.warn("Failed to get user. User not found for ID: {}", id);
            // You might throw a UserNotFoundException or return a default image here
            return null;
        }
    }


    public UserDetailsDtoById getUserDetails(Long userId) {
        Optional<UserEntity> optionalUser = userModuleRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            DocResponseByUserGetId userDto = UserMapper.userToDocResponseByUserGetId(userEntity, new DocResponseByUserGetId());

            // Fetch enrollments for the user
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByUserId(userId);

            // Map enrollments to EnrollCourseListDto
            List<EnrollCourseListDto> courseList = enrollments.stream()
                    .map(enrollment -> new EnrollCourseListDto(
                            enrollment.getCourse().getId(),
                            enrollment.getCourse().getLabel(),
                            enrollment.getCourse().getCourseName()
                            // Add other properties as needed
                    ))
                    .collect(Collectors.toList());

            // Fetch education details for the user
            Optional<EducationDetails> optionalEducationDetails = educationDetailsRepo.findByUserId(userId);
            EducationDetailsDto educationDetailsDto = optionalEducationDetails.map(UserMapper::educationDetailsToEducationDetailsDto).orElse(null);

            // Create UserDetailsDto with mapped data
            return new UserDetailsDtoById(userDto, courseList, educationDetailsDto, UserConstants.HttpStatus_OK);
        } else {
            // If user is not found, you might want to handle this case appropriately
            return null; // Or throw an exception, or return a default DTO, etc.
        }
    }

    public void updateUserDocuments(Long userId, MultipartFile profilePhoto, MultipartFile aadharFront, MultipartFile aadharBack, MultipartFile incomeCert) {
      Optional<UserEntity> user=  userModuleRepository.findById(userId);
        if(user.isPresent()){
            UserEntity userEntity = user.get();
            try {
                if (profilePhoto != null && !profilePhoto.isEmpty()) {
                    userEntity.setProfilePic(profilePhoto.getBytes());
                }
                if (aadharFront != null && !aadharFront.isEmpty()) {
                    userEntity.setAadharFront(aadharFront.getBytes());
                }
                if (aadharBack != null && !aadharBack.isEmpty()) {
                    userEntity.setAadharBack(aadharBack.getBytes());
                }
                if (incomeCert != null && !incomeCert.isEmpty()) {
                    userEntity.setIncomeCert(incomeCert.getBytes());
                }
                userModuleRepository.save(userEntity);
            } catch (IOException e) {
                logger.error("Error while setting Stroring image/pdf for user", e);
            }
        }
    }

    /*public Object getUserData(Long userId) {
        Optional<UserEntity> user = userModuleRepository.findById(userId);
        var userEducation = educationDetailsRepo.findByUserId(userId);
        var userEnrollments = enrollmentRepository.findByUserId(userId);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            UserDto userDto = new UserDto();
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            userDto.setPhoneNumber(userEntity.getPhoneNumber());
            userDto.setEmail(userEntity.getEmail());
            return userDto;
        } else {
            return null;
        }
    }*/


    // Fetch all user Data by ID - (User's personal data,
    // educational details and enrolled course data - if possible mandatory certificates details)


//    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
//        Optional<UserEntity> user = userModuleRepository.findByEmail(forgotPasswordDto.getEmail());
//        if (user.isPresent()) {
//            UserEntity userEntity = user.get();
//            userEntity.setOtp(forgotPasswordDto.getOtp());
//            userModuleRepository.save(userEntity);
//            sendOtpByEmail(forgotPasswordDto.getEmail(), forgotPasswordDto.getOtp());
//        } else {
//            logger.warn("Failed to send OTP for forgot password. User not found for email: {}", forgotPasswordDto.getEmail());
//        }
//    }
}

