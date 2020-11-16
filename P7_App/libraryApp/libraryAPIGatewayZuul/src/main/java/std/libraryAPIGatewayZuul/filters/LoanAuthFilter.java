package std.libraryAPIGatewayZuul.filters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.General;

import std.libraryAPIGatewayZuul.filters.filtersMethods.ZuulHeadModifier;
import std.libraryAPIGatewayZuul.filters.filtersMethods.generalAuth.GeneralAuthMethodService;
import std.libraryAPIGatewayZuul.filters.filtersMethods.loanAuth.LoanAuthFilterMethodService;
import std.libraryAPIGatewayZuul.filters.filtersMethods.loanAuth.LoanAuthFilterMethodServiceImpl;
import std.libraryAPIGatewayZuul.security.codeGenerator.CodeGenerator;
import std.libraryAPIGatewayZuul.security.proxies.LibraryCustomerProxy;

@Component
public class LoanAuthFilter extends ZuulFilter {

	@Autowired
	private LoanAuthFilterMethodService loanAuth;

	@Autowired
	private GeneralAuthMethodService generalAuth;


	Logger log = LoggerFactory.getLogger(this.getClass());



	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		generalAuth.isKnown();
		loanAuth.authTokenManagement("loanTracking","token", 60, true);
		loanAuth.authTokenManagement("postPone","token", 60, true);

		return null;
	}




	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}



}
