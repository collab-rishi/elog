package tendering.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tendering.model.UserRole;

public class SignupDTO {

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@Size(max = 255, message = "Email should not exceed 255 characters")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
	private String email;

	@NotBlank(message = "Company name is required")
	@Size(min = 2, max = 255, message = "Company name must be between 2 and 255 characters")
	@Pattern(regexp = "^[A-Za-z0-9\\s\\-&.,()]+$", message = "Company name can only contain letters, numbers, spaces, and special characters (-&.,())")
	private String companyName;

	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Please provide a valid 10-digit Indian mobile number")
	private String mobileNumber;

	@Size(max = 255, message = "Location should not exceed 255 characters")
	@Pattern(regexp = "^[A-Za-z\\s,.-]+$", message = "Location can only contain letters, spaces, commas, dots, and hyphens")
	private String location;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	private String password;

	@NotNull(message = "Role is required")
	private UserRole role;

	public SignupDTO() {
	}

	public SignupDTO(String email, String companyName, String mobileNumber, String location, String password,
			UserRole role) {
		this.email = email;
		this.companyName = companyName;
		this.mobileNumber = mobileNumber;
		this.location = location;
		this.password = password;
		this.role = role;
	}

	// Getters and setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

}
