package rs.ac.uns.ftn.bsep.pki.dto;

public class SubjectDTO {

	public Long id;
	public String typeOfSubject;
	public String commonName;
	public String givenName;
	public String surname;
	public String organization;
	public String organizationUnit;
	public String email;
	public String coutryCode;
	public String state;
	public String locality;
	
	public SubjectDTO() {}
	
	public SubjectDTO(Long id, String typeOfSubject, String commonName, String givenName, String surname,
			String organization, String organizationUnit, String email, String coutryCode, String state,
			String locality) {
		super();
		this.id = id;
		this.typeOfSubject = typeOfSubject;
		this.commonName = commonName;
		this.givenName = givenName;
		this.surname = surname;
		this.organization = organization;
		this.organizationUnit = organizationUnit;
		this.email = email;
		this.coutryCode = coutryCode;
		this.state = state;
		this.locality = locality;
	}
	
}
