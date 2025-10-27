package tendering.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tendering.dto.ProfileRequestDTO;
import tendering.dto.SignupDTO;
import tendering.model.Signup;
import tendering.model.UserRole;
import tendering.repository.SignupRepository;
import tendering.service.LoginService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserListController {

    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    private final SignupRepository signupRepository;
    private final LoginService loginService;

    @Autowired
    public UserListController(SignupRepository signupRepository, LoginService loginService) {
        this.signupRepository = signupRepository;
        this.loginService = loginService;
    }

    @GetMapping("/lsp")
    public ResponseEntity<?> getAllLSPs() {
        log.info("[ENTRY] Fetching all LSP users");
        try {
            List<Signup> lsps = signupRepository.findByRole(UserRole.LSP);
            log.info("[EXIT] Found {} LSP users", lsps.size());
            return ResponseEntity.ok(lsps);
        } catch (Exception e) {
            log.error("[ERROR] Failed to fetch LSP users - {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to fetch LSP users.");
        }
    }

    @GetMapping("/3pl")
    public ResponseEntity<?> getAll3PLs() {
        log.info("[ENTRY] Fetching all 3PL users");
        try {
            List<Signup> pls = signupRepository.findByRole(UserRole.THREE_PL);
            log.info("[EXIT] Found {} 3PL users", pls.size());
            return ResponseEntity.ok(pls);
        } catch (Exception e) {
            log.error("[ERROR] Failed to fetch 3PL users - {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to fetch 3PL users.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        log.info("[ENTRY] Fetching all users");
        try {
            List<Signup> users = signupRepository.findAll();
            log.info("[EXIT] Found {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("[ERROR] Failed to fetch all users - {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to fetch all users.");
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestBody ProfileRequestDTO request) {
        log.info("[ENTRY] Retrieving user profile");
        try {
            Optional<SignupDTO> result = loginService.getUserProfile(request);
            if (result.isPresent()) {
                log.info("[EXIT] User profile retrieved successfully");
                return ResponseEntity.ok(result.get());
            } else {
                log.info("[EXIT] User not found");
                return ResponseEntity.status(404).body("User not found.");
            }
        } catch (IllegalArgumentException e) {
            log.warn("[BAD REQUEST] {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("[ERROR] Exception occurred - {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error retrieving profile.");
        }
    }
}    
