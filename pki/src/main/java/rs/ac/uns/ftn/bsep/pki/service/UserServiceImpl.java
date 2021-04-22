package rs.ac.uns.ftn.bsep.pki.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.bsep.pki.dto.AddUserDTO;
import rs.ac.uns.ftn.bsep.pki.exception.BadRequestException;
import rs.ac.uns.ftn.bsep.pki.model.Authority;
import rs.ac.uns.ftn.bsep.pki.model.ConfirmationToken;
import rs.ac.uns.ftn.bsep.pki.model.User;
import rs.ac.uns.ftn.bsep.pki.repository.ConfirmationTokenRepository;
import rs.ac.uns.ftn.bsep.pki.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthorityService authorityService;
	private EmailService emailService;
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityService authorityService, 
			EmailService emailService, ConfirmationTokenRepository confirmationTokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityService = authorityService;
		this.emailService = emailService;
		this.confirmationTokenRepository = confirmationTokenRepository;
	}

	@Override
	public Collection<User> getAllSubjects() {
		return userRepository.getAllSubjects();
	}

	@Override
	public void addSubject(AddUserDTO userDTO) throws Exception {
		if(userRepository.findByUsername(userDTO.username) != null)
			throw new BadRequestException("Username already exists.");
		
		User user = new User();
		Authority authority = authorityService.findByname("ROLE_SUBJECT");
		String salt = generateSalt();
		user.setUsername(userDTO.username);	
		user.setSalt(salt);
		user.setPassword(passwordEncoder.encode(userDTO.password + salt));
		user.setAuthority(authority);
		user.setEnabled(false);
		
		userRepository.save(user);
		
		ConfirmationToken confirmationToken = new ConfirmationToken(user);
		confirmationTokenRepository.save(confirmationToken);
		emailService.sendActivationEmail(userDTO.username, confirmationToken);
	}

	@Override
	public boolean confirmUser(String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
				
		if(token != null) {
	      	User user = userRepository.findByUsername(token.getUser().getUsername());
	      	user.setEnabled(true);
	      	userRepository.save(user);
	      	return true;
		}		
		return false;
	}
	
	@Override
	public String getSaltByUsername(String username) {
		return userRepository.getSaltByUsername(username);
	}
	
	private String generateSalt() {
		String salt = UUID.randomUUID().toString().substring(0, 8);
		System.out.println("-------------------------- salt: " + salt);
		return salt;
	}

}
