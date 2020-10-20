package std.libraryUi.security.configClass;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;

public class UnauthentificatedRequest implements AuthenticationEntryPoint {


	 @Override
	    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {


	            System.out.println("request= ");
	          response.setStatus(401);
	            System.out.println(response.getStatus());
	            response.sendRedirect("/login");



	    }

}
