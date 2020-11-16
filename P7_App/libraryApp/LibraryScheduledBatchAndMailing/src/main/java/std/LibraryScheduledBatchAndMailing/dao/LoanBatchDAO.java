package std.LibraryScheduledBatchAndMailing.dao;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import std.LibraryScheduledBatchAndMailing.entities.LoanBatch;

public interface LoanBatchDAO extends JpaRepositoryImplementation<LoanBatch, Integer> {

	List<LoanBatch> findByReturnedFalse();

}
