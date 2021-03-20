package rs.ac.uns.ftn.bsep.pki.mapper;

import java.util.ArrayList;
import java.util.Collection;

import rs.ac.uns.ftn.bsep.pki.dto.SubjectDTO;
import rs.ac.uns.ftn.bsep.pki.model.Subject;

public class SubjectMapper {

	public static SubjectDTO toSubjectDTO(Subject subject) {
		return new SubjectDTO(subject.getId(), subject.getTypeOfClient().name(), subject.getCommonName(), subject.getGivenname(),
							  subject.getSurname(), subject.getOrganization(), subject.getOrganizationUnit(),
							  subject.getEmail(), subject.getCoutryCode(), subject.getState(), subject.getLocality());
	}
	
	public static Collection<SubjectDTO> toSubjectDTOs(Collection<Subject> subjects){
		Collection<SubjectDTO> subjectDTOs = new ArrayList<SubjectDTO>();
		for(Subject subject:subjects) {
			subjectDTOs.add(toSubjectDTO(subject));
		}
		return subjectDTOs;
	}
}
