package rs.ac.uns.ftn.bsep.pki.service;

import java.util.Collection;

import rs.ac.uns.ftn.bsep.pki.dto.AddUserDTO;
import rs.ac.uns.ftn.bsep.pki.model.User;

public interface UserService {

	Collection<User> getAllActiveSubjects();
	void addSubject(AddUserDTO userDTO) throws Exception;
	boolean confirmUser(String confirmationToken);
	String getSaltByUsername(String username);
	void sendNewActivationLink(String username) throws Exception;
}
