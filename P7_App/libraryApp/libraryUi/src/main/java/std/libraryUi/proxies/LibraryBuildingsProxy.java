package std.libraryUi.proxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import std.libraryUi.beans.LibraryBuildingBean;



@FeignClient(name = "libraryBuildings", url = "localhost:9002")
public interface LibraryBuildingsProxy {

	@GetMapping(value = "buildings")
	public List<LibraryBuildingBean> getBuildings();

}
