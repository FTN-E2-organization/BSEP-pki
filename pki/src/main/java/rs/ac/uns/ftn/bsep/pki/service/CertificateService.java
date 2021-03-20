package rs.ac.uns.ftn.bsep.pki.service;

import rs.ac.uns.ftn.bsep.pki.dto.AddCertificateDTO;
import rs.ac.uns.ftn.bsep.pki.model.Certificate;

public interface CertificateService {

	void addCertificate(AddCertificateDTO certificateDTO, boolean isSelfSigned) throws Exception;
	void revokeOneCertificate(Long id);
	void revokeCertificateAndChildren(Long id);
	Certificate save(AddCertificateDTO certificateDTO);
}
