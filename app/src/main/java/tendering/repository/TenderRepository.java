package tendering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tendering.model.Tender;
import tendering.model.TenderStatus;

import java.util.List;

@Repository
public interface TenderRepository extends JpaRepository<Tender, Long> {

	// Count how many tenders already exist with this baseTenderNo
	long countByTenderNoStartingWith(String baseTenderNo);

	// Fetch tenders by createdBy (companyName) and status
	List<Tender> findByCreatedByAndStatus(String createdBy, TenderStatus status);
}