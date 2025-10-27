package tendering.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tendering.dto.*;
import tendering.exception.UserAlreadyExistsException;
import tendering.service.AuthService;
import tendering.service.LoginService;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final LoginService loginService;

    public AuthController(AuthService authService, LoginService loginService) {
        this.authService = authService;
        this.loginService = loginService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> showStatus() {
        log.info("[ENTRY] /ping - Server health check initiated");
        String status = "ELogisticsTendering is up and running.";
        log.info("[EXIT] /ping - Status: {}", status);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> initiateSignup(@Valid @RequestBody SignupDTO signupDTO) {
        String companyName = signupDTO.getCompanyName();
        log.info("[ENTRY] /signup - Signup request for company: {}", companyName);

        try {
            String message = authService.initiateSignup(signupDTO);
            log.info("[EXIT] /signup - OTP sent successfully to company: {}", companyName);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (UserAlreadyExistsException e) {
            log.warn("[EXIT] /signup - User already exists for company: {} | Reason: {}", companyName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("[EXIT] /signup - Validation failed for company: {} | Reason: {}", companyName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("[EXIT] /signup - Exception occurred for company: {}", companyName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during signup. Please try again.");
        }
    }

    @PostMapping("/verify-signup-otp")
    public ResponseEntity<String> verifySignupOtp(@Valid @RequestBody SignupVerificationDTO otpDTO) {
        String mobile = otpDTO.getMobileNumber();
        log.info("[ENTRY] /verify-signup-otp - OTP verification for mobile: {}", mobile);

        try {
            SignupDTO user = authService.verifySignupOtp(mobile, otpDTO.getOtp());
            log.info("[EXIT] /verify-signup-otp - OTP verified for company: {}", user.getCompanyName());
            return ResponseEntity.ok("Account created successfully for company: " + user.getCompanyName());
        } catch (IllegalArgumentException e) {
            log.warn("[EXIT] /verify-signup-otp - OTP verification failed for mobile: {} | Reason: {}", mobile,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("[EXIT] /verify-signup-otp - Exception occurred for mobile: {}", mobile, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during OTP verification. Please try again.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail();
        log.info("[ENTRY] /login - Login attempt for email: {}", email);

        try {
            Optional<String> result = loginService.loginWithEmail(emailLoginDTO);
            String message = result.orElse("No account found with this email.");

            if ("Incorrect password.".equals(message)) {
                log.warn("[EXIT] /login - Incorrect password for email: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
            } else if ("No account found with this email.".equals(message)) {
                log.warn("[EXIT] /login - No user found for email: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }

            log.info("[EXIT] /login - Login successful for email: {}", email);
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            log.error("[EXIT] /login - Exception during login for email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login. Please try again later.");
        }
    }
    
    @PostMapping("/login-with-password") 
    public ResponseEntity<String> loginWithMobile(@Valid @RequestBody MobileAuthDTO mobileAuthDTO) {
        String mobileNumber = mobileAuthDTO.getMobileNumber();
        log.info("[ENTRY] /login-with-password - Login attempt for mobile number: {}", mobileNumber);

        try {
            Optional<String> result = loginService.loginWithMobile(mobileAuthDTO);
            String message = result.orElse("No account found with this mobile number.");

            if ("Incorrect password.".equals(message)) {
                log.warn("[EXIT] /login-with-password - Incorrect password for mobile number: {}", mobileNumber);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
            } else if ("No account found with this mobile number.".equals(message)) {
                log.warn("[EXIT] /login-with-password - No user found for mobile number: {}", mobileNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }

            log.info("[EXIT] /login-with-password - Login successful for mobile number: {}", mobileNumber);
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            log.error("[EXIT] /login-with-password - Exception during login for mobile number: {}", mobileNumber, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login. Please try again later.");
        }
    }

    

    @PostMapping("/login-with-mobile")
    public ResponseEntity<String> loginWithMobile(@Valid @RequestBody MobileLoginDTO mobileLoginDTO) {
        String mobile = mobileLoginDTO.getMobileNumber();
        log.info("[ENTRY] /login-with-mobile - OTP login request for mobile: {}", mobile);

        try {
            Optional<String> otpMessage = loginService.sendOtpToMobile(mobileLoginDTO);

            if (otpMessage.isEmpty()) {
                String message = "No account found with this mobile number.";
                log.warn("[EXIT] /login-with-mobile - Mobile not registered: {}", mobile);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }

            log.info("[EXIT] /login-with-mobile - OTP sent to mobile: {}", mobile);
            return ResponseEntity.ok(otpMessage.get());
        } catch (Exception e) {
            log.error("[EXIT] /login-with-mobile - OTP sending failed for mobile: {}", mobile, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while sending OTP. Please try again.");
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody MobileOtpVerifyDTO otpDTO) {
        String mobile = otpDTO.getMobileNumber();
        log.info("[ENTRY] /verify-otp - OTP verification attempt for mobile: {}", mobile);

        try {
            Optional<String> loginMessage = loginService.verifyOtpAndLogin(mobile, otpDTO.getOtp());
            String message = loginMessage.orElse("OTP verification failed.");

            if ("OTP verification failed.".equals(message)) {
                log.warn("[EXIT] /verify-otp - OTP verification failed for mobile: {}", mobile);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }

            log.info("[EXIT] /verify-otp - OTP verified and login successful for mobile: {}", mobile);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("[EXIT] /verify-otp - Exception during OTP verification for mobile: {}", mobile, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while verifying OTP. Please try again later.");
        }
    }
}