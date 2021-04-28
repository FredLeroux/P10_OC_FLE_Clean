package std.libraryUi.proxies;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "libraryGateWayZuul")
@RibbonClient(name = "libraryScheduledBatchAndMailing")
public interface LibraryScheduledBatchAndMailingProxy {

    @PostMapping(value = "/libraryScheduledBatchAndMailing/sendNotificationBookAvailable")
    public void sendNotificationBookAvailable(@RequestParam(value = "customerEmail") String customerEmail,
	    @RequestParam(value = "bookTitle") String bookTitle,
	    @RequestParam(value = "buildingName") String buildingName,
	    @RequestParam(value = "reference") Integer reference);

    @PostMapping(value = "/libraryScheduledBatchAndMailing/sendNotificationBookAvailableAfterCustomerCancel")
    public void sendNotificationBookAvailableAfterCustomerCancel(@RequestParam(value = "bookTitle") String bookTitle,
	    @RequestParam(value = "priority") Integer priority);
}
