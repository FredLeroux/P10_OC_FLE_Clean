package std.libraryUi.proxies;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryUi.beans.BookKindsBean;
import std.libraryUi.beans.BooksBean;

@FeignClient(name = "gateway-zuul",url = "localhost:9005")
@RibbonClient(name = "/libraryBookCase")
public interface LibraryBookCaseProxy {

	@GetMapping(value = "/libraryBookCase/books")///libraryBookCase
	public List<BooksBean> books();

	@GetMapping(value = "/libraryBookCase/kinds")
	public List<BookKindsBean> kinds();

	@GetMapping(value = "/libraryBookCase/buildingFiltered")
	public List<BooksBean> booksBuildingFiltered(
			@RequestParam(name = "libraryBuilding") Integer libraryBuilding);

	@GetMapping(value ="/libraryBookCase/kindsFiltered")
	public List<BooksBean>booksKindsFiltered(
			@RequestParam(name = "kinds") List<String> kinds);

	@GetMapping(value = "/libraryBookCase/buildingAndKindsFiltered")
	public List<BooksBean> booksBuildingAndKindsFiltered(
			@RequestParam(name = "libraryBuilding") Integer libraryBuilding,
			@RequestParam(name = "kinds") List<String> kinds);


}
