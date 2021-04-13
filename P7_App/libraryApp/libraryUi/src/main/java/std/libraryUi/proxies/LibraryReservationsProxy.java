package std.libraryUi.proxies;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryUi.beans.LibraryReservationBean;

@FeignClient(name = "libraryGateWayZuul")
@RibbonClient(name = "libraryReservations")
public interface LibraryReservationsProxy {

    @PostMapping(value = "/libraryReservations/createReservation")
    public void createReservation(@RequestParam(value = "bookId") Integer bookId,
	    @RequestParam(value = "customerEmail") String customerEmail);

    @GetMapping(value = "/libraryReservations/customerReservations")
    public List<LibraryReservationBean> customerReservations(
	    @RequestParam(value = "customerEmail") String customerEmail);

    @PostMapping(value = "/libraryReservations/cancelReservation")
    public void cancelReservation(@RequestParam(value = "reservationId") Integer reservationId);

}
