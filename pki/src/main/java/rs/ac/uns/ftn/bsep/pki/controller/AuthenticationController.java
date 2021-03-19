package rs.ac.uns.ftn.bsep.pki.controller;

import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.bsep.pki.model.User;
import rs.ac.uns.ftn.bsep.pki.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.ftn.bsep.pki.security.auth.TokenUtils;
import rs.ac.uns.ftn.bsep.pki.security.auth.UserTokenState;
import rs.ac.uns.ftn.bsep.pki.service.CertificateService;
import rs.ac.uns.ftn.bsep.pki.service.CustomUserDetailsService;

@RestController
@RequestMapping(value = "api/auth" /*, produces = MediaType.APPLICATION_JSON_VALUE*/)
public class AuthenticationController {

	private TokenUtils tokenUtils;

	private AuthenticationManager authenticationManager;

//	@Autowired
//	private CustomUserDetailsService userDetailsService;

	
	@Autowired
	public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		this.tokenUtils = tokenUtils;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		
		try {			
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
							authenticationRequest.getPassword()));

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
		/*catch (Exception e) {
			return new ResponseEntity<>("An error occurred while sending request for log in.", HttpStatus.BAD_REQUEST);
		}*/
		
	}		
		
}
