package rs.ac.uns.ftn.bsep.pki.validator;

import java.time.LocalDate;
import java.util.regex.Pattern;
import rs.ac.uns.ftn.bsep.pki.dto.AddCertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;

public class CertificateValidator {

	public static void addCertificateValidation(AddCertificateDTO certificateDTO) throws Exception {
		
		if(certificateDTO.issuerId <= 0 || certificateDTO.subjectId <= 0)
			throw new ValidationException("Wrong format of issuer and subject ID.");
		if(certificateDTO.startDate == null)
			throw new ValidationException("Start date is required field.");
		if(certificateDTO.endDate == null)
			throw new ValidationException("End date is required field.");
		if(certificateDTO.startDate.isBefore(LocalDate.now()))
			throw new ValidationException("Start date must be greater or equal than today.");
		if(certificateDTO.endDate.isBefore(certificateDTO.startDate))
			throw new ValidationException("End date must be greater or equal than start date.");
		if(certificateDTO.commonName == null)
			throw new ValidationException("Common name is required field.");
		if(certificateDTO.countryCode == null)
			throw new ValidationException("Country code is required field.");
		if(certificateDTO.state == null)
			throw new ValidationException("State is required field.");
		if(certificateDTO.locality == null)
			throw new ValidationException("Locality is required field.");
		
		if(certificateDTO.typeOfClient.equals("Person")) {
			if(certificateDTO.givenName == null)
				throw new ValidationException("Given name is required field.");
			if(certificateDTO.surname == null)
				throw new ValidationException("Surname is required field.");
			if(certificateDTO.email == null)
				throw new ValidationException("Email is required field.");
			/*if(!isValidEmail(certificateDTO.email))
				throw new ValidationException("Wrong format of email.");*/
		}else {
			if(certificateDTO.organization == null)
				throw new ValidationException("Organization is required field.");
			if(certificateDTO.organizationUnit == null)
				throw new ValidationException("Organization unit is required field.");
		}
	}
	
	private static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-] + (?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
