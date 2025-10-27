package tendering.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tendering.dto.VehicleDetailsRequest;
import tendering.model.LspAssignment;
import tendering.service.LspAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/lsp/assignment")
@CrossOrigin(origins = "*")
public class LspAssignmentController {

    private static final Logger log = LoggerFactory.getLogger(LspAssignmentController.class);

    private final LspAssignmentService lspAssignmentService;

    @Autowired
    public LspAssignmentController(LspAssignmentService lspAssignmentService) {
        this.lspAssignmentService = lspAssignmentService;
    }

    /**
     * Submit vehicle details for a confirmed LSP response
     */
    @PostMapping("/{lspResponseId:\\d+}/vehicle-details")
    public ResponseEntity<LspAssignment> submitVehicleDetails(
            @PathVariable Long lspResponseId,
            @Valid @RequestBody VehicleDetailsRequest request) {
        
        log.info("ENTRY: Submitting vehicle details for LSP response ID: {}", lspResponseId);

        try {
            LspAssignment assignment = lspAssignmentService.submitVehicleDetails(lspResponseId, request);
            log.info("EXIT: Successfully submitted vehicle details for LSP response ID: {}", lspResponseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while submitting vehicle details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Submit vehicle details using tender number instead of response id
     */
    @PostMapping("/{tenderNo}/vehicle-details")
    public ResponseEntity<LspAssignment> submitVehicleDetailsByTenderNo(
            @PathVariable String tenderNo,
            @Valid @RequestBody VehicleDetailsRequest request) {

        log.info("ENTRY: Submitting vehicle details for tender: {}", tenderNo);

        try {
            LspAssignment assignment = lspAssignmentService.submitVehicleDetailsByTenderNo(tenderNo, request);
            log.info("EXIT: Successfully submitted vehicle details for tender: {}", tenderNo);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while submitting vehicle details by tender: {} - {}", tenderNo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get assignment (vehicle) details by tender number
     */
    @GetMapping("/tender/{tenderNo}")
    public ResponseEntity<LspAssignment> getAssignmentByTenderNo(@PathVariable String tenderNo) {
        log.info("ENTRY: Fetching assignment by tender: {}", tenderNo);
        try {
            var assignmentOpt = lspAssignmentService.getAssignmentByTenderNo(tenderNo);
            return assignmentOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching assignment by tender: {} - {}", tenderNo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get assignment details for an LSP response
     */
    @GetMapping("/{lspResponseId}")
    public ResponseEntity<LspAssignment> getAssignmentByResponseId(@PathVariable Long lspResponseId) {
        log.info("ENTRY: Fetching assignment for LSP response ID: {}", lspResponseId);

        try {
            var assignment = lspAssignmentService.getAssignmentByLspResponseId(lspResponseId);
            
            if (assignment.isPresent()) {
                log.info("EXIT: Found assignment for LSP response ID: {}", lspResponseId);
                return ResponseEntity.ok(assignment.get());
            } else {
                log.info("EXIT: No assignment found for LSP response ID: {}", lspResponseId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all assignments for an LSP company
     */
    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<LspAssignment>> getLspAssignments(@PathVariable String companyName) {
        log.info("ENTRY: Fetching assignments for LSP company: {}", companyName);

        try {
            List<LspAssignment> assignments = lspAssignmentService.getAssignmentsByLspCompany(companyName);
            log.info("EXIT: Found {} assignments for LSP company: {}", assignments.size(), companyName);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            log.error("EXIT: Exception occurred while fetching LSP assignments: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

