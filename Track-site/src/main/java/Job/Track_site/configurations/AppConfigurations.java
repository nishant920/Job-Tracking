package Job.Track_site.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Properties;

@Configuration
@Slf4j
public class AppConfigurations {
    @Bean
    public HashMap<Integer, Integer> genrateHashMap(){
        return new HashMap<>();
    }
    @Value("${MAIL_USERNAME}")
    String mailUsername;
    @Value("${MAIL_PASSWORD}")
    String password;
    @Bean
    public JavaMailSender genrateJavaMailsender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.gmail.com"); // For now email which i am using belongs to gmail so, the host will be smtp.gmail.com
        javaMailSender.setPort(587); // genrally to send mail from our computer we require some port number so, the port number which we will use is 587
        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPassword(password);
        log.info("mailUsername : " + mailUsername);
        log.info("mailPassword : " + password.length());
        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true"); // Our springboot api will connect gmail to send email via password so, mail.smtp.auth is true
        props.put("mail.smtp.starttls.enable", "true");// This property we are setting for secure connection
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return javaMailSender;
    }
}
