package tendering.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;

import tendering.config.TwilioConfig;
import tendering.dto.EmailLoginDTO;
import tendering.dto.MobileLoginDTO;
import tendering.dto.MobileOtpVerifyDTO;
import tendering.dto.ProfileRequestDTO;
import tendering.dto.SignupDTO;
import tendering.dto.MobileAuthDTO;
import tendering.model.Signup;
import tendering.repository.SignupRepository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginService {

	private final SignupRepository signupRepository;
	private final TwilioConfig twilioConfig;
	private final Map<String, MobileOtpVerifyDTO> otpStore = new ConcurrentHashMap<>();
	private final int OTP_EXPIRY_MINUTES = 5;

	@Autowired
	public LoginService(SignupRepository signupRepository, TwilioConfig twilioConfig) {
		this.signupRepository = signupRepository;
		this.twilioConfig = twilioConfig;
	}

	@PostConstruct
	public void init() {
		// Twilio integration is disabled for testing. Enable in production:
		// Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}
	// Initializes Twilio service after construction; currently disabled for testing
	// purposes.

	public Optional<String> loginWithEmail(EmailLoginDTO loginDTO) {
		return signupRepository.findByEmail(loginDTO.getEmail()).map(user -> {
			if (user.getPassword().equals(loginDTO.getPassword())) {
				return "Login successful. Welcome " + user.getRole() + "! Company: " + user.getCompanyName();
			} else {
				return "Incorrect password.";
			}
		}).or(() -> Optional.of("No account found with this email."));
	}
	// Authenticates a user by email and password, returning a welcome message or an
	// error if authentication fails.

	public Optional<String> loginWithMobile(MobileAuthDTO authDTO) {
		return signupRepository.findByMobileNumber(authDTO.getMobileNumber()).map(user -> {
			if (user.getPassword().equals(authDTO.getPassword())) {
				return "Login successful. Welcome " + user.getRole() + "! Company: " + user.getCompanyName();
			} else {
				return "Incorrect password.";
			}
		}).or(() -> Optional.of("No account found with this mobile number."));
	}
	// Authenticates a user by mobile number and password, returning a welcome
	// message or an error if authentication fails.

	public Optional<String> sendOtpToMobile(MobileLoginDTO mobileDTO) {
		return signupRepository.findByMobileNumber(mobileDTO.getMobileNumber()).map(user -> {
			String otp = generateOtp();
			otpStore.put(mobileDTO.getMobileNumber(),
					new MobileOtpVerifyDTO(mobileDTO.getMobileNumber(), otp, LocalDateTime.now()));
			System.out.println("[DEV] OTP for " + mobileDTO.getMobileNumber() + ": " + otp);
			return "OTP sent to server. Verify to continue.";
		});
	}
	// Generates and stores a one-time password (OTP) for a mobile number,
	// simulating OTP sending for verification.

	public Optional<String> verifyOtpAndLogin(String mobileNumber, String otp) {
		MobileOtpVerifyDTO stored = otpStore.get(mobileNumber);

		if (stored == null)
			return Optional.of("No OTP request found.");
		if (ChronoUnit.MINUTES.between(stored.getTimestamp(), LocalDateTime.now()) > OTP_EXPIRY_MINUTES) {
			otpStore.remove(mobileNumber);
			return Optional.of("OTP expired.");
		}

		if (!stored.getOtp().equals(otp)) {
			return Optional.of("Invalid OTP.");
		}

		otpStore.remove(mobileNumber);
		return signupRepository.findByMobileNumber(mobileNumber)
				.map(user -> "OTP verified. Welcome " + user.getRole() + "! Company: " + user.getCompanyName())
				.or(() -> Optional.of("User not found after OTP verification."));
	}
	// Validates the provided OTP against stored data, checks expiry, and logs the
	// user in if verification succeeds.

	private String generateOtp() {
		return String.format("%04d", new Random().nextInt(10000));
	}
	// Generates a random 4-digit numeric OTP as a zero-padded string.

	private String formatToE164(String mobileNumber) {
		if (mobileNumber == null || !mobileNumber.matches("^[6-9]\\d{9}$")) {
			throw new IllegalArgumentException("Invalid Indian mobile number format.");
		}
		return "+91" + mobileNumber;
	}
	// Validates and formats an Indian mobile number to the E.164 international
	// format.

	public Optional<SignupDTO> getUserProfile(ProfileRequestDTO request) {
		Optional<Signup> userOptional = Optional.empty();

		if ((request.getEmail() == null || request.getEmail().trim().isEmpty())
				&& (request.getMobileNumber() == null || request.getMobileNumber().trim().isEmpty())) {
			throw new IllegalArgumentException("Either email or mobile number must be provided.");
		}

		if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
			userOptional = signupRepository.findByEmail(request.getEmail().trim());
		} else if (request.getMobileNumber() != null && !request.getMobileNumber().trim().isEmpty()) {
			userOptional = signupRepository.findByMobileNumber(request.getMobileNumber().trim());
		}

		return userOptional.map(user -> {
			SignupDTO dto = new SignupDTO();
			dto.setEmail(user.getEmail());
			dto.setMobileNumber(user.getMobileNumber());
			dto.setCompanyName(user.getCompanyName());
			dto.setLocation(user.getLocation());
			dto.setPassword(user.getPassword());
			dto.setRole(user.getRole());
			return dto;
		});
	}
	// Retrieves user profile details based on email or mobile number, returning a
	// DTO with user information.

}
