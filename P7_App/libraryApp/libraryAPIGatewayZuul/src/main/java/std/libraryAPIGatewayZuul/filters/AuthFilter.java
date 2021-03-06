package std.libraryAPIGatewayZuul.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import std.libraryAPIGatewayZuul.filters.filtersMethods.generalAuth.GeneralAuthMethodService;
import std.libraryAPIGatewayZuul.filters.filtersMethods.tokenAuth.TokenAuthFilterMethodService;

@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private TokenAuthFilterMethodService tokenAuth;

    @Autowired
    private GeneralAuthMethodService generalAuth;

    @Override
    public boolean shouldFilter() {
	RequestContext context = RequestContext.getCurrentContext();
	String request = context.getRequest().getRequestURI();
	if (request.contains("loanTracking") || request.contains("postPone") || request.equals("/")
		|| request.contains("/home") || request.contains("/booksList") || request.contains("/bookListTable")
		|| request.contains("/booksListFiltered") || request.contains("/reserveBook")
		|| request.contains("/onGoingReservations") || request.contains("/myReservationsList")) {
	    return true;
	}
	return false;
    }

    @Override
    public Object run() throws ZuulException {
	generalAuth.isKnown("/");
	generalAuth.isKnown("/home");
	generalAuth.isKnown("/reserveBook");
	generalAuth.isKnown("/booksList");
	generalAuth.isKnown("/bookListTable");
	generalAuth.isKnown("/booksListFiltered");
	generalAuth.isKnown("/onGoingReservations");
	tokenAuth.authTokenManagement("loanTracking", "token", 60, true);
	tokenAuth.authTokenManagement("postPone", "token", 60, true);
	tokenAuth.authTokenManagement("reserveBook", "token", 60, true);
	tokenAuth.authTokenManagement("myReservationsList", "token", 60, true);
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
