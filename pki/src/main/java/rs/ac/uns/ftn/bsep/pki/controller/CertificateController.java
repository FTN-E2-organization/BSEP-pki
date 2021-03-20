package rs.ac.uns.ftn.bsep.pki.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.bsep.pki.dto.CertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;
import rs.ac.uns.ftn.bsep.pki.service.CertificateService;
import rs.ac.uns.ftn.bsep.pki.validator.CertificateValidator;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

	private CertificateService certificateService;
	
	@Autowired
	public CertificateController(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
	
	@PutMapping("/{id}/revoke")
	public ResponseEntity<?> acceptRequest(@PathVariable Long id){
		try {
			certificateService.revokeCertificateAndChildren(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e) {
			return new ResponseEntity<>("An error occurred while revocation a certificate.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/non-self-signed", consumes = "application/json")
	public ResponseEntity<?> addNonSelfSignedCertificate(@RequestBody CertificateDTO certificateDTO){
		try {
			CertificateValidator.addCertificateValidation(certificateDTO);
			certificateService.addCertificate(certificateDTO, false);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (ValidationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/self-signed", consumes = "application/json")
	public ResponseEntity<?> addSelfSignedCertificate(@RequestBody CertificateDTO certificateDTO){
		try {
			CertificateValidator.addCertificateValidation(certificateDTO);
			certificateService.addCertificate(certificateDTO, true);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (ValidationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/ca")
	public ResponseEntity<?> getAllCAs(){
		try {
			Collection<CertificateDTO> caDTOs = certificateService.getAllCA();
			return new ResponseEntity<Collection<CertificateDTO>>(caDTOs, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAll(){
		try {
			Collection<CertificateDTO> cDTOs = certificateService.getAll();
			return new ResponseEntity<Collection<CertificateDTO>>(cDTOs, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			CertificateDTO cDTO = certificateService.getById(id);
			return new ResponseEntity<CertificateDTO>(cDTO, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}/subject")
	public ResponseEntity<?> getBySubjectId(@PathVariable Long id){
		try {
			Collection<CertificateDTO> cDTOs = certificateService.getBySubjectId(id);
			return new ResponseEntity<Collection<CertificateDTO>>(cDTOs, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
