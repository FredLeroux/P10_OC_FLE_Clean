package std.libraryCustomers.service.libraryRolesService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryCustomers.dao.LibraryRolesDAO;
import std.libraryCustomers.entities.LibraryRole;

@Service
public class LibraryRolesServiceImpl implements LibraryRolesService {

	@Autowired
	private LibraryRolesDAO dao;

	@Override
	public Optional<LibraryRole> getRoleById(Integer id) {
		return dao.findById(id);
		}


}
