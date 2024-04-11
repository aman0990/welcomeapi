package com.udyogi.util;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerRepositories;
import lombok.AllArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
@AllArgsConstructor
public class CustomIdGenerator implements IdentifierGenerator {


    private final EmployerRepositories employerRepositories;

    // Pattern for generating custom id
    // UDY-COMPANYNAME-000001
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        EmployerAdmin companyName = (EmployerAdmin) employerRepositories.findByemail("email");
        String customId = null;
        customId = "UDY-" + companyName.getCompanyName() + "-" + String.format("%06d", companyName.getId());
        return customId;

    }
}