package tendering.service;

import tendering.dto.SignupVerificationDTO;
import tendering.dto.SignupDTO;
import tendering.exception.UserAlreadyExistsException;
import tendering.model.Signup;
import tendering.model.UserRole;
import tendering.repository.SignupRepository;
import tendering.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private static final int OTP_VALIDITY_MINUTES = 5;
	private static final int OTP_LENGTH = 4;

	private final SignupRepository signupRepository;
	private final EmailService emailService;

	private final Map<String, SignupVerificationDTO> otpStorage = new HashMap<>();
	private final Map<String, SignupDTO> pendingSignups = new HashMap<>();

	@Autowired
	public AuthService(SignupRepository signupRepository, EmailService emailService) {
		this.signupRepository = signupRepository;
		this.emailService = emailService;
	}

	@PostConstruct
	public void initializeTwilio() {
		// Twilio initialization (commented)
	}
	// Placeholder method for Twilio initialization, currently commented out.

	public String initiateSignup(SignupDTO signupDTO) {
		logger.info("[SIGNUP] Initiating signup for Company: {}, Role: {}, Email: {}, Mobile: {}",
				signupDTO.getCompanyName(), signupDTO.getRole(), signupDTO.getEmail(), signupDTO.getMobileNumber());

		validateUserExistence(signupDTO);

		String otp = generateOtp();
		SignupVerificationDTO otpData = new SignupVerificationDTO(signupDTO.getMobileNumber(), otp,
				LocalDateTime.now());

		otpStorage.put(signupDTO.getMobileNumber(), otpData);
		pendingSignups.put(signupDTO.getMobileNumber(), signupDTO);

		logger.info("[OTP] Generated OTP: {} for Mobile: {}", otp, signupDTO.getMobileNumber());

		return "OTP has been generated on the server. Please verify within 5 minutes.";
	}
	// Initiates signup by validating uniqueness, generating OTP, and storing
	// pending signup info.

	public SignupDTO verifySignupOtp(String mobileNumber, String otp) {
		SignupVerificationDTO otpData = otpStorage.get(mobileNumber);

		if (otpData == null) {
			logger.warn("[OTP VERIFY] No OTP generated for Mobile: {}", mobileNumber);
			throw new IllegalArgumentException("No OTP generated for this mobile number.");
		}

		if (!otp.equals(otpData.getOtp())) {
			logger.warn("[OTP VERIFY] Incorrect OTP for Mobile: {}", mobileNumber);
			throw new IllegalArgumentException("The OTP you entered is incorrect.");
		}

		if (isOtpExpired(otpData.getTimestamp())) {
			otpStorage.remove(mobileNumber);
			logger.warn("[OTP VERIFY] OTP expired for Mobile: {}", mobileNumber);
			throw new IllegalArgumentException("The OTP has expired. Please request a new one.");
		}
		SignupDTO signupDTO = pendingSignups.remove(mobileNumber);
		otpStorage.remove(mobileNumber);

		if (signupDTO == null) {
			logger.error("[OTP VERIFY] Pending signup data is missing for Mobile: {}", mobileNumber);
			throw new IllegalStateException("Pending signup data is missing for this mobile number.");
		}

		saveSignup(signupDTO);
		logger.info("[SIGNUP] Account created successfully for Company: {}, Email: {}", signupDTO.getCompanyName(),
				signupDTO.getEmail());

		emailService.sendWelcomeEmail(signupDTO.getEmail(), signupDTO.getCompanyName());
		logger.info("[EMAIL] Welcome email sent to: {}", signupDTO.getEmail());

		return signupDTO;
	}
	// Verifies the OTP for signup, saves the new user if valid, and sends a welcome
	// email.

	private void validateUserExistence(SignupDTO signupDTO) {
		if (signupRepository.findByEmail(signupDTO.getEmail()).isPresent()) {
			logger.warn("[VALIDATION] Email already registered: {}", signupDTO.getEmail());
			throw new UserAlreadyExistsException("This email is already registered. Please try another one.");
		}

		if (signupRepository.findByMobileNumber(signupDTO.getMobileNumber()).isPresent()) {
			logger.warn("[VALIDATION] Mobile number already registered: {}", signupDTO.getMobileNumber());
			throw new UserAlreadyExistsException("This mobile number is already in use. Please try another one.");
		}

		if (signupRepository.findByCompanyName(signupDTO.getCompanyName()).isPresent()) {
			logger.warn("[VALIDATION] Company name already registered: {}", signupDTO.getCompanyName());
			throw new UserAlreadyExistsException("This company name is already registered. Please try another one.");
		}

		logger.info("[VALIDATION] No duplicate user found. Proceeding to OTP generation.");
	}
	// Checks if email, mobile number, or company name already exist in the system
	// before signup.

	private boolean isOtpExpired(LocalDateTime timestamp) {
		return timestamp.plusMinutes(OTP_VALIDITY_MINUTES).isBefore(LocalDateTime.now());
	}
	// Determines if the OTP timestamp exceeds the allowed validity period.

	private String generateOtp() {
		return String.format("%0" + OTP_LENGTH + "d", new Random().nextInt((int) Math.pow(10, OTP_LENGTH)));
	}
	// Generates a zero-padded numeric OTP string of specified length.

	private void saveSignup(SignupDTO signupDTO) {
		Signup user = new Signup();
		user.setEmail(signupDTO.getEmail());
		user.setCompanyName(signupDTO.getCompanyName());
		user.setMobileNumber(signupDTO.getMobileNumber());
		user.setLocation(signupDTO.getLocation());
		user.setRole(signupDTO.getRole());
		user.setPassword(signupDTO.getPassword()); // Note: Hash passwords in production

		signupRepository.save(user);

		logger.info("[DB SAVE] User saved | Company: {} | Email: {} | Mobile: {} | Role: {} | Location: {}",
				user.getCompanyName(), user.getEmail(), user.getMobileNumber(), user.getRole(), user.getLocation());
	}
	// Converts SignupDTO to entity and persists the new user to the database,
	// logging the operation.

}
