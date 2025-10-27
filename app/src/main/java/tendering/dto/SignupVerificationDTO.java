package tendering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class SignupVerificationDTO {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Please provide a valid 10-digit Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "OTP must be a 4-digit number")
    private String otp;

    private LocalDateTime timestamp;

    public SignupVerificationDTO() {}

    public SignupVerificationDTO(String mobileNumber, String otp, LocalDateTime timestamp) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "SignupVerificationDTO{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", otp='" + otp + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}