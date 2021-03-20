package rs.ac.uns.ftn.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.pki.model.User;

public interface UserRepository extends JpaRepository<User,Long> {

	User findByUsername(String username);

}
