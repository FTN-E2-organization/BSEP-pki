package rs.ac.uns.ftn.bsep.pki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.bsep.pki.dto.AddCertificateDTO;
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
	public ResponseEntity<?> addNonSelfSignedCertificate(@RequestBody AddCertificateDTO certificateDTO){
		try {
			CertificateValidator.addCertificateValidation(certificateDTO);
			certificateService.addNonSelfSignedCertificate(certificateDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (ValidationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			//return new ResponseEntity<>("An error occurred while creating a certificate.", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/self-signed", consumes = "application/json")
	public ResponseEntity<?> addSelfSignedCertificate(@RequestBody AddCertificateDTO certificateDTO){
		try {
			CertificateValidator.addCertificateValidation(certificateDTO);
			certificateService.addSelfSignedCertificate(certificateDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (ValidationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			//return new ResponseEntity<>("An error occurred while creating a certificate.", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
