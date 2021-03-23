package rs.ac.uns.ftn.bsep.pki.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.bsep.pki.dto.UserDTO;
import rs.ac.uns.ftn.bsep.pki.mapper.UserMapper;
import rs.ac.uns.ftn.bsep.pki.service.UserService;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/subjects")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getSubjects(){
		try {
			Collection<UserDTO> userDTOs = UserMapper.toUserDTOs(userService.getAllSubjects());
			return new ResponseEntity<>(userDTOs, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
