package rs.ac.uns.ftn.bsep.pki.dto;

public class PasswordRequestDTO {
	public String token;
	public String password;
	
	public PasswordRequestDTO() {}

	public PasswordRequestDTO(String token, String password) {
		super();
		this.token = token;
		this.password = password;
	}
	
}
