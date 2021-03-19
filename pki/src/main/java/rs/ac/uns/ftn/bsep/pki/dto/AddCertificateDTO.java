package rs.ac.uns.ftn.bsep.pki.dto;

import java.time.LocalDate;

public class AddCertificateDTO {

	public Long subjectId;
	public Long issuerId;
	public LocalDate startDate;
	public LocalDate endDate;
	public boolean isCA;
    
	public String typeOfClient;
	public String commonName;
	public String givenName;
	public String surname;
	public String organization;
	public String organizationUnit;
	public String countryCode;
	public String email;
    public String state;
    public String locality;
    
    public AddCertificateDTO() {}

	public AddCertificateDTO(Long subjectId, Long issuerId, LocalDate startDate, LocalDate endDate, boolean isCA,
			String typeOfClient, String commonName, String givenName, String surname, String organization, String organizationUnit,
			String coutryCode, String email, String state, String locality) {
		super();
		this.subjectId = subjectId;
		this.issuerId = issuerId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isCA = isCA;
		this.typeOfClient = typeOfClient;
		this.commonName = commonName;
		this.givenName = givenName;
		this.surname = surname;
		this.organization = organization;
		this.organizationUnit = organizationUnit;
		this.countryCode = coutryCode;
		this.email = email;
		this.state = state;
		this.locality = locality;
	}
    
}
