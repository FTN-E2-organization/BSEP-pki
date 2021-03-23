package rs.ac.uns.ftn.bsep.pki.dto;

public class UserDTO {

	public Long id;
	public String username;
	
	public UserDTO() {}

	public UserDTO(Long id, String username) {
		super();
		this.id = id;
		this.username = username;
	}
	
}
