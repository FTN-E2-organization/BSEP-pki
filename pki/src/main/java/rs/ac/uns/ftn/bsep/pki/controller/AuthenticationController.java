package rs.ac.uns.ftn.bsep.pki.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import rs.ac.uns.ftn.bsep.pki.dto.PasswordRequestDTO;
import rs.ac.uns.ftn.bsep.pki.exception.BadRequestException;
import rs.ac.uns.ftn.bsep.pki.model.Authority;
import rs.ac.uns.ftn.bsep.pki.model.User;
import rs.ac.uns.ftn.bsep.pki.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.ftn.bsep.pki.security.auth.TokenUtils;
import rs.ac.uns.ftn.bsep.pki.security.auth.UserTokenState;
import rs.ac.uns.ftn.bsep.pki.service.UserService;
import rs.ac.uns.ftn.bsep.pki.validator.UserValidator;

import org.springframework.security.core.AuthenticationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.InetAddress;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	
	InetAddress ip;
	
	private TokenUtils tokenUtils;
	private AuthenticationManager authenticationManager;
	private UserService userService;
	private static Logger logger = LogManager.getLogger(AuthenticationController.class);
	
	@Autowired
	public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.tokenUtils = tokenUtils;
		this.userService = userService;
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {		
		try {		
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
							authenticationRequest.getPassword() + userService.getSaltByUsername(authenticationRequest.getUsername())));

			// Ubaci korisnika u trenutni security kontekst
			SecurityContextHolder.getContext().setAuthentication(authentication);

			User user = (User) authentication.getPrincipal();
			
			logger.info("User " + user.getUsername() + " logged in, ip address: " + authenticationRequest.getIpAddress());
			
			String jwt = tokenUtils.generateToken(user.getUsername(), user.getId(), user.getAuthority().getName());			
			int expiresIn = tokenUtils.getExpiredIn();
			Authority authority = user.getAuthority();

			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, authority.getName()));
		}
//		catch (AuthenticationException a) {
//			try {
//				logger.warn("User " + authenticationRequest.getUsername() + " failed to login");
//			} catch (Exception e) {
//			}
//			throw a;
//		}		
		catch (BadCredentialsException e) {
			try {
				logger.error("User " + authenticationRequest.getUsername() + " failed to login, ip address: " + authenticationRequest.getIpAddress());
				
				ip = InetAddress.getLocalHost();
				System.out.println("-----------------------------------Your current IP address : " + ip);
				
			} catch (Exception ex) {
			}
			throw e;
//			return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
		}
		catch (Exception e) {
			return new ResponseEntity<>("An error occurred while sending request for log in.", HttpStatus.BAD_REQUEST);
		}		
	}	
	
	
	/* kad klikne na link iz mejla, aktivira nalog */
	@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) {
		if(userService.confirmUser(confirmationToken)) {
			modelAndView.setViewName("accountVerified");
		}
		else {
			modelAndView.addObject("message","The link is invalid or broken!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}		
	

	/* kad mijenja sifru dobija link na mejl */
	@PostMapping("/password-recovery")
	public boolean recoverPassword(@RequestBody String username) throws MailException, InterruptedException
	{
		try {
			UserValidator.validateUsernameFormat(username);
			return userService.recoverPassword(username);
		}
		catch (Exception e) {
			return false;
		}
		
	}
	
	@PostMapping("/password-change")
	public ResponseEntity<?> changePassword(@RequestBody PasswordRequestDTO dto)
	{
		try {		
			return new ResponseEntity<>(userService.changePassword(dto), HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}		
		
	}

	@PostMapping("/new-activation-link")
	public ResponseEntity<?> sendNewActivationLink(@RequestBody String username) {	
		try {		
			userService.sendNewActivationLink(username);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}		
	}	
		
}
