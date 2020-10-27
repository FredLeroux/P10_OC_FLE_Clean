package std.libraryUi.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryCustomers.controller.LibraryCustomerController;
import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryUi.beans.LibraryCustomerLogBean;

@Service
public class LibraryUserDetailsService implements UserDetailsService {


	private LibraryCustomerController customerLog;

	@Override
	public UserDetails loadUserByUsername(@RequestParam(value = "username") String user) throws UsernameNotFoundException {
		CustomerLogDTO logDetails = customerLog.getCustomer(user);
		/*if (!logDetails.isPresent()) {
			throw new UsernameNotFoundException(user);
		}*/
		UserDetails userD = (UserDetails) new User(logDetails.getCustomerEmail(),
				logDetails.getCustomerPassword(), authority(logDetails.getRole().getRoleType()));
		return userD;
	}

	private List<GrantedAuthority> authority(String logDetails) {
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority auth = new SimpleGrantedAuthority(logDetails);
		grantList.add(auth);
		return grantList;
	}

}
