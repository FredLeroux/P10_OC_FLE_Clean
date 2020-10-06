package std.libraryBuildings.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryBuildings.entities.LibraryBuilding;

@Repository
public interface LibraryBuildingsDAO extends JpaRepository<LibraryBuilding, Integer> {

}
