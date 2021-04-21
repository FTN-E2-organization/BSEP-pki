package rs.ac.uns.ftn.bsep.pki.service;

import org.springframework.mail.MailException;
import rs.ac.uns.ftn.bsep.pki.model.ConfirmationToken;

public interface EmailService {

	void sendActivationEmail(String email, ConfirmationToken confirmationToken) throws MailException, InterruptedException;
	
}
