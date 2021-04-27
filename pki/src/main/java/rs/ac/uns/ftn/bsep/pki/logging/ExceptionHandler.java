package rs.ac.uns.ftn.bsep.pki.logging;

import java.lang.Thread.UncaughtExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExceptionHandler implements UncaughtExceptionHandler {
	
	private static Logger l=LogManager.getLogger(ExceptionHandler.class);
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		try {
			l.error("Exception: "+e.getMessage()+ "in Thread: "+t.getName());
		}
		catch(Exception ex) {			
		}
	}
}
