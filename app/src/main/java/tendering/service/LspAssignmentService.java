package tendering.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tendering.dto.VehicleDetailsRequest;
import tendering.exception.ResourceNotFoundException;
import tendering.model.LspAssignment;
import tendering.model.LspResponse;
import tendering.model.LspResponse.SelectionStatus;
import tendering.repository.LspAssignmentRepository;
import tendering.repository.LspResponseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LspAssignmentService {

	private static final Logger log = LoggerFactory.getLogger(LspAssignmentService.class);

	private final LspAssignmentRepository lspAssignmentRepository;
	private final LspResponseRepository lspResponseRepository;

	@Autowired
	public LspAssignmentService(LspAssignmentRepository lspAssignmentRepository,
			LspResponseRepository lspResponseRepository) {
		this.lspAssignmentRepository = lspAssignmentRepository;
		this.lspResponseRepository = lspResponseRepository;
	}

	/**
	 * Submit vehicle details for a confirmed LSP response
	 */
	@Transactional
	public LspAssignment submitVehicleDetails(Long lspResponseId, VehicleDetailsRequest request) {
		log.info("ENTRY: Submitting vehicle details for LSP response ID: {}", lspResponseId);

		// Verify that the LSP response exists and is confirmed
		Optional<LspResponse> lspResponseOpt = lspResponseRepository.findById(lspResponseId);
		if (lspResponseOpt.isEmpty()) {
			throw new ResourceNotFoundException("LSP response not found with ID: " + lspResponseId);
		}

		LspResponse lspResponse = lspResponseOpt.get();
		if (lspResponse.getSelectionStatus() != LspResponse.SelectionStatus.CONFIRMED) {
			throw new IllegalStateException("Cannot submit vehicle details for non-confirmed LSP response");
		}

		// Check if assignment already exists
		if (lspAssignmentRepository.existsByLspResponseId(lspResponseId)) {
			throw new IllegalStateException("Vehicle details already submitted for this LSP response");
		}

		// Create new assignment
		LspAssignment assignment = new LspAssignment(lspResponseId, lspResponse.getTenderNo(),
				lspResponse.getCreatedByCompanyName(), // 3PL company name
				lspResponse.getLspCompanyName(), request.getVehicleNumber(), request.getDriverName(),
				request.getDriverContact(), request.getDlNumber()

		);

		LspAssignment savedAssignment = lspAssignmentRepository.save(assignment);

		// Update LSP response status to COMPLETED
		// lspResponse.setStatus(LspResponse.Status.COMPLETED);
		lspResponseRepository.save(lspResponse);

		log.info("EXIT: Vehicle details submitted successfully for LSP response ID: {}", lspResponseId);
		return savedAssignment;
	}

	/**
	 * Submit vehicle details for a confirmed LSP response using tender number
	 */
	@Transactional
	public LspAssignment submitVehicleDetailsByTenderNo(String tenderNo, VehicleDetailsRequest request) {
		log.info("ENTRY: Submitting vehicle details for tender: {}", tenderNo);

		// Find confirmed LSP response for this tender
		var confirmedResponses = lspResponseRepository.findByTenderNoAndSelectionStatus(tenderNo,
				SelectionStatus.CONFIRMED);
		if (confirmedResponses == null || confirmedResponses.isEmpty()) {
			throw new ResourceNotFoundException("No CONFIRMED LSP response found for tender: " + tenderNo);
		}

		LspResponse lspResponse = confirmedResponses.get(0);

		// Prevent duplicate assignments for this tender/response
		if (lspAssignmentRepository.existsByTenderNo(tenderNo)
				|| lspAssignmentRepository.existsByLspResponseId(lspResponse.getId())) {
			throw new IllegalStateException("Vehicle details already submitted for this tender/response");
		}

		// Create new assignment
		LspAssignment assignment = new LspAssignment(lspResponse.getId(), lspResponse.getTenderNo(),
				lspResponse.getCreatedByCompanyName(), lspResponse.getLspCompanyName(), request.getVehicleNumber(),
				request.getDriverName(), request.getDriverContact(), request.getDlNumber());

		LspAssignment savedAssignment = lspAssignmentRepository.save(assignment);

		// Mark response as completed (business rule: after vehicle details submitted)
		lspResponse.setStatus(LspResponse.Status.COMPLETED);
		lspResponseRepository.save(lspResponse);

		log.info("EXIT: Vehicle details submitted successfully for tender: {}", tenderNo);
		return savedAssignment;
	}

	/**
	 * Get assignment details by tender number
	 */
	public Optional<LspAssignment> getAssignmentByTenderNo(String tenderNo) {
		log.info("ENTRY: Fetching assignment for tender: {}", tenderNo);
		Optional<LspAssignment> assignment = lspAssignmentRepository.findByTenderNo(tenderNo);
		log.info("EXIT: Assignment found: {}", assignment.isPresent());
		return assignment;
	}

	/**
	 * Get assignment details by LSP response ID
	 */
	public Optional<LspAssignment> getAssignmentByLspResponseId(Long lspResponseId) {
		log.info("ENTRY: Fetching assignment for LSP response ID: {}", lspResponseId);
		Optional<LspAssignment> assignment = lspAssignmentRepository.findByLspResponseId(lspResponseId);
		log.info("EXIT: Assignment found: {}", assignment.isPresent());
		return assignment;
	}

	/**
	 * Get all assignments for a 3PL company
	 */
	public List<LspAssignment> getAssignmentsBy3plCompany(String threePlCompanyName) {
		log.info("ENTRY: Fetching assignments for 3PL company: {}", threePlCompanyName);
		List<LspAssignment> assignments = lspAssignmentRepository.findByAssignedBy3pl(threePlCompanyName);
		log.info("EXIT: Found {} assignments", assignments.size());
		return assignments;
	}

	/**
	 * Get all assignments for an LSP company
	 */
	public List<LspAssignment> getAssignmentsByLspCompany(String lspCompanyName) {
		log.info("ENTRY: Fetching assignments for LSP company: {}", lspCompanyName);
		List<LspAssignment> assignments = lspAssignmentRepository.findByLspCompanyName(lspCompanyName);
		log.info("EXIT: Found {} assignments", assignments.size());
		return assignments;
	}
}