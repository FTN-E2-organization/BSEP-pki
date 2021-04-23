package rs.ac.uns.ftn.bsep.pki.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

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
	public Collection<User> getAllSubjects() {
		return userRepository.getAllSubjects();
	}

	@Override
	public void addSubject(AddUserDTO userDTO) throws Exception {
		if (userRepository.findByUsername(userDTO.username) != null)
			throw new BadRequestException("Username already exists.");

		User user = new User();
		Authority authority = authorityService.findByname("ROLE_SUBJECT");
		user.setUsername(userDTO.username);
		user.setPassword(passwordEncoder.encode(userDTO.password));
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

		if (token != null) {
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
		user.setPassword(passwordEncoder.encode(dto.password));
		userRepository.save(user);
		recoveryTokenRepository.delete(token);
		return true;
	}
}
