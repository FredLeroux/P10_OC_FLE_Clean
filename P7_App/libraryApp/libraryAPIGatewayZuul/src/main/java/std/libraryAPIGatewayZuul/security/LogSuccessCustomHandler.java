package std.libraryAPIGatewayZuul.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;


public class LogSuccessCustomHandler extends SimpleUrlAuthenticationSuccessHandler
 {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
		System.out.println(savedRequest.getRedirectUrl());
		HttpSession session = request.getSession();
		session.setAttribute("code", "123456578");
		setUseReferer(true);
		handle(request, response, authentication);







	}

}
