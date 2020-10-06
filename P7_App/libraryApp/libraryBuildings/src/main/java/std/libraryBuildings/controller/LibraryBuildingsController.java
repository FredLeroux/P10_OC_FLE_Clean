package std.libraryBuildings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBuildings.dto.LibraryBuildingDTO;
import std.libraryBuildings.service.LibraryBuildingService;

@RestController
public class LibraryBuildingsController {

	@Autowired
	LibraryBuildingService service;

	@GetMapping(value = "buildings")
	public List<LibraryBuildingDTO> getBuildings(){
		return service.getAllBuildings();
	}

}
