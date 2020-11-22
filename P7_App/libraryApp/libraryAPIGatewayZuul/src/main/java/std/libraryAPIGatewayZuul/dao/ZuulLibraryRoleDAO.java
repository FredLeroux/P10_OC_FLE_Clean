package std.libraryAPIGatewayZuul.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import std.libraryAPIGatewayZuul.entities.ZuulLibraryRole;

public interface ZuulLibraryRoleDAO extends JpaRepository<ZuulLibraryRole, Integer>{

}
