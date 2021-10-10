package com.zhongzx.reminder.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConfig mailConfig;

    private boolean send(String from, String subject, String text, String... to) {
        boolean success = true;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // 设置邮件内容，第二个参数设置是否支持 text/html 类型
            helper.setText(text, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            success = false;
            log.error("发送邮件异常 {}", e.getMessage());
        }
        return success;
    }

    public boolean send() {
        String[] to = mailConfig.getTo().toArray(new String[]{});
        return send(mailConfig.getUsername(), mailConfig.getSubject(), mailConfig.getText(), to);
    }
}
