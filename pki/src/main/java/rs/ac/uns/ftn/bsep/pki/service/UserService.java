package rs.ac.uns.ftn.bsep.pki.service;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;

import rs.ac.uns.ftn.bsep.pki.dto.AddUserDTO;
import rs.ac.uns.ftn.bsep.pki.dto.PasswordRequestDTO;
import rs.ac.uns.ftn.bsep.pki.model.User;

public interface UserService {

	Collection<User> getAllActiveSubjects();
	void addSubject(AddUserDTO userDTO) throws Exception;
	boolean confirmUser(String confirmationToken);
	boolean recoverPassword(String username) throws MailException, InterruptedException;
	Boolean changePassword(PasswordRequestDTO dto) throws Exception;
	String getSaltByUsername(String username);
	void sendNewActivationLink(String username) throws Exception;
}
