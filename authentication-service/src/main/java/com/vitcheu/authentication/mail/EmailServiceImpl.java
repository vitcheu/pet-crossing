package com.vitcheu.authentication.mail;

import java.util.Date;
import java.util.Properties;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.vitcheu.authentication.constants.AppSettings;
import com.vitcheu.common.exception.EmailSendException;
import com.vitcheu.common.model.email.Email;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;
  private final JavaMailSenderImpl javaMailSenderImpl;
  private final AppSettings settings;

  @Override
  @Async
  public void sendMail(Email notificationEmail) throws EmailSendException {
    Dotenv dotenv = Dotenv.configure().
      directory(settings.getMailEnvDir()).filename(settings.getMailEnvFile()).load();

    String username = dotenv.get(
      "MAIL_USERNAME",
      "Unable to fetch MAIL_USERNAME"
    );
    String password = dotenv.get(
      "MAIL_PASSWORD",
      "Unable to fetch MAIL_PASSWORD"
    );
    int port = Integer.parseInt(
      dotenv.get("MAIL_PORT", "Unable to fetch MAIL_PORT")
    );
    String protocol = dotenv.get(
      "MAIL_PROTOCOL",
      "Unable to fetch MAIL_PROTOCOL"
    ).toLowerCase();
    String host = dotenv.get("MAIL_HOST", "Unable to fetch MAIL_HOST");

    this.javaMailSenderImpl.setUsername(username);
    this.javaMailSenderImpl.setPassword(password);
    this.javaMailSenderImpl.setPort(port);
    this.javaMailSenderImpl.setProtocol(protocol);
    this.javaMailSenderImpl.setHost(host);

    Properties props = new Properties();
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.socketFactory.port", String.valueOf(port));
    props.put(
      "mail.smtp.socketFactory.class",
      "javax.net.ssl.SSLSocketFactory"
    );
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", String.valueOf(port));
    javaMailSenderImpl.setJavaMailProperties(props);

    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

      messageHelper.setFrom(settings.getMailFrom());
      messageHelper.setTo(notificationEmail.getRecipient());
      messageHelper.setSubject(notificationEmail.getSubject());
      messageHelper.setText(notificationEmail.getBody());
      messageHelper.setReplyTo(notificationEmail.getReplyTo());
      messageHelper.setSentDate(new Date());
    };

    try {
      this.javaMailSender.send(messagePreparator);

      EmailServiceImpl.log.info("Activation email sent!!");
    } catch (MailException e) {
      EmailServiceImpl.log.error("Exception occurred when sending mail", e);
      throw new EmailSendException(
        "Exception occurred when sending mail to " +
        notificationEmail.getRecipient()
      );
    }
  }
}
