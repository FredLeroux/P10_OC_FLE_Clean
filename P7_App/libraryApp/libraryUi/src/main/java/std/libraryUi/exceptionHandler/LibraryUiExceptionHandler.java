package std.libraryUi.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.sun.jersey.api.NotFoundException;

import std.libraryUi.errorDecoder.exceptions.AlreadyReservedOrLoanedByCustomerException;
import std.libraryUi.errorDecoder.exceptions.BookNotAvailableException;
import std.libraryUi.errorDecoder.exceptions.ChronoUnitNotImplementedException;
import std.libraryUi.errorDecoder.exceptions.LoanNotFoundException;
import std.libraryUi.errorDecoder.exceptions.ReservationNotFoundException;
import std.libraryUi.exceptions.UnknowErrorException;

@RestControllerAdvice
public class LibraryUiExceptionHandler {

    @ExceptionHandler(value = LoanNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView postponeLoanNotFoundError() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.NOT_FOUND.value());
	return model;
    }

    @ExceptionHandler(value = ChronoUnitNotImplementedException.class)
    @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
    public ModelAndView postponeChronoUnitError() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.NOT_IMPLEMENTED.value());
	return model;
    }

    @ExceptionHandler(value = AlreadyReservedOrLoanedByCustomerException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ModelAndView reserveBookAlreadyreservedError() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.CONFLICT.value());
	return model;
    }

    @ExceptionHandler(value = UnknowErrorException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView unknowError() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
	return model;
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView notFoundError() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.NOT_FOUND.value());
	return model;
    }

    @ExceptionHandler(value = BookNotAvailableException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ModelAndView bookNotAvailabler() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.CONFLICT.value());
	return model;
    }

    @ExceptionHandler(value = ReservationNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView ReservationNotFoundError() {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.NOT_FOUND.value());
	return model;
    }

    private ModelAndView errorModel(Integer httpStatusCode) {
	ModelAndView model = new ModelAndView("/errorPageTrigger");
	model.addObject("errorCode", HttpStatus.NOT_IMPLEMENTED.value());
	return model;
    }
}
