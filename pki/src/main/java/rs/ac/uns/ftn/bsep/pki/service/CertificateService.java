package rs.ac.uns.ftn.bsep.pki.service;

import java.io.File;
import java.util.Collection;

import rs.ac.uns.ftn.bsep.pki.dto.CertificateDTO;
import rs.ac.uns.ftn.bsep.pki.model.Certificate;

public interface CertificateService {

	void addCertificate(CertificateDTO certificateDTO, boolean isSelfSigned) throws Exception;
	void revokeOneCertificate(Long id);
	void revokeCertificateAndChildren(Long id);
	Certificate save(CertificateDTO certificateDTO, String keystorePath);
	Collection<CertificateDTO> getAllCA() throws Exception;
	Collection<CertificateDTO> getAll() throws Exception;
	CertificateDTO getById(Long id) throws Exception;
	Collection<CertificateDTO> getBySubjectId(Long subjectId) throws Exception;
	boolean isCertificateValid(Long id) throws Exception;
	File downloadCertificate(Long id);

}
