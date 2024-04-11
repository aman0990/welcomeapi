package com.udyogi.util;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerRepositories;
import lombok.AllArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
@AllArgsConstructor
public class CustomIdGenerator implements IdentifierGenerator {


    private final EmployerRepositories employerRepositories;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        EmployerAdmin companyName = (EmployerAdmin) employerRepositories.findByemail("email");
    }
}