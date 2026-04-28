package Job.Track_site.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

@Service
@Slf4j
public class MailService {

    //we added thymleaf dependency for sending mail to the user - Thymeleaf is a Java template engine used to generate dynamic HTML.
    // In Spring Boot, it is commonly used for server-rendered pages or HTML emails


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
        /*
        Context stores values you want to use in HTML.(is an object that holds variables passed from Java to the template)
        verification_link is the variable name.
        templateEngine.process(...) opens User-Verification-mail.html.
        Thymeleaf replaces matching placeholders.
        result becomes final HTML email conten
        */
        Context context = new Context();
        context.setVariable("name", userName);
        context.setVariable("verification_link", verificationLink);
        String htmlContent =templateEngine.process("User-Verification-mail", context);

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage(); //MIME stands for Multipurpose Internet Mail Extensions
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("Verification Notification");
            mimeMessageHelper.setText(htmlContent, true);//Treat this body content as HTML.
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            log.error("Failed to send verification link", e);
            throw new RuntimeException("Failed to send verification link");
        }
    }

}
