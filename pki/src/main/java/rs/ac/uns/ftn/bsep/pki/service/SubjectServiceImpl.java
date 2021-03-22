package rs.ac.uns.ftn.bsep.pki.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.pki.model.Subject;
import rs.ac.uns.ftn.bsep.pki.repository.SubjectRepository;

@Service
public class SubjectServiceImpl implements SubjectService {
	
	private SubjectRepository subjectRepository;
	
	@Autowired
	public SubjectServiceImpl(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	@Override
	public Collection<Subject> getAll() {
		return subjectRepository.findAll();
	}

	@Override
	public Subject getById(Long id) {
		return subjectRepository.getOne(id);
	}

}
