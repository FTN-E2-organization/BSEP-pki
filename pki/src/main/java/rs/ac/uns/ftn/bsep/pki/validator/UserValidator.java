package rs.ac.uns.ftn.bsep.pki.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.ac.uns.ftn.bsep.pki.dto.AddUserDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;

public class UserValidator {

	public static void addSubjectValidator(AddUserDTO userDTO) throws Exception {
		validateField(userDTO.username, "Username is required field.");
		validateUsernameFormat(userDTO.username);
		validateField(userDTO.password, "Password is required field.");
		validatePasswordFormat(userDTO.password);
	}
	
	private static void validateField(String field, String message) throws Exception {
		if(field == null || field.isEmpty()) {
			throw new ValidationException(message);
		}
	}
	
	public static void validatePasswordFormat(String password) throws Exception{
		/*Must have at least one numeric character.
		Must have at least one lowercase character.
		Must have at least one uppercase character.
		Must have at least one special symbol.
		Password must contain a length of at least 10 characters and a maximum of 32 characters.*/
		Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[?|!@#.$%/]).{10,32}$");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) {
        	throw new ValidationException("Wrong format of password.");
        }
	}
	
	public static void validateUsernameFormat(String username) throws Exception{
		Pattern usernameRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = usernameRegex.matcher(username);
        if(!matcher.find())
        	throw new ValidationException("Wrong format of username.");
	}
}
