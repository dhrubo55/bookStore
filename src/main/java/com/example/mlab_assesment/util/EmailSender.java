package com.example.mlab_assesment.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String email, String subject, String body) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);

    }
}
