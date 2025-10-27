package tendering.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tendering.dto.LspResponseFilterRequest;
import tendering.dto.LspResponseViewDTO;
import tendering.dto.ResponseSearchRequest;
import tendering.dto.TenderReplyRequest;
import tendering.model.LspResponse;
import tendering.service.LspResponseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lsp/responses")
@CrossOrigin(origins = "*")
public class LspResponseController {

    private static final Logger log = LoggerFactory.getLogger(LspResponseController.class);
    private final LspResponseService lspResponseService;

    public LspResponseController(LspResponseService lspResponseService) {
        this.lspResponseService = lspResponseService;
    }

    // ---------------- GET: Fetch responses by company ----------------
    @GetMapping
    public ResponseEntity<List<LspResponse>> getResponsesByCompany(@RequestParam String companyName) {
        log.info("ENTRY: Fetching responses for company '{}'", companyName);

        List<LspResponse> responses = lspResponseService.getResponsesByCompanyName(companyName);

        if (responses.isEmpty()) {
            log.info("EXIT: No responses found for '{}'", companyName);
            return ResponseEntity.noContent().build();
        }

        log.info("EXIT: {} response(s) found for '{}'", responses.size(), companyName);
        return ResponseEntity.ok(responses);
    }

    // ---------------- PUT: Reply to a tender ----------------
    @PutMapping("/reply/{tenderNo}")
    public ResponseEntity<String> replyToTender(
            @PathVariable String tenderNo,
            @RequestParam String companyName,
            @Valid @RequestBody TenderReplyRequest request) {

        log.info("ENTRY: Replying to tender '{}' by company '{}'", tenderNo, companyName);

        boolean updated = lspResponseService.updateLspTenderReply(tenderNo, companyName, request);

        if (updated) {
            log.info("EXIT: Reply submitted successfully for tender '{}' by '{}'", tenderNo, companyName);
            return ResponseEntity.ok("Tender reply submitted successfully.");
        } else {
            log.warn("EXIT: No tender '{}' found for '{}'", tenderNo, companyName);
            return ResponseEntity.notFound().build();
        }
    }

    // ---------------- POST: Filter responses by company and status ----------------
    @PostMapping("/filter")
    public ResponseEntity<?> filterResponsesByCompanyAndStatus(@Valid @RequestBody ResponseSearchRequest request) {
        String companyName = request.getCompanyName();
        String status = request.getStatus();

        log.info("ENTRY: Filtering for company='{}', status='{}'", companyName, status);

        try {
            LspResponse.Status enumStatus = LspResponse.Status.valueOf(status.toUpperCase());
            List<LspResponse> responses = lspResponseService.getResponsesByCompanyAndStatus(companyName, enumStatus);

            if (responses.isEmpty()) {
                log.info("EXIT: No responses for company='{}' with status='{}'", companyName, status);
                return ResponseEntity.ok("No responses found for company: " + companyName + " with status: " + status);
            }

            log.info("EXIT: {} response(s) found", responses.size());
            return ResponseEntity.ok(responses);

        } catch (IllegalArgumentException e) {
            log.error("EXIT: Invalid status '{}'", status);
            return ResponseEntity.badRequest().body("Invalid status. Allowed values: PENDING, INPROCESS, COMPLETED.");
        }
    }

    // ---------------- POST: Filter responses by company and tender number ----------------
    @PostMapping("/filter-by-tender")
    public ResponseEntity<List<LspResponseViewDTO>> getResponsesByTenderAndCompany(@RequestBody LspResponseFilterRequest request) {
        String companyName = request.getCompanyName();
        String tenderNo = request.getTender_no();

        log.info("ENTRY: Filtering responses for company='{}', tender='{}'", companyName, tenderNo);

        List<LspResponse> responses = lspResponseService.getResponsesByCreatedByCompanyAndTenderNo(companyName, tenderNo);

        List<LspResponseViewDTO> responseDTOs = responses.stream()
                .map(resp -> new LspResponseViewDTO(
                        resp.getLspCompanyName(),
                        resp.getBidPrice(),
                        resp.getEstimatedArrivalDate(),
                        resp.getLspMessage()
                ))
                .collect(Collectors.toList());

        log.info("EXIT: {} response(s) returned for tender='{}', company='{}'",
                responseDTOs.size(), tenderNo, companyName);

        return ResponseEntity.ok(responseDTOs);
    }
}
