package rs.ac.uns.ftn.bsep.pki.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.bsep.pki.model.User;

public interface UserRepository extends JpaRepository<User,Long> {

	User findByUsername(String username);

	@Query(value = "select * from users u where u.authority_id=2", nativeQuery = true)
	Collection<User> getAllSubjects();
}
