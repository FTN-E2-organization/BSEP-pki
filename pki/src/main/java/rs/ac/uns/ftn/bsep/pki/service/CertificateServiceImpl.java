package rs.ac.uns.ftn.bsep.pki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.bsep.pki.keystore.KeyStoreReader;
import rs.ac.uns.ftn.bsep.pki.keystore.KeyStoreWriter;
import rs.ac.uns.ftn.bsep.pki.model.Certificate;
import rs.ac.uns.ftn.bsep.pki.repository.CertificateRepository;

@Service
public class CertificateServiceImpl implements CertificateService{
	
	private CertificateRepository certificateRepository;
	private KeyStoreWriter keyStoreWriter;
	private KeyStoreReader keyStoreReader;
	private Environment enviroment;
	
	@Autowired
	public CertificateServiceImpl(CertificateRepository certificateRepository, KeyStoreWriter keyStoreWriter, 
								  KeyStoreReader keyStoreReader, Environment environment) {
		this.certificateRepository = certificateRepository;
		this.keyStoreWriter = keyStoreWriter;
		this.keyStoreReader = keyStoreReader;
		this.enviroment = environment;
	}

	@Override
	public void revokeOneCertificate(Long id) {
		Certificate certificate = certificateRepository.getOne(id);
		certificate.setIsRevoked(true);
		certificateRepository.save(certificate);	
	}

	@Override
	public void revokeCertificateAndChildren(Long id) {
		Certificate certificate = certificateRepository.getOne(id);
		if(certificate.getIsCA()) {
			// TODO dodati nakon sto odradimo cuvanje sve 3 vrste setifikata u keyStoreFile (konvencija imenovanja)
		}else {
			revokeOneCertificate(id);
		}	
	}

}
