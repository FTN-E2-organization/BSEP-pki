package rs.ac.uns.ftn.bsep.pki.service;

public interface CertificateService {

	void revokeOneCertificate(Long id);
	void revokeCertificateAndChildren(Long id);
}
