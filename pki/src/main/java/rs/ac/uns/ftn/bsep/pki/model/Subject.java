package rs.ac.uns.ftn.bsep.pki.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject extends User {

	private static final long serialVersionUID = 3696698179159915486L;
	
	@Column(nullable = false)
	private TypeOfSubject typeOfSubject;
	
	@Column(nullable = false, length = 64)
	private String commonName;
	
	@Column
	private String givenName;
	
	@Column
	private String surname;
	
	@Column(length = 64)
	private String organization;
	
	@Column(length = 64)
	private String organizationUnit;
	
	@Column(length = 2)
	private String countryCode;
	
	@Column
	private String email;
	
	@Column
	private String state;
	
	@Column
	private String locality;

	/*public Long getId() {
		return id;
	}*/

	public TypeOfSubject getTypeOfClient() {
		return typeOfSubject;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getGivenname() {
		return givenName;
	}

	public String getSurname() {
		return surname;
	}

	public String getOrganization() {
		return organization;
	}

	public String getOrganizationUnit() {
		return organizationUnit;
	}

	public String getCoutryCode() {
		return countryCode;
	}

	public String getEmail() {
		return email;
	}

	public String getState() {
		return state;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getLocality() {
		return locality;
	}

}
