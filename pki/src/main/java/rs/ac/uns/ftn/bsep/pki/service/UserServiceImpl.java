package rs.ac.uns.ftn.bsep.pki.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.bsep.pki.dto.AddUserDTO;
import rs.ac.uns.ftn.bsep.pki.dto.PasswordRequestDTO;
import rs.ac.uns.ftn.bsep.pki.exception.BadRequestException;
import rs.ac.uns.ftn.bsep.pki.model.Authority;
import rs.ac.uns.ftn.bsep.pki.model.ConfirmationToken;
import rs.ac.uns.ftn.bsep.pki.model.RecoveryToken;
import rs.ac.uns.ftn.bsep.pki.model.User;
import rs.ac.uns.ftn.bsep.pki.repository.ConfirmationTokenRepository;
import rs.ac.uns.ftn.bsep.pki.repository.RecoveryTokenRepository;
import rs.ac.uns.ftn.bsep.pki.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthorityService authorityService;
	private EmailService emailService;
	private ConfirmationTokenRepository confirmationTokenRepository;
	private RecoveryTokenRepository recoveryTokenRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthorityService authorityService, EmailService emailService,
			ConfirmationTokenRepository confirmationTokenRepository, RecoveryTokenRepository recoveryTokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityService = authorityService;
		this.emailService = emailService;
		this.confirmationTokenRepository = confirmationTokenRepository;
		this.recoveryTokenRepository = recoveryTokenRepository;
	}

	@Override
	public Collection<User> getAllActiveSubjects() {
		return userRepository.getAllActiveSubjects();
	}

	@Override
	public void addSubject(AddUserDTO userDTO) throws Exception {
		if (userRepository.findByUsername(userDTO.username) != null)
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

						
		if(token != null && (token.getCreationDate().plusDays((long) 7).isAfter(LocalDate.now()))) {			
	      	User user = userRepository.findByUsername(token.getUser().getUsername());
	      	user.setEnabled(true);
	      	userRepository.save(user);
	      	return true;
		}		
		return false;
	}
	@Override
	public boolean recoverPassword(String username) throws MailException, InterruptedException {
		User user = userRepository.findByUsername(username);
		if (user == null || !user.isEnabled()) { 
			return false;
		}
		RecoveryToken token = recoveryTokenRepository.findByUser(user);
		if(token == null) {
			token = new RecoveryToken();
			token.setUser(user);
		}
		token.setExparationTime(LocalDateTime.now().plusMinutes(10));
		token.setRecoveryToken(UUID.randomUUID().toString());
		recoveryTokenRepository.save(token);
		emailService.sendRecoveryEmail(username, token);

		return true;
	}
	
	@Override
	public boolean changePassword(PasswordRequestDTO dto) {
		RecoveryToken token = recoveryTokenRepository.findByRecoveryToken(dto.token);
		if(token == null || token.getExparationTime().isBefore(LocalDateTime.now())) {
			return false;
		}
		
		User user = token.getUser();

		String salt = generateSalt();	
		user.setSalt(salt);
		user.setPassword(passwordEncoder.encode(dto.password + salt));
		userRepository.save(user);
		recoveryTokenRepository.delete(token);
		return true;
	}
		
	
	@Override
	public String getSaltByUsername(String username) {
		return userRepository.getSaltByUsername(username);
	}
	
	@Override
	public void sendNewActivationLink(String username) throws Exception {
		ConfirmationToken oldToken = confirmationTokenRepository.getTokenByUsername(username);
		if (oldToken == null) {
			throw new Exception("You did not register!");
		}
		else if (oldToken.getUser().isEnabled()) {
			throw new Exception("Your account is already active!");
		}
		else if (oldToken.getCreationDate().plusDays((long) 7).isAfter(LocalDate.now())) {
			throw new Exception("Your old activation link is still valid!");
		}
		
		ConfirmationToken newToken = new ConfirmationToken(oldToken.getUser());
		newToken.setTokenid(oldToken.getTokenid());
		confirmationTokenRepository.save(newToken);
		emailService.sendActivationEmail(username, newToken);
	}	
	
	private String generateSalt() {
		String salt = UUID.randomUUID().toString().substring(0, 8);
		System.out.println("-------------------------- salt: " + salt);
		return salt;
	}

}
