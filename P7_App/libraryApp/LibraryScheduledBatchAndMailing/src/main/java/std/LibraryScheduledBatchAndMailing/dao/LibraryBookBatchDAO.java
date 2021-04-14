package std.LibraryScheduledBatchAndMailing.dao;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import std.LibraryScheduledBatchAndMailing.entities.LibraryBookBatch;

public interface LibraryBookBatchDAO extends JpaRepositoryImplementation<LibraryBookBatch, Integer> {

    public List<LibraryBookBatch> findByIdInAndAvailabilityTrue(List<Integer> ids);

}
