package rs.ac.uns.ftn.bsep.pki.config;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter {
	private static Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getRequestURI().equals("/error"))
			return true;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication == null ? null : authentication.getName();
		if (currentPrincipalName == null || currentPrincipalName.equals("anonymousUser"))
			currentPrincipalName = "";
		else
			currentPrincipalName = "User: " + currentPrincipalName;
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		String body = getParameters(request);
		try {
			log.info(request.getMethod() + " " + request.getRequestURI() + " " + ipAddress + " " + currentPrincipalName
					+ " parameters: " + body);
		} catch (Exception exc) {
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (request.getRequestURI().equals("/error"))
			return;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication == null ? null : authentication.getName();
		if (currentPrincipalName == null || currentPrincipalName.equals("anonymousUser"))
			currentPrincipalName = "";
		else
			currentPrincipalName = "User: " + currentPrincipalName;
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		try {
		log.info("Response code: " + response.getStatus() + " " + ipAddress + " " + " " + currentPrincipalName);
		}catch(Exception ex) {
			
		}
	}

	private String getParameters(HttpServletRequest request) {
		StringBuffer posted = new StringBuffer();
		Enumeration<?> e = request.getParameterNames();
		if (e != null && e.hasMoreElements()) {
			posted.append("?");
		}

		while (e.hasMoreElements()) {
			if (posted.length() > 1) {
				posted.append("&");
			}
			String curr = (String) e.nextElement();
			posted.append(curr + "=");
			if (curr.contains("password") || curr.contains("pass") || curr.contains("pwd")) {
				posted.append("*****");
			} else {
				posted.append(request.getParameter(curr));
			}
		}

		return posted.toString();
	}

}

