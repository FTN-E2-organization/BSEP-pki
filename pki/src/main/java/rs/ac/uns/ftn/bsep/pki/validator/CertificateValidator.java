package rs.ac.uns.ftn.bsep.pki.validator;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.ac.uns.ftn.bsep.pki.dto.AddCertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;

public class CertificateValidator {

	public static void addCertificateValidation(AddCertificateDTO certificateDTO) throws Exception {
		
		if(certificateDTO.issuerId <= 0 || certificateDTO.subjectId <= 0)
			throw new ValidationException("Wrong format of issuer or subject ID.");
		if(certificateDTO.startDate == null)
			throw new ValidationException("Start date is required field.");
		if(certificateDTO.endDate == null)
			throw new ValidationException("End date is required field.");
		if(certificateDTO.startDate.isBefore(LocalDate.now()))
			throw new ValidationException("Start date must be greater or equal than today.");
		if(certificateDTO.endDate.isBefore(certificateDTO.startDate))
			throw new ValidationException("End date must be greater or equal than start date.");
		if(certificateDTO.commonName == null || certificateDTO.commonName.isEmpty())
			throw new ValidationException("Common name is required field.");
		if(certificateDTO.countryCode == null || certificateDTO.countryCode.isEmpty())
			throw new ValidationException("Country code is required field.");
		if(certificateDTO.state == null || certificateDTO.state.isEmpty())
			throw new ValidationException("State is required field.");
		if(certificateDTO.locality == null || certificateDTO.locality.isEmpty())
			throw new ValidationException("Locality is required field.");
		
		if(certificateDTO.typeOfClient.equals("Person")) {
			if(certificateDTO.givenName == null || certificateDTO.givenName.isEmpty())
				throw new ValidationException("Given name is required field.");
			if(certificateDTO.surname == null || certificateDTO.surname.isEmpty())
				throw new ValidationException("Surname is required field.");
			if(certificateDTO.email == null || certificateDTO.email.isEmpty())
				throw new ValidationException("Email is required field.");
			if(!isValidEmail(certificateDTO.email))
				throw new ValidationException("Wrong format of email.");
		}
		else if(certificateDTO.typeOfClient.equals("Software")) {
			if(certificateDTO.organization == null || certificateDTO.organization.isEmpty())
				throw new ValidationException("Organization is required field.");
			if(certificateDTO.organizationUnit == null || certificateDTO.organizationUnit.isEmpty())
				throw new ValidationException("Organization unit is required field.");
		}
		else {
			throw new ValidationException("Wrong type of client.");
		}
	}
	
	private static boolean isValidEmail(String email)
    {
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(email);
        return matcher.find();
    }
}
