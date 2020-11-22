package std.libraryAPIGatewayZuul.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryAPIGatewayZuul.entities.ZuulCustomer;

@Repository
public interface ZuulCustomerDAO extends JpaRepository<ZuulCustomer, Integer> {

	Optional<ZuulCustomer> findByCustomerEmail(String customerEmail);

}
