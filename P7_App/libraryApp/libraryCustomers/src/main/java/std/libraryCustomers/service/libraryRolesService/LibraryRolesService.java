package std.libraryCustomers.service.libraryRolesService;


import java.util.Optional;

import std.libraryCustomers.entities.LibraryRole;

public interface LibraryRolesService {

	public Optional<LibraryRole> getRoleById(Integer id);

}
