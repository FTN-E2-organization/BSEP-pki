package rs.ac.uns.ftn.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.pki.model.Authority;

public interface AuthorityRepository  extends JpaRepository<Authority, Long> {

}
