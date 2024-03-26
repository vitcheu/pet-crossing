package com.vitcheu.authentication.mail;

import com.vitcheu.common.exception.EmailSendException;
import com.vitcheu.common.model.email.Email;

public interface EmailService {
  void sendMail(Email notificationEmail) throws EmailSendException;
}
