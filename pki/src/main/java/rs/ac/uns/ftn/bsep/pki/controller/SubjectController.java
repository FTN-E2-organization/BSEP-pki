package rs.ac.uns.ftn.bsep.pki.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.bsep.pki.dto.SubjectDTO;
import rs.ac.uns.ftn.bsep.pki.mapper.SubjectMapper;
import rs.ac.uns.ftn.bsep.pki.service.SubjectService;

@RestController
@RequestMapping(value = "api/subject")
public class SubjectController {

	private SubjectService subjectService;
	
	@Autowired
	public SubjectController(SubjectService subjectService) {
		this.subjectService = subjectService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAll(){
		try {
			Collection<SubjectDTO> subjectDTOs = SubjectMapper.toSubjectDTOs(subjectService.getAll());
			return new ResponseEntity<>(subjectDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error occurred while getting subjects.", HttpStatus.BAD_REQUEST);
		}
	}
}
