package rs.ac.uns.ftn.bsep.pki.mapper;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

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
			X500Name subjName = new JcaX509CertificateHolder(cert).getSubject();
			X500Name iss = new JcaX509CertificateHolder(cert).getIssuer();

			RDN cn = subjName.getRDNs(BCStyle.CN)[0];
			String cname = IETFUtils.valueToString(cn.getFirst().getValue());
			cDTO.commonName = cname;
			
			RDN gn = subjName.getRDNs(BCStyle.GIVENNAME)[0];
			String gname = IETFUtils.valueToString(gn.getFirst().getValue());
			cDTO.givenName = gname;
			
			RDN sn = subjName.getRDNs(BCStyle.SURNAME)[0];
			String sname = IETFUtils.valueToString(sn.getFirst().getValue());
			cDTO.surname = sname;

			RDN on = subjName.getRDNs(BCStyle.O)[0];
			String oname = IETFUtils.valueToString(on.getFirst().getValue());
			cDTO.organization = oname;

			RDN oun = subjName.getRDNs(BCStyle.OU)[0];
			String ouname = IETFUtils.valueToString(oun.getFirst().getValue());
			cDTO.organizationUnit = ouname;

			RDN con = subjName.getRDNs(BCStyle.C)[0];
			String conname = IETFUtils.valueToString(con.getFirst().getValue());
			cDTO.countryCode = conname;

			RDN loc = subjName.getRDNs(BCStyle.L)[0];
			String locname = IETFUtils.valueToString(loc.getFirst().getValue());
			cDTO.locality = locname;

			RDN sta = subjName.getRDNs(BCStyle.ST)[0];
			String staname = IETFUtils.valueToString(sta.getFirst().getValue());
			cDTO.state = staname;

			RDN en = subjName.getRDNs(BCStyle.E)[0];
			String emname = IETFUtils.valueToString(en.getFirst().getValue());
			cDTO.email = emname;

			RDN subjectId = iss.getRDNs(BCStyle.UID)[0];
			String subId = IETFUtils.valueToString(subjectId.getFirst().getValue());
			cDTO.subjectId = Long.parseLong(subId);
			
			cDTO.issuerId = certificate.getIssuerId();

			cDTO.id = Long.parseLong(String.valueOf((cert).getSerialNumber()));
			cDTO.startDate = certificate.getStartDate();
			cDTO.endDate = certificate.getEndDate();
			
			if(cert.getBasicConstraints()!=-1) {
				cDTO.isCA = true;
			}
			else {
				cDTO.isCA = false;
			}
			
			return cDTO;

		} catch (CertificateEncodingException e) {
			return null;
		}
	}
}
