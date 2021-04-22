package rs.ac.uns.ftn.bsep.pki.security.auth;

//DTO koji enkapsulira generisani JWT i njegovo trajanje koji se vracaju klijentu
public class UserTokenState {
	
    private String accessToken;
    private Long expiresIn;
    private String authority;

    public UserTokenState() {
        super();
    }

    public UserTokenState(String accessToken, long expiresIn, String authority) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.authority = authority;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
    
}
