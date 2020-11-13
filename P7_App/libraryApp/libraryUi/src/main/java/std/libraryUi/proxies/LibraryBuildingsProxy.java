package std.libraryUi.proxies;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import std.libraryUi.beans.LibraryBuildingBean;



@FeignClient(name = "gateway-zuul")
@RibbonClient(name = "libraryBuildings")
public interface LibraryBuildingsProxy {

	@GetMapping(value = "/libraryBuildings/buildings")///libraryBuildings
	public List<LibraryBuildingBean> getBuildings();

}
