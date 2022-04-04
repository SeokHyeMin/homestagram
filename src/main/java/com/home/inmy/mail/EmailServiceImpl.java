package com.home.inmy.mail;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component @Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessage emailMessage) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
                                                            //첨부파일 없이 이메일 보내기
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject()); //제목
            mimeMessageHelper.setText(emailMessage.getMessage(), true); //본문, HTML 형태로 보내기
            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessage.getMessage());

        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new RuntimeException(e);
        }
    }
}
