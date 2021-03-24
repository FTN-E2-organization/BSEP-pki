package rs.ac.uns.ftn.bsep.pki.mapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.LinkedList;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import rs.ac.uns.ftn.bsep.pki.dto.CertificateDTO;
import rs.ac.uns.ftn.bsep.pki.model.Certificate;

public class CertificateMapper {

	public static CertificateDTO toCertificateDTO(X509Certificate cert, Certificate certificate) {

		CertificateDTO cDTO = new CertificateDTO();
		try {
			X500Name subj = new JcaX509CertificateHolder(cert).getSubject();

			RDN cn = subj.getRDNs(BCStyle.CN)[0];
			String cname = IETFUtils.valueToString(cn.getFirst().getValue());
			cDTO.commonName = cname;

			RDN gn = subj.getRDNs(BCStyle.GIVENNAME)[0];
			String gname = IETFUtils.valueToString(gn.getFirst().getValue());
			cDTO.givenName = gname;

			RDN sn = subj.getRDNs(BCStyle.SURNAME)[0];
			String sname = IETFUtils.valueToString(sn.getFirst().getValue());
			cDTO.surname = sname;

			RDN on = subj.getRDNs(BCStyle.O)[0];
			String oname = IETFUtils.valueToString(on.getFirst().getValue());
			cDTO.organization = oname;

			RDN oun = subj.getRDNs(BCStyle.OU)[0];
			String ouname = IETFUtils.valueToString(oun.getFirst().getValue());
			cDTO.organizationUnit = ouname;

			RDN con = subj.getRDNs(BCStyle.C)[0];
			String conname = IETFUtils.valueToString(con.getFirst().getValue());
			cDTO.countryCode = conname;

			RDN loc = subj.getRDNs(BCStyle.L)[0];
			String locname = IETFUtils.valueToString(loc.getFirst().getValue());
			cDTO.locality = locname;

			RDN sta = subj.getRDNs(BCStyle.ST)[0];
			String staname = IETFUtils.valueToString(sta.getFirst().getValue());
			cDTO.state = staname;

			RDN en = subj.getRDNs(BCStyle.E)[0];
			String emname = IETFUtils.valueToString(en.getFirst().getValue());
			cDTO.email = emname;

			cDTO.subjectId = certificate.getSubjectId();
			cDTO.issuerId = certificate.getIssuerId();

			cDTO.id = Long.parseLong(String.valueOf((cert).getSerialNumber()));
			cDTO.startDate = certificate.getStartDate();
			cDTO.endDate = certificate.getEndDate();
			cDTO.isRevoked = certificate.getIsRevoked();

			if (cert.getBasicConstraints() != -1) {
				cDTO.isCA = true;
			} else {
				cDTO.isCA = false;
			}
			try {
				cDTO.issuerAlternativeName = cert.getIssuerAlternativeNames().stream().findFirst().get().get(1).toString();
			} catch (Exception e) { }
			try {
				cDTO.subjectAlternativeName = cert.getSubjectAlternativeNames().stream().findFirst().get().get(1).toString();
			} catch (Exception e) {}
			
			boolean[] keyUsages = cert.getKeyUsage();
			cDTO.keyUsage = new LinkedList<>();
			for (int i = 0; i < keyUsages.length; i++) {
				if (keyUsages[i])
					cDTO.keyUsage.add(i);
			}
			try {
				cDTO.placeOfBirth= getExtensionValue(cert, "2.5.29.9",0); 
				String a = getExtensionValue(cert, "2.5.29.9",1); 
				cDTO.dateOfBirth=LocalDate.parse(a);
				
			} catch (Exception e) { }

			return cDTO;

		} catch (CertificateEncodingException e) {
			return null;
		}
	}

	private static String getExtensionValue(X509Certificate X509Certificate, String oid,int index) throws IOException {
		String decoded = null;
		byte[] extensionValue = X509Certificate.getExtensionValue(oid);

		if (extensionValue != null) {
			ASN1Primitive derObject = toDERObject(extensionValue);
			if (derObject instanceof DEROctetString) {
				DEROctetString derOctetString = (DEROctetString) derObject;

				derObject = toDERObject(derOctetString.getOctets());
				if (derObject instanceof DLSequence) {
					ASN1Encodable s = ((DLSequence) derObject).getObjectAt(index);
					decoded=s.toASN1Primitive().toString().split(",")[1].trim().replace("]" , "").replace("[" , "");
				}
			}
		}
		return decoded;
	}

	private static ASN1Primitive toDERObject(byte[] data) throws IOException {
		ByteArrayInputStream inStream = new ByteArrayInputStream(data);
		ASN1InputStream asnInputStream = new ASN1InputStream(inStream);

		return asnInputStream.readObject();
	}
}
