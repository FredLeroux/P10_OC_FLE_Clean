package std.LibraryScheduledBatchAndMailing.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import std.LibraryScheduledBatchAndMailing.entities.CustomerBatch;

public interface CustomerBatchDAO extends JpaRepositoryImplementation<CustomerBatch, Integer> {

}
