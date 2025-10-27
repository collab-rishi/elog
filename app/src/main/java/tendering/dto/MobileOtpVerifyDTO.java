package tendering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class MobileOtpVerifyDTO {
	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Please provide a valid 10-digit Indian mobile number")
	private String mobileNumber;

	@NotBlank(message = "OTP is required")
	@Pattern(regexp = "^[0-9]{4}$", message = "OTP must be a 4-digit number")
	private String otp;

	private LocalDateTime timestamp;
	private boolean verified;

	public MobileOtpVerifyDTO() {
	}

	public MobileOtpVerifyDTO(String mobileNumber, String otp, LocalDateTime timestamp) {
		this.mobileNumber = mobileNumber;
		this.otp = otp;
		this.timestamp = timestamp;
		this.verified = false;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
