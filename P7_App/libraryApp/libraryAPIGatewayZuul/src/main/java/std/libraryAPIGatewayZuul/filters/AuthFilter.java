package std.libraryAPIGatewayZuul.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

import std.libraryAPIGatewayZuul.filters.filtersMethods.generalAuth.GeneralAuthMethodService;
import std.libraryAPIGatewayZuul.filters.filtersMethods.tokenAuth.TokenAuthFilterMethodService;

@Component
public class AuthFilter extends ZuulFilter {

	@Autowired
	private TokenAuthFilterMethodService tokenAuth;

	@Autowired
	private GeneralAuthMethodService generalAuth;


	Logger log = LoggerFactory.getLogger(this.getClass());



	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		generalAuth.isKnown("/");
		tokenAuth.authTokenManagement("loanTracking","token", 60, true);
		tokenAuth.authTokenManagement("postPone","token", 60, true);
		return null;
	}




	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}



}
