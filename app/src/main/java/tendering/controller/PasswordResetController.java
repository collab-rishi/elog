package tendering.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tendering.dto.PasswordResetDTO;
import tendering.dto.VerifyResetOtpDTO;
import tendering.service.ResetService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class PasswordResetController {

	private static final Logger log = LoggerFactory.getLogger(PasswordResetController.class);
	private final ResetService resetService;

	@Autowired
	public PasswordResetController(ResetService resetService) {
		this.resetService = resetService;
	}

	@PostMapping("/request-password-reset")
	public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid VerifyResetOtpDTO request) {
		final String email = request.getEmail();
		log.info("[ENTRY] requestPasswordReset - Email: {}", email);

		try {
			String response = resetService.initiatePasswordReset(email);
			log.info("[EXIT] requestPasswordReset - Email: {}", email);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.warn("[EXIT] requestPasswordReset - Email: {} | Reason: {}", email, e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			log.error("[EXIT] requestPasswordReset - Email: {}", email, e);
			return ResponseEntity.internalServerError().body("Something went wrong. Please try again.");
		}
	}

	@PostMapping("/verify-reset-otp")
	public ResponseEntity<String> verifyOtp(@RequestBody @Valid VerifyResetOtpDTO request) {
		final String email = request.getEmail();
		log.info("[ENTRY] verifyOtp - Email: {}", email);

		try {
			String response = resetService.verifyOtp(email, request.getOtp());
			log.info("[EXIT] verifyOtp - Email: {}", email);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.warn("[EXIT] verifyOtp - Email: {} | Reason: {}", email, e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			log.error("[EXIT] verifyOtp - Email: {}", email, e);
			return ResponseEntity.internalServerError().body("Something went wrong. Please try again.");
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetDTO request) {
		final String email = request.getEmail();
		log.info("[ENTRY] resetPassword - Email: {}", email);

		try {
			String response = resetService.resetPassword(email, request.getNewPassword(), request.getConfirmPassword());
			log.info("[EXIT] resetPassword - Email: {}", email);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.warn("[EXIT] resetPassword - Email: {} | Reason: {}", email, e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			log.error("[EXIT] resetPassword - Email: {}", email, e);
			return ResponseEntity.internalServerError().body("Something went wrong. Please try again.");
		}
	}
}