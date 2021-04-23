package rs.ac.uns.ftn.bsep.pki.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.bsep.pki.model.ConfirmationToken;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

	ConfirmationToken findByConfirmationToken(String confirmationToken);

	@Query(value = "select * from users u, confirmation_token ct where ct.user_id = u.id and u.username=?1 limit 1", nativeQuery = true)
	ConfirmationToken getTokenByUsername(String username);
}
