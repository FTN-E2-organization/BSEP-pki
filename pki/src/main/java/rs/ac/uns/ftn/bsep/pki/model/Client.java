package rs.ac.uns.ftn.bsep.pki.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(nullable = false)
	TypeOfClient typeOfClient;
	
	@Column(nullable = false, length = 64)
	String commonName;
	
	@Column
	String givenName;
	
	@Column
	String surname;
	
	@Column(length = 64)
	String organization;
	
	@Column(length = 64)
	String organizationUnit;
	
	@Column(length = 2)
	String coutryCode;
	
	@Column
	String email;
	
	@Column
	String state;

	public Long getId() {
		return id;
	}

	public TypeOfClient getTypeOfClient() {
		return typeOfClient;
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
		return coutryCode;
	}

	public String getEmail() {
		return email;
	}

	public String getState() {
		return state;
	}
	
	
}