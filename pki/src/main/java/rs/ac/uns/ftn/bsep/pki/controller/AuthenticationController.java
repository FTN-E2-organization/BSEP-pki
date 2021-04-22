package rs.ac.uns.ftn.bsep.pki.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import rs.ac.uns.ftn.bsep.pki.model.User;
import rs.ac.uns.ftn.bsep.pki.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.ftn.bsep.pki.security.auth.TokenUtils;
import rs.ac.uns.ftn.bsep.pki.security.auth.UserTokenState;
import rs.ac.uns.ftn.bsep.pki.service.UserService;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	private TokenUtils tokenUtils;
	private AuthenticationManager authenticationManager;
	private UserService userService;
	
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
			String jwt = tokenUtils.generateToken(user.getUsername(), user.getId(), user.getAuthority().getName());			
			int expiresIn = tokenUtils.getExpiredIn();

			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
		}
		catch (BadCredentialsException e) {
			return new ResponseEntity<>("Bad credentials.", HttpStatus.UNAUTHORIZED);
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
	
		
}
