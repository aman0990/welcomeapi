package com.udyogi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@SpringBootApplication
public class DemoprojectApplication {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Value("${spring.mail.host}")
    private String mailHost = "smtp.gmail.com";
    //@Value("${spring.mail.port}")
    private String mailPort = "587";
    //@Value("${spring.mail.username}")
    private String mailUsername = "amanrashm@gmail.com";
    //@Value("${spring.mail.password}")
    private String mailPassword = "cchgrrsdnchmraxc";

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(Integer.parseInt(mailPort));
        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPassword(mailPassword);
        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
        return javaMailSender;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoprojectApplication.class, args);
    }

}
