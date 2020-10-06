package std.libraryBuildings.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryBuildings.dao.LibraryBuildingsDAO;
import std.libraryBuildings.dto.LibraryBuildingDTO;
import std.libraryBuildings.entities.LibraryBuilding;

@Service
public class LibraryBuildingServiceImpl implements LibraryBuildingService {

	@Autowired
	LibraryBuildingsDAO dao;

	private ModelMapper modelMapper = new ModelMapper();
	private LibraryBuildingDTO libraryBuildingDTO = new LibraryBuildingDTO();

	@Override
	public List<LibraryBuildingDTO> getAllBuildings() {
		return dao.findAll().stream().map(o->convertToDTO(o)).collect(Collectors.toList());
	}

	private LibraryBuildingDTO convertToDTO(LibraryBuilding building){
		return modelMapper.map(building, libraryBuildingDTO.getClass());
	}



}
