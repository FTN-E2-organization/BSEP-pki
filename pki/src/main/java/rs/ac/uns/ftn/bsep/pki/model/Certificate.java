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
	
	@Column(nullable = false)
	private Long subjectId;
	
	@Column(nullable = false)
	private Long issuerId;

	public Long getId() {
		return id;
	}

	public Boolean getIsRevoked() {
		return isRevoked;
	}

	public Boolean getIsCA() {
		return isCA;
	}

	public void setIsRevoked(Boolean isRevoked) {
		this.isRevoked = isRevoked;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public Long getIssuerId() {
		return issuerId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIsCA(Boolean isCA) {
		this.isCA = isCA;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public void setIssuerId(Long issuerId) {
		this.issuerId = issuerId;
	}
	
}
