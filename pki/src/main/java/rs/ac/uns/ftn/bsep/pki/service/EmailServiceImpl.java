
package rs.ac.uns.ftn.bsep.pki.service;

import org.springframework.mail.MailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.bsep.pki.model.ConfirmationToken;
import rs.ac.uns.ftn.bsep.pki.model.RecoveryToken;

import org.springframework.core.env.Environment;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender mailSender;
	private Environment environment;	
	
	@Autowired public EmailServiceImpl(JavaMailSender mailSender, Environment environment) {
		this.mailSender = mailSender;
		this.environment = environment;
	}
	
	@Override
	@Async
	public void sendActivationEmail(String email, ConfirmationToken confirmationToken) throws MailException, InterruptedException {
		SimpleMailMessage mailMessage = new SimpleMailMessage();		
		String port = environment.getProperty("local.server.port");
		mailMessage.setTo(email);
		mailMessage.setFrom(environment.getProperty("spring.mail.username"));
		mailMessage.setSubject("Complete Registration!");
		mailMessage.setText("To confirm your account, please click here: "
	            +"http://localhost:" + port + "/api/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());
		mailSender.send(mailMessage);			
	}
	
	@Override
	@Async
	public void sendRecoveryEmail(String email, RecoveryToken recoveryToken) throws MailException, InterruptedException {
		SimpleMailMessage mailMessage = new SimpleMailMessage();		
		String port = environment.getProperty("local.server.port");
		mailMessage.setTo(email);
		mailMessage.setFrom(environment.getProperty("spring.mail.username"));
		mailMessage.setSubject("Password recovery");
		mailMessage.setText("This address is associated with the login," + email + ". To set a new password, please click the following link:"
	            +"https://localhost:" + port + "/html/change_password.html?token=" + recoveryToken.getRecoveryToken());
		mailSender.send(mailMessage);			
	}	

}
