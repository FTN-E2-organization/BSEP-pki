package rs.ac.uns.ftn.bsep.pki.exception;

public class ValidationException extends PkiException {

	private static final long serialVersionUID = 7113650439023195095L;
	
	public ValidationException() {
        super();
    }
	
	public ValidationException(String message) {
        super(message);
    }
}
