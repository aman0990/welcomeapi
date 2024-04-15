package com.udyogi.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String email, Integer otp) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            String verifyLink = "http://localhost:8081/api/v1/employee/verifyEmail/" + email + "/" + otp;
            String logoUrl = "https://vrpigroup.com/static/media/vrpiLogo.c468638808a1a63abd35.png";

            String htmlContent = String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Email Verification</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f8f9fa;
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                    }
                    .container {
                        max-width: 600px;
                        margin: 20px;
                        padding: 30px;
                        background-color: #fff;
                        border-radius: 12px;
                        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
                        transform: perspective(1000px) rotateY(5deg);
                    }
                    .header {
                        background-color: #FF6F00;
                        padding: 20px;
                        text-align: center;
                        border-top-left-radius: 12px;
                        border-top-right-radius: 12px;
                        margin-bottom: 20px;
                        position: relative;
                    }
                    .header h1 {
                        font-size: 36px;
                        color: #fff;
                        margin: 0;
                    }
                    .header img {
                        position: absolute;
                        top: 10px;
                        left: 10px;
                        max-width: 100px;
                    }
                    .content {
                        padding: 30px;
                        text-align: center;
                    }
                    p {
                        font-size: 18px;
                        color: #333;
                        margin-bottom: 20px;
                        line-height: 1.6;
                    }
                    .otp {
                        font-size: 24px;
                        color: #FF6F00;
                        font-weight: bold;
                        margin-bottom: 20px;
                    }
                    .btn {
                        display: inline-block;
                        padding: 16px 32px;
                        background-color: #FF6F00;
                        color: #333;
                        text-decoration: none;
                        font-size: 20px;
                        border-radius: 8px;
                        transition: background-color 0.3s ease-in-out;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                        animation: pulse 1s infinite alternate;
                    }
                    .btn:hover {
                        background-color: #FFA726;
                    }
                    @keyframes pulse {
                        from {
                            transform: scale(1);
                        }
                        to {
                            transform: scale(1.05);
                        }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1><img src="%s" alt="Company Logo"> Email Verification</h1>
                    </div>
                    <div class="content">
                        <p>Thank you for registering with us. Please verify your email to activate your account.</p>
                        <p class="otp">Use this OTP to verify your email: %d</p>
                        <p>Our team will contact you soon.</p>
                        <a href="%s" class="btn">Verify Email</a>
                    </div>
                </div>
            </body>
            </html>
            """, logoUrl, otp, verifyLink);
            helper.setTo(email);
            helper.setSubject("Email Verification");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String email, Integer otp) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            var resetLink = "http://localhost:8081/resetPassword/" + email + "/" + otp;
            var htmlContent = """
                    <h1 style="font-size: 24px; color: #4E342E;">Password Reset</h1>
                    <div style="text-align: center; padding: 20px;">
                        <p style="font-size: 18px; color: #5D4037;">You have requested to reset your password. Please click the link below to reset your password.</p>
                        <p style="font-size: 16px; color: #333;">Our team will contact you soon.</p>
                        <a href="%s" target="_blank" style="display: inline-block; padding: 10px 20px; background-color: #8D6E63; color: #FFFFFF; text-decoration: none; font-size: 16px; border-radius: 5px;">Reset Password</a>
                    </div>
                    <p style="font-size: 14px; color: #333;">Please refer to <a href="C:\\Users\\anand\\OneDrive\\Desktop\\LawyerTalk\\target\\surefire-reports" target="_blank">surefire-reports</a> for the individual test results.</p>
                    <p style="font-size: 14px; color: #333;">Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.</p>
                    """.formatted(resetLink);

            senderMethod(email, mimeMessage, helper, htmlContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void senderMethod(String email, MimeMessage mimeMessage, MimeMessageHelper helper, String htmlContent) throws MessagingException, UnsupportedEncodingException {
        helper.setFrom("amanraj@udyogi.com", "Aman Raj");
        helper.setTo(email);
        helper.setCc("amanraj@udyogi.com");
        helper.setSubject("Email Verification");
        helper.setText(htmlContent, true);
        helper.setSentDate(new Date());
        javaMailSender.send(mimeMessage);
    }

    public void sendOtptoHr(String email, int otp) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            var htmlContent = """
                    <h1 style="font-size: 24px; color: #4E342E;">OTP Verification</h1>
                    <div style="text-align: center; padding: 20px;">
                        <p style="font-size: 18px; color: #5D4037;">Your OTP for registration is %s</p>
                        <p style="font-size: 16px; color: #333;">Our team will contact you soon.</p>
                    </div>
                    <p style="font-size: 14px; color: #333;">Please refer to <a href="C:\\Users\\anand\\OneDrive\\Desktop\\LawyerTalk\\target\\surefire-reports" target="_blank">surefire-reports</a> for the individual test results.</p>
                    <p style="font-size: 14px; color: #333;">Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.</p>
                    """.formatted(otp);
            senderMethod(email, mimeMessage, helper, htmlContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendConfirmationEmail(String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            var htmlContent = """
                    <h1 style="font-size: 24px; color: #4E342E;">Email Confirmation</h1>
                    <div style="text-align: center; padding: 20px;">
                        <p style="font-size: 18px; color: #5D4037;">Your email has been successfully verified.</p>
                        <p style="font-size: 16px; color: #333;">Our team will contact you soon.</p>
                    </div>
                    <p style="font-size: 14px; color: #333;">Please refer to <a href="C:\\Users\\anand\\OneDrive\\Desktop\\LawyerTalk\\target\\surefire-reports" target="_blank">surefire-reports</a> for the individual test results.</p>
                    <p style="font-size: 14px; color: #333;">Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.</p>
                    """;
            senderMethod(email, mimeMessage, helper, htmlContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}