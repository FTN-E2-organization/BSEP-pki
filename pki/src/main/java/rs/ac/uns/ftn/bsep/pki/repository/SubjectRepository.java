package rs.ac.uns.ftn.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.bsep.pki.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

	@Query(value = "select * from subject s,users u where u.subject_id=s.id and u.id=?", nativeQuery = true)
	Subject getOneByUserId(Long userId);
}
