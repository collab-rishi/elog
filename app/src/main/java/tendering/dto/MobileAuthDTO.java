package tendering.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MobileAuthDTO {

    @NotBlank(message = "Mobile number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be exactly 10 digits.")
    private String mobileNumber;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters.")
    private String password;

    // Constructors
    public MobileAuthDTO() {
    }

    public MobileAuthDTO(String mobileNumber, String password) {
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    // Getters and Setters
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
