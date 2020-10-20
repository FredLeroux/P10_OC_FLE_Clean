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

import std.libraryUi.beans.LibraryCustomerLogBean;
import std.libraryUi.proxies.LibraryCustomerProxy;

@Service
public class LibraryUserDetailsService implements UserDetailsService {

	@Autowired
	private LibraryCustomerProxy customerLog;

	@Override
	public UserDetails loadUserByUsername(@RequestParam(value = "username") String user) throws UsernameNotFoundException {
		Optional<LibraryCustomerLogBean> logDetails = customerLog.getCustomer(user);
		if (!logDetails.isPresent()) {
			System.out.println("notfound");
			throw new UsernameNotFoundException(user);
		}
		UserDetails userD = (UserDetails) new User(logDetails.get().getCustomerEmail(),
				logDetails.get().getCustomerPassword(), authority(logDetails.get()));
		return userD;
	}

	private List<GrantedAuthority> authority(LibraryCustomerLogBean logDetails) {
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority auth = new SimpleGrantedAuthority(logDetails.getRole().getRoleType());
		grantList.add(auth);
		return grantList;
	}

}
