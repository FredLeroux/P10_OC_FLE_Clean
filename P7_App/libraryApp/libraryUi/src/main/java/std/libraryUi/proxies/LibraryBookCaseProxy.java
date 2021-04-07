package std.libraryUi.proxies;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryUi.beans.BookKindsBean;
import std.libraryUi.beans.BooksBean;
import std.libraryUi.beans.ReservableBookExamplaryBean;

@FeignClient(name = "libraryGateWayZuul")
@RibbonClient(name = "libraryBookCase")
public interface LibraryBookCaseProxy {

    @GetMapping(value = "/libraryBookCase/books")
    public List<BooksBean> books(@RequestParam(name = "maxReservationNumber") Integer maxReservationNumber);

    @GetMapping(value = "/libraryBookCase/kinds")
    public List<BookKindsBean> kinds();

    @GetMapping(value = "/libraryBookCase/buildingFiltered")
    public List<BooksBean> booksBuildingFiltered(@RequestParam(name = "libraryBuilding") Integer libraryBuilding,
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber);

    @GetMapping(value = "/libraryBookCase/kindsFiltered")
    public List<BooksBean> booksKindsFiltered(@RequestParam(name = "kinds") List<String> kinds,
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber);

    @GetMapping(value = "/libraryBookCase/buildingAndKindsFiltered")
    public List<BooksBean> booksBuildingAndKindsFiltered(
	    @RequestParam(name = "libraryBuilding") Integer libraryBuilding,
	    @RequestParam(name = "kinds") List<String> kinds,
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber);

    @GetMapping(value = "/libraryBookCase/reservableBookExamplaries")
    public List<ReservableBookExamplaryBean> getReservableBooks(@RequestParam(name = "title") String title,
	    @RequestParam(name = "buildingName") String buildingName,
	    @RequestParam(name = "maxOfReservation") Integer maxOfReservation);

}
