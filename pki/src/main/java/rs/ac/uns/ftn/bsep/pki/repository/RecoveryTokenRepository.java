package rs.ac.uns.ftn.bsep.pki.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.bsep.pki.model.RecoveryToken;
import rs.ac.uns.ftn.bsep.pki.model.User;

@Repository("recoveryTokenRepository")
public interface RecoveryTokenRepository extends CrudRepository<RecoveryToken, String>{

	RecoveryToken findByRecoveryToken(String recoveryToken);
	RecoveryToken findByUser(User user);
}
