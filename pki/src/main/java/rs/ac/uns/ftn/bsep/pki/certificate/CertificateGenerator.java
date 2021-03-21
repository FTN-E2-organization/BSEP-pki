package rs.ac.uns.ftn.bsep.pki.certificate;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Vector;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectDirectoryAttributes;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import rs.ac.uns.ftn.bsep.pki.data.IssuerData;
import rs.ac.uns.ftn.bsep.pki.data.SubjectData;

public class CertificateGenerator {
	public CertificateGenerator() {
	}

	public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, boolean isCa,
			Integer keyUsage) throws Exception {
		try {
			// Posto klasa za generisanje sertifikata ne moze da primi direktno privatni
			// kljuc pravi se builder za objekat
			// Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za
			// potpisivanje sertifikata
			// Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje
			// sertifiakta
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			// Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
			builder = builder.setProvider("BC");

			// Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za
			// potpisivanje sertifikata
			ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

			// Postavljaju se podaci za generisanje sertifikata
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
					new BigInteger(subjectData.getSerialNumber()), subjectData.getStartDate(), subjectData.getEndDate(),
					subjectData.getX500name(), subjectData.getPublicKey());

			certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(isCa));

			if (subjectData.getSubjAltName() != null) {
				GeneralName altName = new GeneralName(GeneralName.rfc822Name, subjectData.getSubjAltName());
				GeneralNames subjectAltName = new GeneralNames(altName);
				certGen.addExtension(Extension.subjectAlternativeName, false, subjectAltName);
			}
			if (issuerData.getIssuerAltName() != null) {
				GeneralName issAltName = new GeneralName(GeneralName.rfc822Name, issuerData.getIssuerAltName());
				GeneralNames issuerAlternativeName = new GeneralNames(issAltName);
				certGen.addExtension(Extension.issuerAlternativeName, false, issuerAlternativeName);
			}
			if (keyUsage != null) {
				certGen.addExtension(Extension.keyUsage, true, new KeyUsage(keyUsage));
			}

			Vector<Attribute> attributes = new Vector();
			if (subjectData.getPlaceOfBirth() != null) {
				ASN1EncodableVector dirName = new ASN1EncodableVector();
				dirName.add(new DERUTF8String(subjectData.getPlaceOfBirth()));
				DERSet valueSet = new DERSet(dirName);
				Attribute attr = new Attribute(X509Name.PLACE_OF_BIRTH, valueSet);
				attributes.add(attr);
			}

			if (subjectData.getDateOfBirth() != null) {
				ASN1EncodableVector dirName2 = new ASN1EncodableVector();
				dirName2.add(new DERUTF8String(subjectData.getDateOfBirth().toString()));
				DERSet valueSet2 = new DERSet(dirName2);

				Attribute attr2 = new Attribute(X509Name.DATE_OF_BIRTH, valueSet2);
				attributes.add(attr2);

			}
			if(attributes.size() > 0) {
				SubjectDirectoryAttributes subjectDirAttr = new SubjectDirectoryAttributes(attributes);
				certGen.addExtension(Extension.subjectDirectoryAttributes, false, subjectDirAttr);

			}
			
			// Generise se sertifikat
			X509CertificateHolder certHolder = certGen.build(contentSigner);

			// Builder generise sertifikat kao objekat klase X509CertificateHolder
			// Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se
			// koristi certConverter
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");

			// Konvertuje objekat u sertifikat
			return certConverter.getCertificate(certHolder);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
