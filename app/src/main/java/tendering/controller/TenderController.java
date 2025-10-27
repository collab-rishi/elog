package tendering.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tendering.dto.TenderDTO;
import tendering.dto.TenderSearchRequest;
import tendering.model.Tender;
import tendering.model.TenderStatus;
import tendering.service.TenderService;

import java.util.List;
import java.util.NoSuchElementException;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/3PL/tenders")
public class TenderController {

	private static final Logger log = LoggerFactory.getLogger(TenderController.class);

	@Autowired
	private TenderService tenderService;

	// Endpoint for creating a new tender
	@PostMapping("/create")
	public ResponseEntity<Tender> createTender(@RequestBody TenderDTO tenderDTO, @RequestParam String companyName) {
		log.info("ENTRY: createTender - companyName: [{}], tenderDTO: [{}]", companyName, tenderDTO);

		Tender createdTender = tenderService.createTender(tenderDTO, companyName);

		if (createdTender != null) {
			log.info("EXIT: createTender - Successfully created tender with Tender No: [{}]",
					createdTender.getTenderNo());
			return ResponseEntity.status(HttpStatus.CREATED).body(createdTender);
		} else {
			log.error("EXIT: createTender - Failed to create tender for company: [{}]", companyName);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/search")
	public ResponseEntity<?> getTendersByCompanyAndStatus(@RequestBody TenderSearchRequest request) {
		String companyName = request.getCompanyName();
		String status = request.getStatus();

		log.info("ENTRY: searchTenders - company: [{}], status: [{}]", companyName, status);

		// Validate status
		if (!isValidStatus(status)) {
			log.error("EXIT: searchTenders - Invalid status: [{}]", status);
			return ResponseEntity.badRequest().body("Invalid status. Allowed values: ACTIVE, PENDING, COMPLETED.");
		}

		try {
			List<Tender> tenders = tenderService.getTendersByCompanyAndStatus(companyName,
					TenderStatus.valueOf(status.toUpperCase()));

			if (tenders.isEmpty()) {
				// Use INFO instead of ERROR here
				log.info("EXIT: searchTenders - No tenders found for company [{}] with status [{}]", companyName,
						status);
				return ResponseEntity.ok("No " + status.toUpperCase() + " tenders found for company: " + companyName);
			}

			log.info("EXIT: searchTenders - Found {} tenders for company [{}] with status [{}]", tenders.size(),
					companyName, status);
			return ResponseEntity.ok(tenders);

		} catch (NoSuchElementException e) {
			// Instead of logging as an error, it's a normal business case
			log.warn("EXIT: searchTenders - No tenders found for company [{}] with status [{}]: {}", companyName,
					status, e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No " + status.toUpperCase() + " tenders found for company: " + companyName);
		} catch (IllegalArgumentException e) {
			log.error("EXIT: searchTenders - Invalid status value: [{}]", status);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid status. Allowed values: ACTIVE, PENDING, COMPLETED.");
		}
	}

	// Helper method to validate the status
	private boolean isValidStatus(String status) {
		return "ACTIVE".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)
				|| "COMPLETED".equalsIgnoreCase(status);
	}
}
