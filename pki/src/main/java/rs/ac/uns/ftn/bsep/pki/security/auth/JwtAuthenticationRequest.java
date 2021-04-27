package rs.ac.uns.ftn.bsep.pki.security.auth;

//DTO za login
public class JwtAuthenticationRequest {

    private String username;
    private String password;
    private String ipAddress;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password, String ipAddress) {
        this.setUsername(username);
        this.setPassword(password);
        this.setIpAddress(ipAddress);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}	
	
}
