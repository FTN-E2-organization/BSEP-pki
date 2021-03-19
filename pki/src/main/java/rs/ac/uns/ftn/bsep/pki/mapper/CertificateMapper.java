package rs.ac.uns.ftn.bsep.pki.mapper;

import rs.ac.uns.ftn.bsep.pki.dto.AddCertificateDTO;
import rs.ac.uns.ftn.bsep.pki.model.Certificate;

public class CertificateMapper {
	
	public static Certificate toCertificate(AddCertificateDTO certificateDTO) {
		Certificate certificate = new Certificate();
		
		certificate.setSubjectId(certificateDTO.subjectId);
		certificate.setIssuerId(certificateDTO.issuerId);
		certificate.setIsCA(certificateDTO.isCA);
		certificate.setIsRevoked(false);
		certificate.setStartDate(certificateDTO.startDate);
		certificate.setEndDate(certificateDTO.endDate);
		
		return certificate;
	}
	
}
