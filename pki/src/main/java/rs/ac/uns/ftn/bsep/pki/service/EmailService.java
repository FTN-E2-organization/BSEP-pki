package rs.ac.uns.ftn.bsep.pki.service;

import org.springframework.mail.MailException;
import rs.ac.uns.ftn.bsep.pki.model.ConfirmationToken;
import rs.ac.uns.ftn.bsep.pki.model.RecoveryToken;

public interface EmailService {

	void sendActivationEmail(String email, ConfirmationToken confirmationToken) throws MailException, InterruptedException;

	void sendRecoveryEmail(String email, RecoveryToken token)
			throws MailException, InterruptedException;
	
}
