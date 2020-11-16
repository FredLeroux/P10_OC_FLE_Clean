package std.LibraryScheduledBatchAndMailing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.LibraryScheduledBatchAndMailing.dao.CustomerBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.LibraryBookBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.LoanBatchDAO;
import std.LibraryScheduledBatchAndMailing.dto.CustomerBatchEmailDTO;
import std.LibraryScheduledBatchAndMailing.dto.LibraryBookBatchTitleDTO;
import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.CustomerBatch;
import std.LibraryScheduledBatchAndMailing.entities.LibraryBookBatch;
import std.LibraryScheduledBatchAndMailing.entities.LoanBatch;

@Service
public class LoanBatchServiceImpl implements LoanBatchService {

	@Autowired
	LoanBatchDAO loanBatchDao;

	@Autowired
	CustomerBatchDAO customerBatchDao;

	@Autowired
	LibraryBookBatchDAO libraryBookBatchDao;


	private ModelMapper mapper = new ModelMapper();
	private LoanBatchMailInfoDTO loanBatchMailInfoDTO = new LoanBatchMailInfoDTO();
	private CustomerBatchEmailDTO customerBatchEmailDTO = new CustomerBatchEmailDTO();
	private LibraryBookBatchTitleDTO libraryBookBatchTitleDTO = new LibraryBookBatchTitleDTO();



	@Override
	public List<LoanBatchMailInfoDTO> sortLateLoansList(){
		return unReturnedLoanDTO().stream().filter(o->parseReturnDateToLocalDate(o).compareTo(LocalDate.now())<0).collect(Collectors.toList());
	}

	private LocalDate parseReturnDateToLocalDate(LoanBatchMailInfoDTO dto) {
		return LocalDate.parse(dto.getReturnDate());
	}


	private List<LoanBatchMailInfoDTO> unReturnedLoanDTO(){
		return unReturnedLoan().stream().map(o->setLoanBatchMailInfoDTO(o)).collect(Collectors.toList());
	}

	private LoanBatchMailInfoDTO setLoanBatchMailInfoDTO(LoanBatch loanBatch) {
		LoanBatchMailInfoDTO dto = mapLoanBatchMailInfoDTO(loanBatch);
		dto.setCustomer(mapCustomerBatch(customerBatch(loanBatch.getCustomer().getId())));
		dto.setBook(mapLibraryBookBatchTitleDTO(libraryBookBatch(loanBatch.getBook().getId())));
		return dto;
	}

	private LoanBatchMailInfoDTO mapLoanBatchMailInfoDTO(LoanBatch loanBatch) {
		return mappingTo(loanBatch, loanBatchMailInfoDTO);
	}

	private LibraryBookBatch libraryBookBatch(Integer id) {
		Optional<LibraryBookBatch> optLibraryBookBatch = libraryBookBatchDao.findById(id);
		if(optLibraryBookBatch.isPresent()) {
			return optLibraryBookBatch.get();
		}
		//TODO exception
		return null;

	}


	private LibraryBookBatchTitleDTO mapLibraryBookBatchTitleDTO(LibraryBookBatch libraryBookBatch) {
		return mappingTo(libraryBookBatch, libraryBookBatchTitleDTO);
	}

	private CustomerBatch customerBatch(Integer id) {
		Optional<CustomerBatch> optCustomerBatch = customerBatchDao.findById(id);
		if(optCustomerBatch.isPresent()) {
			return optCustomerBatch.get();
		}
			//TODO exception;
		return null;

	}

	private CustomerBatchEmailDTO mapCustomerBatch(CustomerBatch customerBatch) {
		return mappingTo(customerBatch, customerBatchEmailDTO);
	}


	private List<LoanBatch> unReturnedLoan(){
		return loanBatchDao.findByReturnedFalse();
	}



	@SuppressWarnings("unchecked")
	private<O extends Object> O mappingTo(Object source, O destination) {
		return (O) mapper.map(source, destination.getClass());
	}

}
