package tendering.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class VerifyResetOtpDTO {

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@Size(max = 255, message = "Email should not exceed 255 characters")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
	private String email;

	@Pattern(regexp = "^[0-9]{4}$", message = "OTP must be a 4-digit number")
	private String otp;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
}