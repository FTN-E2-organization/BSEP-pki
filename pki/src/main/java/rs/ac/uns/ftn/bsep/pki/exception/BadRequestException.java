package rs.ac.uns.ftn.bsep.pki.exception;

public class BadRequestException extends PkiException {

	private static final long serialVersionUID = 7113650439023195095L;
	
	public BadRequestException() {
        super();
    }
	
	public BadRequestException(String message) {
        super(message);
    }
}
