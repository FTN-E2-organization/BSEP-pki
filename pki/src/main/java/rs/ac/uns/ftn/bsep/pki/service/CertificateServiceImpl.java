package rs.ac.uns.ftn.bsep.pki.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.pki.certificate.CertificateGenerator;
import rs.ac.uns.ftn.bsep.pki.data.IssuerData;
import rs.ac.uns.ftn.bsep.pki.data.SubjectData;
import rs.ac.uns.ftn.bsep.pki.dto.CertificateDTO;
import rs.ac.uns.ftn.bsep.pki.exception.BadRequestException;
import rs.ac.uns.ftn.bsep.pki.exception.ValidationException;
import rs.ac.uns.ftn.bsep.pki.keystore.KeyStoreReader;
import rs.ac.uns.ftn.bsep.pki.keystore.KeyStoreWriter;
import rs.ac.uns.ftn.bsep.pki.mapper.CertificateMapper;
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
	
	public void addCertificate(CertificateDTO certificateDTO, boolean isSelfSigned) throws Exception{
		KeyPair keyPairSubject = getKeyPair();
		String keyStorePassword = enviroment.getProperty("spring.keystore.password");
		String keyStorePath = "";
		SubjectData subjectData;
		IssuerData issuerData;
		
		if(isSelfSigned) {
			keyStorePath = enviroment.getProperty("spring.rootca.path");
			certificateDTO.isCA = true;
			
			Certificate savedCertificate =  save(certificateDTO, keyStorePath);
			updateIssuerId(savedCertificate.getId());
			certificateDTO.issuerId = savedCertificate.getId();
			
			String serialNumber = savedCertificate.getId().toString();
			
			subjectData = generateSubjectData(certificateDTO, keyPairSubject.getPublic(), serialNumber);
			issuerData = generateSelfSignedIssuerData(certificateDTO, keyPairSubject.getPrivate());
		}else {
			if(!isValidDate(certificateDTO.issuerId, certificateDTO.startDate, certificateDTO.endDate)) 
				throw new ValidationException("For the selected period, the issuer certificate is not valid.");
			
			if(certificateDTO.isCA) {
				keyStorePath = enviroment.getProperty("spring.ca.path");
			}
			else {
				keyStorePath = enviroment.getProperty("spring.end.path");
			}
			
			Certificate savedCertificate =  save(certificateDTO, keyStorePath);
			String serialNumber = savedCertificate.getId().toString();
			
			subjectData = generateSubjectData(certificateDTO, keyPairSubject.getPublic(), serialNumber);
			
			try {
				issuerData = keyStoreReader.readIssuerFromStore(enviroment.getProperty("spring.rootca.path"), certificateDTO.issuerId.toString(), 
							 keyStorePassword.toCharArray(), keyStorePassword.toCharArray());
			}catch (Exception e) {
				try {
					issuerData = keyStoreReader.readIssuerFromStore(enviroment.getProperty("spring.ca.path"), certificateDTO.issuerId.toString(), 
								 keyStorePassword.toCharArray(), keyStorePassword.toCharArray());
				}
				catch (Exception ex) {
					throw new BadRequestException("Issuer certificate not found.");
				}
			} 
		}
		
		// Generise se sertifikat za subjekta, potpisan od strane issuer-a
		CertificateGenerator certificateGenerator = new CertificateGenerator();
		X509Certificate x509Certificate = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO.isCA, certificateDTO.keyUsage);
		
		// Cuvanje sertifikata u keystore
		keyStoreWriter.loadKeyStore(keyStorePath, keyStorePassword.toCharArray());
		keyStoreWriter.write(x509Certificate.getSerialNumber().toString(), keyPairSubject.getPrivate(), keyStorePassword.toCharArray(), x509Certificate);
		keyStoreWriter.saveKeyStore(keyStorePath, keyStorePassword.toCharArray());
	}
	
	public Certificate save(CertificateDTO certificateDTO, String keystorePath) {
		Certificate certificate = new Certificate();
		
		certificate.setSubjectId(certificateDTO.subjectId);
		certificate.setIssuerId(certificateDTO.issuerId);
		certificate.setIsCA(certificateDTO.isCA);
		certificate.setIsRevoked(false);
		certificate.setStartDate(certificateDTO.startDate);
		certificate.setEndDate(certificateDTO.endDate);
		certificate.setKeystorePath(keystorePath);
		
		return certificateRepository.save(certificate);
	}
	
	public void updateIssuerId(Long certificateId) {
		Certificate certificate = certificateRepository.getOne(certificateId);
		certificate.setIssuerId(certificateId);
		certificateRepository.save(certificate);
	}
	
	private KeyPair getKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private SubjectData generateSubjectData(CertificateDTO certificateDTO, PublicKey subjectPublicKey, String serialNumber) {
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
			builder.addRDN(BCStyle.C, certificateDTO.countryCode);
			builder.addRDN(BCStyle.ST, certificateDTO.state);
			builder.addRDN(BCStyle.L, certificateDTO.locality);
			builder.addRDN(BCStyle.E, certificateDTO.email);
			builder.addRDN(BCStyle.UID, certificateDTO.subjectId.toString());

			SubjectData subjectData = new SubjectData(subjectPublicKey, builder.build(), serialNumber, startDate, endDate);
			subjectData.setDateOfBirth(certificateDTO.dateOfBirth);
			subjectData.setPlaceOfBirth(certificateDTO.placeOfBirth);
			subjectData.setSubjAltName(certificateDTO.subjectAlternativeName);
			return subjectData;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private IssuerData generateSelfSignedIssuerData(CertificateDTO certificateDTO, PrivateKey issuerPrivateKey) {
		
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, certificateDTO.commonName);
		builder.addRDN(BCStyle.GIVENNAME, certificateDTO.givenName);
		builder.addRDN(BCStyle.SURNAME, certificateDTO.surname);
		builder.addRDN(BCStyle.O, certificateDTO.organization);
		builder.addRDN(BCStyle.OU, certificateDTO.organizationUnit);
		builder.addRDN(BCStyle.C, certificateDTO.countryCode);
		builder.addRDN(BCStyle.ST, certificateDTO.state);
		builder.addRDN(BCStyle.L, certificateDTO.locality);
		builder.addRDN(BCStyle.E, certificateDTO.email);
		builder.addRDN(BCStyle.UID, certificateDTO.issuerId.toString());
		
		IssuerData issuerData = new IssuerData(issuerPrivateKey, builder.build());
		issuerData.setIssuerAltName(certificateDTO.issuerAlternativeName);
		return issuerData;
	}
	
	private boolean isValidDate(Long issuerId, LocalDate startDate, LocalDate endDate) {
		Certificate issuerCertificate = certificateRepository.getOne(issuerId);
		
		if(issuerCertificate.getStartDate().isAfter(startDate))
			return false;
		if(endDate.isAfter(issuerCertificate.getEndDate()))
			return false;
		
		return true;
	}

	@Override
	public Collection<CertificateDTO> getAllCA() throws Exception{
		Collection<CertificateDTO> CAs = new ArrayList<CertificateDTO>();
		Collection<Certificate> allCertificates = certificateRepository.findAll();
		for (Certificate certificate : allCertificates) {
			try {
				KeyStoreReader ksr = new KeyStoreReader();
				X509Certificate cer = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());
				
				if(cer.getBasicConstraints()!=-1) {
					if(isCertificateValid(certificate.getId())) {
						CAs.add(CertificateMapper.toCertificateDTO(cer, certificate));
					}
				}
				
			}catch(Exception e) {
				throw new Exception(e.getMessage());
			}
			
		}
		return CAs;
	}

	@Override
	public Collection<CertificateDTO> getAll() throws Exception {
		Collection<CertificateDTO> certificates = new ArrayList<CertificateDTO>();
		Collection<Certificate> allCertificates = certificateRepository.findAll();
		for (Certificate certificate : allCertificates) {
			try {
				KeyStoreReader ksr = new KeyStoreReader();
				X509Certificate cer = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());
				
				certificates.add(CertificateMapper.toCertificateDTO(cer, certificate));			
				
			}catch(Exception e) {
				throw new Exception(e.getMessage());
			}
			
		}
		return certificates;
	}

	@Override
	public CertificateDTO getById(Long id) throws Exception {
		Certificate certificate = certificateRepository.getOne(id);
		try {
			KeyStoreReader ksr = new KeyStoreReader();
			X509Certificate cer = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());
		
			return CertificateMapper.toCertificateDTO(cer, certificate);
			
		}catch(Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Collection<CertificateDTO> getBySubjectId(Long subjectId) throws Exception {
		Collection<CertificateDTO> subjectCertificates = new ArrayList<CertificateDTO>();
		Collection<Certificate> allCertificates = certificateRepository.findAll();
		for (Certificate certificate : allCertificates) {
			try {
				KeyStoreReader ksr = new KeyStoreReader();
				X509Certificate cer = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());
				
				if(certificate.getSubjectId() == subjectId) {
					CertificateDTO certificateDTO = CertificateMapper.toCertificateDTO(cer, certificate);
					subjectCertificates.add(certificateDTO);
				}
				
			}catch(Exception e) {
				throw new Exception(e.getMessage());
			}
			
		}
		return subjectCertificates;
	}
	
	private boolean isDateValid(Long id) throws Exception {
		Certificate certificate = certificateRepository.getOne(id);
		try {
			KeyStoreReader ksr = new KeyStoreReader();
			X509Certificate c = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());
		
			LocalDate startDate = CertificateMapper.toCertificateDTO(c, certificate).startDate;
			LocalDate endDate = CertificateMapper.toCertificateDTO(c, certificate).endDate;
			
			if(endDate.isBefore(LocalDate.now()) || startDate.isAfter(LocalDate.now())) {			
				return false;
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		return true;
	}

	@Override
	public boolean isCertificateValid(Long id) throws Exception {
		Certificate certificate = certificateRepository.getOne(id);
		KeyStoreReader ksr = new KeyStoreReader();
		X509Certificate c = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());
	
		Long parentId = null;
		try {
			parentId = CertificateMapper.toCertificateDTO(c, certificate).issuerId;
		} catch (Exception e) {
			return false;
		}
		if (parentId != id) {
			if (!isCertificateValid(parentId)) {
				return false;
			}
		}
		
		return isDateValid(id);
	}

	public File downloadCertificate(Long id) {
        File downloadFile = null;
        try {
        	java.security.cert.Certificate c = getSecurityCertificate(id);
        	downloadFile  = writeCertificate(c, id);
            FileInputStream inStream = new FileInputStream(downloadFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadFile;
	}

	private File writeCertificate(java.security.cert.Certificate certificate, long certificateId) {
		String home = System.getProperty("user.home");
		File file = new File(home + "\\Downloads\\", "certificate" + certificateId + ".cer");
		FileOutputStream os = null;
		byte[] buf = new byte[0];
		try {
			buf = certificate.getEncoded();
			os = new FileOutputStream(file);
			os.write(buf);
			Writer wr = new OutputStreamWriter(os, Charset.forName("UTF-8"));
			wr.write(new sun.misc.BASE64Encoder().encode(buf));   
			wr.flush();
			os.close();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	private java.security.cert.Certificate getSecurityCertificate(Long id) throws Exception {
		Certificate certificate = certificateRepository.getOne(id);
		try {
			KeyStoreReader ksr = new KeyStoreReader();
			X509Certificate cer = (X509Certificate) ksr.readCertificate(certificate.getKeystorePath(), enviroment.getProperty("spring.keystore.password"), certificate.getId().toString());			
				return cer;
		}catch(Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
