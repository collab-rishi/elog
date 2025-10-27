// --- Service: LspResponseService.java ---
package tendering.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tendering.dto.TenderReplyRequest;
import tendering.exception.ResourceNotFoundException;
import tendering.model.LspResponse;
import tendering.model.LspResponse.SelectionStatus;
import tendering.model.LspResponse.Status;
import tendering.repository.LspResponseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LspResponseService {

	private static final Logger log = LoggerFactory.getLogger(LspResponseService.class);

	private final LspResponseRepository lspResponseRepository;

	public LspResponseService(LspResponseRepository lspResponseRepository) {
		this.lspResponseRepository = lspResponseRepository;
	}

	public List<LspResponse> getResponsesByCompanyName(String companyName) {
		return lspResponseRepository.findByLspCompanyName(companyName);
	}

	public boolean updateLspTenderReply(String tenderNo, String companyName, TenderReplyRequest request) {
		var optionalResponse = lspResponseRepository.findByTenderNoAndLspCompanyName(tenderNo, companyName);
		if (optionalResponse.isEmpty())
			return false;

		LspResponse response = optionalResponse.get();
		response.setEstimatedArrivalDate(request.getEstimatedArrivalDate());
		response.setBidPrice(request.getBidPrice());
		response.setLspMessage(request.getLspMessage());
		response.setStatus(Status.INPROCESS);

		lspResponseRepository.save(response);
		return true;
	}

	public List<LspResponse> getResponsesByCompanyAndStatus(String companyName, Status status) {
		return lspResponseRepository.findByLspCompanyNameAndStatus(companyName, status);
	}

	public List<LspResponse> getResponsesByCreatedByCompanyAndTenderNo(String companyName, String tenderNo) {
		return lspResponseRepository.findByCreatedByCompanyNameAndTenderNo(companyName, tenderNo);
	}

	/**
	 * Confirm an LSP response and reject all others for the same tender IMPORTANT:
	 * Only updates selection_status, does NOT change the main status field
	 */
//    @Transactional
//    public boolean confirmLspResponseByTenderAndLsp(String tenderNo, String lspCompanyName) {
//        log.info("ENTRY: Confirming LSP '{}' for tender '{}'", lspCompanyName, tenderNo);
//
//        // Find the specific LSP response for this tender and LSP
//        Optional<LspResponse> lspResponseOpt = lspResponseRepository.findFirstByTenderNoAndLspCompanyName(tenderNo, lspCompanyName);
//        if (lspResponseOpt.isEmpty()) {
//            log.warn("EXIT: No LSP response found for tender '{}' and LSP '{}'", tenderNo, lspCompanyName);
//            return false;
//        }
//
//        LspResponse lspResponse = lspResponseOpt.get();
//
//        // Reject all other LSP responses for this tender
//        lspResponseRepository.updateSelectionStatusByTenderNo(tenderNo, SelectionStatus.REJECTED);
//        
//        // Confirm the selected LSP response - ONLY update selection_status, NOT the main status
//        lspResponse.setSelectionStatus(SelectionStatus.CONFIRMED);
//        // NOTE: We do NOT change lspResponse.setStatus()
//        lspResponseRepository.save(lspResponse);
//
//        log.info("EXIT: Successfully confirmed LSP '{}' for tender '{}'. Status remains unchanged.", lspCompanyName, tenderNo);
//        return true;
//    }
	@Transactional
	public boolean confirmLspResponseByTenderAndLsp(String tenderNo, String lspCompanyName, String selectionMessage) {
		log.info("ENTRY: Confirming LSP '{}' for tender '{}'", lspCompanyName, tenderNo);

		// Find the specific LSP response
		Optional<LspResponse> lspResponseOpt = lspResponseRepository.findFirstByTenderNoAndLspCompanyName(tenderNo,
				lspCompanyName);
		if (lspResponseOpt.isEmpty()) {
			log.warn("EXIT: No LSP response found for tender '{}' and LSP '{}'", tenderNo, lspCompanyName);
			return false;
		}

		LspResponse lspResponse = lspResponseOpt.get();

		// Correct: Reject all other LSP responses for this tender except the selected
		// one
		lspResponseRepository.updateSelectionStatusByTenderNo(tenderNo, SelectionStatus.REJECTED, lspCompanyName);

		// Confirm the selected LSP response and save selection message
		lspResponse.setSelectionStatus(SelectionStatus.CONFIRMED);
		lspResponse.setSelectionMessage(selectionMessage); // Can be null
		lspResponseRepository.save(lspResponse);

		log.info("EXIT: Successfully confirmed LSP '{}' for tender '{}'", lspCompanyName, tenderNo);
		return true;
	}

	/**
	 * Get all responses for a tender with their selection status
	 */
	public List<LspResponse> getResponsesByTenderNo(String tenderNo) {
		log.info("ENTRY: Fetching all responses for tender: {}", tenderNo);
		List<LspResponse> responses = lspResponseRepository.findByTenderNo(tenderNo);
		log.info("EXIT: Found {} responses for tender: {}", responses.size(), tenderNo);
		return responses;
	}

	/**
	 * Get confirmed response for a tender
	 */
	public Optional<LspResponse> getConfirmedResponseByTenderNo(String tenderNo) {
		log.info("ENTRY: Fetching confirmed response for tender: {}", tenderNo);
		List<LspResponse> confirmedResponses = lspResponseRepository.findByTenderNoAndSelectionStatus(tenderNo,
				SelectionStatus.CONFIRMED);
		Optional<LspResponse> confirmedResponse = confirmedResponses.isEmpty() ? Optional.empty()
				: Optional.of(confirmedResponses.get(0));
		log.info("EXIT: Confirmed response found: {}", confirmedResponse.isPresent());
		return confirmedResponse;
	}

	/**
	 * Get rejected responses for a tender
	 */
	public List<LspResponse> getRejectedResponsesByTenderNo(String tenderNo) {
		log.info("ENTRY: Fetching rejected responses for tender: {}", tenderNo);
		List<LspResponse> rejectedResponses = lspResponseRepository.findByTenderNoAndSelectionStatus(tenderNo,
				SelectionStatus.REJECTED);
		log.info("EXIT: Found {} rejected responses for tender: {}", rejectedResponses.size(), tenderNo);
		return rejectedResponses;
	}
}