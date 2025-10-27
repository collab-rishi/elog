package tendering.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetDTO {

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@Size(max = 255, message = "Email should not exceed 255 characters")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
	private String email;

	@NotBlank(message = "New password is required")
	@Size(min = 12, max = 255, message = "Password must be between 12 and 255 characters")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{12,}$", 
			message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	private String newPassword;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}