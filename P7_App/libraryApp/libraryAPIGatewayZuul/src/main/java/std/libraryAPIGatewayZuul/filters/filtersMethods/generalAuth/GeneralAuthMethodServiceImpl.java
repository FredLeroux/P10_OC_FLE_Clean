package std.libraryAPIGatewayZuul.filters.filtersMethods.generalAuth;

import org.springframework.stereotype.Service;

import std.libraryAPIGatewayZuul.filters.filtersMethods.ZuulHeadModifier;

@Service
public class GeneralAuthMethodServiceImpl extends ZuulHeadModifier implements GeneralAuthMethodService {

	@Override
	public void isKnown(String urL) {
		if (isRequestEquals(urL)) {
			addToHeader("isKnown", isUserAuthenticated().toString());
		}
	}

}
