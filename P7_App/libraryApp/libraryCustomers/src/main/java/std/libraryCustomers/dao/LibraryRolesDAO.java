package std.libraryCustomers.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryCustomers.entities.LibraryRole;

@Repository
public interface LibraryRolesDAO extends JpaRepository<LibraryRole, Integer> {

	public Optional<LibraryRole> findById(Integer Id);

}
