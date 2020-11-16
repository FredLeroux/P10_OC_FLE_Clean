package std.libraryAPIGatewayZuul.filters.filtersMethods.loanAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.netflix.zuul.context.RequestContext;

import std.libraryAPIGatewayZuul.filters.filtersMethods.ZuulHeadModifier;
import std.libraryAPIGatewayZuul.security.codeGenerator.CodeGenerator;
import std.libraryAPIGatewayZuul.security.proxies.LibraryCustomerProxy;

@Service
public class LoanAuthFilterMethodServiceImpl extends ZuulHeadModifier implements LoanAuthFilterMethodService {

	@Autowired
	private LibraryCustomerProxy customer;

	/**
	 * @param urlRegex the url being filtered
	 * @param headerName
	 * @param size
	 * @param isSymbolAccepted
	 * @apiNote add to filtered url's header a token(generated using size and isSymbolAccepted)
	 *          named as headerName
	 */
	@Override
	public void authTokenManagement(String urlRegex,String headerName, Integer size, Boolean isSymbolAccepted) {
		if (isUserAuthenticated()) {
			if (isRequestContains(urlRegex)) {
				String token = token(size, isSymbolAccepted);
				customer.authToken(userName(), "token");
				addToHeader(headerName, "token");
				if(urlRegex.equals("postPone")) {
					System.out.println("auth post pone");
				}else {
					System.out.println("auth loantracking");
				}
			}
		}

	}



	private String token(Integer size, Boolean isSymbolAccepted) {
		return new CodeGenerator(size, isSymbolAccepted).toString();
	}

	private String userName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
