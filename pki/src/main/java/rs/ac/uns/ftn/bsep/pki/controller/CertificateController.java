package rs.ac.uns.ftn.bsep.pki.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.bsep.pki.dto.CertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.BadRequestException;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;
import rs.ac.uns.ftn.bsep.pki.service.CertificateService;
import rs.ac.uns.ftn.bsep.pki.validator.CertificateValidator;
import rs.ac.uns.ftn.bsep.pki.validator.RegExp;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

	private CertificateService certificateService;
	
	@Autowired
	public CertificateController(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
	
	@PutMapping("/{id}/revoke")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> revokeCertificate(@PathVariable Long id){
		try {
			certificateService.revokeOneCertificate(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e) {
			return new ResponseEntity<>("An error occurred while revocation a certificate.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/non-self-signed", consumes = "application/json")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addNonSelfSignedCertificate(@RequestBody CertificateDTO certificateDTO){
		try {
			CertificateDTO issuerCertificateDTO = certificateService.getById(certificateDTO.issuerId);
			if(issuerCertificateDTO.subjectId == certificateDTO.subjectId)
				throw new BadRequestException("Issuer and subject can't be the same in the case of non self-signed certificate.");
			CertificateValidator.addCertificateValidation(certificateDTO);
			certificateService.addCertificate(certificateDTO, false);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (ValidationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/self-signed", consumes = "application/json")
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasAnyRole('ADMIN','SUBJECT')")
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
	@PreAuthorize("hasRole('SUBJECT')")
	public ResponseEntity<?> getBySubjectId(@PathVariable Long id){
		try {
			Collection<CertificateDTO> cDTOs = certificateService.getBySubjectId(id);
			return new ResponseEntity<Collection<CertificateDTO>>(cDTOs, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}/valid")
	@PreAuthorize("hasAnyRole('SUBJECT','ADMIN')")
	public ResponseEntity<?> getValidById(@PathVariable Long id){
		try {
			boolean isValid = certificateService.isCertificateValid(id);
			return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

    @RequestMapping(method = RequestMethod.GET, value = "/download/{id}")
    @PreAuthorize("hasRole('SUBJECT')")
    public ResponseEntity<?> downloadCertificate(HttpServletResponse response, @PathVariable Long id){
        RegExp reg = new RegExp();
        if(reg.isValidId(id)) {
            File certificateForDownload = certificateService.downloadCertificate(id);
            response.setContentType("application/pkix-cert");
            response.setContentLength((int) certificateForDownload.length());
            response.addHeader("Content-Disposition", "attachment; filename="+ certificateForDownload.getName());

            try {
                Files.copy(Paths.get(certificateForDownload.getPath()), response.getOutputStream() );                
                System.out.println("------------------------" + certificateForDownload.getAbsolutePath());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (FileNotFoundException e) {
            	e.printStackTrace();
            } catch (IOException e) {
            	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
            	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }        
        return new ResponseEntity<>("certificate id is not valid", HttpStatus.BAD_REQUEST);
    }
	
}
