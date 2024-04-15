package com.udyogi.employeemodule.mapper;

import com.udyogi.employeemodule.dtos.EducationDetailsDto;
import com.udyogi.employeemodule.dtos.ExperienceDetailsDto;
import com.udyogi.employeemodule.dtos.SignUpDto;
import com.udyogi.employeemodule.entities.EducationDetails;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.entities.ExperienceDetails;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    /**
     * Maps SignUpDto to EmployeeEntity.
     *
     * @param signUpDto SignUpDto object
     * @return EmployeeEntity mapped from SignUpDto
     */
    public static EmployeeEntity mapSignUpDtoToEmployeeEntity(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setFirstName(signUpDto.getFirstName());
        employeeEntity.setLastName(signUpDto.getLastName());
        employeeEntity.setPhoneNumber(signUpDto.getPhoneNumber());
        employeeEntity.setEmail(signUpDto.getEmail());
        employeeEntity.setPassword(signUpDto.getPassword()); // Note: Password should be encrypted before storing
        employeeEntity.setGender(signUpDto.getGender());
        return employeeEntity;
    }

    public static ExperienceDetails mapExperienceDetailsDtoToExperienceDetails(ExperienceDetailsDto experienceDetailsDto) {
        if (experienceDetailsDto == null) {
            return null;
        }
        ExperienceDetails experienceDetails = new ExperienceDetails();
        experienceDetails.setCompanyName(experienceDetailsDto.companyName());
        experienceDetails.setJobTitle(experienceDetailsDto.jobTitle());
        experienceDetails.setJobLocation(experienceDetailsDto.jobLocation());
        experienceDetails.setCtcInLpa(experienceDetailsDto.ctcInLpa());
        experienceDetails.setFreelancer(experienceDetailsDto.freelancer());
        experienceDetails.setFromDate(experienceDetailsDto.fromDate());
        experienceDetails.setToDate(experienceDetailsDto.toDate());
        experienceDetails.setNoticePeriod(experienceDetailsDto.noticePeriod());
        experienceDetails.setStillWorking(experienceDetailsDto.stillWorking());
        experienceDetails.setYear(experienceDetailsDto.year());
        experienceDetails.setMonth(experienceDetailsDto.month());
        return experienceDetails;
    }

    public static EducationDetails mapEducationDetailsDtoToEducationDetails(EducationDetailsDto educationDetailsDto) {
        if (educationDetailsDto == null) {
            return null;
        }
        EducationDetails educationDetails = new EducationDetails();
        educationDetails.setInstituteName(educationDetailsDto.getInstituteName());
        educationDetails.setDegree(educationDetailsDto.getDegree());
        educationDetails.setStillPursuing(educationDetailsDto.getStillPursuing());
        educationDetails.setFromDate(educationDetailsDto.getFromDate());
        educationDetails.setToDate(educationDetailsDto.getToDate());
        educationDetails.setResume(educationDetailsDto.getResume());
        return educationDetails;
    }
}
