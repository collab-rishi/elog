package tendering.service;

import tendering.dto.OtpVerificationEmailDTO;
import tendering.model.Signup;
import tendering.repository.SignupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class ResetService {

	private static final Logger logger = LoggerFactory.getLogger(ResetService.class);
	private static final int OTP_VALIDITY_MINUTES = 5;
	private static final int OTP_LENGTH = 4;

	private final SignupRepository signupRepository;
	private final EmailService emailService;
	private final Map<String, OtpVerificationEmailDTO> otpStorage = new HashMap<>();

	@Autowired
	public ResetService(SignupRepository signupRepository, EmailService emailService) {
		this.signupRepository = signupRepository;
		this.emailService = emailService;
	}

	// Initiates the password reset process by generating and sending an OTP to the
	// user's email if the email is registered.
	public String initiatePasswordReset(String email) {
		Optional<Signup> userOptional = signupRepository.findByEmail(email);
		if (userOptional.isEmpty()) {
			logger.warn("Password reset requested for non-existent email: {}", email);
			throw new IllegalArgumentException("No user registered with this email.");
		}

		String otp = generateOtp();
		logger.info("OTP generated for email: {}", otp);

		otpStorage.put(email, new OtpVerificationEmailDTO(email, otp, LocalDateTime.now(), false));

		emailService.sendOtp(email, otp);
		return "An OTP has been sent to your registered email. Please verify within 5 minutes.";
	}

	// Verifies the OTP provided by the user against the stored OTP and checks if it
	// is still valid.
	public String verifyOtp(String email, String otp) {
		OtpVerificationEmailDTO otpData = otpStorage.get(email);

		if (otpData == null) {
			throw new IllegalArgumentException("No OTP found for this email.");
		}
		if (!otp.equals(otpData.getOtp())) {
			throw new IllegalArgumentException("Incorrect OTP.");
		}
		if (isOtpExpired(otpData.getTimestamp())) {
			otpStorage.remove(email);
			throw new IllegalArgumentException("OTP expired. Please request a new one.");
		}

		otpData.setVerified(true);
		logger.info("OTP verified for email: {}", email);
		return "OTP verified successfully. You may now reset your password.";
	}

	// Resets the user's password after confirming OTP verification and matching new
	// password entries.
	public String resetPassword(String email, String newPassword, String confirmPassword) {
		Signup user = signupRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("No user registered with this email."));

		OtpVerificationEmailDTO otpData = otpStorage.get(email);
		if (otpData == null || !otpData.isVerified()) {
			throw new IllegalArgumentException("OTP verification is required.");
		}
		if (!newPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException("Passwords do not match.");
		}

		user.setPassword(newPassword); // TODO: Hash password in production
		signupRepository.save(user);
		otpStorage.remove(email);

		logger.info("Password reset successfully for email: {}", email);
		return "Password reset successful. You can now log in with your new password.";
	}

	// Checks if the OTP has expired based on the configured validity duration.
	private boolean isOtpExpired(LocalDateTime timestamp) {
		return timestamp.plusMinutes(OTP_VALIDITY_MINUTES).isBefore(LocalDateTime.now());
	}

	// Generates a zero-padded 4-digit random OTP as a string.
	private String generateOtp() {
		return String.format("%04d", new Random().nextInt(10000));
	}
}
