package rs.ac.uns.ftn.bsep.pki.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.ac.uns.ftn.bsep.pki.dto.AddUserDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;

public class UserValidator {

	public static void addSubjectValidator(AddUserDTO userDTO) throws Exception {
		validateField(userDTO.username, "Username is required field.");
		validateField(userDTO.password, "Password is required field.");
		validatePasswordFormat(userDTO.password);
	}
	
	private static void validateField(String field, String message) throws Exception {
		if(field == null || field.isEmpty()) {
			throw new ValidationException(message);
		}
	}
	
	private static void validatePasswordFormat(String password) throws Exception{
		/*Must have at least one numeric character.
		Must have at least one lowercase character.
		Must have at least one uppercase character.
		Must have at least one special symbol among @#$%
		Password length should be between 8 and 20.
		For example: Helloword#123*/
		Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) {
        	throw new ValidationException("Wrong format of password.");
        }
	}
}
