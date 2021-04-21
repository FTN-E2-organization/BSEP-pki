package rs.ac.uns.ftn.bsep.pki.dto;

public class AddUserDTO {

	public String username;
	public String password;
	
	public AddUserDTO() {}

	public AddUserDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
}
