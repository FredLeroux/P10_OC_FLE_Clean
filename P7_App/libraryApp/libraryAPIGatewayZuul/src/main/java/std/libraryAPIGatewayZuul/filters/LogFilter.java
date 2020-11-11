package std.libraryAPIGatewayZuul.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class LogFilter extends ZuulFilter {

	@Autowired
	HttpServletResponse rep;
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean shouldFilter() {
		System.out.println("rFilter");
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		HttpServletRequest req = RequestContext.getCurrentContext().getRequest();



	      System.out.println("**** Requête interceptée ! L'URL est : {} " + req.getRequestURL()+"****");

	      RequestContext context = RequestContext.getCurrentContext();

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
