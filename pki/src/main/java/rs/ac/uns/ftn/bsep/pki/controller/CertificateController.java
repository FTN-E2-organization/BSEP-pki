package rs.ac.uns.ftn.bsep.pki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.bsep.pki.service.CertificateService;

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
			return new ResponseEntity<>("An error occurred while revocation an certificate.", HttpStatus.BAD_REQUEST);
		}
	}
}
