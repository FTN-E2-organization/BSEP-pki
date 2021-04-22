package rs.ac.uns.ftn.bsep.pki.security.auth;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Utility klasa za rad sa JSON Web Tokenima
@Component
public class TokenUtils {

	// Izdavac tokena
	@Value("security-project")
	private String APP_NAME;

	// Tajna koju samo backend aplikacija treba da zna kako bi mogla da generise i proveri JWT https://jwt.io/
	@Value("somesecret")
	public String SECRET;

	// Period vazenja
	@Value("100000000")
	private int EXPIRES_IN;

	// Naziv headera kroz koji ce se prosledjivati JWT u komunikaciji server-klijent
	@Value("Authorization")
	private String AUTH_HEADER;

	// Moguce je generisati JWT za razlicite klijente
	private static final String AUDIENCE_WEB = "web";
	
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


	public String generateToken(String username, Long userId, String role) {
		return Jwts.builder()
				.setIssuer(APP_NAME)
				.setSubject(username)
				.setAudience(AUDIENCE_WEB)
				.setIssuedAt(new Date())
				.setExpiration(generateExpirationDate())
				.claim("userId", userId)
				.claim("role", role)
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}


	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + EXPIRES_IN);
	}

	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(new Date());
			refreshedToken = Jwts.builder()
					.setClaims(claims)
					.setExpiration(generateExpirationDate())
					.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}


	public Boolean validateToken(String token, UserDetails userDetails) {		
		final String username = getUsernameFromToken(token);		
		return (username != null && username.equals(userDetails.getUsername()));
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public int getExpiredIn() {
		return EXPIRES_IN;
	}

	// Funkcija za preuzimanje JWT tokena iz zahteva
	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
	
	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
}
