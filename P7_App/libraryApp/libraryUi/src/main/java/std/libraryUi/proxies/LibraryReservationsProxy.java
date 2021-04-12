package std.libraryUi.proxies;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "libraryGateWayZuul")
@RibbonClient(name = "libraryReservations")
public interface LibraryReservationsProxy {

    @PostMapping(value = "/libraryReservations/createReservation")
    public void createReservation(@RequestParam(value = "bookId") Integer bookId,
	    @RequestParam(value = "customerEmail") String customerEmail);

}
