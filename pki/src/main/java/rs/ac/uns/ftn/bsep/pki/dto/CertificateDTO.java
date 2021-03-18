package rs.ac.uns.ftn.bsep.pki.dto;

public class CertificateDTO {

	private Long id;
    private String subjectId;
    private String issuerId;
    private String startDate;
    private String endDate;
    private boolean isCA;
    private boolean isRevoked;
    private String alias;
    
    private String commonName;
    private String givenName;
    private String surname;
    private String organization;
    private String organizationUnit;
    private String coutryCode;
    private String email;
    private String state;
    
    public CertificateDTO() {}
    
}
