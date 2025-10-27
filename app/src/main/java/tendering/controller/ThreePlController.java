package tendering.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tendering.dto.LspConfirmationRequest;
import tendering.model.LspAssignment;
import tendering.model.LspResponse;
import tendering.service.LspAssignmentService;
import tendering.service.LspResponseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/3pl")
@CrossOrigin(origins = "*")
public class ThreePlController {

    private static final Logger log = LoggerFactory.getLogger(ThreePlController.class);

    private final LspResponseService lspResponseService;
    private final LspAssignmentService lspAssignmentService;

    @Autowired
    public ThreePlController(LspResponseService lspResponseService, LspAssignmentService lspAssignmentService) {
        this.lspResponseService = lspResponseService;
        this.lspAssignmentService = lspAssignmentService;
    }

    /**
     * Confirm an LSP response and reject all others
     */
    @PostMapping("/confirm-lsp")
    public ResponseEntity<String> confirmLspResponse(@Valid @RequestBody LspConfirmationRequest request) {
        log.info("ENTRY: 3PL confirmation request for tender '{}' and LSP '{}'", request.getTenderNo(), request.getLspCompanyName());

        try {
            boolean confirmed = lspResponseService.confirmLspResponseByTenderAndLsp(
                request.getTenderNo(),
                request.getLspCompanyName(),
                request.getSelectionMessage()
            );

            if (confirmed) {
                log.info("EXIT: Successfully confirmed LSP '{}' for tender '{}'", request.getLspCompanyName(), request.getTenderNo());
                return ResponseEntity.ok("LSP response confirmed successfully. All other responses have been rejected.");
            } else {
                log.warn("EXIT: Failed to confirm LSP '{}' for tender '{}'", request.getLspCompanyName(), request.getTenderNo());
                return ResponseEntity.badRequest().body("Failed to confirm LSP response. Please check the details.");
            }
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while confirming LSP response: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while confirming the LSP response: " + e.getMessage());
        }
    }
    

    /**
     * Get all responses for a tender
     */
    @GetMapping("/tender/{tenderNo}/responses")
    public ResponseEntity<List<LspResponse>> getTenderResponses(@PathVariable String tenderNo) {
        log.info("ENTRY: Fetching all responses for tender: {}", tenderNo);

        try {
            List<LspResponse> responses = lspResponseService.getResponsesByTenderNo(tenderNo);
            log.info("EXIT: Found {} responses for tender: {}", responses.size(), tenderNo);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching tender responses: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get confirmed response for a tender
     */
    @GetMapping("/tender/{tenderNo}/confirmed-response")
    public ResponseEntity<LspResponse> getConfirmedResponse(@PathVariable String tenderNo) {
        log.info("ENTRY: Fetching confirmed response for tender: {}", tenderNo);

        try {
            Optional<LspResponse> confirmedResponse = lspResponseService.getConfirmedResponseByTenderNo(tenderNo);
            
            if (confirmedResponse.isPresent()) {
                log.info("EXIT: Found confirmed response for tender: {}", tenderNo);
                return ResponseEntity.ok(confirmedResponse.get());
            } else {
                log.info("EXIT: No confirmed response found for tender: {}", tenderNo);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching confirmed response: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get assignment details for a tender
     */
    @GetMapping("/tender/{tenderNo}/assignment")
    public ResponseEntity<LspAssignment> getTenderAssignment(@PathVariable String tenderNo) {
        log.info("ENTRY: Fetching assignment details for tender: {}", tenderNo);

        try {
            Optional<LspAssignment> assignment = lspAssignmentService.getAssignmentByTenderNo(tenderNo);
            
            if (assignment.isPresent()) {
                log.info("EXIT: Found assignment for tender: {}", tenderNo);
                return ResponseEntity.ok(assignment.get());
            } else {
                log.info("EXIT: No assignment found for tender: {}", tenderNo);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all assignments for a 3PL company
     */
    @GetMapping("/assignments")
    public ResponseEntity<List<LspAssignment>> get3plAssignments(@RequestParam String companyName) {
        log.info("ENTRY: Fetching assignments for 3PL company: {}", companyName);

        try {
            List<LspAssignment> assignments = lspAssignmentService.getAssignmentsBy3plCompany(companyName);
            log.info("EXIT: Found {} assignments for 3PL company: {}", assignments.size(), companyName);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching 3PL assignments: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


