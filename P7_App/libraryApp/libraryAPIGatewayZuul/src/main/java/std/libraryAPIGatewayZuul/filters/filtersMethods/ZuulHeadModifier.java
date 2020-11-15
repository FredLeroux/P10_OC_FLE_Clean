package std.libraryAPIGatewayZuul.filters.filtersMethods;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.context.RequestContext;

public abstract class ZuulHeadModifier extends AuthenticationCheck{

	public final void addToHeader(String headName,String value) {
		requestContext().addZuulRequestHeader(headName, value);
	}

	public final Boolean isRequestContains(String regex) {
		return request().getRequestURI().contains(regex);
	}



	private HttpServletRequest request() {
		return requestContext().getRequest();
	}

	private RequestContext requestContext() {
		return RequestContext.getCurrentContext();
	}

}
