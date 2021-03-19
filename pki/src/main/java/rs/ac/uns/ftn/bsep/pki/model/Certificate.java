package rs.ac.uns.ftn.bsep.pki.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Certificate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Boolean isRevoked;
	
	@Column(nullable = false)
	private Boolean isCA;

	@Column(nullable = false, unique = true)
	private String alias;

	public Long getId() {
		return id;
	}

	public Boolean getIsRevoked() {
		return isRevoked;
	}

	public String getAlias() {
		return alias;
	}

	public Boolean getIsCA() {
		return isCA;
	}

	public void setIsRevoked(Boolean isRevoked) {
		this.isRevoked = isRevoked;
	}
}
