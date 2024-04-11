package com.udyogi.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String email, @RequestBody Integer otp) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            var verifyLink = "http://localhost:8081/verifyEmail/" + email + "/" + otp;
            var htmlContent = """
                    <h1 style="font-size: 24px; color: #4E342E;">Email Verification</h1>
                    <div style="text-align: center; padding: 20px;">
                        <p style="font-size: 18px; color: #5D4037;">Thank you for registering with us. Please verify your email to activate your account.</p>
                        <p style="font-size: 16px; color: #333;">Our team will contact you soon.</p>
                        <a href= "%s" target="_blank" style="display: inline-block; padding: 10px 20px; background-color: #8D6E63; color: #FFFFFF; text-decoration: none; font-size: 16px; border-radius: 5px;">Verify Email</a>
                    </div>
                    <p style="font-size: 14px; color: #333;">Please refer to <a href="C:\\Users\\anand\\OneDrive\\Desktop\\LawyerTalk\\target\\surefire-reports" target="_blank">surefire-reports</a> for the individual test results.</p>
                    <p style="font-size: 14px; color: #333;">Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.</p>
                    """.formatted(verifyLink);

            senderMethod(email, mimeMessage, helper, htmlContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
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