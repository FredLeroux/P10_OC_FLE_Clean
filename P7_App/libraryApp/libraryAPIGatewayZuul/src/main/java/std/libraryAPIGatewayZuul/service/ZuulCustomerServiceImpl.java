package std.libraryAPIGatewayZuul.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryAPIGatewayZuul.dao.ZuulCustomerDAO;
import std.libraryAPIGatewayZuul.dao.ZuulLibraryRoleDAO;
import std.libraryAPIGatewayZuul.dto.ZuulCustomerLogDTO;
import std.libraryAPIGatewayZuul.dto.ZuulLibraryRoleDTO;
import std.libraryAPIGatewayZuul.entities.ZuulCustomer;
import std.libraryAPIGatewayZuul.entities.ZuulLibraryRole;


@Service
public class ZuulCustomerServiceImpl implements ZuulCustomerService {

	@Autowired
	private ZuulCustomerDAO zullCustomerDAO;

	@Autowired
	private ZuulLibraryRoleDAO zuulLibraryRoleDAO;

	private ModelMapper mapper = new ModelMapper();
	private ZuulCustomerLogDTO zuulCustomerLogDTO = new ZuulCustomerLogDTO();
	private ZuulLibraryRoleDTO zuulLibraryRoleDTO = new ZuulLibraryRoleDTO();

	@Override
	public ZuulCustomerLogDTO findByCustomerEmail(String customerEmail) {
		Optional<ZuulCustomer> optCustomer = zullCustomerDAO.findByCustomerEmail(customerEmail);
		if(optCustomer.isPresent()) {
		ZuulCustomerLogDTO dto = mappingTo(optCustomer.get(),zuulCustomerLogDTO);
		ZuulLibraryRoleDTO roleDTO = mappingTo(optCustomer.get().getRole(),zuulLibraryRoleDTO);
		dto.setRole(roleDTO);
		return dto;
		}
		//TODO exception
		return null;
	}

	@Override
	public void addAuth(String userName, String token) {
		Optional<ZuulCustomer> optCustomer = zullCustomerDAO.findByCustomerEmail(userName);
		if(optCustomer.isPresent()) {
			ZuulCustomer customer = optCustomer.get();
			customer.setCustomerAuthToken(token);
			zullCustomerDAO.save(customer);
	}
	}


	@SuppressWarnings("unchecked")
	private<O extends Object> O mappingTo(Object source, O destination) {
		return (O) mapper.map(source, destination.getClass());
	}

}
