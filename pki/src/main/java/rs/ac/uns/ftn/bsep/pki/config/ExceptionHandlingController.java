package rs.ac.uns.ftn.bsep.pki.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionHandlingController {
	private static Logger l = LogManager.getLogger(ExceptionHandlingController.class);

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> defaultExceptionHandler(Exception e) {
		try {
			l.error("EXCEPTION: " + e.getMessage());
		} catch (Exception exc) {

		}
		return ResponseEntity.status(500).body(e.getMessage());
	}
}
