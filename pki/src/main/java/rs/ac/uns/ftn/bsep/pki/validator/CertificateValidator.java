package rs.ac.uns.ftn.bsep.pki.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.ac.uns.ftn.bsep.pki.dto.CertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;

public class CertificateValidator {

	public static void addCertificateValidation(CertificateDTO certificateDTO) throws Exception {
		
		validateId(certificateDTO.subjectId, "Wrong format of subject ID.");
		validateId(certificateDTO.issuerId, "Wrong format of issuer ID.");
		validateField(certificateDTO.organization, "Organization is required field.");
		validateField(certificateDTO.organizationUnit, "Organization unit is required field.");
		validateField(certificateDTO.commonName, "Common name is required field.");
		validateField(certificateDTO.email, "Email is required field.");
		validateEmailFormat(certificateDTO.email);
		validateStartDate(certificateDTO.startDate);
		validateEndDate(certificateDTO.endDate, certificateDTO.startDate);
		validateField(certificateDTO.countryCode, "Country code is required field.");
		validateField(certificateDTO.state, "State code is required field.");
		validateField(certificateDTO.locality, "Locality code is required field.");
		
		if(certificateDTO.typeOfSubject.equals("Person")) {
			validateField(certificateDTO.givenName, "Given name code is required field.");
			validateField(certificateDTO.surname, "Surname code is required field.");
		}
		
		if(!certificateDTO.keyUsage.isEmpty()) {
			validateKeyUsage(certificateDTO.keyUsage);
		}
		
		if(certificateDTO.givenName == null)
			certificateDTO.givenName = "";
		if(certificateDTO.surname == null)
			certificateDTO.surname = "";
	}
	
	private static void validateId(Long id, String message) throws Exception{
		if(id < 0)
			throw new ValidationException(message);
	}
	
	private static void validateField(String field, String message) throws Exception {
		if(field == null || field.isEmpty()) {
			throw new ValidationException(message);
		}
	}
	
	private static void validateEmailFormat(String email) throws Exception {
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(email);
        if(!matcher.find())
        	throw new ValidationException("Wrong format of email.");
    }
	
	private static void validateStartDate(LocalDate startDate) throws Exception{
		if(startDate == null)
			throw new ValidationException("Start date is required field.");
		if(startDate.isBefore(LocalDate.now()))
			throw new ValidationException("Start date must be earlier or equal than today.");
	}
	
	private static void validateEndDate(LocalDate endDate, LocalDate startDate) throws Exception{
		if(endDate == null)
			throw new ValidationException("Start date is required field.");
		if(endDate.isBefore(startDate))
			throw new ValidationException("End date must be greater or equal than start date.");
	}
	
	private static void validateKeyUsage(List<Integer> keyUsage) throws Exception {
		for(Integer ku:keyUsage) {
			if(ku < 0 || ku > 8)
				throw new ValidationException("Wrong key usage format.");
		}
	}
	
}
