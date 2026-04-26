package Job.Track_site.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

@Service
@Slf4j
public class MailService {

    JavaMailSender javaMailSender;
    TemplateEngine templateEngine;

    @Autowired
    public MailService(JavaMailSender javaMailSender, TemplateEngine templateEngine){
     this.javaMailSender = javaMailSender;
     this.templateEngine = templateEngine;
    }
    @Value("${app.base.url}")
    String appBaseUrl;
    public void sendVerificationEmail(String toEmail, String userName, String token){
        String verificationLink = appBaseUrl +"/api/v1/user/verify?token=" + token;

        Context context = new Context();
        context.setVariable();

    }

}
