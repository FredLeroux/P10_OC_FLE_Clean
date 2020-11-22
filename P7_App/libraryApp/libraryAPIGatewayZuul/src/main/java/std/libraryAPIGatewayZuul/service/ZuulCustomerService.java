package std.libraryAPIGatewayZuul.service;

import std.libraryAPIGatewayZuul.dto.ZuulCustomerLogDTO;

public interface ZuulCustomerService {

	public ZuulCustomerLogDTO findByCustomerEmail(String customerEmail);

	public void addAuth(String userName, String token);

}
