package rs.ac.uns.ftn.bsep.pki.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.bsep.pki.certificate.CertificateGenerator;
import rs.ac.uns.ftn.bsep.pki.data.IssuerData;
import rs.ac.uns.ftn.bsep.pki.data.SubjectData;
import rs.ac.uns.ftn.bsep.pki.dto.AddCertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;
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

	@Override
	public void addSelfSignedCertificate(AddCertificateDTO certificateDTO) throws Exception{
		if(!isValidDate(certificateDTO.issuerId, certificateDTO.startDate, certificateDTO.endDate)) 
			throw new ValidationException("For the selected period, the issuer certificate is not valid.");
		
	}

	@Override
	public void addNonSelfSignedCertificate(AddCertificateDTO certificateDTO) throws Exception{
		if(!isValidDate(certificateDTO.issuerId, certificateDTO.startDate, certificateDTO.endDate)) 
			throw new ValidationException("For the selected period, the issuer certificate is not valid.");
		
		KeyPair keyPairSubject = getKeyPair();
		String keyStorePassword = enviroment.getProperty("spring.keystore.password");
		String keyStorePath;
		if(certificateDTO.isCA)
			keyStorePath = enviroment.getProperty("keystore/ca.jks");
		else
			keyStorePath = enviroment.getProperty("keystore/end_entity.jks");
		
		Certificate savedCertificate =  save(certificateDTO);
		String serialNumber = savedCertificate.getId().toString();
		
		SubjectData subjectData = generateSubjectData(certificateDTO, keyPairSubject.getPublic(), serialNumber);
		IssuerData issuerData = keyStoreReader.readIssuerFromStore(keyStorePath, certificateDTO.issuerId.toString(), 
								keyStorePassword.toCharArray(), keyStorePassword.toCharArray());
		
		// Generise se sertifikat za subjekta, potpisan od strane issuer-a
		CertificateGenerator certificateGenerator = new CertificateGenerator();
		X509Certificate x509Certificate = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO.isCA);
		
		// Cuvanje sertifikata u keystore
		keyStoreWriter.loadKeyStore(keyStorePath, keyStorePassword.toCharArray());
		keyStoreWriter.write(x509Certificate.getSerialNumber().toString(), keyPairSubject.getPrivate(), keyStorePassword.toCharArray(), x509Certificate);
		keyStoreWriter.saveKeyStore(keyStorePath, keyStorePassword.toCharArray());
		
	}
	
	public Certificate save(AddCertificateDTO certificateDTO) {
		Certificate certificate = new Certificate();
		
		certificate.setSubjectId(certificateDTO.subjectId);
		certificate.setIssuerId(certificateDTO.issuerId);
		certificate.setIsCA(certificateDTO.isCA);
		certificate.setIsRevoked(false);
		certificate.setStartDate(certificateDTO.startDate);
		certificate.setEndDate(certificateDTO.endDate);
		
		return certificateRepository.save(certificate);
	}
	
	private KeyPair getKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private SubjectData generateSubjectData(AddCertificateDTO certificateDTO, PublicKey subjectPublicKey, String serialNumber) {
		try {
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse(certificateDTO.startDate.toString());
			Date endDate = iso8601Formater.parse(certificateDTO.endDate.toString());

			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
			builder.addRDN(BCStyle.CN, certificateDTO.commonName);
			builder.addRDN(BCStyle.GIVENNAME, certificateDTO.givenName);
			builder.addRDN(BCStyle.SURNAME, certificateDTO.surname);
			builder.addRDN(BCStyle.O, certificateDTO.organization);
			builder.addRDN(BCStyle.OU, certificateDTO.organizationUnit);
			builder.addRDN(BCStyle.C, certificateDTO.coutryCode);
			builder.addRDN(BCStyle.ST, certificateDTO.state);
			builder.addRDN(BCStyle.L, certificateDTO.locality);
			builder.addRDN(BCStyle.E, certificateDTO.email);
			builder.addRDN(BCStyle.UID, certificateDTO.subjectId.toString());

			return new SubjectData(subjectPublicKey, builder.build(), serialNumber, startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean isValidDate(Long issuerId, LocalDate startDate, LocalDate endDate) {
		Certificate issuerCertificate = certificateRepository.getOne(issuerId);
		
		if(issuerCertificate.getStartDate().isAfter(startDate))
			return false;
		if(endDate.isAfter(issuerCertificate.getEndDate()))
			return false;
		
		return true;
	}

}
